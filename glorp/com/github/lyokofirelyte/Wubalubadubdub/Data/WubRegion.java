package com.github.lyokofirelyte.Wubalubadubdub.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;

public class WubRegion extends JSONObject {
	
	private Wub main;
	private String name;
	
	public WubRegion(String n, Wub i) {
		main = i;
		name = n;
	}
	
	public WubRegion(String name, Wub i, JSONObject o){
		for (Object obj : o.keySet()){
			put(obj, o.get(obj));
		}
		this.main = i;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	public int getPriority(){
		return getInt(WubRegionInfo.PRIORITY);
	}
	
	public int getLength(){
		return getInt(WubRegionInfo.LENGTH);
	}
	
	public int getWidth(){
		return getInt(WubRegionInfo.WIDTH);
	}
	
	public int getHeight(){
		return getInt(WubRegionInfo.HEIGHT);
	}
	
	public int getArea(){
		return getInt(WubRegionInfo.AREA);
	}
	
	public String getMaxBlock(){
		return getStr(WubRegionInfo.MAX_BLOCK);
	}
	
	public String getMinBlock(){
		return getStr(WubRegionInfo.MIN_BLOCK);
	}
	
	public boolean isDisabled(){
		return getBool(WubRegionInfo.DISABLED);
	}
	
	public boolean getFlag(WubFlag flag){
		return getBool(flag);
	}
	
	public void set(Enum<?> o, Object value){
		put(o.toString(), value);
	}
	
	public Map<WubFlag, Boolean> getFlags(){
		Map<WubFlag, Boolean> flagMap = new HashMap<>();
		for (WubFlag f : WubFlag.values()){
			if (containsKey(f.toString())){
				flagMap.put(f, getBool(f));
			}
		}
		return flagMap;
	}
	
	public boolean canBuild(org.bukkit.entity.Player p){
		
		for (String perm : (List<String>)getList(WubRegionInfo.PERMS)){
			if (WubData.PERMS.getData(p, main).asListString().contains(perm)){
				return true;
			}
		}
		
		return getBool(WubRegionInfo.DISABLED) ? true : false;
	}

	public World world() {
		return Bukkit.getWorld(getStr(WubRegionInfo.WORLD));
	}

	public String getWorld() {
		return getStr(WubRegionInfo.WORLD);
	}

	public List<String> getPerms() {
		return getList(WubRegionInfo.PERMS);
	}
	
	public List<String> getList(WubRegionInfo i){
		List<String> list = new ArrayList<String>();
		try {
			JSONArray array = (JSONArray) get(i.toString());
			for (Object thing : array){
				list.add(thing.toString());
			}
		} catch (Exception e){
			List<String> array = (List<String>) get(i.toString());
			return array;
		}

		return list;
	}
	
	public int getInt(WubRegionInfo i){
		return Integer.parseInt(get(i.toString()).toString());
	}
	
	public boolean getBool(WubRegionInfo i){
		return Boolean.parseBoolean(get(i.toString()).toString());
	}
	
	public boolean getBool(WubFlag i){
		return Boolean.parseBoolean(get(i.toString()).toString());
	}
	
	public String getStr(WubRegionInfo i){
		return get(i.toString()).toString();
	}
}