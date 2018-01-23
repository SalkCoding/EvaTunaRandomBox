package net.alkarb.boxmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.alkarb.config.BoxConfig;

public class BoxManager {

	private static ArrayList<Box> list = new ArrayList<Box>();
	
	public static List<Box> getList() {
		return list;
	}
	
	public static Box getBoxName(String name) {
		if(list == null)
			return null;
		for(Box box:list) {
			if(box.getName().equals(name)) {
				return box;
			}
		}
		return null;
	}
	
	public static Box getBox(String displayname) {
		if(list == null)
			return null;
		for(Box box:list) {
			if(box.getDisplayName().equals(displayname)) {
				return box;
			}
		}
		return null;
	}
	
	public static void loadList() {
		try {
			list = BoxConfig.loadBoxList();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
