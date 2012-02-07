################################################################################
                    THE CPSTextInterpreter REPOSITORY
################################################################################

This document describes the CPSTextInterpreter repository and how to build and
use it. For information about CPSText as a language itself, you can visit
the wiki (coming soon).

Part I. The repository layout
--------------------------------------------------------------------------------                            

Follows the file layout of the repository. This is not a complete listing. :: 

  scala/
   +--builds/                   The destination folder for distributions build with maven.
   +--pom.xml                 	The maven build configuration.
   +--examples/                 Some examples of CPSText programs.
   +--lib/                      Pre-compiled libraries for the build.
   |   +--json.jar              The json library, needed by the Java abstraction layer to NaoService.
   +--model/			        Contains models for CPSText.
   +--README	                The file you are currently reading.
   +--src/                      All the source files of CPSTextInterpreter.
   |   +--main/               	The sources of CPSTextInterpreter itself.
   |   ---test/             	The sources of tests.



Part II. Building CPSTextInterpreter with maven
--------------------------------------------------------------------------------

Maven is used to compile Scala. It is automated and takes care of
managing the dependencies and will build a runnable jar executable with Java.

- install maven (http://maven.apache.org/)
- run ``mvn package``, the resulting jar is placed in ``builds/``

^^^^^^^^^^^^^^^^^^^^^^^^
REQUIREMENTS FOR CPSTextInterpreter:
^^^^^^^^^^^^^^^^^^^^^^^^
The following is assumed to be installed on the build machine:

- A Java runtime environment (JRE) or SDK 1.6 or above.
- maven

The following is assumed to be installed on the machine you want to run
CPSTextInterpreter:

- A Java runtime environment (JRE) or SDK 1.6 or above. (``java`` needs to be in the PATH)
- Scala 2.8.0 (``fsc`` needs to be in the PATH)


Part IV. Contributing to CPSTextInterpreter
--------------------------------------------------------------------------------

If you wish to contribute, you can find all of the necessary information here on 
GitHub (see the projects wiki, coming soon)


Part V. Publications
--------------------------------------------------------------------------------
