package com.uzm.hylex.core.commands;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.Core;
import com.uzm.hylex.core.utils.HylexMethods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpeedCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§fHey brother, stop do it! You cannot execute commands.");
			return true;
		}

		Player player = (Player) sender;
		if (!player.hasPermission(Core.getLoader().permissions.get(label.toLowerCase()))) {
			player.sendMessage("§c[Stone] §cSem §c§npermissão §cpara executar esse comando.");
			return true;
		}

		if (label.equalsIgnoreCase("speed")) {
			if (args.length == 1) {
				if (HylexMethods.isNumeric(args[0])) {
					float value = Float.parseFloat(args[0]);
					if (value <= 1) {
						player.sendMessage("§aVocê alterou sua velocidade de vôo para §f§n" + value + "§a.");
						player.setFlySpeed(value);
					} else {
						player.sendMessage("§c[Stone] O valor cedido deve ser um número entre 0 e 1.");
					}
				} else {
					player.sendMessage("§c[Stone] O valor cedido deve ser um número.");
				}
			} else {
				help(player, label);
			}
		}

		return false;
	}

	public static List<String> getInvoke() {
		return Lists.newArrayList("speed");
	}

	public void help(Player player, String label) {
		player.sendMessage("");
		player.sendMessage("   §eAjuda do comando §f'" + label + "'");
		player.sendMessage("");
		player.sendMessage("  §e- §f/" + label + " <valor> §7Altere a sua velocidade de vôo.");
		player.sendMessage("");
	}
}
