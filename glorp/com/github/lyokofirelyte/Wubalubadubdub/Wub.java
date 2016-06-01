package com.github.lyokofirelyte.Wubalubadubdub;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Wub extends JavaPlugin implements Listener {
	
	private Map<String, String> kolors = new HashMap<>();
	private List<Object> commands = new ArrayList<>();
	public Map<String, JSONObject> playerData = new HashMap<>();
	public Map<String, Long> cooldowns = new HashMap<>();
	
	public boolean isCooldownFinished(Player p, String cooldownName){
		return !cooldowns.containsKey(p.getUniqueId().toString() + "__" + cooldownName) || System.currentTimeMillis() >= cooldowns.get(p.getUniqueId().toString() + "__" + cooldownName);
	}
	
	public void addCooldown(Player p, String name, long duration){
		cooldowns.put(p.getUniqueId().toString() + "__" + name, System.currentTimeMillis() + duration);
	}
	
	public long cooldownSecondsLeft(Player p, String name){
		return (cooldowns.get(p.getUniqueId().toString() + "__" + name) - System.currentTimeMillis()) / 1000;
	}
	
	public void cooldownMessage(Player p, String name){
		sendMessage(p, "&c&oYour &6&o" + name + " &c&ocooldown still has &4&o" + cooldownSecondsLeft(p, name) + " &c&oseconds left.");
	}

	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new CommandDirector(this), this);
		System.out.println("That's the way the news goes!");
		load();
		registerClasses(new Class<?>[]{
			CommandHome.class,
			CommandReload.class,
			CommandNick.class
		});
		for (Player p : Bukkit.getOnlinePlayers()){
			motd(p);
		}
	}
	
	@SneakyThrows
	private void registerClasses(Class<?>... clazzez){
		for (Class<?> clazz : clazzez){
			commands.add(clazz.getConstructor(Wub.class).newInstance(this));
		}
	}
	
	private void load(){
		
		JSONParser parser = new JSONParser();
		File path = new File("./plugins/Wubalubadubdub/players/");
		path.mkdirs();
		
		for (String file : path.list()){
	        try {
	            Object obj = parser.parse(new FileReader("./plugins/Wubalubadubdub/players/" + file));
	            JSONObject jsonObject = (JSONObject) obj;
	            playerData.put(file.replace(".json", ""), jsonObject);
	        } catch (Exception e){
	        	e.printStackTrace();
	        }
		}
		
		for (Player p : Bukkit.getOnlinePlayers()){
			if (!playerData.containsKey(p.getUniqueId().toString())){
				playerData.put(p.getUniqueId().toString(), new JSONObject());
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		e.setJoinMessage(null);
		for (Player p : Bukkit.getOnlinePlayers()){
			sendMessage(p, new String[]{"&a" + e.getPlayer().getDisplayName() + " is here!"});
		}
		if (!playerData.containsKey(e.getPlayer().getUniqueId().toString())){
			playerData.put(e.getPlayer().getUniqueId().toString(), new JSONObject());
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		e.setQuitMessage(null);
		for (Player p : Bukkit.getOnlinePlayers()){
			sendMessage(p, new String[]{"&c" + e.getPlayer().getDisplayName() + " has fled!"});
		}
	}
	
	@Override
	public void onDisable(){
		System.out.println("LATER BITCHES!");
		save();
	}
	
	private void save(){
		for (String player : playerData.keySet()){
			try (FileWriter file = new FileWriter("./plugins/Wubalubadubdub/players/" + player + ".json")) {
				file.write(playerData.get(player).toJSONString());
				file.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e){
		motd(e.getPlayer());
	}
	
	public void motd(Player p){
		sendMessage(p, new String[]{
			"&2Welcome! We're running &bWubalubadubdub v1.0",
			"",
			"&6Commands&f:",
			"  &e!namecolor <color>",
			"    &e&oColors are a-f and 0-9",
			"    &e&oresets on server reboot",
			"  &e/sethome, /home",
			"    &e&oSet your home and return to it",
			"  &e/nick <name>",
			"    &e&oSet your nickname!"
		});
	}
	
	public void broadcast(String... message){
		for (Player p : Bukkit.getOnlinePlayers()){
			sendMessage(p, message);
		}
	}
	
	public void sendMessage(Player p, String... message){
		for (String m : message){
			p.sendMessage(AS("&c\u1440 &7" + m));
		}
	}
	
	public String AS(String message){
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		
		e.setCancelled(true);
		String message = "&7" + e.getPlayer().getDisplayName() + " &7> &f" + e.getMessage();
		
		List<String> colors = new ArrayList<String>(Arrays.asList(
			"a", "b", "c", "d", "e", "f", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
		));
		
		if (e.getMessage().toLowerCase().startsWith("!namecolor")){
			if (colors.contains(e.getMessage().toLowerCase().split(" ")[1])){
				kolors.put(e.getPlayer().getUniqueId().toString(), e.getMessage().toLowerCase().split(" ")[1]);
				e.getPlayer().sendMessage("It worked and stuff.");
			} else {
				e.getPlayer().sendMessage("Invalid color!");
			}
			return;
		}
		
		if (kolors.containsKey(e.getPlayer().getUniqueId().toString())){
			message = "&" + kolors.get(e.getPlayer().getUniqueId().toString()) + message.substring(2);
		}
		
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public List<Object> getCommands(){
		return commands;
	}
}
