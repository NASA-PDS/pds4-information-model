:: Copyright 2019, California Institute of Technology ("Caltech").
:: U.S. Government sponsorship acknowledged.
::
:: All rights reserved.
::
:: Redistribution and use in source and binary forms, with or without
:: modification, are permitted provided that the following conditions are met:
::
:: * Redistributions of source code must retain the above copyright notice,
:: this list of conditions and the following disclaimer.
:: * Redistributions must reproduce the above copyright notice, this list of
:: conditions and the following disclaimer in the documentation and/or other
:: materials provided with the distribution.
:: * Neither the name of Caltech nor its operating division, the Jet Propulsion
:: Laboratory, nor the names of its contributors may be used to endorse or
:: promote products derived from this software without specific prior written
:: permission.
::
:: THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
:: AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
:: IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
:: ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
:: LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
:: CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
:: SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
:: INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
:: CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
:: ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
:: POSSIBILITY OF SUCH DAMAGE.

:: Batch file that allows easy execution of the termmap Tool
:: without the need to set the CLASSPATH or having to type in that long java
:: command (java gov.nasa.pds.termmap ...)

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

:: Executes the termmap Tool via the executable jar file
:: The special variable '%*' allows the arguments
:: to be passed into the executable.

:: echo %DMDOC_JAR%

"%JAVA_HOME%\bin\java" -jar -p -map "%DMDOC_JAR%" %*

:END
