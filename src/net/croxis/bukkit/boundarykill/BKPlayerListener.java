package net.croxis.bukkit.boundarykill;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BKPlayerListener extends PlayerListener{

	private BoundaryKill plugin;

	public BKPlayerListener(BoundaryKill boundaryKill) {
		this.plugin = boundaryKill;
	}

	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();
		World world = player.getWorld();
		String name = world.getName();
		plugin.inBorder(player, location, world, name);

	}
	
	public void onPlayerTeleport(PlayerTeleportEvent event){
		Player player = event.getPlayer();
		Location location = event.getTo();
		World world = player.getWorld();
		String name = world.getName();
		if (plugin.isInBorder(location, name) == false){
			event.setCancelled(true);
			player.sendMessage("That target is outside the border.");
		} 
	}
	
	public void onPlayerQuit(PlayerEvent event){
		plugin.removePlayer(event.getPlayer());
	}

}
