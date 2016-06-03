package com.github.lyokofirelyte.Wubalubadubdub.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;

public class EventMine implements Listener {
	
	private Wub main;
	private List<Material> miningBlocks = new ArrayList<Material>(Arrays.asList(
		Material.COBBLESTONE,
		Material.DIAMOND_ORE,
		Material.GOLD_ORE,
		Material.IRON_ORE,
		Material.OBSIDIAN,
		Material.REDSTONE_ORE,
		Material.COAL_BLOCK,
		Material.STONE,
		Material.EMERALD_ORE,
		Material.QUARTZ_ORE,
		Material.NETHERRACK,
		Material.SANDSTONE
	));
	
	private List<Material> treeBlocks = new ArrayList<Material>(Arrays.asList(
		Material.LOG,
		Material.LOG_2
	));
	
	private List<Material> digBlocks = new ArrayList<Material>(Arrays.asList(
			Material.SAND,
			Material.GRASS,
			Material.DIRT,
			Material.GRAVEL
	));
	
	public EventMine(Wub i){
		main = i;
	}

	@EventHandler
	public void onDamage(BlockBreakEvent e){
		Player p = e.getPlayer();
		if (miningBlocks.contains(e.getBlock().getType())){
			int percent = WubData.GXP_ROCK.getData(p, main).asInt();
			if (percent < 100){
				WubData.GXP_ROCK.setData(p, percent+1, main);
			}
			main.updateDisplayBar(p, "&b\u15D1 Mining: " + percent + "%");
			((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);
		} else if (treeBlocks.contains(e.getBlock().getType())){
			int percent = WubData.GXP_TREE.getData(p, main).asInt();
			if (percent < 100){
				WubData.GXP_TREE.setData(p, percent+1, main);
			}
			main.updateDisplayBar(p, "&b\u15D1 Logging: " + percent + "%");
			((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);
		} else if (digBlocks.contains(e.getBlock().getType())) {
			int percent = WubData.GXP_DIG.getData(p, main).asInt();
			if(percent < 100) {
				WubData.GXP_DIG.setData(p, percent+1, main);
			}
			main.updateDisplayBar(p, "&b\u15D1 Digging: " + percent + "%");
			((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);
		}
	}
}