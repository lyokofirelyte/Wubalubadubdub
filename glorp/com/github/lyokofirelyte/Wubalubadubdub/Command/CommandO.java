package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandO {
	
	private Wub main;
	
	public CommandO(Wub w){
		main = w;
	}

	@WubCommand(commands = {"o"}, help = "/o <msg>", desc = "Talk in o chat!")
	public void onNick(String[] args, Player p){
		String staffRank = WubData.STAFF_RANK.getData(p, main).asString();
		if (staffRank.length() > 2){
			for (Player staff : Bukkit.getOnlinePlayers()){
				if (WubData.STAFF_RANK.getData(staff, main).asString().length() > 2){
					staff.sendMessage(main.AS("&4\u1445 Staff \u1440 &7" + p.getDisplayName() + " > &c" + main.argsToString(args)));
				}
			}
		} else {
			main.sendMessage(p, "&c&oYou don't have permission for this!");
		}
	}
}