package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Utils;
import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandPay {
	
	private Wub main;
	
	public CommandPay(Wub w){
		main = w;
	}

	@WubCommand(commands = {"pay"}, help = "/pay <username> <amount>", desc = "Send people money!")
	public void onNick(String[] args, Player p){
		if(args.length == 2 && Utils.isInteger(args[1])) {
			int amount = Integer.parseInt(args[1]);
			if(amount >= 0) {
				if(Bukkit.getPlayer(args[0]) != null) {
					Player receiver = Bukkit.getPlayer(args[0]);
					int senderMoney = WubData.TRADING_STICKS.getData(p, main).asInt();
					
					if(senderMoney - amount >= 0) {
						WubData.TRADING_STICKS.setData(p, senderMoney - amount, main);
						
						int receiverMoney = WubData.TRADING_STICKS.getData(receiver, main).asInt();
						WubData.TRADING_STICKS.setData(receiver, receiverMoney + amount, main);
						main.sendMessage(p, "You've sent &6" + amount + " &7trading stick" + (amount == 1 ? "" : "s") + " to user &a" + args[0]);
						main.sendMessage(p, "You've received &6" + amount + " &7trading stick" + (amount == 1 ? "" : "s") + " from user &a" + WubData.DISPLAY_NAME.getData(p, main).asString());
					} else {
						main.sendMessage(p, "Not enough money!");
					}
				} else {
					main.sendMessage(p, "Player " + args[0] + " not found!");
				}
			} else {
				main.sendMessage(p, "Cannot send 0 or negative amounts of money!");
			}
		} else {
			main.sendMessage(p, "Correct usage: /pay <username> <amount>");
		}
	}
}