package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemProtect;

public class CommandCP {
	
	private Wub main;
	
	public CommandCP(Wub w){
		main = w;
	}

	@WubCommand(commands = {"cp", "checkpoint"}, help = "/cp", desc = "Return to your checkpoint")
	public void onCP(String[] args, Player p){
		if (!WubData.CHECKPOINT.getData(p, main).asString().equals("none")){
			if (((SystemProtect) main.getInstance(SystemProtect.class)).isInRegion(p.getLocation(), "emerald")){
				p.teleport(main.stringToLoc(WubData.CHECKPOINT.getData(p, main).asString()).add(0, 1, 0));
				main.sendMessage(p, "&aReturned to checkpoint!");
			} else {
				main.sendMessage(p, "&c&oYou can't do that from here.");
			}
		} else {
			main.sendMessage(p, "&c&oYou don't have any checkpoints set!");
		}
	}
}