package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandStaffList {

	Wub main;
	
	public CommandStaffList(Wub i){
		main = i;
	}
	
	@WubCommand(commands = {"list"}, help = "/list", desc = "Shows online staff")
	public void onStaff(String[] args, Player p) {
		main.sendMessage(p, "Staff list: ");
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(WubData.STAFF_RANK.getData(player, main).asString().length() > 2) {
				main.sendMessage(p, "  - " + player.getDisplayName());
			}
		}
	}
	
}
