package crl.deploy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import sz.crypt.DESEncrypter;

public class MonsterEncrypter {
	public static void main(String[] args){
		try {
			System.out.println("Writing encrypted file");
			DESEncrypter e = new DESEncrypter("65csvlk3489585f9rjh");
			e.encrypt(new FileInputStream("data/monsters.xml"), new FileOutputStream("data/monsters.exml"));
	//		e.encrypt(new FileInputStream("data/monsters.csv"), new FileOutputStream("data/monsters.ecsv"));
			System.out.println("Files written");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}}
