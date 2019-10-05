package mitw.meetup.board;

import org.bukkit.entity.Player;

import java.util.List;

public interface FrameAdapter {

	String getTitle(Player player);

	List<String> getLines(Player player);

}
