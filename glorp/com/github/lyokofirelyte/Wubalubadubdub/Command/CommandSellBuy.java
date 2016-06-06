package com.github.lyokofirelyte.Wubalubadubdub.Command;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubMarkkitItem;

public class CommandSellBuy {
	
	private Wub main;
	
	public CommandSellBuy(Wub w){
		main = w;
	}

	@WubCommand(commands = {"sell"}, help = "/sell", desc = "Sell your whole stack!")
	public void onSell(String[] args, Player p){
		if (p.getInventory().getItemInMainHand() != null){
			int amt = p.getInventory().getItemInMainHand().getAmount();
			String type = p.getInventory().getItemInMainHand().getType().toString();
			if (amt >= 64){
				if (main.items.containsKey(p.getInventory().getItemInMainHand().getTypeId())){
					WubMarkkitItem toSell = main.items.get(p.getInventory().getItemInMainHand().getTypeId());
					if (args.length == 0){
						main.sendMessage(p, "&aSell 64 " + type + " for " + toSell.getSellAmt() + "?");
						main.sendMessage(p, "&a&oType &f&o/sell confirm &a&oto confirm.");
					} else if (args[0].equals("confirm")){
						p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
						WubData.TRADING_STICKS.setData(p, WubData.TRADING_STICKS.getData(p, main).asInt() + toSell.getSellAmt(), main);
						main.sendMessage(p, "&aYou now have &6" + WubData.TRADING_STICKS.getData(p, main) + " &atrading sticks.");
					}
				} else {
					main.sendMessage(p, "&c&oWe don't currently have this item, sorry.");
				}
			} else {
				main.sendMessage(p, "&c&oYou must have 64 to sell!");
			}
		} else {
			main.sendMessage(p, "&c&oHold the item you wish to sell in your main hand.");
		}
	}
}