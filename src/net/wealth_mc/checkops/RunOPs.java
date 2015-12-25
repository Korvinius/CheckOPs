package net.wealth_mc.checkops;

import java.io.FileNotFoundException;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class RunOPs implements Runnable {
	protected Player playerOPs;

	@Override
	public void run() {
		VaultOPs vaultops = new VaultOPs();
		playerOPs = ListOPs.instance.player;
		String playerName = playerOPs.getName();
		String group = vaultops.getPrimaryGroup(playerOPs);
		String adminPermissions = "'*'";
		String logs = null;
		
		if (playerOPs.isOp()
				|| vaultops.playerHas(playerOPs, adminPermissions)
				|| vaultops.playerHasPerm(playerOPs, CheckOPs.instance.permsspl)) {
			logs = "Залогинился: " + playerName
					+ " с правами Администратора!";
			CheckOPs.instance.getLogger().info(logs);
			try {
				CheckOPs.instance.saveLogs(logs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (CheckOPs.instance.cheop == 1) {
				if (CheckOPs.instance.groupsop.contains(group)) {
					logs = "Игрок: " 
							+ playerName + " состоит в группе: " + group + ", вход разрешен.";
					CheckOPs.instance.getLogger().info(logs);
					try {
						CheckOPs.instance.saveLogs(logs);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					return;
				}
			} else if (CheckOPs.instance.cheop == 2) {
				for(Entry<String, Object> entry : CheckOPs.instance.playerop.entrySet()) {		
					if(entry.getKey().equals(playerName)) {
						logs = "Для игрока: " + playerName +
								" есть разрешение на вход с правами Администратора.";
						CheckOPs.instance.getLogger().info(logs);
						try {
							CheckOPs.instance.saveLogs(logs);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
							return;
					}
				}
			} else if (CheckOPs.instance.cheop == 3) {
				long delay = CheckOPs.instance.timesKeyJoin*1000;
				long delmess = 5000;
				
				for(Entry<String, Object> entry : CheckOPs.instance.playerop.entrySet()) {		
					if(entry.getKey().equals(playerName)) {
						try {
							for (long i = delay; i > delmess; i = i-delmess) {
								if (!CheckOPs.instance.listsops.isEmpty() &&
										CheckOPs.instance.listsops.contains(playerName)){
									logs = "Игрок: " + playerName +
											" подтвердил свою подлинность," +
											" вход с правами Администратора разрешен.";
									CheckOPs.instance.getLogger().info(logs);
									try {
										CheckOPs.instance.saveLogs(logs);
									} catch (FileNotFoundException e) {
										e.printStackTrace();
									}
									return;
								}
								playerOPs.sendMessage(ChatColor.GOLD + "[CheckOPs]: " + ChatColor.RED 
										+ CheckOPs.instance.keysMessage);
								Thread.sleep(delmess);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			if (playerOPs.isOnline()) {
				BukkitScheduler scheduler = CheckOPs.instance.getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(CheckOPs.instance, new Runnable() {
					@Override
					public void run() {
						playerOPs.kickPlayer(CheckOPs.instance.kickMessage);
						String logs = "Игрок: " + playerOPs
								+ " был кикнут по причине: "
								+ CheckOPs.instance.kickMessage;
						try {
							CheckOPs.instance.saveLogs(logs);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}, 5L);
			}
		}
	}
}

