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
	
	public void checkForRankup(Player p){
		int combat = WubData.GXP_MOB.getData(p, main).asInt();
		int tree = WubData.GXP_TREE.getData(p, main).asInt();
		int rock = WubData.GXP_ROCK.getData(p, main).asInt();
		int sell = WubData.GXP_SELL.getData(p, main).asInt();
		int cook = WubData.GXP_COOK.getData(p, main).asInt();
		int dig = WubData.GXP_DIG.getData(p, main).asInt();
		int gxp = WubData.GXP.getData(p, main).asInt();
		int needed = WubData.GXP_NEEDED.getData(p, main).asInt();
		if (combat >= 100 && tree >= 100 && rock >= 100 && cook >= 100 && dig >= 100 && sell >= (WubData.RANK.getData(p, main).asInt() + 1) * 5000){
			WubData.GXP.setData(p, gxp + 100, main);
			gxp = WubData.GXP.getData(p, main).asInt();
			main.updateDisplayBar(p, "&b\u15D1 GXP: " + gxp + " &f/&b " + needed);
			main.sendMessage(p, "&bYou have earned 100 GXP. Your collection limits have been reset for another cycle.");
			WubData.GXP_COOK.setData(p, 0, main);
			WubData.GXP_TREE.setData(p, 0, main);
			WubData.GXP_ROCK.setData(p, 0, main);
			WubData.GXP_SELL.setData(p, 0, main);
			WubData.GXP_MOB.setData(p, 0, main);
			WubData.GXP_DIG.setData(p, 0, main);
			if (gxp >= needed){
				WubData.GXP.setData(p, 0, main);
				WubData.GXP_NEEDED.setData(p, needed + 100, main);
				rankUp(p);
			}
		}
	}
	
	public void rankUp(Player p){
		int currRank = WubData.RANK.getData(p, main).asInt();
		if (currRank < WubData.values().length - 1){
			WubData.RANK.setData(p, currRank+1, main);
			main.broadcast(p.getDisplayName() + " &2has ranked up to " + getRankWithColor(p) + "&2!");
		} else {
			//main.sendMessage(p, "&c&oYou're already max rank.");
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