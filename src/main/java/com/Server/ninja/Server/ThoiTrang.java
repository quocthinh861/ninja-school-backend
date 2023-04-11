 package com.Server.ninja.Server;

public class ThoiTrang
{
    protected static final byte HAIR = 0;
    protected static final byte Body = 1;
    protected static final byte LEG = 2;
    protected static final byte WEA_PONE = 3;
    protected static final byte PP = 4;
    protected static final byte NAME = 5;
    protected static final byte HORSE = 6;
    protected static final byte RANK = 7;
    protected static final byte MAT_NA = 8;
    protected static final byte Bien_Hinh = 9;
    
    protected static void setThoiTrang(final Char _char, final short templateId) {
        switch (templateId) {
            case 795 ->  {
                _char.setThoiTrang[0] = 37;
                _char.setThoiTrang[1] = 38;
                _char.setThoiTrang[2] = 39;
            }
            case 796 ->  {
                _char.setThoiTrang[0] = 40;
                _char.setThoiTrang[1] = 41;
                _char.setThoiTrang[2] = 42;
            }
            case 797 ->  {
                _char.setThoiTrang[4] = 43;
            }
            case 798 ->  {
                _char.setThoiTrang[6] = 36;
            }
            case 799 ->  {
                _char.setThoiTrang[3] = 44;
            }
            case 800 ->  {
                _char.setThoiTrang[3] = 46;
            }
            case 801 ->  {
                _char.setThoiTrang[6] = 47;
            }
            case 802 ->  {
                _char.setThoiTrang[6] = 48;
            }
            case 803 ->  {
                _char.setThoiTrang[6] = 49;
            }
            case 804 ->  {
                _char.setThoiTrang[0] = 58;
                _char.setThoiTrang[1] = 59;
                _char.setThoiTrang[2] = 60;
            }
            case 805 ->  {
                _char.setThoiTrang[0] = 55;
                _char.setThoiTrang[1] = 56;
                _char.setThoiTrang[2] = 57;
            }
            case 813 ->  {
                _char.setThoiTrang[8] = 54;
            }
            case 814 ->  {
                _char.setThoiTrang[8] = 53;
            }
            case 815 ->  {
                _char.setThoiTrang[8] = 52;
            }
            case 816 ->  {
                _char.setThoiTrang[8] = 51;
            }
            case 817 ->  {
                _char.setThoiTrang[8] = 50;
            }
            case 825 ->  {
                _char.setThoiTrang[5] = 61;
            }
            case 826 ->  {
                _char.setThoiTrang[5] = 62;
            }
            case 827 ->  {
                _char.setThoiTrang[6] = 63;
            }
            case 830 ->  {
                _char.setThoiTrang[0] = 69;
                _char.setThoiTrang[1] = 70;
                _char.setThoiTrang[2] = 71;
            }
            case 831 ->  {
                _char.setThoiTrang[6] = 72;
            }
            case 968 ->  {
                _char.setThoiTrang[6] = 117;
            }
            case 972 ->  {
                _char.setThoiTrang[0] = 171;
                _char.setThoiTrang[1] = 172;
                _char.setThoiTrang[2] = 173;
            }
            case 973 ->  {
                _char.setThoiTrang[0] = 174;
                _char.setThoiTrang[1] = 175;
                _char.setThoiTrang[2] = 176;
            }
            case 988 ->  {
                _char.setThoiTrang[3] = 163;
            }
            case 989 ->  {
                _char.setThoiTrang[3] = 164;
            }
            case 990 ->  {
                _char.setThoiTrang[3] = 161;
            }
            case 991 ->  {
                _char.setThoiTrang[3] = 160;
            }
            case 992 ->  {
                _char.setThoiTrang[3] = 159;
            }
            case 993 ->  {
                _char.setThoiTrang[3] = 162;
            }
        }
    }
    
    protected static void removeThoiTrang(final Char _char, final short templateId) {
        switch (templateId) {
            case 795, 830, 805, 804, 973, 972, 796 -> {
                _char.setThoiTrang[0] = -1;
                _char.setThoiTrang[1] = -1;
                _char.setThoiTrang[2] = -1;
            }
            case 797 -> {
                _char.setThoiTrang[4] = -1;
            }
            case 799, 800,988,989,990,991,992 -> {
                _char.setThoiTrang[3] = -1;
            }
            case 798, 801, 802, 827, 831, 968, 803 -> {
                _char.setThoiTrang[6] = -1;
            }
            case 813, 814, 815, 816, 817 -> {
                _char.setThoiTrang[8] = -1;
            }
            case 825, 826 -> {
                _char.setThoiTrang[5] = -1;
            }
        }
    }
}
