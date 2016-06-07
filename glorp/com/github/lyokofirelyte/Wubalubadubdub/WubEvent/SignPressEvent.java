package com.github.lyokofirelyte.Wubalubadubdub.WubEvent;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubRegion;

import lombok.Getter;
import lombok.Setter;

public class SignPressEvent {
	
	@Getter @Setter
	private Sign sign;
	
	@Getter @Setter
	private Location signLoc;
	
	@Getter @Setter
	private WubRegion region;
	
	@Getter @Setter
	private Player presser;
	
	@Getter @Setter
	private String[] lines = new String[0];
	
	@Getter @Setter
	private Wub main;

	public SignPressEvent(Sign sign, Location signLoc, WubRegion region, Player p, String[] lines, Wub main){
		this.sign = sign;
		this.signLoc = signLoc;
		this.region = region != null ? region : null;
		this.presser = p;
		this.lines = lines;
		this.main = main;
	}
	
	public void fire(){
		
		if (region.getName().equals("emerald")){
			if (lines[0].contains("[ Wub ]")){
				if (lines[1].contains("Checkpoint")){
					WubData.CHECKPOINT.setData(presser, main.locToString(presser.getLocation()), main);
					main.sendMessage(presser, "&aCheckpoint set!");
				}
				if (lines[1].contains("Complete") && lines[2].contains("Parkour 1")){
					main.broadcast(presser.getDisplayName() + " &bhas finished Parkour 1 and obtained 500 trading sticks!");
					WubData.CHECKPOINT.setData(presser, "none", main);
					WubData.TRADING_STICKS.setData(presser, WubData.TRADING_STICKS.getData(presser, main).asInt() + 500, main);
					presser.chat("/spawn");
				}
			}
		}
		
		if (region.getName().equals("markkit")){
			for (int i = 0; i < lines.length; i++){
				lines[i] = ChatColor.stripColor(main.AS(lines[i]));
			}
			if (lines[0].equals("[ Wub ]")){
				
			}
		}
	}
}