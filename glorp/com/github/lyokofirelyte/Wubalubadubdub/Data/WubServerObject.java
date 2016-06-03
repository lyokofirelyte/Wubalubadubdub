package com.github.lyokofirelyte.Wubalubadubdub.Data;

import org.json.simple.JSONObject;

public class WubServerObject extends JSONObject {
	
	public WubServerObject(){
		defaults();
	}
	
	public WubServerObject(JSONObject o){
		defaults();
		for (Object key : o.keySet()){
			put(key, o.get(key));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void defaults(){
		put("build", 10);
	}
}