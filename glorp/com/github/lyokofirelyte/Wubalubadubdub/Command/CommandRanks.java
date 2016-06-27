package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandRanks implements Listener {
	
	private Wub main;
	
	public CommandRanks(Wub w){
		main = w;
	}

	@WubCommand(commands = {"ranks", "stats"}, perm = "wub.lub", help = "/ranks", desc = "Rank Information")
	public void onRanks(String[] args, Player p){
		main.sendMessage(p, 
			"&6Rank up by earning GXP. GXP is broken up into 5 parts:", 
			"&eCombat&f: &eKilling monsters (" + WubData.GXP_MOB.getData(p, main).asInt() + "%)",
			"&eLogging&f: &eChopping trees (" + WubData.GXP_TREE.getData(p, main).asInt() + "%)",
			"&eMining&f: &eMining rocks or gems (" + WubData.GXP_ROCK.getData(p, main).asInt() + "%)",
			"&eDigging&f: &eMining dirt, gravel or sand (" + WubData.GXP_DIG.getData(p, main).asInt() + "%)",
			"&eTrade&f: &eSelling things in the market (" + ((WubData.GXP_SELL.getData(p, main).asInt() * 100) / ((WubData.RANK.getData(p, main).asInt() + 1)*3500)) + "%)",
			"&eFood&f: &eCooking things (" + WubData.GXP_COOK.getData(p, main).asInt() + "%)",
			"&6Each part has a limit you must reach. After all of the limits are reached, GXP is granted.",
			"&6&oOnce enough GXP is gained, you will rank up. Your limits are reset when you gain GXP."
		);
	}	
}