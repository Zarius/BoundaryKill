package net.croxis.bukkit.boundarykill;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

public class BoundaryKill extends JavaPlugin{

	private BKPlayerListener playerListener;
	private BKVehicleListener vehicleListener;
	public static Logger log;
	public int health = 1;
	public int ticks = 10;
	public String outMessage = "You have exited the boundary. Prepair to DIE!";
	public String inMessage = "You have returned to safer lands.";
	@SuppressWarnings("rawtypes")
	public HashMap<String, HashMap> worlddb = new HashMap<String, HashMap>(); 
	public HashMap<String, List<String>> worlddbexception = new HashMap<String, List<String>>(); 
	private int taskID;
	private Runnable killer;
	
	public ArrayList<Player> outofBounds;
	private Configuration _config;


	@Override
	public void onDisable() {
		outofBounds.clear();
		
	}

	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		_config = this.getConfiguration();

        initializeSettings(getDataFolder());
        
        Map<String, ConfigurationNode> worlds = _config.getNodes("worlds");

        for(Map.Entry<String, ConfigurationNode> entry : worlds.entrySet()){	
        	String name = entry.getKey();
        	int x = worlds.get(name).getInt("x", 0);
        	int z = worlds.get(name).getInt("z", 0);
        	int r = worlds.get(name).getInt("radius", 0);
        	ArrayList<String> except = new ArrayList<String>();
            except.add("croxis");
    		List<String> exceptions = worlds.get(name).getStringList("exceptions", except);
        	HashMap<String, Integer> world = new HashMap<String, Integer>();
        	world.put("x", x);
        	world.put("z", z);
        	world.put("radius", r);
        	worlddb.put(name, world);
        	worlddbexception.put(name, exceptions);
        }
        
        outMessage = _config.getString("outMessage", "You have exited the boundary. Prepair to DIE!");
        inMessage = _config.getString("inMessage", "You have returned to safer lands.");
        
        updateConfig();

        outofBounds = new ArrayList<Player>();
        playerListener = new BKPlayerListener(this);
        vehicleListener = new BKVehicleListener(this);
        killer = new BKKiller(this);
        taskID = getServer().getScheduler().scheduleSyncRepeatingTask(this, killer, ticks, ticks);
        
        
        if (taskID == -1){
        	log.warning("Scheduler failed for BoundaryKill!");
        }
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.VEHICLE_MOVE, vehicleListener, Priority.Normal, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( "["+pdfFile.getName() + ":" + pdfFile.getVersion() + "] enabled!" );
	}
	
	private void updateConfig() {
		int version = _config.getInt("version", 0);
		if (version == 0){
			Map<String, ConfigurationNode> worlds = _config.getNodes("worlds");
			
			ArrayList<String> except = new ArrayList<String>();
            except.add("croxis");
	        for(Map.Entry<String, ConfigurationNode> entry : worlds.entrySet()){	
	        	String name = entry.getKey();
	        	worlds.get(name).setProperty("exceptions", except);
	        	_config.setProperty("version", 1);
	        	_config.save();
	        }
		}
		
	}

	public void addPlayer(Player player){
		//Adds a player to the out of bound list
		if(outofBounds.contains(player) != true){
			outofBounds.add(player);
			if (worlddbexception.get(player.getWorld().getName()).contains(player.getName()) == true){
				player.sendMessage(ChatColor.RED + outMessage + " (You are in the exception list)");
			} else {
				player.sendMessage(ChatColor.RED + outMessage);
			}
		}
	}
	
	public void removePlayer(Player player){
		//Removes player from out of bounds list
		if(outofBounds.contains(player) == true){
			outofBounds.remove(player);
			player.sendMessage(ChatColor.BLUE + inMessage);
		}
	}
	
	private void initializeSettings(File dataFolder) {
		if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
            _config.setProperty("worlds.world.x", 0);
            _config.setProperty("worlds.world.z", 0);
            _config.setProperty("worlds.world.radius", 0);
            ArrayList<String> except = new ArrayList<String>();
            except.add("croxis");
            _config.setProperty("worlds.world.exceptions", except);
            _config.setProperty("ticks", 10);
            _config.setProperty("health", 1);
            _config.setProperty("outMessage", "You have exited the boundary. Prepair to DIE!");
            _config.setProperty("inMessage", "You have returned to safer lands.");
            _config.save();
        }
	}
	
	public void inBorder(Player player, Location location, World world, String worldName){//PlayerMoveEvent event){
		if (worlddb.containsKey(worldName) == false){
			return;
		}

		int radius = (Integer) worlddb.get(worldName).get("radius");
		if (radius == 0){
			return;
		}
		if (isInBorder(location, worldName) == false){
			addPlayer(player);
		} else {
			removePlayer(player);
		}
		
	}

	public boolean isInBorder(Location location, String worldName) {
		int radius = (Integer) worlddb.get(worldName).get("radius");
		int x = (Integer) worlddb.get(worldName).get("x");
		int z = (Integer) worlddb.get(worldName).get("z");
		int dX = (int) (location.getX() - x);
		int dZ = (int) (location.getZ() - z);
		double distance = Math.sqrt(Math.pow(dX,2) + Math.pow(dZ, 2));
		
		if (distance >= radius){
			return false;
		} 
		return true;
	}

}
