package net.development.meetup.impl;

import java.util.UUID;

import org.bukkit.entity.Player;

import net.development.meetup.Main;
import net.development.meetup.player.UHCPlayer;
import net.development.mitw.utils.timer.PlayerTimer;

public class NocleanTimer extends PlayerTimer {

	public NocleanTimer() {
		super("Noclean", 400L, true);
	}

	@Override
	protected void handleExpiry(final Player player, final UUID playerUUID) {

		final UHCPlayer profile = Main.getGM().getData.get(playerUUID);

		profile.setNoClean(false);

		if (player == null)
			return;

		player.sendMessage(Main.get().getLang().translate(player, "noCleanStop"));


	}

}
