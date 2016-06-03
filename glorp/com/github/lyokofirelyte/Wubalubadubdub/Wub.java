package com.github.lyokofirelyte.Wubalubadubdub;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandDirector;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandHome;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandNick;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandRanks;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandReload;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandSpawn;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandStaff;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandStaffList;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandWub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubObject;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubServerObject;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventChat;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventCook;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventDamageTaken;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventMine;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventMobDeath;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventSignInteract;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;

public class Wub extends JavaPlugin implements Listener {
	
	private List<Object> commands = new ArrayList<>();
	private List<Object> listeners = new ArrayList<>();
	public WubServerObject serverObject = new WubServerObject();
	
	public Map<String, WubObject> playerData = new HashMap<>();
	public Map<String, Long> cooldowns = new HashMap<>();
	public Map<String, Object> clazzez = new HashMap<>();
	
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
	
	public Object getInstance(Class<?> clazz){
		return clazzez.get(clazz.getName().toString());
	}
	
	public void updateDisplayBar(Player p, String msg){
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + AS(msg) + "\"}");
		PacketPlayOutChat chat = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(chat);
	}

	@Override
	public void onEnable(){
		
		Bukkit.getPluginManager().registerEvents(this, this);
		load();
		
		registerClasses(new Class<?>[]{
			CommandHome.class,
			CommandReload.class,
			CommandNick.class,
			CommandStaff.class,
			CommandWub.class,
			CommandRanks.class,
			CommandSpawn.class,
			CommandStaffList.class
		});
		
		registerListeners(new Class<?>[]{
			CommandDirector.class,
			EventChat.class,
			EventDamageTaken.class,
			EventSignInteract.class,
			EventMobDeath.class,
			EventMine.class,
			EventCook.class
		});
		
		clazzez.put(SystemRanks.class.getName().toString(), new SystemRanks(this));
		
		for (Player p : Bukkit.getOnlinePlayers()){
			motd(p);
			WubData.INVULN.setData(p, 0, this);
		}
		
		System.out.println("That's the way the news goes!");
	}
	
	@SneakyThrows
	private void registerListeners(Class<?>... clazzez){
		for (Class<?> clazz : clazzez){
			Object o = clazz.getConstructor(Wub.class).newInstance(this);
			if (o instanceof Listener){
				Bukkit.getPluginManager().registerEvents((Listener) o, this);
				listeners.add(o);
			}
		}
	}
	
	@SneakyThrows
	private void registerClasses(Class<?>... clazzez){
		for (Class<?> clazz : clazzez){
			commands.add(clazz.getConstructor(Wub.class).newInstance(this));
		}
	}

	@SneakyThrows
	private void load(){
		
		JSONParser parser = new JSONParser();
		
		/* [server] */
		
		File serverInfo = new File("./plugins/Wubalubadubdub/server/");
		serverInfo.mkdirs();
		
		try {
			Object serverObject = parser.parse(new FileReader("./plugins/Wubalubadubdub/server/server.json"));
			JSONObject serverJSONObject = (JSONObject) serverObject;
			this.serverObject = new WubServerObject(serverJSONObject);
		} catch (Exception e){
			e.printStackTrace();
		}
        
        /* [players] */
        
		File path = new File("./plugins/Wubalubadubdub/players/");
		path.mkdirs();
		
		for (String file : path.list()){
	        try {
	            Object obj = parser.parse(new FileReader("./plugins/Wubalubadubdub/players/" + file));
	            JSONObject jsonObject = (JSONObject) obj;
	            playerData.put(file.replace(".json", ""), new WubObject(jsonObject));
	        } catch (Exception e){
	        	e.printStackTrace();
	        }
		}
		
		for (Player p : Bukkit.getOnlinePlayers()){
			if (!playerData.containsKey(p.getUniqueId().toString())){
				playerData.put(p.getUniqueId().toString(), new WubObject());
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		
		e.setJoinMessage(null);
		
		if (!playerData.containsKey(e.getPlayer().getUniqueId().toString())){
			playerData.put(e.getPlayer().getUniqueId().toString(), new WubObject());
		}
		
		if (!WubData.DISPLAY_NAME.getData(e.getPlayer(), this).asString().equals("none")){
			e.getPlayer().setDisplayName(WubData.DISPLAY_NAME.getData(e.getPlayer(), this).asString());
			e.getPlayer().setPlayerListName(e.getPlayer().getDisplayName());
		}
		
		for (Player p : Bukkit.getOnlinePlayers()){
			sendMessage(p, new String[]{"&a" + e.getPlayer().getDisplayName() + " is here!"});
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
		serverObject.put("build", (((long) serverObject.get("build")) + 1));
		save();
	}
	
	@SneakyThrows
	private void save(){
		
		FileWriter file = new FileWriter("./plugins/Wubalubadubdub/server/server.json");
		file.write(serverObject.toJSONString());
		file.close();
		
		for (String player : playerData.keySet()){
			try {
				file = new FileWriter("./plugins/Wubalubadubdub/players/" + player + ".json");
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
			"&2Welcome! We're running &bWubalubadubdub v1.0, &2build &f#" + serverObject.get("build"),
			"&2Type &3/wub &2for a list of commands!"
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
	
	public List<Object> getCommands(){
		return commands;
	}
}