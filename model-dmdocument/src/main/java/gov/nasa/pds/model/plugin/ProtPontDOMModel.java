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
import java.util.*;

/**
 * Transforms a token array (parsed Protege .pont file) into logical entities.
 *   ProtPontDOMModel - get token array from parser and convert into classes and attributes
 */
class ProtPontDOMModel extends DOMInfoModel{
	
	DOMClass lClass;
	DOMProtAttr lProtAttr;
	ArrayList <String> tokenArray;
	String attrTitle;
	String className;
	String classNameSpaceIdNC;
	TreeMap <String, String> masterClassDispo = new TreeMap <String, String> ();
	
	public ProtPontDOMModel () {
		parsedClassMap = new TreeMap <String, DOMClass> ();
		
		parsedProtAttrArr = new ArrayList <DOMProtAttr> ();		
		parsedProtAttrMap = new TreeMap <String, DOMProtAttr> ();		
		
		className = "";
		classNameSpaceIdNC = "";
		return;
	}
	
	/**
	 *   getProtModel  - get token array from parser and convert into classes and attributes
	 */
	public void getProtModel (String modelId, String fname) throws Throwable {
		ProtFramesParser lparser = new ProtFramesParser();
		if (lparser.parse(fname)) {
			tokenArray = lparser.getTokenArray();
			Iterator<String> tokenIter = tokenArray.iterator();
			getOntology(modelId, tokenIter);
		}
		return;
	}
	
	private void getOntology(String subModelId, Iterator<String> tokenIter) {
		int type = 0;
		int nestlevel = 0;
		
		while (tokenIter.hasNext()) {
			String token = (String) tokenIter.next();
			if (token.compareTo("name_") == 0) {
				token = "name";				
			}				
			if (token.compareTo("(") == 0) {
				nestlevel++;
			} else if (token.compareTo(")") == 0) {
				nestlevel--;
			}
			switch (type) {
			case 0:
				if (token.compareTo("defclass") == 0) {
					type = 1;
				} else if (token.compareTo("is-a") == 0) {
					type = 2;
				} else if (token.compareTo("single-slot") == 0) {
					type = 3;
				} else if (token.compareTo("multislot") == 0) {
					type = 4;
				} else if (token.compareTo("type") == 0) {
					type = 5;
				} else if (token.compareTo("allowed-classes") == 0) {
					type = 6;
				} else if (token.compareTo("value") == 0) {
					type = 7;
				} else if (token.compareTo("cardinality") == 0) {
					type = 8;
				} else if (token.compareTo("comment") == 0) {
					type = 9;
				} else if (token.compareTo("role") == 0) {
					type = 10;
				} else if (token.compareTo("default") == 0) {
					type = 11;
				}
				break;
			case 1: // class name
				className = token;
				lClass = new DOMClass();
				lClass.title = className;
				lClass.setRDFIdentifier(lClass.title);
				lClass.versionId = DMDocument.classVersionIdDefault;
				lClass.docSecType = className;
				lClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
				lClass.registeredByValue = DMDocument.registeredByValue;
				lClass.subModelId = subModelId;

				// get disposition for the class from MDPTNConfig
				boolean isInParsedClassMap = false;
				if (DMDocument.useMDPTNConfig) {
					classNameSpaceIdNC = "tbd";
					lClass.getDOMClassDisposition (true);
					if (lClass != null) {
						if (lClass.used.compareTo("Y") == 0) {
							classNameSpaceIdNC = lClass.nameSpaceIdNC;  // global needed for parser
							parsedClassMap.put(lClass.rdfIdentifier, lClass);
							isInParsedClassMap = true;
							String token1 = (String) tokenIter.next();
							if (token1.compareTo("(") != 0) {
								lClass.definition = DOMInfoModel.unEscapeProtegeString(token1);
							}
						} else if (lClass.used.compareTo("I") == 0) {
							// class is "hidden" ignore it and do not print warning

							// *** added ***
						/*	if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;   // omit %3ACLIPS_TOP_LEVEL_SLOT_CLASS
							lClass.isInactive = true;
							classNameSpaceIdNC = lClass.nameSpaceIdNC;  // global needed for parser
							parsedClassMap.put(lClass.rdfIdentifier, lClass);
							isInParsedClassMap = true;
							String token1 = (String) tokenIter.next();
							if (token1.compareTo("(") != 0) {
								lClass.definition = DOMInfoModel.unEscapeProtegeString(token1);
							} */
							// *** end of added ***
						
						} else {	// disposition exists but not clear why, print warning 
							if (lClass.identifier.compareTo("TBD_identifier") != 0) {
								DMDocument.registerMessage ("1>warning " + "Class omitted from build - Class Identifier:" + lClass.identifier);
							}

						/*  lClass.isInactive = true;
							classNameSpaceIdNC = lClass.nameSpaceIdNC;  // global needed for parser
							parsedClassMap.put(lClass.rdfIdentifier, lClass);
							isInParsedClassMap = true;
							String token1 = (String) tokenIter.next();
							if (token1.compareTo("(") != 0) {
								lClass.definition = DOMInfoModel.unEscapeProtegeString(token1);
							} */
							// *** end of added ***

						}
					} else {
						if (! DMDocument.LDDToolFlag) {
							DMDocument.registerMessage ("1>warning " + "Class disposition was not found - " + "<Record> <Field>Y</Field> <Field>UpperModel." + DMDocument.registrationAuthorityIdentifierValue + "." + lClass.title + "</Field> <Field>1M</Field> <Field>#nm</Field> <Field>ns</Field> </Record>"); 
						}
					}
					lClass.setIdentifier (classNameSpaceIdNC, className);
				}
				
				// get disposition for the class from dd11179
//				if (DMDocument.overWriteClass) {
				if (false) {
					lClass.getDOMClassDisposition2 ();
					if (! isInParsedClassMap) {
					parsedClassMap.put(lClass.rdfIdentifier, lClass);
					}
					String token1 = (String) tokenIter.next();
					if (token1.compareTo("(") != 0) {
						lClass.definition = DOMInfoModel.unEscapeProtegeString(token1);
					}
					lClass.setIdentifier (lClass.nameSpaceIdNC, className);
				}
				type = 0;
				break;
			case 2: // subClassOf
				lClass.subClassOfTitle = token;
				type = 0;
				break;
			case 3: // single-slot
				attrTitle = token;
				
				// new DOMProtAttr
				lProtAttr = new DOMProtAttr ();
				lProtAttr.subModelId = subModelId;
				lProtAttr.title = attrTitle;
//				lProtAttr.definition = definition;	This is set below after parsing comment.
//				lProtAttr.versionId = versionId; This is set from dictionary.
//				lProtAttr.sequenceId = sequenceId; This is set from dictionary.
//				lProtAttr.registrationStatus = registrationStatus; This is set from DMDocument.
				lProtAttr.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
				lProtAttr.classSteward = lClass.steward;
				lProtAttr.steward = lClass.steward;
				lProtAttr.setRDFIdentifier(lClass.title, lProtAttr.title);
				lProtAttr.parentClassTitle = className;
				lProtAttr.classNameSpaceIdNC = classNameSpaceIdNC;
				lProtAttr.nameSpaceIdNC = classNameSpaceIdNC;
				lProtAttr.nameSpaceId = lProtAttr.nameSpaceIdNC + ":";
				lProtAttr.setIdentifier (classNameSpaceIdNC, className, classNameSpaceIdNC, attrTitle);
				lProtAttr.cardMin = "0";
				lProtAttr.cardMinI = 0;
				lProtAttr.cardMax = "1";
				lProtAttr.cardMaxI = 1;
				lProtAttr.isPDS4 = true;
//				lProtAttr.isAttribute = ?;  This is set below after parsing type:Instance

				// add to dictionaries
				parsedProtAttrArr.add(lProtAttr);
				parsedProtAttrMap.put(lProtAttr.rdfIdentifier, lProtAttr);
				
				// link to class
				lClass.hasDOMProtAttr.add(lProtAttr);

				type = 0;
				break;
			case 4: // multislot
				attrTitle = token;
				
				// new DOMProtAttr
				lProtAttr = new DOMProtAttr ();
				lProtAttr.subModelId = subModelId;
				lProtAttr.title = attrTitle;
//				lProtAttr.definition = definition;	This is set below after parsing comment.
//				lProtAttr.versionId = versionId; This is set from dictionary.
//				lProtAttr.sequenceId = sequenceId; This is set from dictionary.
//				lProtAttr.registrationStatus = registrationStatus; This is set from DMDocument.
				lProtAttr.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
				lProtAttr.classSteward = lClass.steward;
				lProtAttr.steward = lClass.steward;
				lProtAttr.setRDFIdentifier(lClass.title, lProtAttr.title);
				lProtAttr.parentClassTitle = className;
				lProtAttr.classNameSpaceIdNC = classNameSpaceIdNC;
				lProtAttr.nameSpaceIdNC = classNameSpaceIdNC;
				lProtAttr.nameSpaceId = lProtAttr.nameSpaceIdNC + ":";
				lProtAttr.setIdentifier (classNameSpaceIdNC, className, classNameSpaceIdNC, attrTitle);
//				lProtAttr.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
				lProtAttr.cardMin = "0";
				lProtAttr.cardMinI = 0;
				lProtAttr.cardMax = "*";
				lProtAttr.cardMaxI = 9999999;
				lProtAttr.isPDS4 = true;
//				lProtAttr.isAttribute = ?;  This is set below after parsing type:Instance
				
				// add to dictionaries		
				parsedProtAttrArr.add(lProtAttr);
				parsedProtAttrMap.put(lProtAttr.rdfIdentifier, lProtAttr);
				
				// link to class
				lClass.hasDOMProtAttr.add(lProtAttr);
				
				type = 0;
				break;
			case 5: // type:Instance
				if (token.compareTo("INSTANCE") == 0) {
					lProtAttr.propType = "INSTANCE";
					lProtAttr.protValType = "CLASS";
					lProtAttr.isAttribute = false;
				} else {
					lProtAttr.propType = "ATTRIBUTE";
					lProtAttr.protValType = token.toLowerCase();
					lProtAttr.isAttribute = true;
					lProtAttr.isOwnedAttribute = true;
				}
				type = 0;
				break;
			case 6: // allowed-classes - all class titles are temporarily added to the valArr of the current attribute.
				if (token.compareTo(")") == 0) {
					type = 0;
				} else {
					if (token.indexOf("XSChoice%23") > -1) {
						lProtAttr.isChoice= true;
						lProtAttr.groupName = token;
					} else {
						lProtAttr.valArr.add(token);
						lProtAttr.isEnumerated = true;
					}
				}
				break;
			case 7: // values - all values are temporarily added to the valArr of the current attribute.
				if (token.compareTo(")") == 0) {
					type = 0;
				} else {
					if (token.indexOf("XSChoice#") > -1) {
						lProtAttr.isChoice= true;
						lProtAttr.groupName = token;
					} else {
						lProtAttr.valArr.add(token);
						lProtAttr.isEnumerated = true;
					}
				}
				break;
			case 8: // cardinality
				Integer ival = new Integer(token);
				lProtAttr.cardMin = token;
				lProtAttr.cardMinI = ival;
				String token2 = (String) tokenIter.next();
				if (token2.compareTo("?VARIABLE") == 0) {
					lProtAttr.cardMax = "*";
					lProtAttr.cardMaxI = 9999999;
				} else {
					ival = new Integer(token2);
					lProtAttr.cardMax = token2;
					lProtAttr.cardMaxI = ival;
				}
				type = 0;
				break;
			case 9: // comment
				lProtAttr.definition = DOMInfoModel.unEscapeProtegeString(token);
				type = 0;
				break;
			case 10: // role
				lClass.role = token;
//				lClass.isAbstract = false;
				if (lClass.role.compareTo("abstract") == 0) {
					lClass.isAbstract = true;					
				}
				type = 0;
				break;
			case 11: // default - XSChoice#
				if (token.compareTo(")") == 0) {
					type = 0;
				} else {
					if (token.indexOf("XSChoice#") > -1) {
						lProtAttr.isChoice= true;
						lProtAttr.groupName = token;
					} else if (token.indexOf("33") > -1) {
						lProtAttr.isChoice= true;
						lProtAttr.groupName = "XSChoice#" + token;
					}
				}
				type = 0;
				break;
			}
		}
	}
}
