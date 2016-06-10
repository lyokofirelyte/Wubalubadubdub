package com.github.lyokofirelyte.Wubalubadubdub.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;

public class EventMobDeath implements Listener {
	
	private Wub main;
	
	private List<EntityType> hostileMobs = new ArrayList<EntityType>(Arrays.asList(
			EntityType.SKELETON,
			EntityType.ZOMBIE,
			EntityType.BLAZE,
			EntityType.CAVE_SPIDER,
			EntityType.CREEPER,
			EntityType.ENDERMAN,
			EntityType.GUARDIAN,
			EntityType.ENDERMITE,
			EntityType.GHAST,
			EntityType.GIANT,
			EntityType.MAGMA_CUBE,
			EntityType.PIG_ZOMBIE,
			EntityType.SHULKER,
			EntityType.SILVERFISH,
			EntityType.SPIDER,
			EntityType.WITHER,
			EntityType.WITCH,
			EntityType.SLIME
	));
	
	public EventMobDeath(Wub i){
		main = i;
	}

	@EventHandler
	public void onDamage(EntityDeathEvent e){
		if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player && hostileMobs.contains(e.getEntity().getType())){
			Player p = e.getEntity().getKiller();
			int percent = WubData.GXP_MOB.getData(p, main).asInt();
			int sticks = new Random().nextInt(30);;
			if (percent < 100){
				WubData.GXP_MOB.setData(p, percent+1, main);
			}
			WubData.TRADING_STICKS.setData(p, WubData.TRADING_STICKS.getData(p, main).asInt() + sticks, main);
			main.updateDisplayBar(e.getEntity().getKiller(), "&b\u15D1 Combat: " + percent + "%, + " + sticks + " sticks");
			((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);
		}
	}
}