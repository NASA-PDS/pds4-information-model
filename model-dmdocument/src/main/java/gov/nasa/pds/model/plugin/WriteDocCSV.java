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

class WriteDocCSV extends Object {
	// class structures
	TreeMap <String, ClassClassificationDefn> classClassificationMap;
	ArrayList <ClassClassificationDefn> classClassificationArr;	
	
	// attribute structures
	TreeMap <String, AttrClassificationDefn> attrClassificationMap;
	ArrayList <AttrClassificationDefn> attrClassificationArr;
	
	String begDel = "\"";
	String midDel = "\", \"";
	String endDel = "\"";

	public WriteDocCSV () {
		// class structures
		classClassificationMap = new TreeMap <String, ClassClassificationDefn> ();
		attrClassificationMap = new TreeMap <String, AttrClassificationDefn> ();
		
		// get the current namespaces
		ArrayList <SchemaFileDefn> lSchemaFileDefnArr = new ArrayList <SchemaFileDefn> (DMDocument.masterSchemaFileSortMap.values());
		for (Iterator <SchemaFileDefn> i = lSchemaFileDefnArr.iterator(); i.hasNext();) {
			SchemaFileDefn lSchemaFileDefn = (SchemaFileDefn) i.next();
			classClassificationMap.put(lSchemaFileDefn.identifier, new ClassClassificationDefn (lSchemaFileDefn.identifier));
			attrClassificationMap.put(lSchemaFileDefn.identifier, new AttrClassificationDefn (lSchemaFileDefn.identifier));		}
		
		classClassificationMap.put("pds.product", new ClassClassificationDefn ("pds.product"));
		classClassificationMap.put("pds.pds3", new ClassClassificationDefn ("pds.pds3"));
		classClassificationMap.put("pds.support", new ClassClassificationDefn ("pds.support"));
		classClassificationMap.put("pds.other", new ClassClassificationDefn ("pds.other"));
		classClassificationMap.put("other", new ClassClassificationDefn ("other"));
		classClassificationArr = new ArrayList <ClassClassificationDefn> (classClassificationMap.values());
		
		attrClassificationMap.put("other", new AttrClassificationDefn ("other"));
		attrClassificationArr = new ArrayList <AttrClassificationDefn> (attrClassificationMap.values());
		
		return;
	}
	
//	print DocBook File
	public void writeDocCSV (SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
		String lFileName = lSchemaFileDefn.relativeFileSpecCCSDSCSV;			
		lFileName += ".csv";
		PrintWriter prDocBook = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		writeHeader (prDocBook);
		writeClassSection (DMDocument.masterNameSpaceIdNCLC,prDocBook);
        writeAttrSection (DMDocument.masterNameSpaceIdNCLC, prDocBook); // will  need to iterate for each namespace
//    	writeDataTypeUnitsSection (DMDocument.masterNameSpaceIdNCLC, prDocBook);
		writeFooter (prDocBook);
		prDocBook.close();
		return;
	}
	
	private void writeClassSection (String lNameSpaceId, PrintWriter prDocBook) {
        prDocBook.println("");
		
		// get the class classification maps
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			getClassClassification (lClass);
		}
		
		// get the classification arrays
		for (Iterator <ClassClassificationDefn> i = classClassificationArr.iterator(); i.hasNext();) {
			ClassClassificationDefn lClassClassificationDefn = (ClassClassificationDefn) i.next();
			lClassClassificationDefn.classArr = new ArrayList <PDSObjDefn> (lClassClassificationDefn.classMap.values());
		}
		
		ClassClassificationDefn lClassClassificationDefn = classClassificationMap.get("pds.product");
		if (lClassClassificationDefn != null) {
			prDocBook.println("-----pds.product-----");
			for (Iterator <PDSObjDefn> j = lClassClassificationDefn.classArr.iterator(); j.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) j.next();
				writeClass (lClass, prDocBook);						
			}
	        prDocBook.println("");
		}

		lClassClassificationDefn = classClassificationMap.get("pds.support");
		if (lClassClassificationDefn != null) {
			prDocBook.println("-----pds.support-----");
			for (Iterator <PDSObjDefn> j = lClassClassificationDefn.classArr.iterator(); j.hasNext();) {
				PDSObjDefn lClass = (PDSObjDefn) j.next();
				writeClass (lClass, prDocBook);						
			}
	        prDocBook.println("");
		}
	}

		
	public void getClassClassification (PDSObjDefn lClass) {
		if (lClass.isDataType) return;
		if (lClass.isUnitOfMeasure) return;
			
		// classify the class by namespace and other criteria
		ClassClassificationDefn lClassClassificationDefn = classClassificationMap.get(lClass.nameSpaceIdNC);
		if (lClassClassificationDefn != null) {			
			if (lClass.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) != 0) {
				lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
			} else {
				if (lClass.isRegistryClass) {
					lClassClassificationDefn = classClassificationMap.get("pds.product");
					lClassClassificationDefn.classMap.put(lClass.identifier, lClass);	
				} else if (lClass.title.indexOf("PDS3") > -1) {
					lClassClassificationDefn = classClassificationMap.get("pds.pds3");
					lClassClassificationDefn.classMap.put(lClass.identifier, lClass);	
				} else {
					lClassClassificationDefn = classClassificationMap.get("pds.support");
					lClassClassificationDefn.classMap.put(lClass.identifier, lClass);		
				}
			}
		} else {
			lClassClassificationDefn = classClassificationMap.get("other");
			lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
		}
		return;
	}
	
	private void writeClass (PDSObjDefn lClass, PrintWriter prDocBook) {
 		String lValueString = "";
 		String lValueDel = "";
        String lRegistrationStatus = "Active";
        String lRegistrationStatusInsert = "";
        if (lClass.registrationStatus.compareTo("Retired") == 0) {
        	lRegistrationStatus = "Deprecated";
        	lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;;
        }
    	prDocBook.println(begDel + getValue(lClass.title) + endDel);
        prDocBook.println(begDel + "Name: " +  getValue(lClass.title) + midDel +  "Registration Status: " + lRegistrationStatusInsert + endDel);
        prDocBook.println(begDel + "Version Id: " +  getValue(lClass.versionId) + endDel);
        prDocBook.println(begDel + "Description: " + getValue(lClass.description) + endDel);
        prDocBook.println(begDel + "Namespace Id: " +  getValue(lClass.nameSpaceIdNC) + midDel +  "Steward: " + lClass.steward + midDel +  "Role: " + lClass.role + midDel +  "Status: " + lRegistrationStatus + endDel);
        
        // write hierarchy
 		ArrayList <PDSObjDefn> lClassArr = new ArrayList <PDSObjDefn> (lClass.superClass);
 		lClassArr.add(lClass);
 		lValueString = "";
 		lValueDel = "";
		for (Iterator <PDSObjDefn> i = lClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lHierClass = (PDSObjDefn) i.next();
			lValueString += lValueDel + getValue(lHierClass.title);
			lValueDel = " :: "; 
		}
        prDocBook.println(begDel + "Class Hierarchy: " + lValueString + endDel);

		ArrayList <AttrDefn> lAttrArr = new ArrayList <AttrDefn> ();
		ArrayList <AttrDefn> lAssocArr = new ArrayList <AttrDefn> ();
        if (lClass.allAttrAssocArr != null) {
			for (Iterator <AttrDefn> i = lClass.allAttrAssocArr.iterator(); i.hasNext();) {
				AttrDefn lAttr = (AttrDefn) i.next();
				if (lAttr.isAttribute) lAttrArr.add(lAttr); 
				else lAssocArr.add(lAttr);
			}
        }
			
		// write the attributes
		if (lAttrArr.size() == 0) {
	           prDocBook.println(begDel + "No Attributes" + endDel);

		} else {
            prDocBook.println(begDel + "Attribute(s)" + midDel + "Name" + midDel + "Cardinality" + midDel + "Value" + endDel);

			for (Iterator <AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (! lAttr.isAttribute) continue;
	            lValueString = "None";
	            lValueDel = "";
	    		if ( ! (lAttr.permValueArr == null || lAttr.permValueArr.size() == 0)) {
		            lValueString = "";
	    			for (Iterator <PermValueDefn> k = lAttr.permValueArr.iterator(); k.hasNext();) {
	    				PermValueDefn lPermValueDefn = (PermValueDefn) k.next();
	    				lValueString += lValueDel + getValue(lPermValueDefn.value);
	    				lValueDel = ", ";
	    			}
	    		}
	    		
	            prDocBook.println(begDel + " " + midDel +  getValue(lAttr.title) + midDel +  getValue(getCardinality(lAttr.cardMinI, lAttr.cardMaxI)) + midDel + lValueString + endDel);
			}
		}
		// write the associations
		if (lAssocArr.size() == 0) {
	           prDocBook.println(begDel + "No Associations" + endDel);
		} else {

			// write the associations
            prDocBook.println(begDel + "Association(s)" + midDel + "Name" + midDel + "Cardinality" + midDel + "Class" + endDel);


			for (Iterator <AttrDefn> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.isAttribute) continue;
	            lValueString = "None";
	            lValueDel = "";
	    		if (! (lAttr.valClassArr == null || lAttr.valClassArr.isEmpty())) {
	    			lValueString = "";
	    			for (Iterator<PDSObjDefn> k = lAttr.valClassArr.iterator(); k.hasNext();) {				
	    				PDSObjDefn lCompClass = (PDSObjDefn) k.next();
	    				if (lCompClass == null) continue;
	    				lValueString += lValueDel + getValue(lCompClass.title);
	    				lValueDel = ", ";
	    			}
	    		}
	            prDocBook.println(begDel + "" + midDel + getValue(lAttr.title) + midDel + getValue(getCardinality(lAttr.cardMinI, lAttr.cardMaxI)) + midDel + lValueString  + endDel);
	        }
		}

        // write the references
 		ArrayList <PDSObjDefn> lRefClassArr = getClassReferences (lClass);
 		lValueString = "";
 		lValueDel = "";
 		if (! (lRefClassArr == null || lRefClassArr.isEmpty())) {
 			for (Iterator <PDSObjDefn> i = lRefClassArr.iterator(); i.hasNext();) {
 				PDSObjDefn lRefClass = (PDSObjDefn) i.next();
 				lValueString += lValueDel + getValue(lRefClass.title);
 				lValueDel = ", ";
 			}
 		} else {
 	 		lValueString = "None";
 		}
        prDocBook.println(begDel + "Referenced from: " +  lValueString + endDel);
    	prDocBook.println(" ");
        return;
	}	
				
	private void writeAttrSection (String lNameSpaceId, PrintWriter prDocBook) {
        prDocBook.println("");
        prDocBook.println(begDel + "ATTRIBUTES" + endDel);
		
		// get the attribute classification maps
		for (Iterator <AttrDefn> i = InfoModel.masterMOFAttrArr.iterator(); i.hasNext();) {
			AttrDefn lAttr = (AttrDefn) i.next();
			getAttrClassification (lAttr);
		}
		
		// get the classification arrays
		for (Iterator <AttrClassificationDefn> i = attrClassificationArr.iterator(); i.hasNext();) {
			AttrClassificationDefn lAttrClassificationDefn = (AttrClassificationDefn) i.next();
			lAttrClassificationDefn.attrArr = new ArrayList <AttrDefn> (lAttrClassificationDefn.attrMap.values());
		}
		
		AttrClassificationDefn lAttrClassificationDefn = attrClassificationMap.get(DMDocument.masterNameSpaceIdNCLC);
		if (lAttrClassificationDefn != null) {
			for (Iterator <AttrDefn> j = lAttrClassificationDefn.attrArr.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				writeAttr (lAttr, prDocBook);						
			}
	        prDocBook.println("");
		}
        prDocBook.println("");
	}
	
	private void writeAttr (AttrDefn lAttr, PrintWriter prDocBook) {
        String lRegistrationStatus = "Active";
        String lRegistrationStatusInsert = "";
        if (lAttr.registrationStatus.compareTo("Retired") == 0) {
        	lRegistrationStatus = "Deprecated";
        	lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;;
        }
        
	    prDocBook.println("");
	    prDocBook.println(begDel + getValue(lAttr.title) + midDel + getValue(lAttr.attrParentClass.title) + endDel);
	    prDocBook.println("");
	    prDocBook.println(begDel + "Name: " + getValue(lAttr.title) + midDel + lRegistrationStatusInsert + endDel);
	    prDocBook.println(begDel + "Version Id: " + getValue("1.0.0.0")  + endDel);
	    prDocBook.println(begDel + "Description: " + getValue(lAttr.description) + endDel);
	    prDocBook.println(begDel + "NamespaceIdNC: " +   getValue(lAttr.getNameSpaceIdNC ()) + endDel);
	    prDocBook.println(begDel + "Steward: " +   getValue(lAttr.getSteward ()) + endDel);
	    prDocBook.println(begDel + "Class Name: " + getValue(lAttr.attrParentClass.title) + endDel);
	    prDocBook.println(begDel + "Type: " + getValue(lAttr.valueType) + endDel);
	    prDocBook.println(begDel + "Minimum Value: " + getValueReplaceTBDWithNone(lAttr.getMinimumValue2 (true, false)) + endDel);
	    prDocBook.println(begDel + "Maximum Value: " + getValueReplaceTBDWithNone(lAttr.getMaximumValue2 (true, false)) + endDel);
	    prDocBook.println(begDel + "Minimum Characters: " + getValueReplaceTBDWithNone(lAttr.getMinimumCharacters2 (true, false)) + endDel);
	    prDocBook.println(begDel + "Maximum Characters: " + getValueReplaceTBDWithNone(lAttr.getMaximumCharacters2 (true, false)) + endDel);
	    prDocBook.println(begDel + "Unit of Measure Type: " + getUnitIdLink(lAttr.unit_of_measure_type) + endDel);
	    prDocBook.println(begDel + "Default Unit Id: " + getValueReplaceTBDWithNone(lAttr.default_unit_id) + endDel);
	    prDocBook.println(begDel + "Attribute Concept: " + getValueReplaceTBDWithNone(lAttr.classConcept) + endDel);
	    prDocBook.println(begDel + "Conceptual Domain: " + getValueReplaceTBDWithNone(lAttr.dataConcept) + endDel);
	    prDocBook.println(begDel + "Status: " + lRegistrationStatus + endDel);
	    prDocBook.println(begDel + "Nillable: " + lAttr.isNilable + endDel);            
	    prDocBook.println(begDel + "Pattern: " + getValueReplaceTBDWithNone(lAttr.getPattern(true)) + endDel);
	    
		if (lAttr.permValueArr == null || lAttr.permValueArr.size() == 0) {
	       prDocBook.println(begDel + "Permissible Value(s): " + endDel);
		} else {
	       prDocBook.println(begDel + "Permissible Value(s) " + endDel);

			for (Iterator <PermValueDefn> j = lAttr.permValueArr.iterator(); j.hasNext();) {
				PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
				if (lPermValueDefn.value.compareTo("...") == 0) continue;
				lRegistrationStatusInsert = "";
				if (lPermValueDefn.registrationStatus.compareTo("Retired") == 0) lRegistrationStatusInsert = " - " + DMDocument.Literal_DEPRECATED;

				String lDependValue = lAttr.valueDependencyMap.get(lPermValueDefn.value);
				String lDependClause = "";
				if (lDependValue != null) lDependClause = " (" + lDependValue + ")";
				
				String lValueMeaning = lPermValueDefn.value_meaning;
				if (lAttr.title.compareTo("pattern") == 0 && lValueMeaning.indexOf("TBD") == 0) lValueMeaning = "";
		        prDocBook.println(begDel + midDel + getValue(lPermValueDefn.value) + midDel + getValue(lValueMeaning) + midDel + getValue(lDependClause) + "-" + lRegistrationStatusInsert + endDel);
			}
		}
		if (! (lAttr.permValueExtArr == null || lAttr.permValueExtArr.isEmpty())) {
		
			for (Iterator <PermValueExtDefn> i = lAttr.permValueExtArr.iterator(); i.hasNext();) {
				PermValueExtDefn lPermValueExt = (PermValueExtDefn) i.next();	
				if (lPermValueExt.permValueExtArr == null || lPermValueExt.permValueExtArr.isEmpty()) continue;
				for (Iterator <PermValueDefn> j = lPermValueExt.permValueExtArr.iterator(); j.hasNext();) {
					PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
			        prDocBook.println(begDel + "Extended Value(s) for: " + getValue(lPermValueExt.xpath) + midDel + getValue (lPermValueDefn.value) + midDel + getValue(lPermValueDefn.value_meaning) + endDel);
				}
			}
		}
	  	prDocBook.println("");
	}
	
	public void getAttrClassification (AttrDefn lAttr) {
		if (! (lAttr.isUsedInClass && lAttr.isAttribute)) return;
		
		
		// classify the class by namespace and other criteria
		String lAttrId = lAttr.title + "." + lAttr.attrNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.classNameSpaceIdNC + "." +  DMDocument.registrationAuthorityIdentifierValue;
		AttrClassificationDefn lAttrClassificationDefn = attrClassificationMap.get(lAttr.attrNameSpaceIdNC);
		if (lAttrClassificationDefn != null) {
			lAttrClassificationDefn.attrMap.put(lAttrId, lAttr);	
		} else {
			lAttrClassificationDefn = attrClassificationMap.get("other");
			lAttrClassificationDefn.attrMap.put(lAttrId, lAttr);
		}
		return;
	}	
	
	private void writeDataTypeUnitsSection (String lNameSpaceIdNC, PrintWriter prDocBook) {
        prDocBook.println("");
        prDocBook.println("      <!-- =====================Part4 Begin=========================== -->");
        prDocBook.println("");
		prDocBook.println("    <chapter>");
		prDocBook.println("       <title>Data Types in the common (pds) namespace.</title>");
		prDocBook.println("       <para>These classes define the PDS4 data types. </para>");
					
//		Sort the data types			
		TreeMap <String, PDSObjDefn> sortDataTypeMap = new TreeMap <String, PDSObjDefn> ();
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lNameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (!lClass.isDataType) continue;
			sortDataTypeMap.put(lClass.title, lClass);
		}	
		ArrayList <PDSObjDefn> sortDataTypeArr = new ArrayList <PDSObjDefn> (sortDataTypeMap.values());	
		if (sortDataTypeArr.size() <= 0) return;
			
//		Write the data types
		String lSchemaBaseType = "None", lMinChar = "None", lMaxChar = "None", lMinVal = "None", lMaxVal = "None";
		ArrayList <PermValueDefn> lPermValueArr = null;
		for (Iterator<PDSObjDefn> i = sortDataTypeArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			
			lSchemaBaseType = "None"; lMinChar = "None"; lMaxChar = "None"; lMinVal = "None"; lMaxVal = "None";
			lPermValueArr = null;

			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.title.compareTo("xml_schema_base_type") == 0) {
					lSchemaBaseType = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lSchemaBaseType == null) lSchemaBaseType = "None";
					lSchemaBaseType = DMDocument.replaceString (lSchemaBaseType, "xsd:", "");
				}

				if (lAttr.title.compareTo("maximum_characters") == 0) {
					lMaxChar = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMaxChar == null || lMaxChar.compareTo("2147483647") == 0)) lMaxChar = "None";
				}
				
				if (lAttr.title.compareTo("minimum_characters") == 0) {
					lMinChar = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMinChar == null || lMinChar.compareTo("-2147483648") == 0)) lMinChar = "None";

				}
				if (lAttr.title.compareTo("maximum_value") == 0) {
					lMaxVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMaxVal == null || lMaxVal.compareTo("2147483647") == 0)) lMaxVal = "None";

				}
				if (lAttr.title.compareTo("minimum_value") == 0) {
					lMinVal = InfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMinVal == null || lMinVal.compareTo("-2147483648") == 0)) lMinVal = "None";

				}
				if (lAttr.title.compareTo("pattern") == 0) {
					lPermValueArr = lAttr.permValueArr;
					if (lPermValueArr == null || lPermValueArr.size() == 0) lPermValueArr = null;
				}
			}
					
            prDocBook.println("<sect1>");
            prDocBook.println("    <title>" + getClassAnchor(lClass) + getValue(lClass.title) + "</title>");
            prDocBook.println("");
            prDocBook.println("<para>");
            prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
            prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
            prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <thead>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">" + getPrompt("Name: ") + getValue(lClass.title) + "</entry>");
//            prDocBook.println("                    <entry>" + getPrompt("Version Id: ") + getValue("1.0.0.0") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Version Id: ") + getValue(lClass.versionId) + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("            </thead>");
            prDocBook.println("            <tbody>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Description ") + getValue(lClass.description) + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + getPrompt("Schema Base Type:  ") + getValueReplaceTBDWithNone(lSchemaBaseType) + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("") + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + getPrompt("Minimum Value: ") + getValueReplaceTBDWithNone(lMinVal) + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Maximum Value: ") + getValueReplaceTBDWithNone(lMaxVal) + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Minimum Characters: ") + getValueReplaceTBDWithNone(lMinChar) + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Maximum Characters: ") + getValueReplaceTBDWithNone(lMaxChar) + "</entry>");
            prDocBook.println("                </row>");

            if (lPermValueArr == null) {
                prDocBook.println("                <row>");
                prDocBook.println("                    <entry>" + "</entry>");
                prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">" + getPrompt("No Pattern") + "</entry>");
	            prDocBook.println("                </row>");
            } else {
                prDocBook.println("                <row>");
                prDocBook.println("                    <entry>" + "</entry>");
            	prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">" + getPrompt("Pattern") + "</entry>");           	
	            prDocBook.println("                </row>");
// v1.3	            
    			for (Iterator <PermValueDefn> k = lPermValueArr.iterator(); k.hasNext();) {
    				PermValueDefn lPattern = (PermValueDefn) k.next();
    	            prDocBook.println("                <row>");
                    prDocBook.println("                    <entry>" + "</entry>");
    	            prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">" + getValue(lPattern.value) + "</entry>");
    	            prDocBook.println("                </row>");
    			}
            }
			
            prDocBook.println("");
            prDocBook.println("            </tbody>");
            prDocBook.println("        </tgroup>");
            prDocBook.println("        </informaltable>");
            prDocBook.println("</para>");
          	prDocBook.println("</sect1> ");
		}
		
		prDocBook.println("    </chapter>");		
		prDocBook.println("    <chapter>");
		prDocBook.println("       <title>Units of Measure in the common (pds) namespace.</title>");
		prDocBook.println("       <para>These classes define the PDS4 units of measure. </para>");
		
		// get the units
		ArrayList <PermValueDefn> lPermValueDefnArr = new ArrayList <PermValueDefn> ();
		TreeMap <String, PDSObjDefn> sortUnitsMap = new TreeMap <String, PDSObjDefn> ();
		for (Iterator<PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lNameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (! lClass.isUnitOfMeasure) continue;
			sortUnitsMap.put(lClass.title, lClass);
		}	
		ArrayList <PDSObjDefn> sortUnitsArr = new ArrayList <PDSObjDefn> (sortUnitsMap.values());	
		if (sortUnitsArr.size() <= 0) return;

		for (Iterator<PDSObjDefn> i = sortUnitsArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			lPermValueDefnArr = new ArrayList <PermValueDefn> ();
			for (Iterator<AttrDefn> j = lClass.ownedAttribute.iterator(); j.hasNext();) {
				AttrDefn lAttr = (AttrDefn) j.next();
				if (lAttr.title.compareTo("unit_id") == 0) {
					if (lAttr.permValueArr == null || lAttr.permValueArr.size() == 0) {
						PermValueDefn lPermValueDefn = new PermValueDefn ("None", "None", "");
						lPermValueDefnArr.add(lPermValueDefn);
					}
					else for (Iterator <PermValueDefn> k = lAttr.permValueArr.iterator(); k.hasNext();) {
						PermValueDefn lPermValueDefn = (PermValueDefn) k.next();
						lPermValueDefnArr.add(lPermValueDefn);
					}
				}
			}
					
            prDocBook.println("<sect1>");
            prDocBook.println("    <title>" + getClassAnchor(lClass) + getValue(lClass.title) + "</title>");
            prDocBook.println("");
            prDocBook.println("<para>");
            prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
            prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
            prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <thead>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">" + getPrompt("Name:  ") + getValue(lClass.title) + "</entry>");
//            prDocBook.println("                    <entry>" + getPrompt("Version Id:  ") + getValue("1.0.0.0") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Version Id:  ") + getValue(lClass.versionId) + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("            </thead>");
            prDocBook.println("            <tbody>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Description: ") + getValue(lClass.description) + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + "</entry>");
            prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">" + getPrompt("Unit Id") + "</entry>");
            prDocBook.println("                </row>");
            
			for (Iterator <PermValueDefn> k = lPermValueDefnArr.iterator(); k.hasNext();) {
				PermValueDefn lPermValueDefn = (PermValueDefn) k.next();
	            prDocBook.println("                <row>");
	            prDocBook.println("                    <entry>" + "</entry>");
	            prDocBook.println("                    <entry>" + getValue(lPermValueDefn.value) + "</entry>");
	            prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">" + getValue(lPermValueDefn.value_meaning) + "</entry>");
	            prDocBook.println("                </row>");
			}         
			
            prDocBook.println("");
            prDocBook.println("            </tbody>");
            prDocBook.println("        </tgroup>");
            prDocBook.println("        </informaltable>");
            prDocBook.println("</para>");
          	prDocBook.println("</sect1> ");
		}
		
		// finalize Part 4
		prDocBook.println("         </chapter>");
        prDocBook.println("");
        prDocBook.println("      <!-- ===================== Part4 End=========================== -->");
        prDocBook.println("");
				
	}
	
	private void writeHeader (PrintWriter prDocBook) {
		prDocBook.println(begDel + "IM V" + DMDocument.masterPDSSchemaFileDefn.ont_version_id  + midDel  + DMDocument.sTodaysDate + endDel);
		prDocBook.println("");
		return;
	}
	
	private void writeFooter (PrintWriter prDocBook) {
		prDocBook.println("");
	}

	private String getPrompt (String lPrompt) {
//		return "<emphasis role=\"italic\">" + "<emphasis role=\"bold\">" + lPrompt + "</emphasis>" + "</emphasis>";
//		return "<emphasis role=\"italic\">" + lPrompt + "</emphasis>";
		return "<emphasis>" + lPrompt + "</emphasis>";
	}
	
	private String getValue (String lValue) {
		return InfoModel.escapeXMLChar(lValue);
	}
	
	private String getValueBreak (String lValue) {
		String lValueBreak = DMDocument.replaceString(lValue, "_", "_&#x200B;");
		return lValueBreak;
	}
	
	private String getValueReplaceTBDWithNone(String lValue) {
		if (lValue.indexOf("TBD") == 0) return "None";
		return InfoModel.escapeXMLChar(lValue);
	}
			
	private String getValueAnchor(AttrDefn lAttr, String lValue) {
		String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.attrNameSpaceIdNC + "." + lAttr.title + "." + lValue;
		int lAnchorI = lAnchor.hashCode();
		lAnchor = "N" + Integer.toString(lAnchorI);
		return "<anchor xml:id=\"" + lAnchor + "\"/>";
	}
	
	private String getValueLink(AttrDefn lAttr, String lValue) {
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.attrNameSpaceIdNC + "." + lAttr.title + "." + lValue;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lValue) + "</link>";
	}
	
	private String getAttrAnchor(AttrDefn lAttr) {
		String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.attrNameSpaceIdNC + "." + lAttr.title;
		int lAnchorI = lAnchor.hashCode();
		lAnchor = "N" + Integer.toString(lAnchorI);
		return "<anchor xml:id=\"" + lAnchor + "\"/>";
	}

	private String getAttrLink(AttrDefn lAttr) {
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.attrNameSpaceIdNC + "." + lAttr.title;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		String lRegistrationStatusInsert = "";
		if (lAttr.registrationStatus.compareTo("Retired") == 0) lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lAttr.title) + lRegistrationStatusInsert + "</link>";
	}
	
	private String getClassAnchor(PDSObjDefn lClass) {
		String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title;
		int lAnchorI = lAnchor.hashCode();
		lAnchor = "N" + Integer.toString(lAnchorI);
		return "<anchor xml:id=\"" + lAnchor + "\"/>";
	}

	private String getClassLink(PDSObjDefn lClass) {
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		String lRegistrationStatusInsert = "";
		if (lClass.registrationStatus.compareTo("Retired") == 0) lRegistrationStatusInsert = " " +  DMDocument.Literal_DEPRECATED;
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lClass.title) + lRegistrationStatusInsert + "</link>";
	}
	
	private String getDataTypeLink(String lDataType) {
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + DMDocument.masterNameSpaceIdNCLC + "." + lDataType;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lDataType) + "</link>";
	}
	
	private String getUnitIdLink(String lUnitId) {
		if (lUnitId.indexOf("TBD") == 0) return "None";
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + DMDocument.masterNameSpaceIdNCLC + "." + lUnitId;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		return "<link linkend=\"" + lLink + "\">" + lUnitId + "</link>";
	}
	
	private String getCardinality(int lCardMin, int lCardMax) {
		String pCardMax = "Unbounded";
		if (lCardMax != 9999999) pCardMax = (new Integer(lCardMax)).toString();
		String pCardMin = (new Integer(lCardMin)).toString();
		return pCardMin + ".." + pCardMax;
	}

//	return all classes that reference this class 
	private ArrayList <PDSObjDefn> getClassReferences (PDSObjDefn lTargetClass) {
		ArrayList <PDSObjDefn> refClassArr = new ArrayList <PDSObjDefn> ();
		for (Iterator <PDSObjDefn> i = InfoModel.masterMOFClassArr.iterator(); i.hasNext();) {
			PDSObjDefn lClass = (PDSObjDefn) i.next();
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
				for (Iterator <AttrDefn> j = lClass.ownedAssociation.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
				    if (! (lAttr.valClassArr == null || lAttr.valClassArr.isEmpty())) {
		    			for (Iterator<PDSObjDefn> k = lAttr.valClassArr.iterator(); k.hasNext();) {				
		    				PDSObjDefn lCompClass = (PDSObjDefn) k.next();
							if (lTargetClass == lCompClass) {
								if (! refClassArr.contains(lClass)) {
									refClassArr.add(lClass);
								}
							}
						}
					}
				}
				for (Iterator <AttrDefn> j = lClass.inheritedAssociation.iterator(); j.hasNext();) {
					AttrDefn lAttr = (AttrDefn) j.next();
				    if (! (lAttr.valClassArr == null || lAttr.valClassArr.isEmpty())) {
		    			for (Iterator<PDSObjDefn> k = lAttr.valClassArr.iterator(); k.hasNext();) {				
		    				PDSObjDefn lCompClass = (PDSObjDefn) k.next();
							if (lTargetClass == lCompClass) {
								if (! refClassArr.contains(lClass)) {
									refClassArr.add(lClass);
								}
							}
						}
					}
				}
			}
		}
		return refClassArr;
	}	
	
	/**
	* escape certain characters for DocBook
	*/
	 String escapeDocBookChar (String aString) {
		String lString = aString;
		return lString;
	}
}
