:: Copyright 2012, by the California Institute of Technology.
:: ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
:: Any commercial use must be negotiated with the Office of Technology Transfer
:: at the California Institute of Technology.
::
:: This software is subject to U. S. export control laws and regulations
:: (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
:: is subject to U.S. export control laws and regulations, the recipient has
:: the responsibility to obtain export licenses or other export authority as
:: may be required before exporting such information to foreign countries or
:: providing access to foreign nationals.
::
:: $Id: lddtool.bat 11061 2012-10-10 16:45:41Z shardman $

:: Batch file that allows easy execution of the LDDTool Tool
:: without the need to set the CLASSPATH or having to type in that long java
:: command (java gov.nasa.pds.lddtool ...)

:: Expects the DMDocument jar file to be located in the ../lib directory.

@echo off

:: Set the JAVA_HOME environment variable here in the script if it will
:: not be defined in the environment.

:: if not defined JAVA_HOME (
::   set JAVA_HOME=\path\to\java\home
:: )

if not defined JAVA_HOME (
  set JAVA_HOME=C:\Program Files\Java\jre7
)

:: echo %JAVA_HOME%

:: Setup environment variables.
set SCRIPT_DIR=%~dp0
set PARENT_DIR=%SCRIPT_DIR%..
set LIB_DIR=%PARENT_DIR%\lib

:: echo %LIB_DIR%

:: Check for dependencies.
if exist "%LIB_DIR%\DMDocument.jar" (
set DMDOC_JAR=%LIB_DIR%\DMDocument.jar
) else (
echo Cannot find DMDocument jar file in %LIB_DIR%
goto END
)

:: Executes the LDDTool Tool via the executable jar file
:: The special variable '%*' allows the arguments
:: to be passed into the executable.

:: echo %DMDOC_JAR%

"%JAVA_HOME%\bin\java" -jar "%DMDOC_JAR%" %*

:END
