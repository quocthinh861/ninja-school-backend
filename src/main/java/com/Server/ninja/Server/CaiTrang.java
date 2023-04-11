 package com.Server.ninja.Server;

public class CaiTrang {
    protected static short Head(Char c, short part) {
        if (c.typeCaiTrang != -1) {
            return c.getTypeCaiTrang();
        }
        if (c.head == 44 || c.head == 53 || c.head == 65) {
            part = c.head;
        }
        if (c.head == 96) {
            part = c.head;
        }
        if (part > 44) {
            return part;
        } else {
            return -1;
        }
    }

    protected static short Weapon(final short part) {
        return -1;
    }

    protected static short Body(Char c, short part) {
        if (c.typeCaiTrang != -1) {
            return (short) (c.getTypeCaiTrang() + 1);
        }
        if (c.head == 44 || c.head == 53 || c.head == 65) {
            part = c.head;
        }
        if (c.head == 96) {
            return 95;
        }
        if (part > 44) {
            return (short) (part + 1);
        } else {
            return -1;
        }
    }

    protected static short Leg(Char c, short part) {
        if (c.typeCaiTrang != -1) {
            return (short) (c.getTypeCaiTrang() + 2);
        }
        if (c.head == 44 || c.head == 53 || c.head == 65) {
            part = c.head;
        }
        if (c.head == 96) {
            return 94;
        }
        if (part > 44) {
            return (short) (part + 2);
        } else {
            return -1;
        }
    }

    protected static int checkUpgradeCT(Char c, int index) {
        return c.user.player.ItemCaiTrang[index].upgrade;
    }
}
