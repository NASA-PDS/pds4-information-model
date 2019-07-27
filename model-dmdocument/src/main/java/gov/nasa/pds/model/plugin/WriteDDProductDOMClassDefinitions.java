// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.model.plugin; 
import java.io.*;
import java.util.*;

/**
 * Write the attribute definition products 
 *   
 */

class WriteDDProductDOMClassDefinitions extends Object{
	PrintWriter prDDReg;
// 1.4	String uId, uIdFileName;

	public WriteDDProductDOMClassDefinitions () {
		return;
	}

	// write the Product Class Definition files
	public void writeDDProductDOMClassDefnFiles (SchemaFileDefn lSchemaFileDefn, String todaysDate) throws java.io.IOException {
				
		// cycle once for each data element
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			String lLID = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title;
//			lLID = "urn:nasa:pds:" + "context:" + "class:" + lLID + "_" + lSchemaFileDefn.lab_version_id;
			lLID = "urn:nasa:pds:context:class:" + lLID + "_" + lSchemaFileDefn.lab_version_id;
			lLID = lLID.toLowerCase();
			String lUIdFileName = DMDocument.registrationAuthorityIdentifierValue + "_" + lClass.nameSpaceIdNC + "_" + lClass.title;
			String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecClassDefn + lUIdFileName + "_" + lSchemaFileDefn.lab_version_id + ".xml";
			prDDReg = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
			writeDDProductDOMClassDefnFile(lSchemaFileDefn, prDDReg, todaysDate, lLID, lClass);
			prDDReg.close();
		}
	}
			
	
	// Print the class Definition Header
	public void writeDDProductDOMClassDefnFile (SchemaFileDefn lSchemaFileDefn, PrintWriter prDDReg, String todaysDate, String lLID, DOMClass lClass) {
		prDDReg.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//		prDDReg.println("<Product_Class_Definition xmlns=\"http://pds.nasa.gov/pds4/pds/v" + DOMInfoModel.ns_version_id + "\"");
		prDDReg.println("<Product_Class_Definition xmlns=\"http://pds.nasa.gov/pds4/pds/v" + lSchemaFileDefn.ns_version_id + "\"");
		prDDReg.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
//		prDDReg.println(" xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" + DOMInfoModel.ns_version_id + "/PDS4_PDS_" + DOMInfoModel.lab_version_id + ".xsd\">");
		prDDReg.println(" xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v" + lSchemaFileDefn.ns_version_id + "/PDS4_PDS_" + lSchemaFileDefn.lab_version_id + ".xsd\">");
		prDDReg.println("    <Identification_Area>");
		prDDReg.println("        <logical_identifier>" + lLID + "</logical_identifier>");
//		prDDReg.println("        <version_id>" + DOMInfoModel.identifier_version_id + "</version_id>");
		prDDReg.println("        <version_id>" + lSchemaFileDefn.identifier_version_id + "</version_id>");
		prDDReg.println("        <title>" + lClass.title + "</title>");
		prDDReg.println("        <information_model_version>" + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</information_model_version>");
		prDDReg.println("        <product_class>Product_Class_Definition</product_class>");
		prDDReg.println("    </Identification_Area>");
		prDDReg.println("    <DD_Class_Full>");
		prDDReg.println("        <name>" + lClass.title + "</name>");
//		prDDReg.println("        <version_id>" + DOMInfoModel.identifier_version_id + "</version_id>");
		prDDReg.println("        <version_id>" + lSchemaFileDefn.identifier_version_id + "</version_id>");
		prDDReg.println("        <local_identifier>" + lLID + "</local_identifier>");
		prDDReg.println("        <steward_id>" + lClass.steward + "</steward_id>");
		prDDReg.println("        <type>" + "PDS4" + "</type>");
		prDDReg.println("        <namespace_id>" + lClass.nameSpaceIdNC + "</namespace_id>");
		prDDReg.println("        <submitter_name>" + lClass.steward + "</submitter_name>");
		prDDReg.println("        <definition>" + lClass.definition + "</definition>");
		prDDReg.println("        <registered_by>" + "RA_0001_NASA_PDS_1" + "</registered_by>");
		prDDReg.println("        <registration_authority_id>" + DMDocument.registrationAuthorityIdentifierValue + "</registration_authority_id>");
		prDDReg.println("        <abstract_flag>" + lClass.isAbstract + "</abstract_flag>");

		
	
		for (Iterator<DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {		
			DOMProp lProp = (DOMProp) j.next();			    	
			if (!(lProp.isAttribute))  {
			    DOMClass lDOMClass = (DOMClass) lProp.hasDOMObject;				        	
			    String lClassLID = DMDocument.registrationAuthorityIdentifierValue + ":" + lDOMClass.nameSpaceIdNC + ":" + lDOMClass.title;
			    lClassLID = "urn:nasa:pds:context:class:" + lClassLID; 
				lClassLID = lClassLID.toLowerCase();
				prDDReg.println("        <DD_Association>");
				prDDReg.println("           <local_identifier>" + lClassLID + "</local_identifier>");
				prDDReg.println("           <reference_type>component_of</reference_type>");
				prDDReg.println("           <minimum_occurrences>" + lProp.cardMin + "</minimum_occurrences>");
			    prDDReg.println("           <maximum_occurrences>" + lProp.cardMax + "</maximum_occurrences>");
			    prDDReg.println("        </DD_Association>");
			} else {							
		    	DOMAttr lDOMAttr = (DOMAttr) lProp.hasDOMObject;			               
			
			    prDDReg.println("        <DD_Association>");
		        String lAttrLID = DMDocument.registrationAuthorityIdentifierValue + ":" + lDOMAttr.classNameSpaceIdNC + ":" + lDOMAttr.parentClassTitle + ":" + lDOMAttr.nameSpaceIdNC + ":" + lDOMAttr.title;
		        lAttrLID = "urn:nasa:pds:context:attribute:" + lAttrLID; 
		        lAttrLID = lAttrLID.toLowerCase();
			    prDDReg.println("           <local_identifier>" + lAttrLID + "</local_identifier>");
			    prDDReg.println("           <reference_type>attribute_of</reference_type>");
		        prDDReg.println("           <minimum_occurrences>" + lProp.cardMin + "</minimum_occurrences>");
			    prDDReg.println("           <maximum_occurrences>" + lProp.cardMax + "</maximum_occurrences>");
			    prDDReg.println("        </DD_Association>");					    					
		    }
		}			
		
		prDDReg.println("        <Terminological_Entry>");
		prDDReg.println("            <name>" + lClass.title + "</name>");
		prDDReg.println("            <definition>" + lClass.definition + "</definition>");
		prDDReg.println("            <language>English</language>");
		prDDReg.println("            <preferred_flag>true</preferred_flag>");
		prDDReg.println("        </Terminological_Entry>");
			
		prDDReg.println("    </DD_Class_Full>");		
		prDDReg.println("</Product_Class_Definition>");
		prDDReg.println("");
	}
}
