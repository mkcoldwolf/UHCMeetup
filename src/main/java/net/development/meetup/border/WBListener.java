package net.development.meetup.border;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.development.meetup.Main;


public class WBListener implements Listener
{
	
	public WBListener() {
		Config.plugin = Main.get();
		Config.StartBorderTimer();
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		// if knockback is set to 0, simply return
		if (Config.KnockBack() == 0.0)
			return;

		if (Config.Debug())
			Config.log("Teleport cause: " + event.getCause().toString());

		Location newLoc = BorderCheckTask.checkPlayer(event.getPlayer(), event.getTo(), true, true);
		if (newLoc != null)
		{
			if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && Config.getDenyEnderpearl())
			{
				event.setCancelled(true);
				return;
			}

			event.setTo(newLoc);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerPortal(PlayerPortalEvent event)
	{
		// if knockback is set to 0, or portal redirection is disabled, simply return
		if (Config.KnockBack() == 0.0 || !Config.portalRedirection())
			return;

		Location newLoc = BorderCheckTask.checkPlayer(event.getPlayer(), event.getTo(), true, false);
		if (newLoc != null)
			event.setTo(newLoc);
	}
}
