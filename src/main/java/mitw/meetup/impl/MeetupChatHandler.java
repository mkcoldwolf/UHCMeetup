package mitw.meetup.impl;

import mitw.meetup.UHCMeetup;
import mitw.meetup.player.PlayerProfile;
import mitw.meetup.player.Rank;
import net.development.mitw.chat.ChatHandler;
import org.bukkit.entity.Player;

public class MeetupChatHandler implements ChatHandler {

    @Override
    public String getPrefix(Player player) {
        PlayerProfile playerProfile = UHCMeetup.getInstance().getGameManager().getProfile(player.getUniqueId());
        return "&7[" + Rank.getRank(playerProfile.getElo()).getIcon() + playerProfile.getElo() + "&7]";
    }

    @Override
    public String getSuffix(Player player) {
        return "";
    }
}
