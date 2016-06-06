package com.github.lyokofirelyte.Wubalubadubdub.Command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lombok.SneakyThrows;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandDirector implements Listener {

private Wub main;

	List<String> errorMessages = Arrays.asList(
		"&c&oI have no idea what that is. Try &6&o/wub&c&o.",
		"&c&oWhat? That's not right. Try &6&o/wub&c&o.",
		"&c&oWe don't have that here. See &6&o/wub&c&o.",
		"&c&oEverytime you type in an invalid command, we kill a hostage.",
		"&c&oI too love to make up my own commands. See &6&o/wub&c&o.",
		"&c&oCommand not found. Did you mean &6&o/kick WinneonSword &c&o?",
		"&c&oYour unknown command level is now 99.",
		"&c&oHelp, I'm stuck in an error message factory!",
		"&c&oCommand not valid. I assume you're a bad speller?",
		"&c&oI'm afraid I can't do that, Dave. See &6&o/wub&c&o.",
		"&c&oOut of all the possible commands you choose an invalid one.",
		"&c&oYou tried. &6&o/wub&c&o.",
		"&b&oThis is a blue, misleading error message. &6&o/wub&b&o.",
		"&a&oSuccess! You typed an invalid command. &6&o/wub&a&o.",
		"&c&oI'm ashamed of you for trying that. &6&o/wub&c&o.",
		"&c&oNo, I will not perform that command. &6&o/wub&c&o.",
		"&c&oAnd the invalid command award goes to...",
		"&c&oGo fish. &6&o/wub&c&o.",
		"&c&oI'll do a lot of things, but I won't do that!",
		"&6&o/wubmepleaseIdontknowwhatImdoing",
		"&c&oThere's a time and place for everything, but not now!",
		"&c&oIf only that was really a command... &7&o/wub&c&o.",
		"&c&oHelp me help you help us all, by typing &6&o/wub&c&o."
	);
	
	public CommandDirector(Wub w){
		main = w;
	}

	@EventHandler
	public void onIncomingCommand(PlayerCommandPreprocessEvent e){
		
		if (e.isCancelled()){
			return;
		}
		
		String[] args = e.getMessage().split(" ");
		String command = new String("" + args[0].replace("/", ""));
		Player p = e.getPlayer();
		
		if (args.length >= 1){
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
								try {
									m.invoke(o, args, p);
								} catch (Exception eee){ eee.printStackTrace(); }
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
		
		if (Bukkit.getHelpMap().getHelpTopic(e.getMessage().split(" ")[0]) == null){
			e.setCancelled(true);
			main.sendMessage(e.getPlayer(), errorMessages.get(new Random().nextInt(errorMessages.size())));
		}
	}
}