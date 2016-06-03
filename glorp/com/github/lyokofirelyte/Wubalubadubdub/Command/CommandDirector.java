package com.github.lyokofirelyte.Wubalubadubdub.Command;

import java.lang.reflect.Method;

import lombok.SneakyThrows;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandDirector implements Listener {

private Wub main;
	
	public CommandDirector(Wub w){
		main = w;
	}

	@EventHandler @SneakyThrows
	public void onIncomingCommand(PlayerCommandPreprocessEvent e){
		
		String[] args = e.getMessage().split(" ");
		String command = new String("" + args[0].replace("/", ""));
		Player p = e.getPlayer();
		
		if (args.length > 1){
			String[] newArgs = new String[args.length - 1];
			for (int i = 1; i < args.length; i++){
				newArgs[i-1] = args[i];
			}
			args = newArgs;
		} else {
			args = new String[0];
		}
		
		for (Object o : main.getCommands()){
			for (Method m : o.getClass().getMethods()){
				if (m.getAnnotation(WubCommand.class) != null){
					WubCommand c = m.getAnnotation(WubCommand.class);
					for (String currentCommand : c.commands()){
						if (command.equals(currentCommand)){
							if (c.perm().equals("wub.lub") || WubData.PERMS.getData(p, main).asListString().contains(c.perm())){
								m.invoke(o, args, p);
								e.setCancelled(true);
								return;
							} else {
								main.sendMessage(p, "&c&oNo perms!");
								e.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}
}