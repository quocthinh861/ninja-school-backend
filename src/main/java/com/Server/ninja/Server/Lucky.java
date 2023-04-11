 package com.Server.ninja.Server;

import java.sql.ResultSet;
import com.Server.io.MySQL;
import java.util.ArrayList;

public class Lucky
{
    private static final short TIME_SET = 120;
    private static final int maxTotal = 1500000000;
    protected String title;
    protected short time;
    protected int mili;
    protected int totalMoney;
    protected short numPlayer;
    protected String winnerInfo;
    protected byte typeLucky;
    protected boolean start;
    protected boolean isLockJoin;
    private final int minJoinMoney;
    private final int maxMoney;
    private final int minMoney;
    protected static Lucky[] arrLucky;
    private final ArrayList<LuckyPlayer> vPlayer;
    protected final Object LOCK;
    
    private Lucky(final byte typeLucky, final String title, final int minJoinMoney, final int maxMoney, final int minMoney) {
        this.time = 0;
        this.mili = 1000;
        this.totalMoney = 0;
        this.numPlayer = 0;
        this.winnerInfo = null;
        this.start = false;
        this.isLockJoin = false;
        this.vPlayer = new ArrayList<LuckyPlayer>();
        this.LOCK = new Object();
        this.typeLucky = typeLucky;
        this.title = title;
        this.time = 120;
        this.minJoinMoney = minJoinMoney;
        this.maxMoney = maxMoney;
        this.minMoney = minMoney;
    }
    
    protected void Join(final Player _char, final int money) {
        synchronized (this.LOCK) {
            if (MapServer.notTrade(_char.tileMap.mapID)) {
                Service.ServerMessage(_char, Text.get(0, 300));
            }
            else if (this.isLockJoin) {
                Service.ServerMessage(_char, Text.get(0, 58));
            }
            else if (this.totalMoney > 1500000000) {
                Service.ServerMessage(_char, String.format(Text.get(0, 65), this.minMoney));
            }
            else if (_char.xu < this.minJoinMoney) {
                Service.ServerMessage(_char, String.format(Text.get(0, 61), this.minJoinMoney));
            }
            else if (money > this.maxMoney) {
                Service.ServerMessage(_char, String.format(Text.get(0, 59), this.maxMoney));
            }
            else if (money < this.minMoney) {
                Service.ServerMessage(_char, String.format(Text.get(0, 60), this.minMoney));
            }
            else if (money > _char.xu) {
                Service.ServerMessage(_char, String.format(Text.get(0, 62), this.minMoney));
            }
            else {
                LuckyPlayer player = null;
                for (short i = 0; i < this.numPlayer; ++i) {
                    final LuckyPlayer player2 = this.vPlayer.get(i);
                    if (player2 != null && player2.playerId == _char.playerId) {
                        player = player2;
                    }
                }
                if (player != null) {
                    if (money + (long)player.myMoney > this.maxMoney) {
                        Service.ServerMessage(_char, String.format(Text.get(0, 59), this.maxMoney));
                    }
                    else {
                        final LuckyPlayer luckyPlayer = player;
                        luckyPlayer.myMoney += money;
                        this.totalMoney += money;
                        _char.upCoin(-money, (byte)1);
                        Service.AlertLuck(_char, this);
                    }
                }
                else {
                    player = new LuckyPlayer(_char.user.userName, _char.cName, _char.playerId, money, (byte)0);
                    this.totalMoney += money;
                    this.vPlayer.add(player);
                    ++this.numPlayer;
                    _char.upCoin(-money, (byte)1);
                    Service.AlertLuck(_char, this);
                    if (this.numPlayer >= 2 && !this.start) {
                        this.start = true;
                        final Player player3 = Client.getPlayer(this.vPlayer.get(0).cName);
                        if (player3 != null && !player3.isTrade && player3.statusMe != 14) {
                            Service.AlertLuck(player3, this);
                        }
                    }
                }
            }
        }
    }
    
    protected float percentWin(final int playerId) {
        try {
            for (short i = 0; i < this.numPlayer; ++i) {
                final LuckyPlayer player = this.vPlayer.get(i);
                if (player != null && player.playerId == playerId) {
                    return player.myMoney * 100.0f / this.totalMoney;
                }
            }
        }
        catch (Exception ex) {}
        return 0.0f;
    }
    
    protected int myMoney(final int playerId) {
        try {
            for (short i = 0; i < this.numPlayer; ++i) {
                final LuckyPlayer player = this.vPlayer.get(i);
                if (player != null && player.playerId == playerId) {
                    return player.myMoney;
                }
            }
        }
        catch (Exception ex) {}
        return 0;
    }
    
    protected void reset() {
        this.totalMoney = 0;
        this.time = 120;
        this.vPlayer.clear();
        this.isLockJoin = false;
        this.numPlayer = 0;
    }
    
    protected void Turned() {
        LuckyPlayer player = null;
        for (int n = 0; player == null && n < 1000; ++n) {
            final LuckyPlayer player2 = this.vPlayer.get(Util.nextInt(this.vPlayer.size()));
            if (player2 != null && this.percentWin(player2.playerId) >= Util.nextInt(1, Util.nextInt(50, 100))) {
                player = player2;
                break;
            }
        }
        if (player == null && this.vPlayer.size() > 0) {
            player = this.vPlayer.get(Util.nextInt(this.vPlayer.size()));
        }
        if (player != null) {
            long moneyWin = this.totalMoney;
            if (this.numPlayer >= 10) {
                moneyWin = (int)(moneyWin * 90L / 100L);
            }
            else {
                moneyWin = moneyWin * (100 - (this.numPlayer - 1)) / 100L;
            }
            Player _char = Client.getPlayer(player.cName);
            final User user = Client.getUser(player.uName);
            if (_char != null && user != null) {
                _char.upCoin(moneyWin, (byte)1);
            }
            else if (user != null) {
                for (byte i = 0; i < user.players.length; ++i) {
                    _char = user.players[i];
                    if (_char != null && _char.playerId == player.playerId) {
                        _char.upCoin(moneyWin, (byte)0);
                    }
                }
            }
            else {
                try {
                    final MySQL mySQL = new MySQL(1);
                    try {
                        final ResultSet red = mySQL.stat.executeQuery("SELECT `xu` FROM `player` WHERE `playerId` = " + player.playerId + " LIMIT 1;");
                        if (red.first()) {
                            int xu = red.getInt("xu");
                            if (xu + moneyWin > 2000000000L) {
                                xu = 2000000000;
                            }
                            xu += (int)moneyWin;
                            mySQL.stat.executeUpdate("UPDATE `player` SET `xu`=" + xu + " WHERE `playerId`=" + player.playerId + ";");
                        }
                    }
                    finally {
                        mySQL.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.winnerInfo = String.format(Text.get(0, 64), (this.typeLucky == 0) ? ("c" + Util.nextInt(10)) : "", player.cName, Util.getFormatNumber(moneyWin), Util.getFormatNumber(player.myMoney));
            Client.chatServer("server", String.format(Text.get(0, 63), player.cName.toUpperCase(), Util.getFormatNumber(moneyWin)));
        }
    }
    
    static {
        Lucky.arrLucky = new Lucky[] { new Lucky((byte)0, "Vòng quay vip", 11000000, 50000000, 1000000), new Lucky((byte)1, "Vòng quay thường", 0, 100000, 10000) };
    }
    
    private class LuckyPlayer
    {
        private final String uName;
        private final String cName;
        private final int playerId;
        private int myMoney;
        
        private LuckyPlayer(final String uName, final String cName, final int playerId, final int myMoney, final byte type) {
            this.uName = uName;
            this.cName = cName;
            this.playerId = playerId;
            this.myMoney = myMoney;
        }
    }
}
