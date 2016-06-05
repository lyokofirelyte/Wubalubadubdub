package com.github.lyokofirelyte.Wubalubadubdub.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.lyokofirelyte.Wubalubadubdub.Wub;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubCommand;
import com.github.lyokofirelyte.Wubalubadubdub.Data.WubData;

public class CommandTell {
	
	private Wub main;
	
	public CommandTell(Wub w){
		main = w;
	}

	@WubCommand(commands = {"tell", "pm", "t", "msg"}, help = "/tell <player> <msg>", desc = "Message a Player!")
	public void onTell(String[] args, Player p){
		if (args.length >= 2){
			Player target = Bukkit.getPlayer(args[0]);
			if (target != null){
				String message = main.argsToString(args);
				message = message.replace(message.split(" ")[0], "");
				String toMessage = "&d\u1445 -> " + target.getDisplayName() + " &d\u1440 &7" + message;
				String fromMessage = "&d\u1445 <- " + p.getDisplayName() + " &d\u1440 &7" + message;
				p.sendMessage(main.AS(toMessage));
				target.sendMessage(main.AS(fromMessage));
				WubData.R.setData(target, p.getUniqueId().toString(), main);
			} else {
				main.sendMessage(p, "&c&oThat player is not online.");
			}
		} else {
			main.sendMessage(p, "&c&o/tell <player> <msg>");
		}
	}
	
	@WubCommand(commands = {"r", "reply"}, help = "/r <msg>", desc = "Reply to your last message!")
	public void onR(String[] args, Player p){
		if (!WubData.R.getData(p, main).asString().equals("none")){
			Player target = Bukkit.getPlayer(UUID.fromString(WubData.R.getData(p, main).asString()));
			if (target != null){
				p.chat("/msg " + target.getName() + " " + main.argsToString(args));
			} else {
				main.sendMessage(p, "&c&oThat player is not online.");
			}
		}
	}
}