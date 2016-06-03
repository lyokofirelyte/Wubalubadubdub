package com.github.lyokofirelyte.Wubalubadubdub.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class EventDamageTaken implements Listener {
	
	private Wub main;
	
	public EventDamageTaken(Wub i){
		main = i;
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if (e.getEntity() instanceof Player){
			if (WubData.INVULN.getData((Player) e.getEntity(), main).asInt() == 1){
				e.setCancelled(true);
			}
		}
	}
}