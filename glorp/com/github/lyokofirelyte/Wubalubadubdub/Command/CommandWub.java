package com.github.lyokofirelyte.Wubalubadubdub.Command;

import java.lang.reflect.Method;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;

public class CommandWub implements Listener {
	
	private Wub main;
	
	public CommandWub(Wub w){
		main = w;
	}

	@WubCommand(commands = {"wub"}, perm = "wub.lub", help = "/wub")
	public void onWub(String[] args, Player p){
		main.sendMessage(p, "&6Showing all commands&f:");
		main.sendMessage(p, "&f-------------------------");
		for (Object o : main.getCommands()){
			for (Method m : o.getClass().getMethods()){
				if (m.getAnnotation(WubCommand.class) != null){
					WubCommand c = m.getAnnotation(WubCommand.class);
					main.sendMessage(p, "&a" + c.commands()[0] + "&f: &e" + c.desc());
				}
			}
		}
	}	
}