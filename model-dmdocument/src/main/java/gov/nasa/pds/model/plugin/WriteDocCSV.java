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

// Write the body content for a CCSDS White Book

class WriteDocCSV extends Object {

	UMLModelClassifications lUMLModelClassifications;
	
	public WriteDocCSV () {
		lUMLModelClassifications = new UMLModelClassifications ();
		return;
	}
	
//	print DocBook File
	public void writeDocCSV (SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
		String lFileName = lSchemaFileDefn.relativeFileSpecCCSDSCSV;			
		lFileName += ".txt";
		PrintWriter prDocBook = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		writeHeader (prDocBook);
		writeClassSection (prDocBook);
		writeFooter (prDocBook);
		prDocBook.close();
		return;
	}
	
	private void writeClassSection (PrintWriter prDocBook) {
        prDocBook.println("");
		
		// get the classification arrays
		for (Iterator <ClassDOMCCSDSClassificationDefn> i = lUMLModelClassifications.classClassificationArr.iterator(); i.hasNext();) {
			ClassDOMCCSDSClassificationDefn lClassClassificationDefn = (ClassDOMCCSDSClassificationDefn) i.next();
			lClassClassificationDefn.classArr = new ArrayList <DOMClass> (lClassClassificationDefn.classMap.values());
		}
		
		// sort according to class order
		TreeMap <String, ClassDOMCCSDSClassificationDefn> sortClassClassificationMap = new TreeMap <String, ClassDOMCCSDSClassificationDefn> ();
		ArrayList <ClassDOMCCSDSClassificationDefn> sortClassClassificationArr;	
		for (Iterator <ClassDOMCCSDSClassificationDefn> i = lUMLModelClassifications.classClassificationArr.iterator(); i.hasNext();) {
			ClassDOMCCSDSClassificationDefn lClassClassificationDefn = (ClassDOMCCSDSClassificationDefn) i.next();
			sortClassClassificationMap.put(lClassClassificationDefn.order, lClassClassificationDefn);
		}
		sortClassClassificationArr = new ArrayList <ClassDOMCCSDSClassificationDefn> (sortClassClassificationMap.values());
				
		// write the classes
		for (Iterator <ClassDOMCCSDSClassificationDefn> i = sortClassClassificationArr.iterator(); i.hasNext();) {
			ClassDOMCCSDSClassificationDefn lClassClassificationDefn = (ClassDOMCCSDSClassificationDefn) i.next();
//	    	prDocBook.println("#1i0 " + lClassClassificationDefn.identifier + "  " + lClassClassificationDefn.title + "  " + lClassClassificationDefn.definition);
	    	prDocBook.println("#11i0 " + "###" + lClassClassificationDefn.title);
//	    	prDocBook.println("#12i0 " + lClassClassificationDefn.definition);
	    	prDocBook.println("#12i0 " + getValue(removeSource(lClassClassificationDefn.definition)));
			for (Iterator <DOMClass> j = lClassClassificationDefn.classArr.iterator(); j.hasNext();) {
				DOMClass lClass = (DOMClass) j.next();
				writeClass (lClassClassificationDefn, lClass, prDocBook);						
			}
		}
	}
	
	private void writeClass (ClassDOMCCSDSClassificationDefn lClassClassificationDefn, DOMClass lClass, PrintWriter prDocBook) {
    	prDocBook.println("#21i0 " + getValue(lClass.title));
        prDocBook.println("#22i0 " + getValue(removeSource(lClass.definition)));
        if (lClass.subClassOf != null) {
        	String lSubClassOfTitle = lClass.subClassOf.title;
        	if ((lSubClassOfTitle.compareTo("System_Architecture") != 0)
        			&& (lSubClassOfTitle.compareTo("Information_Architecture") != 0)
        			&& (lSubClassOfTitle.compareTo("USER") != 0)) {
        		prDocBook.println("#23i1 " + "- Subclass of: " + getValue(lClass.subClassOf.title));
        	}
        }
        
        int itemNum = 0;
		for (Iterator <DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
			DOMProp lProp = (DOMProp) j.next();
			if (lProp.hasDOMObject != null && lProp.hasDOMObject instanceof DOMClass) {
				DOMClass lAssocClass = (DOMClass) lProp.hasDOMObject;
				
//				System.out.println("\ndebug writeClass lProp.identifier:" + lProp.identifier);
//				System.out.println("                 lAssocClass.identifier:" + lAssocClass.identifier);
//				System.out.println("                 lClass.identifier:" + lClass.identifier);
//				System.out.println("                 lClass.title:" + lClass.title);
				
				String lCode = UMLModelClassifications.definedClassToClassificationMap.get(lClass.title);
//				System.out.println("                 lCode:" + lCode);
//				if (lCode != null && (lCode.compareTo("1121") == 0 || lCode.compareTo("1124") == 0 ) && (lClass.title.compareTo("Information_Object") != 0)) continue;
				if (lCode == null) continue;
				if ((lCode.compareTo("1121") == 0 || lCode.compareTo("1124") == 0 )
						&& (lClass.title.compareTo("Information_Object") != 0)
						&& (lClass.title.compareTo("Access_Rights_Information") != 0)
						&& (lClass.title.compareTo("Content_Information") != 0)
						&& (lClass.title.compareTo("Context_Information") != 0)
						&& (lClass.title.compareTo("Fixity_Information") != 0)
						&& (lClass.title.compareTo("Identification_Information") != 0)
						&& (lClass.title.compareTo("Preservation_Description_Information") != 0)
						&& (lClass.title.compareTo("Provenance_Information") != 0)
						&& (lClass.title.compareTo("Reference_Information") != 0)
						&& (lClass.title.compareTo("Representation_Information") != 0)
						) continue;

				
				//					System.out.println("                 FOUND 1121 - lClass.title:" + lClass.title);
//		        prDocBook.println(begDel  + "Assoc Title:" +  lProp.title + midDel + "Assoc Defn:" +  lProp.definition + midDel + "Class Title:" +  lAssocClass.title + endDel);
//		        prDocBook.println("#31i1 " + lProp.title);
				itemNum++;
		        prDocBook.println("#32i1 " + itemNum + ". " + getValue(removeSource(lProp.definition)));
//		        prDocBook.println("#33i1 " + lAssocClass.title);
		        if (lProp.definition.indexOf("TBD") == 0) {
//		        	System.out.println("debug writeClass lProp.identifier:" + lProp.identifier);
//		        	System.out.println("                 lProp.definition:" + lProp.definition);
		        }
		        	
			}
		}
        return;
	}
		
	private void writeHeader (PrintWriter prDocBook) {
		prDocBook.println("--- CCSDS Document Insert --- ");
		prDocBook.println("");
		return;
	}
	
	private void writeFooter (PrintWriter prDocBook) {
		prDocBook.println("");
	}

	private String removeSource (String lValue) {
		int sourceIndex = lValue.indexOf("/source:");
		String lNewValue = lValue;
		if (sourceIndex > -1) lNewValue = lValue.substring(0, sourceIndex);
		return lNewValue;
	}
	
	private String getValue (String lValue) {
		return DOMInfoModel.escapeXMLChar(lValue);
	}

		
	/**
	* escape certain characters for DocBook
	*/
	 String escapeDocBookChar (String aString) {
		String lString = aString;
		return lString;
	}
}
