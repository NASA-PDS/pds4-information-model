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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

class WriteDOMCSVFileClassHier extends Object {
  
  // list of the upper level classes in the model to be considered
  ArrayList <String> selectedUpperLevelClasses = new ArrayList<>( Arrays.asList(
          "Provenance"
      ));

  // setup a structure to capture multiple sorted lines and a variable number of values per line
  int itemOrderNum = 0;
  TreeMap <String, ClassAttrValueStruct> classAttrValueStructMap = new TreeMap <> ();

  public WriteDOMCSVFileClassHier() {
    return;
  }

  // write the CSV File
  public void writeDOMCSVFile(ArrayList<DOMClass> inputClassArr, SchemaFileDefn lSchemaFileDefn,
		  String lOtherLanguage) throws java.io.IOException {

	  // set up the output file
	  String lFileName = lSchemaFileDefn.relativeFileSpecDDCSV;
	  lFileName = lSchemaFileDefn.relativeFileSpecDDCSV + "_EIMSpreadSheet" ;
	  lFileName += ".txt";
	  FileOutputStream lFileOutputStream = new FileOutputStream(lFileName);
	  BufferedWriter prCSVAttr =
			  new BufferedWriter(new OutputStreamWriter(lFileOutputStream, "UTF8"));
	  prCSVAttr.write("Class Name" + "	" + "Class Definition" + "	" + "Attribute Name" + "	" + "Attribute Definition" + "	" + "Attribute Value" + "	" + "Value Meaning");
	  prCSVAttr.newLine();
    
	  // get the selected classes
	  for (DOMClass selectedInputClass : inputClassArr) {
		  if ((selectedInputClass.isInactive || selectedInputClass.isUSERClass || selectedInputClass.isUnitOfMeasure || selectedInputClass.isDataType || selectedInputClass.isVacuous)) continue;
		  if (selectedInputClass.title.compareTo(DMDocument.TopLevelAttrClassName) == 0) continue;
		  if (! selectedUpperLevelClasses.contains(selectedInputClass.title)) continue;
		  prCSVAttr.write(selectedInputClass.title + "	" + selectedInputClass.definition);
		  prCSVAttr.newLine();
		  
		  // *** get all components of the selected class ***
		  getAllComponentClasses(selectedInputClass, 1);
		  
		  ArrayList <ClassAttrValueStruct> classAttrValueStructArr = new ArrayList <> (classAttrValueStructMap.values());
		  for (ClassAttrValueStruct classAttrValueStruct : classAttrValueStructArr) {;
			  prCSVAttr.write(classAttrValueStruct.getLine());
			  prCSVAttr.newLine();
		  }
	  }
	  prCSVAttr.close();
  }
  

  // write the class attributes
  private void getAllComponentClasses(DOMClass lRootDOMClass, int classLevel) {
	  classLevel++;
	  
	  // navigate the class component properties to gather up all the component classes
	  for (DOMProp lDOMProp : lRootDOMClass.ownedAssocArr) {
		  if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMClass) {
			  DOMClass lDOMClass = (DOMClass) lDOMProp.domObject;  
			  ClassAttrValueStruct classAttrValueStruct = new ClassAttrValueStruct ();
			  classAttrValueStructMap.put(getNextItemOrderNumber(), classAttrValueStruct);
			  classAttrValueStruct.classTitle = lDOMClass.title;
			  classAttrValueStruct.classDefinition = lDOMClass.definition;
			  classAttrValueStruct.classLevel = classLevel;
			  
			  // get the classes attributes and permissible values
			  getAttrsAndPermValues(classLevel, lDOMClass, classAttrValueStruct);
			  
			  // recurse
			  getAllComponentClasses(lDOMClass, classLevel);
		  }
	  }
	  return;
  }
  
  // get the attributes and permissible values
  private void getAttrsAndPermValues(Integer classLevel, DOMClass lClass, ClassAttrValueStruct classAttrValueStruct) {
	  
	  // select and sort the attributes
	  TreeMap <String, DOMAttr> sortedDOMAttrMap = new TreeMap <> ();
	  ArrayList<DOMProp> allAttr = new ArrayList<>();
	  allAttr.addAll(lClass.ownedAttrArr);
	  allAttr.addAll(lClass.inheritedAttrArr);
	  for (DOMProp lDOMProp : allAttr) {
		  if (lDOMProp.domObject != null && lDOMProp.domObject instanceof DOMAttr) {
			  DOMAttr lDOMAttr = (DOMAttr) lDOMProp.domObject;
			  String sortKey = lDOMProp.classOrder + "_" + lClass.title;
			  sortedDOMAttrMap.put(sortKey, lDOMAttr);
		  }
	  }
	  
	  // get the attributes and the permissible values
	  ArrayList <DOMAttr> sortedDOMAttrArr = new ArrayList <> (sortedDOMAttrMap.values());
	  for (DOMAttr lDOMAttr : sortedDOMAttrArr) {
			  
		  // valueArr is first initialized with the class name, the first attr is printed 
		  // then for each succedding attribute, a new valueArr is created, but with the init class name.
			  if (! classAttrValueStruct.isFirst) {
				  classAttrValueStruct = new ClassAttrValueStruct ();
				  classAttrValueStructMap.put(getNextItemOrderNumber(), classAttrValueStruct);
			  }
			  classAttrValueStruct.isFirst = false;
			  classAttrValueStruct.attrTitle = lDOMAttr.title;
			  classAttrValueStruct.attrDefinition = lDOMAttr.definition;
			  classAttrValueStruct.classLevel = classLevel;
			  
			  for (DOMProp lDOMProp2 : lDOMAttr.domPermValueArr) {
				  if ((lDOMProp2.domObject == null) || !(lDOMProp2.domObject instanceof DOMPermValDefn)) continue;
				  DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp2.domObject;
				  
				  if (! classAttrValueStruct.isFirst) {
					  classAttrValueStruct = new ClassAttrValueStruct ();
					  classAttrValueStruct.classLevel = classLevel;
					  classAttrValueStructMap.put(getNextItemOrderNumber(), classAttrValueStruct);
				  }
				  classAttrValueStruct.isFirst = false;
				  classAttrValueStruct.permValValue = lDOMPermValDefn.value;
				  classAttrValueStruct.permValValueMeaning = lDOMPermValDefn.value_meaning;
			  }
	  }
  }
  
  // get the next item order number
  private String getNextItemOrderNumber() {
	  itemOrderNum++;
	  int tempNum = (itemOrderNum * 10) + 10000;
	  return String.valueOf(tempNum);
  } 
  
  // structure designed to capture the elements of one row of the CSV file.
  private class ClassAttrValueStruct {
	  boolean isFirst = true;
	  int classLevel = 0;
	  String classTitle = "";
	  String classDefinition = "";
	  String attrTitle = "";
	  String attrDefinition = "";
	  String permValValue = "";
	  String permValValueMeaning = "";

	  public String getLine () {
		  String line = "";
		  String space20 = "                    ";
//		  String prefix = "" + space20.substring(0, classLevel * 2) + classLevel + ") ";
		  String prefix = "" + space20.substring(0, classLevel * 3);
		  line = prefix + classTitle + "	" + classDefinition + "	" + attrTitle + "	" + attrDefinition + "	" + permValValue + "	" + permValValueMeaning;
		  return line;
	  }
  }
}
