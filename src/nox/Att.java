package nox;

public final class Att {

	public String name, value;
	
	public Att(String[] nameval) {
		name = nameval[0];	// Alas, we must preserve case for more case-finicky files.
		// despite strict technical rule being to have lowercase atts I think.
		value = nameval[1];
	}
	
	@Override
	public String toString() {
		return String.format("%s=\"%s\"", name, value);
	}
}
