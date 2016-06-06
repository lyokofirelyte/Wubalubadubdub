package com.github.lyokofirelyte.Wubalubadubdub.Event;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubRegion;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemProtect;
import com.github.lyokofirelyte.Wubalubadubdub.WubEvent.SignPressEvent;

public class EventPressParkourSign implements Listener {
	
	private Wub main;
	
	public EventPressParkourSign(Wub i){
		this.main = i;
	}

	@EventHandler(ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent e){
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK){
			if (e.getClickedBlock().getType().equals(Material.SIGN) || e.getClickedBlock().getState().getType().equals(Material.SIGN)){
				Sign sign = (Sign) e.getClickedBlock().getState();
				SystemProtect pro = (SystemProtect) main.getInstance(SystemProtect.class);
				String regionResult = pro.isInAnyRegion(sign.getLocation());
				if (!regionResult.equals("none")){
					WubRegion rg = main.getRegions().get(regionResult);
					new SignPressEvent(sign, sign.getLocation(), rg, e.getPlayer(), sign.getLines(), main).fire();
				} else {
					new SignPressEvent(sign, sign.getLocation(), null, e.getPlayer(), sign.getLines(), main).fire();
				}
			}
		}
	}
}