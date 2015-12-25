package net.wealth_mc.checkops;

import java.util.List;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultOPs {
	
	private static boolean vault_perm = false;
	private static Permission permission = null;
	

	protected static void init() {
        if (checkVault()){
            vault_perm = setupPermissions();
        }else CheckOPs.instance.getLogger().info("Плагин Vault НЕ обнаружен! " +
        		"Качественная работа плагина НЕ ВОЗМОЖНА");
    }
    private static boolean checkVault(){
        Plugin vplg = Bukkit.getServer().getPluginManager().getPlugin("Vault");
        return  ((vplg != null)&&(vplg instanceof Vault));
    }
    private static boolean setupPermissions(){
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	
	protected static boolean isPermissionConected(){
        return vault_perm;
    }
	protected String getPrimaryGroup(Player p){
		if (!isPermissionConected()) return null;
		return permission.getPrimaryGroup(p);
	}
	protected boolean playerHas (Player p, String perm){
		if (!isPermissionConected()) {
			return false;
		}
		if (permission.playerHas(p, perm)){
			return true;
		}
		return false;
	}
	protected boolean playerHasPerm(Player p, List<String> perms){
		String perm = null;
		if (!isPermissionConected()) {
			return false;
		}
		for (int i = 0; i < perms.size(); i++){
			perm = perms.get(i);
			if (permission.playerHas(p, perm)){
				return true;
			}
		}
		return false;
	}
}
