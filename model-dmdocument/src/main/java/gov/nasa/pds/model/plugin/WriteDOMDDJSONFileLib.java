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
import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// import gov.nasa.pds.model.plugin.WriteDOMTermEntryJSON.TermEntryDefnGroup;

/**
 * Writes the PDS4 DOM DD content to a JSON file
 * 
 */

class WriteDOMDDJSONFileLib {

	ArrayList<String> adminRecUsedArr, adminRecTitleArr;

	int bnNum = 1;
	ArrayList <String> selectNamespaceArr = new ArrayList <> ();

	public WriteDOMDDJSONFileLib() {
		selectNamespaceArr.add(DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC);
		return;
	}
  
	// write the JSON file
	public void writeJSONFile(SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
		if (!DMDocument.LDDToolFlag) {	// write Common Dictionary to JSON file

			// get the level 0 JSON Object with name=null and value=empty JSON Array
			JSONArray jsonArrayLevel0 = (JSONArray) getJSONArrayLevel1 (lSchemaFileDefn);
			if (jsonArrayLevel0 != null) {

				// write the JSON object
				String lFileName = lSchemaFileDefn.relativeFileSpecDOMModelJSON;	
//				lFileName = lFileName.replace(".JSON", "_NEW2.JSON");
				writeJson(jsonArrayLevel0, lFileName);
			}

		} else {
			// add all LDD namespaces for this run
			selectNamespaceArr.addAll(DMDocument.LDDImportNameSpaceIdNCArr);

			// get the level 0 JSON Object with name=null and value=empty JSON Array
			JSONArray jsonArrayLevel0 = (JSONArray) getJSONArrayLevel1 (lSchemaFileDefn);
			//  	  	System.out.println("debug writeJSONFile LDD - jsonArrayLevel0:" + jsonArrayLevel0);
			if (jsonArrayLevel0 != null) {

				// write the JSON file for the LDD
				String lFileName = DMDocument.masterLDDSchemaFileDefn.relativeFileSpecDOMModelJSON;
//				lFileName = lFileName.replace(".JSON", "_NEW3.JSON");
				writeJson(jsonArrayLevel0, lFileName);
			}
		}
	}

	// get JSON Array with name=null and value= one JSON Object
	@SuppressWarnings("unchecked")
	private JSONArray getJSONArrayLevel1 (SchemaFileDefn lSchemaFileDefn) {
		JSONArray jsonArrayLevel1 = new JSONArray();
		jsonArrayLevel1.add(getJSONObjectLevel2 (lSchemaFileDefn));
		return jsonArrayLevel1;
	}
	
	// get JSON Object with name=dataDictionary and value=JSON Object dataDictionary
	private JSONObject getJSONObjectLevel2 (SchemaFileDefn lSchemaFileDefn) {

		// get the get the Data Dictionary
		JSONObject jsonObjectDataDictionary = (JSONObject) getNameValuePair ("dataDictionary", getValueDataDictionary (lSchemaFileDefn));
		return jsonObjectDataDictionary;
	}
	
	// get the JSON Object dataDictionary
	@SuppressWarnings("unchecked")
	private JSONObject getValueDataDictionary (SchemaFileDefn lSchemaFileDefn) {

		// put the the header information
		JSONObject jsonObjectDataDictionary = new JSONObject ();
		jsonObjectDataDictionary.put(formatValue("Title"), formatValue("PDS4 Data Dictionary"));
		jsonObjectDataDictionary.put(formatValue("IM Version"), formatValue(DMDocument.masterPDSSchemaFileDefn.ont_version_id));
		jsonObjectDataDictionary.put(formatValue("LDD Version"), formatValue(lSchemaFileDefn.ont_version_id));
		jsonObjectDataDictionary.put(formatValue("Date"), formatValue(DMDocument.masterTodaysDateTimeUTCFromInstant));
		jsonObjectDataDictionary.put(formatValue("Description"), formatValue("This document is a dump of the contents of the PDS4 Data Dictionary"));

		// get the namespace array
		JSONArray jsonArrayNamespaces = new JSONArray ();
		jsonArrayNamespaces.add(getNameValuePair("bn" + Integer.toString(bnNum++), formatValue("pds:"))); // always include common (pds)
	    for (SchemaFileDefn lSchemaFileDefnVal : DMDocument.LDDSchemaFileSortArr) {	    	
			jsonArrayNamespaces.add(getNameValuePair ("bn" + Integer.toString(bnNum++), formatValue(lSchemaFileDefnVal.nameSpaceId)));
	    }
	    
		jsonObjectDataDictionary.put(formatValue("namespaces"), jsonArrayNamespaces);

		// get the classDictionary
		jsonObjectDataDictionary.put(formatValue("classDictionary"), (Object) getValueClassDictionary());

		// get the attributeDictionary
		jsonObjectDataDictionary.put(formatValue("attributeDictionary"), (Object) getValueAttributeDictionary());

		// get the dataTypeDictionary
		jsonObjectDataDictionary.put(formatValue("dataTypeDictionary"), (Object) getValueDataTypeDictionary());

		// get the UnitDictionary
		jsonObjectDataDictionary.put(formatValue("UnitDictionary"), (Object) getValueUnitDictionary());

		// get the Property Maps
		ArrayList<PropertyMapsDefn> selectedPropMapArr = getSelectedPropMapsArr(lSchemaFileDefn.nameSpaceIdNC);
		if (! selectedPropMapArr.isEmpty())
			jsonObjectDataDictionary.put(formatValue("PropertyMapDictionary"), (Object) getValuePropertyMapDictionary(selectedPropMapArr));

		// get Terminological Entry - External to PDS4 - e.g. EPN =======
		if (DMDocument.LDDToolFlag) {
			TermEntryDefnGroupMapStruct termEntryDefnGroupMapStruct = getTermEntryDefnGroupMapStruct ();
			jsonObjectDataDictionary.put(formatValue("ExternTermMapDictionary"), getValueJSONObjectExternTermMapDictionary (termEntryDefnGroupMapStruct));
		}

		/* option -5 is no longer working -- query model is in abeyance until needed.
	    // Write Terminological Entry - Query Model
	    ArrayList<DOMClass> lSelectedTEClassArr = new ArrayList<>();
	    for (DOMClass lDOMClass : DOMInfoModel.masterDOMClassArr) {
	      if (lDOMClass.isInactive || !lDOMClass.isQueryModel) continue;
	      lSelectedTEClassArr.add(lDOMClass);
	    }

	    if (lSelectedTEClassArr.size() > 0) {
	      getTermEntriesQM(lSelectedTEClassArr);
	    } */
		
		return jsonObjectDataDictionary;
	}
	
	// ======= get selected entities =======
	
	// 
	private ArrayList<PropertyMapsDefn> getSelectedPropMapsArr(String nameSpaceIdNC) {
		ArrayList<PropertyMapsDefn> selectedPropMapsArr = new ArrayList<> ();
	    for (PropertyMapsDefn propMaps : DOMInfoModel.masterPropertyMapsArr) {
	        if (nameSpaceIdNC.compareTo(propMaps.namespace_id) == 0) {
	        	selectedPropMapsArr.add(propMaps);
	        }
	    }
	    return selectedPropMapsArr;
	}
	
	// ======= classDictionary =======
	
	// get the value for the JSONObject classDictionary
	@SuppressWarnings("unchecked")
	private JSONArray getValueClassDictionary () {

		// create the classDictionary JSON Array
		JSONArray jsonArrayClassDictionary = new JSONArray ();

		// scan the Classes; get object class
		for (DOMClass lSelectedClass : DOMInfoModel.masterDOMClassArr) {
			if (lSelectedClass.isInactive) continue;
			if (lSelectedClass.title.indexOf("PDS3") > -1) continue;
			if (!  selectNamespaceArr.contains(lSelectedClass.nameSpaceIdNC)) continue;
			
			JSONObject jsonObjectClass = (JSONObject) getNameValuePair ("class", getValueJSONObjectClass (lSelectedClass));
			jsonArrayClassDictionary.add(jsonObjectClass);
		}
		return jsonArrayClassDictionary;
	}
	
	// get the JSON Object - Class
	@SuppressWarnings("unchecked")
	public JSONObject getValueJSONObjectClass(DOMClass lClass) {
		JSONObject jsonObjectClass = new JSONObject ();
		jsonObjectClass.put(formatValue("identifier"), formatValue(lClass.identifier));
		jsonObjectClass.put(formatValue("title"), formatValue(lClass.title));
		jsonObjectClass.put(formatValue("registrationAuthorityId"), formatValue(DMDocument.registrationAuthorityIdentifierValue));
		jsonObjectClass.put(formatValue("nameSpaceId"), formatValue(lClass.nameSpaceIdNC));
		jsonObjectClass.put(formatValue("steward"), formatValue(lClass.steward));
		jsonObjectClass.put(formatValue("versionId"), formatValue(lClass.versionId));
		jsonObjectClass.put(formatValue("isAbstract"), formatBooleanValue(lClass.isAbstract));
		jsonObjectClass.put(formatValue("isDeprecated"), formatBooleanValue(lClass.isDeprecated));
		jsonObjectClass.put(formatValue("description"), formatValue(lClass.definition));

		// get the associationList array
		if (! lClass.allAttrAssocArr.isEmpty()) {

			// get the get the associationList JSONObject
			jsonObjectClass.put("associationList", getValueAssociationList (lClass));

		}
		return jsonObjectClass;
	}
	  
	// get the value for the AssociationList
	@SuppressWarnings("unchecked")
	public JSONArray getValueAssociationList (DOMClass lClass) {

		// sort the Associations into classes and attributes
		ArrayList<DOMPropGroup> lDOMPropGroupArr = getSortedAssociationsClassAndAttributes (lClass);
		
		// create the association list
		JSONArray jsonAssociationList = new JSONArray ();
		
		// add the super class association, if any
	    if (lClass.subClassOf != null) {
	      DOMClass lSuperClass = lClass.subClassOf;
	      if (!((lSuperClass == null) || lSuperClass.isAbstract || lSuperClass.isVacuous
	          || (lSuperClass.title.compareTo("USER") == 0))) {
	    	  
	    	  // add the superclass
	    	  jsonAssociationList.add(getJSONObjectSuperClassAssoc(lSuperClass,lClass));
	      }
	    }
		
		// scan the associations array and add each association to the association list		
		for (DOMPropGroup lDOMPropGroup : lDOMPropGroupArr) {
			JSONObject jsonObjectAssociation = (JSONObject) getNameValuePair ("association", getValueJSONObjectAssociation (lDOMPropGroup));
			jsonAssociationList.add(jsonObjectAssociation);
		}
		return jsonAssociationList;
	}
	  
	  
	// get the associationList array
	@SuppressWarnings("unchecked")
	public JSONObject getValueJSONObjectAssociation (DOMPropGroup lDOMPropGroup) {
		
		DOMProp lDOMProp = lDOMPropGroup.domProp;
		JSONObject jsonObjectAssoc = new JSONObject ();
		jsonObjectAssoc.put(formatValue("identifier"), formatValue(lDOMProp.identifier));
		jsonObjectAssoc.put(formatValue("title"), formatValue(lDOMProp.title));
		jsonObjectAssoc.put(formatValue("isChoice"), formatBooleanValue(lDOMProp.isChoice));
		jsonObjectAssoc.put(formatValue("isAny"), formatBooleanValue(lDOMProp.isAny));
		jsonObjectAssoc.put(formatValue("groupName"), formatValue(lDOMProp.groupName));
		jsonObjectAssoc.put(formatValue("minimumCardinality"), formatValue(lDOMProp.cardMin));
		jsonObjectAssoc.put(formatValue("maximumCardinality"), formatValue(lDOMProp.cardMax));
		jsonObjectAssoc.put(formatValue("classOrder"), formatValue(lDOMProp.classOrder));
		if (lDOMProp.isAttribute) {
			jsonObjectAssoc.put(formatValue("assocType"), formatValue("attribute_of"));
			jsonObjectAssoc.put(formatValue("isAttribute"), formatBooleanValue(true));
			
			// get component list, the attributes identifiers
			jsonObjectAssoc.put(null, getValueClassList (lDOMPropGroup));
		} else {
			jsonObjectAssoc.put(formatValue("assocType"), formatValue("component_of"));
			jsonObjectAssoc.put(formatValue("isAttribute"), formatBooleanValue(false));
			
			// get component list, the classes identifiers
			jsonObjectAssoc.put(null, getValueClassList (lDOMPropGroup));
		}
		return jsonObjectAssoc;
	}	
	
	  
	// get the value for the class list name/value pair
	@SuppressWarnings("unchecked")
	public JSONArray getValueClassList (DOMPropGroup lDOMPropGroup) {

		// scan the associations array and create the association list
		JSONArray jsonAssociationList = new JSONArray ();		
		for (ISOClassOAIS11179 lISOClass : lDOMPropGroup.domObjectArr) {
			jsonAssociationList.add(formatValue(lISOClass.identifier));
		}
		return jsonAssociationList;
	}
	  
	// sort the associations on class and attribute 
	public ArrayList<DOMPropGroup> getSortedAssociationsClassAndAttributes (DOMClass lClass) {

		// sort by classOrder
		TreeMap<String, DOMPropGroup> lDOMPropGroupMap = new TreeMap<>();
		DOMPropGroup lDOMPropGroupSort = new DOMPropGroup();
		String pGroupName = "NULL";
		for (DOMProp lDOMProp : lClass.allAttrAssocArr) {
			ISOClassOAIS11179 lDOMObject = lDOMProp.hasDOMObject;
			String lPropType = "C";
			if (lDOMProp.isAttribute) lPropType = "A";
			String lSortOrder = lPropType + lDOMProp.classOrder + "-" + lDOMObject.identifier;
			if ((lDOMProp.groupName.indexOf("TBD") == 0)
					|| (lDOMProp.groupName.compareTo(pGroupName) != 0)) {

				if (lDOMProp.groupName.indexOf("TBD") == 0) pGroupName = "NULL";
				else pGroupName = lDOMProp.groupName;

				lDOMPropGroupSort = new DOMPropGroup();
				lDOMPropGroupSort.domProp = lDOMProp;
				lDOMPropGroupSort.domObjectArr.add(lDOMObject);
				lDOMPropGroupMap.put(lSortOrder, lDOMPropGroupSort);
			} else {
				lDOMPropGroupSort.domObjectArr.add(lDOMObject);
			}
		}
		ArrayList<DOMPropGroup> lDOMPropGroupArr = new ArrayList<>(lDOMPropGroupMap.values());
		return lDOMPropGroupArr;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONObjectSuperClassAssoc(DOMClass lSuperClass, DOMClass lClass) {
	    
	    // get SuperClass jsonObject with Identifier in a jsonArray
	    JSONObject jsonObjectSuperClass = new JSONObject ();
	    jsonObjectSuperClass.put(formatValue("identifier"), formatValue(lClass.identifier + "." + lSuperClass.title + ".generalization"));
	    jsonObjectSuperClass.put(formatValue("title"), formatValue(lSuperClass.title));
	    jsonObjectSuperClass.put(formatValue("assocType"), formatValue("parent_of"));
	    jsonObjectSuperClass.put(formatValue("isAttribute"), "false");
	    jsonObjectSuperClass.put(formatValue("isChoice"), "false");
	    jsonObjectSuperClass.put(formatValue("isAny"), "false");
	    jsonObjectSuperClass.put(formatValue("groupName"), "null");
	    jsonObjectSuperClass.put(formatValue("minimumCardinality"), "1");
	    jsonObjectSuperClass.put(formatValue("maximumCardinality"), "1");
	    jsonObjectSuperClass.put(formatValue("classOrder"), "0000");
	    
	    // get the super class identifier
	    JSONObject jsonObjectSuperClassIdentifier = new JSONObject ();									// jsonObject for Super Class Identifer
	    jsonObjectSuperClassIdentifier.put ("bn" + Integer.toString(bnNum++), formatValue(lSuperClass.identifier));

	    // get the super class identifier, as a member of a JSON array
	    JSONArray jsonArrayValueSuperClassIdentifier = new JSONArray ();		 						// jsonArray for Super Class Identifer
		jsonArrayValueSuperClassIdentifier.add(jsonObjectSuperClassIdentifier);
		
		// add the attributeId JSONArray
	    jsonObjectSuperClass.put(formatValue("attributeId"), jsonArrayValueSuperClassIdentifier);
	    
	    // get the "association" jsonObject
	    JSONObject jsonObjectAssociation = new JSONObject ();
	    jsonObjectAssociation.put("association", jsonObjectSuperClass);
	    
	    // return jsonObjectAssociation;
	    return jsonObjectAssociation;
	  }
	
	// ======= attributeDictionary =======
	
	// get the value for the JSONObject attributeDictionary
	@SuppressWarnings("unchecked")
	private JSONArray getValueAttributeDictionary () {

		// create the attributeDictionary JSON Array
		JSONArray jsonArrayAttrDictionary = new JSONArray ();

		// scan the Attributes
		for (DOMAttr lDOMAttr : DOMInfoModel.masterDOMAttrArr) {
			if (!(lDOMAttr.isUsedInClass && lDOMAttr.isAttribute)) continue;
			if (! selectNamespaceArr.contains(lDOMAttr.nameSpaceIdNC)) continue;

			// add to the Class Dictionary
			jsonArrayAttrDictionary.add((JSONObject) getNameValuePair ("attribute", getValueJSONObjectAttr (lDOMAttr)));
		}
		return jsonArrayAttrDictionary;
	}
	
	// get the JSON Object - Attribute
	@SuppressWarnings("unchecked")
	public JSONObject getValueJSONObjectAttr(DOMAttr lDOMAttr) {
		JSONObject jsonObjectClass = new JSONObject ();
		jsonObjectClass.put(formatValue("identifier"), formatValue(lDOMAttr.identifier));
		jsonObjectClass.put(formatValue("title"), formatValue(lDOMAttr.title));
		jsonObjectClass.put(formatValue("registrationAuthorityId"), formatValue(DMDocument.registrationAuthorityIdentifierValue));
		jsonObjectClass.put(formatValue("nameSpaceId"), formatValue(lDOMAttr.nameSpaceIdNC));
		jsonObjectClass.put(formatValue("steward"), formatValue(lDOMAttr.steward));
		jsonObjectClass.put(formatValue("versionId"), formatValue(lDOMAttr.versionIdentifierValue));
		jsonObjectClass.put(formatValue("description"), formatValue(lDOMAttr.definition));
		jsonObjectClass.put(formatValue("isNillable"), formatBooleanValue(lDOMAttr.isNilable));
		jsonObjectClass.put(formatValue("isEnumerated"), formatBooleanValue(lDOMAttr.isEnumerated));
		jsonObjectClass.put(formatValue("isDeprecated"), formatBooleanValue(lDOMAttr.isDeprecated));
		jsonObjectClass.put(formatValue("dataType"), formatValue(lDOMAttr.valueType));
		jsonObjectClass.put(formatValue("dataTypeId"), formatValue(lDOMAttr.getValueTypeIdentifier()));
		jsonObjectClass.put(formatValue("minimumCharacters"), formatValue(lDOMAttr.getMinimumCharacters(true, true)));
		jsonObjectClass.put(formatValue("maximumCharacters"), formatValue(lDOMAttr.getMaximumCharacters(true, true)));
		jsonObjectClass.put(formatValue("minimumValue"), formatValue(lDOMAttr.getMinimumValue(true, true)));
		jsonObjectClass.put(formatValue("maximumValue"), formatValue(lDOMAttr.getMaximumValue(true, true)));
		jsonObjectClass.put(formatValue("pattern"), formatValue(lDOMAttr.getPattern(true)));
		jsonObjectClass.put(formatValue("unitOfMeasure"), formatValue(lDOMAttr.getUnitOfMeasure(false)));
		jsonObjectClass.put(formatValue("unitOfMeasureId"), formatValue(lDOMAttr.getUnitOfMeasureIdentifier()));
		jsonObjectClass.put(formatValue("unitId"), formatValue(lDOMAttr.getUnits(false)));
		jsonObjectClass.put(formatValue("defaultUnitId"), formatValue(lDOMAttr.getDefaultUnitId(false)));

		// get the associationList array
		if (! lDOMAttr.domPermValueArr.isEmpty()) {

			// get the get the associationList JSONObject
			jsonObjectClass.put("PermissibleValueList", getValuePermissibleValueList (lDOMAttr));
		}
		return jsonObjectClass;
	}
	  
	// get the value for the PermissibleValueList
	@SuppressWarnings("unchecked")
	public JSONArray getValuePermissibleValueList (DOMAttr lDOMAttr) {
		
		// scan the PermissibleValue and create the array
		JSONArray jsonPermissibleValueList = new JSONArray ();
		for (DOMProp lDOMProp : lDOMAttr.domPermValueArr) {
			if ((lDOMProp.hasDOMObject == null) || !(lDOMProp.hasDOMObject instanceof DOMPermValDefn)) continue;
			DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
			JSONObject jsonObjectPermissibleValue = (JSONObject) getNameValuePair ("PermissibleValue", getValuePermissibleValue (lDOMPermValDefn ));
			jsonPermissibleValueList.add(jsonObjectPermissibleValue);
		}
		return jsonPermissibleValueList;
	}
	  
	// get the getValuePermissibleValue
	@SuppressWarnings("unchecked")
	public JSONObject getValuePermissibleValue (DOMPermValDefn lDOMPermValDefn) {
		JSONObject jsonObjectPermVal = new JSONObject ();
		jsonObjectPermVal.put(formatValue("value"), formatValue(lDOMPermValDefn.value));
		jsonObjectPermVal.put(formatValue("valueMeaning"), formatValue(lDOMPermValDefn.value_meaning));
		jsonObjectPermVal.put(formatValue("isDeprecated"), formatBooleanValue(lDOMPermValDefn.isDeprecated));
		return jsonObjectPermVal;
	}
	
	// ======= DataTypeDictionary =======

	// get the value for the JSONObject DataTypeDictionary
	@SuppressWarnings("unchecked")
	private JSONArray getValueDataTypeDictionary () {
		
		// create the DataTypeDictionary JSON Array
		JSONArray jsonArrayDataTypeDictionary = new JSONArray ();
		
		// scan the Data Types
		for (DOMDataType lDOMDataType : DOMInfoModel.masterDOMDataTypeArr) {
			if (lDOMDataType.isInactive) continue;
			if (! selectNamespaceArr.contains(lDOMDataType.nameSpaceIdNC)) continue;
			
			// get the nesting JSONObject for the classDictionary
			JSONObject jsonObjectDataType = (JSONObject) getNameValuePair ("DataType", getValueJSONObjectDataType (lDOMDataType));

			// add to the Class Dictionary
			jsonArrayDataTypeDictionary.add(jsonObjectDataType);
		}
		return jsonArrayDataTypeDictionary;
	}
	
	// get the JSON Object - Data Type
	@SuppressWarnings("unchecked")
	public JSONObject getValueJSONObjectDataType(DOMDataType lDOMDataType) {
		JSONObject jsonObjectDataType = new JSONObject ();
		jsonObjectDataType.put(formatValue("identifier"), formatValue(lDOMDataType.identifier));
		jsonObjectDataType.put(formatValue("title"), formatValue(lDOMDataType.title));
		jsonObjectDataType.put(formatValue("nameSpaceId"), formatValue(lDOMDataType.nameSpaceIdNC));
		jsonObjectDataType.put(formatValue("registrationAuthorityId"), formatValue(DMDocument.registrationAuthorityIdentifierValue));
		jsonObjectDataType.put(formatValue("minimumCharacters"), formatValue(lDOMDataType.getMinimumCharacters(true)));
		jsonObjectDataType.put(formatValue("maximumCharacters"), formatValue(lDOMDataType.getMaximumCharacters(true)));
		jsonObjectDataType.put(formatValue("minimumValue"), formatValue(lDOMDataType.getMinimumValue(true)));
		jsonObjectDataType.put(formatValue("maximumValue"), formatValue(lDOMDataType.getMaximumValue(true)));
		return jsonObjectDataType;
	  }
	
	// ======= unitDictionary =======

	// get the value for unitDictionary, a JSONArray
	@SuppressWarnings("unchecked")
	private JSONArray getValueUnitDictionary () {

		// create the unitDictionary JSON Array
		JSONArray jsonArrayUnitDictionary = new JSONArray ();
		
		// scan the Data Types
		for (DOMUnit lDOMUnit : DOMInfoModel.masterDOMUnitArr) {
			if (lDOMUnit.isInactive) continue;
			if (! selectNamespaceArr.contains(lDOMUnit.nameSpaceIdNC)) continue;
			
			// get the nesting JSONObject for the classDictionary
			JSONObject jsonObjectUnitOfMeasure = (JSONObject) getNameValuePair ("Unit", getValueJSONObjectUnit (lDOMUnit));

			// add to the Class Dictionary
			jsonArrayUnitDictionary.add(jsonObjectUnitOfMeasure);
		}
		return jsonArrayUnitDictionary;
	}

	// get the JSON Object Unit
	@SuppressWarnings("unchecked")
	public JSONObject getValueJSONObjectUnit(DOMUnit lDOMUnit) {
		JSONObject jsonObjectUnit = new JSONObject ();
		jsonObjectUnit.put(formatValue("identifier"), formatValue(lDOMUnit.pds4Identifier));
		jsonObjectUnit.put(formatValue("title"), formatValue(lDOMUnit.title));
		jsonObjectUnit.put(formatValue("nameSpaceId"), formatValue(DMDocument.masterNameSpaceIdNCLC));
		jsonObjectUnit.put(formatValue("registrationAuthorityId"), formatValue(DMDocument.registrationAuthorityIdentifierValue));
		jsonObjectUnit.put(formatValue("defaultUnitId"), formatValue(lDOMUnit.default_unit_id));
		if (lDOMUnit.unit_id.isEmpty()) return jsonObjectUnit;
		jsonObjectUnit.put(formatValue("unitIdList"), getValueUnitIdList (lDOMUnit.unit_id));
		
		return jsonObjectUnit;
	}
	
	// ------- UnitId List-------
	// get the value for UnitIdlist
	@SuppressWarnings("unchecked")
	public JSONArray getValueUnitIdList(ArrayList <String> unitIdArr) {
		
		// create the UnitId JSON Array
		JSONArray jsonArrayUnitIdDictionary = new JSONArray ();
		for (String unitId : unitIdArr) {
			JSONObject jsonObjectUnitID = new JSONObject ();
			jsonObjectUnitID.put(formatValue("value"), formatValue(unitId));
			jsonObjectUnitID.put(formatValue("valueMeaning"), formatValue("TBD"));
			JSONObject jsonObjectNexting = new JSONObject ();
			jsonObjectNexting.put(formatValue("UnitId"), jsonObjectUnitID);
			jsonArrayUnitIdDictionary.add(jsonObjectNexting);
		}
		return jsonArrayUnitIdDictionary;
	}
	
	
	// ======= Terminological Entry - External to PDS4 - e.g. EPN =======
	
	// get the TermEntryDefnGroupMapStructs - find the term mappings and create structure
	private TermEntryDefnGroupMapStruct getTermEntryDefnGroupMapStruct () {
		// map of Term Entry Definitions index by class and attribute identifier
		TermEntryDefnGroupMapStruct termEntryDefnGroupMapStruct = new TermEntryDefnGroupMapStruct ();

        // scan all the classes for terminological entries at the attribute level
		for (DOMClass lDOMClass : DOMInfoModel.masterDOMClassArr) {
			
			// skip inactive
			if (lDOMClass.isInactive) continue;
			
			// scan through all owned attributes of the class
			for (DOMProp lDOMProp : lDOMClass.ownedAttrArr) {
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
					
					// skip attribute with no terminological entry map
					if (lDOMAttr.termEntryMap == null || lDOMAttr.termEntryMap.isEmpty() ) continue;
					
					// found a terminological entry map; create an array of Term Entry Definition Groups
					boolean isFirst = true;
					for (TermEntryDefn lTermEntryDefn : lDOMAttr.termEntryMap.values()) {
						String lKey = lDOMAttr.identifier + "_" + lTermEntryDefn.name + "_" + DOMInfoModel.getNextUId();
						TermEntryDefnGroup termEntryDefnGroup = new TermEntryDefnGroup ();
						if (isFirst) {
							isFirst = false;
							termEntryDefnGroupMapStruct.lddName = lTermEntryDefn.lddName; // needed only once
							termEntryDefnGroupMapStruct.lddVersion = lTermEntryDefn.lddVersion;
						}
						termEntryDefnGroup.identifier = lKey;
						termEntryDefnGroup.attrId = lDOMAttr.nameSpaceId + lDOMAttr.title;
						termEntryDefnGroup.termEntryDefn = lTermEntryDefn;
						termEntryDefnGroupMapStruct.termEntryDefnGroupMap.put(lKey, termEntryDefnGroup);
					}
				}
			}
		}
		//  return termEntryDefnMap;
       return termEntryDefnGroupMapStruct;
	}
	
	
	// get the term mappings as a JSON object
	@SuppressWarnings("unchecked")
	private JSONArray getValueJSONObjectExternTermMapDictionary (TermEntryDefnGroupMapStruct termEntryDefnGroupMapStruct) {
		// create the JSON object
		JSONObject jsonObjectRoot = new JSONObject ();
		jsonObjectRoot.put("datetime", DMDocument.masterTodaysDateTimeUTCFromInstant);
		jsonObjectRoot.put("infoModelVersionId", DMDocument.infoModelVersionId);
		jsonObjectRoot.put("title", "PDS4 Term Mappings");
		jsonObjectRoot.put("lddName", termEntryDefnGroupMapStruct.lddName);
		jsonObjectRoot.put("lddVersion", termEntryDefnGroupMapStruct.lddVersion);

		// create array of one element {from, to} arrays
		JSONArray jsonTermMapElementArrArr = new JSONArray();
		
		// scan over the term entry definitions
		for (TermEntryDefnGroup termEntryDefnGroup : termEntryDefnGroupMapStruct.termEntryDefnGroupMap.values()) {
			
			// get the term map elements
					
			// create a one {from, to} array
			JSONArray jsonFromToArr = new JSONArray();

			// get the From (new) term
			JSONObject jsonFromObject = new JSONObject ();
			String fromInstanceId = DOMInfoModel.escapeXMLChar(termEntryDefnGroup.termEntryDefn.fromInstanceId);
			jsonFromObject.put("from", fromInstanceId);
			jsonFromToArr.add(jsonFromObject);
			
			// get the To (PDS4) term
			JSONObject jsonToObject = new JSONObject ();
			String toInstanceId = DOMInfoModel.escapeXMLChar(termEntryDefnGroup.termEntryDefn.toInstanceId);
			jsonToObject.put("to", toInstanceId);
			jsonFromToArr.add(jsonToObject);
			
			// get the SKOS semanticRelation
			JSONObject jsonSKOSObject = new JSONObject ();
			String semanticRelation = DOMInfoModel.escapeXMLChar(termEntryDefnGroup.termEntryDefn.semanticRelation);
			jsonSKOSObject.put("skos", semanticRelation);
			jsonFromToArr.add(jsonSKOSObject);
			
			// get the description of the mapping
			JSONObject jsonDefinitionObject = new JSONObject ();
			String definition = DOMInfoModel.escapeXMLChar(termEntryDefnGroup.termEntryDefn.definition);
			jsonDefinitionObject.put("defn", definition);
			jsonFromToArr.add(jsonDefinitionObject);
			
			// create one {from, to} enclosing object
			JSONObject jsonFromToObj = new JSONObject ();
			jsonFromToObj.put("termmap", jsonFromToArr);
			
			// add one {from, to} enclosing object 
			jsonTermMapElementArrArr.add(jsonFromToObj);
		}
		jsonObjectRoot.put("termmaps", jsonTermMapElementArrArr);
		
		// create the ExternTermMapDictionary, a JSONArray
		JSONArray jsonArrayExternTermMapDictionary = new JSONArray ();
		jsonArrayExternTermMapDictionary.add(jsonObjectRoot);
		
        return jsonArrayExternTermMapDictionary;
	}
    
    // TermEntryDefn Group Map - structure for sorting termEntryDefns
	private static class TermEntryDefnGroupMapStruct {
		String lddName;
		String lddVersion;
		TreeMap <String, TermEntryDefnGroup> termEntryDefnGroupMap = new TreeMap <String, TermEntryDefnGroup> ();
	}
    
    // structure for sorting termEntryDefns
	private static class TermEntryDefnGroup {
		String identifier;
		String attrId;
		TermEntryDefn termEntryDefn;
	}

	
	// ======= Property Maps =======
	
	// get the value for PropertyMapDictionary, a JSONArray
	@SuppressWarnings("unchecked")
	private JSONArray getValuePropertyMapDictionary (ArrayList<PropertyMapsDefn> selectedPropMapArr) {

		// create the PropertyMapDictionary, a JSONArray
		JSONArray jsonArrayPropertyMapDictionary = new JSONArray ();
		
		// scan the Data Types
		for (PropertyMapsDefn lPropertyMaps : selectedPropMapArr) {
		    if (lPropertyMaps.propertyMapArr.isEmpty()) continue;	    
			
			// get PropertyMaps, a JSONObject
			JSONObject jsonObjectPropertyMaps = new JSONObject ();
			jsonObjectPropertyMaps.put("PropertyMaps", getValuePropertyMaps(lPropertyMaps));
			jsonArrayPropertyMapDictionary.add(jsonObjectPropertyMaps);
		}
		return jsonArrayPropertyMapDictionary;
	}
	
	// get the Property Maps
	@SuppressWarnings("unchecked")
	public JSONObject getValuePropertyMaps(PropertyMapsDefn lPropertyMaps) {

		// create the Property Maps JSONObject
    	JSONObject jsonObjectPropertyMap = new JSONObject ();
    	jsonObjectPropertyMap.put(formatValue("identifier"), formatValue(lPropertyMaps.rdfIdentifier));
    	jsonObjectPropertyMap.put(formatValue("title"), formatValue(lPropertyMaps.title));
    	jsonObjectPropertyMap.put(formatValue("namespace_id"), formatValue(lPropertyMaps.namespace_id));
    	jsonObjectPropertyMap.put(formatValue("description"), formatValue(lPropertyMaps.description));
    	jsonObjectPropertyMap.put(formatValue("external_property_map_id"), formatValue(lPropertyMaps.external_property_map_id));
	    if (! lPropertyMaps.propertyMapArr.isEmpty())
	    	jsonObjectPropertyMap.put(formatValue("propertyMapList"), getValuePropertyMapList (lPropertyMaps));
		return jsonObjectPropertyMap;
	}
	
	// get the PropertyMapList
	@SuppressWarnings("unchecked")
	public JSONArray getValuePropertyMapList(PropertyMapsDefn lPropertyMaps) {
		
		// create the Property Maps JSON Array
		JSONArray jsonArrayValuePropertyMapList = new JSONArray ();		 
	    for (PropertyMapDefn lPropertyMap : lPropertyMaps.propertyMapArr) {
	    	JSONObject jsonObjectPropertyMap = new JSONObject ();
	    	jsonObjectPropertyMap.put(formatValue("identifier"), formatValue(lPropertyMap.identifier));
	    	jsonObjectPropertyMap.put(formatValue("title"), formatValue(lPropertyMap.title));
	    	jsonObjectPropertyMap.put(formatValue("model_object_id"), formatValue(lPropertyMap.model_object_id));
	    	jsonObjectPropertyMap.put(formatValue("model_object_type"), formatValue(lPropertyMap.model_object_type));
	    	jsonObjectPropertyMap.put(formatValue("instance_id"), formatValue(lPropertyMap.instance_id));
	    	jsonObjectPropertyMap.put(formatValue("external_namespace_id"), formatValue(lPropertyMap.external_namespace_id));
	    	jsonObjectPropertyMap.put(formatValue("description"), formatValue(lPropertyMap.description));
		    if (! lPropertyMap.propertyMapEntryArr.isEmpty())
		    	jsonObjectPropertyMap.put(formatValue("propertyMapEntryList"), getValuePropertyMapEntryList (lPropertyMap));
		    jsonArrayValuePropertyMapList.add(jsonObjectPropertyMap);
	    }
		return jsonArrayValuePropertyMapList;
	}

	// get the PropertyMapEntryList
	@SuppressWarnings("unchecked")
	public JSONArray getValuePropertyMapEntryList(PropertyMapDefn lPropertyMap) {
		
		// create the Property Maps JSON Array
		JSONArray jsonArrayValuePropertyMapEntryList = new JSONArray ();		
	    for (PropertyMapEntryDefn  lPropertyMapEntry : lPropertyMap.propertyMapEntryArr) {
	    	JSONObject jsonObjectPropertyMapEntry = new JSONObject ();
			jsonObjectPropertyMapEntry.put("property_name", formatValue(lPropertyMapEntry.property_name));
			jsonObjectPropertyMapEntry.put("property_value", formatValue(lPropertyMapEntry.property_value));
	    	JSONObject jsonObjectNullObject = new JSONObject ();
	    	jsonObjectNullObject.put("bn" + Integer.toString(bnNum++), jsonObjectPropertyMapEntry);
			jsonArrayValuePropertyMapEntryList.add(jsonObjectNullObject);
	    }
		return jsonArrayValuePropertyMapEntryList;
	}
	
	// ======= Write the JSON file =======
  
	// write the JSON string to a file
    private void writeJson(JSONArray jsonObjectLevel0, String file) {
    	// write the JSON level 0 Object
        try {
        	Writer jsonFileWriter = Files.newBufferedWriter(Paths.get(file), UTF_8);            
            jsonFileWriter.write(jsonObjectLevel0.toJSONString());
            jsonFileWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	// ======= Query Model =======
    
  // Print the Terminological Entries - Query Model
  public void getTermEntriesQM(ArrayList<DOMClass> lSelectedDOMClassArr) {

    // scan select classes - QM classes
    for (DOMClass lDOMClass : lSelectedDOMClassArr) {

      // get all QM attributes
      for (DOMProp lDOMProp : lDOMClass.ownedAttrArr) {
        if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
          DOMAttr lDOMAttrQM = (DOMAttr) lDOMProp.hasDOMObject;

          // get all QM PV for this QM attribute
          for (DOMProp lDOMPropPV : lDOMAttrQM.domPermValueArr) {

            if (lDOMPropPV.hasDOMObject != null
                && lDOMPropPV.hasDOMObject instanceof DOMPermValDefn) {
              DOMPermValDefn lPVQM = (DOMPermValDefn) lDOMPropPV.hasDOMObject;

              // for one PV print all QM term entrires
              ArrayList<TermEntryDefn> lPVQMTermEntryDefnArr =
                  new ArrayList<>(lPVQM.termEntryMap.values());
              printTermEntryDefnQM(lDOMClass.title, lDOMAttrQM.title, lDOMClass.extrnTitleQM,
                  lDOMAttrQM.extrnTitleQM, lPVQM.value, lPVQMTermEntryDefnArr);
            }
          } // PV
        }
      } // Attr
    }
    // end TE list
  }

  // Print the Query Model Terminological Entries
  @SuppressWarnings("unchecked")
public JSONObject printTermEntryDefnQM(String lDOMClassQMtitle, String lDOMAttrQMtitle,
		  String lDOMClassExtrnTitle, String lDOMAttrExtrnTitle, String lPVQMValue,
		  ArrayList<TermEntryDefn> lTermEntryDefnArr) {

	  // get the class, title, and query model value JSONObject
	  JSONObject jsonObject999 = new JSONObject ();
	  jsonObject999.put(formatValue("class"), formatValue(lDOMClassExtrnTitle));
	  jsonObject999.put(formatValue("attribute"), formatValue(lDOMAttrExtrnTitle));
	  jsonObject999.put(formatValue("value"), formatValue(lPVQMValue));

	  //add the term entry list JSON OBJECT
	  jsonObject999.put("null10", printTermEntryDefnQMTermEntryList(lDOMClassQMtitle, lDOMAttrQMtitle, lTermEntryDefnArr));

	  // get the query model JSONArray
	  JSONArray jsonArray888 = new JSONArray ();
	  jsonArray888.add(jsonObject999);

	  // get the query model JSONObject
	  JSONObject jsonObject777 = new JSONObject ();
	  jsonObject777.put(formatValue(lDOMAttrQMtitle), jsonArray888);

	  // get the query model JSONObject
	  JSONObject jsonObject666 = new JSONObject ();
	  jsonObject666.put(formatValue(lDOMClassQMtitle), jsonObject777);

	  return jsonObject666;
  }

  // Print the Query Model Term Entry List
  @SuppressWarnings("unchecked")
public JSONObject printTermEntryDefnQMTermEntryList(String lDOMClassQMtitle, String lDOMAttrQMtitle, ArrayList<TermEntryDefn> lTermEntryDefnArr) {
    
	  // get JSON Array of semantic relations
	  JSONArray jsonArray888 = new JSONArray ();
	  for (TermEntryDefn lTermEntryDefn : lTermEntryDefnArr) {
		  JSONObject jsonObject999 = new JSONObject ();
		  jsonObject999.put(formatValue("name"), formatValue(lTermEntryDefn.name));
		  jsonObject999.put(formatValue("semanticRelation"), formatValue(lTermEntryDefn.semanticRelation));
		  jsonArray888.add(jsonObject999);
	  }

	  // get TermEntryList JSONObject
	  JSONObject jsonObject777 = new JSONObject ();
	  jsonObject777.put(formatValue("TermEntryList"), jsonArray888);

	  // nest TermEntryList
	  JSONObject jsonObject666 = new JSONObject ();
	  jsonObject666.put(formatValue("null11"), jsonObject777);

	  return jsonObject666;
  }

/*   Not invokrf either in original WriteDOMDDJSONFile or new WriteDOMDDJSONFileLib
  @SuppressWarnings("unchecked")
public JSONArray printTermEntriesList(DOMClass lSelectedDOMClass) {
	  JSONArray jsonAssociationList = new JSONArray ();
	  Set<String> set9 = lSelectedDOMClass.termEntryMap.keySet();
	  Iterator<String> iter9 = set9.iterator();
	  while (iter9.hasNext()) {
		  String lTitle = iter9.next();
		  TermEntryDefn lTermEntryDefn = lSelectedDOMClass.termEntryMap.get(lTitle);
		  if (!iter9.hasNext()) {
			  JSONObject jsonObject = getTermEntryObject(lTermEntryDefn);
			  jsonAssociationList.add (jsonObject);
		  }	
	  }
	  return jsonAssociationList;
  }

  @SuppressWarnings("unchecked")
  public JSONObject getTermEntryObject(TermEntryDefn lTermEntryDefn) {
	  JSONObject jsonObjectPropertyMap = new JSONObject ();
	  jsonObjectPropertyMap.put(formatValue("name"), formatValue(lTermEntryDefn.name));
	  jsonObjectPropertyMap.put(formatValue("semanticRelation"), formatValue(lTermEntryDefn.semanticRelation));
	  String lIsPreferred = Boolean.toString(lTermEntryDefn.isPreferred);
	  jsonObjectPropertyMap.put(formatValue("isPreferred"), formatValue(lIsPreferred));
	  jsonObjectPropertyMap.put(formatValue("definition"), formatValue(lTermEntryDefn.definition));
	  return jsonObjectPropertyMap;
  }  */
  
  // --- utilities ---
  
  // get a name/value pair
  @SuppressWarnings("unchecked")
  private JSONObject getNameValuePair (String name, Object value) {
	  JSONObject jsonObject = new JSONObject ();
	  jsonObject.put(name, value);		// ==> {name, value}
	  return jsonObject;
  }

  // Format the Boolean String for JSON
  public String formatBooleanValue(boolean lBoolean) {
	  String rString = String.valueOf(lBoolean);
	  return formatValue(rString);
  }

  // Format the String for JSON
  public String formatValue(String lString) {
	  String rString = lString;
	  if (rString == null) {
		  rString = "null";
	  }
	  if (rString.indexOf("TBD") == 0) {
		  rString = "null";
	  }
	  //  rString = DOMInfoModel.escapeJSONChar(rString);
	  return rString;
  } 

// group attributes and classes under a single property
  private static class DOMPropGroup { 
	  DOMProp domProp; // this is the property
	  ArrayList<ISOClassOAIS11179> domObjectArr; // these are the attributes and classes

	  public DOMPropGroup() {
		  domProp = null;
		  domObjectArr = new ArrayList<>();
	  }
  }
}
