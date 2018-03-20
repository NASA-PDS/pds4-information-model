package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

class WriteDOMDocCSV extends Object {
	// class structures
	TreeMap <String, ClassClassificationDefnDOM> classClassificationMap;
	ArrayList <ClassClassificationDefnDOM> classClassificationArr;	
	
	// attribute structures
	TreeMap <String, AttrClassificationDefnDOM> attrClassificationMap;
	ArrayList <AttrClassificationDefnDOM> attrClassificationArr;
	
	String begDel = "\"";
	String midDel = "\", \"";
	String endDel = "\"";

	public WriteDOMDocCSV () {
		// class structures
		classClassificationMap = new TreeMap <String, ClassClassificationDefnDOM> ();
		attrClassificationMap = new TreeMap <String, AttrClassificationDefnDOM> ();
		
		// get the current namespaces
		ArrayList <SchemaFileDefn> lSchemaFileDefnArr = new ArrayList <SchemaFileDefn> (DMDocument.masterSchemaFileSortMap.values());
		for (Iterator <SchemaFileDefn> i = lSchemaFileDefnArr.iterator(); i.hasNext();) {
			SchemaFileDefn lSchemaFileDefn = (SchemaFileDefn) i.next();
			classClassificationMap.put(lSchemaFileDefn.identifier, new ClassClassificationDefnDOM (lSchemaFileDefn.identifier));
			attrClassificationMap.put(lSchemaFileDefn.identifier, new AttrClassificationDefnDOM (lSchemaFileDefn.identifier));		}
		
		classClassificationMap.put("pds.product", new ClassClassificationDefnDOM ("pds.product"));
		classClassificationMap.put("pds.pds3", new ClassClassificationDefnDOM ("pds.pds3"));
		classClassificationMap.put("pds.support", new ClassClassificationDefnDOM ("pds.support"));
		classClassificationMap.put("pds.other", new ClassClassificationDefnDOM ("pds.other"));
		classClassificationMap.put("other", new ClassClassificationDefnDOM ("other"));
		classClassificationArr = new ArrayList <ClassClassificationDefnDOM> (classClassificationMap.values());
		
		attrClassificationMap.put("other", new AttrClassificationDefnDOM ("other"));
		attrClassificationArr = new ArrayList <AttrClassificationDefnDOM> (attrClassificationMap.values());
		
		return;
	}
	
//	print DocBook File
	public void writeDocCSV (SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
		String lFileName = lSchemaFileDefn.relativeFileSpecCCSDSCSV+"_DOM";			
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
		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			getClassClassification (lClass);
		}
		
		// get the classification arrays
		for (Iterator <ClassClassificationDefnDOM> i = classClassificationArr.iterator(); i.hasNext();) {
			ClassClassificationDefnDOM lClassClassificationDefn = (ClassClassificationDefnDOM) i.next();
			lClassClassificationDefn.classArr = new ArrayList <DOMClass> (lClassClassificationDefn.classMap.values());
		}
		
		ClassClassificationDefnDOM lClassClassificationDefn = classClassificationMap.get("pds.product");
		if (lClassClassificationDefn != null) {
			prDocBook.println("-----pds.product-----");
			for (Iterator <DOMClass> j = lClassClassificationDefn.classArr.iterator(); j.hasNext();) {
				DOMClass lClass = (DOMClass) j.next();
				writeClass (lClass, prDocBook);						
			}
	        prDocBook.println("");
		}

		lClassClassificationDefn = classClassificationMap.get("pds.support");
		if (lClassClassificationDefn != null) {
			prDocBook.println("-----pds.support-----");
			for (Iterator <DOMClass> j = lClassClassificationDefn.classArr.iterator(); j.hasNext();) {
				DOMClass lClass = (DOMClass) j.next();
				writeClass (lClass, prDocBook);						
			}
	        prDocBook.println("");
		}
	}

		
	public void getClassClassification (DOMClass lClass) {
		if (lClass.isDataType) return;
		if (lClass.isUnitOfMeasure) return;
			
		// classify the class by namespace and other criteria
		ClassClassificationDefnDOM lClassClassificationDefn = classClassificationMap.get(lClass.nameSpaceIdNC);
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
	
	private void writeClass (DOMClass lClass, PrintWriter prDocBook) {
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
        prDocBook.println(begDel + "Description: " + getValue(lClass.definition) + endDel);
        prDocBook.println(begDel + "Namespace Id: " +  getValue(lClass.nameSpaceIdNC) + midDel +  "Steward: " + lClass.steward + midDel +  "Role: " + lClass.role + midDel +  "Status: " + lRegistrationStatus + endDel);
        
        // write hierarchy
 		ArrayList <DOMClass> lClassArr = new ArrayList <DOMClass> (lClass.superClassHierArr);
 		lClassArr.add(lClass);
 		lValueString = "";
 		lValueDel = "";
		for (Iterator <DOMClass> i = lClassArr.iterator(); i.hasNext();) {
			DOMClass lHierClass = (DOMClass) i.next();
			lValueString += lValueDel + getValue(lHierClass.title);
			lValueDel = " :: "; 
		}
        prDocBook.println(begDel + "Class Hierarchy: " + lValueString + endDel);

	
        int attrCount = 0, assocCount= 0;
        if (lClass.allAttrAssocArr != null) {
           for (Iterator <DOMProp> i = lClass.allAttrAssocArr.iterator(); i.hasNext();) {
               DOMProp lProp = (DOMProp) i.next();
               if (lProp.isAttribute) {
                  attrCount ++;
               } else {
                  assocCount ++;
               }
           }
        }
		// write the attributes
		if (attrCount == 0) {
	           prDocBook.println(begDel + "No Attributes" + endDel);

		} else {
            prDocBook.println(begDel + "Attribute(s)" + midDel + "Name" + midDel + "Cardinality" + midDel + "Value" + endDel);

			for (Iterator <DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				if (! lProp.isAttribute) continue;
	            lValueString = "None";
	            lValueDel = "";
	            DOMAttr lAttr = (DOMAttr)lProp.hasDOMObject;
	    		if ( ! (lAttr.domPermValueArr == null || lAttr.permValueArr.size() == 0)) {
		            lValueString = "";
		            for (Iterator <DOMProp> k = lAttr.domPermValueArr.iterator(); k.hasNext();) {
                        DOMProp lDOMProp = (DOMProp) k.next();
                        DOMPermValDefn lPermValueDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
                        lValueString += lValueDel + getValue(lPermValueDefn.value);
                        lValueDel = ", ";
                }
	    	}
	    		
	        prDocBook.println(begDel + " " + midDel +  getValue(lAttr.title) + midDel +  getValue(getCardinality(lAttr.cardMinI, lAttr.cardMaxI)) + midDel + lValueString + endDel);
			}
		}
		// write the associations
		if (assocCount == 0) {
	           prDocBook.println(begDel + "No Associations" + endDel);
		} else {

			// write the associations
            prDocBook.println(begDel + "Association(s)" + midDel + "Name" + midDel + "Cardinality" + midDel + "Class" + endDel);

            for (Iterator <DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
                DOMProp lProp = (DOMProp) j.next();
                if (lProp.isAttribute) continue;
                lValueString = "";
                lValueDel = "";
                DOMClass assocClass = (DOMClass)lProp.hasDOMObject;
                for (Iterator<DOMProp> k = assocClass.allAttrAssocArr.iterator(); k.hasNext();) {
                    DOMProp lDOMProp = (DOMProp)k.next();
                    if (lDOMProp.isAttribute) continue;
                    DOMClass lDOMClass = (DOMClass)lDOMProp.hasDOMObject;

                    lValueString += lValueDel + getValue(lDOMClass.title);
                    lValueDel = ", ";
                }
                prDocBook.println(begDel + "" + midDel + getValue(lProp.title) + midDel + getValue(getCardinality(lProp.cardMinI, lProp.cardMaxI)) + midDel + lValueString  + endDel);
     	    }

	
		}

        // write the references
 		ArrayList <DOMClass> lRefClassArr = getClassReferences (lClass);
 		lValueString = "";
 		lValueDel = "";
 		if (! (lRefClassArr == null || lRefClassArr.isEmpty())) {
 			for (Iterator <DOMClass> i = lRefClassArr.iterator(); i.hasNext();) {
 				DOMClass lRefClass = (DOMClass) i.next();
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
		for (Iterator <DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			getAttrClassification (lAttr);
		}
		
		// get the classification arrays
		for (Iterator <AttrClassificationDefnDOM> i = attrClassificationArr.iterator(); i.hasNext();) {
			AttrClassificationDefnDOM lAttrClassificationDefn = (AttrClassificationDefnDOM) i.next();
			lAttrClassificationDefn.attrArr = new ArrayList <DOMAttr> (lAttrClassificationDefn.attrMap.values());
		}
		
		AttrClassificationDefnDOM lAttrClassificationDefn = attrClassificationMap.get(DMDocument.masterNameSpaceIdNCLC);
		if (lAttrClassificationDefn != null) {
			for (Iterator <DOMAttr> j = lAttrClassificationDefn.attrArr.iterator(); j.hasNext();) {
				DOMAttr lAttr = (DOMAttr) j.next();
				writeAttr (lAttr, prDocBook);						
			}
	        prDocBook.println("");
		}
        prDocBook.println("");
	}
	
	private void writeAttr (DOMAttr lAttr, PrintWriter prDocBook) {
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
	    prDocBook.println(begDel + "Description: " + getValue(lAttr.definition) + endDel);
	    prDocBook.println(begDel + "NamespaceIdNC: " +   getValue(lAttr.getNameSpaceIdNC ()) + endDel);
	    prDocBook.println(begDel + "Steward: " +   getValue(lAttr.getSteward ()) + endDel);
	    prDocBook.println(begDel + "Class Name: " + getValue(lAttr.attrParentClass.title) + endDel);
	    prDocBook.println(begDel + "Type: " + getValue(lAttr.valueType) + endDel);
	    prDocBook.println(begDel + "Minimum Value: " + getValueReplaceTBDWithNone(lAttr.getMinimumValue (true, false)) + endDel);
	    prDocBook.println(begDel + "Maximum Value: " + getValueReplaceTBDWithNone(lAttr.getMaximumValue (true, false)) + endDel);
	    prDocBook.println(begDel + "Minimum Characters: " + getValueReplaceTBDWithNone(lAttr.getMinimumCharacters (true, false)) + endDel);
	    prDocBook.println(begDel + "Maximum Characters: " + getValueReplaceTBDWithNone(lAttr.getMaximumCharacters (true, false)) + endDel);
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
	
	public void getAttrClassification (DOMAttr lAttr) {
		if (! (lAttr.isUsedInClass && lAttr.isAttribute)) return;
		
		
		// classify the class by namespace and other criteria
		String lAttrId = lAttr.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.classNameSpaceIdNC + "." +  DMDocument.registrationAuthorityIdentifierValue;
		AttrClassificationDefnDOM lAttrClassificationDefn = attrClassificationMap.get(lAttr.nameSpaceIdNC);
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
		TreeMap <String, DOMClass> sortDataTypeMap = new TreeMap <String, DOMClass> ();
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lNameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (!lClass.isDataType) continue;
			sortDataTypeMap.put(lClass.title, lClass);
		}	
		ArrayList <DOMClass> sortDataTypeArr = new ArrayList <DOMClass> (sortDataTypeMap.values());	
		if (sortDataTypeArr.size() <= 0) return;
			
//		Write the data types
		String lSchemaBaseType = "None", lMinChar = "None", lMaxChar = "None", lMinVal = "None", lMaxVal = "None";
		ArrayList <DOMProp> lPermValueArr = null;
		for (Iterator<DOMClass> i = sortDataTypeArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			
			lSchemaBaseType = "None"; lMinChar = "None"; lMaxChar = "None"; lMinVal = "None"; lMaxVal = "None";
			lPermValueArr = null;

			for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lProp = j.next();
				DOMAttr lAttr = (DOMAttr) lProp.hasDOMObject;
				if (lAttr.title.compareTo("xml_schema_base_type") == 0) {
					lSchemaBaseType = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lSchemaBaseType == null) lSchemaBaseType = "None";
					lSchemaBaseType = DMDocument.replaceString (lSchemaBaseType, "xsd:", "");
				}

				if (lAttr.title.compareTo("maximum_characters") == 0) {
					lMaxChar = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMaxChar == null || lMaxChar.compareTo("2147483647") == 0)) lMaxChar = "None";
				}
				
				if (lAttr.title.compareTo("minimum_characters") == 0) {
					lMinChar = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMinChar == null || lMinChar.compareTo("-2147483648") == 0)) lMinChar = "None";

				}
				if (lAttr.title.compareTo("maximum_value") == 0) {
					lMaxVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMaxVal == null || lMaxVal.compareTo("2147483647") == 0)) lMaxVal = "None";

				}
				if (lAttr.title.compareTo("minimum_value") == 0) {
					lMinVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMinVal == null || lMinVal.compareTo("-2147483648") == 0)) lMinVal = "None";

				}
				if (lAttr.title.compareTo("pattern") == 0) {
					lPermValueArr = lAttr.domPermValueArr;
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
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Description ") + getValue(lClass.definition) + "</entry>");
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
    			for (Iterator <DOMProp> k = lPermValueArr.iterator(); k.hasNext();) {
    				DOMProp lProp = k.next();
    				DOMPermValDefn lPattern = (DOMPermValDefn) lProp.hasDOMObject;
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
		ArrayList <DOMPermValDefn> lPermValueDefnArr = new ArrayList <DOMPermValDefn> ();
		TreeMap <String, DOMClass> sortUnitsMap = new TreeMap <String, DOMClass> ();
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lNameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (! lClass.isUnitOfMeasure) continue;
			sortUnitsMap.put(lClass.title, lClass);
		}	
		ArrayList <DOMClass> sortUnitsArr = new ArrayList <DOMClass> (sortUnitsMap.values());	
		if (sortUnitsArr.size() <= 0) return;

		for (Iterator<DOMClass> i = sortUnitsArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			lPermValueDefnArr = new ArrayList <DOMPermValDefn> ();
			for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				DOMAttr lAttr = (DOMAttr) lProp.hasDOMObject;
				if (lAttr.title.compareTo("unit_id") == 0) {
					if (lAttr.domPermValueArr == null || lAttr.domPermValueArr.size() == 0) {
						DOMPermValDefn lPermValueDefn = new DOMPermValDefn ();
						lPermValueDefnArr.add(lPermValueDefn);
					}
					else for (Iterator <DOMProp> k = lAttr.domPermValueArr.iterator(); k.hasNext();) {
						DOMProp lPermValueProp = (DOMProp) k.next();					
						DOMPermValDefn lPermValueDefn = (DOMPermValDefn) lPermValueProp.hasDOMObject;
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
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Description: ") + getValue(lClass.definition) + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + "</entry>");
            prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">" + getPrompt("Unit Id") + "</entry>");
            prDocBook.println("                </row>");
            
			for (Iterator <DOMPermValDefn> k = lPermValueDefnArr.iterator(); k.hasNext();) {
				DOMPermValDefn lPermValueDefn = (DOMPermValDefn) k.next();
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
			
	private String getValueAnchor(DOMAttr lAttr, String lValue) {
		String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.title + "." + lValue;
		int lAnchorI = lAnchor.hashCode();
		lAnchor = "N" + Integer.toString(lAnchorI);
		return "<anchor xml:id=\"" + lAnchor + "\"/>";
	}
	
	private String getValueLink(DOMAttr lAttr, String lValue) {
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.title + "." + lValue;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lValue) + "</link>";
	}
	
	private String getAttrAnchor(DOMAttr lAttr) {
		String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.title;
		int lAnchorI = lAnchor.hashCode();
		lAnchor = "N" + Integer.toString(lAnchorI);
		return "<anchor xml:id=\"" + lAnchor + "\"/>";
	}

	private String getAttrLink(DOMAttr lAttr) {
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.title;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		String lRegistrationStatusInsert = "";
		if (lAttr.registrationStatus.compareTo("Retired") == 0) lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lAttr.title) + lRegistrationStatusInsert + "</link>";
	}
	
	private String getClassAnchor(DOMClass lClass) {
		String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title;
		int lAnchorI = lAnchor.hashCode();
		lAnchor = "N" + Integer.toString(lAnchorI);
		return "<anchor xml:id=\"" + lAnchor + "\"/>";
	}

	private String getClassLink(DOMClass lClass) {
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
	private ArrayList <DOMClass> getClassReferences (DOMClass lTargetClass) {
		ArrayList <DOMClass> refClassArr = new ArrayList <DOMClass> ();
		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
				for (Iterator <DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
					DOMProp lProp = j.next();
					DOMClass ownedClass = (DOMClass) lProp.hasDOMObject;
				    
					if (lTargetClass == ownedClass) {
					   if (! refClassArr.contains(lClass)) {
							refClassArr.add(lClass);
						}
					}
				}
			}
				
			for (Iterator <DOMProp> j = lClass.inheritedAssocArr.iterator(); j.hasNext();) {
					DOMProp lProp = (DOMProp) j.next();
					DOMClass inheritedClass = (DOMClass) lProp.hasDOMObject;
				    
					if (lTargetClass == inheritedClass) {
						if (! refClassArr.contains(lClass)) {
							refClassArr.add(lClass);
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
