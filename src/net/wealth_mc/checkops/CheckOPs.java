package net.wealth_mc.checkops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

public class CheckOPs extends JavaPlugin {
	protected static CheckOPs instance;
	private CmdOPs cmd;
		
	protected int cheop;
	
	protected long timesKeyJoin;
	
	protected String kickMessage;
	protected String keysMessage;
	
	protected List<String> groupsop;
	protected List<String> comandop;
	protected List<String> permsspl;
	protected Set<String> listsops = new HashSet<String>();
	
	protected Map<String, Object> playerop = new HashMap<String, Object>();
	
	@Override
	public void onEnable() {
		instance = this;		
	
		this.getConfig().options().copyDefaults(true).copyHeader(true);
		this.saveDefaultConfig();

		cheop = this.getConfig().getInt("ops.check");
		kickMessage = this.getConfig().getString("ops.kmess");
		keysMessage = this.getConfig().getString("ops.cmess");
		timesKeyJoin = this.getConfig().getLong("ops.times");
		groupsop = this.getConfig().getStringList("ops.groupsop");
		comandop = this.getConfig().getStringList("ops.comand");
		permsspl = this.getConfig().getStringList("ops.permissions");
		playerop = this.getConfig().getConfigurationSection("ops.playerop").getValues(false);

		cheopIn();
		getServer().getPluginManager().registerEvents(new ListOPs(this), this);
		cmd = new CmdOPs(this);
		getCommand("cop").setExecutor(cmd);
		VaultOPs.init();
	}
	protected void cheopIn() {
		if (cheop == 1) {
			getLogger().info("Плагин CheckOPs, включена проверка по группам");
		} else if (cheop == 2) {
			getLogger().info("Плагин CheckOPs, включена проверка по никам");
		} else if (cheop == 3) {
			getLogger().info("Плагин CheckOPs, включена проверка по ник + ключ");
		} else if (cheop == 0) {
			getLogger().info("Плагин CheckOPs, защита отключена!");
		} else {
			cheop = 0;
			getLogger().info("Плагин CheckOPs - защита отключена!");
		}
	}
	protected void reloadConf() {
		this.reloadConfig();
		cheop = this.getConfig().getInt("ops.check");
		kickMessage = this.getConfig().getString("ops.kmess");
		keysMessage = this.getConfig().getString("ops.cmess");
		timesKeyJoin = this.getConfig().getLong("ops.times");
		groupsop = this.getConfig().getStringList("ops.groupsop");
		comandop = this.getConfig().getStringList("ops.comand");
		permsspl = this.getConfig().getStringList("ops.permissions");
		playerop.clear();
		playerop = this.getConfig().getConfigurationSection("ops.playerop").getValues(false);
		
		cheopIn();
	}
	protected synchronized void saveLogs(String log) throws FileNotFoundException {
		String data = java.util.Calendar.getInstance().getTime().toString();
		String logs = data + " - " + log;
		File dir = new File (getDataFolder()+File.separator+"Log"+File.separator);
		if (!dir.exists()) dir.mkdirs();
        File f = new File (getDataFolder()+File.separator+"Log"+File.separator+"checkops.log");
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
