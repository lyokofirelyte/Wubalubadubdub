package com.github.lyokofirelyte.Wubalubadubdub;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;

public enum WubData {

	HOMES("HOMES"),
	PERMS("PERMS");
	
	WubData(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setData(Player p, Object value, Wub main){
		main.playerData.get(p.getUniqueId().toString()).put(name, value);
	}
	
	public WubData getData(Player p, Wub main){
		return getData(p.getUniqueId().toString(), main);
	}
	
	public WubData getData(String uuid, Wub main){
		if (main.playerData.get(uuid).containsKey(name)){
			a = main.playerData.get(uuid).get(getName());
		} else {
			a = "none";
		}
		
		return this;
	}
	
	public String asString(){
		return (String) a;
	}
	
	public int asInt(){
		try {
			return Integer.parseInt("" + a);
		} catch (Exception e){
			return 0;
		}
	}
	
	public float asFloat(){
		try {
			return Float.parseFloat("" + a);
		} catch (Exception e){
			return 0;
		}
	}
	
	public List<String> asListString(){
		JSONArray array = (JSONArray) a;
		List<String> toReturn = new ArrayList<String>();
		for (Object o : array){
			toReturn.add(o.toString());
		}
		return toReturn;
	}
	
	public Object asObject(){
		return a;
	}
	
	private String name;
	private Object a;
}