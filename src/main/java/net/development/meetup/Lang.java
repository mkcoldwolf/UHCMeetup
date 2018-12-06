package net.development.meetup;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {
	static {
		setupFile();
	}

	public static File data;
	public static YamlConfiguration dataConfig;


	private static void setupFile() {
		data = completeCreateFiles(data, "playerdata.yml");
		dataConfig = YamlConfiguration.loadConfiguration(data);
	}

	public static File completeCreateFiles(File file, final String name) {
		file = new File(Main.get().getDataFolder(), name);
		if (!file.exists()) {
			try {
				file.createNewFile();
				Main.get().saveResource(name, true);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	public static String headName = "§6GoldenHead §7(§6金頭顱§7)";
	public static String Title = "&6&lMitw &7| &fMeetup";
	public static String PREFIX = "§7[§6§lMitw§f§lMeetup§7] ";
	public static String LOADING = "&c地圖正在加載中... &7- &cMap is loading...";
	public static String spec1 = "§6§l隨機傳送 §7(Random TP)";
	public static String spec2 = "§b§l回到大廳 §7(Back to Hub)";
	public static String vote = "§a§lScenarios投票 §7(Vote)";
	public static String reTurnToPractice = "§6§l回到Practice分流 §7(Back to Practice)";

	public List<String> zh_tw_JOIN = Arrays.asList(
			"&7&m------------------"
			, " "
			, "&f歡迎來到 &6&lMitw&f&lMeetup &7[1.0]"
			, "&a新版本測試中!!"
			, "&e意見回饋: &7https://goo.gl/pYStiJ"
			, " "
			, "&7&m------------------");

	public String zh_tw_death = "&c{0}&8[&f{1}&8] &7死亡了!";
	public String zh_tw_death_by_player = "&c{0}&8[&f{1}&8] &7被 &c{2}&8[&f{3}&8] &7擊殺了!";
	public String zh_tw_CASTJOIN = " &6<player> &f加入了 §6Mitw§eMeetup &7(<current>/&c<max>&7)";
	public String zh_tw_CASTQUIT = " &c<player> &f退出了 §6Mitw§eMeetup &7(<current>/&c<max>&7)";
	public String zh_tw_NOPERM;
	public String zh_tw_NOBREAK = "§c你不能破壞這個方塊喔!";
	public String zh_tw_INGAME = PREFIX + "&c遊戲已經開始! 請稍等遊戲結束!";
	public String zh_tw_SCATTER_STARTING = PREFIX + "&f投票將在&e <time> &f秒後停止!";
	public String zh_tw_STARTING = PREFIX + "&f遊戲將在&e <time> &f秒後開始!";
	public String zh_tw_Shirnk = PREFIX + "&f邊界收縮到了 &e&l<border>x<border>&f !";
	public String zh_tw_InShirnk = PREFIX + "&f邊界將在 <time> &f後收縮至&e&l <border>x<border> &f!";
	public String zh_tw_scatterFinish = PREFIX + "§e所有玩家都被傳送了!";
	public String zh_tw_noCleanStart = "§a[無敵時間] 你獲得了20秒的無敵時間!";
	public String zh_tw_noCleanStop = "§e[無敵時間] 你的無敵時間失效了!";
	public String zh_tw_airDropCountDown = "§7[§6Air§fDrop§7]§f 空投將在 §e<time>秒 §f後墜落於 §6x: <x> y: <y> z: <z> §f!";
	public String zh_tw_airDrop = "§7[§6Air§fDrop§7]§f 空投墜落在了 §6x: <x> y: <y> z: <z> §f!";
	public String zh_tw_bowLess = "§c§lBowLess 是開啟的! 你不能使用弓!";
	public String zh_tw_noClean = "&f無敵時間: &6<cleanTime>秒";
	public String zh_tw_choose = "&6&l你選擇了 &e&l中文 &6&l!";
	public String zh_tw_cleandb_on = "&7- &6防止撿頭模式 &a&l開啟";
	public String zh_tw_cleandb_off = "&7- &6防止撿頭模式 &c&l關閉";
	public String zh_tw_win = "&e0 &b贏了這場遊戲!";
	public String zh_tw_win_team = "&b隊伍 &e0 &b贏了這場遊戲!";
	public String zh_tw_team_members = "&f隊伍成員:&b 0";
	public String zh_tw_bow = "&c<player> 有 <health>";
	public String zh_tw_s1 = "秒";
	public String zh_tw_s2 = "秒";
	public String zh_tw_m1 = "分鐘";
	public String zh_tw_m2 = "分鐘";
	public String zh_tw_spec1 = "§6§l隨機傳送";
	public String zh_tw_spec2 = "§b§l回到大廳";
	public String zh_tw_vote = "§a§lScenarios投票";
	public String zh_tw_reTurnToPractice = "§6§l回到戰鬥練習";
	public String zh_tw_teamchoose = "&6隊伍&f選擇";
	public String zh_tw_dbmodeon = "&fCleanup紀錄模式 &f- &aON";
	public String zh_tw_dbmodeoff = "&fCleanup紀錄模式 &f- &cOFF";
	public String zh_tw_wait = "&c冷卻中...";

	public String zh_tw_arrowDamage = "&c{0} &7現在有 &c{1} &e{2}";
	public String en_us_arrowDamage = "&c{0} &7now has &c{1} &e{2}";

	public List<String> zh_tw_LobbyList = Arrays.asList(
			"&7&m---------------------------",
			"&f人數: &6<players>",
			"&f伺服器: &e<server>",
			"&f投票將在 &6<starting>&f 中止!",
			" ",
			"&fTimebomb:&6 <s1>票",
			"&fNoClean:&6 <s2>票",
			"&fBowLess:&6 <s3>票",
			"&fRodLess:&6 <s4>票",
			"&fFireLess:&6 <s5>票",
			"&fAirDrops:&6 <s6>票",
			"&fIronKing:&6 <s7>票",
			" ",
			"&6&lMitw.Rip",
			"&7&m---------------------------");
	public List<String> zh_tw_SoloList = Arrays.asList(
			"&7&m---------------------------",
			"&f時間:&6 <time>",
			"&f人數:&6 <players>/<max>",
			"&f擊殺數:&6 <kills>",
			"&f邊界:&6 <border> <format>",
			"<noclean>",
			" ",
			"&6&lMitw.Rip",
			"&7&m---------------------------");
	public List<String> zh_tw_TeamList = Arrays.asList(
			"&7&m---------------------------",
			"&f時間:&6 <time>",
			"&f人數:&6 <players>/<max>",
			"&f擊殺數:&6 <kills>",
			"&f隊伍擊殺數:&6 <teamKills>",
			"&f邊界:&6 <border> <format>",
			"<noclean>",
			" ",
			"&6&lMitw.Rip",
			"&7&m---------------------------");
	public List<String> zh_tw_TPList = Arrays.asList(
			"&7&m---------------------------",
			"&f人數: &6<players>",
			"&f遊戲將在 &6<starting>&f 開始!",
			" ",
			"&6&lMitw.Rip",
			"&7&m---------------------------");

	public List<String> zh_tw_Rules = Arrays.asList(
			"&7&m---------------------------",
			"&f&l在5個人以下clean,&c&lBan除 2 小時",
			"&f&l組隊,&c&lBan除3天",
			"&7&m---------------------------");
	public List<String> zh_tw_ppl5BroadCast = Arrays.asList(
			"&c&m--------------------------------",
			"&c&l遊戲現在只剩下5位玩家",
			"&c&l開始禁止Clean up!",
			"&c&l否則被檢舉,將Ban除2個小時",
			"&c&m--------------------------------");
	public List<String> zh_tw_ppl5BroadCast_team = Arrays.asList(
			"&c&m--------------------------------",
			"&c&l遊戲現在只剩下三個隊伍!",
			"&c&l開始禁止Clean up!",
			"&c&l否則被檢舉,將Ban除2個小時",
			"&c&m--------------------------------");

	/**
	 * ENGLISH
	 */

	public List<String> en_us_JOIN = Arrays.asList(
			"&7&m------------------"
			, " "
			, "&fWelcome to &6&lMitw&f&lMeetup"
			, "&aHope you can have a great time."
			, " "
			, "&7&m------------------");

	public String en_us_CASTJOIN = " &6<player> &fJoined §6Mitw§eMeetup &7(<current>/&c<max>&7)";
	public String en_us_CASTQUIT = " &c<player> &fLeft §6Mitw§eMeetup &7(<current>/&c<max>&7)";
	public String en_us_NOPERM;
	public String en_us_NOBREAK = "§cYou are not allowed to break this block!!";
	public String en_us_INGAME = PREFIX + "&cgame has started already! please wait for next game!!";
	public String en_us_SCATTER_STARTING = PREFIX + "&fvote will end in &e <time> &fsecond(s) !";
	public String en_us_STARTING = PREFIX + "&fthe game will start in &e <time> &fsecond(s)!";
	public String en_us_Shirnk = PREFIX + "&fborder has been reduced to &e&l<border>x<border>&f !";
	public String en_us_InShirnk = PREFIX + "&fwill be reduced at &e<time> &fto&e&l <border>x<border> &f!";
	public String en_us_scatterFinish = PREFIX + "§escatter finished!";
	public String en_us_noCleanStart = "&a[No Clean] You have a 20 second invincibility timer!";
	public String en_us_noCleanStop = "&c[No Clean] You no longer have invincibility!";
	public String en_us_airDropCountDown = "§7[§6Air§fDrop§7]§f the airdrop will be dropping after §e<time> seconds §fto §6x: <x> y: <y> z: <z> §f!";
	public String en_us_airDrop = "§7[§6Air§fDrop§7]§f the airdrop has been dropped to §6x: <x> y: <y> z: <z> §f!";
	public String en_us_bowLess = "§c§lBowLess is on! you're not allowed to use the bow!";
	public String en_us_noClean = "&fNoclean: &6<cleanTime>s";
	public String en_us_choose = "&6&lYou chose &e&lEnglish &6&l!";
	public String en_us_bow = "&c<player> has <health>";
	public String en_us_death = "&c{0}&8[&f{1}&8] &7died!";
	public String en_us_death_by_player = "&c{0}&8[&f{1}&8] &7was slain by &c{2}&8[&f{3}&8] &7!";
	public String en_us_cleandb_on = "&7- &6Cleanup debug mode &a&lON";
	public String en_us_cleandb_off = "&7- &6Cleanup debug mode &c&lOFF";
	public String en_us_win = "&e0 &bwin the game!";
	public String en_us_win_team = "&bTeam &e0 &bwin the game!";
	public String en_us_team_members = "&fTeam members:&b 0";
	public String en_us_s1 = "second";
	public String en_us_s2 = "seconds";
	public String en_us_m1 = "minute";
	public String en_us_m2 = "minutes";
	public String en_us_spec1 = "§6§lRandom Teleport";
	public String en_us_spec2 = "§b§lBack to hub";
	public String en_us_vote = "§a§lScenarios vote";
	public String en_us_reTurnToPractice = "§6§lBack to practice";
	public String en_us_teamchoose = "&6Choose &fTeams";
	public String en_us_dbmodeon = "&fCleanup debug mode &f- &aON";
	public String en_us_dbmodeoff = "&fCleanup debug mode &f- &cOFF";
	public String en_us_wait = "&cCooldown...";

	public List<String> en_us_LobbyList = Arrays.asList(
			"&7&m---------------------------",
			"&fPlayers: &6<players>",
			"&fServer: &e<server>",
			"&fVote ends in &6<starting>&f !",
			" ",
			"&fTimebomb:&6 <s1>",
			"&fNoClean:&6 <s2>",
			"&fBowLess:&6 <s3>",
			"&fRodLess:&6 <s4>",
			"&fFireLess:&6 <s5>",
			"&fAirDrops:&6 <s6>",
			"&fIronKing:&6 <s7>",
			" ",
			"&6&lMitw.Rip",
			"&7&m---------------------------");
	public List<String> en_us_SoloList = Arrays.asList(
			"&7&m---------------------------",
			"&fTime:&6 <time>",
			"&fRemaining:&6 <players>/<max>",
			"&fKills:&6 <kills>",
			"&fBorder:&6 <border> <format>",
			"<noclean>",
			" ",
			"&6&lMitw.Rip",
			"&7&m---------------------------");
	public List<String> en_us_TeamList = Arrays.asList(
			"&7&m---------------------------",
			"&fTime:&6 <time>",
			"&fRemaining:&6 <players>/<max>",
			"&fKills:&6 <kills>",
			"&fTeam kills:&6 <teamKills>",
			"&fBorder:&6 <border> <format>",
			"<noclean>",
			" ",
			"&6&lMitw.Rip",
			"&7&m---------------------------");
	public List<String> en_us_TPList = Arrays.asList(
			"&7&m---------------------------",
			"&fPlayers: &6<players>",
			"&fMatch starting in &6<starting>&f !",
			" ",
			"&6&lMitw.Rip",
			"&7&m---------------------------");

	public List<String> en_us_Rules = Arrays.asList(
			"&7&m---------------------------",
			"&f&lCleaning when there are 5 or less players remaining -> &c&lBan 2hr",
			"&f&lTeaming in meetup ->&c&l Ban 3days",
			"&7&m---------------------------");
	public List<String> en_us_ppl5BroadCast = Arrays.asList(
			"&c&m--------------------------------",
			"&c&lYou can NOT clean now!",
			"&c&lYOU WILL BE BANNED!",
			"&c&m--------------------------------");
	public List<String> en_us_ppl5BroadCast_team = Arrays.asList(
			"&c&m--------------------------------",
			"&c&lYou can NOT clean now!",
			"&c&lYOU WILL BE BANNED!",
			"&c&m--------------------------------");
}
