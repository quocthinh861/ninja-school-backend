package com.Server.ninja.Server;

import java.util.ArrayList;

public class Robot implements Runnable {
    public class Bot {

        protected short x;
        protected short y;
        protected int id;
        protected int idChar;
        protected float hp;
        protected long dame;
        protected TileMap map;
        protected long time;
        protected Player robotPlayer;
        protected short[] arrX;

        public Bot(short x, short y, Player player, int id, TileMap map, long time, int idChar) {
            this.x = x;
            this.y = y;
            this.robotPlayer = player;
            this.id = id;
            this.idChar = idChar;
            this.hp = player.cHP;
            this.map = map;
            this.time = time;
            this.dame = (long) player.cDame((byte) 0, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0);
            this.arrX = setX(this.x);
        }

        private static short[] setX(short x) {
            short[] arr = new short[11];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (short) (x + Util.nextInt(-100, 100));
            }
            return arr;
        }
    }
    public ArrayList<Bot> arrBot;
    public static Robot instance;
    private boolean running;
    
    public Robot() {
        arrBot = new ArrayList<>();
        running = false;
    }
    
    private Thread threadRobot;
    private TileMap robotTileMap;

    public static Robot gI() {
        if (instance == null) {
            instance = new Robot();
        }
        return instance;
    }

    public void callBot(Player p,int id, short x, short y, byte pk) {
        robotTileMap = p.tileMap;
        User user = new User();
        Player player = Player.getChar(user, id);
        arrBot.add(new Bot(x, y, player,id, robotTileMap, System.currentTimeMillis(), p.charID));
        player.cHP = player.cMaxHP();
        player.isBot = true;
        player.cTypePk = pk;
        player.cx = x;
        player.cy = y;
        player.cName = "do " + p.cName + " hộ tống";
        robotTileMap.JOIN(player);
    }
    protected void start() {
        threadRobot = new Thread(this);
        threadRobot.start();
    }
    @Override
    public void run() {
        running = true;
        while (running) {
            for (int i = 0; i < arrBot.size(); i++) {
                Bot bot = arrBot.get(i);
                if (bot.id == 3) {
                    bot.robotPlayer.tileMap.move(bot.robotPlayer.charID, bot.robotPlayer.cx -= 30, bot.robotPlayer.cy);
                } else {
                    bot.robotPlayer.tileMap.move(bot.robotPlayer.charID, bot.arrX[Util.nextInt(0,bot.arrX.length - 1)], bot.robotPlayer.cy);
                }
                Char _char = Client.getPlayer(bot.idChar);
                if (bot.robotPlayer.cx < 0) {
                    if (_char.ctaskId == 17 && _char.ctaskIndex == 1 && bot.id == 3) {
                        _char.uptaskMaint();
                    }

                    removeBot(bot, i);
                }
                if (Math.abs(_char.cx - bot.robotPlayer.cx) > 250) {
                    Service.ServerMessage(_char, "Nhiệm vụ thất bại vì khoảng cách quá xa.");
                    removeBot(bot, i);
                } else if (!bot.robotPlayer.tileMap.findCharInMap(bot.idChar)) {
                    Service.ServerMessage(_char, "Nhiệm vụ thất bại.");
                    removeBot(bot, i);
                }         
                if (bot.robotPlayer.cHP <= 0) {
                    if (_char.ctaskId == 13 && _char.ctaskIndex == 1 && bot.id == 0) {
                        _char.uptaskMaint();
                        TileMap.getMapLTD(_char);
                    } else if (_char.ctaskId == 13 && _char.ctaskIndex == 2 && bot.id == 1) {
                        _char.uptaskMaint();
                        TileMap.getMapLTD(_char);
                    } else if (_char.ctaskId == 13 && _char.ctaskIndex == 3 && bot.id == 2) {
                        _char.uptaskMaint();
                        TileMap.getMapLTD(_char);
                    } else if (_char.ctaskId == 17 && _char.ctaskIndex == 1 && bot.id == 3) {
                        Service.ServerMessage(_char, "Nhiệm vụ thất bại");
                    }
                    removeBot(bot, i);
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(600L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void removeBot(Bot bot, int i) {
        bot.robotPlayer.tileMap.Exit(bot.robotPlayer);
        arrBot.remove(i);
    }
    
    public void clear() {
        running = false;
        for (int i = 0; i < arrBot.size(); i++) {
            Bot aa = arrBot.get(i);
            aa.robotPlayer.tileMap.EXIT(aa.robotPlayer);
        }
        try {
            Thread.sleep(600L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        arrBot.clear();
        arrBot = null;
        threadRobot = null;
        robotTileMap = null;
        instance = null;
    }
}
