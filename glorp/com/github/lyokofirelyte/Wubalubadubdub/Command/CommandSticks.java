package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandSticks {
	
	private Wub main;
	
	public CommandSticks(Wub w){
		main = w;
	}

	@WubCommand(commands = {"sticks", "bal", "balance", "money", "monies", "cash"}, help = "/sticks", desc = "How many monies do haz you?")
	public void onSticks(String[] args, Player p){
		main.sendMessage(p, "&aYou've got &6" + WubData.TRADING_STICKS.getData(p, main).asInt() + " &atrading sticks!");
	}
	
}
