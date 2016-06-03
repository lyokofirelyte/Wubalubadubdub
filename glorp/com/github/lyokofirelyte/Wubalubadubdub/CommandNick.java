package com.github.lyokofirelyte.Wubalubadubdub;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandNick {
	
	private Wub main;
	
	public CommandNick(Wub w){
		main = w;
	}

	@WubCommand(commands = {"nick"})
	public void onNick(String[] args, Player p){
		if (args.length > 0){
			if (args[0].toLowerCase().startsWith(p.getName().toLowerCase().substring(0, 3))){
				main.broadcast("&7" + p.getDisplayName() + " &6-> &7" + args[0]);
				p.setDisplayName(ChatColor.stripColor(main.AS(args[0])));
				p.setPlayerListName(ChatColor.stripColor(main.AS(args[0])));
			} else {
				main.sendMessage(p, "&c&oYour nick must start with the first 3 letters of your name.");
			}
		} else {
			main.sendMessage(p, "&c&o/nick <name>");
		}
	}
}