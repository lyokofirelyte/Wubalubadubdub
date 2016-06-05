package com.github.lyokofirelyte.Wubalubadubdub.Data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;

public enum WubData {

	REGION_PREVIEW("REGION_PREVIEW"),
	GXP("GXP"),
	GXP_NEEDED("GXP_NEEDED"),
	GXP_MOB("GXP_MOB"),
	GXP_TREE("GXP_TREE"),
	GXP_ROCK("GXP_ROCK"),
	GXP_SELL("GXP_SELL"),
	GXP_COOK("GXP_COOK"),
	GXP_DIG("GXP_DIG"),
	TRADING_STICKS("TRADING_STICKS"),
	RANK("RANK"),
	HOMES("HOMES"),
	PERMS("PERMS"),
	NAME("NAME"),
	INVULN("INVULN"),
	STAFF_RANK("STAFF_RANK"),
	DISPLAY_NAME("DISPLAY_NAME"),
	R("R");
	
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
	
	public boolean asBool(){
		try {
			return Boolean.parseBoolean("" + a);
		} catch (Exception e){
			return false;
		}
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
		try {
			JSONArray array = (JSONArray) a;
			List<String> toReturn = new ArrayList<String>();
			for (Object o : array){
				toReturn.add(o.toString());
			}
			return toReturn;
		} catch (Exception e){
			List<String> toReturn = (List<String>) a;
			return toReturn;
		}
	}
	
	public Object asObject(){
		return a;
	}
	
	private String name;
	private Object a;
}