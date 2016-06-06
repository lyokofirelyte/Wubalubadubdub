package com.github.lyokofirelyte.Wubalubadubdub.WubEvent;

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
			if (lines.length == 4){
				if (lines[0].contains("[ Wub ]")){
					if (lines[1].contains("Checkpoint")){
						WubData.CHECKPOINT.setData(presser, main.locToString(signLoc), main);
						main.sendMessage(presser, "&aCheckpoint set!");
					}
				}
			}
		}
	}
}