 package com.Server.ninja.Server;

import java.util.Date;
import java.time.Instant;
import java.sql.ResultSet;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import com.Server.io.MySQL;
import java.util.ArrayList;

public class Top
{
    protected static final ArrayList<Entry>[] tops;
    private static final int[] arrSizeTop;
    private static final boolean[] isRefresh;
    
    public static void init() {
        for (int i = 0; i < Top.tops.length; ++i) {
            Top.tops[i] = new ArrayList<Entry>();
        }
        System.out.println("load BXH");
        for (int i = 0; i < Top.tops.length; ++i) {
            if (Top.isRefresh[i]) {
                initTop(i);
            }
        }
    }
    
    protected static void initTop(final int type) {
        Top.tops[type].clear();
        final ArrayList<Entry> top = Top.tops[type];
        switch (type) {
            case 0: {
                try {
                    final MySQL mySQL = new MySQL(1);
                    try {
                        int i = 1;
                        final ResultSet red = mySQL.stat.executeQuery("SELECT `cName`,`yen` FROM `player` WHERE (`yen` > 0) ORDER BY `yen` DESC LIMIT 10;");
                        while (red.next()) {
                            final Entry topE = new Entry();
                            topE.name = red.getString("cName");
                            topE.index = i;
                            topE.nTop = red.getInt("yen");
                            topE.desc = "";
                            top.add(topE);
                            ++i;
                        }
                        red.close();
                    }
                    finally {
                        mySQL.close();
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 1: {
                try {
                    Top.isRefresh[type] = false;
                    final byte[] ab = NinjaUtil.getFile("assets/text/top_" + type + ".txt");
                    if (ab != null && ab.length > 0) {
                        final String text = new String(ab, "UTF-8");
                        final String[] arrData = text.split("\n");
                        for (byte j = 0; j < arrData.length; ++j) {
                            final String[] res = arrData[j].split(",");
                            final Entry topE2 = new Entry();
                            topE2.name = res[0];
                            topE2.index = j + 1;
                            topE2.nTop = Integer.parseInt(res[1]);
                            topE2.desc = res[2];
                            top.add(topE2);
                        }
                    }
                }
                catch (UnsupportedEncodingException | NumberFormatException ex3) {
                    final Exception ex = null;
                    final Exception e2 = ex;
                    e2.printStackTrace();
                }
                break;
            }
            case 2: {
                try {
                    final MySQL mySQL = new MySQL(1);
                    try {
                        int i = 1;
                        final ResultSet red = mySQL.stat.executeQuery("SELECT `name`,`level`,`main_name`,`numMem`,`maxMem` FROM `clan` WHERE (`level` > 0) ORDER BY `level` DESC LIMIT 10;");
                        while (red.next()) {
                            final Entry topE = new Entry();
                            topE.name = red.getString("name");
                            topE.nTop = red.getInt("level");
                            topE.desc = red.getString("main_name");
                            topE.nTops = new int[] { red.getShort("numMem"), red.getShort("maxMem") };
                            topE.index = i;
                            top.add(topE);
                            ++i;
                        }
                        red.close();
                    }
                    finally {
                        mySQL.close();
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 3: {
                try {
                    Top.isRefresh[type] = false;
                    final byte[] ab = NinjaUtil.getFile("assets/text/top_" + type + ".txt");
                    if (ab != null && ab.length > 0) {
                        final String text = new String(ab, "UTF-8");
                        final String[] arrData = text.split("\n");
                        for (byte j = 0; j < arrData.length; ++j) {
                            final String[] res = arrData[j].split(",");
                            final Entry topE2 = new Entry();
                            topE2.name = res[0];
                            topE2.index = j + 1;
                            topE2.nTop = Integer.parseInt(res[1]);
                            topE2.desc = res[2];
                            top.add(topE2);
                        }
                    }
                }
                catch (UnsupportedEncodingException | NumberFormatException ex4) {
                    final Exception ex2 = null;
                    final Exception e2 = ex2;
                    e2.printStackTrace();
                }
                break;
            }
            case 4: {
                try {
                    final MySQL mySQL = new MySQL(1);
                    try {
                        int i = 1;
                        final ResultSet red = mySQL.stat.executeQuery("SELECT `cName`,`epoint` FROM `player` WHERE (`epoint` > 0) ORDER BY `epoint` DESC LIMIT 10;");
                        while (red.next()) {
                            final Entry topE = new Entry();
                            topE.name = red.getString("cName");
                            topE.index = i;
                            topE.nTop = red.getInt("epoint");
                            topE.desc = "";
                            top.add(topE);
                            ++i;
                        }
                        red.close();
                    }
                    finally {
                        mySQL.close();
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 6: {
                try {
                    final MySQL mySQL = new MySQL(1);
                    try {
                        int i = 1;
                        final ResultSet red = mySQL.stat.executeQuery("SELECT `cName`,`pointTalent` FROM `player` WHERE (`pointTalent` > 0) ORDER BY `pointTalent` DESC LIMIT 10;");
                        while (red.next()) {
                            final Entry topE = new Entry();
                            topE.name = red.getString("cName");
                            topE.index = i;
                            topE.nTop = red.getInt("pointTalent");
                            topE.desc = "";
                            top.add(topE);
                            ++i;
                        }
                        red.close();
                    }
                    finally {
                        mySQL.close();
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    
    protected static void saveTopFile(final byte type) {
        final ArrayList<Entry> top = Top.tops[type];
        if (top.size() > 0) {
            String data = top.get(0).name + "," + top.get(0).nTop + "," + top.get(0).desc;
            for (byte i = 1; i < top.size(); ++i) {
                data = data + "\n" + top.get(i).name + "," + top.get(i).nTop + "," + top.get(i).desc;
            }
            NinjaUtil.saveFile("assets/text/top_" + type + ".txt", data.getBytes());
        }
    }
    
    protected static synchronized void sortTop(final int type, final String name, final String desc, final int nTop, final int[] nTops) {
        final ArrayList<Entry> top = Top.tops[type];
        boolean isAdd = false;
        for (byte i = 0; i < top.size(); ++i) {
            final Entry topE = top.get(i);
            if (topE != null) {
                if (topE.name.equals(name)) {
                    if (topE.nTop < nTop) {
                        topE.nTop = nTop;
                        topE.nTops = nTops;
                        if (type == 1) {
                            topE.desc = Util.toDateString(Date.from(Instant.now()));
                        }
                        else {
                            topE.desc = desc;
                        }
                    }
                    isAdd = false;
                    break;
                }
                if ((i == top.size() - 1 && top.size() < Top.arrSizeTop[type]) || topE.nTop < nTop) {
                    isAdd = true;
                }
            }
        }
        if (isAdd || top.isEmpty()) {
            final Entry topE2 = new Entry();
            topE2.name = name;
            topE2.index = top.size() + 1;
            topE2.nTop = nTop;
            topE2.nTops = nTops;
            if (type == 1) {
                topE2.desc = Util.toDateString(Date.from(Instant.now()));
            }
            else {
                topE2.desc = desc;
            }
            top.add(topE2);
        }
        for (byte i = 0; i < top.size(); ++i) {
            final Entry topE = top.get(i);
            if (topE != null) {
                for (byte j = (byte)(i + 1); j < top.size(); ++j) {
                    final Entry topE3 = top.get(j);
                    if (topE3 != null && topE.nTop < topE3.nTop) {
                        top.set(j, topE);
                        topE.index = j + 1;
                        top.set(i, topE3);
                        topE3.index = i + 1;
                    }
                }
            }
        }
        while (top.size() > Top.arrSizeTop[type]) {
            top.remove(top.size() - 1);
        }
    }
    
    protected static String getStringBXH(final Char _Char, final int type) {
        String str = "";
        switch (type) {
            case 0: {
                if (Top.tops[type].isEmpty()) {
                    str = Text.get(0, 56);
                    break;
                }
                for (final Entry top : Top.tops[type]) {
                    str = str + String.format(Text.get(0, 156), top.index, top.name, Util.getFormatNumber(top.nTop)) + "\n";
                }
                break;
            }
            case 1: {
                if (Top.tops[type].isEmpty()) {
                    str = Text.get(0, 56);
                    break;
                }
                for (final Entry top : Top.tops[type]) {
                    str = str + String.format(Text.get(0, 157), top.index, top.name, top.nTop, top.desc) + "\n";
                }
                break;
            }
            case 2: {
                if (Top.tops[type].isEmpty()) {
                    str = Text.get(0, 56);
                    break;
                }
                for (final Entry top : Top.tops[type]) {
                    str = str + String.format(Text.get(0, 224), top.index, top.name, top.nTop, top.desc, top.nTops[0], top.nTops[1]) + "\n";
                }
                break;
            }
            case 3: {
                if (Top.tops[type].isEmpty()) {
                    str = Text.get(0, 56);
                    break;
                }
                for (final Entry top : Top.tops[type]) {
                    str = str + String.format(Text.get(0, 270), top.index, top.name, top.nTop, top.desc) + "\n";
                }
                break;
            }
            case 4: {
                str = str + "Điểm : " + _Char.user.player.epoint + "\n";
                if (Top.tops[type].isEmpty()) {
                    str = Text.get(0, 56);
                    break;
                }
                for (final Entry top : Top.tops[type]) {
                    str = str + String.format(Text.get(0, 186), top.index, top.name) + "\n";
                }
                break;
            }
            case 5: {
                if (Top.tops[type].isEmpty()) {
                    str = Text.get(0, 56);
                    break;
                }
                for (final Entry top : Top.tops[type]) {
                    str = str + String.format(Text.get(0, 337), top.index, top.name, top.nTop, (top.nTops[0] == 0) ? Text.get(0, 339) : ((top.nTops[0] == 1) ? Text.get(0, 338) : Text.get(0, 340)), top.desc) + "\n";
                }
                break;
            }
            case 6: {
                if (Top.tops[type].isEmpty()) {
                    str = Text.get(0, 56);
                    break;
                }
                str = str + "Điểm tài năng : " + _Char.pointTalent + "\n";
                for (final Entry top : Top.tops[type]) {
                    str = str + String.format(Text.get(0, 397), top.index, top.name, Util.getFormatNumber(top.nTop)) + "\n";
                }
                break;
            }
        }
        return str;
    }
    
    protected static int getTop(final String cName, final int type) {
        for (int i = 0; i < Top.tops[type].size(); ++i) {
            final Entry top = Top.tops[type].get(i);
            if (top.name.equals(cName)) {
                return top.index;
            }
        }
        return 0;
    }
    
    static {
        tops = new ArrayList[7];
        arrSizeTop = new int[] { 10, 10, 10, 10, 10, 10,10 };
        isRefresh = new boolean[] { true, true, true, true, true, true,true};
    }
    
    public static class Entry
    {
        public int index;
        public int id;
        public String name;
        public int nTop;
        public int[] nTops;
        public String desc;
    }
}
