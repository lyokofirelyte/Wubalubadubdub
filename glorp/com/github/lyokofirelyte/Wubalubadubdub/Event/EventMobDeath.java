package com.github.lyokofirelyte.Wubalubadubdub.Event;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;

public class EventMobDeath implements Listener {
	
	private Wub main;
	
	public EventMobDeath(Wub i){
		main = i;
	}

	@EventHandler
	public void onDamage(EntityDeathEvent e){
		if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player){
			Player p = e.getEntity().getKiller();
			int percent = WubData.GXP_MOB.getData(p, main).asInt();
			int sticks = 0;
			if (percent < 100){
				sticks = new Random().nextInt(30);
				WubData.GXP_MOB.setData(p, percent+1, main);
				WubData.TRADING_STICKS.setData(p, WubData.TRADING_STICKS.getData(p, main).asInt() + sticks, main);
			}
			main.updateDisplayBar(e.getEntity().getKiller(), "&b\u15D1 Combat: " + percent + "%, + " + sticks + " sticks");
			((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);
		}
	}
}