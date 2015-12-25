package net.wealth_mc.checkops;

import java.io.FileNotFoundException;
import java.util.Map.Entry;

import net.wealth_mc.checkops.CheckOPs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import fr.xephi.authme.events.LoginEvent;

public class ListOPs implements Listener {

	CheckOPs plg;
	protected static ListOPs instance;
	
	protected ListOPs(CheckOPs pl) {
		this.plg = pl;
	}
	
	protected ListOPs() {
	}

	protected Player player;

// ** Events для проверки уровня зачарований **
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDropItem(PlayerDropItemEvent event){
		if (!CheckOPs.enchantcheck) return;
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!CheckOPs.enchantcheck) return;
		CheckEnchant.checkInteract(event);
	}
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (!CheckOPs.enchantcheck) return;
		CheckEnchant.checkItemPickup(event);
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!CheckOPs.enchantcheck) return;
		CheckEnchant.checkInventoryClick(event);
	}
// **

// ** Events для проверки особых привилегий **
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		instance = this;
		Player playerCmd = event.getPlayer();
		String cmd;

        if(event.getMessage().indexOf(" ") == -1){
            cmd = event.getMessage().substring(1);
        }else{
            cmd = event.getMessage().substring(0, event.getMessage().indexOf(" ")).substring(1);
        }
		String playerName = playerCmd.getName();
		VaultOPs vaultops = new VaultOPs();
		String group = vaultops.getPrimaryGroup(playerCmd);
		String adminPermissions = "'*'";
		String logs = null;
		
		if (playerCmd.isOp()
				|| vaultops.playerHas(playerCmd, adminPermissions)
				|| vaultops.playerHasPerm(playerCmd, CheckOPs.instance.permsspl)) {
			
			if (CheckOPs.instance.comandop.contains(cmd)) {
				logs = "Игрок: " + playerName
						+ " с правами Администратора использовал команду из конфига: "
						+ event.getMessage();
				CheckOPs.instance.getLogger().info(logs);
				try {
					CheckOPs.instance.saveLogs(logs);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				if (CheckOPs.instance.cheop == 1) {
					if (CheckOPs.instance.groupsop.contains(group)) {
						return;
					}
				} else if (CheckOPs.instance.cheop == 2) {
					for(Entry<String, Object> entry : CheckOPs.instance.playerop.entrySet()) {		
						if(entry.getKey().equals(playerName)) {
								return;
						}
					}
				} else if (CheckOPs.instance.cheop == 3) {
					
					for(Entry<String, Object> entry : CheckOPs.instance.playerop.entrySet()) {		
						if(entry.getKey().equals(playerName)) {
							if (!CheckOPs.instance.listsops.isEmpty() &&
									CheckOPs.instance.listsops.contains(playerName)){
								return;
							}
						}
					}
				}
				event.setCancelled(true);
				if (playerCmd.isOnline()) {
					playerCmd.kickPlayer(CheckOPs.instance.kickMessage);
					logs = "Команда запрещена! Игрок: " + playerName
							+ " был кикнут по причине: "
							+ CheckOPs.instance.kickMessage;
					try {
						CheckOPs.instance.saveLogs(logs);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@EventHandler
	public void onAuthLoginEvent(LoginEvent event) {
		instance = this;
		player = event.getPlayer();
		if (plg.cheop != 0) {
			RunOPs o = new RunOPs();
			Thread r = new Thread(o);
			r.start();
		}	
	}

	@EventHandler
	   public void onQuitPlayer(PlayerQuitEvent event){
		String playerName = event.getPlayer().getName();
		String logs = "Игрок с правами Администратора: " + playerName + " разлогинился";
		if (CheckOPs.instance.cheop == 3) {
			
			for(Entry<String, Object> entry : CheckOPs.instance.playerop.entrySet()) {		
				if(entry.getKey().equals(playerName)) {
					if (!CheckOPs.instance.listsops.isEmpty() &&
							CheckOPs.instance.listsops.contains(playerName)){
						CheckOPs.instance.listsops.remove(playerName);
						CheckOPs.instance.getLogger().info(logs);
						try {
							CheckOPs.instance.saveLogs(logs);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
