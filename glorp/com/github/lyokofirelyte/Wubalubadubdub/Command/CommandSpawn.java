package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandSpawn {

	private Wub main;
	
	public CommandSpawn(Wub w){
		main = w;
	}

	@WubCommand(commands = {"spawn"}, help = "/spawn", desc = "Go to spawn!")
	public void onSpawn(String[] args, Player p){
		if(main.serverObject.get("spawn") != null) {
			String[] spl = main.serverObject.get("spawn").toString().split(" ");
			Location homeLoc = new Location(Bukkit.getWorld(spl[0]), toInt(spl[1]), toInt(spl[2]), toInt(spl[3]), toFloat(spl[4]), toFloat(spl[5]));
			p.teleport(homeLoc);
		}
	}
	
	@WubCommand(commands = {"setspawn"}, help = "/setspawn", desc = "Set the spawn!")
	public void onSetSpawn(String[] args, Player p){
		if(p.isOp()) {
			Location l = p.getLocation();
			main.serverObject.put("spawn", l.getWorld().getName() + " " + l.getBlockX() + " " + (l.getBlockY() + 1) + " " + l.getBlockZ() + " " + l.getYaw() + " " + l.getPitch());
		}
	}
	
	private int toInt(String s) {
		return Integer.parseInt(s);
	}
	
	private float toFloat(String s) {
		return Float.parseFloat(s);
	}
	
}
