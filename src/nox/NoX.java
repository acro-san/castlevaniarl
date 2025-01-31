package nox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * NoX's purpose is to create a useful XMLTag structure from String.
 * We can then simply use that structure to traverse down/around and query Atts,
 * Tags, Comments, Cdata/String contents, etc. It's thick-headedly-simple, 
 * strict, minimal and *fault-intolerant* dumbass xml parsing.
 * aka just: 'Say No to XML'.
 */
public class NoX {
	
	// Toggle these on to get reams of messy parse-output log debugging when
	// things inevitably go wrong with processing your files.
	public static boolean
		ATT_DEBUG = false,
		PARSETAG_DEBUG = false;

	public static String stripXMLDeclaration(String xml) {
		int endDecl = xml.indexOf("?>");
		// Also skip any subsequent \n and other whitespace:
		int nextTagStart = xml.indexOf("<", endDecl);
		xml = xml.substring(nextTagStart);
		return xml.trim();
	}


	public static int findEndTag(String xml, String tagname) {
		
		final String opentag = "<"+tagname;
		final String endtag = "</"+tagname+">";
		int firstEndPos = xml.indexOf(endtag);
		
		int nextStartInstance = xml.indexOf(opentag, opentag.length());
		if (nextStartInstance == -1 || nextStartInstance > firstEndPos) {
			//we need to find another end position. and need to keep on counting
			//open tags started between.
			return firstEndPos;
		}
		
		int[] openTagPlaces = listAllInstances(xml, opentag);
		int[] endTagPlaces = listAllInstances(xml, endtag);
		
		int[] openDepths = new int[openTagPlaces.length];
		int[] closeDepths= new int[endTagPlaces.length];
		
		int openIndex = 1,
			closeIndex = 0;
		
		//All we need is: find location of *actual* endtag for THIS start.
		//go through all starts/stops, looking for when our parity hits 0 again.
		openDepths[0] = 0;
		int nestDepth = 1;
		
		int opens  = openTagPlaces.length,
			closes = endTagPlaces.length;
		
		while (openIndex < opens || closeIndex < closes) {

			int nextOpen = (openIndex >= opens) ? Integer.MAX_VALUE: openTagPlaces[openIndex];
			int nextClose = (closeIndex >= closes) ? Integer.MAX_VALUE: endTagPlaces[closeIndex];
			
			// consume an open, or a close? We get next lowest index, and
			// advance the lower one.
			if (nextOpen < nextClose) {
			//	System.err.println("hit "+opentag+" at "+nextOpen+" depth:" + nestDepth);
				openDepths[openIndex] = nestDepth;
				nestDepth++;
				openIndex++;
			} else {
			//	System.err.println("hit "+endtag+" at "+nextClose+" depth:" + nestDepth);
				//the next tag encountered is an end tag.
				nestDepth--;
				closeDepths[closeIndex] = nestDepth;
				closeIndex++;
			}
		}
		
		for (int c=0; c<closeDepths.length; c++) {
			if (closeDepths[c] == 0) {
				return endTagPlaces[c];
			}
		}
		
		//if not found - that's an error. But we're not handling any.
		//So now... LOOK FOR OTHER START TAGS in the in-between!? if none, done.
		//if any - could count how many. then need to keep seeking. for...? idk.
		
		return 0;
	}


	public static int[] listAllInstances(String xml, String needle) {
		int idx = 0;
		int maxPossible = xml.length();
		int[] finds = new int[maxPossible];
		int f = 0;
		if (xml.startsWith(needle)) {
			finds[f++] = 0;
		}
		
		while (idx != -1 && idx <= xml.length()) {
			idx = xml.indexOf(needle, idx+1);
			if (idx != -1) {
				finds[f++] = idx;
			}
		}
		return Arrays.copyOf(finds, f);
	}


	// Sample data:
	//<layer composite-op="svg:src-over" name="Lay  er45" opacity="1.0"  />
	//(we don't receive the < and >. But we DO receive trailing / marks.)
	// .. Could make antagonistic testdata that uses \" inside some text?
	
	private static ArrayList<String> tokenizeAttrs(String tagInnards) {
		// switch delimiter on *unescaped* quotation marks.
		// We split on whitespace, except when inside quotes.
		
		//if we're in 'inquotes' mode, watch out for backspaces, and skipnext.
		int i = 0;
		boolean inQuote = false,
				inToken = false;
		
		ArrayList<String> tokens = new ArrayList<>(tagInnards.length()/5);
		//it'd be unlikely to get a string with tokens denser than a="x" !
		StringBuilder curToken = new StringBuilder();
		
		char c, oldc = ' ';	// i.e. initially inToken is false.
		
		while (i < tagInnards.length()) {
			c = tagInnards.charAt(i);
			
			if (c == '"' && (!inQuote || oldc != '\\')) {
				inQuote = !inQuote;
				curToken.append(c);
				oldc = c;
				i++;
				continue;
			}
			
			// now handle whitespace versus nonwhitespace.
			if (Character.isWhitespace(c) && !inQuote) {
				if (inToken) {
					tokens.add(curToken.toString());
					curToken.setLength(0);	// (clear stringbuilder)
				}
				inToken = false;
			} else {
				// add current char to a current word buffer.
				curToken.append(c);
				inToken = true;
			}
			oldc = c;
			i++;
		}
		
		if (curToken.length() != 0) {
			// dump initial-last or unterminated token!
			tokens.add(curToken.toString());
		}
		return tokens;
	}


	private static TagAndLeftovers consumeNextTag(XMLTag parent, String xml,
			int treeDepth) {
		// this check is 'isBlank', not 'is empty string'. ie ignore whitespace.
		if (isBlank(xml)) {	// or plaintext!!!
			// Java 11's String.isBlank() uses Character.isWhitespace to see if
			// everything in the string is actually whitespace and in that case
			// it returns true! 'isblank'! NOT 'isempty'.
			return null;
		}
		
		XMLTag t = new XMLTag();
		
		int startOfStartTag = xml.indexOf('<');
		
		char afterStart = xml.charAt(startOfStartTag+1);
		String matchEndOfStart = ">";
		if (afterStart == '!') {
			if (xml.charAt(startOfStartTag+2) == '-' &&
				xml.charAt(startOfStartTag+3) == '-')
			{
				t.tagtype = XMLTag.COMMENT;
				startOfStartTag = startOfStartTag + 3;	//<!--
				//Now points to interior of comment. We're STRIPPING
				//its start/end delimiters. Same for Cdata.
				matchEndOfStart = "-->";
			} else {
				String cdMatch = xml.substring(startOfStartTag+2, startOfStartTag+9);
				if (cdMatch.equals("[CDATA[")) {
					t.tagtype = XMLTag.CDATA;
				}
				startOfStartTag = startOfStartTag + 8;	//"<![CDATA["
				matchEndOfStart = "]]>";
			}
		}
		
		int endOfStartTag = xml.indexOf(matchEndOfStart, startOfStartTag);
		if (endOfStartTag == -1) {
			//The 'xml' variable right now is Unescaped (unmarked) cdata!!!
			t.tagtype = XMLTag.CDATA_UNMARKED;	// raw? undelimited?
			t.innerXMLAsText = xml;
			// no leftovers in this case.
			return new TagAndLeftovers(t, null);
		}
		
		String tagdef = xml.substring(startOfStartTag+1, endOfStartTag);
		// that tagdef will be the entire cdata or comment, if it's cdata or a
		// comment.
		
		if (t.tagtype == XMLTag.COMMENT) {
			// shim and return! (+leftover). No descent. Because it's FLAT.
			if (ATT_DEBUG) {
				System.err.println("COMMENT-TAG: {{"+tagdef+"}}");
			}
			t.name = "comment";
			t.innerXMLAsText = tagdef;
			int afterEndComment = endOfStartTag + matchEndOfStart.length();
			String lefto = xml.substring(afterEndComment);
			
			// Comment is SINGLETON. its leftovers are the WHOLE REST OF FILE.
			
			return new TagAndLeftovers(t, lefto);
			
		} else if (t.tagtype == XMLTag.CDATA) {
			if (ATT_DEBUG) {
				System.err.println("CDATA-TAG: {{"+tagdef+"}}");
			}
			int afterEndCDATA = endOfStartTag + matchEndOfStart.length();
			String lefto = xml.substring(afterEndCDATA);
			t.name = "cdata";
			t.innerXMLAsText = tagdef;
			return new TagAndLeftovers(t, lefto);
		} else {
			if (ATT_DEBUG) {
				System.err.println("START-TAG: {{"+tagdef+"}}");
			}
		}
		
		boolean singleton = t.tagtype == XMLTag.TAG && tagdef.endsWith("/");
		if (singleton) {
			tagdef = tagdef.substring(0, tagdef.length()-1);	// strip the '/'
			t.isSingleton = true;
		}
		
		// new case/problem (thanks, J!): CDATA. Can be interior contents of
		// some other tag, so it's the first thing you get:
		// <![[CDATA[ Arbitrary formatted raw text ]]>
		// also: comments. <!-- arbitrary text -->
		
		// Create new tag XML node.
		
		ArrayList<String> nameAtts = tokenizeAttrs(tagdef);
		t.name = nameAtts.get(0);
		
		// IF <layer ... /> -> endofStartTag has '/' right before it.
		ArrayList<Att> atts = new ArrayList<>();
		for (int a=1; a<nameAtts.size(); a++) {
			// split attrs on = and "". We don't tolerate unquoted values.
			atts.add(new Att(attsplit(nameAtts.get(a))));
		}
		t.atts = atts.toArray(new Att[0]);
		
		int endOfTagContent = endOfStartTag;
		String leftoverXML;
		if (!singleton) {
			// Must find the </tag>, and do recursive inner processing with this
			// new tag as the parent, and return back up and add the
			// 'leftoverXML' processing after.
			endOfTagContent = findEndTag(xml, t.name);
			
			// String endtag = "</"+t.name+">";
			int endTagLen = t.name.length()+3;	// No tolerance for nonsense.
			String innerXML = xml.substring(endOfStartTag+1, endOfTagContent);
			int startOfTheRest = endOfTagContent + endTagLen;
			if (startOfTheRest >= xml.length()) {
				leftoverXML = "";
			} else {
				leftoverXML = xml.substring(startOfTheRest);
			}
			t.innerXMLAsText = innerXML;
			if (innerXML.length() > 0) {
				parseTags(t, innerXML, treeDepth+1);
			}
			
		} else {
			// it was singleton:
			int startOfTheRest = endOfTagContent + 1;
			leftoverXML = xml.substring(startOfTheRest);
		}
		
		return new TagAndLeftovers(t, leftoverXML);
	}
	
	
	// Let's handle *NO ERRORS AT ALL*!
	private static void parseTags(XMLTag parentTag, String inputXML, int depth) {
		if (isBlank(inputXML)) {	//.length() == 0
			return;
		}
		
		TagAndLeftovers tl = consumeNextTag(parentTag, inputXML, depth);
		if (PARSETAG_DEBUG) {
			if (tl != null) {
				System.err.format("parseTags tag+leftovers: {%s} {%s}\n",
					tl.tag.toString(), tl.leftovers);
			} else {
				System.err.format("parseTags tag+leftovers: null!\n");
			}
		}
		
		parentTag.addChild(tl.tag);
		// continue consuming tags until rest of the xml string is used up.
		String leftovers = tl.leftovers;
		// But if any of *those* tags need to recurse, do so.
		while (!isBlank(leftovers)) {
			TagAndLeftovers nextResult = consumeNextTag(parentTag, leftovers, depth);
			parentTag.addChild(nextResult.tag);
			leftovers = nextResult.leftovers;
		}
	}

	// or bump the java version to > 11.
	private static boolean isBlank(String s) {
		if (s == null || s.length() <= 0) {
			return true;
		};
		// return is ALL WHITESPACE (newline carriage return tab space etc.)
		for (char ch: s.toCharArray()) {
			if (!Character.isWhitespace(ch)) {
				return false;
			}
		}
		return true;
	}


	private static String[] attsplit(String txt) {
		// Make a string of form name="value" into name,value.
		if (ATT_DEBUG) {
			System.err.println("Splitting attrs {"+txt+"}");
		}
		String[] parts = txt.split("=");
		parts[1] = parts[1].substring(1, parts[1].length()-1);
		return parts;
	}


	// API function for easy use.
	public static XMLTag xmlFromStream(InputStream is) {
		InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		BufferedReader br = new BufferedReader(isr);
		String x = br.lines().parallel().collect(Collectors.joining("\n"));
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlFromString(x);
	}
	
	
	public static XMLTag xmlFromString(String xml) {
		xml = stripXMLDeclaration(xml);
		
		XMLTag rootTag = new XMLTag();
		
		parseTags(rootTag, xml, 0);
		
		return rootTag;
	}
	
	
}