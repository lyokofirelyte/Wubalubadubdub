package com.github.lyokofirelyte.Wubalubadubdub.Event;

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
			if (percent < 100){
				WubData.GXP_MOB.setData(p, percent+1, main);
			}
			main.updateDisplayBar(e.getEntity().getKiller(), "&b\u15D1 Combat: " + percent + "%");
			((SystemRanks) main.getInstance(SystemRanks.class)).checkForRankup(p);
		}
	}
}