package com.github.lyokofirelyte.Wubalubadubdub.Data;

public enum WubRank {

	/*BARON(0, "1"),
	FREIHERR(1, "2"),
	VISCOUNT(2, "3"),
	COUNT(3, "4"),
	LANGRAVE(4, "5"),
	MARQUIS(5, "6"),
	ELECTOR(6, "7"),
	INFANTE(7, "8"),
	DAUPHIN(8, "9"),
	PRINCE(9, "a"),
	DUKE(10, "b"),
	GRAND_PRINCE(11, "c"),
	GRAND_DUKE(12, "d"),
	ARCHDUKE(13, "e");*/
	
	SERF(0, "1"),
	PEASANT(1, "2"),
	YEOMAN(2, "3"),
	SQUIRE(3, "4"),
	REEVE(4, "5"),
	KNIGHT(5, "6"),
	BARON(6, "7"),
	VISCOUNT(7, "8"),
	COUNT(8, "9"),
	DUKE(9, "a"),
	REGENT(10, "b"),
	PRINCE(11, "c"),
	KING(12, "d"),
	EMPEROR(13, "e");
	
	WubRank(int rank, String color){
		this.rank = rank;
		this.color = color;
	}
	
	public int getRank(){
		return rank;
	}
	
	public String getColor(){
		return "&" + color;
	}
	
	private int rank;
	private String color;
}