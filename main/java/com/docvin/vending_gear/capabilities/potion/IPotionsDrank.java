package com.docvin.vending_gear.capabilities.potion;

public interface IPotionsDrank {
	public void reset();

	public void add();

	public void setDrank(int count);

	public boolean hasDrankEnough();

	public int totalDrank();

	public int drinksNeeded();

	public void setDrinksNeeded(int count);
}
