package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;

public class CommandStaff implements Listener {
	
	private Wub main;
	
	public CommandStaff(Wub w){
		main = w;
	}

	@WubCommand(commands = {"staff"}, perm = "wub.staff.dev", help = "/staff", desc = "Admin staff command")
	public void onStaff(String[] args, Player p){
		
	}	
}