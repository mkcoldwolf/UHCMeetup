package net.development.meetup.border;

import net.development.meetup.Main;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;


public class Config {
    // private stuff used within this class
    public static Main plugin;
    public static volatile DecimalFormat coord = new DecimalFormat("0.0");
    private static int borderTask = -1;
    public static volatile WorldFillTask fillTask = null;
    private static Runtime rt = Runtime.getRuntime();

    // actual configuration values which can be changed
    private static boolean shapeRound = false;
    private static Map<String, BorderData> borders = Collections.synchronizedMap(new LinkedHashMap<String, BorderData>());
    private static Set<UUID> bypassPlayers = Collections.synchronizedSet(new LinkedHashSet<UUID>());
    private static String message;        // raw message without color code formatting
    private static String messageFmt = "&c";    // message with color code formatting ("&" changed to funky sort-of-double-dollar-sign for legitimate color/formatting codes)
    private static String messageClean;    // message cleaned of formatting codes
    private static boolean DEBUG = false;
    private static double knockBack = 3;
    private static int timerTicks = 7;
    private static boolean whooshEffect = false;
    private static boolean portalRedirection = true;
    private static int remountDelayTicks = 0;
    private static boolean killPlayer = false;
    private static boolean denyEnderpearl = false;
    private static int fillAutosaveFrequency = 30;
    private static int fillMemoryTolerance = 500;
    private static boolean preventBlockPlace = false;
    private static boolean preventMobSpawn = false;

    // for monitoring plugin efficiency
//	public static long timeUsed = 0;

    public static long Now() {
        return System.currentTimeMillis();
    }


    public static void setBorder(String world, BorderData border, boolean logIt) {
        borders.put(world, border);
        if (logIt)
            log("Border set. " + BorderDescription(world));
        save(true);
    }

    public static void setBorder(String world, BorderData border) {
        setBorder(world, border, true);
    }

    public static void setBorder(String world, int radiusX, int radiusZ, double x, double z, Boolean shapeRound) {
        BorderData old = Border(world);
        boolean oldWrap = (old != null) && old.getWrapping();
        setBorder(world, new BorderData(x, z, radiusX, radiusZ, shapeRound, oldWrap), true);
    }

    public static void setBorder(String world, int radiusX, int radiusZ, double x, double z) {
        BorderData old = Border(world);
        Boolean oldShape = (old == null) ? null : old.getShape();
        boolean oldWrap = (old != null) && old.getWrapping();
        setBorder(world, new BorderData(x, z, radiusX, radiusZ, oldShape, oldWrap), true);
    }


    // backwards-compatible methods from before elliptical/rectangular shapes were supported
    public static void setBorder(String world, int radius, double x, double z, Boolean shapeRound) {
        setBorder(world, new BorderData(x, z, radius, radius, shapeRound), true);
    }

    public static void setBorder(String world, int radius, double x, double z) {
        setBorder(world, radius, radius, x, z);
    }


    // set border based on corner coordinates
    public static void setBorderCorners(String world, double x1, double z1, double x2, double z2, Boolean shapeRound, boolean wrap) {
        double radiusX = Math.abs(x1 - x2) / 2;
        double radiusZ = Math.abs(z1 - z2) / 2;
        double x = ((x1 < x2) ? x1 : x2) + radiusX;
        double z = ((z1 < z2) ? z1 : z2) + radiusZ;
        setBorder(world, new BorderData(x, z, (int) Math.round(radiusX), (int) Math.round(radiusZ), shapeRound, wrap), true);
    }

    public static void setBorderCorners(String world, double x1, double z1, double x2, double z2, Boolean shapeRound) {
        setBorderCorners(world, x1, z1, x2, z2, shapeRound, false);
    }

    public static void setBorderCorners(String world, double x1, double z1, double x2, double z2) {
        BorderData old = Border(world);
        Boolean oldShape = (old == null) ? null : old.getShape();
        boolean oldWrap = (old != null) && old.getWrapping();
        setBorderCorners(world, x1, z1, x2, z2, oldShape, oldWrap);
    }


    public static void removeBorder(String world) {
        borders.remove(world);
        log("Removed border for world \"" + world + "\".");
        save(true);
    }

    public static void removeAllBorders() {
        borders.clear();
        log("Removed all borders for all worlds.");
        save(true);
    }

    public static String BorderDescription(String world) {
        BorderData border = borders.get(world);
        if (border == null)
            return "No border was found for the world \"" + world + "\".";
        else
            return "World \"" + world + "\" has border " + border.toString();
    }

    public static Set<String> BorderDescriptions() {
        Set<String> output = new HashSet<String>();

        for (String worldName : borders.keySet()) {
            output.add(BorderDescription(worldName));
        }

        return output;
    }

    public static BorderData Border(String world) {
        return borders.get(world);
    }

    public static Map<String, BorderData> getBorders() {
        return new LinkedHashMap<String, BorderData>(borders);
    }

    public static void setMessage(String msg) {
        updateMessage(msg);
        save(true);
    }

    public static void updateMessage(String msg) {
        message = msg;
        messageFmt = replaceAmpColors(msg);
        messageClean = stripAmpColors(msg);
    }

    public static String Message() {
        return ChatColor.translateAlternateColorCodes('&', messageFmt);
    }

    public static String MessageRaw() {
        return message;
    }

    public static String MessageClean() {
        return messageClean;
    }

    public static void setShape(boolean round) {
        shapeRound = round;
        log("Set default border shape to " + (ShapeName()) + ".");
        save(true);
    }

    public static boolean ShapeRound() {
        return shapeRound;
    }

    public static String ShapeName() {
        return ShapeName(shapeRound);
    }

    public static String ShapeName(Boolean round) {
        if (round == null)
            return "default";
        return round ? "elliptic/round" : "rectangular/square";
    }

    public static void setDebug(boolean debugMode) {
        DEBUG = debugMode;
        log("Debug mode " + (DEBUG ? "enabled" : "disabled") + ".");
        save(true);
    }

    public static boolean Debug() {
        return DEBUG;
    }

    public static void setWhooshEffect(boolean enable) {
        whooshEffect = enable;
        log("\"Whoosh\" knockback effect " + (enable ? "enabled" : "disabled") + ".");
        save(true);
    }

    public static boolean whooshEffect() {
        return whooshEffect;
    }

    public static void showWhooshEffect(Location loc) {
        if (!whooshEffect())
            return;

        World world = loc.getWorld();
        world.playEffect(loc, Effect.ENDER_SIGNAL, 0);
        world.playEffect(loc, Effect.ENDER_SIGNAL, 0);
        world.playEffect(loc, Effect.SMOKE, 4);
        world.playEffect(loc, Effect.SMOKE, 4);
        world.playEffect(loc, Effect.SMOKE, 4);
        world.playEffect(loc, Effect.GHAST_SHOOT, 0);
    }

    public static boolean preventBlockPlace() {
        return preventBlockPlace;
    }

    public static boolean preventMobSpawn() {
        return preventMobSpawn;
    }

    public static boolean getIfPlayerKill() {
        return killPlayer;
    }

    public static boolean getDenyEnderpearl() {
        return denyEnderpearl;
    }

    public static void setDenyEnderpearl(boolean enable) {
        denyEnderpearl = enable;
        log("Direct cancellation of ender pearls thrown past the border " + (enable ? "enabled" : "disabled") + ".");
        save(true);
    }

    public static void setPortalRedirection(boolean enable) {
        portalRedirection = enable;
        log("Portal redirection " + (enable ? "enabled" : "disabled") + ".");
        save(true);
    }

    public static boolean portalRedirection() {
        return portalRedirection;
    }

    public static void setKnockBack(double numBlocks) {
        knockBack = numBlocks;
        log("Knockback set to " + knockBack + " blocks inside the border.");
        save(true);
    }

    public static double KnockBack() {
        return knockBack;
    }

    public static void setTimerTicks(int ticks) {
        timerTicks = ticks;
        log("Timer delay set to " + timerTicks + " tick(s). That is roughly " + (timerTicks * 50) + "ms / " + (((double) timerTicks * 50.0) / 1000.0) + " seconds.");
        StartBorderTimer();
        save(true);
    }

    public static int TimerTicks() {
        return timerTicks;
    }

    public static void setRemountTicks(int ticks) {
        remountDelayTicks = ticks;
        if (remountDelayTicks == 0)
            log("Remount delay set to 0. Players will be left dismounted when knocked back from the border while on a vehicle.");
        else {
            log("Remount delay set to " + remountDelayTicks + " tick(s). That is roughly " + (remountDelayTicks * 50) + "ms / " + (((double) remountDelayTicks * 50.0) / 1000.0) + " seconds.");
            if (ticks < 10)
                logWarn("setting the remount delay to less than 10 (and greater than 0) is not recommended. This can lead to nasty client glitches.");
        }
        save(true);
    }

    public static int RemountTicks() {
        return remountDelayTicks;
    }

    public static void setFillAutosaveFrequency(int seconds) {
        fillAutosaveFrequency = seconds;
        if (fillAutosaveFrequency == 0)
            log("World autosave frequency during Fill process set to 0, disabling it. Note that much progress can be lost this way if there is a bug or crash in the world generation process from Bukkit or any world generation plugin you use.");
        else
            log("World autosave frequency during Fill process set to " + fillAutosaveFrequency + " seconds (rounded to a multiple of 5). New chunks generated by the Fill process will be forcibly saved to disk this often to prevent loss of progress due to bugs or crashes in the world generation process.");
        save(true);
    }

    public static int FillAutosaveFrequency() {
        return fillAutosaveFrequency;
    }


    public static void setDynmapBorderEnabled(boolean enable) {
        log("DynMap border display is now " + (enable ? "enabled" : "disabled") + ".");
        save(true);
    }

    public static void setDynmapMessage(String msg) {
        log("DynMap border label is now set to: " + msg);
        save(true);
    }

    public static void setPlayerBypass(UUID player, boolean bypass) {
        if (bypass)
            bypassPlayers.add(player);
        else
            bypassPlayers.remove(player);
        save(true);
    }

    public static boolean isPlayerBypassing(UUID player) {
        return bypassPlayers.contains(player);
    }

    public static ArrayList<UUID> getPlayerBypassList() {
        return new ArrayList<UUID>(bypassPlayers);
    }

    public static boolean isBorderTimerRunning() {
        if (borderTask == -1) return false;
        return (plugin.getServer().getScheduler().isQueued(borderTask) || plugin.getServer().getScheduler().isCurrentlyRunning(borderTask));
    }

    public static void StartBorderTimer() {
        StopBorderTimer(false);

        borderTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new BorderCheckTask(), timerTicks, timerTicks);

        if (borderTask == -1)
            logWarn("Failed to start timed border-checking task! This will prevent the plugin from working. Try restarting Bukkit.");

        logConfig("Border-checking timed task started.");
    }

    public static void StopBorderTimer() {
        StopBorderTimer(true);
    }

    public static void StopBorderTimer(boolean logIt) {
        if (borderTask == -1) return;

        plugin.getServer().getScheduler().cancelTask(borderTask);
        borderTask = -1;
        if (logIt)
            logConfig("Border-checking timed task stopped.");
    }

    public static void StopFillTask() {
        if (fillTask != null && fillTask.valid())
            fillTask.cancel();
    }

    public static void StoreFillTask() {
    }

    public static void UnStoreFillTask() {
        save(false);
    }

    public static void RestoreFillTask(String world, int fillDistance, int chunksPerRun, int tickFrequency, int x, int z, int length, int total, boolean forceLoad) {
        fillTask = new WorldFillTask(plugin.getServer(), null, world, fillDistance, chunksPerRun, tickFrequency, forceLoad);
        if (fillTask.valid()) {
            fillTask.continueProgress(x, z, length, total);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, fillTask, 20, tickFrequency);
            fillTask.setTaskID(task);
        }
    }

    // for backwards compatibility
    public static void RestoreFillTask(String world, int fillDistance, int chunksPerRun, int tickFrequency, int x, int z, int length, int total) {
        RestoreFillTask(world, fillDistance, chunksPerRun, tickFrequency, x, z, length, total, false);
    }

    public static int AvailableMemory() {
        return (int) ((rt.maxMemory() - rt.totalMemory() + rt.freeMemory()) / 1048576);  // 1024*1024 = 1048576 (bytes in 1 MB)
    }

    public static boolean AvailableMemoryTooLow() {
        return AvailableMemory() < fillMemoryTolerance;
    }


    public static boolean HasPermission(Player player, String request) {
        return HasPermission(player, request, true);
    }

    public static boolean HasPermission(Player player, String request, boolean notify) {
        if (player == null)                // console, always permitted
            return true;

        if (player.hasPermission("worldborder." + request))    // built-in Bukkit superperms
            return true;

        if (notify)
            player.sendMessage("You do not have sufficient permissions.");

        return false;
    }


    public static String replaceAmpColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // adapted from code posted by Sleaker
    public static String stripAmpColors(String message) {
        return message.replaceAll("(?i)&([a-fk-or0-9])", "");
    }


    public static void log(Level lvl, String text) {
        Main.Log(text);
    }

    public static void log(String text) {
        log(Level.INFO, text);
    }

    public static void logWarn(String text) {
        log(Level.WARNING, text);
    }

    public static void logConfig(String text) {
        log(Level.INFO, "[CONFIG] " + text);
    }

    public static void save(boolean logIt) {
    }
}
