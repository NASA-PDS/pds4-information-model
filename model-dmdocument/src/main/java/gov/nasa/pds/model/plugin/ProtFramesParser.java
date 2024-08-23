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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import gov.nasa.pds.model.plugin.util.Utility;

/**
 * Parses a Protege Frames file (.pont or .pins) and creates an array of tokens. parse - open file,
 * parse, and generate array of tokens and types getTokenArray - return token array
 * getTokenTypeArray - return token type array
 */
public class ProtFramesParser extends Object {
  InputStreamReader PBReader;

  char iptChar;
  int intIptChar;
  public ArrayList<String> tokenArr;
  public ArrayList<String> tokenType;
  public StringBuffer tokenBuf;

  public String dsid;
  public String ptname;
  int ptnamecnt;

  public ProtFramesParser() {
    tokenArr = new ArrayList<>();
    tokenType = new ArrayList<>();
    tokenBuf = new StringBuffer(32);
    dsid = "";
    ptname = "";
    ptnamecnt = 0;
  }

  /**
   * Opens a FileReader and parses a Protege Frames file (.pont or .pins) and creates an array of
   * tokens.
   */
  public boolean parse(String fname) throws Throwable {

    try {
      PBReader = new InputStreamReader(new FileInputStream(new File(fname)), "UTF-8");
      Utility.registerMessage("0>info " + "Found required file: " + fname);
    } catch (Exception e) {
      Utility.registerMessage("3>error " + "Could not find required file: " + fname);
      return false;
    }
    if (!getTokens()) {
      PBReader.close();
      return false;
    }
    PBReader.close();
    // listtokens();
    return true;
  }

  /**
   * Returns the array of parsed tokens.
   */
  public ArrayList<String> getTokenArray() {
    return tokenArr;
  }

  /**
   * Returns the array of parsed token types.
   */
  public ArrayList<String> getTokenTypeArray() {
    return tokenType;
  }


  /**
   * Parses a Protege Frames file (.pont or .pins) and creates an array of tokens.
   */
  private boolean getTokens() throws Throwable {
    tokenBuf.setLength(0);
    boolean eofFlag = false;
    int tokenBufCharCnt = 0;
    int type = 0;
    int charcnt = 0;

    tokenArr.clear();
    tokenType.clear();
    while (!eofFlag && (intIptChar = PBReader.read()) != -1) {
      iptChar = (char) intIptChar;
      charcnt++;
      if (tokenBufCharCnt > 10000) {
        type = 0;
        tokenBuf.setLength(0);
        tokenBufCharCnt = 0;
      }
      // System.out.println("debug char:"+iptChar);
      // ts = tokenBuf.toString();
      // System.out.println(" debug token:"+ts);

      switch (type) {
        case 0:
          if (Character.isLetter(iptChar) || iptChar == '%' || iptChar == '?'
              || Character.isDigit(iptChar) || iptChar == '-' || iptChar == '+') {
            type = 1;
            tokenBuf.append(iptChar);
            tokenBufCharCnt = 0;
          } else if (iptChar == '"') {
            type = 6;
          } else if (Character.isDefined(iptChar) && !Character.isWhitespace(iptChar)) {
            tokenBuf.append(iptChar);
            tokenBufCharCnt = 0;
            saveToken(tokenBuf, "O");
            tokenBuf.setLength(0);
          } else if (Character.isWhitespace(iptChar)) {
            tokenBufCharCnt++;
          } else {
            tokenBufCharCnt++;
          }
          break;
        case 1: // identifiers
          if (Character.isLetterOrDigit(iptChar) || iptChar == '_' || iptChar == '-'
              || iptChar == '+' || iptChar == '.' || iptChar == '#' || iptChar == '\\'
              || iptChar == '%') {
            tokenBuf.append(iptChar);
            tokenBufCharCnt++;
          } else {
            saveToken(tokenBuf, "I");
            type = 0;
            tokenBuf.setLength(0);
            tokenBufCharCnt = 0;
            if (Character.isDefined(iptChar) && !Character.isWhitespace(iptChar)) {
              tokenBuf.append(iptChar);
              tokenBufCharCnt = 0;
              saveToken(tokenBuf, "O");
              tokenBuf.setLength(0);
            }
          }
          break;
        case 6: // "quoted strings"
          if (iptChar == '"') {
            saveToken(tokenBuf, "Q");
            type = 0;
            tokenBuf.setLength(0);
          } else if (iptChar == '\\') { // Protege Escape Character - Backslash
            intIptChar = PBReader.read();
            iptChar = (char) intIptChar;
            charcnt++;
            if (iptChar == '"') { // Protege Quote Escaped
              tokenBuf.append('"');
              tokenBufCharCnt++;
            } else if (iptChar == '\\') { // Protege Backslash Escaped
              tokenBuf.append('\\');
              tokenBufCharCnt++;
            } else {
              tokenBuf.append('\\');
              tokenBufCharCnt++;
              tokenBuf.append(iptChar);
              tokenBufCharCnt++;
            }
          } else if (iptChar == '�') { // MS Word Escape for special quotes and apostrophes
            intIptChar = PBReader.read();
            charcnt++;
            intIptChar = PBReader.read();
            charcnt++;
            iptChar = (char) intIptChar;
            if (iptChar == '�') { // MS Word character designator for apostrophes
              tokenBuf.append('\'');
              tokenBufCharCnt++;
            } else {
              tokenBuf.append('"');
              tokenBufCharCnt++;
            }
          } else if ((Character.isDefined(iptChar) && !Character.isWhitespace(iptChar))
              || intIptChar == ' ') {
            tokenBuf.append(iptChar);
            tokenBufCharCnt++;
          }
          break;
      }
    }
    return true;
  }

  private void saveToken(StringBuffer tsbuf, String ttype) {
    String ts;
    ts = tsbuf.toString();
    tokenArr.add(ts);
    tokenType.add(ttype);
  }

  // get an ODL statement from the parsed tokens.

  private void listtokens() {
    String ts1 = "", tt = "";

    for (Iterator<String> iter1 = tokenArr.iterator(), iter2 = tokenType.iterator(); iter1
        .hasNext();) {
      ts1 = iter1.next();
      tt = iter2.next();
      System.out.println("debug token:" + ts1 + "   tokentype:" + tt);
    }
  }
}
