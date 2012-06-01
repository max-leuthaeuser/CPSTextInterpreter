################################################################################
                    THE CPSTextInterpreter REPOSITORY
################################################################################

This document describes the CPSTextInterpreter repository and how to build and
use it. For information about CPSText (previously NaoText) as a language itself, you can read
the publications (coming soon).

Part I. The repository layout
--------------------------------------------------------------------------------                            

Follows the file layout of the repository. This is not a complete listing. :: 

  scala/
   +--builds/                   The destination folder for distributions build with maven.
   +--pom.xml                 	The maven build configuration.
   +--examples/                 Some examples of CPSText programs.
   +--lib/                      Pre-compiled libraries for the build.
   |   +--jnaoqi.jar            The naoqi library, needed by the Java abstraction layer for RPC calls to the Nao robot.
   +--model/			        Contains models for CPSText.
   +--README	                The file you are currently reading.
   +--src/                      All the source files of CPSTextInterpreter.
   |   +--main/               	The sources of CPSTextInterpreter itself.
   |   ---test/             	The sources of tests.



Part II. Building CPSTextInterpreter with Maven
--------------------------------------------------------------------------------

Maven is used to manage the dependencies and it will build a runnable jar executable with Java.

- install Maven (http://maven.apache.org/)
- run ``mvn package``, the resulting jar is placed in ``builds/``

^^^^^^^^^^^^^^^^^^^^^^^^
REQUIREMENTS FOR CPSTextInterpreter:
^^^^^^^^^^^^^^^^^^^^^^^^
The following is assumed to be installed on the build machine:

- A Java runtime environment (JRE) or SDK 1.7 or above.
- Maven
- Scalatest plugin from here: http://github.com/teigen/maven-scalatest-plugin
- install the jnaoqi libs for Maven: ``mvn install:install-file -Dfile=CPSTextInterpreter/lib/jnaoqi.jar -DgroupId=com.aldebaran -DartifactId=jnaoqi -Dversion=1.12.3 -Dpackaging=jar``

The following is assumed to be installed on the machine you want to run
CPSTextInterpreter:

- A Java runtime environment (JRE) or SDK 1.7 or above.


Part IV. Contributing to CPSTextInterpreter
--------------------------------------------------------------------------------

If you wish to contribute, you can find all of the necessary information here on 
GitHub.


Part V. Publications
--------------------------------------------------------------------------------
