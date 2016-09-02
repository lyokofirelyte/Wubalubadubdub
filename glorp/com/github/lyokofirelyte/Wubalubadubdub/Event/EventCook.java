package com.github.lyokofirelyte.Wubalubadubdub.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;

public class EventCook implements Listener {
	
	private Wub main;
	private List<Material> oreBlocks = new ArrayList<Material>(Arrays.asList(
			Material.IRON_INGOT,
			Material.GOLD_INGOT
	));
	
	private List<Material> foodItems = new ArrayList<Material>(Arrays.asList(
			Material.COOKED_BEEF,
			Material.COOKED_CHICKEN,
			Material.COOKED_MUTTON,
			Material.COOKED_RABBIT,
			Material.BAKED_POTATO,
			Material.GRILLED_PORK
	));
	
	public EventCook(Wub i) {
		this.main = i;
	}
	
	@EventHandler
	public void onCook(FurnaceExtractEvent e) {
		Player p = e.getPlayer();
		
		if (!p.getGameMode().equals(GameMode.CREATIVE)){
			int percent = WubData.GXP_COOK.getData(p, main).asInt();
			
			if (percent < 100) {
				if(oreBlocks.contains(e.getBlock().getType())) {
					//Do something with ores
					WubData.GXP_COOK.setData(p, percent + ((int) e.getItemAmount() / 2), main);
				} else if(foodItems.contains(e.getBlock().getType())) {
					//Do something for food items
					WubData.GXP_COOK.setData(p, percent + ((int) e.getItemAmount() / 3), main);
				} else {
					//Do something with things of less value, aka stone/netherbrick/whatever else
					WubData.GXP_COOK.setData(p, percent + ((int) e.getItemAmount() / 4), main);
				}
				
				main.updateDisplayBar(p, "&b\u15D1 Cooking: " + percent + "%");
				((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);	
			}	
		}
	}
}