// 
// Decompiled by Procyon v0.5.36
// 

package com.Server.ninja.template;

import com.Server.ninja.Server.ItemTree;

public class MapTemplate
{
    public short mapID;
    public byte mapVersion;
    public String mapName;
    public String mapDescription;
    protected char[] mapData;
    protected short mapH;
    protected short mapW;
    public short[] WminX;
    public short[] WminY;
    public short[] WmaxX;
    public short[] WmaxY;
    public short[] WgoX;
    public short[] WgoY;
    public short[] WmapID;
    public short[] itemMap;
    public ItemTree[] ItemTreeFront;
    public ItemTree[] ItemTreeBehind;
    public ItemTree[] ItemTreeBetwen;
    public byte[] npcType;
    public short[] npcX;
    public short[] npcY;
    public byte[] npcID;
    protected short sumMob;
    public int[] mobID;
    public short[] mobLevel;
    public short[] mobX;
    public short[] mobY;
    public byte[] mobStatus;
    public byte[] moblevelBoss;
    public boolean[] arrisboss;
    public int[] mobRefreshTime;
    public byte tileID;
    public byte bgID;
    public byte typeMap;
    public byte numZone;
    public short goX;
    public short goY;
    
    public static int T_EMPTY = 0;
    public static int T_TOP = 2;
    public static int T_LEFT = 4;
    public static int T_RIGHT = 8;
    public static int T_TREE = 16;
    public static int T_WATERFALL = 32;
    public static int T_WATERFLOW = 64;
    public static int T_TOPFALL = 128;
    public static int T_OUTSIDE = 256;
    public static int T_DOWN1PIXEL = 512;
    public static int T_BRIDGE = 1024;
    public static int T_UNDERWATER = 2048;
    public static int T_SOLIDGROUND = 4096;
    public static int T_BOTTOM = 8192;
    public static int T_DIE = 16384;
    public static int T_HEBI = 32768;
    public static int T_BANG = 65536;
    public static int T_JUM8 = 131072;
    public static int T_NT0 = 262144;
    public static int T_NT1 = 524288;
    
    public int tmw;
    public int tmh;
    public int pxw;
    public int pxh;

    public char[] maps;
    public int[] types;
}
