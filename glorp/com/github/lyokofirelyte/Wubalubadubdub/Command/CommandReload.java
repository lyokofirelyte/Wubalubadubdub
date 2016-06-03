package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandReload {
	
	private Wub main;
	
	public CommandReload(Wub w){
		main = w;
	}

	@WubCommand(commands = {"rel"}, desc = "Admin reload command", help = "/rel")
	public void onReload(String[] args, Player p){
		if (p.getName().equals("Hugh_Jasses")){
			main.broadcast("&5Server reload! Hold on to your socks!");
			main.broadcast("&5&oAll players are invulnerable during this time...");
			for (Player player : Bukkit.getOnlinePlayers()){
				WubData.INVULN.setData(player, 1, main);
			}
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "reload");
		}
	}
}