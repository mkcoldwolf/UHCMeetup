package mitw.meetup.util;

import mitw.meetup.UHCMeetup;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class SoundUtil {

    public static void PlayerSound(Player p, Sound sound, float level, float level2) {
        p.playSound(p.getLocation(), sound, level, level2);
    }

    public static void PlayerSound(Player p, Sound sound, float level) {
        PlayerSound(p, sound, level, 1f);
    }

    public static void PlayerSound(Player p, Sound sound) {
        PlayerSound(p, sound, 1F, 1F);
    }

    public static void PlaySoundAll(Sound sound, float level, float level2) {
        UHCMeetup.getInstance().getOnlinePlayers().forEach(u -> {
            Player p = Bukkit.getPlayer(u);
            if (p != null)
                PlayerSound(p, sound, level, level2);
        });
    }

    public static void PlaySoundAll(Sound sound) {
        UHCMeetup.getInstance().getOnlinePlayers().forEach(u -> {
            Player p = Bukkit.getPlayer(u);
            if (p != null)
                PlayerSound(p, sound, 1f, 1f);
        });
    }

}
