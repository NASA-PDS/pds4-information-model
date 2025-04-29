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

import java.io.FileOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Writes the PDS4 DD content to a TTL (RDF) file 
 * 
 * *** Currently Used ***
 * IMTool - PDS4.All.Products.Class.Prop
 * LDDTool - PDS4.LDD.All * 
 */

class WriteDOMRDFTTLFile extends Object{

	String masterNameSpaceIdNC = "TBD_masterNameSpaceIdNC";
	PrintWriter prRDFFile;
	
	// allow each class to be defined only once
	ArrayList <String> writtenClassIdArr = new ArrayList <String> ();

	// ensure each class is written once
	TreeMap <String, ClassPropertiesOWL> referencedClassIdMap = new TreeMap <String, ClassPropertiesOWL> ();
	
	// flag for oaisif (fuseki) vs pds4 (graphdb)
	// slashes should be OK, just use <>
	boolean isPDS = true;

	public WriteDOMRDFTTLFile () {
		return;
	}

	// write the TTL file
	public void writeDOMRDFTTLFile (String lClassificationType, ClassAttrPropClassification lClassAttrPropClassification) throws java.io.IOException {
		String lFileName = DMDocument.masterPDSSchemaFileDefn.relativeFileSpecOWLRDF_DOM;
		lFileName = DOMInfoModel.replaceString (lFileName, ".rdf", "_IKG.ttl");
		
		
		prRDFFile = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		
		// set the namespaceid to be used as the RDF prefix
		masterNameSpaceIdNC = ClassAttrPropClassification.getMasterNameSpaceIdNC();
		
		// write the Header
		writeRDFHdr(lClassAttrPropClassification);
		
		// write the Body
		writeRDFBody (lClassAttrPropClassification);
		
		// write the Footer and close
		writeRDFFtr();
		prRDFFile.close();
		
	}	
	// Print the JSON Header
	public void writeRDFHdr (ClassAttrPropClassification lClassAttrPropClassification) {
		
		prRDFFile.println("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .");
		prRDFFile.println("@prefix owl: <http://www.w3.org/2002/07/owl#> .");
		prRDFFile.println("@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .");
		prRDFFile.println("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .");
		prRDFFile.println("@prefix dc: <http://purl.org/dc/elements/1.1/> .");
		for (String lPre : lClassAttrPropClassification.nameSpaceIdNCArr) {
			prRDFFile.println("@prefix " + lPre + ": <http://ontology.pds.nasa.gov/" + lPre + "/> .");
		}
		prRDFFile.println("###########################################");
		prRDFFile.println("########## Class definitions");
		prRDFFile.println("########## DateTime:" + DMDocument.masterTodaysDateTimeUTCFromInstant);
		prRDFFile.println("###########################################");
	}

	// Print the JSON Footer
	public  void writeRDFFtr () {
		prRDFFile.println("  ");
	}
	
//	print the body
	public  void writeRDFBody (ClassAttrPropClassification lClassAttrPropClassification) {

   		// write the simple base properties, e.g., ISA, attributeOf, componentOf
//   		writeSubClassRedefined (lClassAttrPropClassification); // PDS definition for ISA -> subClassOf
   		writeDatatypePropertyAttributeOf (lClassAttrPropClassification);  // definition for attributeOf
   		writeDatatypePropertyComponentOf (lClassAttrPropClassification); // definition for componentOf

		// scan through the ClassPropertiesOWL and write the classes
		ArrayList <ClassPropertiesOWL> lClassPropertiesOWLArr = lClassAttrPropClassification.getClassPropertiesOWL ();

		// debug
/*		ArrayList <String> allClassIdArr = new ArrayList <String> ();
		for(ClassPropertiesOWL lClassPropertiesOWL : lClassPropertiesOWLArr) {
			allClassIdArr.add(lClassPropertiesOWL.theClass.identifier);
		}
   		System.out.println("\ndebug ******* writeRDFBody allClassIdArr:" + allClassIdArr); */
		
		for(ClassPropertiesOWL lClassPropertiesOWL : lClassPropertiesOWLArr) {	
           	// write the RDF for the class and its properties, including its superclass
//    		writeClassDefn (lClassPropertiesOWL, lClassAttrPropClassification, true);
    		writeClassDefn (lClassPropertiesOWL, lClassAttrPropClassification);
		}
        
        // write a class definition for each class referenced but not defined
        ArrayList <String> referencedClassIdArr = new ArrayList <String> (referencedClassIdMap.keySet());
		for (String lClassId : referencedClassIdArr) {
        	if (! writtenClassIdArr.contains(lClassId)) {
        		ClassPropertiesOWL lClassPropertiesOWL = referencedClassIdMap.get(lClassId);
        		String preISA = "rdfs";
        		if (true) preISA = masterNameSpaceIdNC;
        		prRDFFile.println(" ");	
        		prRDFFile.println("### Referenced Undefined:" + lClassPropertiesOWL.theClass.identifier + " ###");	
        		prRDFFile.println(lClassPropertiesOWL.iri + " " + "rdf:type" + " " + "rdfs:Class" + ";");	
        		prRDFFile.println("  " + "rdfs:label \"" + lClassPropertiesOWL.title + "\";");	
        		prRDFFile.println("  " + "rdfs:comment \"" + escapeXMLChar(lClassPropertiesOWL.definition) + "\".");
        	}
        }
		
		// write the relationships
//		ArrayList <RelationshipProperties> lRelationshipPropertiesArr = lClassAttrPropClassification.getRelationshipProperties ();
//		writeRelationships (lRelationshipPropertiesArr);
	}
	
	// write a class
//	public  void writeClassDefn (ClassPropertiesOWL lClassPropertiesOWL, ClassAttrPropClassification lClassAttrPropClassification, boolean useISA) {	
	public  void writeClassDefn (ClassPropertiesOWL lClassPropertiesOWL, ClassAttrPropClassification lClassAttrPropClassification) {	
//		System.out.println("debug writeClassDefn -PARENT- lClassPropertiesOWL.identifier:" + lClassPropertiesOWL.identifier + "  --  lClassPropertiesOWL.theClass.identifier:" + lClassPropertiesOWL.theClass.identifier);

		// write each class only once; two or more instances can occur as children
		if (writtenClassIdArr.contains(lClassPropertiesOWL.theClass.identifier)) return;
		writtenClassIdArr.add(lClassPropertiesOWL.theClass.identifier);
		
		String preISA = "rdfs";
//		if (useISA) preISA = masterNameSpaceIdNC;
		prRDFFile.println(" ");	
		
		prRDFFile.println("### Class (Selected):" + lClassPropertiesOWL.theClass.identifier + " ###");	
		prRDFFile.println(lClassPropertiesOWL.iri + " " + "rdf:type" + " " + "rdfs:Class" + ";");	
		prRDFFile.println("  " + "rdfs:label \"" + lClassPropertiesOWL.title + "\";");	
		prRDFFile.println("  " + "owl:versionInfo \"" + lClassPropertiesOWL.versionId + "\";");	
		prRDFFile.println("  " + preISA + ":subClassOf " + lClassPropertiesOWL.subClassOfIRI + ";");							
		prRDFFile.println("  " + "rdfs:comment \"" + escapeXMLChar(lClassPropertiesOWL.definition) + "\".");
		
		// ensure the subclass of this class is ultimately defined
		ClassPropertiesOWL lClassPropertiesOWLSubclass  = new ClassPropertiesOWL (lClassPropertiesOWL.subClassOf);
		referencedClassIdMap.put(lClassPropertiesOWLSubclass.theClass.identifier, lClassPropertiesOWLSubclass);

   		// write the RDF for classes associated with the class.
 		writeAssociatedClasses (lClassPropertiesOWL);
  		
		// write the RDF for each subclass of this class and its properties (e.g., Instrument.type = Accelerometer)
//		writeTypesAsSubClasses (lClassPropertiesOWL, lClassAttrPropClassification, 1);
		return;
	}
	
	// write the RDF for classes associated with the class.
	public  void writeAssociatedClasses (ClassPropertiesOWL lClassPropertiesOWL) {
		
		// return if the Component OF Class Array is null
		if (lClassPropertiesOWL.theComponentOfClassArr == null) return;

		int numComp = lClassPropertiesOWL.theComponentOfClassArr.size();
		
		// return if the Component OF Class Array is empty
		if (numComp == 0) return;
		
		// if the Component OF Class Array has one entry ...
		if (numComp == 1) {
			writeAssociatedClassSingle (lClassPropertiesOWL);
		} else { // if the Component OF Class Array has more than one entry ...
			writeAssociatedClassMulti (lClassPropertiesOWL);
		}
		return;
	}
	
	// write the RDF for classes associated with the class.
	public  void writeAssociatedClassSingle (ClassPropertiesOWL lClassPropertiesOWL) {
		
		// write an association for the single component class
		for (ClassPropertiesOWL lClassPropertiesOWLComp : lClassPropertiesOWL.theComponentOfClassArr) {

			// write the definition of the ObjectProperty that relates the single component class to the selected class			
			prRDFFile.println(" ");	
			prRDFFile.println("### ObjectProperty (One):" + lClassPropertiesOWL.theClass.identifier + " ###");	
//			prRDFFile.println(" ");	
			String lIRI = ClassPropertiesBase.formValueIRI(ClassPropertiesBase.objectPropertyComponentOfP1 + "-" + lClassPropertiesOWL.title);
			prRDFFile.println(lIRI + " " + "rdf:type" + " " + "owl:ObjectProperty" + ";");	
			prRDFFile.println("  " + "rdfs:label \"" + "component of " + lClassPropertiesOWL.title + "\";");	
			prRDFFile.println("  " + "rdfs:domain " + lClassPropertiesOWLComp.iri + ";");	
			prRDFFile.println("  " + "rdfs:range " + lClassPropertiesOWL.iri + ";");	
			prRDFFile.println("  " + "rdfs:subPropertyOf " + ClassPropertiesBase.objectPropertyComponentOf + ";");	
			prRDFFile.println("  " + "rdfs:comment \"" + "These are the component(s) of " + lClassPropertiesOWL.title + "\".");

			// ensure the component is ultimately defined
			referencedClassIdMap.put(lClassPropertiesOWLComp.theClass.identifier, lClassPropertiesOWLComp);				
		}
	}
	
	// write the RDF for classes associated with the class.
	public  void writeAssociatedClassMulti (ClassPropertiesOWL lClassPropertiesOWL) {
		
		// write the definition of the ObjectProperty that relates the parent components class to the selected class
		// the component classes are subclasses of the parent "components" class
		String lIRICompSuperClass = ClassPropertiesBase.formValueIRI(ClassPropertiesBase.objectPropertySubclassPrefix + "-" + lClassPropertiesOWL.title + "-components");
		String lIRI = ClassPropertiesBase.formValueIRI(ClassPropertiesBase.objectPropertyComponentOfP1 + "-" + lClassPropertiesOWL.title);
		prRDFFile.println(" ");	
		prRDFFile.println("### ObjectProperty (Multi):" + lClassPropertiesOWL.theClass.identifier + " ###");	
//		prRDFFile.println(" ");	
		prRDFFile.println(lIRI + " " + "rdf:type" + " " + "owl:ObjectProperty" + ";");	
		prRDFFile.println("  " + "rdfs:label \"" + "components of " + lClassPropertiesOWL.title + "\";");
		prRDFFile.println("  " + "rdfs:domain " + lIRICompSuperClass + ";");	
		prRDFFile.println("  " + "rdfs:range " + lClassPropertiesOWL.iri + ";");	
		prRDFFile.println("  " + "rdfs:subPropertyOf " + ClassPropertiesBase.objectPropertyComponentOf + ";");	
		prRDFFile.println("  " + "rdfs:comment \"" + "These are the component(s) of " + lClassPropertiesOWL.title + "\".");
		
		
		// write the definition of the single "Components" class
		prRDFFile.println(" ");	
		prRDFFile.println("### Proxy Component Class (Multi):" + lClassPropertiesOWL.theClass.identifier + " ###");	
		prRDFFile.println(lIRICompSuperClass + " " + "a" + " " + "owl:Class" + ";");	
		prRDFFile.println("  " + "rdfs:label \"" + lClassPropertiesOWL.title + " Components" + "\";");	
		prRDFFile.println("  " + "rdfs:comment \"" + "This is the " + lClassPropertiesOWL.title + " components superclass." + "\".");
		
		// write the definition of each individual component class, as a subclass of the single "Components" class.
		for (ClassPropertiesOWL lClassPropertiesOWLComp : lClassPropertiesOWL.theComponentOfClassArr) {
			prRDFFile.println(" ");	
			prRDFFile.println("### Proxy Component Subclass (Multi):" + lClassPropertiesOWLComp.theClass.identifier + " ###");	
			prRDFFile.println(lClassPropertiesOWLComp.iri + " " + "a" + " " + "owl:Class" + ";");	
			prRDFFile.println("  " + "rdfs:label \"" + lClassPropertiesOWLComp.title + "\";");	
			prRDFFile.println("  " + "rdfs:subClassOf " + lIRICompSuperClass + ".");	

			// ensure the component is ultimately defined
			referencedClassIdMap.put(lClassPropertiesOWLComp.theClass.identifier, lClassPropertiesOWLComp);		
		}
	}
			
	// write the values of *_type as subclasses (e.g., Instrument.type = Accelerometer)
	public  void writeTypesAsSubClasses (ClassPropertiesOWL lClassPropertiesOWL, ClassAttrPropClassification lClassAttrPropClassification, int sign) {
		// check for ClassPropertiesOWL with a null DOMClass; such as created from permissible values - see note below					
		if (lClassPropertiesOWL.theClass == null) {
			return;
		}
		for (Iterator <DOMProp> j = lClassPropertiesOWL.theClass.ownedAttrArr.iterator(); j.hasNext();) {
			DOMProp lDOMProp = (DOMProp) j.next();
			if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMAttr) {					
				DOMAttr lDOMAttr = (DOMAttr) lDOMProp.domObject;
				if (lDOMAttr.title.compareTo("type") == 0) {
					for (Iterator <DOMProp> k = lDOMAttr.domPermValueArr.iterator(); k.hasNext();) {
						DOMProp lDOMPropPermValue = (DOMProp) k.next();
						if (lDOMPropPermValue.domObject != null && lDOMPropPermValue.domObject instanceof DOMPermValDefn) {
							DOMPermValDefn lDOMPermVal = (DOMPermValDefn) lDOMPropPermValue.domObject;

							// note that this creates a ClassPropertiesOWL with a null DOMClass					
							ClassPropertiesOWL lClassPropertiesPermVal = new ClassPropertiesOWL (lDOMPermVal, lClassPropertiesOWL);
//							writeClassDefn (lClassPropertiesPermVal, lClassAttrPropClassification, false);
							writeClassDefn (lClassPropertiesPermVal, lClassAttrPropClassification);
						}
					}
				}
			}
		}
	}	
	
	public  void writeRelationships (ArrayList <RelationshipProperties> lRelationshipPropertiesArr) {
		for(RelationshipProperties lRelationshipProperties : lRelationshipPropertiesArr) {
			writeRelationshipProperties (lRelationshipProperties);
		}	
	}
	
	public  void writeRelationshipProperties (RelationshipProperties lRelationshipProperties) {
		prRDFFile.println(" ");	
		String lLIDVID = ClassPropertiesBase.formValueLID(lRelationshipProperties.fromClassProperties.nameSpaceIdNC + ":" + lRelationshipProperties.lidvid); 
		prRDFFile.println(masterNameSpaceIdNC + ":" + lLIDVID + " " + "rdf:type" + " " + "rdf:Property" + ";");	
		prRDFFile.println("  " + "rdfs:label \"" + lRelationshipProperties.title + "\";");	
		prRDFFile.println("  " + "rdfs:domain " + lRelationshipProperties.fromClassProperties.nameSpaceIdNC + ":" + lRelationshipProperties.fromClassProperties.lidvid + ";");				
		prRDFFile.println("  " + "rdfs:range " + lRelationshipProperties.toClassProperties.nameSpaceIdNC + ":" + lRelationshipProperties.toClassProperties.lidvid + ".");	
	}	

	// write the RDF for attributes associated with the class.
	public  void writeAssociatedAttributes (ClassPropertiesOWL lClassPropertiesOWL) {
		ArrayList <ClassPropAttr> theClassPropAttrArr = lClassPropertiesOWL.getTheClassPropAttrArr () ;
		for (ClassPropAttr lClassPropAttr : theClassPropAttrArr) {
			DOMAttr lDOMAttr = lClassPropAttr.toAttr;
			prRDFFile.println(" ");	
			String lIRI = ClassPropertiesBase.formValueIRI(lClassPropertiesOWL.lid + lDOMAttr.title + "::" + lClassPropertiesOWL.vid);
			prRDFFile.println(lIRI  + " " + "rdf:type" + " " + "owl:DatatypeProperty" + ";");	
			prRDFFile.println("  " + "rdfs:label \"" + lDOMAttr.parentClassTitle + " " + lDOMAttr.title + "\";");	
			prRDFFile.println("  " + "rdfs:domain " + lClassPropertiesOWL.iri + ";");	
			prRDFFile.println("  " + "rdfs:range " + lDOMAttr.xmlBaseDataType + ";");	
			prRDFFile.println("  " + "rdfs:subPropertyOf " + ClassPropertiesBase.datatypePropertyAttributeOf + ";");	
			prRDFFile.println("  " + "rdfs:comment \"" + escapeXMLChar(lDOMAttr.definition) + "\".");	
		}
	}
	
	// special definitions
	
	// write RDF for the rdfs:subClassOf property; label as ISA
	public  void writeSubClassRedefined (ClassAttrPropClassification lClassAttrPropClassification) {
		prRDFFile.println(" ");	
		prRDFFile.println(masterNameSpaceIdNC + ":" + "subClassOf" + " " + "rdfs:subPropertyOf" + " " + "rdfs:subClassOf" + ";");	
		prRDFFile.println("  " + "rdfs:label \"" + "is_a" + "\".");	

		// Pds4:subClassOf a rdfs:subClassOf ;
		//   Pds4:label: “ISA”
	}
	
	// write the one definition for attributeOf
	public  void writeDatatypePropertyAttributeOf (ClassAttrPropClassification lClassAttrPropClassification) {
		prRDFFile.println(" ");	
		prRDFFile.println(ClassPropertiesBase.datatypePropertyAttributeOf + " " + "rdf:type" + " " + "owl:DatatypeProperty" + ";");	
		prRDFFile.println("  " + "rdfs:label \"" + "attributeOf" + "\".");	
	}	
	
	// write the one definition for componentOf
	public  void writeDatatypePropertyComponentOf (ClassAttrPropClassification lClassAttrPropClassification) {
		prRDFFile.println(" ");	
		prRDFFile.println(ClassPropertiesBase.objectPropertyComponentOf + " " + "rdf:type" + " " + "owl:ObjectProperty" + ";");	
		prRDFFile.println("  " + "rdfs:label \"" + "componentOf" + "\".");	
	}	

	static String escapeXMLChar (String aString) {
		String lString = aString;
		lString = DOMInfoModel.replaceString (lString, "\\", "\\\\");  // escape of backslash must be first
		lString = DOMInfoModel.replaceString (lString, "\"", "&quot;");
		lString = DOMInfoModel.replaceString (lString, "'", "&apos;");
		return lString;
	}
}	
