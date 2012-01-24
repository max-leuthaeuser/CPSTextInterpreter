/*
 * Copyright 2010 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Modified version for CPSTextInterpreter.
 *
 * @author Max Leuthaeuser
 * @date 07.01.2012
 *
 */

package interpreter

import java.io._
import java.math.BigInteger
import java.net.URLClassLoader
import java.security.MessageDigest
import java.util.Random
import java.util.jar.JarFile
import scala.collection.mutable
import scala.io.Source
import scala.tools.nsc.{Global, Settings}
import scala.tools.nsc.interpreter.AbstractFileClassLoader
import scala.tools.nsc.io.{AbstractFile, VirtualDirectory}
import scala.tools.nsc.reporters.AbstractReporter
import scala.tools.nsc.util.{BatchSourceFile, Position}
import scala.annotation.tailrec
import java.io.{InputStream, OutputStream, ByteArrayOutputStream}
import scala.util.matching.Regex

object string {

  final class RichString(wrapped: String) {
    /**
     * For every section of a string that matches a regular expression, call
     * a function to determine a replacement (as in python's
     * `re.sub`). The function will be passed the Matcher object
     * corresponding to the substring that matches the pattern, and that
     * substring will be replaced by the function's result.
     *
     * For example, this call:
     *
     *     "ohio".regexSub("""h.""".r) { m => "n" }
     *
     * will return the string `"ono"`.
     *
     * The matches are found using `Matcher.find()` and so
     * will obey all the normal java rules (the matches will not overlap,
     * etc).
     *
     * @param re the regex pattern to replace
     * @param replace a function that takes Regex.MatchData objects and
     *     returns a string to substitute
     * @return the resulting string with replacements made
     */
    def regexSub(re: Regex)(replace: (Regex.MatchData => String)): String = {
      var offset = 0
      val out = new StringBuilder()

      for (m <- re.findAllIn(wrapped).matchData) {
        if (m.start > offset) {
          out.append(wrapped.substring(offset, m.start))
        }

        out.append(replace(m))
        offset = m.end
      }

      if (offset < wrapped.length) {
        out.append(wrapped.substring(offset))
      }
      out.toString
    }

    private val QUOTE_RE = "[\u0000-\u001f\u007f-\uffff\\\\\"]".r

    /**
     * Quote a string so that unprintable chars (in ASCII) are represented by
     * C-style backslash expressions. For example, a raw linefeed will be
     * translated into <code>"\n"</code>. Control codes (anything below 0x20)
     * and unprintables (anything above 0x7E) are turned into either
     * <code>"\xHH"</code> or <code>"\\uHHHH"</code> expressions, depending on
     * their range. Embedded backslashes and double-quotes are also quoted.
     *
     * @return a quoted string, suitable for ASCII display
     */
    def quoteC(): String = {
      regexSub(QUOTE_RE) {
        m =>
          m.matched.charAt(0) match {
            case '\r' => "\\r"
            case '\n' => "\\n"
            case '\t' => "\\t"
            case '"' => "\\\""
            case '\\' => "\\\\"
            case c =>
              if (c <= 255) {
                "\\x%02x".format(c.asInstanceOf[Int])
              } else {
                "\\u%04x" format c.asInstanceOf[Int]
              }
          }
      }
    }

    // we intentionally don't unquote "\$" here, so it can be used to escape interpolation later.
    private val UNQUOTE_RE = """\\(u[\dA-Fa-f]{4}|x[\dA-Fa-f]{2}|[/rnt\"\\])""".r

    /**
     * Unquote an ASCII string that has been quoted in a style like
     * {@link #quoteC} and convert it into a standard unicode string.
     * <code>"\\uHHHH"</code> and <code>"\xHH"</code> expressions are unpacked
     * into unicode characters, as well as <code>"\r"</code>, <code>"\n"<code>,
     * <code>"\t"</code>, <code>"\\"<code>, and <code>'\"'</code>.
     *
     * @return an unquoted unicode string
     */
    def unquoteC() = {
      regexSub(UNQUOTE_RE) {
        m =>
          val ch = m.group(1).charAt(0) match {
            // holy crap! this is terrible:
            case 'u' => Character.valueOf(Integer.valueOf(m.group(1).substring(1), 16).asInstanceOf[Int].toChar)
            case 'x' => Character.valueOf(Integer.valueOf(m.group(1).substring(1), 16).asInstanceOf[Int].toChar)
            case 'r' => '\r'
            case 'n' => '\n'
            case 't' => '\t'
            case x => x
          }
          ch.toString
      }
    }

    /**
     * Turn a string of hex digits into a byte array. This does the exact
     * opposite of `Array[Byte]#hexlify`.
     */
    def unhexlify(): Array[Byte] = {
      val buffer = new Array[Byte]((wrapped.length + 1) / 2)
      (wrapped.grouped(2).toSeq zipWithIndex) foreach {
        case (substr, i) =>
          buffer(i) = Integer.parseInt(substr, 16).toByte
      }
      buffer
    }
  }

  final class RichByteArray(wrapped: Array[Byte]) {
    /**
     * Turn an Array[Byte] into a string of hex digits.
     */
    def hexlify(): String = {
      val out = new StringBuffer
      for (b <- wrapped) {
        val s = (b.toInt & 0xff).toHexString
        if (s.length < 2) {
          out append '0'
        }
        out append s
      }
      out.toString
    }
  }

  implicit def stringToConfiggyString(s: String) = new RichString(s)

  implicit def byteArrayToConfiggyByteArray(b: Array[Byte]) = new RichByteArray(b)
}

object StreamIO {
  /**
   * Copy an InputStream to an OutputStream in chunks of the given
   * buffer size (default = 1KB).
   */
  @tailrec
  final def copy(
                  inputStream: InputStream,
                  outputStream: OutputStream,
                  bufferSize: Int = 1024
                  ) {
    val buf = new Array[Byte](bufferSize)
    inputStream.read(buf, 0, buf.size) match {
      case -1 => ()
      case n =>
        outputStream.write(buf, 0, n)
        copy(inputStream, outputStream, bufferSize)
    }
  }

  /**
   * Buffer (fully) the given input stream by creating & copying it to
   * a ByteArrayOutputStream.
   */
  def buffer(inputStream: InputStream): ByteArrayOutputStream = {
    val bos = new java.io.ByteArrayOutputStream
    copy(inputStream, bos)
    bos
  }
}

/**
 * Evaluate a file or string and return the result.
 */
@deprecated("use a throw-away instance of Eval instead")
object Eval extends Eval {
  private val jvmId = java.lang.Math.abs(new Random().nextInt())
  val classCleaner: Regex = "\\W".r
}

/**
 * evaluates files, strings or input streams, and returns the result.
 * In all cases, code to be evaled is wrapped in an apply method in a
 * generated class. An instance of the class is instantiated, and the
 * result of apply is returned.
 *
 * If target is None, the results are compiled to memory (and are therefore
 * ephemeral). If target is Some(path), path must point to a directory, and
 * eval emits class files to that directory.
 *
 * eval also supports a limited set of preprocessors. Limited means
 * exactly one, that supports directives of the form #include <file>.
 *
 * The general flow of evaluation is
 * # convert arguments to a string
 * # run preprocessors on that string
 * # wrap processed code in a class
 * # compile the class
 * # create an instance of that class
 * # return the results of apply()
 */
class Eval(target: Option[File]) {
  /**
   * empty constructor for backwards compatibility
   */
  def this() {
    this (None)
  }

  import Eval.jvmId

  private lazy val compilerPath = try {
    jarPathOfClass("scala.tools.nsc.Interpreter")
  } catch {
    case e =>
      throw new RuntimeException("Unable lo load scala interpreter from classpath (scala-compiler jar is missing?)", e)
  }

  private lazy val libPath = try {
    jarPathOfClass("scala.ScalaObject")
  } catch {
    case e =>
      throw new RuntimeException("Unable to load scala base object from classpath (scala-library jar is missing?)", e)
  }

  /**
   * Preprocessors to run the code through before it is passed to the Scala compiler.
   * if you want to add new resolvers, you can do so with
   * new Eval(...) {
   *   lazy val preprocessors = {...}
   * }
   */
  protected lazy val preprocessors: Seq[Preprocessor] =
    Seq(
      new IncludePreprocessor(
        Seq(
          new ClassScopedResolver(getClass),
          new FilesystemResolver(new File(".")),
          new FilesystemResolver(new File("." + File.separator + "config"))
        ) ++ (
          Option(System.getProperty("com.twitter.util.Eval.includePath")) map {
            path =>
              new FilesystemResolver(new File(path))
          }
          )
      )
    )

  private lazy val compiler = new StringCompiler(2, target)

  /**
   * run preprocessors on our string, returning a String that is the processed source
   */
  def sourceForString(code: String): String = {
    preprocessors.foldLeft(code) {
      (acc, p) =>
        p(acc)
    }
  }

  /**
   * write the current checksum to a file
   */
  def writeChecksum(checksum: String, file: File) {
    val writer = new FileWriter(file)
    writer.write("%s".format(checksum))
    writer.close
  }

  /**
   * val i: Int = new Eval()("1 + 1") // => 2
   */
  def apply[T](code: String, resetState: Boolean = true): T = {
    val processed = sourceForString(code)
    applyProcessed(processed, resetState)
  }

  /**
   * val i: Int = new Eval()(new File("..."))
   */
  def apply[T](files: File*): T = {
    if (target.isDefined) {
      val targetDir = target.get
      val unprocessedSource = files.map {
        scala.io.Source.fromFile(_).mkString
      }.mkString("\n")
      val processed = sourceForString(unprocessedSource)
      val sourceChecksum = uniqueId(processed, None)
      val checksumFile = new File(targetDir, "checksum")
      val lastChecksum = if (checksumFile.exists) {
        Source.fromFile(checksumFile).getLines.take(1).toList.head
      } else {
        -1
      }

      if (lastChecksum != sourceChecksum) {
        compiler.reset()
        writeChecksum(sourceChecksum, checksumFile)
      }

      // why all this nonsense? Well.
      // 1) We want to know which file the eval'd code came from
      // 2) But sometimes files have characters that aren't valid in Java/Scala identifiers
      // 3) And sometimes files with the same name live in different subdirectories
      // so, clean it hash it and slap it on the end of Evaluator
      val cleanBaseName = fileToClassName(files(0))
      val className = "Evaluator__%s_%s".format(
        cleanBaseName, sourceChecksum)
      applyProcessed(className, processed, false)
    } else {
      apply(files.map {
        scala.io.Source.fromFile(_).mkString
      }.mkString("\n"), true)
    }
  }

  /**
   * val i: Int = new Eval()(getClass.getResourceAsStream("..."))
   */
  def apply[T](stream: InputStream): T = {
    apply(sourceForString(Source.fromInputStream(stream).mkString))
  }

  /**
   * same as apply[T], but does not run preprocessors.
   * Will generate a classname of the form Evaluater__<unique>,
   * where unique is computed from the jvmID (a random number)
   * and a digest of code
   */
  def applyProcessed[T](code: String, resetState: Boolean): T = {
    val id = uniqueId(code)
    val className = "Evaluator__" + id
    applyProcessed(className, code, resetState)
  }

  /**
   * same as apply[T], but does not run preprocessors.
   */
  def applyProcessed[T](className: String, code: String, resetState: Boolean): T = {
    val cls = compiler(wrapCodeInClass(className, code), className, resetState)
    cls.getConstructor().newInstance().asInstanceOf[() => Any].apply().asInstanceOf[T]
  }

  /**
   * converts the given file to evaluable source.
   * delegates to toSource(code: String)
   */
  def toSource(file: File): String = {
    toSource(scala.io.Source.fromFile(file).mkString)
  }

  /**
   * converts the given file to evaluable source.
   */
  def toSource(code: String): String = {
    sourceForString(code)
  }

  /**
   * Compile an entire source file into the virtual classloader.
   */
  def compile(code: String) {
    compiler(sourceForString(code))
  }

  /**
   * Like `Eval()`, but doesn't reset the virtual classloader before evaluating. So if you've
   * loaded classes with `compile`, they can be referenced/imported in code run by `inPlace`.
   */
  def inPlace[T](code: String) = {
    apply[T](code, false)
  }

  /**
   * Check if code is Eval-able.
   * @throw CompilerException if not Eval-able.
   */
  def check(code: String) {
    val id = uniqueId(sourceForString(code))
    val className = "Evaluator__" + id
    val wrappedCode = wrapCodeInClass(className, code)
    compile(wrappedCode) // may throw CompilerException
  }

  /**
   * Check if files are Eval-able.
   * @throw CompilerException if not Eval-able.
   */
  def check(files: File*) {
    val code = files.map {
      scala.io.Source.fromFile(_).mkString
    }.mkString("\n")
    check(code)
  }

  /**
   * Check if stream is Eval-able.
   * @throw CompilerException if not Eval-able.
   */
  def check(stream: InputStream) {
    check(scala.io.Source.fromInputStream(stream).mkString)
  }

  def findClass(className: String): Class[_] = {
    compiler.findClass(className).getOrElse {
      throw new ClassNotFoundException("no such class: " + className)
    }
  }

  private def uniqueId(code: String, idOpt: Option[Int] = Some(jvmId)): String = {
    val digest = MessageDigest.getInstance("SHA-1").digest(code.getBytes())
    val sha = new BigInteger(1, digest).toString(16)
    idOpt match {
      case Some(id) => sha + "_" + jvmId
      case _ => sha
    }
  }

  private def fileToClassName(f: File): String = {
    import string._

    // HOPE YOU'RE HAPPY GUYS!!!!
    val fileName = f.getName
    val baseName = fileName.lastIndexOf('.') match {
      case -1 => fileName
      case dot => fileName.substring(0, dot)
    }
    baseName.regexSub(Eval.classCleaner) {
      m =>
        "$%02x".format(m.group(0).charAt(0).toInt)
    }
  }

  /*
   * Wrap source code in a new class with an apply method.
   */
  private def wrapCodeInClass(className: String, code: String) = {
    "class " + className + " extends (() => Any) {\n" +
      "  def apply() = {\n" +
      code + "\n" +
      "  }\n" +
      "}\n"
  }

  /*
   * For a given FQ classname, trick the resource finder into telling us the containing jar.
   */
  private def jarPathOfClass(className: String) = try {
    val resource = className.split('.').mkString("/", "/", ".class")
    val path = getClass.getResource(resource).getPath
    val indexOfFile = path.indexOf("file:") + 5
    val indexOfSeparator = path.lastIndexOf('!')
    List(path.substring(indexOfFile, indexOfSeparator))
  }

  /*
   * Try to guess our app's classpath.
   * This is probably fragile.
   */
  lazy val impliedClassPath: List[String] = {
    val currentClassPath = this.getClass.getClassLoader.asInstanceOf[URLClassLoader].getURLs.
      map(_.toString).filter(_.startsWith("file:")).map(_.substring(5)).toList

    // if there's just one thing in the classpath, and it's a jar, assume an executable jar.
    currentClassPath ::: (if (currentClassPath.size == 1 && currentClassPath(0).endsWith(".jar")) {
      val jarFile = currentClassPath(0)
      val relativeRoot = new File(jarFile).getParentFile()
      val nestedClassPath = new JarFile(jarFile).getManifest.getMainAttributes.getValue("Class-Path")
      if (nestedClassPath eq null) {
        Nil
      } else {
        nestedClassPath.split(" ").map {
          f => new File(relativeRoot, f).getAbsolutePath
        }.toList
      }
    } else {
      Nil
    })
  }

  trait Preprocessor {
    def apply(code: String): String
  }

  trait Resolver {
    def resolvable(path: String): Boolean

    def get(path: String): InputStream
  }

  class FilesystemResolver(root: File) extends Resolver {
    private[this] def file(path: String): File =
      new File(root.getAbsolutePath + File.separator + path)

    def resolvable(path: String): Boolean =
      file(path).exists

    def get(path: String): InputStream =
      new FileInputStream(file(path))
  }

  class ClassScopedResolver(clazz: Class[_]) extends Resolver {
    private[this] def quotePath(path: String) =
      "/" + path

    def resolvable(path: String): Boolean =
      clazz.getResourceAsStream(quotePath(path)) != null

    def get(path: String): InputStream =
      clazz.getResourceAsStream(quotePath(path))
  }

  class ResolutionFailedException(message: String) extends Exception

  /*
   * This is a preprocesor that can include files by requesting them from the given classloader
   *
   * Thusly, if you put FS directories on your classpath (e.g. config/ under your app root,) you
   * mix in configuration from the filesystem.
   *
   * @example #include file-name.scala
   *
   * This is the only directive supported by this preprocessor.
   *
   * Note that it is *not* recursive. Included files cannot have includes
   */
  class IncludePreprocessor(resolvers: Seq[Resolver]) extends Preprocessor {
    def maximumRecursionDepth = 100

    def apply(code: String): String =
      apply(code, maximumRecursionDepth)

    def apply(code: String, maxDepth: Int): String = {
      val lines = code.lines map {
        line: String =>
          val tokens = line.trim.split(' ')
          if (tokens.length == 2 && tokens(0).equals("#include")) {
            val path = tokens(1)
            resolvers find {
              resolver: Resolver =>
                resolver.resolvable(path)
            } match {
              case Some(r: Resolver) => {
                // recursively process includes
                if (maxDepth == 0) {
                  throw new IllegalStateException("Exceeded maximum recusion depth")
                } else {
                  apply(StreamIO.buffer(r.get(path)).toString, maxDepth - 1)
                }
              }
              case _ =>
                throw new IllegalStateException("No resolver could find '%s'".format(path))
            }
          } else {
            line
          }
      }
      lines.mkString("\n")
    }
  }

  /**
   * Dynamic scala compiler. Lots of (slow) state is created, so it may be advantageous to keep
   * around one of these and reuse it.
   */
  private class StringCompiler(lineOffset: Int, targetDir: Option[File]) {
    val target = targetDir match {
      case Some(dir) => AbstractFile.getDirectory(dir)
      case None => new VirtualDirectory("(memory)", None)
    }

    val cache = new mutable.HashMap[String, Class[_]]()

    val settings = new Settings
    settings.deprecation.value = true // enable detailed deprecation warnings
    settings.unchecked.value = true // enable detailed unchecked warnings
    settings.outputDirs.setSingleOutput(target)

    val pathList = compilerPath ::: libPath
    settings.bootclasspath.value = pathList.mkString(File.pathSeparator)
    settings.classpath.value = (pathList ::: impliedClassPath).mkString(File.pathSeparator)

    val reporter = new AbstractReporter {
      val settings = StringCompiler.this.settings
      val messages = new mutable.ListBuffer[List[String]]

      def display(pos: Position, message: String, severity: Severity) {
        severity.count += 1
        val severityName = severity match {
          case ERROR => "error: "
          case WARNING => "warning: "
          case _ => ""
        }
        messages += (severityName + "line " + (pos.line - lineOffset) + ": " + message) ::
          (if (pos.isDefined) {
            pos.inUltimateSource(pos.source).lineContent.stripLineEnd ::
              (" " * (pos.column - 1) + "^") ::
              Nil
          } else {
            Nil
          })
      }

      def displayPrompt {
        // no.
      }

      override def reset {
        super.reset
        messages.clear()
      }
    }

    val global = new Global(settings, reporter)

    /*
     * Class loader for finding classes compiled by this StringCompiler.
     * After each reset, this class loader will not be able to find old compiled classes.
     */
    var classLoader = new AbstractFileClassLoader(target, this.getClass.getClassLoader)

    def reset() {
      targetDir match {
        case None => {
          target.asInstanceOf[VirtualDirectory].clear
        }
        case Some(t) => {
          target.foreach {
            abstractFile =>
              if (abstractFile.file == null || abstractFile.file.getName.endsWith(".class")) {
                abstractFile.delete
              }
          }
        }
      }
      cache.clear()
      reporter.reset
      classLoader = new AbstractFileClassLoader(target, this.getClass.getClassLoader)
    }

    object Debug {
      val enabled =
        System.getProperty("eval.debug") != null

      def printWithLineNumbers(code: String) {
        printf("Code follows (%d bytes)\n", code.length)

        var numLines = 0
        code.lines foreach {
          line: String =>
            numLines += 1
            println(numLines.toString.padTo(5, ' ') + "| " + line)
        }
      }
    }

    def findClass(className: String): Option[Class[_]] = {
      synchronized {
        cache.get(className).orElse {
          try {
            val cls = classLoader.loadClass(className)
            cache(className) = cls
            Some(cls)
          } catch {
            case e: ClassNotFoundException => None
          }
        }
      }
    }

    /**
     * Compile scala code. It can be found using the above class loader.
     */
    def apply(code: String) {
      if (Debug.enabled)
        Debug.printWithLineNumbers(code)

      // if you're looking for the performance hit, it's 1/2 this line...
      val compiler = new global.Run
      val sourceFiles = List(new BatchSourceFile("(inline)", code))
      // ...and 1/2 this line:
      compiler.compileSources(sourceFiles)

      if (reporter.hasErrors || reporter.WARNING.count > 0) {
        throw new CompilerException(reporter.messages.toList)
      }
    }

    /**
     * Compile a new class, load it, and return it. Thread-safe.
     */
    def apply(code: String, className: String, resetState: Boolean = true): Class[_] = {
      synchronized {
        if (resetState) reset()
        findClass(className).getOrElse {
          apply(code)
          findClass(className).get
        }
      }
    }
  }

  class CompilerException(val messages: List[List[String]]) extends Exception(
    "Compiler exception " + messages.map(_.mkString("\n")).mkString("\n"))

}