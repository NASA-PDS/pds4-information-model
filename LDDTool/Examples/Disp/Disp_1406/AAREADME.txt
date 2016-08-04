*******************************************************************************

                     AAREADME File for Display Dictionary

*******************************************************************************

 QUICKSTART: Before using this dictionary, you must edit one line in the XML
 catalog file, named "catalog_1100.xml".  Open this file in any text editor.

 Find the line in the file that begins with "<group xml:base=".  Edit this line
 to point to the directory on your computer containing the dictionary you have
 just downloaded.  For operating systems based on Unix (Linux, MacOSX), the
 file path should have a form similar to the following:

     file:///Users/myusername/mydirectorypath    (MacOSX)
 or
     file:///home/myusername/mydirectorypath     (Unix, Linux)

 For PC based operating systems, the file path should have the form:

     file:///S:\myusername\mydirectorypath

 You should now be able to view and test this dictionary by opening the file
 "PDS4_DISP_1100.xpr" as a project in Oxygen.

 At the present time, the exact details of how to load this dictionary into
 Eclipse and use it are unclear.  Once this process is worked out, detailed
 instructions will be made available in future versions of this dictionary.

*******************************************************************************

 You have just downloaded the Display discipline dictionary produced by the PDS
 Imaging Node.  This dictionary consists of several files.  The following list
 describes all the files that have been included in this dictionary.

 PDS4_DISP_1100.xsd           - the XML schema file containing the dictionary
 PDS4_DISP_1100.sch           - the Schematron validation file for the
                                dictionary
 PDS4_DISP_1100.xml           - the PDS4 XML label for the dictionary product

 PDS4_Display_1100_20140221201259.xml - the PDS4 XML Ingest_LDD file used to
                                create the dictionary
 PDS4_Display_1100_20140221201259_ext.xml - a non-standard "extended" PDS4 XML
                                Ingest_LDD file used to create the dictionary
                                (may contain Schematron rules and native unit
                                definitions)

 PDS4_DISP_1100.html          - the HTML documentation file for the dictionary
 catalog_1100.xml             - an OASIS XML catalog file providing the
                                mappings between the local and PDS common
                                namespaces and their corresponding schema files
 PDS4_DISP_1100.xpr           - a project file that can be used to laod the
                                dictionary and its associated files into Oxygen
 .project                     - a project file that can be used to load the
                                dictionary and its associated files into
                                Eclipse (not yet functional)

 PDS4_PDS_1101.xsd            - a copy of the PDS common dictonary schema file
 PDS4_PDS_1101.sch            - a copy of the PDS common dictionary Schematron
                                validation file
 PDS4_PDS_1101.xml            - a copy of the PDS common dictionary PDS4 XML
                                label

 sample_display_2d.img        - PDS3 labeled MGS MOC data product.
 sample_display_2d.xml        - PDS4 label demonstrating use of the Display
                                dictionary for a simple grayscale image.
 sample_color.img             - PDS3 labeled MPF Rover Camera data product.
 sample_color.xml             - PDS4 label demonstrating use of the Display
                                dictionary for a color image.
 sample_movie.gif             - MPF IMP movie sequence in animated GIF format.
 sample_movie.img             - Faked up MPF IMP movie sequence in raster
                                format.
 sample_movie.xml             - PDS4 label demonstrating use of the Display
                                dictionary for movie data.
