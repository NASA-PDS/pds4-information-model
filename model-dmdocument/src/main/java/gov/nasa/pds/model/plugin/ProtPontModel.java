package gov.nasa.pds.model.plugin;
import java.util.*;

/**
 * Transforms a token array (parsed Protege .pont file) into logical entities (e.g. classes and attributes).
 *   getProtModel  - get token array from parser and convert into classes and attributes
 */
class ProtPontModel extends InfoModel{
	
	PDSObjDefn objClass;
	AttrDefn attrClass;
	ArrayList <String> tokenArray;
	String attrTitle;
	String className;
	String classNameSpaceIdNC;
	TreeMap <String, String> masterClassDispo = new TreeMap <String, String> ();
	
	public ProtPontModel () {
		objArr = new ArrayList <PDSObjDefn> ();
		objDict = new HashMap<String, PDSObjDefn> ();
		attrDict = new HashMap<String, AttrDefn>();
		className = "";
		classNameSpaceIdNC = "";
		
		// create the USER class - the root of all classes
		String title = "USER";
		String lClassRdfIdentifier = DMDocument.rdfPrefix + title + "." + getNextUId();
		className = title;
		classNameSpaceIdNC = "pds";

		objClass = new PDSObjDefn(lClassRdfIdentifier);
		objClass.title = title;
		objClass.docSecType = title;
		objClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
		objClass.subModelId = "ROOT";
		objDict.put(objClass.rdfIdentifier, objClass);
		InfoModel.user = objClass;
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
		objDict = new HashMap<String, PDSObjDefn>();
		
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
//				if (className.compareTo("%3ACLIPS_TOP_LEVEL_SLOT_CLASS") == 0) className = DMDocument.TopLevelAttrClassName;
				String lClassRdfIdentifier = DMDocument.rdfPrefix + className + "." + getNextUId();
				objClass = new PDSObjDefn(lClassRdfIdentifier);
//				objClass.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + className;
				objClass.title = className;
//				objClass.versionId = InfoModel.identifier_version_id;
				objClass.versionId = DMDocument.classVersionIdDefault;
				objClass.docSecType = className;
				objClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
				objClass.subModelId = subModelId;
				
				// get disposition identifier
				String lDispId = objClass.subModelId + "." + DMDocument.registrationAuthorityIdentifierValue + "." + className;
				DispDefn lDispDefn = DMDocument.masterClassDispoMap2.get(lDispId);
				if (lDispDefn != null) {
					objClass.section = lDispDefn.section;
					String lDisp = lDispDefn.disposition;
					String lStewardInd = lDisp.substring(0, 1);
					String lSteward = DMDocument.masterClassStewardMap.get(lStewardInd);
					objClass.steward = lSteward;
					classNameSpaceIdNC = lDispDefn.intNSId;
					objClass.nameSpaceIdNC = classNameSpaceIdNC;
					objClass.nameSpaceId = classNameSpaceIdNC + ":";
					objClass.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + classNameSpaceIdNC + "." + className;
					objClass.isMasterClass = true;
					if (lDisp.indexOf("V") > -1) {
						objClass.isVacuous = true;
					}
					if (lDisp.indexOf("S") > -1) {
						objClass.isSchema1Class = true;
					}
					if (lDisp.indexOf("R") > -1) {
						objClass.isRegistryClass = true;
					}
					if (lDisp.indexOf("T") > -1) {
						objClass.isTDO = true;
					}
					if (lDisp.indexOf("d") > -1) {
						objClass.isDataType = true;
					}
					if (lDisp.indexOf("u") > -1) {
						objClass.isUnitOfMeasure = true;
					}
					if (lDisp.indexOf("E") > -1) {
						objClass.versionId = "1.1.0.0";
					} else if (lDisp.indexOf("F") > -1) {
						objClass.versionId = "1.n.0.0";
					}
//					} else {
//						objClass.versionId = "1.1.0.0";
//					}

					objDict.put(objClass.rdfIdentifier, objClass);
					objArr.add(objClass);
//					System.out.println("\ndebug ProtPontModel objClass.rdfIdentifier:" + objClass.rdfIdentifier);					
					attrClass = new AttrDefn("TBD");
					String token1 = (String) tokenIter.next();
					if (token1.compareTo("(") != 0) {
						objClass.description = token1;
					}
				} else {
					objClass.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + "tbd" + "." + className;
					if (! DMDocument.LDDToolFlag) {
						System.out.println(">>warning - Class disposition was not found - " + "<Record> <Field>Y</Field> <Field>UpperModel.0001_NASA_PDS_1." + objClass.title + "</Field> <Field>1M</Field> <Field>#nm</Field> <Field>ns</Field> </Record>"); 
					}
				}	
				type = 0;
				break;
			case 2: // subClassOf -- these are converted to rdfIdentifiers by getSubClassOf
				objClass.subClassOfTitle = token;
				type = 0;
				break;
			case 3: // single-slot
				attrTitle = token;
				String luid1 = getNextUId();
				String lSsRdfIdentifier = DMDocument.rdfPrefix + objClass.title + "." + attrTitle + "." + luid1;			
				attrClass = new AttrDefn(lSsRdfIdentifier);
				attrClass.uid = luid1;
				attrClass.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + classNameSpaceIdNC + "." + className + "." + attrTitle;
				attrClass.set11179Attr(attrClass.identifier);
				attrClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
				attrClass.subModelId = subModelId;
				attrClass.cardMin = "0";
				attrClass.cardMinI = 0;
				attrClass.cardMax = "1";
				attrClass.cardMaxI = 1;
				attrClass.title = attrTitle;
				attrClass.className = className;
				attrClass.attrNameSpaceIdNC = classNameSpaceIdNC;
				attrClass.attrNameSpaceId = attrClass.attrNameSpaceIdNC + ":";
				attrClass.classSteward = objClass.steward;
				attrClass.steward = attrClass.classSteward;
				
				attrClass.classNameSpaceIdNC = classNameSpaceIdNC;
				attrClass.isPDS4 = true;
				objClass.hasSlot.add(attrClass);
				attrDict.put(attrClass.rdfIdentifier, attrClass);
//				System.out.println("debug ProtPontModel singleSlot attrClass.rdfIdentifier:" + attrClass.rdfIdentifier);
				type = 0;
				break;
			case 4: // multislot
				attrTitle = token;
				String luid2 = getNextUId();
				String lMsRdfIdentifier = DMDocument.rdfPrefix + objClass.title + "." + attrTitle + "." + luid2;
				attrClass = new AttrDefn(lMsRdfIdentifier);
				attrClass.uid = luid2;
				attrClass.identifier = DMDocument.registrationAuthorityIdentifierValue + "." + classNameSpaceIdNC + "." + className + "." + attrTitle;
				attrClass.set11179Attr(attrClass.identifier);
				attrClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
				attrClass.subModelId = subModelId;
				attrClass.cardMin = "0";
				attrClass.cardMinI = 0;
				attrClass.cardMax = "*";
				attrClass.cardMaxI = 9999999;
				attrClass.title = attrTitle;
				attrClass.attrNameSpaceIdNC = classNameSpaceIdNC;
				attrClass.attrNameSpaceId = attrClass.attrNameSpaceIdNC + ":";
				attrClass.classSteward = objClass.steward;
				attrClass.steward = attrClass.classSteward;
				
				attrClass.className = className;
				attrClass.classNameSpaceIdNC = classNameSpaceIdNC;
				attrClass.isPDS4 = true;
				objClass.hasSlot.add(attrClass);
				attrDict.put(attrClass.rdfIdentifier, attrClass);
//				System.out.println("debug ProtPontModel multiSlot attrClass.rdfIdentifier:" + attrClass.rdfIdentifier);
				type = 0;
				break;
			case 5: // type:Instance
				if (token.compareTo("INSTANCE") == 0) {
					attrClass.propType = "INSTANCE";
					attrClass.protValType = "CLASS";
					attrClass.isAttribute = false;
					objClass.ownedAssocTitle.add(attrClass.title);
					objClass.ownedAssocId.add(attrClass.identifier);
					objClass.ownedAssociation.add(attrClass);
				} else {
					attrClass.propType = "ATTRIBUTE";
					attrClass.protValType = token.toLowerCase();
					attrClass.isAttribute = true;
					attrClass.isOwnedAttribute = true;
					objClass.ownedAttrTitle.add(attrClass.title);
					objClass.ownedAttrId.add(attrClass.identifier);
					objClass.ownedAttribute.add(attrClass);
				}
				type = 0;
				break;
			case 6: // allowed-classes
				if (token.compareTo(")") == 0) {
					type = 0;
				} else {
					if (token.indexOf("XSChoice%23") > -1) {
						attrClass.isChoice= true;
					} else {
						attrClass.valArr.add(token);
						attrClass.isEnumerated = true;
					}
//					attrClass.valArr.add(token);
				}
				break;
			case 7: // values
				if (token.compareTo(")") == 0) {
					type = 0;
				} else {
					if (token.indexOf("XSChoice#") > -1) {
						attrClass.isChoice= true;
					} else {
						attrClass.valArr.add(token);
						attrClass.isEnumerated = true;
					}
//					System.out.println("debug 3             attrClass.valArr.add(token):" + token );
				}
				break;
			case 8: // cardinality
//				System.out.println("debug attrTitle:" + attrTitle);
				Integer ival = new Integer(token);
				attrClass.cardMin = token;
				attrClass.cardMinI = ival;
				String token2 = (String) tokenIter.next();
				if (token2.compareTo("?VARIABLE") == 0) {
					attrClass.cardMax = "*";
					attrClass.cardMaxI = 9999999;
				} else {
					ival = new Integer(token2);
					attrClass.cardMax = token2;
					attrClass.cardMaxI = ival;
				}
				type = 0;
				break;
			case 9: // comment
				attrClass.description = token;
				type = 0;
				break;
			case 10: // role
				objClass.role = token;
				objClass.isAbstract = false;
				if (objClass.role.compareTo("concrete") != 0) {
					objClass.isAbstract = true;					
				}
				type = 0;
				break;
			case 11: // default - formation rule
//				attrClass.format = token;
				type = 0;
				break;
			}
		}
	}
}
