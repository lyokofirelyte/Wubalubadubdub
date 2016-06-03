package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandHome implements Listener {
	
	private Wub main;
	
	public CommandHome(Wub w){
		main = w;
	}

	@WubCommand(commands = {"home"}, desc = "Return home command", help = "/home")
	public void onHome(String[] args, Player p){
		String home = WubData.HOMES.getData(p, main).asString();
		if (!home.equals("none")){
			if (main.isCooldownFinished(p, "home")){
				String[] spl = home.split(" ");
				Location homeLoc = new Location(Bukkit.getWorld(spl[0]), toInt(spl[1]), toInt(spl[2]), toInt(spl[3]), toFloat(spl[4]), toFloat(spl[5]));
				p.teleport(homeLoc);
				main.addCooldown(p, "home", 60 * 1000L);
			} else {
				main.cooldownMessage(p, "home");
			}
		} else {
			main.sendMessage(p, "You have no homes.");
		}
	}
	
	@WubCommand(commands = {"sethome"}, desc = "Set home command", help = "/sethome")
	public void onSetHome(String[] args, Player p){
		Location l = p.getLocation();
		WubData.HOMES.setData(p, l.getWorld().getName() + " " + l.getBlockX() + " " + (l.getBlockY() + 1) + " " + l.getBlockZ() + " " + l.getYaw() + " " + l.getPitch(), main);
		main.sendMessage(p, "New home set here!");
	}
	
	private int toInt(String i){
		return Integer.parseInt(i);
	}
	
	private float toFloat(String i){
		return Float.parseFloat(i);
	}	
}