package sz.util;

public class Position implements java.io.Serializable {
	public int x,y,z;

	public Position(int px, int py) {
		x = px;
		y = py;
	}

	public Position(int px, int py, int pz) {
		x = px;
		y = py;
		z = pz;
	}

	public Position(Position p) {
		x = p.x;
		y = p.y;
		z = p.z;
	}

	public static Position add(Position a, Position b) {
		return new Position(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	public static Position subs(Position a, Position b) {
		return new Position(a.x - b.x, a.y - b.y, a.z - b.z);
	}

	public static Position mul(Position a, int c) {
		return new Position(a.x * c, a.y * c, a.z * c);
	}

	public static Position mul(Position a, Position b) {
		return new Position(a.x * b.x, a.y * b.y, a.z * b.z);
	}

	public void mul(Position pos) {
		x *= pos.x;
		y *= pos.y;
		z *= pos.z;
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		try {
			if (((Position)o).x == x && ((Position)o).y == y && ((Position)o).z == z) {
				return true;
			}
		} catch (ClassCastException cce) {
			Debug.byebye("Error comparing points "+this+" "+o);
		}
		return false;
	}
	
	public int hashCode() {
		return toString().hashCode();
	}


	public String toString() {
		return "("+x+","+y+","+z+")";
	}

	public static int flatDistance(Position a, Position b) {
		int dx = a.x - b.x,
			dy = a.y - b.y;
		return (int)Math.sqrt(dx*dx + dy*dy);
	}

	public static int flatDistance(int x1, int y1, int x2, int y2) {
		int dx = x1 - x2,
			dy = y1 - y2;
		return (int)Math.sqrt(dx*dx + dy*dy);
	}
	
	// Is this an err in naming? Shouldn't this also do 'z'?
	public static int distance(Position a, Position b) {
		return (int)Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}
	
/*	public static int distance(Position a, Position b){
		return (int) Math.s(Math.pow(a.x - b.x, 2) + Math.pow (a.y - b.y, 2));
	}*/

	public void add(Position p) {
		x += p.x;
		y += p.y;
		z += p.z;
	}


	public static final int ihash(int x, int y, int z) {
		int hash =
			((z << 24) & 0xff000000) |
			((y << 12) & 0x00fff000) |
			((x      ) & 0x00000fff);
		return hash;
	}
	
	public int ihash() {
		return ihash(x, y, z);
	}

}