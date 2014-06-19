package gov.nasa.pds.model.plugin;
import java.io.*;
import java.util.*;

/**
 * Parses a Protege Frames file (.pont or .pins) and creates an array of tokens.
 *   parse - open file, parse, and generate array of tokens and types
 *   getTokenArray - return token array
 *   getTokenTypeArray - return token type array
 */
public class ProtFramesParser extends Object
{
	Reader PBReader;
	char iptChar;
	int intIptChar;
	public ArrayList<String> tokenArr;
	public ArrayList<String> tokenType;
	public StringBuffer tokenBuf;
	
	public String dsid;
	public String ptname;
	int ptnamecnt;
	
	public ProtFramesParser () {
		tokenArr = new ArrayList<String>();
		tokenType = new ArrayList<String>();
		tokenBuf = new StringBuffer(32);
		dsid = "";
		ptname = "";
		ptnamecnt = 0;
	}

	/**
	 * Opens a FileReader and parses a Protege Frames file (.pont or .pins) and creates an array of tokens.
	 */
	public boolean parse(String fname) throws Throwable {
		
		try {
			PBReader = new FileReader(fname);
			System.out.println(">>info    - Found required file: " + fname);
		} catch (Exception e) {
			System.out.println(">>error   - Could not find required file: " + fname);     
			return false;
		}
		if (!getTokens()) {
			PBReader.close();
			return false;
		}
		PBReader.close();
//		listtokens();
		return true;
	}

	/**
	 * Returns the array of parsed tokens.
	 */
	public ArrayList<String> getTokenArray () {
		return tokenArr;
	}

	/**
	 * Returns the array of parsed token types.
	 */
	public ArrayList<String> getTokenTypeArray () {
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
		while (! eofFlag && (intIptChar = PBReader.read()) != -1) {
			iptChar = (char) intIptChar;
			charcnt++;
			if (tokenBufCharCnt > 10000) {
					type = 0;
					tokenBuf.setLength(0);
					tokenBufCharCnt = 0;
			}
//			System.out.println("debug char:"+iptChar);
//			ts = tokenBuf.toString();
//			System.out.println("   debug token:"+ts);

			switch (type) {
			case 0:
				if (Character.isLetter(iptChar) || iptChar == '%' || iptChar == '?' || Character.isDigit(iptChar) || iptChar == '-' || iptChar == '+') {
					type = 1;
					tokenBuf.append(iptChar);
					tokenBufCharCnt = 0;
				} else if (iptChar == '"') {
					type = 6;
				} else if (Character.isDefined(iptChar) && !Character.isWhitespace(iptChar)) {
					tokenBuf.append(iptChar);
					tokenBufCharCnt = 0;
					saveToken (tokenBuf, "O");
					tokenBuf.setLength(0);
				} else if (Character.isWhitespace(iptChar)) {
					tokenBufCharCnt++;
				} else {
					tokenBufCharCnt++;
				}
				break;
			case 1:  // identifiers
				if (Character.isLetterOrDigit(iptChar) || iptChar == '_' || iptChar == '-' || iptChar == '+' || iptChar == '.' || iptChar == '#' || iptChar == '\\' || iptChar == '%') {
					tokenBuf.append(iptChar);
					tokenBufCharCnt++;
				} else {
					saveToken (tokenBuf, "I");
					type = 0;
					tokenBuf.setLength(0);
					tokenBufCharCnt = 0;
					if (Character.isDefined(iptChar) && !Character.isWhitespace(iptChar)) {
						tokenBuf.append(iptChar);
						tokenBufCharCnt = 0;
						saveToken (tokenBuf, "O");
						tokenBuf.setLength(0);
					}
				}
				break;
			case 6:  // "quoted strings"
			    if (iptChar == '"') {
			        saveToken (tokenBuf, "Q");
			        type = 0;
			        tokenBuf.setLength(0);
					} else if (iptChar == '\\') {		// Protege Escape Character - Backslash
						intIptChar = PBReader.read();
						iptChar = (char) intIptChar;
						charcnt++;
						if (iptChar == '"') {					// Protege Quote Escaped
			        tokenBuf.append('"');
					    tokenBufCharCnt++;
						} else if (iptChar == '\\') {	// Protege Backslash Escaped
			        tokenBuf.append('\\');
					    tokenBufCharCnt++;
					  } else {
			        tokenBuf.append('\\');
					    tokenBufCharCnt++;
			        tokenBuf.append(iptChar);
					    tokenBufCharCnt++;
					  }
					} else if (iptChar == '?') {			// MS Word Escape for special quotes and apostrophes
						intIptChar = PBReader.read();
						charcnt++;
						intIptChar = PBReader.read();
						charcnt++;
						iptChar = (char) intIptChar;
						if (iptChar == '?') {						// MS Word character designator for apostrophes
			        tokenBuf.append('\'');
					    tokenBufCharCnt++;
					  } else {
			        tokenBuf.append('"');
					    tokenBufCharCnt++;
					  }
			    } else if ((Character.isDefined(iptChar) && !Character.isWhitespace(iptChar)) || intIptChar == ' ') {
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
//		System.out.println("debug ProtFramesParser saveToken - ttype:" + ttype + " token:" + ts);
		ts = replaceString (ts, "%28", "(");	// replace Protege Escaped Characters
		ts = replaceString (ts, "%29", ")");
		ts = replaceString (ts, "%2F", "/");
		tokenArr.add(ts);
		tokenType.add(ttype);
	}

//	get an ODL statement from the parsed tokens.

	private void listtokens() {
		String ts1 = "", tt = "";

    for (Iterator<String> iter1 = tokenArr.iterator(), iter2 = tokenType.iterator(); iter1.hasNext();) {
			ts1 = (String) iter1.next();
			tt = (String) iter2.next();
			System.out.println("debug token:" + ts1 + "   tokentype:" + tt);
		}		
	}

	/**
		* Replace string with string (gleaned from internet)
		*/
	
	static String replaceString (String str, String pattern, String replace) {
			int s = 0;
			int e = 0;
			StringBuffer result = new StringBuffer();
			
			while ((e = str.indexOf(pattern, s)) >= 0) {
				result.append(str.substring(s, e));
				result.append(replace);
				s = e+pattern.length();
			}
			result.append(str.substring(s));
			return result.toString();
	 }
}
					