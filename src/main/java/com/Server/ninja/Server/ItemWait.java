 package com.Server.ninja.Server;

import java.sql.ResultSet;
import org.json.simple.parser.ParseException;
import java.sql.SQLException;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import com.Server.io.MySQL;
import java.util.ArrayList;

public class ItemWait
{
    protected int take_playerId;
    protected String take_cName;
    protected byte type;
    protected Item item;
    protected int coinLock;
    protected int coin;
    protected int gold;
    protected long exp;
    protected static ArrayList<ItemWait> arrWait;
    protected static final Object LOCK;
    protected static final int SettimeBackup = 60000;
    protected static int timeBackup;
    
    protected ItemWait(final int take_playerId, final String take_cName, final byte type) {
        this.item = null;
        this.coinLock = 0;
        this.coin = 0;
        this.gold = 0;
        this.exp = 0L;
        this.take_playerId = take_playerId;
        this.take_cName = take_cName;
        this.type = type;
    }
    
    protected ItemWait() {
        this.item = null;
        this.coinLock = 0;
        this.coin = 0;
        this.gold = 0;
        this.exp = 0L;
    }
    
    protected static void addItem(final int take_playerId, final String take_cName, final Item item) {
        synchronized (ItemWait.LOCK) {
            final ItemWait wait = new ItemWait(take_playerId, take_cName, (byte)0);
            wait.item = item;
            ItemWait.arrWait.add(wait);
        }
    }
    
    protected static void addCoinLock(final int take_playerId, final String take_cName, final int coinLock) {
        synchronized (ItemWait.LOCK) {
            final ItemWait wait = new ItemWait(take_playerId, take_cName, (byte)1);
            wait.coinLock = coinLock;
            ItemWait.arrWait.add(wait);
        }
    }
    
    protected static void addCoin(final int take_playerId, final String take_cName, final int coin) {
        synchronized (ItemWait.LOCK) {
            final ItemWait wait = new ItemWait(take_playerId, take_cName, (byte)2);
            wait.coin = coin;
            ItemWait.arrWait.add(wait);
        }
    }
    
    protected static void addGold(final int take_playerId, final String take_cName, final int gold) {
        synchronized (ItemWait.LOCK) {
            final ItemWait wait = new ItemWait(take_playerId, take_cName, (byte)3);
            wait.gold = gold;
            ItemWait.arrWait.add(wait);
        }
    }
    
    protected static void addExp(final int take_playerId, final String take_cName, final long xp) {
        synchronized (ItemWait.LOCK) {
            final ItemWait wait = new ItemWait(take_playerId, take_cName, (byte)4);
            wait.exp = xp;
            ItemWait.arrWait.add(wait);
        }
    }
    
    protected static void Init() {
        try {
            synchronized (ItemWait.LOCK) {
                final MySQL mySQL = new MySQL(1);
                ResultSet res;
                try {
                    System.out.println("Load Item Wait");
                    res = mySQL.stat.executeQuery("SELECT * FROM `itemlist`;");
                    while (res.next()) {
                        final byte typeUI = res.getByte("type");
                        final String list = res.getString("list");
                        if (typeUI == 0) {
                            final JSONArray jarrlsit = (JSONArray)JSONValue.parseWithException(list);
                            for (int i = 0; i < jarrlsit.size(); ++i) {
                                final JSONArray jarr = (JSONArray)jarrlsit.get(i);
                                final ItemWait wait = new ItemWait();
                                wait.take_playerId = Integer.parseInt(jarr.get(0).toString());
                                wait.take_cName = jarr.get(1).toString();
                                wait.type = Byte.parseByte(jarr.get(2).toString());
                                final String value = jarr.get(3).toString();
                                if (wait.type == 0) {
                                    wait.item = Item.parseItem(value);
                                }
                                else if (wait.type == 1) {
                                    wait.coinLock = Integer.parseInt(value);
                                }
                                else if (wait.type == 2) {
                                    wait.coin = Integer.parseInt(value);
                                }
                                else if (wait.type == 3) {
                                    wait.gold = Integer.parseInt(value);
                                }
                                else if (wait.type == 4) {
                                    wait.exp = Long.parseLong(value);
                                }
                                ItemWait.arrWait.add(wait);
                            }
                        }
                    }
                }
                finally {
                    mySQL.close();
                }
                res.close();
            }
        }
        catch (SQLException | ParseException | NumberFormatException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    protected static void flush() {
        System.out.println("Flush cache ItemWait");
        try {
            final JSONArray jarrlist = new JSONArray();
            for (int i = 0; i < ItemWait.arrWait.size(); ++i) {
                final ItemWait wait = ItemWait.arrWait.get(i);
                if (wait != null) {
                    final JSONArray list = new JSONArray();
                    list.add((Object)wait.take_playerId);
                    list.add((Object)wait.take_cName);
                    list.add((Object)wait.type);
                    if (wait.type == 0) {
                        list.add(JSONValue.parseWithException(wait.item.toString()));
                    }
                    else if (wait.type == 1) {
                        list.add((Object)wait.coinLock);
                    }
                    else if (wait.type == 2) {
                        list.add((Object)wait.coin);
                    }
                    else if (wait.type == 3) {
                        list.add((Object)wait.gold);
                    }
                    else if (wait.type == 4) {
                        list.add((Object)wait.exp);
                    }
                    jarrlist.add((Object)list);
                }
            }
            final String sql = "`list`='" + jarrlist.toJSONString() + "'";
            final MySQL mySQL = new MySQL(1);
            try {
                mySQL.stat.executeUpdate("UPDATE `itemlist` SET " + sql + " WHERE `type`=" + 0 + " LIMIT 1;");
            }
            finally {
                mySQL.close();
            }
        }
        catch (ParseException | SQLException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    static {
        ItemWait.arrWait = new ArrayList<ItemWait>();
        LOCK = new Object();
        ItemWait.timeBackup = 60000;
    }
}
