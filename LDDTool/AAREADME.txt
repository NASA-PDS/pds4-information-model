LDDTOOL                                                                         June 2013

NAME
       lddtool - Local Data Dictionary (LDD) Tool 

ARGUMENTS
       	-p - Set the context to PDS4 - This flag is always required for PDS processing.
       	-l - Process a local data dictionary input file
       	-c - Write definitions for class elements
       	-a - Write definitions for attribute elements
       	-m - Generate a file that can be merged into the master database. 
       	<filename> - input file name; the file extension .xml is assumed.
       	
       	Example:  - lddtool -pl inputfilename > lddtool.log

DESCRIPTION

       To Install: Extract the directory LDDTool and its contents onto a local disk.
                   If JAVA_HOME is not already set on your host platform then try the following.
                      If the host platform is a Mac or Unix system, edit the line JAVA_HOME=/path/to/java/ in the lddtool Bourne Shell script. 
                      If the host platform is a Windows System, edit the line set JAVA_HOME=\path\to\java\home in the lddtool.bat file.

       To Run: If the host platform is a Mac or Unix system
                  connect to ./LDDTool/bin
                  enter lddtool -lp [inputfilename] > lddtool.log
                  
               If the host platform is a Windows System
                  connect to .\LDDTool\bin
                  enter lddtool.bat -lp [inputfilename] > lddtool.log
                  
               Note that the runapp and runapp.bat scripts in the LDDTool directory contain the calling sequence.

       Input:  The input file to LDDTool is an XML document that contains a 
               completed local data dictionary template. This file must conform to the 
               PDS4 Ingest_LDDTool schema.

       Output: The output files written by LDDTool are:
                  - an XML Schema file (.xsd),
                  - a Schematron file (.sch),
                  - a process report file (.txt),
                  - a spreadsheet file (.csv) containing the local data dictionary information.
               The output files all have the same name as the input file name.
                                 
       Process: - The input XML document is parsed and the contents are validated against the master database.
                - The validated contents are integrated into the master database.
                - The XML Schema and Schematron files are written.
                - The process report and spreadsheet are written.

                
                
                
