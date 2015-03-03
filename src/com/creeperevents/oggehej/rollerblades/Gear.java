package com.creeperevents.oggehej.rollerblades;

import com.creeperevents.oggehej.rollerblades.exceptions.NoGearException;

public enum Gear
{
	NONE(0),
	LOW(1),
	MID(2),
	HIGH(3);

	private int num;

	Gear(int num)
	{
		this.num = num;
	}

	/**
	 * Return the speed of the gear
	 * 
	 * @return Speed
	 */
	public float getSpeed(RollerBlades plugin)
	{
		if(num == 0)
			return .2F;
		else
			return (float) plugin.getConfig().getDouble("Gears." + num);
	}

	/**
	 * Return the gear as a number
	 * @return Gear
	 */
	public int getGear()
	{
		return this.num;
	}

	/**
	 * Return next gear
	 * 
	 * @return Gear
	 * @throws NoGearException No lower gear
	 */
	public Gear nextGear() throws NoGearException
	{
		switch(num)
		{
		case 0:
			return Gear.LOW;
		case 1:
			return Gear.MID;
		case 2:
			return Gear.HIGH;
		default:
			throw new NoGearException();
		}
	}

	/**
	 * Return previous gear
	 * 
	 * @return Gear
	 * @throws NoGearException No higher gear
	 */
	public Gear prevGear() throws NoGearException
	{
		switch(num)
		{
		case 2:
			return Gear.LOW;
		case 3:
			return Gear.MID;
		default:
			throw new NoGearException();
		}
	}
}
