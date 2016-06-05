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
	
	SHOCHI(0, "1"),
	DAICHI(1, "2"),
	SHOGI(2, "3"),
	DAIGI(3, "4"),
	SHOSHIN(4, "5"),
	DAISHIN(5, "6"),
	SHOREI(6, "7"),
	DAIREI(7, "8"),
	SHOJIN(8, "9"),
	DAIJIN(9, "a"),
	SHOTOKU(10, "b"),
	DAITOKU(11, "c");
	//GRAND_DUKE(12, "d"),
	//ARCHDUKE(13, "e");
	
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