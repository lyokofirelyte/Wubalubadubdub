package com.github.lyokofirelyte.Wubalubadubdub.System;

import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubRank;

public class SystemRanks {

	private Wub main;
	
	public SystemRanks(Wub i){
		this.main = i;
	}
	
	public String getRank(Player p){
		return rankFromInt(WubData.RANK.getData(p, main).asInt());
	}
	
	public String getRankWithColor(Player p){
		return getRankColor(p) + getRank(p);
	}
	
	public String getRankColor(Player p){
		return WubRank.valueOf(getRank(p).toUpperCase()).getColor();
	}
	
	public void rankUp(Player p){
		int currRank = WubData.RANK.getData(p, main).asInt();
		if (currRank < WubData.values().length - 1){
			
		} else {
			main.sendMessage(p, "&c&oYou're already max rank.");
		}
	}
	
	private String rankFromInt(int r){
		for (WubRank rank : WubRank.values()){
			if (rank.getRank() == r){
				return rank.toString().toLowerCase().substring(0, 1).toUpperCase() + rank.toString().toLowerCase().substring(1);
			}
		}
		
		return "none";
	}
}