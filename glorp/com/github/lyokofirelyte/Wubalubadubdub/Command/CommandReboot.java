package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;

public class CommandReboot {
	
	private Wub main;
	
	public CommandReboot(Wub w){
		main = w;
	}

	@WubCommand(commands = {"reboot"}, help = "/reboot <msg>", desc = "Reboots the server!")
	public void onNick(String[] args, Player p){
		if(p.isOp()) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.kickPlayer("Worlds apart is restarting!");
			}
			Bukkit.getServer().shutdown();
		}
	}
	
}
