package com.github.lyokofirelyte.Wubalubadubdub.Command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.lyokofirelyte.Wubalubadubdub.Utils;
import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubMarkkitItem;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemProtect;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;

public class CommandSellBuy {
	
	private Wub main;
	
	public CommandSellBuy(Wub w){
		main = w;
	}

	@WubCommand(commands = {"sell"}, help = "/sell", desc = "Sell your whole stack!")
	public void onSell(String[] args, Player p){
		
		if (p.getInventory().getItemInMainHand() != null && p.getGameMode() == GameMode.SURVIVAL){
			int amt = p.getInventory().getItemInMainHand().getAmount();
			String type = p.getInventory().getItemInMainHand().getType().toString();
			if (amt >= 64){
				if (main.items.containsKey(p.getInventory().getItemInMainHand().getTypeId())){
					if (((SystemProtect) main.getInstance(SystemProtect.class)).isInRegion(p.getLocation(), "markkit")){
						WubMarkkitItem toSell = main.items.get(p.getInventory().getItemInMainHand().getTypeId());
						if (args.length == 0){
							main.sendMessage(p, "&aSell 64 " + type + " for " + toSell.getSellAmt() + "?");
							main.sendMessage(p, "&a&oType &f&o/sell confirm &a&oto confirm.");
						} else if (args[0].equals("confirm")){
							p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
							WubData.TRADING_STICKS.setData(p, WubData.TRADING_STICKS.getData(p, main).asInt() + toSell.getSellAmt(), main);
							main.sendMessage(p, "&aYou now have &6" + WubData.TRADING_STICKS.getData(p, main).asInt() + " &atrading sticks.");
							int needed = (WubData.RANK.getData(p, main).asInt() + 1) * 5000;
							int traded = WubData.GXP_SELL.getData(p, main).asInt();
							traded += toSell.getSellAmt();
							float percent = (traded / needed);
							WubData.GXP_SELL.setData(p, traded, main);
							main.updateDisplayBar(p, "&b\u15D1 Trading: " + (percent*100) + "%");
							((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);
						}
					} else {
						main.sendMessage(p, "&c&oYou must be in the markkit!");
					}
				} else {
					main.sendMessage(p, "&c&oWe don't currently have this item, sorry.");
				}
			} else {
				main.sendMessage(p, "&c&oYou must have 64 to sell!");
			}
		} else {
			main.sendMessage(p, "&c&oHold the item you wish to sell in your main hand in survival mode.");
		}
	}
	
	@WubCommand(commands = {"buy"}, help = "/buy <item> <amount>", desc = "Buy shit from the markkit")
	public void onBuy(String[] args, Player p){
		
		if (((SystemProtect) main.getInstance(SystemProtect.class)).isInRegion(p.getLocation(), "markkit")){
			int tradingSticks = WubData.TRADING_STICKS.getData(p, main).asInt();
			if (args.length >= 2){
				List<String> matches = new ArrayList<String>();
				for (Material m : Material.values()){
					if (m.toString().toLowerCase().equals(args[0].toLowerCase())){
						matches.clear();
						matches.add(m.toString());
						break;
					} else if (main.items.containsKey(m.getId()) && m.toString().toLowerCase().contains(args[0].toString().toLowerCase())){
						matches.add(m.toString());
					}
				}
				if (matches.size() == 1){
					boolean ready = args.length == 3 && args[2].equals("confirm");
					if (Utils.isInteger(args[1])){
						int amount = Integer.parseInt(args[1]);
						if (amount > 0 && amount <= 64){
							WubMarkkitItem markkitItem = main.items.get(Material.valueOf(matches.get(0)).getId());
							int price = (markkitItem.getBuyAmt() / 64) * amount;
							if (!ready){
								main.sendMessage(p, "&aBuy &f" + amount + " &aof &f" + matches.get(0) + " &afor " + price + "&a?");
								main.sendMessage(p, "&a&oAdd confirm to the end of your command to confirm.");
							} else {
								if (tradingSticks >= price){
									WubData.TRADING_STICKS.setData(p, WubData.TRADING_STICKS.getData(p, main).asInt() - price, main);
									main.sendMessage(p, "&aYou now have &6" + WubData.TRADING_STICKS.getData(p, main).asInt() + " &atrading sticks.");
									p.getInventory().addItem(new ItemStack(markkitItem.getId(), amount));
								} else {
									main.sendMessage(p, "&c&oYou can't afford it.");
								}
							}
						} else {
							main.sendMessage(p, "&c&oYou can only buy up to 64 at a time, yo!");
						}
					} else {
						main.sendMessage(p, "&c&oThat's not quite a number.");
					}
				} else if (matches.size() > 0){
					main.sendMessage(p, "&c&oFound a lot of possible items:");
					for (String match : matches){
						main.sendMessage(p, "&e" + match);
					}
					main.sendMessage(p, "&c&oPlease type in more letters to narrow it down.");
				} else {
					main.sendMessage(p, "&c&oNo items found with that in the name.");
				}
			} else {
				main.sendMessage(p, "&c&o/buy <item> <amount>");
			}
		} else {
			main.sendMessage(p, "&c&oYou must be in the markkit!");
		}
	}
}