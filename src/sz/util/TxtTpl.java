package sz.util;

import crl.player.Player;

public class TxtTpl {
	
	// REPL_TOKENS = {"%n", "%he", "%him", "%his"};
	// TODO Could use 0==MALE, 1==FEMALE for easy indexing? currently 1,2.
	private static final String[]
		HIS_OR_HER = {"_@_", "his", "her"},
		HIM_OR_HER = {"_@_", "him", "her"},
		HE_OR_SHE  = {"_@_", "he", "she"};
	
	// Could make the tpl keywords be like: '%%he' and '%%name'. and '%%his'. etc.
	// (and %%He (capitalized) could also be useful!?)
	// Would it be 'nicer' to use a single %? That would require special escape
	// for '%' itself. But how often does that appear in txt-templated strings?
	private static final String[]
		REPL_MARKS = {"%%SEX", "%%his", "%%him", "%%he", "%%NAME"};	// case sensitive?
	
	public static String t(Player p, String tpl) {
		return t(p.getName(), p.sex, tpl);
	}
	
	public static String t(String name, int sex, String tpl) {
		//String heshe = HE_OR_SHE[sex];
		String hisher = HIS_OR_HER[sex];
		String himher = HIM_OR_HER[sex];
		String heshe = HE_OR_SHE[sex];
		//TODO Loop indexof %% then consume/replace token, output to sbuilder.
		// can also then hash lookup replacements instead of looping, and print
		// errmessages if finding unrecognised tokens.
		String[] replacements = {hisher, hisher, himher, heshe, name};
		return replace(REPL_MARKS, replacements, tpl);
	}
	
	
	public static String replace(String[] marks, String[] replacements, String source) {
		String ret = source;
		for (int i = 0; i < marks.length; i++) {
			ret = ret.replaceAll(marks[i], replacements[i]);
		}
		return ret;
	}
}
