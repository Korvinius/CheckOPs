package net.wealth_mc.checkops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class CheckEnchant {

	public static void checkInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		String logs = null;
		if (item == null) return;
		if (checkEnchant(item)) {
			item = new ItemStack(Material.AIR);
			player.setItemInHand(item);
			player.sendMessage(CheckOPs.enchantmess);
			event.setCancelled(true);
			logs = event.getPlayer().getName() + ": " + event.getEventName()
					+ ": " + event.getItem();
			try {
				saveEnchantLevelLogs(logs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void onPlayerDropItem(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		String logs = null;
		if (item == null) return;
		if (checkEnchant(item)) {
			player.sendMessage(CheckOPs.enchantmess);
			event.setCancelled(true);
			logs = event.getPlayer().getName() + ": " + event.getEventName()
					+ ": " + event.getItemDrop().getItemStack();
			try {
				saveEnchantLevelLogs(logs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void checkItemPickup(PlayerPickupItemEvent event) {
		ItemStack item = event.getItem().getItemStack();
		if (item == null) return;
		if (checkEnchant(item)) {
			event.setCancelled(true);
		}
	}

	public static void checkInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		String logs = null;
		ItemStack item = event.getCurrentItem();
		if (item == null) return;
		if (checkEnchant(item)) {
			item = new ItemStack(Material.AIR);
			player.setItemInHand(item);
			player.sendMessage(CheckOPs.enchantmess);
			event.setCancelled(true);
			logs = event.getWhoClicked().getName() + ": " + event.getEventName()
					+ ": " + event.getCurrentItem();
			try {
				saveEnchantLevelLogs(logs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean checkEnchant(ItemStack item) {
		for (Integer i : item.getEnchantments().values()) {
			if (i.intValue() > CheckOPs.enchantlevel) {
				return true;
			}
		}
		return false;
	}
	protected synchronized static void saveEnchantLevelLogs(String log) throws FileNotFoundException {
		String data = java.util.Calendar.getInstance().getTime().toString();
		String logs = data + " - " + log;
		File dir = new File (CheckOPs.instance.getDataFolder()+File.separator+"Log"+File.separator);
		if (!dir.exists()) dir.mkdirs();
        File f = new File (CheckOPs.instance.getDataFolder()+File.separator+"Log"+File.separator+"enchant.log");
        if(!f.exists()){
            try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        PrintStream printStream = new PrintStream(new FileOutputStream(f, true), true);
		printStream.println(logs);
		printStream.close();
        
	}
}
