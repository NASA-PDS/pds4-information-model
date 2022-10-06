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
import java.util.TreeMap;

public class ISOClassOAIS11179 extends ISOClassOAIS {
  String regAuthId; // registration authority identifier
  String subModelId; // identifier of submodel within the registration authority's model.
  String steward; // steward
  String nameSpaceId; // namespace id - assigned namespace id with colon
  String nameSpaceIdNC; // namespace id - assigned namespace id No Colon
  String lddTitle; // class and attribute title from LDD processing - DD_Associate_External_Class
  String anchorString; // "class_" + lClass.nameSpaceIdNC + "_" + lClass.title

  boolean isUsedInModel;
  boolean isAbstract;
  boolean isFromLDD; // has been ingested from Ingest_LDD

  ArrayList<ISOClassOAIS11179> hasDOMObject; // allows more than one object (DOMProp OVERRIDE allows
                                             // only one DOMxxx class)
  TreeMap<String, TermEntryDefn> termEntryMap;
  TreeMap<String, PropertyMapDefn> propertyMapMap; // for miscellaneous meta-attributes, e.g. system
                                                   // status, dates, etc

  public ISOClassOAIS11179() {
    regAuthId = "TBD_registration_authority_identifier";
    subModelId = "TBD_subModelId";
    steward = "TBD_steward";
    nameSpaceId = "TBD_namespaceid";
    nameSpaceIdNC = "TBD_namespaceidNC";
    anchorString = "TBD_anchorString";

    isUsedInModel = false;
    isAbstract = false;
    isFromLDD = false;

    hasDOMObject = new ArrayList<>();
    termEntryMap = new TreeMap<>();
    propertyMapMap = new TreeMap<>();
  }

  public String getAnchorString() {
    return anchorString;
  }

  public void setAnchorString(String anchorString) {
    this.anchorString = anchorString;
  }

  public String getRegAuthId() {
    return regAuthId;
  }

  public void setRegAuthId(String regAuthId) {
    this.regAuthId = regAuthId;
  }

  public String getSteward() {
    return steward;
  }

  public void setSteward(String steward) {
    this.steward = steward;
  }

  public String getNameSpaceId() {
    return nameSpaceIdNC + ":";
  }

  public void setNameSpaceId(String nameSpaceId) {
    this.nameSpaceId = nameSpaceId;
  }

  public String getNameSpaceIdNC() {
    return nameSpaceIdNC;
  }

  public void setNameSpaceIdNC(String nameSpaceIdNC) {
    this.nameSpaceIdNC = nameSpaceIdNC;
    this.nameSpaceId = this.nameSpaceIdNC + ":";
  }

  // get the name in the indicated language; use the attribute title as a default
  public String getNameInLanguage(String lLanguage) {
    if (lLanguage == null) {
      return this.title;
    }
    TermEntryDefn lTermEntry = this.termEntryMap.get(lLanguage);
    if (lTermEntry == null) {
      return this.title;
    }
    return lTermEntry.name;
  }

  // get the definition in the indicated language; use the attribute description as a default
  public String getDefinitionInLanguage(String lLanguage) {
    if (lLanguage == null) {
      return this.definition;
    }
    TermEntryDefn lTermEntry = this.termEntryMap.get(lLanguage);
    if (lTermEntry == null) {
      return this.definition;
    }
    return lTermEntry.definition;
  }
}
