package net.croxis.bukkit.boundarykill;

public class BKKiller implements Runnable{

	private BoundaryKill plugin;

	public BKKiller(BoundaryKill boundaryKill) {
		this.plugin = boundaryKill;
	}

	@Override
	public void run() {
		int i = 0;
		for(i=0; i<plugin.outofBounds.size(); i++){
			if (plugin.outofBounds.get(i).getHealth() - 1 >= 0){
				plugin.outofBounds.get(i).damage(plugin.health);
			}
		}	
	}

}
