package nox;

public final class XMLTag {
	
	public static final byte
		TAG = 0,
		COMMENT = 1,
		CDATA = 2,
		CDATA_UNMARKED = 3;	// fault tolerance of tag inners with no structure.
	
	public byte tagtype = TAG;
	
	public String name;
	public Att[] atts;
	
	public String innerXMLAsText;	// the 'body' xml inside this's 'contents'.
	public XMLTag[] children;
	public XMLTag parent;

	// Singletons: write back out as <tag />, not <tag></tag>.
	public boolean isSingleton = false;


	public void addChild(XMLTag childTag) {
		if (children == null) {
			children = new XMLTag[] {childTag};
			childTag.parent = this;
			return;
		}
		int clen = children.length;
		XMLTag[] newkids = new XMLTag[clen+1];
		// could 2x the len and all that?
		System.arraycopy(children, 0, newkids, 0, clen);
		newkids[clen] = childTag;
		childTag.parent = this;
		children = newkids;
	}
	
	
	public int getIntAttr(String attName) {
		String atn = attName.toLowerCase();
		for (int a=0; a<atts.length; a++) {
			if (atts[a].name.equals(atn)) {
				return Integer.parseInt(atts[a].value);
			}
		}
		return 0;
	}
	
	
	public String getStrAttrOrDefault(String attName, String defaultValue) {
		String atn = attName.toLowerCase();
		for (int a=0; a<atts.length; a++) {
			if (atts[a].name.equals(atn)) {
				return atts[a].value;
			}
		}
		return defaultValue;
	}
	
	public String getStrAttr(String attName) {
		String atn = attName.toLowerCase();
		for (int a=0; a<atts.length; a++) {
			if (atts[a].name.equals(atn)) {
				return atts[a].value;
			}
		}
		return "";	// do we need a better nullable check to tell if hasatt?
	}
	
	
	public float getFloatAttr(String attName) {
		String atn = attName.toLowerCase();
		for (int a=0; a<atts.length; a++) {
			if (atts[a].name.equals(atn)) {
				return Float.parseFloat(atts[a].value);
			}
		}
		return 0;
	}
	
	
	public String toString(final String indent, final String lbr) {
		if (atts == null || atts.length == 0) {
			if (tagtype == CDATA) {
				return indent+"<![CDATA["+innerXMLAsText+"]]>";
			} else if (tagtype == CDATA_UNMARKED) {
				return innerXMLAsText;	// raw. kinda 'all bets are off'.
			} else if (tagtype == COMMENT) {
				return indent+"<!--"+innerXMLAsText+"-->";
			}
			String se = "";
			if (isSingleton) {
				se = " /";
			}
			return indent+"<"+name+se+">";
			// FIXME: Looks like an error!? -> tags with no attrs don't write
			// out their *CONTENTS*!? (i.e. not singletons, here.)
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(indent);
		sb.append('<');
		sb.append(name);
		sb.append(' ');
		
		if (atts.length > 1) {
			for (int a=0; a<atts.length-1; a++) {
				sb.append(atts[a].toString());
				sb.append(" ");
			}
		}
		sb.append(atts[atts.length-1].toString());	// Last has no space after
		if (isSingleton) {
			sb.append(" />");
			sb.append(lbr);		// mayyybe.
		} else {
			sb.append('>');
		}
		
		if (innerXMLAsText == null || innerXMLAsText.equals("")) {
			if (!isSingleton) {
				sb.append("</");
				sb.append(name);
				sb.append('>');
				sb.append(lbr);
			}
			return sb.toString();
		}
		
		sb.append(innerXMLAsText);
		sb.append("</");
		sb.append(name);
		sb.append('>');
		sb.append(lbr);
		// extra line after tag. Used to space out john's files (optional?)
		sb.append(indent);
		sb.append(lbr);
		
		return sb.toString();
	}
	
	
	@Override
	public String toString() {
		return toString("", "\n");
	}
	
	
	// start an in-order traversal search for the first tag named 'tagname'.
	public XMLTag findTag(String tagname) {
		if (tagname.equals(name)) {
			return this;
		}
		
		if (children == null || children.length < 1) {
			return null;	// not found in this subtree.
		}
		for (XMLTag child: children) {
			XMLTag foundInChild = child.findTag(tagname);
			if (foundInChild != null) {
				return foundInChild;
			}
		}
		return null;
	}
	
	// found? seektag?
	// public XMLTag findTags(String tagname) {	//-> it's listChildrenOfType(..)
	//		//find all children matching the given name. Wait - why not just
	// loop it yourself though?
	// }
	
}