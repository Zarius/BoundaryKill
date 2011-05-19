package net.croxis.bukkit.boundarykill;

public class Boundary {
	private int radius, x, z;
	
	public Boundary(int radius, int x, int z){
        this.setRadius(radius);
        this.setX(x);
        this.setZ(z);
    }

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getZ() {
		return z;
	}

}
