package com.github.lyokofirelyte.Wubalubadubdub.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;

public class CommandZone {
	
	private Wub main;
	private Map<String, Integer> tasks = new HashMap<>();
	
	public CommandZone(Wub w){
		main = w;
	}

	@WubCommand(commands = {"zone"}, help = "/zone", desc = "Manage your protection zones!")
	public void onZone(String[] args, Player p){
		if (args.length > 0 || !args[0].equals("zone")){
			
		} else {
			main.sendMessage(p, "&c&o/zone");
		}
	}
}