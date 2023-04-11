 package com.Server.ninja.Server;

import java.util.ArrayList;

import com.Server.ninja.option.ItemOption;
import com.Server.ninja.template.MobTemplate;

import java.util.HashMap;

public class Mob {
    protected final Object LOCK;
    private TileMap tileMap;
    protected int fighttime;
    private final HashMap<Integer, Integer> vFight;
    protected static final byte TYPE_DUNG = 0;
    protected static final byte TYPE_DI = 1;
    protected static final byte TYPE_NHAY = 2;
    protected static final byte TYPE_LET = 3;
    protected static final byte TYPE_BAY = 4;
    protected static final byte TYPE_BAY_DAU = 5;
    protected static final byte TYPE_HIDE = 6;
    protected static MobTemplate[] arrMobTemplate;
    protected static final byte MA_INHELL = 0;
    protected static final byte MA_DEADFLY = 1;
    protected static final byte MA_STANDWAIT = 2;
    protected static final byte MA_ATTACK = 3;
    protected static final byte MA_STANDFLY = 4;
    protected static final byte MA_WALK = 5;
    protected static final byte MA_FALL = 6;
    protected static final byte MA_INJURE = 7;
    protected static final byte MA_NOTACTIVE = 8;
    protected static final byte MA_DISSAPPEAR = 9;
    protected static final byte MA_REVIVAL = 10;
    protected String flyString;
    protected int flyx;
    protected int flyy;
    protected int flyIndex;
    protected int hp;
    protected int maxHp;
    protected short x;
    protected short y;
    protected int frame;
    protected int dir;
    protected int dirV;
    protected int status;
    protected int p1;
    protected int p2;
    protected int p3;
    protected int xFirst;
    protected int yFirst;
    protected int vy;
    protected int exp;
    protected int w;
    protected int h;
    protected int hpInjure;
    protected int charIndex;
    protected int timeStatus;
    protected short mobId;
    protected boolean isx;
    protected boolean isy;
    protected boolean isDisable;
    protected int timeDisable;
    protected boolean isDontMove;
    protected int timeDontMove;
    protected boolean isFire;
    protected int timeFrie;
    protected boolean isIce;
    protected int timeIce;
    protected boolean isWind;
    protected int timeWind;
    protected ArrayList vMobMove;
    protected boolean isGo;
    protected String mobName;
    protected int templateId;
    protected short pointx;
    protected short pointy;
    protected Player cFocus;
    protected BuNhin bFocus;
    protected int dame;
    protected int dameMp;
    protected byte sys;
    protected int typeAtt;
    protected short levelBoss;
    protected short level;
    protected boolean isBoss;
    protected long timeStartDie;
    private int frameIndex;
    protected static Player interestChar;
    protected static Player CharAtt;
    protected static ArrayList vEggMonter;
    protected static boolean isdetrung;
    protected static long timewait;
    protected boolean removeWhenDie;
    protected boolean removeWhenDie_1;
    protected int RefreshTimeDie;
    protected boolean isBusyAttackSomeOne;
    protected long timeCurrent;
    private byte[] cou;
    protected Player injureBy;
    protected boolean injureThenDie;
    protected Mob mobToAttack;
    protected Player charToAttack;
    protected short idSkill_atk;
    protected byte typeAtk;
    protected byte typeTool;
    private byte indexlongden;
    protected int playerId;
    protected int dameSys;
    protected int dameBase;
    protected int ReactDame;

    protected Mob(final TileMap tileMap, final short mobId, final int templateId, final byte sys, final int hp, final int level, final short pointx, final short pointy, final byte status, final byte levelBoss, final boolean isBos, final int RefreshTimeDie) {
        this.LOCK = new Object();
        this.tileMap = null;
        this.fighttime = 0;
        this.dir = 1;
        this.dirV = 1;
        this.isDisable = false;
        this.timeDisable = 0;
        this.isDontMove = false;
        this.timeDontMove = 0;
        this.isFire = false;
        this.timeFrie = 0;
        this.isIce = false;
        this.timeIce = 0;
        this.isWind = false;
        this.timeWind = 0;
        this.vMobMove = new ArrayList<>();
        this.isBusyAttackSomeOne = true;
        this.cou = new byte[]{-1, 1};
        this.indexlongden = -1;
        this.playerId = -1;
        this.dameSys = 0;
        this.dameBase = 0;
        this.ReactDame = 0;
        this.tileMap = tileMap;
        this.sys = sys;
        this.mobId = mobId;
        this.templateId = templateId;
        this.maxHp = hp;
        this.hp = hp;
        this.level = (short) level;
        this.pointx = pointx;
        this.x = pointx;
        this.xFirst = pointx;
        this.pointy = pointy;
        this.y = pointy;
        this.yFirst = pointy;
        this.status = status;
        this.levelBoss = levelBoss;
        this.isBoss = isBos;
        this.RefreshTimeDie = RefreshTimeDie;
        this.removeWhenDie = (RefreshTimeDie == -1);
        this.removeWhenDie_1 = false;
        if (this.levelBoss == 1) {
            final int n = this.maxHp * 10;
            this.maxHp = n;
            this.hp = n;
        } else if (this.levelBoss == 2) {
            final int n2 = this.maxHp * 100;
            this.maxHp = n2;
            this.hp = n2;
        } else if (this.levelBoss == 3) {
            final int n3 = this.maxHp * 200;
            this.maxHp = n3;
            this.hp = n3;
        }
        if (this.templateId == 138) {
            this.ReactDame = 1000;
        }
        this.vFight = new HashMap<Integer, Integer>();
        this.dameSys = this.level * (this.level / (3 + this.level / 20));
    }

    protected void startDie() {
        this.hp = 0;
        this.timeStartDie = System.currentTimeMillis();
        if (this.removeWhenDie_1) {
            this.tileMap.aMob.remove(this);
        }
        if (this.injureBy != null) {
            this.injureThenDie = true;
        } else {
            this.injureThenDie = true;
            this.hp = 0;
            this.status = 0;
            this.p1 = -5;
            this.p2 = -this.dir << 2;
            this.p3 = 0;
        }
    }

    public void ClearFight() {
        synchronized (this.LOCK) {
            this.vFight.clear();
        }
    }

    public int sortPlayerFight() {
        int idN = 0;
        int dameMax = 0;
        for (final int value : this.vFight.keySet()) {
            final Player player = Client.getPlayer(value);
            if (player != null) {
                if (this.vFight.get(value) <= dameMax) {
                    continue;
                }
                dameMax = this.vFight.get(value);
                idN = player.playerId;
            }
        }
        return idN;
    }

    public void CharFight(final int charId, final int dame) {
        if (!this.vFight.containsKey(charId)) {
            this.vFight.put(charId, dame);
        } else {
            int damenew = this.vFight.get(charId);
            damenew += dame;
            this.vFight.replace(charId, damenew);
        }
    }

    public void removeCharFight(final int charId) {
        synchronized (this.LOCK) {
            if (this.vFight.containsKey(charId)) {
                this.vFight.remove(charId);
            }
        }
    }

    public boolean isCharFight(final int charId) {
        return this.vFight.containsKey(charId);
    }

    protected static short itemMob(final Item item) {
        try {
            switch (item.template.id) {
                case 246: {
                    return 70;
                }
                case 419: {
                    return 122;
                }
                case 568: {
                    return 205;
                }
                case 569: {
                    return 206;
                }
                case 570: {
                    return 207;
                }
                case 571: {
                    return 208;
                }
                case 583: {
                    return 211;
                }
                case 584: {
                    return 212;
                }
                case 585: {
                    return 213;
                }
                case 586: {
                    return 214;
                }
                case 587: {
                    return 215;
                }
                case 588: {
                    return 216;
                }
                case 589: {
                    return 217;
                }
                case 742: {
                    return 229;
                }
                case 744: {
                    return 229;
                }
                case 772: {
                    return 234;
                }
                case 773: {
                    return 234;
                }
                case 781: {
                    return 229;
                }
                case 832: {
                    return 238;
                }
                case 833:
                case 842:
                case 904:
                case 913:
                case 922:
                case 931:
                case 940:
                case 949: {
                    return 247;
                }
                case 834:
                case 843:
                case 905:
                case 914:
                case 923:
                case 932:
                case 941:
                case 950: {
                    return 248;
                }
                case 844:
                case 835:
                case 906:
                case 915:
                case 924:
                case 933:
                case 942:
                case 951:{
                    return 249;
                }
                case 845:
                case 836:
                case 907:
                case 916:
                case 925:
                case 934:
                case 943:
                case 952:{
                    return 250;
                }
                case 846:
                case 837:
                case 908:
                case 917:
                case 926:
                case 935:
                case 944:
                case 953:{
                    return 251;
                }
                case 847:
                case 838:
                case 909:
                case 918:
                case 927:
                case 936:
                case 945:
                case 954:{
                    return 252;
                }
                case 848:
                case 839:
                case 910:
                case 919:
                case 928:
                case 937:
                case 946:
                case 955:{
                    return 253;
                }
                case 849:
                case 840:
                case 911:
                case 920:
                case 929:
                case 938:
                case 947:
                case 956:{
                    return 254;
                }
                case 850:
                case 841:
                case 912:
                case 921:
                case 930:
                case 939:
                case 948:
                case 957:{
                    return 246;
                }
            }
        } catch (Exception ex) {
        }
        return -1;
    }

    protected static void MobMeAtk(final Char _char, final Mob mob, final Char player) {
        try {
            for (int i = 0; i < _char.tileMap.numPlayer; ++i) {
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null) {
                    Service.ThuNuoiAttack(_char.tileMap.aCharInMap.get(i), _char.charID, mob.mobId, (short) 0, (byte) 1, (byte) 1, (byte) 0, 0);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Item item = _char.ItemBody[10];
        int dameThuNuoi = 0;
        if (item.itemId >= 584 && item.itemId <= 589) {
            for (int i = 0; i < item.options.size(); i++) {
                ItemOption option = item.options.get(i);
                if (option.optionTemplate.id == 102) {
                    dameThuNuoi = option.param;
                }
            }
        } else {
            dameThuNuoi = _char.cDame(mob.sys, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0) * 20 / 100;
        }
        SkillUse.AttackMob(_char.tileMap, _char, _char, mob, Util.nextInt((int) dameThuNuoi * 99 / 100, dameThuNuoi), false, (byte) 1);
    }

    protected static void MobAttack(final Mob mob) {
        if (!mob.isDisable && !mob.isIce && !mob.isWind) {
            int time = 2500 - mob.level * Util.nextInt(5, 20);
            if (time < 500) {
                time = 500;
            }
            mob.fighttime = time;
            int dame = mob.dameSys;
            if (mob.levelBoss == 1) {
                dame *= 2;
            } else if (mob.levelBoss == 2) {
                dame *= 3;
            } else if (mob.levelBoss == 3) {
                dame *= 4;
            }
            if (mob.isBoss) {
                dame *= 4;
            }
            final MobTemplate template = Mob.arrMobTemplate[mob.templateId];
            for (short i = 0; i < mob.tileMap.aCharInMap.size() && dame > 0; ++i) {
                final Char player = mob.tileMap.aCharInMap.get(i);
                if (player != null && player.statusMe != 14 && !player.isInvisible && !player.isNhanban && (mob.playerId == -1 || mob.playerId == player.user.player.playerId) && ((!mob.isMobWhite() && !mob.isMobBlack()) || (mob.isMobBlack() && player.cTypePk != 5) || (mob.isMobWhite() && player.cTypePk != 4))) {
                    short dx = 80;
                    short dy = 2;
                    if (template.type > 3) {
                        dy = 80;
                    }
                    if (mob.isBoss) {
                        dx = 110;
                    }
                    if (player.user != null && mob.isCharFight(player.charID)) {
                        dx = 350;
                        dy = 280;
                    }
                    if (Math.abs(player.cx - mob.x) < dx && Math.abs(player.cy - mob.y) < dy) {
                        final int dameAttack = dame;
                        MobAttackPlayer(mob, dameAttack, player);
                        dame -= 500;
                    }
                }
            }
        }
    }

    protected static void MobAttackPlayer(final Mob mob, int dameAttack, final Char player) {
        for (short k = 0; k < mob.tileMap.aBuNhin.size(); ++k) {
            final BuNhin bunhin = mob.tileMap.aBuNhin.get(k);
            if (bunhin != null && bunhin.charID == player.charID && Math.abs(bunhin.x - player.cx) <= bunhin.dx && Math.abs(bunhin.y - player.cy) <= bunhin.dy) {
                final BuNhin buNhin = bunhin;
                buNhin.hp -= dameAttack;
                for (int j = 0; j < mob.tileMap.numPlayer; ++j) {
                    if (mob.tileMap.aCharInMap.get(j).user != null && mob.tileMap.aCharInMap.get(j).user.session != null) {
                        Service.attackBunhin(player, mob.mobId, (short) 0, (short) (-1), (byte) (-1), (byte) (-1));
                    }
                }
                if (bunhin.hp <= 0) {
                    mob.tileMap.aBuNhin.remove(k);
                    for (int j = 0; j < mob.tileMap.numPlayer; ++j) {
                        if (mob.tileMap.aCharInMap.get(j).user != null && mob.tileMap.aCharInMap.get(j).user.session != null) {
                            Service.ClearBunhin(mob.tileMap.aCharInMap.get(j), k);
                        }
                    }
                }
                return;
            }
        }
        if (mob.sys == 1) {
            dameAttack -= player.ResFire();
        } else if (mob.sys == 2) {
            dameAttack -= player.ResIce();
        } else if (mob.sys == 3) {
            dameAttack -= player.ResWind();
        }
        dameAttack += mob.dameBase;
        dameAttack -= player.cdameDown();
        if (dameAttack < 1) {
            dameAttack = 1;
        }
        if (player.cMiss() > Util.nextInt(1, Util.nextInt(100, 10000))) {
            dameAttack = 0;
        }
        int dameMp = 0;
        if (player.cMP * 100 / player.cMaxMP() > 10) {
            final Effect effect = player.getEffType((byte) 6);
            if (effect != null) {
                final int Mpold = player.cMP;
                final int newMp = dameAttack * effect.param / 100;
                player.upMP(-newMp);
                dameAttack -= (dameMp = Mpold - player.cMP);
            }
        }
        if (player.cHP < dameAttack) {
            dameAttack = player.cHP;
        }
        player.upHP(-dameAttack);
        if ((player.tileMap.mapID == 84) && Util.nextInt(10) < 7) {
            int timeFire = 20;
            timeFire -= player.cFireDownTime();
            player.upHP(-(Util.nextInt(500, 1000)));
            Char.setEffect(player, (byte) 5, -1, timeFire, (short) 0, null, (byte) 0);
        }
        if ((player.tileMap.mapID == 85) && Util.nextInt(10) < 7) {
            int timeWind = 20;
            timeWind -= player.cWindDownTime();
            Char.setEffect(player, (byte) 7, -1, timeWind, (short) 0, null, (byte) 0);
        }
        if ((player.tileMap.mapID == 86) && Util.nextInt(10) < 7) {
            int timeIce = 20;
            timeIce -= player.cIceDownTime();
            Char.setEffect(player, (byte) 6, -1, timeIce, (short) 0, null, (byte) 0);
        }
        if (player.tileMap.mapID == 90) {
            int timeEff = 7;
            if (Util.nextInt(10) < 5) {
                Char.setEffect(player, (byte) 6, -1, timeEff, (short) 0, null, (byte) 0);
            } else if (Util.nextInt(10) < 5) {
                Char.setEffect(player, (byte) 7, -1, timeEff, (short) 0, null, (byte) 0);
            } else if (Util.nextInt(10) < 5) {
                Char.setEffect(player, (byte) 5, -1, timeEff, (short) 0, null, (byte) 0);
            }
        }
        if (player.PramSkillTotal(69) > Util.nextInt(100)) {
            final int timeIce = player.PramSkillTotal(70);
            upIce(mob, true, timeIce);
        }
        if (player.user != null && player.user.session != null) {
            Service.MobAttackMe(player, mob.mobId, dameAttack, dameMp, (short) (-1), (byte) (-1), (byte) (-1));
        }
        for (short i = 0; i < mob.tileMap.numPlayer; ++i) {
            if (mob.tileMap.aCharInMap.get(i).user != null && mob.tileMap.aCharInMap.get(i).user.session != null && mob.tileMap.aCharInMap.get(i).charID != player.charID) {
                Service.MobAttackPlayer(mob.tileMap.aCharInMap.get(i), mob.mobId, player, (short) (-1), (byte) (-1), (byte) (-1));
            }
        }
        if (player.cHP <= 0) {
            player.upDie((byte) 0);
        }
    }

    protected static void LiveMob(final Mob mob, final byte sys, final byte levelBoss, final int changeHP, final int changeSys) {
        mob.ClearFight();
        mob.sys = sys;
        mob.levelBoss = levelBoss;
        mob.status = 5;
        mob.injureThenDie = false;
        if (changeHP != -1) {
            mob.maxHp = changeHP;
            mob.hp = changeHP;
        } else {
            final int hp = Mob.arrMobTemplate[mob.templateId].hp;
            mob.maxHp = hp;
            mob.hp = hp;
            if (mob.levelBoss == 1) {
                final int n = mob.maxHp * 10;
                mob.maxHp = n;
                mob.hp = n;
            } else if (mob.levelBoss == 2) {
                final int n2 = mob.maxHp * 100;
                mob.maxHp = n2;
                mob.hp = n2;
            } else if (mob.levelBoss == 3) {
                final int n3 = mob.maxHp * 200;
                mob.maxHp = n3;
                mob.hp = n3;
            }
        }
        if (changeSys != -1) {
            mob.dameSys = changeSys;
        }
        for (short i = 0; i < mob.tileMap.aCharInMap.size(); ++i) {
            if (mob.tileMap.aCharInMap.get(i).user != null && mob.tileMap.aCharInMap.get(i).user.session != null) {
                Service.MobLive(mob.tileMap.aCharInMap.get(i), mob);
            }
        }
    }

    protected static void upDisable(final Mob mob, final boolean isDisable, final int timeDisable) {
        mob.isDisable = isDisable;
        mob.timeDisable = timeDisable;
        try {
            for (short i = 0; i < mob.tileMap.numPlayer; ++i) {
                final Char _char = mob.tileMap.aCharInMap.get(i);
                if (_char != null && _char.user != null && _char.user.session != null) {
                    Service.mobIsDisable(_char, mob.mobId, isDisable);
                }
            }
        } catch (Exception ex) {
        }
    }

    protected static void upMove(final Mob mob, final boolean isMove, final int timeMove) {
        mob.isDontMove = isMove;
        mob.timeDontMove = timeMove;
        try {
            for (short i = 0; i < mob.tileMap.numPlayer; ++i) {
                final Char _char = mob.tileMap.aCharInMap.get(i);
                if (_char != null && _char.user != null && _char.user.session != null) {
                    Service.mobIsMove(_char, mob.mobId, isMove);
                }
            }
        } catch (Exception ex) {
        }
    }

    protected static void upFire(final Mob mob, final boolean isFire, final int timeFire) {
        mob.isFire = isFire;
        mob.timeFrie = timeFire;
        try {
            for (short i = 0; i < mob.tileMap.numPlayer; ++i) {
                final Char _char = mob.tileMap.aCharInMap.get(i);
                if (_char != null && _char.user != null && _char.user.session != null) {
                    Service.mobIsFire(_char, mob.mobId, isFire);
                }
            }
        } catch (Exception ex) {
        }
    }

    protected static void upIce(final Mob mob, final boolean isIce, final int timeIce) {
        mob.isIce = isIce;
        mob.timeIce = timeIce;
        try {
            for (short i = 0; i < mob.tileMap.numPlayer; ++i) {
                final Char _char = mob.tileMap.aCharInMap.get(i);
                if (_char != null && _char.user != null && _char.user.session != null) {
                    Service.mobIsIce(_char, mob.mobId, isIce);
                }
            }
        } catch (Exception ex) {
        }
    }

    protected static void upWind(final Mob mob, final boolean isWind, final int timeWind) {
        mob.isWind = isWind;
        mob.timeWind = timeWind;
        try {
            for (short i = 0; i < mob.tileMap.numPlayer; ++i) {
                final Char _char = mob.tileMap.aCharInMap.get(i);
                if (_char != null && _char.user != null && _char.user.session != null) {
                    Service.mobIsWind(_char, mob.mobId, isWind);
                }
            }
        } catch (Exception ex) {
        }
    }

    protected boolean isMobBlack() {
        return this.templateId == 96 || this.templateId == 99;
    }

    protected boolean isMobWhite() {
        return this.templateId == 97 || this.templateId == 98;
    }

    static {
        Mob.vEggMonter = new ArrayList();
        Mob.isdetrung = false;
    }
}
