package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandNick {
	
	private Wub main;
	
	public CommandNick(Wub w){
		main = w;
	}

	@WubCommand(commands = {"nick"}, help = "/nick <name>", desc = "Change your name!")
	public void onNick(String[] args, Player p){
		if (args.length > 0){
			if (args[0].toLowerCase().startsWith(p.getName().toLowerCase().substring(0, 3))){
				main.broadcast("&7" + p.getDisplayName() + " &6-> &7" + args[0]);
				p.setDisplayName(ChatColor.stripColor(main.AS(args[0])));
				p.setPlayerListName(ChatColor.stripColor(main.AS(args[0])));
				WubData.DISPLAY_NAME.setData(p, ChatColor.stripColor(main.AS(args[0])), main);
			} else {
				main.sendMessage(p, "&c&oYour nick must start with the first 3 letters of your name.");
			}
		} else {
			main.sendMessage(p, "&c&o/nick <name>");
		}
	}
}