package net.alkarb.boxmanager;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class Box {

	String name;
	String displayname;
	List<String> lore;
	List<Double> rating;
	List<ItemStack> rewards;
	List<Sound> sounds;
	List<Boolean> epicset;
	List<Boolean> random;
	String epic;
	Boolean daily;
	

	public Box(String name, String displayname, List<String> lore, List<Double> rating, List<ItemStack> rewards,
			List<Sound> sounds, List<Boolean> epicset,List<Boolean> random, String epic, Boolean daily) {
		this.name = name;
		this.displayname = displayname;
		this.lore = lore;
		this.rating = rating;
		this.rewards = rewards;
		this.sounds = sounds;
		this.epicset = epicset;
		this.epic = epic;
		this.daily = daily;
		this.random = random;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayname;
	}

	public List<String> getLore() {
		return lore;
	}

	public List<Double> getRating() {
		return rating;
	}

	public List<ItemStack> getRewards() {
		return rewards;
	}

	public List<Sound> getSounds() {
		return sounds;
	}

	public List<Boolean> getEpicSet() {
		return epicset;
	}
	
	public List<Boolean> getRandom(){
		return random;
	}
	
	public String getEpic() {
		return epic;
	}

	public Boolean getDailyReward() {
		return daily;
	}


	
}
