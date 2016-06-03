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
		e.setMessage(ChatColor.stripColor(main.AS(e.getMessage())));
		
		SystemRanks sr = (SystemRanks) main.getInstance(SystemRanks.class);
		String staffRank = WubData.STAFF_RANK.getData(p, main).asString();
		boolean official = staffRank.length() > 2 && e.getMessage().startsWith("@");
		String rankColor = official ? "&b" : sr.getRankColor(p);
		String rank = official ? staffRank : sr.getRank(p);
		String message = rankColor + "\u1445 " + rank + " \u1440 &7" + e.getPlayer().getDisplayName() + " &7> &f" + main.AS((official ? "&b" + e.getMessage().substring(1) : e.getMessage()));
		for (Player po : Bukkit.getOnlinePlayers()){
			po.sendMessage(main.AS(message));
		}
		System.out.println(e.getPlayer().getName() + " > " + e.getMessage());
	}
}