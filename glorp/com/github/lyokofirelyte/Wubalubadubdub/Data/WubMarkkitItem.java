package com.github.lyokofirelyte.Wubalubadubdub.Data;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class WubMarkkitItem {

	@Getter @Setter
	private int id;
	
	@Getter @Setter
	private byte idByte;
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private int sellAmt;
	
	@Getter @Setter
	private int buyAmt;
	
	@Getter @Setter
	private int sellStackAmt;
	
	@Getter @Setter
	private int buyStackAmt;
	
	@Getter @Setter
	private ItemStack item;
	
	public WubMarkkitItem(int id, byte idByte, String name, int sellAmt, int buyAmt, int sellStackAmt, int buyStackAmt){
		this.id = id;
		this.idByte = idByte;
		this.name = name;
		this.sellAmt = sellAmt;
		this.buyAmt = buyAmt;
		this.sellStackAmt = sellStackAmt;
		this.buyStackAmt = buyStackAmt;
		this.item = new ItemStack(id, 64, idByte);
	}
}