package com.github.lyokofirelyte.Wubalubadubdub;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandCP;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandDirector;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandHome;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandModify;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandNick;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandO;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandPay;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandPerms;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandRanks;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandRankup;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandReboot;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandReload;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandSellBuy;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandSpawn;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandStaff;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandStaffList;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandSticks;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandTell;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandWub;
import com.github.lyokofirelyte.Wubalubadubdub.Command.CommandZone;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubMarkkitItem;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubObject;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubRegion;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubServerObject;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventChat;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventCook;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventDamageTaken;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventMine;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventMobDeath;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventPressParkourSign;
import com.github.lyokofirelyte.Wubalubadubdub.Event.EventSignInteract;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemProtect;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemRanks;
import com.github.lyokofirelyte.Wubalubadubdub.Timer.TimerMarkkit;
import com.github.lyokofirelyte.Wubalubadubdub.Timer.WubTimer;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Wub extends JavaPlugin implements Listener {
	
	private List<Object> commands = new ArrayList<>();
	private List<Object> listeners = new ArrayList<>();
	private Map<String, WubRegion> regions = new HashMap<>();
	
	public WubServerObject serverObject = new WubServerObject();
	public WorldEditPlugin we;
	public Map<String, WubObject> playerData = new HashMap<>();
	public Map<String, Long> cooldowns = new HashMap<>();
	public Map<String, Object> clazzez = new HashMap<>();
	public Map<String, WubMarkkitItem> items = new HashMap<>();
	public Map<String, WubTimer> tasks = new HashMap<>();
	
	public void addTimer(WubTimer task){
		tasks.put(task.getName(), task);
	}
	
	public void sendTitle(Player player, String top, String bottom){
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + AS(top) + "\"}");
		IChatBaseComponent chatSubtitle = ChatSerializer.a("{\"text\": \"" + AS(bottom) + "\"}");

		PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle length = new PacketPlayOutTitle(0, 10, 0);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
		
		title = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubtitle);
		length = new PacketPlayOutTitle(0, 10, 0);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
	}
	
	public void clearTitle(Player p){
		PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.CLEAR, null);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
	}
	
	public String argsToString(String[] args){
		String full = "";
		for (String a : args){
			full += (full.equals("") ? "" : " ") + a;
		}
		return full;
	}
	
	public WubMarkkitItem getMarkkitItemFromLines(String[] lines){
		try {
			String idData = lines[2].split(" ")[0];
			int stackAmount = Integer.parseInt(lines[2].split(" ")[1].replace("*", ""));
			WubMarkkitItem item = items.containsKey(idData) ? items.get(idData) : null;
			if (item != null){
				item.setBuyStackAmt(stackAmount);
				item.setSellStackAmt(stackAmount);
			}
			return item;
		} catch (Exception e){
			return null;
		}
	}
	
	public Location stringToLoc(String s){
		String[] split = s.split(" ");
		if (split.length == 4){
			return new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
		} else {
			return new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
		}
	}
	
	public String locToString(Location l){
		return new String(
			l.getWorld().getName() + " " +
			l.getBlockX() + " " +
			l.getBlockY() + " " + 
			l.getBlockZ() + " " + 
			l.getYaw() + " " +
			l.getPitch()
		);
	}
	
	public String locToString(Location l, int plusX, int plusY, int plusZ){
		return new String(
			l.getWorld().getName() + " " +
			(l.getBlockX()+plusX) + " " +
			(l.getBlockY()+plusY) + " " + 
			(l.getBlockZ()+plusZ) + " " + 
			l.getYaw() + " " +
			l.getPitch()
		);
	}
	
	public Map<String, WubRegion> getRegions(){
		return regions;
	}
	
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
		
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		SystemProtect pro = new SystemProtect(this);
		commands.add(pro);
		listeners.add(pro);
		
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(pro, this);
		load();
		
		registerClasses(new Class<?>[]{
			CommandHome.class,
			CommandReload.class,
			CommandNick.class,
			CommandStaff.class,
			CommandWub.class,
			CommandRanks.class,
			CommandSpawn.class,
			CommandStaffList.class,
			CommandO.class,
			CommandPerms.class,
			CommandZone.class,
			CommandReboot.class,
			CommandTell.class,	
			CommandCP.class,
			CommandSellBuy.class,
			CommandSticks.class,
			CommandModify.class,
			CommandRankup.class,
			CommandPay.class
		});
		
		registerListeners(new Class<?>[]{
			CommandDirector.class,
			EventChat.class,
			EventDamageTaken.class,
			EventSignInteract.class,
			EventMobDeath.class,
			EventMine.class,
			EventCook.class,
			EventPressParkourSign.class
		});
		
		clazzez.put(SystemRanks.class.getName().toString(), new SystemRanks(this));
		clazzez.put(SystemProtect.class.getName().toString(), pro);
		
		for (Player p : Bukkit.getOnlinePlayers()){
			motd(p);
			WubData.INVULN.setData(p, 0, this);
		}
		
		startTimers();
		
		System.out.println("That's the way the news goes!");
	}
	
	private void startTimers(){
		tasks.put("markkit", new TimerMarkkit("markkit", 0L, 500L, this));
		tasks.get("markkit").cycle();
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
		
		File items = new File("./plugins/Wubalubadubdub/server/prices.txt");
		for (String item : Files.readLines(items, Charsets.UTF_8)){
			try {
				String[] spl = item.split("~");
				String idString = spl[0];
				int id = 0;
				byte byteString = 0;
				if (idString.contains(":")){
					id = Integer.parseInt(idString.split(":")[0]);
					byteString = Byte.parseByte(idString.split(":")[1]);
				} else {
					id = Integer.parseInt(spl[0]);
					idString += ":0";
				}
				this.items.put(idString, new WubMarkkitItem(id, byteString, spl[1], Integer.parseInt(spl[5]), Integer.parseInt(spl[7]), Integer.parseInt(spl[6]), Integer.parseInt(spl[4])));
				System.out.println("Marrkit id: " + id);
			} catch (Exception nope){
				nope.printStackTrace();
			}
		}
		
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
		
		/* [regions] */
		
		File regionInfo = new File("./plugins/Wubalubadubdub/regions/");
		regionInfo.mkdirs();
		
		for (String region : regionInfo.list()){
			try {
				region = region.replace(".json", "");
				Object regionObject = parser.parse(new FileReader("./plugins/Wubalubadubdub/regions/" + region + ".json"));
				JSONObject regionJSONObject = (JSONObject) regionObject;
				getRegions().put(region, new WubRegion(region, this, regionJSONObject));
			} catch (Exception e){
				e.printStackTrace();
			}
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
		
		for (String region : getRegions().keySet()){
			try {
				file = new FileWriter("./plugins/Wubalubadubdub/regions/" + region + ".json");
				file.write(getRegions().get(region).toJSONString());
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