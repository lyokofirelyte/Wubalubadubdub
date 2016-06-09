package com.github.lyokofirelyte.Wubalubadubdub.Timer;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubMarkkitItem;
import com.github.lyokofirelyte.Wubalubadubdub.System.SystemProtect;

import lombok.Getter;
import lombok.Setter;

public class TimerMarkkit implements WubTimer {
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private long cycleDelay = 0L;
	
	@Getter @Setter
	private long startDelay = 0L;
	
	@Getter @Setter
	private int id;
	
	private Wub main;
	private Set<Material> set = null;
	
	public TimerMarkkit(String name, long cycleDelay, long startDelay, Wub main){
		this.name = name;
		this.cycleDelay = cycleDelay;
		this.startDelay = startDelay;
		this.main = main;
	}

	public int cycle(){
		this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
			public void run(){
				for (Player p : Bukkit.getOnlinePlayers()){
					if (((SystemProtect)main.getInstance(SystemProtect.class)).isInRegion(p.getLocation(), "markkit")){
						Block b = p.getTargetBlock(set, 30);
						if (b != null && (b.getType().equals(Material.SIGN) || b.getType().equals(Material.WALL_SIGN))){
							Sign sign = (Sign) b.getState();
							if (sign.getLines().length == 4 && sign.getLines()[0].contains("[ Wub ]")){
								WubMarkkitItem item = main.getMarkkitItemFromLines(sign.getLines());
								if (item != null){
									if (sign.getLines()[1].contains("Buy")){
										main.sendTitle(p, main.AS("&bBuy " + item.getBuyAmt() + " " + item.getName()), "&b&o" + ((item.getBuyAmt() / 64) * item.getBuyStackAmt()) + " sticks");
									} else if (sign.getLines()[1].contains("Sell")){
										main.sendTitle(p, main.AS("&bSell " + item.getSellAmt() + " " + item.getName()), "&b&o" + ((item.getSellAmt() / 64) * item.getSellStackAmt()) + " sticks");
									}
									main.updateDisplayBar(p, "&aRight-click to confirm transaction!");
								}
							}
						}
					}
				}
			}
		}, startDelay, cycleDelay);
		return id;
	}
	
	public void cancel(){
		Bukkit.getScheduler().cancelTask(id);
		main.tasks.remove(name);
	}
}