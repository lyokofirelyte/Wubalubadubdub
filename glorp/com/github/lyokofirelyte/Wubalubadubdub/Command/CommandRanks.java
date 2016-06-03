package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;

public class CommandRanks implements Listener {
	
	private Wub main;
	
	public CommandRanks(Wub w){
		main = w;
	}

	@WubCommand(commands = {"ranks"}, perm = "wub.lub", help = "/ranks", desc = "Rank Information")
	public void onRanks(String[] args, Player p){
		main.sendMessage(p, 
			"&6Rank up by earning GXP. GXP is broken up into 5 parts:", 
			"&eCombat&f: &eKilling monsters",
			"&eLogging&f: &eChopping trees",
			"&eMining&f: &eMining rocks or dirt",
			"&eTrade&f: &eSelling things in the market",
			"&eFood&f: &eCooking things",
			"&6Each part has a limit you must reach. After all of the limits are reached, GXP is granted.",
			"&6&oOnce enough GXP is gained, you will rank up. Your limits are reset when you gain GXP."
		);
	}	
}