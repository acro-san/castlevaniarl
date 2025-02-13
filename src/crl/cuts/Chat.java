package crl.cuts;

import java.util.ArrayList;

import crl.conf.gfx.data.Textures;

public class Chat {	// Dialogue?
	
	// Could do as:
	// class Name,Text,PortraitID. <- one struct. then have an AoS. simpler re-
	// ordering, and access via: get(i) and then .name, .text, .portraitID.
	
	// SoA! (WHY!?)
	private ArrayList<String> names = new ArrayList<>(10);
	private ArrayList<String> conversations = new ArrayList<>(10);
	private ArrayList<Byte> portraitIDs = new ArrayList<>(10);
	
	public void add(String name, String conversation, byte portraitIndex) {
		names.add(name);
		conversations.add(conversation);
		portraitIDs.add(portraitIndex);
	}
	
	public void add(String name, String conversation) {
		names.add(name);
		conversations.add(conversation);
		portraitIDs.add(Textures.PRT_NONE);
	}
	
	public int getConversations() {
		return conversations.size();
	}
	
	public String getConversation(int i) {
		return conversations.get(i);
	}
	
	public String getName(int i) {
		return names.get(i);
	}
	
	public byte getPortraitIndex(int i) {
		return portraitIDs.get(i);
	}

}
