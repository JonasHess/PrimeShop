package eu.Blockup.PrimeShop.Other;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class CooldownManager {

	private long time;
	private HashMap<String, Long> players;
	
	public CooldownManager(int time, Time type) {
		if(type == Time.SECONDS)
			this.time = time * 1000;
		else
			this.time = time * 1000 * type.getValue();
		this.players = new HashMap<String, Long>();
	}
	
	public CooldownManager(long miliseconds) {
		this.time = miliseconds;
		this.players = new HashMap<String, Long>();
	}
	
	public enum Time {
		
		SECONDS(0),
		MINUTES(60),
		HOURS(3600),
		DAYS(86400);
		
		private int value;
		
		Time(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
	}
	
//	private boolean contains(Player player) {
//		return players.containsKey(player.getName());
//	}
	
//	private void remove(Player player) {
//		players.remove(player.getName());
//	}
	
	private boolean interact(Player player) {
		long currentTime = System.currentTimeMillis();
		
		if(players.containsKey(player.getName())) {
			if((players.get(player.getName()) + time) > currentTime) return false;
		}
		
		players.put(player.getName(), currentTime);
		
		return true;
	}
	
	private boolean allowed(Player player) {
		long currentTime = System.currentTimeMillis();
		
		if(players.containsKey(player.getName())) {
			if((players.get(player.getName()) + time) > currentTime) return false;
		}
		return true;
	}
	
//	private int timeLeft(Player player, Time type) {
//		if(type == Time.SECONDS) {
//			return (int)(((players.get(player.getName()) + time) - System.currentTimeMillis())/1000);
//		}
//		
//		return (int)(((players.get(player.getName()) + time) - System.currentTimeMillis())/1000/type.getValue());
//	}
//	
//	private long timeLeftRaw(Player player) {
//		return (((players.get(player.getName()) + time) - System.currentTimeMillis()));
//	}
	
	
	public boolean is_player_Spamming(Player player) {
		return !allowed(player);
	}
	
	public void player_Clicked(Player player) {
		interact(player);
	}
	
}