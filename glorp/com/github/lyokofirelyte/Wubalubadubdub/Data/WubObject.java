package com.github.lyokofirelyte.Wubalubadubdub.Data;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONObject;

public class WubObject extends JSONObject {

	public WubObject(){
		defaults();
	}
	
	public WubObject(JSONObject o){
		defaults();
		for (Object key : o.keySet()){
			put(key, o.get(key));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void defaults(){
		put(WubData.GXP.getName(), 0);
		put(WubData.RANK.getName(), WubRank.values()[0].toString());
		put(WubData.PERMS.getName(), new ArrayList<String>(Arrays.asList(
			"wub.lub",
			"wub.rank.0"
		)));
		put(WubData.INVULN.getName(), 0);
		put(WubData.STAFF_RANK.getName(), "");
		put(WubData.GXP_NEEDED.getName(), 100);
		put(WubData.MARKKIT_BOX.getName(), new ArrayList<String>());
	}
}