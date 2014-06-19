package gov.nasa.pds.model.plugin;
import java.util.*;

/**
 * Transforms a token array (parsed Protege .pins file) into logical entities (e.g. dictionary attributes).
 *   getProtInst - get token array from parse and convert into logical entities.
 *   
 *   called for DMDocument.pins, dd11179.pins, etc
 */
class ProtPins extends Object{
	InstDefn lInst;
	ArrayList <String> tokenArray;
	ArrayList <String> genSlotValArray;
	HashMap <String, InstDefn> instDict;
//	String gSteward;
	String gNameSpaceIdNC;
	String lAttrName, lAttrVal;
	
	public ProtPins () {
		instDict = new HashMap <String, InstDefn> ();
		return;
	}
	
	/**
	 * Transform a token array (parsed Protege .pins file) into logical entities (e.g. dictionary attributes).
	 */
//	public void getProtInst (String rdfPrefix, String lSteward, String fname) throws Throwable {
	public void getProtInst (String rdfPrefix, String lNameSpaceIdNC, String fname) throws Throwable {
//		this.gSteward = lSteward;
		gNameSpaceIdNC = lNameSpaceIdNC;
		ProtFramesParser lparser = new ProtFramesParser();
		if (lparser.parse(fname)) {
			tokenArray = lparser.getTokenArray();
			Iterator <String> tokenIter = tokenArray.iterator();
			getInstances(tokenIter);
		}
		return;
	}
	
	private void getInstances(Iterator <String> tokenIter) {
		int type = 0;
		int nestlevel = 0;
		
	    while (tokenIter.hasNext()) {
			String token = (String) tokenIter.next();
			if (token.compareTo("(") == 0) {
				nestlevel++;
			} else if (token.compareTo(")") == 0) {
				nestlevel--;
			}
			switch (type) {
			case 0:
//				System.out.println("debug0 token:" + token);
				if (token.compareTo("[") == 0 && nestlevel == 1) {
					type = 1;
				}
				break;
			case 1: // Instance Name
//				System.out.println("debug1 instance name:" + token);
				String title = token;
				String rdfIdentifier = gNameSpaceIdNC + "." + title;
				String identifier = gNameSpaceIdNC + "."  + title;
				lInst = new InstDefn(rdfIdentifier); 
				lInst.title = title;
				lInst.identifier = identifier;
				lInst.steward = gNameSpaceIdNC;
//				lInst.nameSpaceId = gSteward;	// Default - can be reset in 11179 dictionary
				lInst.nameSpaceId = gNameSpaceIdNC;
				lInst.genSlotMap = new HashMap <String, ArrayList<String>> ();
				instDict.put(lInst.rdfIdentifier, lInst);
				String token1 = (String) tokenIter.next();
				 token1 = (String) tokenIter.next();
				if (token1.compareTo("of") == 0) {
					String token2 = (String) tokenIter.next();
					lInst.className = token2;
				}
				type = 2;
				break;
			case 2: // start slots
//				System.out.println("debug2 start slots lInst.title:" + lInst.title + "  nestlevel:" + nestlevel);
				if (token.compareTo("(") == 0 && nestlevel == 2) {
					type = 3;
				}
				if (token.compareTo(")") == 0 && nestlevel == 0) {
					type = 0;
				}
				break;
			case 3: // a slot
				genSlotValArray = new ArrayList <String> ();
				lInst.genSlotMap.put(token, genSlotValArray);
				lAttrName = token;
//				System.out.println("\ndebug ProtPins.getInstances gSteward:" + gSteward + "  lInst.title:" + lInst.title + "  lAttrName:" + lAttrName);				
				type = 4;
				break;
			case 4: // value list
				if (token.compareTo(")") == 0) {
					type = 2;
				} else if ((token.compareTo("[") == 0) || (token.compareTo("]") == 0)) {
					type = 4;
				} else {
					genSlotValArray.add(InfoModel.unEscapeProtegeString(token));
					lAttrVal = InfoModel.unEscapeProtegeString(token);
//					System.out.println("                            lInst.title:" + lInst.title + "  lAttrName:" + lAttrName + "  lAttrVal:" + lAttrVal);
				}
				break;
			}
		}
	}
}
