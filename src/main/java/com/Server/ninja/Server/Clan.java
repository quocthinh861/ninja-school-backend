 package com.Server.ninja.Server;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;

import java.util.Date;
import java.time.Instant;

import com.Server.io.MySQL;

import java.util.HashMap;
import java.util.ArrayList;

public class Clan {
    protected static final int CREATE_CLAN = 0;
    protected static final int MOVEMEM_CLAN = 1;
    protected static final int INPUTCOIN_CLAN = 2;
    protected static final int OUTPUTCOIN_CLAN = 3;
    protected static final int FIRECOIN_CLAN = 4;
    protected static final int UPLEVEL_CLAN = 5;
    protected static final int OPENITEM_CLAN = 6;
    protected static final int createGold = 200000;
    protected static final int createLevel = 40;
    protected static final ArrayList<Clan> Aclan;
    protected static final HashMap<String, Clan> clan_name;
    protected static final HashMap<Integer, Clan> clan_id;
    protected static final Object LOCK;
    protected static final int[] arrCoinOutClan;
    protected int clanId;
    protected String name;
    protected int exp;
    protected int level;
    protected int itemLevel;
    protected int coin;
    protected String main_name;
    protected String assist_name;
    protected String elder1_name;
    protected String elder2_name;
    protected String elder3_name;
    protected String elder4_name;
    protected String elder5_name;
    protected String reg_date;
    protected String log;
    protected String alert;
    protected int use_card;
    protected int openDun;
    protected final ArrayList<Member> members;
    protected short numMember;
    protected short maxMember;
    protected ArrayList<Item> items;
    protected byte debt;
    protected boolean isDissolution;
    protected long tolltimeWeek;

    protected static final int SettimeBackup = 60000;
    protected int timeBackup;
    protected final Object LOCK_CLAN;
    protected int warId;
    protected int delayWarClan;
    protected byte typeWar;
    protected String warClanname;
    protected WarClan warClan;
    protected final ArrayList<Clan_ThanThu> clan_thanThu;
    protected ClanManor clanManor;

    public Clan() {
        this.name = "";
        this.exp = 0;
        this.level = 1;
        this.itemLevel = 0;
        this.coin = 1000000;
        this.main_name = "";
        this.assist_name = "";
        this.elder1_name = "";
        this.elder2_name = "";
        this.elder3_name = "";
        this.elder4_name = "";
        this.elder5_name = "";
        this.reg_date = "";
        this.log = "";
        this.alert = "";
        this.use_card = 4;
        this.openDun = 3;
        this.members = new ArrayList<>();
        this.numMember = 0;
        this.maxMember = 50;
        this.items = new ArrayList<Item>();
        this.debt = 0;
        this.isDissolution = false;
        this.tolltimeWeek = -1L;
        this.timeBackup = 60000;
        this.typeWar = 0;
        this.clan_thanThu = new ArrayList<>();
        this.LOCK_CLAN = new Object();
    }

    protected static Clan get(final int clanId) {
        synchronized (Clan.LOCK) {
            return Clan.clan_id.get(clanId);
        }
    }

    protected static Clan get(final String clanName) {
        synchronized (Clan.LOCK) {
            return Clan.clan_name.get(clanName);
        }
    }

    protected static void add(final Clan clan) {
        synchronized (Clan.LOCK) {
            Clan.Aclan.add(clan);
            Clan.clan_name.put(clan.name, clan);
            Clan.clan_id.put(clan.clanId, clan);
        }
    }

    protected static int getMemMax(final int level) {
        return 45 + level * 5;
    }

    protected static int getexpNext(final int level) {
        int expNext = 2000;
        for (int i = 1; i < level; ++i) {
            if (i == 1) {
                expNext = 3720;
            } else if (i < 10) {
                expNext = (expNext / i + 310) * (i + 1);
            } else if (i < 20) {
                expNext = (expNext / i + 620) * (i + 1);
            } else {
                expNext = (expNext / i + 930) * (i + 1);
            }
        }
        return expNext;
    }

    protected static int getfreeCoin(final int numMember) {
        return 30000 + numMember * 5000;
    }

    public static int getCoinUp(final int level) {
        int coinUp = 500000;
        for (int i = 1; i < level; ++i) {
            if (i < 10) {
                coinUp += 100000;
            } else if (i < 20) {
                coinUp += 200000;
            } else {
                coinUp += 300000;
            }
        }
        return coinUp;
    }

    protected static byte itemLevel(final short itemTemplateId) {
        switch (itemTemplateId) {
            case 423: {
                return 1;
            }
            case 424: {
                return 2;
            }
            case 425: {
                return 3;
            }
            case 426: {
                return 4;
            }
            case 427: {
                return 5;
            }
            default: {
                return 0;
            }
        }
    }

    protected static byte useItemLevel(final short itemTemplateId) {
        switch (itemTemplateId) {
            case 436: {
                return 1;
            }
            case 437: {
                return 10;
            }
            case 438: {
                return 20;
            }
            default: {
                return 0;
            }
        }
    }

    protected static int useItemParam(final short itemTemplateId) {
        switch (itemTemplateId) {
            case 436: {
                return Util.nextInt(100, 300);
            }
            case 437: {
                return Util.nextInt(300, 800);
            }
            case 438: {
                return Util.nextInt(1000, 2000);
            }
            default: {
                return 0;
            }
        }
    }

    protected static void createClan(final Char _char, final short npcTemplateId, final String clanName) {
        if (!Util.CheckString(clanName, "^[a-zA-Z0-9]+$") || clanName.length() < 5 || clanName.length() > 12) {
            Service.openUISay(_char, npcTemplateId, Text.get(0, 191));
        } else {
            synchronized (Clan.LOCK) {
                if (Clan.clan_name.get(clanName) != null) {
                    Service.openUISay(_char, npcTemplateId, Text.get(0, 192));
                } else {
                    try {
                        final MySQL mySQL = new MySQL(1);
                        try {
                            _char.user.player.upGold(-30000L, (byte) 0);
                            final Clan clan = new Clan();
                            clan.name = clanName;
                            clan.main_name = _char.cName;
                            clan.reg_date = Util.toDateString(Date.from(Instant.now()));
                            clan.tolltimeWeek = System.currentTimeMillis();
                            _char.member.typeClan = 4;
                            clan.members.add(_char.member);
                            final Clan clan2 = clan;
                            ++clan2.numMember;
                            clan.writeLog("", 0, clan.coin, Util.toDateString(Date.from(Instant.now())));
                            final JSONArray members = new JSONArray();
                            for (short i = 0; i < clan.members.size(); ++i) {
                                members.add(JSONValue.parseWithException(clan.members.get(i).toString()));
                            }
                            mySQL.stat.executeUpdate("INSERT INTO clan(`name`,`main_name`,`reg_date`,`log`,`members`,`numMem`,`maxMem`,`items`,`tolltimeWeek`) VALUES ('" + Util.strSQL(clan.name) + "','" + Util.strSQL(clan.main_name) + "','" + Util.strSQL(clan.reg_date) + "','" + Util.strSQL(clan.log) + "','" + members.toJSONString() + "'," + clan.numMember + "," + clan.maxMember + ",'[]'," + clan.tolltimeWeek + ");");
                            final ResultSet red = mySQL.stat.executeQuery("SELECT `clanId` FROM `clan` WHERE `name`LIKE'" + Util.strSQL(clan.name) + "' LIMIT 1;");
                            red.first();
                            clan.clanId = red.getInt("clanId");
                            red.close();
                            _char.clan = clan;
                            Clan.Aclan.add(clan);
                            Clan.clan_name.put(clanName, clan);
                            Clan.clan_id.put(clan.clanId, clan);
                            _char.cClanName = clan.name;
                            _char.ctypeClan = _char.member.typeClan;
                            _char.user.flush();
                            Top.sortTop(2, clan.name, clan.main_name, clan.level, new int[]{clan.numMember, clan.maxMember});
                            Service.createClan(_char, clanName);
                            Service.openUISay(_char, npcTemplateId, Text.get(0, 193));
                            Player.updateInfoPlayer(_char);
                        } finally {
                            mySQL.close();
                        }
                    } catch (ParseException | SQLException ex2) {
                        final Exception ex = null;
                        final Exception e = ex;
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected static void Init() {
        int k = 0;
        try {
            synchronized (Clan.LOCK) {
                final MySQL mySQL = new MySQL(1);
                try {
                    final ResultSet res = mySQL.stat.executeQuery("SELECT * FROM `clan`;");
                    while (res.next()) {
                        final Clan clan = new Clan();
                        clan.clanId = res.getInt("clanId");
                        clan.name = res.getString("name");
                        clan.exp = res.getInt("exp");
                        clan.level = res.getInt("level");
                        clan.itemLevel = res.getByte("itemLevel");
                        clan.coin = res.getInt("coin");
                        clan.main_name = res.getString("main_name");
                        clan.assist_name = res.getString("assist_name");
                        clan.elder1_name = res.getString("elder1_name");
                        clan.elder2_name = res.getString("elder2_name");
                        clan.elder3_name = res.getString("elder3_name");
                        clan.elder4_name = res.getString("elder4_name");
                        clan.elder5_name = res.getString("elder5_name");
                        clan.reg_date = res.getString("reg_date");
                        clan.log = res.getString("log");
                        clan.alert = res.getString("alert");
                        clan.use_card = res.getByte("use_card");
                        clan.openDun = res.getByte("openDun");
                        final JSONArray jarrMem = (JSONArray) JSONValue.parse(res.getString("members"));
                        for (int i = 0; i < jarrMem.size(); ++i) {
                            final Member mem = Member.parseMember(jarrMem.get(i).toString());
                            if (mem != null) {
                                clan.members.add(mem);
                                final Clan clan2 = clan;
                                ++clan2.numMember;
                            }
                        }
                        clan.maxMember = res.getShort("maxMem");
                        final JSONArray jarrItem = (JSONArray) JSONValue.parse(res.getString("items"));
                        for (byte j = 0; j < jarrItem.size(); ++j) {
                            final Item item = Item.parseItem(jarrItem.get((int) j).toString());
                            if (item != null) {
                                clan.items.add(item);
                            }
                        }
                        final JSONArray jarrTT = (JSONArray) JSONValue.parse(res.getString("than_thu"));
                        for (int i = 0; i < jarrTT.size(); ++i) {
                            final Clan_ThanThu thanThu = Clan_ThanThu.parseThanThu(jarrTT.get(i).toString());
                            if (thanThu != null) {
                                clan.clan_thanThu.add(thanThu);
                            }
                        }
                        clan.debt = res.getByte("debt");
                        clan.isDissolution = res.getBoolean("isDissolution");
                        clan.tolltimeWeek = res.getLong("tolltimeWeek");
                        Clan.Aclan.add(clan);
                        Clan.clan_name.put(clan.name, clan);
                        Clan.clan_id.put(clan.clanId, clan);
                        ++k;
                    }
                } finally {
                    mySQL.close();
                }
            }
        } catch (Exception e) {
            System.err.println("loi clan voi index la " + k);
            e.printStackTrace();
            System.exit(0);
        }
    }

    protected static void closes() {
        synchronized (Clan.LOCK) {
            for (int i = 0; i < Clan.Aclan.size(); ++i) {
                final Clan clan = Clan.Aclan.get(i);
                if (clan != null) {
                    clan.close();
                }
            }
        }
    }

    protected void flush() {
        try {
            String sql = "`exp`=" + this.exp + ",`level`=" + this.level + ",`itemLevel`=" + this.itemLevel + ",`coin`=" + this.coin + ",`main_name`='" + Util.strSQL(this.main_name) + "',`assist_name`='" + Util.strSQL(this.assist_name) + "',`elder1_name`='" + Util.strSQL(this.elder1_name) + "',`elder2_name`='" + Util.strSQL(this.elder2_name) + "',`elder3_name`='" + Util.strSQL(this.elder3_name) + "',`elder4_name`='" + Util.strSQL(this.elder4_name) + "',`elder5_name`='" + Util.strSQL(this.elder5_name) + "',`log`='" + Util.strSQL(this.log) + "',`alert`='" + Util.strSQL(this.alert) + "',`use_card`=" + this.use_card + ",`openDun`=" + this.openDun + "";
            final JSONArray jarrMem = new JSONArray();
            for (short i = 0; i < this.members.size(); ++i) {
                final Member mem = this.members.get(i);
                if (mem != null) {
                    jarrMem.add(JSONValue.parseWithException(mem.toString()));
                }
            }
            final JSONArray jarrItem = new JSONArray();
            for (short j = 0; j < this.items.size(); ++j) {
                final Item item = this.items.get(j);
                if (item != null) {
                    jarrItem.add(JSONValue.parseWithException(item.toString()));
                }
            }
            final JSONArray jarrTT = new JSONArray();
            for (short i = 0; i < this.clan_thanThu.size(); ++i) {
                final Clan_ThanThu thanThu = this.clan_thanThu.get(i);
                if (thanThu != null) {
                    jarrTT.add(JSONValue.parseWithException(thanThu.toString()));
                }
            }
            sql = sql + ",`members`='" + jarrMem.toJSONString() + "',`numMem`=" + this.numMember + ",`maxMem`=" + this.maxMember + ",`items`='" + jarrItem.toJSONString() + "',`than_thu`='" + jarrTT.toJSONString() + "',`debt`=" + this.debt + ",`isDissolution`=" + this.isDissolution + ",`tolltimeWeek`=" + this.tolltimeWeek + "";
            final MySQL mySQL = new MySQL(1);
            try {
                mySQL.stat.executeUpdate("UPDATE `clan` SET " + sql + " WHERE `clanId`=" + this.clanId + " LIMIT 1;");
            } finally {
                mySQL.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateCoin(long num) {
        final long xunew = this.coin + num;
        if (xunew > 2000000000L) {
            num = 2000000000L - this.coin;
        } else if (xunew < -2000000000L) {
            num = -2000000000L - this.coin;
        }
        this.coin += (int) num;
        if (this.coin >= 0) {
            this.debt = 0;
        } else if (num < 0L) {
            ++this.debt;
            if (this.debt >= 3) {
                this.isDissolution = true;
            }
        }
    }

    protected void updateExp(long num) {
        final long xpnew = this.exp + num;
        if (xpnew > 2000000000L) {
            num = 2000000000L - this.exp;
        } else if (xpnew < -2000000000L) {
            num = -2000000000L - this.exp;
        }
        this.exp += (int) num;
    }

    protected int getCoinOpen() {
        if (this.itemLevel == 0) {
            return 1000000;
        }
        if (this.itemLevel == 1) {
            return 5000000;
        }
        if (this.itemLevel == 2) {
            return 10000000;
        }
        if (this.itemLevel == 3) {
            return 20000000;
        }
        if (this.itemLevel == 4) {
            return 30000000;
        }
        return 0;
    }

    protected void writeLog(final String cName, final int num, final int number, final String date) {
        final String[] array = this.log.split("\n");
        this.log = cName + "," + num + "," + number + "," + date + "\n";
        for (int i = 0; i < array.length && i != 10; ++i) {
            this.log = this.log + array[i] + "\n";
        }
    }

    protected static Member getMem(final Clan clan, final String cName) {
        synchronized (clan.LOCK_CLAN) {
            for (short i = 0; i < clan.members.size(); ++i) {
                final Member mem = clan.members.get(i);
                if (mem != null && mem.cName.equals(cName)) {
                    return mem;
                }
            }
            return null;
        }
    }

    protected void inputCoinClan(final Char _char, final int coin) {
        synchronized (this.LOCK_CLAN) {
            this.updateCoin(coin);
            if (coin >= 1000) {
                this.writeLog(_char.cName, 2, coin, Util.toDateString(Date.from(Instant.now())));
            }
            this.ClanMessage(String.format(Text.get(0, 194), _char.cName, coin));
        }
    }

    protected void changeTypeClan(final String name, final byte typeClan) {
        short i = 0;
        while (i < this.members.size()) {
            final Member mem = this.members.get(i);
            if (mem != null && mem.cName.equals(name)) {
                if (typeClan == 0) {
                    if (mem.typeClan == 3) {
                        this.assist_name = "";
                    } else if (mem.typeClan == 2) {
                        if (this.elder1_name.equals(name)) {
                            this.elder1_name = "";
                        } else if (this.elder2_name.equals(name)) {
                            this.elder2_name = "";
                        } else if (this.elder3_name.equals(name)) {
                            this.elder3_name = "";
                        } else if (this.elder4_name.equals(name)) {
                            this.elder4_name = "";
                        } else if (this.elder5_name.equals(name)) {
                            this.elder5_name = "";
                        }
                    }
                } else if (typeClan == 3) {
                    this.assist_name = name;
                } else if (typeClan == 2) {
                    if (this.elder1_name.isEmpty()) {
                        this.elder1_name = name;
                    } else if (this.elder2_name.isEmpty()) {
                        this.elder2_name = name;
                    } else if (this.elder3_name.isEmpty()) {
                        this.elder3_name = name;
                    } else if (this.elder4_name.isEmpty()) {
                        this.elder4_name = name;
                    } else {
                        this.elder5_name = name;
                    }
                }
                mem.typeClan = typeClan;
                final Player player = Client.getPlayer(name);
                if (player != null) {
                    final Player player2 = player;
                    player.member.typeClan = typeClan;
                    player2.ctypeClan = typeClan;
                    try {
                        for (short k = 0; k < player.tileMap.numPlayer; ++k) {
                            if (player.tileMap.aCharInMap.get(k).user != null && player.tileMap.aCharInMap.get(k).user.session != null) {
                                Service.clanInviteAccept(player.tileMap.aCharInMap.get(k), player.charID, player.cClanName, player.ctypeClan);
                            }
                        }
                    } catch (Exception e) {
                    }
                    break;
                }
                break;
            } else {
                ++i;
            }
        }
    }

    protected void moveMember(final String name, final String message) {
        short i = 0;
        while (i < this.members.size()) {
            final Member mem = this.members.get(i);
            if (mem != null && mem.cName.equals(name)) {
                this.members.remove(i);
                --this.numMember;
                if (mem.typeClan == 3) {
                    this.assist_name = "";
                } else if (mem.typeClan == 2) {
                    if (this.elder1_name.equals(name)) {
                        this.elder1_name = "";
                    } else if (this.elder2_name.equals(name)) {
                        this.elder2_name = "";
                    } else if (this.elder3_name.equals(name)) {
                        this.elder3_name = "";
                    } else if (this.elder4_name.equals(name)) {
                        this.elder4_name = "";
                    } else if (this.elder5_name.equals(name)) {
                        this.elder5_name = "";
                    }
                }
                final Player player = Client.getPlayer(mem.cName);
                if (player != null) {
                    player.cClanName = "";
                    final Member member = player.member;
                    final Player player2 = player;
                    final byte b = -1;
                    player2.ctypeClan = b;
                    member.typeClan = b;
                    player.member.pointClanWeek = 0;
                    player.clan = null;
                    if (message != null && !message.isEmpty()) {
                        Service.ServerMessage(player, message);
                    }
                    try {
                        for (short k = 0; k < player.tileMap.aCharInMap.size(); ++k) {
                            if (player.tileMap.aCharInMap.get(k).user != null && player.tileMap.aCharInMap.get(k).user.session != null) {
                                Service.clanMoveOutMem(player.tileMap.aCharInMap.get(k), player.charID);
                            }
                        }
                    } catch (Exception e) {
                    }
                    break;
                }
                break;
            } else {
                ++i;
            }
        }
    }

    protected void ClanMessage(final String str) {
        if (!str.isEmpty()) {
            for (short i = 0; i < this.members.size(); ++i) {
                final Member mem = this.members.get(i);
                if (mem != null) {
                    final Player player = Client.getPlayer(mem.cName);
                    if (player != null) {
                        Service.ServerMessage(player, str);
                    }
                }
            }
        }
    }

    protected void ClanEndWait(final String str) {
        if (!str.isEmpty()) {
            for (short i = 0; i < this.members.size(); ++i) {
                final Member mem = this.members.get(i);
                if (mem != null) {
                    final Player player = Client.getPlayer(mem.cName);
                    if (player != null) {
                        GameCanvas.EndWait(player, str);
                    }
                }
            }
        }
    }

    protected void chatClan(final String who, final String str) {
        if (!str.isEmpty()) {
            for (short i = 0; i < this.members.size(); ++i) {
                final Member mem = this.members.get(i);
                if (mem != null) {
                    final Player player = Client.getPlayer(mem.cName);
                    if (player != null) {
                        Service.chatClan(player, who, str);
                    }
                }
            }
        }
    }

    protected static void update() {
        for (int i = 0; i < Clan.Aclan.size(); i++) {
            if (Clan.Aclan.get(i).delayWarClan > 0) {
                Clan.Aclan.get(i).delayWarClan -= 1;
            }
        }
    }

    protected void close() {
        synchronized (this.LOCK_CLAN) {
            this.flush();
        }
    }

    static {
        Aclan = new ArrayList<Clan>();
        clan_name = new HashMap<String, Clan>();
        clan_id = new HashMap<Integer, Clan>();
        LOCK = new Object();
        arrCoinOutClan = new int[]{10000, 20000, 50000, 100000};
    }
}
