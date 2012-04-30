package de.qualitune.util

import java.util.Calendar
import java.text.SimpleDateFormat
import java.io.{IOException, File, PrintWriter, FileWriter}

object IOUtils {

  object Time {
    def apply[T](name: String)(block: => T) {
      val start = System.currentTimeMillis
      try {
        block
      } finally {
        val diff = System.currentTimeMillis - start
        println("# " + name + " completed, time taken: " + diff + " ms (" + diff / 1000.0 + " s)")
      }
    }
  }

  /**
   * Used for reading/writing to database, files, etc.
   * Code From the book "Beginning Scala"
   * http://www.amazon.com/Beginning-Scala-David-Pollak/dp/1430219890
   */
  def using[A <: {def close()}, B](param: A)(f: A => B): B =
    try {
      f(param)
    } finally {
      param.close()
    }

  def writeToFile(fileName: String, data: String) {
    using(new FileWriter(fileName)) {
      fileWriter => fileWriter.write(data)
    }
  }

  def appendToFile(fileName: String, textData: String) {
    using(new FileWriter(fileName, true)) {
      fileWriter => using(new PrintWriter(fileWriter)) {
        printWriter => printWriter.println(textData)
      }
    }
  }

  def isWindows = {
    System.getProperty("os.name").toLowerCase.indexOf("win") >= 0
  }

  def now = {
    val DATE_FORMAT_NOW = "HH:mm:ss:SSS"
    val cal = Calendar.getInstance()
    val sdf = new SimpleDateFormat(DATE_FORMAT_NOW)
    sdf.format(cal.getTime)
  }

  def createDirectory(dir: File) {
    if (dir.exists && !dir.isDirectory)
      throw new IOException(dir + " exists and is not a directory.")
    else dir.mkdirs()
  }
}
