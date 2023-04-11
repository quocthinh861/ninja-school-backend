package com.Server.ninja.Server;

import com.Server.ninja.option.ItemOption;

public class ViThu {
    protected static final int max10 = 20;
    protected static final int max30 = 70;
    protected static final int max50 = 90;
    protected static final int max70 = 110;
    protected static final int max100 = 145;
    public static ItemOption[] viThuOptionDefault(final short itemTemplateId) {
        int[] option = new int[] {5,5,5,5};
        if (itemTemplateId >= 833 && itemTemplateId <= 850) {
//            if (Util.nextInt(1,4) == 1) {
//                option = new int[] {5,4,4,4};
//            } else if (Util.nextInt(1,4) == 2) {
//                option = new int[] {4,5,4,4};
//            } else if (Util.nextInt(1,4) == 3) {
//                option = new int[] {4,4,5,4};
//            } else {
//                option = new int[] {4,4,4,5};
//            }
            return new ItemOption[] { new ItemOption(140, 0), new ItemOption(150, 1000)
                    , new ItemOption(144, option[0]), new ItemOption(146, option[1])
                    , new ItemOption(147, option[2]), new ItemOption(145, option[3])
                    , new ItemOption(154, option[1]*10), new ItemOption(6, option[1]*10)
                    , new ItemOption(87, option[0]*10), new ItemOption(148, option[3]*10)
                    , new ItemOption(149, option[3]*10), new ItemOption(151, 0) };
        } else if (itemTemplateId >= 904 && itemTemplateId <= 921) {
//            int per = Util.nextInt(1,6);
//            if (per == 1) {
//                option = new int[] {5,5,4,4};
//            } else if (per == 2) {
//                option = new int[] {5,4,5,4};
//            } else if (per == 3) {
//                option = new int[] {5,4,4,5};
//            } else if (per == 4) {
//                option = new int[] {4,5,5,4};
//            } else if (per == 5) {
//                option = new int[] {4,5,4,5};
//            } else if (per == 6) {
//                option = new int[] {4,4,5,5};
//            }
            return new ItemOption[] { new ItemOption(140, 0), new ItemOption(150, 1500)
                    , new ItemOption(144, option[0]), new ItemOption(146, option[1])
                    , new ItemOption(147, option[2]), new ItemOption(145, option[3])
                    , new ItemOption(154, option[1]*10), new ItemOption(6, option[1]*10)
                    , new ItemOption(87, option[0]*10), new ItemOption(148, option[3]*10)
                    , new ItemOption(149, option[3]*10), new ItemOption(151, 0) };
        } else if (itemTemplateId >= 922 && itemTemplateId <= 939) {
//            int per = Util.nextInt(1,3);
//            if (per == 1) {
//                option = new int[] {5,5,5,4};
//            } else if (per == 2) {
//                option = new int[] {5,5,4,5};
//            } else if (per == 3) {
//                option = new int[] {4,5,5,5};
//            }
            return new ItemOption[] { new ItemOption(140, 0), new ItemOption(150, 2000)
                    , new ItemOption(144, option[0]), new ItemOption(146, option[1])
                    , new ItemOption(147, option[2]), new ItemOption(145, option[3])
                    , new ItemOption(154, option[1]*10), new ItemOption(6, option[1]*10)
                    , new ItemOption(87, option[0]*10), new ItemOption(148, option[3]*10)
                    , new ItemOption(149, option[3]*10), new ItemOption(151, 0) };
        } else if (itemTemplateId >= 940 && itemTemplateId <= 957) {
            option = new int[] {6,6,6,6};
            return new ItemOption[] { new ItemOption(140, 0), new ItemOption(150, 3000)
                    , new ItemOption(144, option[0]), new ItemOption(146, option[1])
                    , new ItemOption(147, option[2]), new ItemOption(145, option[3])
                    , new ItemOption(154, option[1]*10), new ItemOption(6, option[1]*10)
                    , new ItemOption(87, option[0]*10), new ItemOption(148, option[3]*10)
                    , new ItemOption(149, option[3]*10), new ItemOption(151, 0) };
        } else {
            return null;
        }
    }
    public static void UseItem(final Char _char, final Item item) {
        if (_char.arrItemViThu[4].expires > 0) {
            Service.ServerMessage(_char, "Phải sử dụng vĩ thú vĩnh viễn mới có thể dùng.");
            return;
        }
        if (_char.arrItemViThu[4].upgrade >= 100) {
            Service.ServerMessage(_char, "Vĩ thú đã đạt cấp độ tối đa.");
            return;
        }
        switch (item.template.id) {
            case 894 ->                 {
                    Item vt = _char.arrItemViThu[4];
                    for (int i = 0; i < vt.options.size(); i++) {
                        if (vt.options.get(i).optionTemplate.id == 151) {
                            int up = Util.nextInt(1,10);
                            if (vt.options.get(i).param >= 9999) {
                                Service.ServerMessage(_char, "Vĩ thú đã đạt kinh nghiệm tối đa.");
                                break;
                            } else {
                                if (vt.options.get(i).param + up > 9999) {
                                    vt.options.get(i).param = 9999;
                                } else {
                                    vt.options.get(i).param += up;
                                }
                                Service.ServerMessage(_char, "Kinh nghiệm vĩ thú tăng lên " + up + ".");
                                Player.LoadViThu(_char);
                                _char.user.player.ItemBagUse((byte) item.indexUI, 1);
                            }
                        }
                    }                      }
            case 895 ->                 {
                    Item vt = _char.arrItemViThu[4];
                    for (int i = 0; i < vt.options.size(); i++) {
                        if (vt.options.get(i).optionTemplate.id == 151) {
                            if (vt.options.get(i).param >= 9999) {
                                Service.ServerMessage(_char, "Vĩ thú đã đạt kinh nghiệm tối đa.");
                                break;
                            } else {
                                if (vt.options.get(i).param + 200 > 9999) {
                                    vt.options.get(i).param = 9999;
                                } else {
                                    vt.options.get(i).param += 200;
                                }
                                Service.ServerMessage(_char, "Kinh nghiệm vĩ thú tăng lên 200.");
                                Player.LoadViThu(_char);
                                _char.user.player.ItemBagUse((byte) item.indexUI, 1);
                            }
                        }
                    }                      }
            case 896 ->                 {
                    Item vt = _char.arrItemViThu[4];
                    for (int i = 0; i < vt.options.size(); i++) {
                        if (vt.options.get(i).optionTemplate.id == 151) {
                            if (vt.options.get(i).param >= 9999) {
                                Service.ServerMessage(_char, "Vĩ thú đã đạt kinh nghiệm tối đa.");
                                break;
                            } else {
                                if (vt.options.get(i).param + 300 > 9999) {
                                    vt.options.get(i).param = 9999 ;
                                } else {
                                    vt.options.get(i).param += 300 ;
                                }
                                Service.ServerMessage(_char, "Kinh nghiệm vĩ thú tăng lên 300.");
                                Player.LoadViThu(_char);
                                _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                            }
                        }
                    }                      }
            case 897 ->                 {
                    Item vt = _char.arrItemViThu[4];
                    int exp = 0;
                    for (int i = 0; i < vt.options.size(); i++) {
                        if (vt.options.get(i).optionTemplate.id == 151) {
                            exp = vt.options.get(i).param;
                            break;
                        }
                    }       if (exp < 9999) {
                        Service.ServerMessage(_char, "Khinh nghiệm vĩ thú chưa đủ.");
                        return;
                    }       if (ViThu.checkTN(vt) && (vt.upgrade == 10 || vt.upgrade == 30 || vt.upgrade == 50 || vt.upgrade == 70 || vt.upgrade == 100)) {
                        Service.ServerMessage(_char, "Phải nâng max tiềm năng trước.");
                        return;
                    }       if (vt.upgrade >= 49) {
                        Service.ServerMessage(_char, "Hãy đến Kensinto để tiến hoá vĩ thú trước.");
                        return;
                    }       vt.upLVViThu((byte) 1);
                    Service.ServerMessage(_char, "Vĩ thú đã được tăng cấp.");
                    Player.LoadViThu(_char);
                    _char.user.player.ItemBagUse((byte)item.indexUI, 1);
                }
            default -> {
            }
        }
    }
    protected static void useEgg(Char _char, Item item, byte indexUI) {
        if (item.itemId >= 899 && item.itemId <= 902) {
            if (!_char.isHuman) {
                Service.ServerMessage(_char, Text.get(0, 310));
            } else if (_char.user.player.ItemBagSlotNull() < 1) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
            }
            short idItem = -1;
            switch (item.itemId) {
                case 899 -> idItem = 833;
                case 900 -> idItem = 904;
                case 901 -> idItem = 922;
                case 902 -> idItem = 940;
                default -> {
                }
            }
            if (Util.nextInt(120) < 10) {
                idItem += 1;
            } else if (Util.nextInt(120) < 10) {
                idItem += 2;
            } else if (Util.nextInt(120) < 10) {
                idItem += 3;
            } else if (Util.nextInt(120) < 10) {
                idItem += 4;
            } else if (Util.nextInt(120) < 10) {
                idItem += 5;
            } else if (Util.nextInt(120) < 10) {
                idItem += 6;
            } else if (Util.nextInt(120) < 10) {
                idItem += 7;
            } else if (Util.nextInt(120) < 5) {
                idItem += 8;
            } else if (Util.nextInt(120) < 5) {
                idItem += 9;
            } else if (Util.nextInt(120) < 5) {
                idItem += 10;
            } else if (Util.nextInt(120) < 5) {
                idItem += 11;
            } else if (Util.nextInt(120) < 5) {
                idItem += 12;
            } else if (Util.nextInt(120) < 5) {
                idItem += 13;
            } else if (Util.nextInt(120) < 5) {
                idItem += 14;
            } else if (Util.nextInt(120) < 5) {
                idItem += 15;
            } else if (Util.nextInt(120) < 5) {
                idItem += 16;
            } else if (Util.nextInt(120) < 5) {
                idItem += 17;
            }
            Item itemUp = new Item(ViThu.viThuOptionDefault(idItem), idItem , 1, (int) item.expires, false, (byte) 0, 5);
            if (Util.nextInt(120) < 20 && item.expires > 0) {
                itemUp.upgrade = 10;
            } else if (Util.nextInt(120) < 5 && item.expires > 0) {
                itemUp.upgrade = 30;
            } else {
                itemUp.upgrade = 1;
            }
            _char.user.player.ItemBagAdd(itemUp);
            _char.user.player.ItemBagUse(indexUI, 1);
        } else if (item.itemId == 903) {
            if (!_char.isHuman) {
                Service.ServerMessage(_char, Text.get(0, 310));
            } else if (_char.user.player.ItemBagSlotNull() < 1) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
            } else if (_char.user.player.yen < 1000000) {
                GameCanvas.startOKDlg(_char.user.session, "Không đủ yên để giải phong ấn");
            } else {
                _char.user.player.upCoinLock(-1000000L, (byte) 1);
                short idItem = 899;
                if (Util.nextInt(120) < 10) {
                    idItem = 900;
                } else if (Util.nextInt(120) < 5) {
                    idItem = 901;
                } else if (Util.nextInt(120) == 7) {
                    idItem = 902;
                }
                Item itemUp = new Item(null, idItem, 1, 604800, false, (byte) 0, 5);
                if (Util.nextInt(120) < 3) {
                    itemUp.expires = -1;
                    Client.chatServer("Server ", String.format("Chúc mừng %s đã phá phong ấn nhận được %s vĩnh viễn", _char.cName, itemUp.template.name));
                }
                _char.user.player.ItemBagAdd(itemUp);
                _char.user.player.ItemBagUse(indexUI, 1);
            }
        }
    }
    protected static void Evolution(Char _char) {
        Item vt = _char.arrItemViThu[4];
        if (_char.arrItemViThu == null || vt.expires > 0) {
            Service.ServerMessage(_char, "Phải sử dụng vĩ thú vĩnh viễn mới có thể tiến hoá.");
            return;
        } else if (vt.upgrade < 49) {
            Service.ServerMessage(_char, "Chưa đủ level để tiến hoá");
            return;
        }
        int exp = 0;
        for (int i = 0; i < vt.options.size(); i++) {
            if (vt.options.get(i).optionTemplate.id == 151) {
                exp = vt.options.get(i).param;
                break;
            }
        }
        if (exp < 9999) {
            Service.ServerMessage(_char, "Khinh nghiệm vĩ thú chưa đủ.");
            return;
        }
        vt.upLVViThu((byte) 1);
        Item vtNew = new Item(null, (short) (vt.template.id + 9), 1, (int) vt.expires, true, (byte) 0, vt.saleCoinLock);
        vtNew.options = vt.options;
        _char.arrItemViThu[4] = vtNew;
        Service.ServerMessage(_char, "Vĩ thú đã được tiến hoá.");
        _char.user.player.upGold(-10000, (byte) 2);
        Player.LoadViThu(_char);
    }
    protected static boolean checkTN(Item item) {
        int tn = 0;
        for (int i = 0; i < item.options.size(); i++) {
            if (item.options.get(i).optionTemplate.id == 144) {
                tn = item.options.get(i).param;
            }
        }
        return tn != getMax(item.upgrade);
    }
    protected static int getMax(int level) {
        int max = 0;
        switch (level) {
            case 10 -> max = ViThu.max10;
            case 30 -> max = ViThu.max30;
            case 50 -> max = ViThu.max50;
            case 70 -> max = ViThu.max70;
            case 100 -> max = ViThu.max100;
            default -> {
            }
        }
        return max;
    }
}
