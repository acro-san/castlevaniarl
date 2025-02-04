package crl.actor;

import sz.util.Position;

public class Message {
	public String text;
	public Position location;

/*	public void act(){
		die();
	} */

	public Message(String pText, Position pLocation){
		text = pText;
		location = pLocation;
	}

	@Override
	public String toString(){
		return text;//getText();
	}
}