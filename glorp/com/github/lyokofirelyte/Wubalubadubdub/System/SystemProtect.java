package com.github.lyokofirelyte.Wubalubadubdub.System;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;

import com.github.lyokofirelyte.Wubalubadubdub.Utils;
import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubFlag;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubRegion;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubRegionInfo;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.RegionSelector;

public class SystemProtect implements Listener {
	
	private Wub main;
	
	public SystemProtect(Wub i){
		main = i;
	}
		
	@EventHandler(ignoreCancelled = false)
	public void onOpen(InventoryOpenEvent e){
		
		if (!e.getPlayer().getWorld().getName().equals("world")){
			if (e.getInventory().getType().equals(InventoryType.ENDER_CHEST)){
				e.setCancelled(true);
				main.sendMessage((Player) e.getPlayer(), "&c&oSorry, enderchests can't be opened here.");
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e){
		if (e.getInventory().getTitle().contains("Deliveries")){
			List<String> items = new ArrayList<String>();
			for (ItemStack i : e.getInventory().getContents()){
				try {
					items.add(i.getTypeId() + " " + i.getAmount() + " " + i.getData().getData());
				} catch (Exception eee){}
			}
			WubData.MARKKIT_BOX.setData((Player) e.getPlayer(), items, main);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		
		String result = isInAnyRegion(e.getBlock().getLocation());
		
		if (hasFlag(result, WubFlag.BLOCK_BREAK)){
			if (!hasRegionPerms(e.getPlayer(), result)){
				e.setCancelled(true);
				main.sendMessage(e.getPlayer(), "&c&oYou are not authorized to build at &6" + result + "&c&o.");
			}
		}
		
		if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && e.getPlayer().getItemInHand().getType().equals(Material.BLAZE_ROD)){
			e.setCancelled(true);
		}
		
		if (e.getBlock().getType().equals(Material.SIGN) || e.getBlock().getType().equals(Material.WALL_SIGN)){
			Sign sign = (Sign) e.getBlock().getState();
			if (sign.getLines().length > 0){
				if (sign.getLines()[0].contains("[ Wub ]")){
					if (!WubData.PERMS.getData(e.getPlayer(), main).asListString().contains("wub.signs.break")){
						e.setCancelled(true);
						main.sendMessage(e.getPlayer(), "&c&oYou need wub.signs.break to break signs with [ Wub ] on them.");
						main.sendMessage(e.getPlayer(), "&c&oAdmins: /perms add <player> wub.signs.break");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onFoodLevel(FoodLevelChangeEvent e){
		
		if (e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			String result = isInAnyRegion(e.getEntity().getLocation());
			
			if (hasFlag(result, WubFlag.TAKE_DAMAGE)){
				if(e.getFoodLevel() < p.getFoodLevel()){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		
		String result = isInAnyRegion(e.getBlock().getLocation());
		
		if (hasFlag(result, WubFlag.BLOCK_PLACE)){
			if (!hasRegionPerms(e.getPlayer(), result)){
				e.setCancelled(true);
				main.sendMessage(e.getPlayer(), "&c&oYou are not authorized to place at &6" + result + "&c&o.");
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onInteract(PlayerInteractEvent e){
		
		Player p = e.getPlayer();
		String result = isInAnyRegion(p.getLocation());
		Location l = e.getClickedBlock() != null ? e.getClickedBlock().getLocation() : e.getPlayer().getLocation();
		
		if (hasFlag(result, WubFlag.INTERACT)){
			if (!e.getAction().toString().contains("AIR") && !hasRegionPerms(p, result)){
				e.setCancelled(true);
			}
		}
		
		if (p.getGameMode().equals(GameMode.CREATIVE) && p.getItemInHand() != null && p.getItemInHand().getType().equals(Material.BLAZE_ROD)){
			
			RegionSelector sel = main.we.getSession(p).getRegionSelector(main.we.wrapPlayer(p).getWorld());
			com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(l.getBlockX(), l.getBlockY(), l.getBlockZ());
			
			if (e.getAction() == Action.LEFT_CLICK_BLOCK){
				sel.selectPrimary(v, null);
				main.sendMessage(p, "Selected first position!");
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
				sel.selectSecondary(v, null);
				main.sendMessage(p, "Selected second position!");
			}
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e){
		
		if (e.getEntity() instanceof Player){
		
			Player p = (Player) e.getEntity();
			
			String result = isInAnyRegion(p.getLocation());
			
			if (hasFlag(result, WubFlag.TAKE_DAMAGE) || e.getDamager() instanceof SmallFireball){
				e.setCancelled(true);
			}
			
		} else if (e.getEntity() instanceof ItemFrame || e.getEntity() instanceof Painting){
			if (hasFlag(isInAnyRegion(e.getEntity().getLocation()), WubFlag.TAKE_DAMAGE)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageEvent e){
		
		if (e.getEntity() instanceof Player){
		
			Player p = (Player)e.getEntity();
			String result = isInAnyRegion(p.getLocation());

			if (hasFlag(result, WubFlag.TAKE_DAMAGE)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onCommand(PlayerCommandPreprocessEvent e){
		
		String result = isInAnyRegion(e.getPlayer().getLocation());
		
		if (hasFlag(result, WubFlag.USE_COMMANDS)){
			if (!hasRegionPerms(e.getPlayer(), result) && !e.getMessage().startsWith("/cp") && !e.getMessage().startsWith("/spawn") && !e.getMessage().startsWith("/tell")){
				e.setCancelled(true);
				main.sendMessage(e.getPlayer(), "&c&oYou are not authorized to use commands at &6" + result + "&c&o.");
			}
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {

		if (hasFlag(isInAnyRegion(e.getEntity().getLocation()), WubFlag.TNT_EXPLODE)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent e){
		
		if (hasFlag(isInAnyRegion(e.getEntity().getLocation()), WubFlag.MOB_SPAWN) && e.getSpawnReason() != SpawnReason.BREEDING){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent e){
		
		if (hasFlag(isInAnyRegion(e.getBlock().getLocation()), WubFlag.FIRE_SPREAD)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onGravity(EntityChangeBlockEvent e){
		
		if (hasFlag(isInAnyRegion(e.getBlock().getLocation()), WubFlag.GRAVITY)){
			e.setCancelled(true);
		}
	
		if (e.getBlock().getType().equals(Material.LADDER)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFlow(BlockPhysicsEvent e){
		
		Material mat = e.getBlock().getType();
		
		if (mat.equals(Material.WATER)){
			if (hasFlag(isInAnyRegion(e.getBlock().getLocation()), WubFlag.WATER_FLOW)){
				e.setCancelled(true);
			}
		} else if (mat.equals(Material.LAVA)){
			if (hasFlag(isInAnyRegion(e.getBlock().getLocation()), WubFlag.LAVA_FLOW)){
				e.setCancelled(true);
			}
		} else if (mat.equals(Material.LADDER)){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFlow(BlockFromToEvent e){
		
		Material mat = e.getToBlock().getType();
		
		if (mat.equals(Material.WATER)){
			if (hasFlag(isInAnyRegion(e.getToBlock().getLocation()), WubFlag.WATER_FLOW)){
				e.setCancelled(true);
			}
		} else if (mat.equals(Material.LAVA)){
			if (hasFlag(isInAnyRegion(e.getToBlock().getLocation()), WubFlag.LAVA_FLOW)){
				e.setCancelled(true);
			}
		} else if (e.getBlock().getType().equals(Material.ICE)){
			if (hasFlag(isInAnyRegion(e.getToBlock().getLocation()), WubFlag.MELT)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDecay(BlockFadeEvent e){
		
		Material mat = e.getBlock().getType();
		
		if (mat.equals(Material.LEAVES) || mat.equals(Material.LEAVES_2)){
			if (hasFlag(isInAnyRegion(e.getBlock().getLocation()), WubFlag.LEAF_DECAY)){
				e.setCancelled(true);
			}
		} else if (mat.equals(Material.SNOW_BLOCK) || mat.equals(Material.SNOW) || mat.equals(Material.ICE)){
			if (hasFlag(isInAnyRegion(e.getBlock().getLocation()), WubFlag.MELT)){
				e.setCancelled(true);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@WubCommand(commands = {"protect", "pro"}, desc = "World Protection Command", help = "/pro help")
	public void onProtect(String[] args, final Player p){
		
		if (args.length == 0){
			args = new String[1];
			args[0] = "help";
		}
		
		if (!args[0].equals("info") && !args[0].equals("view")){
			if (WubData.STAFF_RANK.getData(p, main).asString().length() <= 1){
				return;
			}
		}
		
		switch (args[0]){
		
			case "remove": case "redefine": case "flag": case "view": case "select": case "tp": case "disable": case "visual": case "priority":
				
			if (args.length < 2 || !doesRegionExist(args[1])){
				main.sendMessage(p, "&c&oThat region does not exist!");
				return;
			}
		}
		
		switch (args[0]){
		
			case "help":
				
				String[] messages = new String[] {
					"/pro create <region>",
					"/pro remove <region>",
					"/pro redefine <region>",
					"/pro flag <region> <flag> <value>",
					"/pro viewflags",
					"/pro priority <region> <number>",
					"/pro view <region>",
					"/pro perms <add, remove> <region> <perm>",
					"/pro select <region>",
					"/pro list",
					"/pro tp <region>",
					"/pro disable <region>",
					"/pro visual <region>",
					"/pro info"
				};
				
				for (String s : messages){
					main.sendMessage(p, s);
				}
				
			break;
			
			case "list":
				
				List<String> regions = new ArrayList<String>();
				
				for (WubRegion m : main.getRegions().values()){
					regions.add(m.getName());
				}
				
				String rgColor = regions.size() > 0 && !main.getRegions().get(regions.get(0)).isDisabled() ? "&a" : "&c";
				String msg = regions.size() > 0 ? rgColor + regions.get(0) : "&c&oNo regions are defined.";
				
				for (int i = 1; i < regions.size(); i++){
					rgColor = regions.size() > i && !main.getRegions().get(regions.get(i)).isDisabled() ? "&a" : "&c";
					msg = regions.size() > i ? msg + "&7, " + rgColor + regions.get(i) : msg;
				}
				
				main.sendMessage(p, "&3Region List");
				main.sendMessage(p, msg);
				
			break;
			
			case "visual":
				
				if (!WubData.REGION_PREVIEW.getData(p, main).asBool()){
					
					WubRegion rg = main.getRegions().get(args[1]);
					
					if (rg.getLength() > 500 || rg.getWidth() > 500){
						main.sendMessage(p, "&c&oThis region is too big to visualize.");
						return;
					}
					
					final List<Location> locs = new ArrayList<Location>();
					List<Location> toRemove = new ArrayList<Location>();
					
					String[] minBlock = rg.getMinBlock().split(" ");
					String[] maxBlock = rg.getMaxBlock().split(" ");
					int counter = 0;
					
					WubData.REGION_PREVIEW.setData(p, true, main);
					
					for (int i = 0; i <= rg.getWidth(); i++){
						locs.add(new Location(Bukkit.getWorld(rg.getWorld()), d(minBlock[0]) + i, p.getLocation().getY(), d(minBlock[2])));
					}
					
					for (int i = 0; i <= rg.getLength(); i++){
						locs.add(new Location(Bukkit.getWorld(rg.getWorld()), d(minBlock[0]), p.getLocation().getY(), d(minBlock[2]) + i));
					}
					
					for (int i = 0; i <= rg.getWidth(); i++){
						locs.add(new Location(Bukkit.getWorld(rg.getWorld()), d(maxBlock[0]) - i, p.getLocation().getY(), d(maxBlock[2])));
					}
					
					for (int i = 0; i <= rg.getLength(); i++){
						locs.add(new Location(Bukkit.getWorld(rg.getWorld()), d(maxBlock[0]), p.getLocation().getY(), d(maxBlock[2]) - i));
					}
					
					for (Location l : locs){
						if (counter == 0){
							l.getBlock().setTypeIdAndData(Material.STAINED_GLASS.getId(),(byte) new Random().nextInt(16), true);
							counter = 3;
						} else {
							counter--;
							toRemove.add(l);
						}
					}
					
					for (Location l : toRemove){
						locs.remove(l);
					}
					
					main.sendMessage(p, "The visualization will revert in 20 seconds.");
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable(){ public void run(){
						
						for (Location l : locs){
							l.getBlock().setType(Material.AIR);
						}
						
						WubData.REGION_PREVIEW.setData(p, false, main);
						main.sendMessage(p, "Visualization terminated.");
						
					}}, 400L);
					
				} else {
					main.sendMessage(p, "&c&oYou're already viewing a visualization.");
				}

			break;
			
			case "redefine":
					
				if (main.we.getSelection(p) != null && main.we.getSelection(p) instanceof CuboidSelection){
						
					Selection sel = main.we.getSelection(p);
					WubRegion region = main.getRegions().get(args[1].toLowerCase());
					Vector max = sel.getMaximumPoint().toVector();
					Vector min = sel.getMinimumPoint().toVector();
						
					region.set(WubRegionInfo.WORLD, p.getWorld().getName());
					region.set(WubRegionInfo.MAX_BLOCK, max.getBlockX() + " " + max.getBlockY() + " " + max.getBlockZ());
					region.set(WubRegionInfo.MIN_BLOCK, min.getBlockX() + " " + min.getBlockY() + " " + min.getBlockZ());
					region.set(WubRegionInfo.AREA, sel.getArea());
					region.set(WubRegionInfo.LENGTH, sel.getLength());
					region.set(WubRegionInfo.HEIGHT, sel.getHeight());
					region.set(WubRegionInfo.WIDTH, sel.getWidth());
					
					if (isInAnyRegion(p.getLocation()).equals("none")){
						region.set(WubRegionInfo.PRIORITY, 0);
					} else {
						region.set(WubRegionInfo.PRIORITY, main.getRegions().get(isInAnyRegion(p.getLocation())).getPriority() + 1);
					}
						
					main.sendMessage(p, "Redefine successful.");
						
				} else {
					main.sendMessage(p, "&c&oYou must select a cuboid with WorldEdit.");
				}
				
			break;
			
			case "tp":

				WubRegion rg = main.getRegions().get(args[1]);
				String[] mins = rg.getMinBlock().split(" ");
				p.teleport(new Location(Bukkit.getWorld(rg.getWorld()), Integer.parseInt("" + (i(mins[0]) + (rg.getWidth()/2))), Integer.parseInt(mins[1]), Integer.parseInt("" + (i(mins[2]) + (rg.getLength()/2)))));
				
			break;
			
			case "remove":

				main.getRegions().remove(args[1].toLowerCase());
				main.sendMessage(p, "Deleted &6" + args[1] + "&b.");
				
			break;
			
			case "disable":

				main.getRegions().get(args[1]).set(WubRegionInfo.DISABLED, !main.getRegions().get(args[1]).isDisabled());
				main.sendMessage(p, args[1] + " updated.");
				
			break;
			
			case "create":
				
				if (args.length == 2){
					
					if (!doesRegionExist(args[1])){
						
						if (main.we.getSelection(p) != null && main.we.getSelection(p) instanceof CuboidSelection){
								
							Selection sel = main.we.getSelection(p);
							WubRegion region = new WubRegion(args[1].toLowerCase(), main);
							Vector max = sel.getMaximumPoint().toVector();
							Vector min = sel.getMinimumPoint().toVector();
								
							region.set(WubRegionInfo.WORLD, p.getWorld().getName());
							region.set(WubRegionInfo.MAX_BLOCK, max.getBlockX() + " " + max.getBlockY() + " " + max.getBlockZ());
							region.set(WubRegionInfo.MIN_BLOCK, min.getBlockX() + " " + min.getBlockY() + " " + min.getBlockZ());
							region.set(WubRegionInfo.AREA, sel.getArea());
							region.set(WubRegionInfo.LENGTH, sel.getLength());
							region.set(WubRegionInfo.HEIGHT, sel.getHeight());
							region.set(WubRegionInfo.WIDTH, sel.getWidth());
							region.set(WubRegionInfo.DISABLED, false);
							region.set(WubRegionInfo.PERMS, new JSONArray());
								
							if (isInAnyRegion(p.getLocation()).equals("none")){
								region.set(WubRegionInfo.PRIORITY, 0);
							} else {
								region.set(WubRegionInfo.PRIORITY, main.getRegions().get(isInAnyRegion(p.getLocation())).getPriority() + 1);
							}
								
							main.sendMessage(p, "Creation successful.");
							main.sendMessage(p, "&7&oFlag with /pro flag <region> <flag> <value>.");
							main.sendMessage(p, "&7&oAdjust perms with /pro perms <add, remove> <region> <perm>.");
							main.getRegions().put(args[1].toLowerCase(), region);
								
						} else {
							main.sendMessage(p, "&c&oYou must select a cubiod region with WorldEdit.");
						}
						
					} else {
						main.sendMessage(p, "&c&oThat region already exists!");
					}
					
				} else {
					main.sendMessage(p, "/pro create <region>");
				}
				
			break;
			
			case "perms":
				
				if (args.length == 4){
					
					if (args[1].equals("add") || args[1].equals("remove")){
						
						if (doesRegionExist(args[2])){
							
							rg = main.getRegions().get(args[2]);
							List<String> perms = rg.getList(WubRegionInfo.PERMS);
							
							if (args[1].equals("add")){
								perms.add(args[3]);
								rg.set(WubRegionInfo.PERMS, perms);
								main.sendMessage(p, "Added &6" + args[3]);
							} else {
								perms.remove(args[3]);
								rg.set(WubRegionInfo.PERMS, perms);
								main.sendMessage(p, "Removed &6" + args[3]);
							}
							
						} else {
							main.sendMessage(p, "&c&oThat region does not exist.");
						}
						
					} else {
						main.sendMessage(p, "/pro perms <add, remove> <region> <perm>");
					}
					
				} else {
					main.sendMessage(p, "/pro perms <add, remove> <region> <perm>");
				}
				
			break;
			
			case "select":

				rg = main.getRegions().get(args[1]);
				String[] max = rg.getMaxBlock().split(" ");
				String[] min = rg.getMinBlock().split(" ");
						
				if (rg.getWorld().equals(p.getWorld().getName())){
							
					RegionSelector sel = main.we.getSession(p).getRegionSelector(main.we.wrapPlayer(p).getWorld());
					com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(d(max[0]), d(max[1]), d(max[2]));
					com.sk89q.worldedit.Vector v2 = new com.sk89q.worldedit.Vector(d(min[0]), d(min[1]), d(min[2]));
					sel.selectPrimary(v, null);
					sel.selectSecondary(v2, null);
							
					main.sendMessage(p, "Selected &6" + args[1] + " &bas a cuboid!");
							
				} else {
					main.sendMessage(p, "&c&oWrong world! You must be in &6" + rg.getWorld() + "&b!");
				}
				
			break;
			
			case "view":

				rg = main.getRegions().get(args[1]);
				Map<WubFlag, Boolean> flagz = rg.getFlags();
				List<WubFlag> keys = new ArrayList<WubFlag>(flagz.keySet());
				List<String> perms = rg.getPerms();
				String flagMessage = flagz.size() > 0 ? "&6" + keys.get(0).s().toLowerCase() + "&f: &7" + flagz.get(keys.get(0)).toString().replace("true", "&cdeny").replace("false", "&aallow") : "&c&oNo flags listed.";
				String permMessage = perms.size() > 0 ? "&6" + perms.get(0) : "&c&oNo perms listed.";
						
				for (int i = 1; i < keys.size(); i++){
					flagMessage = keys.size() > i ? flagMessage + "&3, &6" + keys.get(i).s().toLowerCase() + "&f: " + flagz.get(keys.get(i)).toString().replace("true", "&cdeny").replace("false", "&aallow") : flagMessage;
				}
						
				for (int i = 1; i < perms.size(); i++){
					permMessage = perms.size() > i ? permMessage + "&3, &6" + perms.get(i) : permMessage;
				}
						
				main.sendMessage(p, "&3Viewing Region: &6" + args[1]);
				main.sendMessage(p, "World: &6" + rg.getWorld());
				main.sendMessage(p, "Minimum Bound: &6" + rg.getMinBlock());
				main.sendMessage(p, "Maximum Bound: &6" + rg.getMaxBlock());
				main.sendMessage(p, "Priority: &6" + rg.getPriority());
				main.sendMessage(p, flagMessage);
				main.sendMessage(p, permMessage);
				
			break;
			
			case "priority":
				
				rg = main.getRegions().get(args[1]);
				
				if (Utils.isInteger(args[2])){
					rg.set(WubRegionInfo.PRIORITY, i(args[2]));
					main.sendMessage(p, "Priority changed for &6" + args[1] + " &bto &6" + args[2] + "&b.");
				} else {
					main.sendMessage(p, "&c&oMust be a number!");
				}
				
			break;
			
			case "info":
				
				String results = isInAnyRegion(p.getLocation());
				
				if (!results.equals("none")){
					p.chat("/pro view " + results);
				} else {
					main.sendMessage(p, "&c&oYou're not standing in any regions.");
				}
				
			break;
			
			case "viewflags":
				
				List<WubFlag> flags = Arrays.asList(WubFlag.values());
				msg = flags.size() > 0 ? "&6" + flags.get(0).s().toLowerCase() : "&c&oNo flags avalible.";
				
				for (int i = 1; i < flags.size(); i++){
					msg = flags.size() > i ? msg + "&7, &6" + flags.get(i).s().toLowerCase() : msg;
				}
				
				main.sendMessage(p, msg);
				
			break;
			
			case "flag":
				
				if (args.length == 4){
					
					if (doesRegionExist(args[1])){
						
						try {
							
							if (args[3].equals("allow") || args[3].equals("deny")){
								boolean flag = args[3].equals("allow") ? false : true;
								main.getRegions().get(args[1]).set(WubFlag.valueOf(args[2].toUpperCase()), flag);
								main.sendMessage(p, "Flag &6" + args[2] + " &bfor &6" + args[1] + " &bchanged to &6" + args[3]);
							} else {
								main.sendMessage(p, "&c&oallow or deny value.");
							}
							
						} catch (Exception e){
							main.sendMessage(p, "&c&oInvalid flag. See /pro viewflags");
						}
						
					} else {
						main.sendMessage(p, "&c&oThat region does not exist.");
					}
					
				} else {
					main.sendMessage(p, "/pro flag <region> <flag> <allow, deny>");
				}
				
			break;
			
			default:
				
				main.sendMessage(p, "/pro help");
				
			break;
		}
	}
	
	public String isInAnyRegion(Location l){
		
		Map<Integer, WubRegion> foundRegions = new HashMap<Integer, WubRegion>();
		
		for (WubRegion rg : main.getRegions().values()){
			if (isInRegion(l, rg.getName())){
				foundRegions.put(rg.getPriority(), rg);
			}
		}
		
		if (foundRegions.size() > 0){
			List<Integer> priority = new ArrayList<Integer>(foundRegions.keySet());
			Collections.sort(priority);
			Collections.reverse(priority);
			return foundRegions.get(priority.get(0)).getName();
		}
		
		return "none";
	}
	
	public boolean isInRegion(Location l, String region){
		Vector v = l.toVector();
		return isInRegion(l.getWorld().getName(), v.getBlockX(), v.getBlockY(), v.getBlockZ(), region);
	}
	
	public boolean isInRegion(String c, String region){
		String[] coords = c.split(" ");
		return isInRegion(coords[0], i(coords[1]), i(coords[2]), i(coords[3]), region);
	}
	
	public boolean isInRegion(String world, String c, String region){
		String[] coords = c.split(" ");
		return isInRegion(world, i(coords[0]), i(coords[1]), i(coords[2]), region);
	}
	
	public boolean isInRegion(String world, int x, int y, int z, String region){
		
		if (doesRegionExist(region)){
			
			WubRegion rg = main.getRegions().get(region);
			String[] min = rg.getMinBlock().split(" "); //x y z
			String[] max = rg.getMaxBlock().split(" ");
			
			if (rg.isDisabled()){
				return false;
			}
			
			if (rg.getWorld().equals(world)){
				if (x >= i(min[0]) && x <= i(max[0])){
					if (y >= i(min[1]) && y <= i(max[1])){
						if (z >= i(min[2]) && z <= i(max[2])){
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean doesRegionExist(String region){
		return main.getRegions().containsKey(region);
	}
	
	public boolean hasRegionPerms(Player p, String region){
		
		if (doesRegionExist(region)){
			
			if (main.getRegions().get(region).isDisabled()){
				return true;
			}
			
			for (String perm : main.getRegions().get(region).getPerms()){
				if (WubData.PERMS.getData(p, main).asListString().contains(perm)){
					return true;
				}
			}
			
			return false;
		}
		
		return true;
	}
	
	public boolean hasFlag(String region, WubFlag flag){
		if (doesRegionExist(region)){
			return main.getRegions().get(region).get(flag.toString()) != null ? main.getRegions().get(region).getBool(flag) : false;
		}
		return false;
	}
	
	private int i(String i){
		return Integer.parseInt(i);
	}
	
	private double d(String d){
		return Double.parseDouble(d);
	}
}