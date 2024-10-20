package com.docvin.vending_gear;

import java.util.concurrent.ThreadLocalRandom;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.sync.ConfigSynchronization;

public class VendingGearConfigs {
	@CreativeConfig(type = ConfigSynchronization.UNIVERSAL)
	public SpawnConfigs spawnConfigs = new SpawnConfigs();

	@CreativeConfig(type = ConfigSynchronization.SERVER)
	public TankConfigs tankConfigs = new TankConfigs();

	public class TankConfigs {

	}

	public class SpawnConfigs {
		@CreativeConfig
		// Tells if a random number should be used
		public boolean randomCount = true;

		@CreativeConfig
		@CreativeConfig.IntRange(min = 1, max = Integer.MAX_VALUE - 1)
		// Smallest possible number to randomly generate
		public int min = 1;

		@CreativeConfig
		@CreativeConfig.IntRange(min = 1, max = Integer.MAX_VALUE)
		// When randomCount is true, Largest possible number to randomly generate
		// when randomCount is false, Goal for how many potions to drink
		public int max = 25;

		/**
		 * Gives the total number of potions needed to be drank
		 * 
		 * @return When {@link SpawnConfigs#randomCount} is True it will return a random
		 *         number between {@link SpawnConfigs#min} and {@link SpawnConfigs#max}.
		 *         <li>When {@link SpawnConfigs#randomCount} is False it will return
		 *         {@link SpawnConfigs#max} as the goal.</li>
		 */
		public int getGoal() {
			return randomCount ? ThreadLocalRandom.current().nextInt(spawnConfigs.min, spawnConfigs.max + 1) : max;
		}

	}
}
