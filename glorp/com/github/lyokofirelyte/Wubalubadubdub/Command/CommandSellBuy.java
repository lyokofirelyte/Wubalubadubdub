package com.github.lyokofirelyte.Wubalubadubdub.Command;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	private HashMap<Material, Integer> maxItemDurability = new HashMap<Material, Integer>() {
		{
			put(Material.WOOD, 5);
			put(Material.LEAVES, 15);
			put(Material.SAPLING, 15);
			put(Material.SAND, 1);
			put(Material.WOOL, 15);
			put(Material.STAINED_CLAY, 15);
			put(Material.STAINED_GLASS, 15);
			put(Material.CARPET, 15);
			put(Material.INK_SACK, 15);
			put(Material.COAL, 1);
			put(Material.LONG_GRASS, 2);
			put(Material.RED_ROSE, 8);
			put(Material.DOUBLE_PLANT, 5);
			put(Material.SMOOTH_BRICK, 3);
			put(Material.COBBLE_WALL, 1);
			put(Material.GOLDEN_APPLE, 1);
			put(Material.LOG, 5);
			put(Material.QUARTZ_BLOCK, 4);
		}
	};
	
	public CommandSellBuy(Wub w){
		main = w;
	}

	@WubCommand(commands = {"sell"}, help = "/sell", desc = "Sell your whole stack!")
	public void onSell(String[] args, Player p){
		
		if (p.getInventory().getItemInMainHand() != null && p.getGameMode() == GameMode.SURVIVAL){
			int amt = p.getInventory().getItemInMainHand().getAmount();
			String type = p.getInventory().getItemInMainHand().getType().toString();
			if (main.items.containsKey(p.getInventory().getItemInMainHand().getTypeId())){
				if (((SystemProtect) main.getInstance(SystemProtect.class)).isInRegion(p.getLocation(), "markkit")){
					WubMarkkitItem toSell = main.items.get(p.getInventory().getItemInMainHand().getTypeId());
					int sellPrice = toSell.getSellAmt() * amt / 64;
					if (args.length == 0){
						main.sendMessage(p, "&aSell " + amt + " " + type + " for " + sellPrice + "?");
						main.sendMessage(p, "&a&oType &f&o/sell confirm &a&oto confirm.");
					} else if (args[0].equals("confirm")){
						p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
						WubData.TRADING_STICKS.setData(p, WubData.TRADING_STICKS.getData(p, main).asInt() + sellPrice, main);
						main.sendMessage(p, "&aYou now have &6" + WubData.TRADING_STICKS.getData(p, main).asInt() + " &atrading sticks.");
						int needed = (WubData.RANK.getData(p, main).asInt() + 1) * 3500;
						int traded = WubData.GXP_SELL.getData(p, main).asInt();
						traded += sellPrice;
						float percent = (traded / needed);
						WubData.GXP_SELL.setData(p, traded, main);
						main.updateDisplayBar(p, "&b\u15D1 Trading: " + ((percent*100) >= 100 ? 100 : (percent*100)) + "%");
						((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);
					}
				} else {
					main.sendMessage(p, "&c&oYou must be in the markkit!");
				}
			} else {
				main.sendMessage(p, "&c&oWe don't currently have this item, sorry.");
			}
		} else {
			main.sendMessage(p, "&c&oHold the item you wish to sell in your main hand in survival mode.");
		}
	}
	
	@WubCommand(commands = {"buy"}, help = "/buy <item:data> <amount> ", desc = "Buy shit from the markkit")
	public void onBuy(String[] args, Player p){
		
		if (((SystemProtect) main.getInstance(SystemProtect.class)).isInRegion(p.getLocation(), "markkit")){
			int tradingSticks = WubData.TRADING_STICKS.getData(p, main).asInt();
			if (args.length >= 2){
				String[] itm = args[0].split(":");
				List<String> matches = new ArrayList<String>();
				for (Material m : Material.values()){
					if (m.toString().toLowerCase().equals(itm[0].toLowerCase())){
						matches.clear();
						matches.add(m.toString());
						break;
					} else if (main.items.containsKey(m.getId()) && m.toString().toLowerCase().contains(itm[0].toString().toLowerCase())){
						matches.add(m.toString());
					}
				}
				if (matches.size() == 1){
					boolean ready = args.length == 3 && args[2].equals("confirm");
					if (Utils.isInteger(args[1])){
						int amount = Integer.parseInt(args[1]);

						if (amount > 0 && amount <= 64){
							WubMarkkitItem markkitItem = main.items.get(Material.valueOf(matches.get(0)).getId());
							int data = itm.length == 2 && Utils.isInteger(itm[1]) && maxItemDurability.containsKey(Material.getMaterial(markkitItem.getId())) && maxItemDurability.get(Material.getMaterial(markkitItem.getId())) >= Integer.parseInt(itm[1]) && Integer.parseInt(itm[1]) > 0 ? Integer.parseInt(itm[1]) : 0;

							int price = (markkitItem.getBuyAmt() / 64) * amount;
							if (!ready){
								main.sendMessage(p, "&aBuy &f" + amount + " &aof &f" + matches.get(0) + " &afor " + price + "&a?");
								main.sendMessage(p, "&a&oAdd confirm to the end of your command to confirm.");
							} else {
								if (tradingSticks >= price){
									WubData.TRADING_STICKS.setData(p, WubData.TRADING_STICKS.getData(p, main).asInt() - price, main);
									main.sendMessage(p, "&aYou now have &6" + WubData.TRADING_STICKS.getData(p, main).asInt() + " &atrading sticks.");
									p.getInventory().addItem(new ItemStack(markkitItem.getId(), amount, (short)data));
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
				main.sendMessage(p, "&c&o/buy <item:data> <amount>");
			}
		} else {
			main.sendMessage(p, "&c&oYou must be in the markkit!");
		}
	}
}