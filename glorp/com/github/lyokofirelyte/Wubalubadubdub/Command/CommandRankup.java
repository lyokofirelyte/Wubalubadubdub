package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;

public class CommandRankup {
	
	private Wub main;
	
	public CommandRankup(Wub w){
		main = w;
	}

	@WubCommand(commands = {"rankup"}, desc = "Manual rankup command!", help = "/rankup")
	public void onReload(String[] args, Player p){
		if (main.isCooldownFinished(p, "rankup")){
			((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);
			main.addCooldown(p, "rankup", 60 * 1000L);
			main.sendMessage(p, "Checked for rankup!");
		} else {
			main.cooldownMessage(p, "rankup");
		}

	}
}