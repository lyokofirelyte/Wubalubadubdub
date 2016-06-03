package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandStaff implements Listener {
	
	private Wub main;
	
	public CommandStaff(Wub w){
		main = w;
	}

	@WubCommand(commands = {"staff"}, help = "/staff <player>", desc = "Admin staff command")
	public void onStaff(String[] args, Player p){
		if (p.isOp()){
			if (args.length == 2){
				Player toStaff = Bukkit.getPlayer(args[0]);
				if (toStaff != null){
					WubData.STAFF_RANK.setData(toStaff, args[1], main);
					main.sendMessage(toStaff, "&aYou have been given a staff rank by&f: " + p.getDisplayName());
					main.sendMessage(toStaff, "&a&oTo speak officially, type @ in front of your messages.");
					main.sendMessage(toStaff, "&c&oAbuse of this power will result in it being revoked.");
					main.sendMessage(p, "&aSuccess!");
				} else {
					main.sendMessage(p, "&c&oThat player is offline.");
				}
			}
		}
	}
}