package net.wealth_mc.checkops;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdOPs implements CommandExecutor {

	protected CmdOPs(CheckOPs plugin) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reload")) {
				return reloadConfig(sender);
			}else if (args[0].equalsIgnoreCase(cmdOPs(sender))) {
				return checkOPs(sender, args);
			}
		}
		return false;
	}
	private boolean checkOPs(CommandSender sender, String[] args) {
		Player p = null;
		String pn = sender.getName();
		String adminPermissions = "'*'";
		VaultOPs vaultops = new VaultOPs();
		if (sender instanceof Player) {
			p = (Player) sender;
			
			if((sender.isOp()
					|| vaultops.playerHas(p, adminPermissions)
					|| vaultops.playerHasPerm(p, CheckOPs.instance.permsspl))
					&& !CheckOPs.instance.listsops.contains(pn)
					&& CheckOPs.instance.cheop == 3) {
				sender.sendMessage(ChatColor.GOLD + "[CheckOPs]: " + ChatColor.AQUA 
						+ "секретный ключ принят.");
				CheckOPs.instance.listsops.add(pn);
				return true;
			}
		}
		return false;
	}
	private boolean reloadConfig(CommandSender sender) {
		if (!(sender instanceof Player)) {
			CheckOPs.instance.reloadConf();
			sender.sendMessage("[CheckOPs]: конфинг перегружен");
			return true;
		}
		return false;
		}
	private String cmdOPs(CommandSender sender) {
		String cm = null;
		String pn = sender.getName();
		if (sender instanceof Player) {
			for(Entry<String, Object> entry : CheckOPs.instance.playerop.entrySet()) {		
				if(entry.getKey().equals(pn)) {
					cm = (String) entry.getValue();
					return cm;
				}
			}
		}
		return null;
	}

}
