package mitw.meetup.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import mitw.meetup.UHCMeetup;
import org.bukkit.entity.Player;

import mitw.meetup.player.PlayerProfile;
import net.development.mitw.utils.timer.PlayerTimer;

public class NocleanTimer extends PlayerTimer {

	public NocleanTimer() {
		super("Noclean", TimeUnit.SECONDS.toMillis(20L), true);
	}

	@Override
	protected void handleExpiry(final Player player, final UUID playerUUID) {

		super.handleExpiry(player, playerUUID);

		if (player == null || !player.isOnline()) {
			return;
		}

		final PlayerProfile profile = UHCMeetup.getInstance().getGameManager().getProfile(playerUUID);

		profile.setNoClean(false);

		if (player == null)
			return;

		player.sendMessage(UHCMeetup.getInstance().getLanguage().translate(player, "noCleanStop"));


	}

}
