package mitw.meetup.player;

import mitw.meetup.UHCMeetup;
import net.development.mitw.Mitw;
import net.development.mitw.utils.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class Rank {

	private static final List<Rank> ranks = new ArrayList<>();

	private final String name;
	private final int min;
	private final int max;
	private final Map<String, String> displayName = new HashMap<>();

	public Rank(final ConfigurationSection section) {
		this.name = section.getName();
		this.min = section.getInt("min");
		if (section.get("max") instanceof String) {
			this.max = Integer.MAX_VALUE;
		} else {
			this.max = section.getInt("max");
		}
		final ConfigurationSection section2 = section.getConfigurationSection("displayname");
		for (final String key : section2.getKeys(false)) {
			this.displayName.put(key, StringUtil.cc(section2.getString(key)));
		}
		ranks.add(this);
	}

	public boolean isInRange(final int elo) {
		return elo > min && elo < max;
	}

	public String getDisplayName(final Player player) {
		return displayName.get(Mitw.getInstance().getLanguageData().getLang(player));
	}

	public String getIcon() {
		return displayName.get("icon");
	}

	public static Rank getRank(final Player player) {
		return getRank(player.getUniqueId());
	}

	public static Rank getRank(final UUID uuid) {
		return getRank(UHCMeetup.getInstance().getGameManager().getProfile(uuid));
	}

	public static Rank getRank(final PlayerProfile playerProfile) {
		for (final Rank rank : ranks) {
			if (rank.isInRange(playerProfile.getElo()))
				return rank;
		}
		return null;
	}

	public static Rank getRank(final int rating) {
		for (final Rank rank : ranks) {
			if (rank.isInRange(rating))
				return rank;
		}
		return null;
	}

}
