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
import java.util.TreeMap;

// package gov.nasa.pds.model.plugin;

public class DOMMsgDefn extends Object {

  static ArrayList<String> messageLevelArr = new ArrayList<>();
  static TreeMap<String, String> messageLevelTitleMap = new TreeMap<>();
  static TreeMap<String, String> messagePrefixMap = new TreeMap<>();

  private Integer msgOrder;
  private String msgTypeLevel;
  private String msgPrefix;
  private String msgOrgText;
  private String msgCleanText;
  private String msgGroupTitle;
  private String nameSpaceIdNCLC;

  public DOMMsgDefn() {
    // messageLevelArr is initialized (init()) when masterDOMMsgDefn is created in DMDocument
	  messageLevelArr = new ArrayList<>();
	  messageLevelTitleMap = new TreeMap<>();
	  messagePrefixMap = new TreeMap<>();
  }

  public DOMMsgDefn(String lMessage) {
    msgOrder = (DMDocument.msgOrder += 10);
    msgTypeLevel = "TBD_msgTypeLevel";
    msgPrefix = "TBD_msgPrefix";
    msgOrgText = "TBD_msgOrgText";
    msgCleanText = "TBD_msgCleanText";
    msgGroupTitle = "TBD_msgGroupTitle";
    nameSpaceIdNCLC = "TBD_nameSpaceIdNCLC";

    int lMessageLength = lMessage.length();
    if (lMessageLength <= 0) {
      return;
    }

    msgOrgText = lMessage;
    for (Iterator<String> i = DOMMsgDefn.messageLevelArr.iterator(); i.hasNext();) {
      String lMessageLevel = i.next();
      int lLevelInd = lMessage.indexOf(lMessageLevel);
      if (lLevelInd > -1) {
        String lCleanedMessage = lMessage.substring(lMessageLevel.length() + 1, lMessageLength);
        msgTypeLevel = lMessageLevel;
        // msgPrefix = ">> " + lMessageLevel.substring(2, lMessageLevel.length());
        String lMsgGroupInd = lMessageLevel.substring(0, 1);
        if (lMsgGroupInd.compareTo("2") != 0) {
          msgPrefix = ">>  " + messagePrefixMap.get(lMessageLevel);
        } else {
          msgPrefix = ">>> " + messagePrefixMap.get(lMessageLevel);
        }
        msgCleanText = lCleanedMessage;
        msgGroupTitle = messageLevelTitleMap.get(lMessageLevel);
        return;
      }
    }
    msgTypeLevel = ">4>error";
    return;
  }

  static void init() {

    // message level titles
    messageLevelTitleMap.put("0>info", "Debug");
    messageLevelTitleMap.put("0>warning", "Debug");
    messageLevelTitleMap.put("0>error", "Debug");
    messageLevelTitleMap.put("1>info", "Main Processing");
    messageLevelTitleMap.put("1>warning", "Main Processing");
    messageLevelTitleMap.put("1>error", "Main Processing");
    messageLevelTitleMap.put("2>info", "Local Data Dictionary (LDD)");
    messageLevelTitleMap.put("2>warning", "Local Data Dictionary (LDD)");
    messageLevelTitleMap.put("2>error", "Local Data Dictionary (LDD)");
    messageLevelTitleMap.put("3>error", "Fatal Error");
    messageLevelTitleMap.put("4>error", "System Fatal Error");

    // message level titles
    messagePrefixMap.put("0>info", "DEBUG");
    messagePrefixMap.put("0>warning", "DEBUG");
    messagePrefixMap.put("0>error", "DEBUG");
    messagePrefixMap.put("1>info", "INFO");
    messagePrefixMap.put("1>warning", "WARNING");
    messagePrefixMap.put("1>error", "ERROR");
    messagePrefixMap.put("2>info", "INFO");
    messagePrefixMap.put("2>warning", "WARNING");
    messagePrefixMap.put("2>error", "ERROR");
    messagePrefixMap.put("3>error", "FATAL ERROR");
    messagePrefixMap.put("4>error", "SYSTEM FATAL ERROR");

    // message levels
    // debug level
    messageLevelArr.add("0>info");
    messageLevelArr.add("0>warning");
    messageLevelArr.add("0>error");

    // main level
    messageLevelArr.add("1>info");
    messageLevelArr.add("1>warning");
    messageLevelArr.add("1>error");

    // LDD
    messageLevelArr.add("2>info");
    messageLevelArr.add("2>warning");
    messageLevelArr.add("2>error");

    // fatal errors
    messageLevelArr.add("3>error");
    messageLevelArr.add("4>error");
  }

  public static ArrayList<String> getMessageLevelArr() {
    return messageLevelArr;
  }

  public static void setMessageLevelArr(ArrayList<String> messageLevelArr) {
    DOMMsgDefn.messageLevelArr = messageLevelArr;
  }

  public static TreeMap<String, String> getMessageLevelTitleMap() {
    return messageLevelTitleMap;
  }

  public static void setMessageLevelTitleMap(TreeMap<String, String> messageLevelTitleMap) {
    DOMMsgDefn.messageLevelTitleMap = messageLevelTitleMap;
  }

  public static TreeMap<String, String> getMessagePrefixMap() {
    return messagePrefixMap;
  }

  public static void setMessagePrefixMap(TreeMap<String, String> messagePrefixMap) {
    DOMMsgDefn.messagePrefixMap = messagePrefixMap;
  }

  public Integer getMsgOrder() {
    return msgOrder;
  }

  public void setMsgOrder(Integer msgOrder) {
    this.msgOrder = msgOrder;
  }

  public String getMsgTypeLevel() {
    return msgTypeLevel;
  }

  public void setMsgTypeLevel(String msgTypeLevel) {
    this.msgTypeLevel = msgTypeLevel;
  }

  public String getMsgPrefix() {
    return msgPrefix;
  }

  public void setMsgPrefix(String msgPrefix) {
    this.msgPrefix = msgPrefix;
  }

  public String getMsgOrgText() {
    return msgOrgText;
  }

  public void setMsgOrgText(String msgOrgText) {
    this.msgOrgText = msgOrgText;
  }

  public String getMsgCleanText() {
    return msgCleanText;
  }

  public void setMsgCleanText(String msgCleanText) {
    this.msgCleanText = msgCleanText;
  }

  public String getMsgGroupTitle() {
    return msgGroupTitle;
  }

  public void setMsgGroupTitle(String msgGroupTitle) {
    this.msgGroupTitle = msgGroupTitle;
  }

  public String getNameSpaceIdNCLC() {
    return nameSpaceIdNCLC;
  }

  public void setNameSpaceIdNCLC(String nameSpaceIdNCLC) {
    this.nameSpaceIdNCLC = nameSpaceIdNCLC;
  }
}
