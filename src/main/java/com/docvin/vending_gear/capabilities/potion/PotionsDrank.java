package com.docvin.vending_gear.capabilities.potion;

import com.docvin.vending_gear.VendingGear;

/**
 * This is the main object for defining the methods inside IPotiondDrank
 */
public class PotionsDrank implements IPotionsDrank {

	private int potionsDrank = 0;
	private int drinksNeeded = VendingGear.config.spawnConfigs.getGoal();

	@Override
	public void reset() {
		potionsDrank = 0;
		drinksNeeded = VendingGear.config.spawnConfigs.getGoal();
	}

	@Override
	public void add() {
		potionsDrank++;
	}

	@Override
	public boolean hasDrankEnough() {
		if (this.totalDrank() >= drinksNeeded) {
			return true;
		}
		return false;
	}

	@Override
	public int totalDrank() {
		return potionsDrank;
	}

	@Override
	public void setDrinksNeeded(int count) {
		drinksNeeded = count;
	}

	@Override
	public void setDrank(int count) {
		potionsDrank = count;
	}

	@Override
	public int drinksNeeded() {
		// TODO Auto-generated method stub
		return drinksNeeded;
	}

}
