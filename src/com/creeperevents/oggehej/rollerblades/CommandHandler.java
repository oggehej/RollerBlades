package com.creeperevents.oggehej.rollerblades;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
	private RollerBlades plugin;
	CommandHandler(RollerBlades instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String title, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.AQUA + " -- [RollerBlades v" + plugin.getDescription().getVersion() + "] --");
			if(sender.hasPermission("rollerblades.reload"))
				sender.sendMessage(ChatColor.GOLD + "/RollerBlades reload" + ChatColor.WHITE + " - Reload the config");
		} else if(args[0].equalsIgnoreCase("reload"))
			if(sender.hasPermission("rollerblades")) {
				plugin.reloadConfig();
				plugin.saveConfig();
				sender.sendMessage(ChatColor.GOLD + "Config reloaded!");
			}
			else
				sender.sendMessage(ChatColor.RED + "No permission!");
		else
			sender.sendMessage(ChatColor.RED + "Invalid arguments!");
		return true;
	}
}
