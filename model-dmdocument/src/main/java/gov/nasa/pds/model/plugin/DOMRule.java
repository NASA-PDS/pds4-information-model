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

public class DOMRule extends ISOClassOAIS11179 {
  String type;
  String roleId;
  String xpath;
  String attrTitle;
  String attrNameSpaceNC;
  String classTitle;
  String classNameSpaceNC;
  String classSteward;
  boolean alwaysInclude; // the rule is to always be included in the schematron file
  boolean isMissionOnly; // the rule is to be included in an LDDTool generated .sch file at the
                         // mission level
  boolean isAssociatedExternalClass; // the class was defined using DD_Associate_External_Class

  ArrayList<String> letAssignArr;
  ArrayList<String> letAssignPatternArr;
  ArrayList<DOMAssert> assertArr;

  public DOMRule(String id) {
    identifier = id;
    type = "TBD_type";
    roleId = "TBD_roleId";
    xpath = "TBD_xpath";
    attrTitle = "TBD_attributeName";
    attrNameSpaceNC = "TBD_attNameSpaceNC";
    classTitle = "TBD_classTitle";
    classNameSpaceNC = "TBD_NameSpaceNC";
    classSteward = "TBD_classSteward";
    alwaysInclude = false;
    isMissionOnly = false;
    isAssociatedExternalClass = false;

    letAssignArr = new ArrayList<>();
    letAssignPatternArr = new ArrayList<>();
    assertArr = new ArrayList<>();
  }

  public void setRDFIdentifier() {
    rdfIdentifier = DMDocument.rdfPrefix + "." + identifier + "." + DOMInfoModel.getNextUId();
  }

  public void printRuleDebug() {
    System.out.println("\n==========================  Rule  ===================================");
    System.out.println("  lRule.rdfIdentifier:" + this.rdfIdentifier);
    System.out.println("  lRule.identifier:" + this.identifier);
    System.out.println("  lRule.nameSpaceIdNC:" + this.nameSpaceIdNC);
    System.out.println("  lRule.type:" + this.type);
    System.out.println("  lRule.xpath:" + this.xpath);
    System.out.println("  lRule.roleId:" + this.roleId);
    System.out.println("  lRule.attrTitle:" + this.attrTitle);
    System.out.println("  lRule.attrNameSpaceNC:" + this.attrNameSpaceNC);
    System.out.println("  lRule.classTitle:" + this.classTitle);
    System.out.println("  lRule.classNameSpaceNC:" + this.classNameSpaceNC);
    System.out.println("  lRule.classSteward:" + this.classSteward);
    System.out.println("  lRule.alwaysInclude:" + this.alwaysInclude);
    System.out.println("  lRule.isMissionOnly:" + this.isMissionOnly);

    System.out.println("-------------------------  Let Assignments - Pattern  -----------");
    if (this.letAssignPatternArr != null) {
      for (Iterator<String> i = this.letAssignPatternArr.iterator(); i.hasNext();) {
        String lLetAssignPattern = i.next();
        System.out.println("    lLetAssignPattern:" + lLetAssignPattern);
      }
    }

    System.out.println("-------------------------  Let Assignments - Rule  --------------");
    if (this.letAssignArr != null) {
      for (Iterator<String> i = this.letAssignArr.iterator(); i.hasNext();) {
        String lLetAssign = i.next();
        System.out.println("    lLetAssign:" + lLetAssign);
      }
    }

    System.out.println("-------------------------  Assert Statement  --------------------");
    if (this.assertArr != null) {
      for (Iterator<DOMAssert> i = this.assertArr.iterator(); i.hasNext();) {
        DOMAssert lAssert = i.next();
        System.out.println("    lAssert.identifier:" + lAssert.identifier);
        System.out.println("    lAssert.attrTitle:" + lAssert.attrTitle);
        System.out.println("    lAssert.assertType:" + lAssert.assertType);
        System.out.println("    lAssert.assertMsg:" + lAssert.assertMsg);
        System.out.println("    lAssert.assertStmt:" + lAssert.assertStmt);
        System.out.println("    lAssert.specMesg:" + lAssert.specMesg);

        System.out.println("-------------------------  Assert Statement Test Values  --------");
        if (lAssert.testValArr != null) {
          for (Iterator<String> j = lAssert.testValArr.iterator(); j.hasNext();) {
            String lTestValue = j.next();
            System.out.println("       lTestValue:" + lTestValue);
          }
        }
      }
    }
  }
}
