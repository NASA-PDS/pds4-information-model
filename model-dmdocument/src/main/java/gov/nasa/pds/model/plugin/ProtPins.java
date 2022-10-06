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
import java.util.HashMap;
import java.util.Iterator;

/**
 * Transforms a token array (parsed Protege .pins file) into logical entities (e.g. dictionary
 * attributes). getProtInst - get token array from parse and convert into logical entities.
 * 
 * called for DMDocument.pins, dd11179.pins, etc
 */
class ProtPins extends Object {
  InstDefn lInst;
  ArrayList<String> tokenArray;
  ArrayList<String> genSlotValArray;
  HashMap<String, InstDefn> instDict;
  // String gSteward;
  String gNameSpaceIdNC;
  String lAttrName, lAttrVal;

  public ProtPins() {
    instDict = new HashMap<>();
    return;
  }

  /**
   * Transform a token array (parsed Protege .pins file) into logical entities (e.g. dictionary
   * attributes).
   */
  public void getProtInst(String rdfPrefix, String lNameSpaceIdNC, String fname) throws Throwable {
    // this.gSteward = lSteward;
    gNameSpaceIdNC = lNameSpaceIdNC;
    ProtFramesParser lparser = new ProtFramesParser();
    if (lparser.parse(fname)) {
      tokenArray = lparser.getTokenArray();
      Iterator<String> tokenIter = tokenArray.iterator();
      getInstances(tokenIter);
    }
    return;
  }

  private void getInstances(Iterator<String> tokenIter) {
    int type = 0;
    int nestlevel = 0;

    while (tokenIter.hasNext()) {
      String token = tokenIter.next();
      if (token.compareTo("(") == 0) {
        nestlevel++;
      } else if (token.compareTo(")") == 0) {
        nestlevel--;
      }
      switch (type) {
        case 0:
          // System.out.println("debug0 token:" + token);
          if (token.compareTo("[") == 0 && nestlevel == 1) {
            type = 1;
          }
          break;
        case 1: // Instance Name
          // System.out.println("debug ProtPins - instance name - token:" + token);
          // 444 String title = token;
          String lToken = DOMInfoModel.unEscapeProtegeString(token);
          String title = lToken;
          String rdfIdentifier = gNameSpaceIdNC + "." + title;
          String identifier = gNameSpaceIdNC + "." + title;
          lInst = new InstDefn(rdfIdentifier);
          lInst.title = title;
          lInst.identifier = identifier;
          lInst.steward = gNameSpaceIdNC;
          // lInst.nameSpaceId = gSteward; // Default - can be reset in 11179 dictionary
          lInst.nameSpaceId = gNameSpaceIdNC;
          lInst.genSlotMap = new HashMap<>();
          instDict.put(lInst.rdfIdentifier, lInst);
          String token1 = tokenIter.next();
          token1 = tokenIter.next();
          if (token1.compareTo("of") == 0) {
            String token2 = tokenIter.next();
            lInst.className = token2;
          }
          type = 2;
          break;
        case 2: // start slots
          // System.out.println("debug2 start slots lInst.title:" + lInst.title + " nestlevel:" +
          // nestlevel);
          if (token.compareTo("(") == 0 && nestlevel == 2) {
            type = 3;
          }
          if (token.compareTo(")") == 0 && nestlevel == 0) {
            type = 0;
          }
          break;
        case 3: // a slot
          genSlotValArray = new ArrayList<>();
          // 444
          // lInst.genSlotMap.put(token, genSlotValArray);
          // lAttrName = token;
          String lToken2 = DOMInfoModel.unEscapeProtegeString(token);
          lInst.genSlotMap.put(lToken2, genSlotValArray);
          lAttrName = lToken2;
          // System.out.println("\ndebug ProtPins.getInstances gSteward:" + gSteward + "
          // lInst.title:" + lInst.title + " lAttrName:" + lAttrName);
          type = 4;
          break;
        case 4: // value list
          if (token.compareTo(")") == 0) {
            type = 2;
          } else if ((token.compareTo("[") == 0) || (token.compareTo("]") == 0)) {
            type = 4;
          } else {
            String lToken3 = DOMInfoModel.unEscapeProtegeString(token);
            genSlotValArray.add(lToken3);
            lAttrVal = lToken3;
          }
          break;
      }
    }
  }
}
