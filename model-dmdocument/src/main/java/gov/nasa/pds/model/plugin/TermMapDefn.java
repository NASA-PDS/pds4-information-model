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

import java.util.Comparator;

public class TermMapDefn {
  String identifier;
  String namespaceId;
  String stewardId;
  String title;
  String description;
  String referenceIdentifier;
  String skosRelationName;
  String modelObjectId;
  String modelObjectType;
  String instanceId;
  String parent;



  public TermMapDefn() {
    description = "";
    referenceIdentifier = null;
    instanceId = null;
    skosRelationName = "exactMatch"; // default for PDS4 obj
  }

  public void CopyFrom(TermMapDefn obj) {
    stewardId = obj.stewardId;
    namespaceId = obj.namespaceId;
    title = obj.title;
    parent = obj.parent;
    modelObjectId = obj.modelObjectId;
  }

  /* Comparator for sorting the list by modelObjectId */
  public static Comparator<TermMapDefn> TermMapComparator = new Comparator<TermMapDefn>() {

    @Override
    public int compare(TermMapDefn s1, TermMapDefn s2) {
      String modelObjectId1 = s1.modelObjectId.toUpperCase();
      String modelObjectId2 = s2.modelObjectId.toUpperCase();

      // ascending order
      return modelObjectId1.compareTo(modelObjectId2);

      // descending order
      // return modelObjectId2.compareTo(modelObojectId1);
    }
  };

  @Override
  public String toString() {
    return "[ identifier=" + identifier + "\n namespace=" + namespaceId + "\n title=" + title
        + "\n modelObjectId =" + modelObjectId + "]";
  }
}
