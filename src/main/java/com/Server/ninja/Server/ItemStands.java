 package com.Server.ninja.Server;

import java.sql.SQLException;
import org.json.simple.parser.ParseException;
import java.sql.ResultSet;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import com.Server.io.MySQL;
import java.util.ArrayList;

public class ItemStands
{
    protected int itemId;
    protected Item item;
    protected int price;
    protected int timeEnd;
    protected int timeStart;
    protected String seller;
    protected static int baseId;
    protected static ArrayList<ItemStands>[] arrItemStands;
    protected static int fireCoin;
    protected static final Object LOCK;
    protected static final int SettimeBackup = 60000;
    protected static int timeBackup;
    protected static int minCoinSale;
    protected static int maxCoinSale;
    protected static int tollCoinPercent;
    
    protected ItemStands(final Item item, final String seller, final int price) {
        this.item = item;
        this.seller = seller;
        this.price = price;
    }
    
    protected ItemStands() {
    }
    
    protected static int getSendItemMax(final int level) {
        return 0;
    }
    
    protected static void sendItem(final Item item, final String cName, final int coinSale) {
        synchronized (ItemStands.LOCK) {
            byte type;
            if (item.isTypeCrystal()) {
                type = 0;
            }
            else if (item.template.type < 10) {
                type = (byte)(item.template.type + 1);
            }
            else {
                type = 11;
            }
            item.typeUI = 37;
            final ItemStands itemStands = new ItemStands(item, cName, coinSale);
            itemStands.itemId = ItemStands.baseId++;
            itemStands.timeStart = (int)(System.currentTimeMillis() / 1000L);
            itemStands.timeEnd = 86400;
            ItemStands.arrItemStands[type].add(itemStands);
        }
    }
    
    protected static int numbSale(final String cName, final int type) {
        synchronized (ItemStands.LOCK) {
            int num = 0;
            if (type == -1) {
                for (byte k = 0; k < ItemStands.arrItemStands.length; ++k) {
                    for (int i = 0; i < ItemStands.arrItemStands[k].size(); ++i) {
                        final ItemStands itemStands = ItemStands.arrItemStands[k].get(i);
                        if (itemStands.seller.equals(cName)) {
                            ++num;
                        }
                    }
                }
            }
            return num;
        }
    }
    
    protected static void Init() {
        synchronized (ItemStands.LOCK) {
            for (byte i = 0; i < ItemStands.arrItemStands.length; ++i) {
                ItemStands.arrItemStands[i] = new ArrayList<ItemStands>();
            }
            try {
                final MySQL mySQL = new MySQL(1);
                try {
                    System.out.println("Load Item Stands");
                    final ResultSet res = mySQL.stat.executeQuery("SELECT * FROM `itemlist`;");
                    while (res.next()) {
                        final byte typeUI = (byte)(res.getByte("type") - 1);
                        final String list = res.getString("list");
                        if (typeUI >= 0 && typeUI < 12) {
                            final JSONArray jarrlsit = (JSONArray)JSONValue.parseWithException(list);
                            for (int j = 0; j < jarrlsit.size(); ++j) {
                                final JSONArray jarr = (JSONArray)jarrlsit.get(j);
                                final ItemStands itemStands = new ItemStands();
                                itemStands.itemId = ItemStands.baseId++;
                                itemStands.seller = jarr.get(0).toString();
                                itemStands.price = Integer.parseInt(jarr.get(1).toString());
                                itemStands.timeStart = Integer.parseInt(jarr.get(2).toString());
                                itemStands.timeEnd = Integer.parseInt(jarr.get(3).toString());
                                itemStands.item = Item.parseItem(jarr.get(4).toString());
                                ItemStands.arrItemStands[typeUI].add(itemStands);
                            }
                        }
                    }
                    res.close();
                }
                finally {
                    mySQL.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
    
    protected static void flush() {
        System.out.println("Flush cache ItemStands");
        try {
            for (byte k = 0; k < ItemStands.arrItemStands.length; ++k) {
                final JSONArray jarrlist = new JSONArray();
                for (int i = 0; i < ItemStands.arrItemStands[k].size(); ++i) {
                    final ItemStands itemStands = ItemStands.arrItemStands[k].get(i);
                    if (itemStands != null) {
                        final JSONArray list = new JSONArray();
                        list.add((Object)itemStands.seller);
                        list.add((Object)itemStands.price);
                        list.add((Object)itemStands.timeStart);
                        list.add((Object)itemStands.timeEnd);
                        list.add(JSONValue.parseWithException(itemStands.item.toString()));
                        jarrlist.add((Object)list);
                    }
                }
                final String sql = "`list`='" + jarrlist.toJSONString() + "'";
                final MySQL mySQL = new MySQL(1);
                try {
                    mySQL.stat.executeUpdate("UPDATE `itemlist` SET " + sql + " WHERE `type`=" + (k + 1) + " LIMIT 1;");
                }
                finally {
                    mySQL.close();
                }
            }
        }
        catch (ParseException | SQLException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    static {
        ItemStands.baseId = 0;
        ItemStands.arrItemStands = (ArrayList<ItemStands>[])new ArrayList[12];
        ItemStands.fireCoin = 5000;
        LOCK = new Object();
        ItemStands.timeBackup = 60000;
        ItemStands.minCoinSale = 1;
        ItemStands.maxCoinSale = 999999999;
        ItemStands.tollCoinPercent = 10;
    }
}
