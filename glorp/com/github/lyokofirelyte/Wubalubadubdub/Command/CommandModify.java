package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandModify {
	
	private Wub main;
	
	public CommandModify(Wub w){
		main = w;
	}

	@WubCommand(commands = {"modify"}, desc = "Admin modify command", help = "/modify <attr> <player> <val>", perm = "wub.staff.dev")
	public void onModify(String[] args, Player p){
		try {
			WubData.valueOf(args[0]).setData(Bukkit.getPlayer(args[1]), args[2], main);
			main.sendMessage(p, "&aUpdated value of " + args[0] + " to " + args[2] + " for " + args[1] + ".");
			main.sendMessage(Bukkit.getPlayer(args[1]), "&aUpdated value of " + args[0] + " to " + args[2] + " for " + args[1] + ".");
		} catch (Exception e){}
	}
}