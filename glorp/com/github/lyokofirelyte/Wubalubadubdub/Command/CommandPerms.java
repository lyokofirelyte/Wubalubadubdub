package com.github.lyokofirelyte.Wubalubadubdub.Command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandPerms implements Listener {
	
	private Wub main;
	
	public CommandPerms(Wub w){
		main = w;
	}

	@WubCommand(commands = {"perms"}, help = "/perms <add/remove/view> <player> [perm]")
	public void onPerms(String[] args, Player p){
		if (p.isOp()){
			if (args.length == 2 && args[0].equals("view")){
				Player lookup = Bukkit.getPlayer(args[1]);
				if (lookup != null){
					for (String perm : WubData.PERMS.getData(lookup, main).asListString()){
						main.sendMessage(p, "&e + " + perm);
					}
				} else {
					main.sendMessage(p, "&c&oThey're not online.");
				}
			} else if (args.length == 3){
				Player lookup = Bukkit.getPlayer(args[1]);
				if (lookup != null){
					List<String> perms = WubData.PERMS.getData(p, main).asListString();
					if (args[0].equals("add")){
						perms.add(args[2]);
					} else {
						perms.remove(args[2]);
					}
					WubData.PERMS.setData(lookup, perms, main);
					main.sendMessage(p, "&aSuccess!");
				} else {
					main.sendMessage(p, "&c&oThey're not online.");
				}
			}
		} else {
			main.sendMessage(p, "&c&oNo permissions!");
		}
	}	
}