package mitw.meetup.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardManager extends BukkitRunnable {

	private final JavaPlugin plugin;
	@Getter
	private final Map<UUID, Board> playerBoards = new HashMap<>();
	private final BoardAdapter adapter;

	@Override
	public void run() {
		this.adapter.preLoop();

		for (final Player player : plugin.getServer().getOnlinePlayers()) {
			final Board board = this.playerBoards.get(player.getUniqueId());

			if (board == null) {
				continue;
			}

			try {
				final Scoreboard scoreboard = board.getScoreboard();

				final List<String> scores = this.adapter.getScoreboard(player, board);

				if (scores != null) {
					Collections.reverse(scores);

					final Objective objective = board.getObjective();

					if (!objective.getDisplayName().equals(this.adapter.getTitle(player))) {
						objective.setDisplayName(this.adapter.getTitle(player));
					}

					if (scores.isEmpty()) {
						final Iterator<BoardEntry> iter = board.getEntries().iterator();
						while (iter.hasNext()) {
							final BoardEntry boardEntry = iter.next();
							boardEntry.remove();
							iter.remove();
						}
						continue;
					}

					forILoop:
						for (int i = 0; i < scores.size(); i++) {
							final String text = scores.get(i);
							final int position = i + 1;

							for (final BoardEntry boardEntry : new LinkedList<>(board.getEntries())) {
								final Score score = objective.getScore(boardEntry.getKey());

								if (score != null && boardEntry.getText().equals(text)) {
									if (score.getScore() == position) {
										continue forILoop;
									}
								}
							}

							Iterator<BoardEntry> iter = board.getEntries().iterator();
							while (iter.hasNext()) {
								final BoardEntry boardEntry = iter.next();
								final int entryPosition = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(boardEntry
										.getKey()).getScore();
								if (entryPosition > scores.size()) {
									boardEntry.remove();
									iter.remove();
								}
							}

							final int positionToSearch = position - 1;

							final BoardEntry entry = board.getByPosition(positionToSearch);
							if (entry == null) {
								new BoardEntry(board, text).send(position);
							} else {
								entry.setText(text).setup().send(position);
							}

							if (board.getEntries().size() > scores.size()) {
								iter = board.getEntries().iterator();
								while (iter.hasNext()) {
									final BoardEntry boardEntry = iter.next();
									if (!scores.contains(boardEntry.getText()) || Collections.frequency(board
											.getBoardEntriesFormatted(), boardEntry.getText()) > 1) {
										boardEntry.remove();
										iter.remove();
									}
								}
							}
						}
				} else {
					if (!board.getEntries().isEmpty()) {
						board.getEntries().forEach(BoardEntry::remove);
						board.getEntries().clear();
					}
				}

				this.adapter.onScoreboardCreate(player, scoreboard);

				player.setScoreboard(scoreboard);
			} catch (final Exception e) {
				e.printStackTrace();

				plugin.getLogger().severe("Something went wrong while updating " + player.getName() + "'s scoreboard " +
						"" + board + " - " + board.getAdapter() + ")");
			}
		}
	}

}
