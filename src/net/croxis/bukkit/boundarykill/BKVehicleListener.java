package net.croxis.bukkit.boundarykill;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class BKVehicleListener extends VehicleListener{

	private BoundaryKill plugin;

	public BKVehicleListener(BoundaryKill boundaryKill) {
		this.plugin = boundaryKill;
	}
	
	public void onVehicleMove (VehicleMoveEvent event){
		//Entity player = event.getVehicle().getPassenger();
		//Location location = player.getLocation();
		//String worldName = player.getWorld().getName();
		//plugin.inBorder(player, location, worldName);	
		Entity passenger = event.getVehicle().getPassenger(); 
		if (passenger instanceof Player){
			Player player = (Player)event.getVehicle().getPassenger();
			Location location = event.getVehicle().getLocation();
			World world = event.getVehicle().getWorld();
			String worldName = player.getWorld().getName();
			plugin.inBorder(player, location, world, worldName);
		}
	}

}
