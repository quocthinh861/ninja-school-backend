 package com.Server.ninja.Server;

import com.Server.ninja.option.ItemOption;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class Clan_ThanThu {
    public byte stars;
    public short iconThanThu;
    public short iconMini;
    public long time_aptrung;
    public Item items;
    public int curExp;
    public int maxExp;
    public int level;
    public byte type;

    public Clan_ThanThu() {
        this.time_aptrung = System.currentTimeMillis() + 60000L; //604800000L
        this.curExp = 0;
        this.maxExp = 0;
        this.stars = 0;
        this.level = 0;
    }

    protected static Clan_ThanThu parseThanThu(final String s) {
        try {
            final Clan_ThanThu thanThu = new Clan_ThanThu();
            final JSONArray jarr = (JSONArray) JSONValue.parseWithException(s);
            thanThu.type = Byte.parseByte(jarr.get(0).toString());
            thanThu.level = Integer.parseInt(jarr.get(1).toString());
            thanThu.stars = Byte.parseByte(jarr.get(2).toString());
            thanThu.curExp = Integer.parseInt(jarr.get(3).toString());
            thanThu.maxExp = Integer.parseInt(jarr.get(4).toString());
            thanThu.iconThanThu = Short.parseShort(jarr.get(5).toString());
            thanThu.iconMini = Short.parseShort(jarr.get(6).toString());
            thanThu.time_aptrung = Long.parseLong(jarr.get(7).toString());
            thanThu.items = Item.parseItem(jarr.get(8).toString());
            return thanThu;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static void apTrungFinish(final Clan clan) {
        for (int i = 0; i < clan.clan_thanThu.size(); i++) {
            Clan_ThanThu clanThanThu = clan.clan_thanThu.get(i);
            if (clanThanThu.time_aptrung > 0 && System.currentTimeMillis() > clanThanThu.time_aptrung) {
                clanThanThu.time_aptrung = 0;
                clanThanThu.level = 1;
                clanThanThu.stars = 1;
                clanThanThu.curExp = 0;
                clanThanThu.setMiniIcon();
                clanThanThu.setMaxExp();
                clanThanThu.setItem();
                clanThanThu.setIconThanThu();
            }
        }
    }

    public void setIconThanThu() {
        if (iconThanThu == 2506 && stars == 2) {
            iconThanThu = 2507;
        } else if (iconThanThu == 2507 && stars == 3) {
            iconThanThu = 2508;
        } else if (iconThanThu == 2502 && stars == 2) {
            iconThanThu = 2503;
        } else if (iconThanThu == 2503 && stars == 3) {
            iconThanThu = 2504;
        } else if (iconThanThu == 2493) {
            iconThanThu = 2506;
        } else if (iconThanThu == 2494) {
            iconThanThu = 2502;
        }
    }

    public void setMiniIcon() {
        if (iconMini == 2484 && stars == 2) {
            iconMini = 2485;
        } else if (iconMini == 2485 && stars == 3) {
            iconMini = 2486;
        } else if (iconMini == 2487 && stars == 2) {
            iconMini = 2488;
        } else if (iconMini == 2488 && stars == 3) {
            iconMini = 2489;
        } else if (iconMini == 2493) {
            iconMini = 2487;
        } else if (iconMini == 2494) {
            iconMini = 2484;
        }
    }

    protected void setItem() {
        if (items.template.id == 596) {
            items = new Item(ItemOption.arrOptionDefault((short) 587, (byte) 0), (short) 587, 1, -1, true, (byte) 0, 5);
        } else if (items.template.id == 601) {
            items = new Item(ItemOption.arrOptionDefault((short) 584, (byte) 0), (short) 584, 1, -1, true, (byte) 0, 5);
        }
    }

    public void upParamTT() {
        if (items.options != null) {
            for (short i = 0; i < items.options.size(); ++i) {
                final ItemOption itemOption = items.options.get(i);
                if (itemOption.optionTemplate.id == 102) {
                    final ItemOption itemOption2 = itemOption;
                    itemOption2.param = 150 * this.level * this.stars;
                } else if (itemOption.optionTemplate.id == 103) {
                    final ItemOption itemOption3 = itemOption;
                    itemOption3.param = 30 * this.level * this.stars;
                }
            }
        }
    }

    protected void upExpThanThu(Char _char, int curExp) {
        int expNew = this.curExp + curExp;
        if (expNew >= this.maxExp) {
            setLevel(_char);
            this.curExp = expNew - maxExp;
            setMaxExp();
            upParamTT();
        } else {
            this.curExp = expNew;
        }
    }

    protected void upStarsThanThu(Char _char) {
        if (stars >= 3) {
            Service.ServerMessage(_char, "Thần thú đã được tiến hóa hoá tối đa.");
        } else {
            int percent = Util.nextInt(100);
            if (stars == 1 && percent < 10) {
                stars++;
                items = new Item(ItemOption.arrOptionDefault((short) 587, (byte) 0), (short) (items.itemId + 1), 1, -1, true, (byte) 0, 5);
                upParamTT();
                setIconThanThu();
                setMiniIcon();
                Service.ServerMessage(_char, "Thần thú đã được tiến hóa lên cấp 2.");
                _char.clan.flush();
            } else if (stars == 2 && percent < 2) {
                stars++;
                items = new Item(ItemOption.arrOptionDefault((short) 587, (byte) 0), (short) (items.itemId + 1), 1, -1, true, (byte) 0, 5);
                upParamTT();
                setIconThanThu();
                setMiniIcon();
                Service.ServerMessage(_char, "Thần thú đã được tiến hóa lên cấp 3.");
                _char.clan.flush();
            } else {
                Service.ServerMessage(_char, "Tiến hoá thất bại.");
            }
        }
    }

    public void setLevel(Char _char) {
        if (level >= 120) {
            Service.ServerMessage(_char, " Thần thú đã đạt cấp độ tối đa.");
            return;
        }
        this.level += 1;
    }

    public void setMaxExp() {
        if (level <= 30) {
            maxExp = 1000;
        } else if (30 < level && level <= 70) {
            maxExp = 5000;
        } else if (level > 70) {
            maxExp = 10000;
        }
    }

    @Override
    public String toString() {
        String a = "[" + this.type;
        a = a + "," + this.level;
        a = a + "," + this.stars;
        a = a + "," + this.curExp;
        a = a + "," + this.maxExp;
        a = a + "," + this.iconThanThu;
        a = a + "," + this.iconMini;
        a = a + "," + this.time_aptrung;
        a = a + "," + this.items.toString();
        return a + "]";
    }
}
