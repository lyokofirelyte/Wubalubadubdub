package com.github.lyokofirelyte.Wubalubadubdub.WubEvent;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubMarkkitItem;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubRegion;

import lombok.Getter;
import lombok.Setter;

public class SignPressEvent {
	
	@Getter @Setter
	private Sign sign;
	
	@Getter @Setter
	private Location signLoc;
	
	@Getter @Setter
	private WubRegion region;
	
	@Getter @Setter
	private Player presser;
	
	@Getter @Setter
	private String[] lines = new String[0];
	
	@Getter @Setter
	private Wub main;

	public SignPressEvent(Sign sign, Location signLoc, WubRegion region, Player p, String[] lines, Wub main){
		this.sign = sign;
		this.signLoc = signLoc;
		this.region = region != null ? region : null;
		this.presser = p;
		this.lines = lines;
		this.main = main;
	}
	
	public void fire(){
		
		if (region.getName().equals("emerald")){
			if (lines[0].contains("[ Wub ]")){
				if (lines[1].contains("Checkpoint")){
					WubData.CHECKPOINT.setData(presser, main.locToString(presser.getLocation()), main);
					main.sendMessage(presser, "&aCheckpoint set!");
				}
				if (lines[1].contains("Complete") && lines[2].contains("Parkour 1")){
					main.broadcast(presser.getDisplayName() + " &bhas finished Parkour 1 and obtained 500 trading sticks!");
					WubData.CHECKPOINT.setData(presser, "none", main);
					WubData.TRADING_STICKS.setData(presser, WubData.TRADING_STICKS.getData(presser, main).asInt() + 500, main);
					presser.chat("/spawn");
				}
			}
		}
		
		if (region.getName().equals("markkit")){
			for (int i = 0; i < lines.length; i++){
				lines[i] = ChatColor.stripColor(main.AS(lines[i]));
			}
			WubMarkkitItem item = main.getMarkkitItemFromLines(lines);
			if (item != null){
				int yourCash = WubData.TRADING_STICKS.getData(presser, main).asInt();
				if (lines[1].contains("Buy")){
					int cost = item.getBuyAmt()  * item.getBuyStackAmt() / 64;
					if (yourCash >= cost){
						if (WubData.MARKKIT_BOX.getData(presser, main).asListString().size() < 54){
							WubData.TRADING_STICKS.setData(presser, yourCash - cost, main);
							addToBox(item.getId(), item.getIdByte(), item.getBuyStackAmt());
							presser.sendMessage("");
							main.sendMessage(presser, "&aPurchased &6" + item.getBuyStackAmt() + " &aof &6" + item.getName() + "&a! You have &6" + WubData.TRADING_STICKS.getData(presser, main).asInt() + " &asticks left.");
							main.sendMessage(presser, "&a&oClaim all of your purchases in the mailbox near the front of the markkit when you leave.");
							presser.sendMessage("");
						} else {
							main.sendMessage(presser, "&c&oYour mailbox is full. Clear it out first!");
						}
					} else {
						main.sendMessage(presser, "&c&oYou can't afford that.");
					}
				} else if (lines[1].contains("Sell")){
//					int toRemove = -1;
//					for (int x = 0; x < presser.getInventory().getContents().length; x++){
					int cost = item.getSellAmt()  * item.getSellStackAmt() / 64;
					ItemStack i = presser.getItemInHand();
					if (i != null && i.getTypeId() == item.getId() && i.getData().getData() == item.getIdByte() && i.getAmount() == item.getSellStackAmt()){
						WubData.TRADING_STICKS.setData(presser, yourCash + cost, main);
						main.sendMessage(presser, "&aSold &6" + item.getSellStackAmt() + " &aof &6" + item.getName() + "&a! You now have &6" + WubData.TRADING_STICKS.getData(presser, main).asInt() + " &asticks.");
						presser.setItemInHand(new ItemStack(Material.AIR));
						WubData.GXP_SELL.setData(presser, WubData.GXP_SELL.getData(presser, main).asInt() + cost, main);
//						toRemove = x;
//						break;
					} else {
						main.sendMessage(presser, "You're holding the wrong item in your hand or it's not a full stack!");
					}
//					}
//					if (toRemove != -1){
//						presser.getInventory().remove(toRemove);
//					}
				}
			}
			if (lines[0].contains("[ Mailbox ]")){
				displayBox();
			}
			if (WubData.PERMS.getData(presser, main).asListString().contains("wub.signs.edit")){
				ItemStack inHand = presser.getInventory().getItemInMainHand();
				if (main.items.containsKey(inHand.getTypeId() + ":" + inHand.getData().getData())){
					WubMarkkitItem theItem = main.items.get(inHand.getTypeId() + ":" + inHand.getData().getData());
					sign.setLine(0, "[ Wub ]");
					sign.setLine(1, main.AS(presser.isSneaking() ? "&aSell" : "&cBuy"));
					sign.setLine(2, theItem.getId() + ":" + theItem.getIdByte() + " *" + (presser.isSneaking() ? theItem.getSellStackAmt() : theItem.getBuyStackAmt()));
					sign.setLine(3, "> press <");
					sign.update();
				}
			}
		}
	}
	
	private void addToBox(int id, byte data, int amount){
		List<String> items = WubData.MARKKIT_BOX.getData(presser, main).asListString();
		items.add(id + " " + amount + " " + data);
		WubData.MARKKIT_BOX.setData(presser, items, main);
	}
	
	private void displayBox(){
		List<String> items = WubData.MARKKIT_BOX.getData(presser, main).asListString();
		Inventory inv = Bukkit.createInventory(null, 54, main.AS("&aDeliveries"));
		for (String item : items){
			String[] spl = item.split(" ");
			ItemStack i = new ItemStack(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]), Byte.parseByte(spl[2]));
			inv.addItem(i);
		}
		presser.openInventory(inv);
	}
}