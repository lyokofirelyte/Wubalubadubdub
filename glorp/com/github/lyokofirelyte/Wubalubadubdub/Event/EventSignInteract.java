package com.github.lyokofirelyte.Wubalubadubdub.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;

public class EventSignInteract implements Listener {
	
	private Wub main;
	
	public EventSignInteract(Wub i){
		this.main = i;
	}

	@EventHandler
	public void onSignColor(SignChangeEvent e){
		for (int i = 0; i < e.getLines().length; i++){
			try {
				e.setLine(i, main.AS(e.getLine(i)));
			} catch (Exception ee){}
		}
	}
}