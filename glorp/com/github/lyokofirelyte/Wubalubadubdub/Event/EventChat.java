package com.github.lyokofirelyte.Wubalubadubdub.Event;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Utils;
import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;

import lombok.SneakyThrows;

public class EventChat implements Listener {
	
	private Wub main;
	private PrintWriter pw;
	
	public EventChat(Wub i){
		this.main = i;
		//registerSockets();
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		e.setCancelled(true);
		Player p = e.getPlayer();
		e.setMessage(ChatColor.stripColor(main.AS(e.getMessage())));
		
		SystemRanks sr = (SystemRanks) main.getInstance(SystemRanks.class);
		String staffRank = WubData.STAFF_RANK.getData(p, main).asString();
		boolean official = staffRank.length() > 2 && e.getMessage().startsWith("@");
		String rankColor = official || p.getGameMode().equals(GameMode.CREATIVE) ? "&b" : sr.getRankColor(p);
		String rank = official ? staffRank : p.getGameMode().equals(GameMode.CREATIVE) ? "&bCreative" : sr.getRank(p);
		String message = rankColor + "\u1445 " + rank + " \u1440 &7" + e.getPlayer().getDisplayName() + " &7> &f" + main.AS((official ? "&b" + e.getMessage().substring(1) : e.getMessage()));
		for (Player po : Bukkit.getOnlinePlayers()){
			po.sendMessage(main.AS(message));
		}
		if (pw != null){
			try {
				message = "`(" + rank + ") " + e.getPlayer().getDisplayName() + "` " + e.getMessage();
				pw.println(ChatColor.stripColor(main.AS(message)));
				pw.flush();
			} catch (Exception ee){
				ee.printStackTrace();
			}
		} else {
			//System.out.println("null");
		}
	}
	
	/*@SneakyThrows
	public void registerSockets(){
		new Thread(new Runnable(){
			public void run(){
				try {
					ServerSocket socket = new ServerSocket(25579);
					while (true){
						Socket incoming = socket.accept();
						System.out.println("Accepted connection from Discord.");
						pw = new PrintWriter(incoming.getOutputStream());
						BufferedReader bis = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
						String text = "";
						try {
							while ((text = bis.readLine()) != null){
								System.out.println(text);
								String[] spl = text.split("%split%");
								if (spl[0].equals("chat")){
									try {
										Bukkit.broadcastMessage(Utils.AS("&8\u1445 &oDiscord&8 \u1440 &7" + spl[1] + " > &f" + spl[2]));
									} catch (Exception e){}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e){
					System.out.println("Connection to Discord closed.");
				}
			}
		}).start();
	}*/
}