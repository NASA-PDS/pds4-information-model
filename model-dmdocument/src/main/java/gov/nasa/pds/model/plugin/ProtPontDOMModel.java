package gov.nasa.pds.model.plugin; 
import java.util.*;

/**
 * Transforms a token array (parsed Protege .pont file) into logical entities.
 *   ProtPontDOMModel - get token array from parser and convert into classes and attributes
 */
//class ProtPontDOMModel extends InfoModel{
class ProtPontDOMModel extends DOMInfoModel{
	
	DOMClass lClass;
	DOMProtAttr lProtAttr;
	ArrayList <String> tokenArray;
	String attrTitle;
	String className;
	String classNameSpaceIdNC;
	TreeMap <String, String> masterClassDispo = new TreeMap <String, String> ();
	
	public ProtPontDOMModel () {
		parsedClassArr = new ArrayList <DOMClass> ();
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
				lClass.subModelId = subModelId;

				// get disposition for the class
				DOMClass lClassWDisp = DMDocument.getDOMClassDisposition (lClass, className, true);
				classNameSpaceIdNC = "tbd";
				if (lClassWDisp != null) {
					classNameSpaceIdNC = lClassWDisp.nameSpaceIdNC;  // global needed for parser
					parsedClassMap.put(lClassWDisp.rdfIdentifier, lClassWDisp);
					parsedClassArr.add(lClassWDisp);
					String token1 = (String) tokenIter.next();
					if (token1.compareTo("(") != 0) {
						lClassWDisp.definition = InfoModel.unEscapeProtegeString(token1);
					}
//					lClass.identifier = InfoModel.getClassIdentifier(classNameSpaceIdNC, className);
				} else {
//					lClass.identifier = InfoModel.getClassIdentifier(classNameSpaceIdNC, className);
					if (! DMDocument.LDDToolFlag) {
						System.out.println(">>warning - Class disposition was not found - " + "<Record> <Field>Y</Field> <Field>UpperModel." + DMDocument.registrationAuthorityIdentifierValue + "." + lClass.title + "</Field> <Field>1M</Field> <Field>#nm</Field> <Field>ns</Field> </Record>"); 
					}
				}
				lClass.setIdentifier (classNameSpaceIdNC, className);
				lClass.setNSTitle(lClass.nameSpaceIdNC, lClass.title);
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
				lProtAttr = resolveAttrNamespace (lProtAttr);
				lProtAttr.setNSTitle(lProtAttr.nameSpaceIdNC, lProtAttr.title);
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
				lProtAttr = resolveAttrNamespace (lProtAttr);
				lProtAttr.setNSTitle(lProtAttr.nameSpaceIdNC, lProtAttr.title);
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
				lProtAttr.definition = InfoModel.unEscapeProtegeString(token);
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
	
	/**
	 *   resolveAttrNamespace - temporary method to resolve attribute namespaces
	 *   Plan on using OWL version so that namespaces can be specified in Protege.
	 */
	private DOMProtAttr resolveAttrNamespace (DOMProtAttr lProtAttr) {
		String lNameSpaceIdNC = DOMInfoModel.attrNamespaceResolutionMap.get(lProtAttr.classNameSpaceIdNC + "." + lProtAttr.parentClassTitle + "." + lProtAttr.nameSpaceIdNC + "." + lProtAttr.title);
		if (lNameSpaceIdNC != null) {
			lProtAttr.nameSpaceIdNC = lNameSpaceIdNC;
			lProtAttr.nameSpaceId = lProtAttr.nameSpaceIdNC + ":";
			lProtAttr.setIdentifier (lProtAttr.classNameSpaceIdNC, lProtAttr.parentClassTitle, lProtAttr.nameSpaceIdNC, lProtAttr.title);
//			lAttr.set11179Attr(lAttr.identifier);
		}
		return lProtAttr;
	}
}
