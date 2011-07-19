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
//				System.out.println("Damaging player..."+plugin.outofBounds.get(i).getName());
				if (plugin.worlddbexception.get(plugin.outofBounds.get(i).getWorld().getName()).contains(plugin.outofBounds.get(i).getName()) == false){
					plugin.outofBounds.get(i).damage(plugin.health);
				}
			}
		}	
	}

}
