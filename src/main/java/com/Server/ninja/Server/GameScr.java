 package com.Server.ninja.Server;

import com.Server.PartMount.PartLeg;
import com.Server.PartMount.PartMountNew;
import com.Server.PartMount.PartNormal;
import java.sql.SQLException;
import com.Server.io.MySQL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import com.Server.ninja.template.ItemTemplate;
import com.Server.ninja.template.SkillTemplate;
import java.util.HashMap;

import com.Server.ninja.template.ItemOptionTemplate;
import com.Server.ninja.template.SkillOptionTemplate;
import com.Server.ninja.template.MapTemplate;
import com.Server.ninja.template.TaskTemplate;

public class GameScr
{
    protected static int post;
    private static String host;
    private static String mysql_host;
    private static String mysql_database;
    private static String mysql_user;
    private static String mysql_pass;
    protected static byte vData;
    protected static byte vMap;
    protected static byte vSkill;
    protected static byte vItem;
    protected static byte vEvent;
    protected static byte vEvent_1;
    protected static byte max_CreateChar;
    protected static int max_upLevel;
    protected static byte max_Friend;
    protected static byte max_Enemies;
    protected static byte qua_top;
    protected static int up_exp;
    public static byte[][] tasks;
    public static byte[][] mapTasks;
    protected static NClass[] nClasss;
    protected static Skill[] skills;
    protected static long[] exps;
    protected static int[] crystals;
    protected static int[] upClothe;
    protected static int[] upAdorn;
    public static int[] upWeapon;
    public static int[] coinUpCrystals;
    public static int[] coinUpClothes;
    public static int[] coinUpAdorns;
    public static int[] coinUpWeapons;
    public static int[] maxPercents;
    public static int[] goldUps;
    public static int[] coinGotngoc;
    protected static TaskTemplate[] taskTemplates;
    public static MapTemplate[] mapTemplates;
    public static SkillOptionTemplate[] sOptionTemplates;
    public static ItemOptionTemplate[] iOptionTemplates;
    protected static HashMap<Byte, Item[]> itemStores;
    protected static SkillTemplate[] skillTemplates;
    protected static ItemTemplate[] itemTemplates;
    protected static int[] arrModIdTaThu30 = new int[]{30, 33, 35, 37};
    protected static int[] arrModIdTaThu40 = new int[]{40, 43, 45, 47, 49};
    protected static int[] arrModIdTaThu50 = new int[]{51, 53, 57, 59};
    protected static int[] arrModIdTaThu60 = new int[]{61, 65, 67, 63};
    protected static int[] arrModIdTaThu70 = new int[]{129, 132, 135};
    protected static int[] arrModIdTaThu70_2 = new int[]{71, 74, 77};
    public static int[] arrModIdTaThu80 = new int[]{130,137};
    protected static int[] arrModIdTaThu80_2 = new int[]{80,88};
    protected static int[] arrModIdTaThu100 = new int[]{133};
    public static PartNormal[] head_Jump;
    public static PartNormal[] head_Normal;
    public static PartNormal[] head_Boc_Dau;
    public static PartNormal[] body_Jump;
    public static PartNormal[] body_Normal;
    public static PartNormal[] body_Boc_Dau;
    public static PartLeg[] Legs;
    public static PartMountNew[] mount_New;
    
    private static void loadConfigFile() {
        final byte[] ab = NinjaUtil.getFile("ninja.conf");
        if (ab == null) {
            System.out.println("Config file not found!");
            System.exit(0);
        }
        final String data = new String(ab);
        final HashMap<String, String> configMap = new HashMap<String, String>();
        final StringBuilder sbd = new StringBuilder();
        boolean bo = false;
        for (int i = 0; i <= data.length(); ++i) {
            final char es;
            if (i == data.length() || (es = data.charAt(i)) == '\n') {
                bo = false;
                final String sbf = sbd.toString().trim();
                if (sbf != null && !sbf.equals("") && sbf.charAt(0) != '#') {
                    final int j = sbf.indexOf(58);
                    if (j > 0) {
                        final String key = sbf.substring(0, j).trim();
                        final String value = sbf.substring(j + 1).trim();
                        configMap.put(key, value);
                        System.out.println("config: " + key + "-" + value);
                    }
                }
                sbd.setLength(0);
            }
            else {
                if (es == '#') {
                    bo = true;
                }
                if (!bo) {
                    sbd.append(es);
                }
            }
        }
        if (configMap.containsKey("debug")) {
            Util.setDebug(Boolean.parseBoolean(configMap.get("debug")));
        }
        else {
            Util.setDebug(false);
        }
        if (configMap.containsKey("host")) {
            GameScr.host = configMap.get("host");
        }
        else {
            GameScr.host = "localhost";
        }
        if (configMap.containsKey("post")) {
            GameScr.post = Short.parseShort(configMap.get("post"));
        }
        else {
            GameScr.post = 14444;
        }
        if (configMap.containsKey("mysql-host")) {
            GameScr.mysql_host = configMap.get("mysql-host");
        }
        else {
            GameScr.mysql_host = "localhost";
        }
        if (configMap.containsKey("mysql-user")) {
            GameScr.mysql_user = configMap.get("mysql-user");
        }
        else {
            GameScr.mysql_user = "root";
        }
        if (configMap.containsKey("mysql-password")) {
            GameScr.mysql_pass = configMap.get("mysql-password");
        }
        else {
            GameScr.mysql_pass = "";
        }
        if (configMap.containsKey("mysql-database")) {
            GameScr.mysql_database = configMap.get("mysql-database");
        }
        else {
            GameScr.mysql_database = "ninja";
        }
        if (configMap.containsKey("version-Data")) {
            GameScr.vData = Byte.parseByte(configMap.get("version-Data"));
            GameScr.vData += Util.nextInt(10);
        }
        else {
            GameScr.vData = 1;
        }
        if (configMap.containsKey("version-Map")) {
            GameScr.vMap = Byte.parseByte(configMap.get("version-Map"));
            GameScr.vMap += Util.nextInt(10);
        }
        else {
            GameScr.vMap = 1;
        }
        if (configMap.containsKey("version-Skill")) {
            GameScr.vSkill = Byte.parseByte(configMap.get("version-Skill"));
        }
        else {
            GameScr.vSkill = 1;
        }
        if (configMap.containsKey("version-Item")) {
            GameScr.vItem = Byte.parseByte(configMap.get("version-Item"));
            GameScr.vItem += Util.nextInt(10);
        }
        else {
            GameScr.vItem = 1;
        }
        if (configMap.containsKey("version-Event")) {
            GameScr.vEvent = Byte.parseByte(configMap.get("version-Event"));
        }
        else {
            GameScr.vEvent = 0;
        }
        if (configMap.containsKey("version-Event_1")) {
            GameScr.vEvent_1 = Byte.parseByte(configMap.get("version-Event_1"));
        }
        else {
            GameScr.vEvent_1 = 0;
        }
        if (configMap.containsKey("max-taoNhanVat")) {
            GameScr.max_CreateChar = Byte.parseByte(configMap.get("max-taoNhanVat"));
        }
        else {
            GameScr.max_CreateChar = 3;
        }
        if (configMap.containsKey("max-upLevel")) {
            GameScr.max_upLevel = Integer.parseInt(configMap.get("max-upLevel"));
        }
        else {
            GameScr.max_upLevel = 130;
        }
        if (configMap.containsKey("max-Friend")) {
            GameScr.max_Friend = Byte.parseByte(configMap.get("max-Friend"));
        }
        else {
            GameScr.max_Friend = 50;
        }
        if (configMap.containsKey("max-Enemies")) {
            GameScr.max_Enemies = Byte.parseByte(configMap.get("max-Enemies"));
        }
        else {
            GameScr.max_Enemies = 20;
        }
        if (configMap.containsKey("qua-top")) {
            GameScr.qua_top = Byte.parseByte(configMap.get("qua-top"));
        }
        else {
            GameScr.qua_top = 0;
        }
        if (configMap.containsKey("up-exp")) {
            GameScr.up_exp = Integer.parseInt(configMap.get("up-exp"));
        }
        else {
            GameScr.up_exp = 1;
        }
    }
    
    protected static void init() {
        loadConfigFile();
        try {
            MySQL.createConnection(0, GameScr.mysql_host, "ninjaschool_data", GameScr.mysql_user, GameScr.mysql_pass);
            MySQL.createConnection(1, GameScr.mysql_host, GameScr.mysql_database, GameScr.mysql_user, GameScr.mysql_pass);
            Init.init();
            final MySQL mySQL = new MySQL(1);
            mySQL.stat.executeUpdate("UPDATE `user` SET `online` = 0;");
            mySQL.stat.executeUpdate("UPDATE `player` SET `caveId` = -1;");
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    protected static int getMaxpPoint(final int level) {
        if (level < 70) {
            return 10;
        }
        if (level < 80) {
            return 20;
        }
        if (level < 90) {
            return 30;
        }
        return 50;
    }
    
    protected static long getMaxEXP(final int level) {
        long num = 0L;
        for (int i = 0; i < level; ++i) {
            num += GameScr.exps[i];
        }
        return num;
    }
    
    protected static long[] getLevelExp(final long exp) {
        long num;
        int i;
        for (num = exp, i = 0; i < GameScr.exps.length && num >= GameScr.exps[i]; num -= GameScr.exps[i], ++i) {}
        return new long[] { i, num };
    }
    
    public static void setLevel_Exp(final Char _char, final long exp, final boolean value) {
        final long[] levelExp = getLevelExp(exp);
        if (value) {
            _char.cLevel = (int)levelExp[0];
        }
        _char.cExpR = levelExp[1];
    }
    
    protected static synchronized void chatKTG(final Char _char, final String text) {
        try {
            if (_char.user.luong < 5) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 8));
                return;
            }
            if (_char.user.secondKTG > System.currentTimeMillis()) {
                GameCanvas.startOKDlg(_char.user.session, String.format(Text.get(0, 7), Util.getStrTime(_char.user.secondKTG - System.currentTimeMillis())));
                return;
            }
            _char.user.secondKTG = System.currentTimeMillis() + 5000L;
            _char.user.player.upGold(-5L, (byte)1);
            Client.chatServer(_char.cName, text);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected static byte getLevelSpeed(final int level) {
        if (level >= 40) {
            return 3;
        }
        if (level >= 25) {
            return 2;
        }
        if (level >= 10) {
            return 1;
        }
        return 0;
    }
    
    protected static short resetpPoint(final byte nClass, final byte index) {
        if (nClass == 2 || nClass == 4 || nClass == 6) {
            switch (index) {
                case 0: {
                    return 5;
                }
                case 1: {
                    return 5;
                }
                case 2: {
                    return 10;
                }
                case 3: {
                    return 10;
                }
            }
        }
        else {
            switch (index) {
                case 0: {
                    return 15;
                }
                case 1: {
                    return 5;
                }
                case 2: {
                    return 5;
                }
                case 3: {
                    return 5;
                }
            }
        }
        return 0;
    }
    
    protected static int EXPMobX(final short level) {
        if (level >= 100) {
            return 2;
        }
        if (level >= 90) {
            return 3;
        }
        if (level >= 80) {
            return 4;
        }
        if (level >= 70) {
            return 3;
        }
        if (level >= 40) {
            return 2;
        }
        return 1;
    }
    
    public static int getPort(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));
 
        StringBuilder response = new StringBuilder();
        String inputLine;
 
        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);
 
        in.close();
        int port = Integer.parseInt(response.toString());
        return port;
    }
    
    static {
        GameScr.coinGotngoc = new int[] { 0, 5000, 40000, 135000, 320000, 625000, 1080000, 1715000, 2560000, 3645000, 5000000 };
    }
}
