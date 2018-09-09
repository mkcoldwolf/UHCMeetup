package net.development.meetup.board.create;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.development.meetup.Lang;
import net.development.meetup.Main;
import net.development.meetup.UHCPlayer;
import net.development.meetup.board.ScoreHelper;
import net.development.meetup.enums.Status;
import net.development.meetup.task.GameTask;
import net.development.meetup.task.PVPBorderTask;
import net.development.meetup.util.Tools;

public class CreateSoloBoard extends BukkitRunnable{
	
	@Override
	public void run() {
		if(!Status.isState(Status.PVP)) {
			cancel();
			return;
		}for(Player p : Bukkit.getOnlinePlayers()) {
			updateScore(p);
		}
	}
	
	public void updateScore(Player p) {
		if(ScoreHelper.hasScore(p)) {
			ScoreHelper helper = ScoreHelper.getByPlayer(p);
			List<String> setboardList = new ArrayList<>();
			for(String s : Main.get().getLang().translateArrays(p, "SoloList")) {
				if(s.equals("<noclean>")) {
					if(Main.getGM().getData.get(p.getUniqueId()).isNoClean()) {
						setboardList.add(placeholder(p, Main.get().getLang().translate(p, "noClean")));
						continue;
					}else continue;
				}
				setboardList.add(placeholder(p, s));
			}
			helper.setSlotsFromList(setboardList);
		}else {
			Bukkit.getScheduler().runTask(Main.get(), new Runnable() {
				@Override
				public void run() {
					ScoreHelper helper = ScoreHelper.createScore(p);
					helper.setTitle(Lang.Title);
				}
			});
		}
	}
	
	public static String placeholder(Player p, String s) {
		UHCPlayer up = Main.getGM().getData.get(p.getUniqueId());
		String untiShrink = "&7(&c"+Tools.UntiShrinkTime(PVPBorderTask.sec)+"&7)";
		if(PVPBorderTask.list.isEmpty())untiShrink = "";
		String withVars = s
				.replaceAll("<players>", "" + Main.getGM().players.size()).replaceAll("&", "§")
				.replaceAll("<border>", "" + Main.getGM().getBorder()+"")
				.replaceAll("<format>", untiShrink)
				.replaceAll("<time>", Tools.getTimeHora(GameTask.time))
				.replaceAll("<kills>", Main.getGM().getData.get(p.getUniqueId()).getKills()+"")
				.replaceAll("<max>", Main.getGM().max+"").replaceAll("<cleanTime>", new DecimalFormat("0.0").format(up.getNoCleanTimer()));
		return withVars;
	}
	
}
