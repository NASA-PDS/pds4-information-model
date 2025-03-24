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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import gov.nasa.pds.model.plugin.util.Utility;

public class DMProcessState {

  // processing flags map
  TreeMap<String, Integer> processFlagMap;
  TreeMap<Integer, String> processFlagSortMap;

  // written files map
  TreeMap<String, Integer> writtenFilesMap;
  TreeMap<Integer, String> writtenFilesSortMap;

  public DMProcessState() {
    // init the process flag map
    processFlagMap = new TreeMap<>();
    processFlagSortMap = new TreeMap<>();
    writtenFilesMap = new TreeMap<>();
    writtenFilesSortMap = new TreeMap<>();
  }

  // - getters -

  public TreeMap<String, Integer> getProcessFlagMap() {
    return processFlagMap;
  }

  public ArrayList<String> getProcessFlagNameArr() {
    return new ArrayList<>(processFlagMap.keySet());
  }

  public ArrayList<String> getSortedProcessFlagNameArr() {
    // sort the flags
    Set<String> processFlagKeySet = processFlagMap.keySet();
    Iterator<String> processFlagKeySetIter = processFlagKeySet.iterator();
    while (processFlagKeySetIter.hasNext()) {
      String lProcessFlag = processFlagKeySetIter.next();
      Integer lOrder = processFlagMap.get(lProcessFlag);
      processFlagSortMap.put(lOrder, lProcessFlag);
    }
    return new ArrayList<>(processFlagSortMap.values());
  }

  public ArrayList<String> getSortedWrittenFileNameArr() {
    // sort the files
    Set<String> writtenFilesKeySet = writtenFilesMap.keySet();
    Iterator<String> writtenFilesKeySetIter = writtenFilesKeySet.iterator();
    while (writtenFilesKeySetIter.hasNext()) {
      String lWrittenFile = writtenFilesKeySetIter.next();
      Integer lOrder = writtenFilesMap.get(lWrittenFile);
      writtenFilesSortMap.put(lOrder, lWrittenFile);
    }
    return new ArrayList<>(writtenFilesSortMap.values());
  }

  // public ArrayList <String> getFlagSetArr () {
  // return flagSetArr;
  // }

  // public ArrayList <String> getInputFileNameArr () {
  // return inputFileNameArr;
  // }

  // public ArrayList <String> getOutputFileNameArr () {
  // return outputFileNameArr;
  // }

  // public ArrayList <String> getOutputFileDirArr () {
  // return outputFileDirArr;
  // }

  public Boolean getPDSOptionalFlag() {
    Integer lProcessOrder = processFlagMap.get("PDS4 Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getLDDToolFlag() {
    Integer lProcessOrder = processFlagMap.get("LDD Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean gethelpFlag() {
    Integer lProcessOrder = processFlagMap.get("Help Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getmapToolFlag() {
    Integer lProcessOrder = processFlagMap.get("Map Tool Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getLDDToolAnnotateDefinitionFlag() {
    Integer lProcessOrder = processFlagMap.get("Annotate Definition Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getLDDToolMissionFlag() {
    Integer lProcessOrder = processFlagMap.get("Mission Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getPDS4MergeFlag() {
    Integer lProcessOrder = processFlagMap.get("Merge Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getLDDNuanceFlag() {
    Integer lProcessOrder = processFlagMap.get("Nuance Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getprintNamespaceFlag() {
    Integer lProcessOrder = processFlagMap.get("Print Namespace Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getLDDAttrElementFlag() {
    Integer lProcessOrder = processFlagMap.get("Attr Element Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getversionFlag() {
    Integer lProcessOrder = processFlagMap.get("Version Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getdisciplineMissionFlag() {
    Integer lProcessOrder = processFlagMap.get("Discipline Mission Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getexportDDFileFlag() { // ???
    return false;
  }

  public Boolean getexportJSONFileFlag() {
    Integer lProcessOrder = processFlagMap.get("Export JSON Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getexportSpecFileFlag() {
    Integer lProcessOrder = processFlagMap.get("Export IM Spec Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getimportJSONAttrFlag() {
    Integer lProcessOrder = processFlagMap.get("Import JSON Attr Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getexportOWLRDFTTLFileFlag() {
    Integer lProcessOrder = processFlagMap.get("Export OWL/RDF/TTL File Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getexportOWLRDFFileFlag() {
    Integer lProcessOrder = processFlagMap.get("Export OWL/RDF File Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }
  
  public Boolean getIncludeAllNamespacesFlag() {
    Integer lProcessOrder = processFlagMap.get("Include All Namespaces Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  public Boolean getcheckFileNameFlag() {
    Integer lProcessOrder = processFlagMap.get("Check File Name Flag");
    if (lProcessOrder != null) {
      return true;
    }
    return false;
  }

  // setters

  // public void addInputFileNameArr (String lInputFileName) {
  // inputFileNameArr.add(lInputFileName);
  // return;
  // }

  // public void addOutputFileNameArr (String lOutputFileName) {
  // outputFileNameArr.add(lOutputFileName);
  // return;
  // }

  // public void addOutputFileDirArr (String lOutputFileDir) {
  // outputFileDirArr.add(lOutputFileDir);
  // return;
  // }

  public void setPDSOptionalFlag() {
    processFlagMap.put("PDS4 Flag", 1010);
    return;
  }

  public void setLDDToolFlag() {
    processFlagMap.put("LDD Flag", 1020);
    return;
  }

  public void sethelpFlag() {
    processFlagMap.put("Help Flag", 1030);
    return;
  }

  public void setalternateIMVersionFlag() {
    processFlagMap.put("IM Version", 1040);
    return;
  }

  public void setmapToolFlag() {
    processFlagMap.put("Map Tool Flag", 1050);
    return;
  }

  public void setLDDToolAnnotateDefinitionFlag() {
    processFlagMap.put("Annotate Definition Flag", 1060);
    return;
  }

  public void setLDDToolMissionFlag() {
    processFlagMap.put("Mission Flag", 1070);
    return;
  }

  public void setPDS4MergeFlag() {
    processFlagMap.put("Merge Flag", 1080);
    return;
  }

  public void setLDDNuanceFlag() {
    processFlagMap.put("Nuance Flag", 1090);
    return;
  }

  public void setprintNamespaceFlag() {
    processFlagMap.put("Print Namespace Flag", 1100);
    return;
  }

  public void setLDDAttrElementFlag() {
    processFlagMap.put("Attr Element Flag", 1110);
    return;
  }

  public void setversionFlag() {
    processFlagMap.put("Version Flag", 1120);
    return;
  }

  public void setdisciplineMissionFlag() {
    processFlagMap.put("Discipline Mission Flag", 1130);
    return;
  }

  public void setexportDDFileFlag() {
    processFlagMap.put("Export DD Flag", 1140);
    return;
  }

  public void setexportJSONFileFlag() {
    processFlagMap.put("Export JSON Flag", 1150);
    return;
  }

  public void setexportSpecFileFlag() {
    processFlagMap.put("Export IM Spec Flag", 1160);
    return;
  }

  public void setimportJSONAttrFlag() {
    processFlagMap.put("Import JSON Attr Flag", 1180);
    return;
  }

  public void setExportOWLRDFTTLFileFlag() {
    processFlagMap.put("Export OWL/RDF/TTL Flag - Classifications", 1190);
    return;
  }
  
  public void setExportOWLRDFFileFlag() {
	processFlagMap.put("Export OWL/RDF Flag - Model", 1195);
	return;
  }

  public void setIncludeAllNamespacesFlag() {
    processFlagMap.put("Include All Namespaces Flag", 1200);
    return;
  }

  public void setcheckFileNameFlag() {
    processFlagMap.put("Check File Name Flag", 1210);
    return;
  }
  
  public void setExportTermMapFileFlag() {
	  processFlagMap.put("ExportTermMapFileFlag", 1220);
	  return;
  }
	        
/*  public void setExportOWLFileFlag() {
     processFlagMap.put("ExportOWLFileFlag", 1230);
     return;
  } */

  public void setExportCustomFileFlag() {
      processFlagMap.put("ExportCustomFileFlag", 1240);
      return;
  }  

  // written files map

  public void setRelativeFileSpecModelSpec_DOM(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecModelSpec_DOM, 1020);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecXMLSchema(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecXMLSchema, 1030);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecSchematron(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecSchematron, 1040);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecXMLLabel(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecXMLLabel, 1080);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecDOMModelJSON(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecDOMModelJSON, 1100);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecDDCSV(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecDDCSV, 1110);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecCCSDSCSV(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecCCSDSCSV, 1120);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/CSV/");
    }
    return;
  }

  public void setRelativeFileSpecLDDPontMerge(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecLDDPontMerge, 1130);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecReportTXT(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecReportTXT, 1140);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecDDDocXML(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecDDDocXML, 1150);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/DD/");
    }
    return;
  }

  public void setRelativeFileSpecDDProtPins(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecDDProtPins, 1160);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecDDProtPinsSN(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecDDProtPinsSN, 1170);
    // checkCreateDirectory not needed, file is in root directory
    return;
  }

  public void setRelativeFileSpecModelRDF(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecModelRDF, 1190);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/rdf/");
    }
    return;
  }

  public void setRelativeFileSpecOWLRDF_DOM(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecOWLRDF_DOM, 1210);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/owl/");
    }
    return;
  }

  public void setRelativeFileSpecSKOSTTL_DOM(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecSKOSTTL_DOM, 1230);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/skos/");
    }
    return;
  }
  
  public void setRelativeFileSpecOWLRDFTTL(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecOWLRDF_DOM, 1231);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/owl/");
    }
    return;
  }
  
  public void setRelativeFileSpecOWLRDF(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecOWLRDF_DOM, 1232);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/owl/");
    }
    return;
  }

  public void setRelativeFileSpecUMLXMI(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecUMLXMI, 1240);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/xmi/");
    }
    return;
  }

  public void setRelativeFileSpecUMLXMI2(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecUMLXMI2, 1250);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/xmi/");
    }
    return;
  }

  public void setRelativeFileSpecAttrDefn(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecAttrDefn, 1300);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/defnAttr/");
    }
    return;
  }

  public void setRelativeFileSpecClassDefn(SchemaFileDefn lSchemaFileDefn) {
    writtenFilesMap.put(lSchemaFileDefn.relativeFileSpecClassDefn, 1310);
    if (lSchemaFileDefn.isLDD) {
      Utility.checkCreateDirectory(DMDocument.getOutputDirPath() + "export/defnClass/");
    }
    return;
  }
}
