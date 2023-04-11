package com.Server.ninja.Server;

import com.Server.ninja.template.MapTemplate;

public class MapServer
{
    protected static final short[] mapServerID;
    protected static Map[] mapServer;
    
    protected static void loadMapServer() {
        if (MapServer.mapServer == null) {
            MapServer.mapServer = new Map[MapServer.mapServerID.length];
        }
        else {
            for (byte i = 0; i < MapServer.mapServer.length; ++i) {
                if (MapServer.mapServer[i] != null) {
                    MapServer.mapServer[i].close();
                    MapServer.mapServer[i] = null;
                }
            }
        }
        for (byte i = 0; i < MapServer.mapServer.length; ++i) {
            final MapTemplate template = GameScr.mapTemplates[MapServer.mapServerID[i]];
            MapServer.mapServer[i] = new Map(template.mapID, (byte)20, template.numZone);
        }
        for (byte i = 0; i < MapServer.mapServer.length; ++i) {
            final Map map = MapServer.mapServer[i];
            if (map != null) {
                for (byte j = 0; j < map.tileMaps.length; ++j) {
                    final TileMap tileMap = map.tileMaps[j];
                    if (tileMap != null) {
                        tileMap.initWaypoint(0);
                    }
                }
            }
        }
        for (byte i = 0; i < MapServer.mapServer.length; ++i) {
            final Map map = MapServer.mapServer[i];
            if (map != null) {
                map.start();
            }
        }
    }
    
    protected static Map getMapServer(final int id) {
        for (byte i = 0; i < MapServer.mapServer.length; ++i) {
            final Map map = MapServer.mapServer[i];
            if (map != null && map.template.mapID == id) {
                return map;
            }
        }
        return null;
    }
    
    protected static boolean notCombat(final int mapId) {
        return mapId == 1 || mapId == 10 || mapId == 17 || mapId == 22 || mapId == 27 || mapId == 32 || mapId == 38 || mapId == 43 || mapId == 48 || mapId == 72 || mapId == 110 || mapId == 111;
    }
    
    protected static boolean notTrade(final int mapId) {
        return mapId == 110 || mapId == 111;
    }
    
    protected static boolean notLive(final int mapId) {
        return mapId == 111 || mapId == 112;
    }
    
    protected static void close() {
        for (byte i = 0; i < MapServer.mapServer.length; ++i) {
            final Map map = MapServer.mapServer[i];
            if (map != null) {
                map.close();
            }
        }
    }
    
    static {
        mapServerID = new short[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148 ,160,161};
        MapServer.mapServer = null;
    }
}
