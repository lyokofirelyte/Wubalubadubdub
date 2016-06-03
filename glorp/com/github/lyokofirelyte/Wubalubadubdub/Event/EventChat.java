package com.github.lyokofirelyte.Wubalubadubdub.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;

public class EventChat implements Listener {
	
	private Wub main;
	
	public EventChat(Wub i){
		this.main = i;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		e.setCancelled(true);
		Player p = e.getPlayer();
		
		SystemRanks sr = (SystemRanks) main.getInstance(SystemRanks.class);
		String staffRank = WubData.STAFF_RANK.getData(p, main).asString();
		String rankColor = sr.getRankColor(p);
		String rank = staffRank + sr.getRank(p);
		String message = rankColor + "\u1445 " + rank + " \u1440 &7" + e.getPlayer().getDisplayName() + " &7> &f" + ChatColor.stripColor(main.AS(e.getMessage()));
		for (Player po : Bukkit.getOnlinePlayers()){
			po.sendMessage(main.AS(message));
		}
	}
}