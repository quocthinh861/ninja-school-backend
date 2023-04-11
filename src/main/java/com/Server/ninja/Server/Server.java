 package com.Server.ninja.Server;

import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.time.Instant;
import java.util.Calendar;
import java.net.ServerSocket;

public class Server extends Thread {
    protected static ServerSocket server;
    protected static boolean start;
    protected static int id;
    protected static long l;
    protected static Server instance;
    protected static int TimeDissConnect;
    protected static int backupTime;
    private final Object LOCK;
    private boolean isrun;
    private static final Calendar rightNow;
    private static final int[] hoursRefreshBoss;
    private static final int[] dayRefreshBoss;
    private static final short[] mapBossVDMQ;
    private static final short[] mapBoss45;
    private static final short[] mapBoss55;
    private static final short[] mapBoss65;
    private static final short[] mapBoss75;
    private static final int[] hoursLighttime;
    protected static boolean Lighttime;
    private static final int[] hoursRefreshCT;
    private static final int[] dayRefreshCT;
    private static final int[] hoursRefreshTL;
    private static final int[] dayRefreshTTA;
    private static final int[] hoursRefreshTTA;
    private static int timePrintSize;

    public Server() {
        this.LOCK = new Object();
        this.isrun = false;
    }

    public static Server getInstance() {
        if (Server.instance == null) {
            Server.instance = new Server();
        }
        return Server.instance;
    }

    private static String addMapNameChat(final int num, final String mapName) {
        if (num == 0) {
            return String.format(Text.get(0, 152), mapName);
        }
        return ", " + mapName;
    }

    private void update() {
        try {
            Server.rightNow.setTimeInMillis(System.currentTimeMillis());
            final int hour = Server.rightNow.get(Calendar.HOUR_OF_DAY);
            final int min = Server.rightNow.get(Calendar.MINUTE);
            final int sec = Server.rightNow.get(Calendar.SECOND);
            final int day = Server.rightNow.get(Calendar.DAY_OF_YEAR);
            Clan.update();
            if (min % 5 == 0 && sec == 0) {
                Session_ME.logIP();
            }
            if (min % 6 == 0 && hour == (hoursRefreshTL[0] + 1) && sec == 0) {
                if (NJTalent.isRun) {
                    NJTalent.startPk();
                    System.out.println("START PK TALENT");
                }
            }
            if (hour == (hoursRefreshTL[0] + 2) && min == 0 && sec == 0) {
                if (NJTalent.isRun) {
                    NJTalent.close();
                    Client.alertServer("Ninja Tài Năng đã kết thúc.");
                    System.out.println("CLOSE TALENT");
                }
            }
            for (int i = 0; i < Server.hoursRefreshBoss.length; ++i) {
                if (Server.hoursRefreshBoss[i] == hour) {
                    int numBoss = 0;
                    if (Server.dayRefreshBoss[i] < day) {
                        StringBuilder textchat = new StringBuilder();
                        for (byte j = 0; j < Util.nextInt(1, 1); ++j) {
                            final Map map = MapServer.getMapServer(Server.mapBoss75[Util.nextInt(Server.mapBoss75.length)]);
                            if (map != null) {
                                Map.liveBoss(map, (byte) Util.nextInt(15, 29), (byte) 0);
                                textchat.append(addMapNameChat(numBoss++, map.template.mapName));
                            }
                        }
                        for (byte j = 0; j < Util.nextInt(1, 2); ++j) {
                            final Map map = MapServer.getMapServer(Server.mapBoss65[Util.nextInt(Server.mapBoss65.length)]);
                            if (map != null) {
                                Map.liveBoss(map, (byte) Util.nextInt(15, 29), (byte) 0);
                                textchat.append(addMapNameChat(numBoss++, map.template.mapName));
                            }
                        }
                        for (byte j = 0; j < Util.nextInt(1, 2); ++j) {
                            final Map map = MapServer.getMapServer(Server.mapBoss55[Util.nextInt(Server.mapBoss55.length)]);
                            if (map != null) {
                                Map.liveBoss(map, (byte) Util.nextInt(15, 29), (byte) 0);
                                textchat.append(addMapNameChat(numBoss++, map.template.mapName));
                            }
                        }
                        for (byte j = 0; j < Util.nextInt(1, 2); ++j) {
                            final Map map = MapServer.getMapServer(Server.mapBoss45[Util.nextInt(Server.mapBoss45.length)]);
                            if (map != null) {
                                Map.liveBoss(map, (byte) Util.nextInt(15, 29), (byte) 0);
                                textchat.append(addMapNameChat(numBoss++, map.template.mapName));
                            }
                        }
                        for (byte j = 0; j < Server.mapBossVDMQ.length; ++j) {
                            final Map map = MapServer.getMapServer(Server.mapBossVDMQ[j]);
                            if (map != null) {
                                int khu = Util.nextInt(15, 29);
                                Map.liveBoss(map, (byte) khu, (byte) 0);
                                textchat.append(addMapNameChat(numBoss++, map.template.mapName));
                            }
                        }
                        Client.alertServer(textchat.toString());
                        Util.log("Ad\u0111 boss day " + day + " hour " + hour);
                        Server.dayRefreshBoss[i] = day;
                    }
                }
            }
            for (byte k = 0; k < Server.hoursLighttime.length; ++k) {
                if (Server.hoursLighttime[k] == hour && GameScr.vEvent == 1) {
                    Server.Lighttime = true;
                    break;
                }
                Server.Lighttime = false;
            }
            for (byte k = 0; k < Lucky.arrLucky.length; ++k) {
                final Lucky lucky = Lucky.arrLucky[k];
                if (lucky != null) {
                    synchronized (lucky.LOCK) {
                        if (lucky.start) {
                            final Lucky lucky3;
                            final Lucky lucky2 = lucky3 = lucky;
                            --lucky3.time;
                            if (lucky.time < 10 && !lucky.isLockJoin) {
                                lucky.isLockJoin = true;
                            } else if (lucky.time <= 0) {
                                lucky.start = false;
                                lucky.Turned();
                                lucky.reset();
                            }
                        }
                    }
                }
            }
            synchronized (Clan.LOCK) {
                for (int l = 0; l < Clan.Aclan.size(); ++l) {
                    final Clan clan = Clan.Aclan.get(l);
                    if (clan != null) {
                        synchronized (clan.LOCK_CLAN) {
                            final Clan clan3;
                            final Clan clan2 = clan3 = clan;
                            clan3.timeBackup -= 1000;
                            Clan_ThanThu.apTrungFinish(clan);
                            if (clan.timeBackup <= 0) {
                                clan.flush();
                                clan.timeBackup = 60000;
                            }
                            if (clan.isDissolution && clan.members.size() > 0) {
                                while (clan.members.size() > 0) {
                                    final Member mem = clan.members.get(0);
                                    if (mem != null) {
                                        clan.moveMember(mem.cName, String.format(Text.get(0, 197), clan.name));
                                    }
                                }
                            } else if (!clan.isDissolution) {
                                final Calendar old = Util.Calendar(clan.tolltimeWeek);
                                final Calendar now = Util.Calendar(System.currentTimeMillis() - 86400000L);
                                if (now.get(Calendar.YEAR) > old.get(Calendar.YEAR) || now.get(Calendar.WEEK_OF_YEAR) > old.get(Calendar.WEEK_OF_YEAR)) {
                                    clan.use_card = 4;
                                    clan.openDun = 3;
                                    for (short m = 0; m < clan.members.size(); ++m) {
                                        final Member mem2 = clan.members.get(m);
                                        if (mem2 != null) {
                                            mem2.pointClanWeek = 0;
                                        }
                                    }
                                    final int coinDown = Clan.getfreeCoin(clan.members.size());
                                    clan.updateCoin(-coinDown);
                                    clan.writeLog("", 4, coinDown, Util.toDateString(Date.from(Instant.now())));
                                    clan.tolltimeWeek = System.currentTimeMillis();
                                }
                            }
                        }
                    }
                }
            }
            synchronized (BackCave.LOCK) {
                for (int l = BackCave.arrCave.size() - 1; l >= 0; --l) {
                    final BackCave cave = BackCave.arrCave.get(l);
                    if (cave != null && cave.timeLength <= System.currentTimeMillis() / 1000L) {
                        BackCave.arrCave.remove(l);
                        cave.CLOSE();
                    }
                }
            }
            synchronized (TestDun.LOCK) {
                for (int l = TestDun.arrTestDun.size() - 1; l >= 0; --l) {
                    final TestDun test = TestDun.arrTestDun.get(l);
                    test.update();
                    if (test.timeLength <= System.currentTimeMillis() / 1000L) {
                        TestDun.arrTestDun.remove(l);
                        test.CLOSE();
                    }
                }
            }
            synchronized (WarClan.LOCK) {
                for (int l = WarClan.arrWarClan.size() - 1; l >= 0; --l) {
                    final WarClan war = WarClan.arrWarClan.get(l);
                    if (war != null && war.isInvite && war.timeLength <= System.currentTimeMillis() / 1000L) {
                        war.isInvite = false;
                        war.timeLength = (int)(System.currentTimeMillis() / 1000L + WarClan.setTimeChien);
                        for (Char c : war.AChar) {
                            if (c != null) {
                                Service.mapTime(c, (int) (war.timeLength - System.currentTimeMillis() / 1000L));
                            }
                        }
                    }
                    if (war != null && war.timeLength <= System.currentTimeMillis() / 1000L) {
                        WarClan.arrWarClan.remove(l);
                        war.CLOSE();
                    }
                }
            }
            synchronized (ClanManor.LOCK) {
                for (int z = ClanManor.arrClanManor.size() - 1; z >= 0; --z) {
                    final ClanManor manor = ClanManor.arrClanManor.get(z);
                    if (manor != null && (manor.timeLength - System.currentTimeMillis() / 1000L) <= 0) {
                        ClanManor.arrClanManor.remove(z);
                        manor.CLOSE();
                    }
                }
            }
            synchronized (ItemStands.LOCK) {
                for (byte i2 = 0; i2 < ItemStands.arrItemStands.length; ++i2) {
                    for (int j2 = ItemStands.arrItemStands[i2].size() - 1; j2 >= 0; --j2) {
                        final ItemStands itemStands = ItemStands.arrItemStands[i2].get(j2);
                        if (itemStands != null && itemStands.timeStart + itemStands.timeEnd <= System.currentTimeMillis() / 1000L) {
                            ItemStands.arrItemStands[i2].remove(j2);
                            ItemWait.addItem(-1, itemStands.seller, itemStands.item);
                            ItemStands.flush();
                        }
                    }
                }
                ItemStands.timeBackup -= 1000;
                if (ItemStands.timeBackup <= 0) {
                    ItemStands.flush();
                    ItemStands.timeBackup = 60000;
                }
            }
            synchronized (ItemWait.LOCK) {
                for (int l = ItemWait.arrWait.size() - 1; l >= 0; --l) {
                    final ItemWait wait = ItemWait.arrWait.get(l);
                    if (wait != null) {
                        final Player player = Client.getPlayer(wait.take_cName);
                        if (player != null) {
                            if (wait.type == 0 && player.ItemBagSlotNull() > 0) {
                                player.ItemBagAdd(wait.item);
                                Service.AlertMessage(player, Text.get(0, 234), String.format(Text.get(0, 235), wait.item.template.name));
                                ItemWait.arrWait.remove(l);
                                ItemWait.flush();
                            } else if (wait.type == 1) {
                                player.upCoinLock(wait.coinLock, (byte) 2);
                                Service.AlertMessage(player, Text.get(0, 234), String.format(Text.get(0, 237), Util.getFormatNumber(wait.coinLock)));
                                ItemWait.arrWait.remove(l);
                                ItemWait.flush();
                            } else if (wait.type == 2) {
                                player.upCoin(wait.coin, (byte) 1);
                                Service.AlertMessage(player, Text.get(0, 234), String.format(Text.get(0, 238), Util.getFormatNumber(wait.coin)));
                                ItemWait.arrWait.remove(l);
                                ItemWait.flush();
                            } else if (wait.type == 3) {
                                player.upGold(wait.gold, (byte) 2);
                                Service.AlertMessage(player, Text.get(0, 234), String.format(Text.get(0, 239), Util.getFormatNumber(wait.gold)));
                                ItemWait.arrWait.remove(l);
                                ItemWait.flush();
                            } else if (wait.type == 4) {
                                player.getMyChar().updateExp(wait.exp);
                                Service.AlertMessage(player, Text.get(0, 234), String.format(Text.get(0, 240), Util.getFormatNumber(wait.exp)));
                                ItemWait.arrWait.remove(l);
                                ItemWait.flush();
                            }
                        }
                    }
                }
                ItemWait.timeBackup -= 1000;
                if (ItemWait.timeBackup <= 0) {
                    ItemWait.flush();
                    ItemWait.timeBackup = 60000;
                }
            }
            final ChienTruong ct = ChienTruong.chien_truong;
            if (ct != null) {
                synchronized (ct.CHIENTRUONG_LOCK) {
                    if (ct.timeLength <= System.currentTimeMillis() / 1000L) {
                        if (ct.level == 0) {
                            ct.level = 1;
                            ct.isBaoDanh = false;
                            ct.timeLength = (int) (System.currentTimeMillis() / 1000L + 3600L);
                        } else if (ct.level == 1) {
                            ct.typeWin = (byte) ((ct.totalWhite > ct.totalBlack) ? 0 : ((ct.totalBlack > ct.totalWhite) ? 1 : 2));
                            ct.level = 2;
                            ct.timeLength = (int) (System.currentTimeMillis() / 1000L + 600L);
                        } else if (ct.level == 2) {
                            ct.CLOSE();
                            ChienTruong.chien_truong = null;
                        }
                    } else {
                        ct.update();
                    }
                }
            }
            if (GameScr.vEvent_1 == 1) {
                if (Event_1.timeRefresh > 0L) {
                    Event_1.timeRefresh -= 1000L;
                    if (Event_1.timeRefresh <= 0L) {
                        Event_1.timeRefresh = 0L;
                        Event_1.buy_Count = 0;
                        Event_1.timeSale = 7200000L;
                    }
                }
                if (Event_1.timeSale > 0L) {
                    Event_1.timeSale -= 1000L;
                    if (Event_1.timeSale <= 0L) {
                        Event_1.timeSale = 0L;
                        Event_1.buy_Count = 1000;
                        Event_1.timeRefresh = 3600000L;
                        Event_1.indexTrung = Util.nextInt(Event_1.buy_Count);
                        Event_1.indexDung = 0;
                    }
                }
            }
            for (int l = 0; l < Server.hoursRefreshCT.length; ++l) {
                if (Server.hoursRefreshCT[l] == hour && Server.dayRefreshCT[l] < day) {
                    byte TYPECT = 0;
                    if (hour == 13) {
                        TYPECT = 1;
                    } else if (hour == 21) {
                        TYPECT = 3;
                    }
                    ChienTruong.setChienTruong(TYPECT);
                    Client.alertServer(Text.get(0, 349));
                    Server.dayRefreshCT[l] = day;
                }
            }
            // Thất thú ải
            for (int tta = 0; tta < Server.hoursRefreshTTA.length; ++tta) {
                if (Server.hoursRefreshTTA[tta] == hour && Server.dayRefreshTTA[tta] < day) {
                    SevenBeasts.isRun = true;
                    SevenBeasts.isStart = false;
                    SevenBeasts.isBaoDanh = true;
                    SevenBeasts.time = System.currentTimeMillis();
                    Client.alertServer(Text.get(0, 412));
                    Server.dayRefreshTTA[tta] = day;
                }
            }
            if (SevenBeasts.time <= System.currentTimeMillis() - 20000 && SevenBeasts.isRun) { // 1800000
                SevenBeasts.isStart = true;
                SevenBeasts.isBaoDanh = false;
            }
            if (SevenBeasts.time <= System.currentTimeMillis() - 150000 && SevenBeasts.isRun) { // 5400000
                SevenBeasts.isRun = false;
                SevenBeasts.clear();
            }
            // Ninja tài năng
            if (hour == hoursRefreshTL[0] && min == 50 && sec == 0) {
                NJTalent.startRun();
                Client.alertServer("Ninja Tài Năng đã mở cửa và sẽ bắt đầu trận đấu đầu tiên vào 18 giờ.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        if (this.isrun) {
            this.close();
        }
        this.isrun = true;
        super.start();
    }

    @Override
    public void run() {
        while (this.isrun) {
            try {
                final long l1 = System.currentTimeMillis();
                this.update();
                if (Server.timePrintSize <= 0) {
                    System.out.println("Size_connection = " + Client.sizeClients + " Id_Session=" + Server.id + " timeRun " + Util.getStrTime(System.currentTimeMillis() - Server.l));
                    Server.timePrintSize = 50000;
                } else {
                    Server.timePrintSize -= 1000;
                }
                final long l2 = System.currentTimeMillis() - l1;
                if (1000L - l2 <= 0L) {
                    continue;
                }
                TimeUnit.MILLISECONDS.sleep(1000L - l2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected static void start(final int post) {
        System.out.println("Start socket post=" + post);
        try {
            Server.server = new ServerSocket(post);
            Server.id = 0;
            Client.sizeClients = 0;
            Server.start = true;
            MapServer.loadMapServer();
            Robot.gI().start();
            ItemStands.Init();
            ItemWait.Init();
            Top.init();
            Clan.Init();
            getInstance().start();
            Util.log("Start server Success!");
            Server.l = System.currentTimeMillis();
            while (Server.start) {
                try {
                    final Socket client = Server.server.accept();
                    final Session_ME conn = new Session_ME(client, Server.id++);
                    conn.run();
                    Client.joinClient(conn);
                    if (Client.sizeClients > 1000) {
                        close(1000);
                    }
                    System.err.println("Accept socket " + conn + " done size " + conn.sc.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Util.log("Close server!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void close() {
        for (byte i = 0; i < Lucky.arrLucky.length; ++i) {
            final Lucky lucky = Lucky.arrLucky[i];
            if (lucky != null) {
                synchronized (lucky.LOCK) {
                    if (lucky.numPlayer > 0) {
                        lucky.Turned();
                    }
                }
            }
        }
        this.isrun = false;
    }

    protected static void close(final int time) {
        try {
            Server.start = false;
            Server.server.close();
            Server.backupTime = 0;
            Server.TimeDissConnect = time;
            getInstance().close();
            BackCave.close();
            MapServer.close();
            TestDun.close();
            ChienTruong.close();
            NJTalent.close();
            WarClan.close();
            synchronized (ItemStands.LOCK) {
                ItemStands.flush();
            }
            synchronized (ItemWait.LOCK) {
                ItemWait.flush();
            }
            Top.saveTopFile((byte) 1);
            Top.saveTopFile((byte) 3);
            Clan.closes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        Server.TimeDissConnect = -1;
        Server.backupTime = -1;
        rightNow = Calendar.getInstance();
        hoursRefreshBoss = new int[]{6, 12, 21};
        dayRefreshBoss = new int[]{-1, -1, -1};
        mapBossVDMQ = new short[]{141, 142, 143};
        mapBoss45 = new short[]{14, 15, 16, 34, 35, 52, 68};
        mapBoss55 = new short[]{44, 67};
        mapBoss65 = new short[]{24, 41, 45, 59};
        mapBoss75 = new short[]{18, 36, 54};
        hoursLighttime = new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        Server.Lighttime = true;
        hoursRefreshCT = new int[]{13, 21};
        dayRefreshCT = new int[]{-1, -1};
        hoursRefreshTL = new int[]{17};
        dayRefreshTTA = new int[]{-1, -1};
        hoursRefreshTTA = new int[]{9, 21};
        Server.timePrintSize = 0;
    }
}
