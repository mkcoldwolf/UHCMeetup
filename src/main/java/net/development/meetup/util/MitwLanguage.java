package net.development.meetup.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class MitwLanguage implements Listener {

    public static enum LangType {
        CONFIG, CLASS
    }

    ;

    private static final String DEFAULT_LANGUAGE = "zh_tw";
    private static Map<UUID, String> playerLangs = new HashMap<>();

    private Map<String, List<String>> savedMessages = new HashMap<>();
    private Plugin plugin;
    private LangType type;
    private YamlConfiguration config;
    private Object clazz;
    private SQLConnection conn;

    public MitwLanguage(LangType type, Plugin plugin, SQLConnection conn, YamlConfiguration config) {
        this.type = type;
        this.config = config;
        this.plugin = plugin;
        this.conn = conn;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public MitwLanguage(LangType type, Plugin plugin, SQLConnection conn, Object clazz) {
        this.type = type;
        this.clazz = clazz;
        this.conn = conn;
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public MitwLanguage(LangType type, Plugin plugin, SQLConnection conn, Class<?> clazz, Class<?>... methods) {
        this.type = type;
        this.conn = conn;
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        try {
            this.clazz = clazz.getConstructor(methods).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MitwLanguage(LangType type, Plugin plugin, SQLConnection conn, YamlConfiguration config, Class<?> clazz, Class<?>... methods) {
        this.type = type;
        this.config = config;
        this.conn = conn;
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        try {
            this.clazz = clazz.getConstructor(methods).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LangType getType() {
        return type;
    }

    public void setType(LangType type) {
        this.type = type;
    }

    public YamlConfiguration getYamlConfiguration() {
        return config;
    }

    public void setYamlConfiguration(YamlConfiguration config) {
        this.config = config;
    }

    public Object getObject() {
        return clazz;
    }

    public void setObject(Object clazz) {
        this.clazz = clazz;
    }

    public void setClass(Class<?> clazz, Class<?>... methods) {
        try {
            this.clazz = clazz.getConstructor(methods).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getSavedMessages() {
        return savedMessages;
    }

    public String getLang(Player p) {
        if (playerLangs.containsKey(p.getUniqueId())) {
            return playerLangs.get(p.getUniqueId());
        }
        try {
            PreparedStatement ps = conn.getConnection().prepareStatement("SELECT * FROM `MitwLang` WHERE `uuid` = ?;");
            ps.setString(1, p.getUniqueId().toString());
            ps.executeQuery();
            ResultSet result = ps.getResultSet();
            String lang = null;
            if (result.isBeforeFirst()) {
                while (result.next()) {
                    lang = result.getString("lang");
                }
            }
            return lang;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasLang(Player p) {
        if (playerLangs.containsKey(p.getUniqueId())) {
            return true;
        }
        return hasLangSQL(p);
    }

    public boolean hasLangSQL(Player p) {
        try {
            PreparedStatement ps = conn.getConnection().prepareStatement("SELECT * FROM `MitwLang` WHERE `uuid` = ?;");
            ps.setString(1, p.getUniqueId().toString());
            ps.executeQuery();
            ResultSet result = ps.getResultSet();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setLang(Player p, boolean sql) {
        if (playerLangs.containsKey(p.getUniqueId())) {
            setLang(p, playerLangs.get(p.getUniqueId()), sql);
        } else {
            setLang(p, DEFAULT_LANGUAGE, sql);
        }
    }

    public void setLang(Player p, String string, boolean sql) {
        playerLangs.put(p.getUniqueId(), string);
        if (!sql) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                if (hasLangSQL(p)) {
                    try {
                        PreparedStatement ps = conn.getConnection().prepareStatement("UPDATE `MitwLang` SET `lang` = ? WHERE `uuid` = ?;");
                        ps.setString(1, playerLangs.get(p.getUniqueId()));
                        ps.setString(2, p.getUniqueId().toString());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PreparedStatement ps = conn.getConnection().prepareStatement("INSERT INTO `MitwLang` (uuid, lang) VALUES (?, ?)");
                        ps.setString(1, p.getUniqueId().toString());
                        ps.setString(2, playerLangs.get(p.getUniqueId()));
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public String translate(Player p, String ofrom) {
        String lang = getLang(p);
        String from = lang + "." + ofrom;
        if (savedMessages.containsKey(from)) {
            return savedMessages.get(from).get(0);
        } else {
            String to = null;
            boolean found = false;
            switch (type) {
                case CLASS:
                    try {
                        Field field = clazz.getClass().getDeclaredField(from.replace(".", "_"));
                        Object object = field.get(clazz);
                        if (object == null) {
                            field = clazz.getClass().getDeclaredField((DEFAULT_LANGUAGE + "_" + ofrom).replace(".", "_"));
                            field.setAccessible(true);
                            object = field.get(clazz);
                        } else {
                            found = true;
                        }
                        to = (String) object;
                    } catch (Exception e) {
                        Bukkit.getConsoleSender().sendMessage("§cCant get string field " + from.replace(".", "_") + " from " + clazz.getClass().getName() + " from player " + p.getName() + " !");
                        return "null";
                    }
                    break;
                case CONFIG:
                    String notsure = config.getString(from);
                    if (notsure == null) {
                        notsure = config.getString(DEFAULT_LANGUAGE + "." + ofrom);
                    } else {
                        found = true;
                    }
                    to = notsure;
                    break;
            }
            to = ChatColor.translateAlternateColorCodes('&', to);
            if (found) {
                savedMessages.put(from, Arrays.asList(to));
            }
            return to;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> translateArrays(Player p, String ofrom) {
        String lang = getLang(p);
        String from = lang + "." + ofrom;
        if (savedMessages.containsKey(from)) {
            return savedMessages.get(from);
        } else {
            List<String> to = null;
            boolean found = false;
            switch (type) {
                case CLASS:
                    try {
                        Field field = clazz.getClass().getDeclaredField(from.replace(".", "_"));
                        Object object = field.get(clazz);
                        if (object == null) {
                            field = clazz.getClass().getDeclaredField((DEFAULT_LANGUAGE + "_" + ofrom).replace(".", "_"));
                            field.setAccessible(true);
                            object = field.get(clazz);
                        } else {
                            found = true;
                        }
                        to = (List<String>) object;
                    } catch (Exception e) {
                        Bukkit.getConsoleSender().sendMessage("§cCant get string field " + from.replace(".", "_") + " from " + clazz.getClass().getName() + " from player " + p.getName() + " !");
                        return Arrays.asList("null");
                    }
                    break;
                case CONFIG:
                    List<String> notsure = config.getStringList(from);
                    if (notsure == null) {
                        notsure = config.getStringList(DEFAULT_LANGUAGE + "." + ofrom);
                    } else {
                        found = true;
                    }
                    to = notsure;
                    break;
            }
            for (int i = 0; i < to.size(); i++) {
                to.set(i, ChatColor.translateAlternateColorCodes('&', to.get(i)));
            }
            if (found) {
                savedMessages.put(from, to);
            }
            return to;
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if (hasLang(p)) {
            playerLangs.put(p.getUniqueId(), getLang(p));
        } else {
            setLang(p, DEFAULT_LANGUAGE, true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        setLang(p, true);
    }

    public static class SQLConnection {

        private final String ip;
        private final int port;
        private final String name;
        private final String password;
        private final String database;
        private Connection connection;

        public SQLConnection(String ip, int port, String name, String password, String database) {
            this.ip = ip;
            this.port = port;
            this.name = name;
            this.password = password;
            this.database = database;
        }

        public boolean connect() {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database, this.name,
                        this.password);

                Bukkit.getConsoleSender().sendMessage("§aMitw Language API SQL Connectted");
                createTable();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        private void createTable() {
            query("CREATE TABLE IF NOT EXISTS `MitwLang` (`uuid` VARCHAR(60), `lang` VARCHAR(60))");
        }

        public boolean isConnect() {
            return !(connection == null);
        }

        public Connection getConnection() {
            return connection;
        }

        public void query(String q) {
            query(q, true);
        }

        private void query(String q, boolean first) {
            if (isConnect()) {
                PreparedStatement ps = null;
                try {
                    ps = connection.prepareStatement(q);
                    ps.execute();
                } catch (SQLException e) {
                    if (first) {
                        query(q, false);
                    } else {
                        e.printStackTrace();
                    }
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public ResultSet getResult(String q) {
            return getResult(q, true);
        }

        private ResultSet getResult(String q, boolean info) {
            if (isConnect()) {
                Statement ps = null;
                try {
                    ps = connection.createStatement();
                    ResultSet rs = ps.executeQuery(q);
                    return rs;
                } catch (SQLException e) {
                    if (info) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

}
