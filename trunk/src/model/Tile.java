package model;

public class Tile {

	protected double dust;

	public Tile(double dust) {
		super();
		this.dust = dust;
	}

	public double getDust() {
		return dust;
	}

	public void setDust(double dust) {
		this.dust = dust;
	}

	public void addDust(double dust) {
		this.dust += dust;
	}

	public void removeDust(double dust) {
		this.dust -= dust;
	}

}
