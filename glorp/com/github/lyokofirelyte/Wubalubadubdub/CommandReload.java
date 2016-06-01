package com.github.lyokofirelyte.Wubalubadubdub;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandReload {
	
	private Wub main;
	
	public CommandReload(Wub w){
		main = w;
	}

	@WubCommand(commands = {"rel"})
	public void onReload(String[] args, Player p){
		if (p.getName().equals("Hugh_Jasses")){
			main.broadcast("&5Server reload! Hold on to your socks!");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "reload");
		}
	}
}