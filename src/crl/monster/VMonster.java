package crl.monster;

import java.util.*;

import sz.util.*;

@Deprecated
public class VMonster implements java.io.Serializable{
	private static final long serialVersionUID = -8389021604207397015L;
	
	private Vector<Monster> monsters;

	public void addMonster(Monster what) {
		monsters.add(what);
//		Debug.say(what.getPosition().toString());
		//mLocs.put(what.getPosition().toString(), what);
	}

	public Monster elementAt(int i){
		return (Monster) monsters.elementAt(i);
	}
	
	public boolean contains(Monster who){
		return monsters.contains(who);
	}

	public void removeAll(Collection<Monster> c){
		monsters.removeAll(c);
	}

	public Enumeration<Monster> elements(){
		return monsters.elements();
	}

	public Monster getMonsterAt(Position p){
		for (int i = 0; i < monsters.size(); i++)
			if (((Monster) monsters.elementAt(i)).pos.equals(p))
				return (Monster) monsters.elementAt(i);
		return null;
	}

	public VMonster(int size){
		monsters = new Vector<Monster>(size);
	}

	public Vector<Monster> getVector(){
		return monsters;
	}

	public void remove(Object o){
		monsters.remove(o);
	}

	public int size(){
		return monsters.size();
	}
	
	public void removeAll(){
		monsters.removeAllElements();
	}

}