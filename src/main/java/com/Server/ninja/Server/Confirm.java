 package com.Server.ninja.Server;

import com.Server.ninja.option.ItemOption;
import com.Server.ninja.template.ItemTemplate;

public class Confirm {
    protected static void openConfirmID(final Char _char, final byte id) {
        switch (id) {
            case Byte.MIN_VALUE -> {
                if (_char.nvDV[4] != -1 && _char.cLevel >= 50 && _char.isHuman) {
                    Service.AlertMessage(_char, Text.get(0, 363), String.format(Text.get(0, 364), DanhVong.TILE_NV[_char.nvDV[1]]));
                    DanhVong.clearNV(_char);
                    break;
                }
            }
            case -127, -126 -> {
                final Item item = _char.ItemBody[14];
                if (item == null || item.upgrade >= 10) {
                    break;
                }
                if (_char.user.player.ItemBagSlotNull() < 1) {
                    Service.ServerMessage(_char, String.format(Text.get(0, 371), 1));
                    break;
                }
                final int pointMax = DanhVong.POINT_DV[item.upgrade];
                final int coin = DanhVong.COIN[item.upgrade];
                int gold = 0;
                int percent = DanhVong.PERCENT[item.upgrade];
                if (id == -126) {
                    gold = DanhVong.GOLD[item.upgrade];
                    percent *= 2;
                }
                if ((coin <= _char.user.player.xu || coin <= _char.user.player.yen) && gold <= _char.user.luong) {
                    if (_char.pointNon < pointMax || _char.pointVukhi < pointMax || _char.pointAo < pointMax || _char.pointLien < pointMax || _char.pointGangtay < pointMax || _char.pointNhan < pointMax || _char.pointQuan < pointMax || _char.pointNgocboi < pointMax || _char.pointGiay < pointMax || _char.pointPhu < pointMax) {
                        for (byte i = 0; i < 10; ++i) {
                            if (DanhVong.getPoint(_char, i) < pointMax) {
                                Service.ServerMessage(_char, String.format(Text.get(0, 354), pointMax, DanhVong.TILE_TYPE[i]));
                                return;
                            }
                        }
                    } else if (_char.user.player.ItemBagQuantity(DanhVong.DA_DV[item.upgrade]) < 10) {
                        Service.ServerMessage(_char, String.format(Text.get(0, 375), 10, item.upgrade + 1));
                    } else {
                        if (coin <= _char.user.player.yen) {
                            _char.user.player.upCoinLock(-coin, (byte) 0);
                        } else {
                            _char.user.player.upCoin(-coin, (byte) 0);
                        }
                        _char.user.player.upGold(-gold, (byte) 0);
                        _char.user.player.ItemBagUses(DanhVong.DA_DV[item.upgrade], 10);
                        if (Util.nextInt(1, Util.nextInt(70, 100)) <= percent) {
                            _char.ItemBody[14] = null;
                            final byte upgrade = (byte) (item.upgrade + 1);
                            Service.MELoadALL(_char);
                            Player.updateInfoPlayer(_char);
                            final Item itnew = new Item(ItemOption.arrOptionDefault(DanhVong.GAN[upgrade], (byte) 0), DanhVong.GAN[upgrade], 1, -1, true, (byte) 0, 5);
                            itnew.upgrade = upgrade;
                            _char.user.player.ItemBagAdd(itnew);
                            Service.ServerMessage(_char, Text.get(0, 374));
                        } else {
                            Service.ServerMessage(_char, Text.get(0, 373));
                        }
                    }
                } else {
                    Service.ServerMessage(_char, Text.get(0, 372));
                }
            }
            case -125 -> {
                BiKip.NangCap(_char);
            }
            case -124 -> {
                Clan clan2 = Clan.get(_char.cClanName);
                if (_char.cClanName != null && _char.tileMap != null && clan2.delayWarClan > 0) {
                    Clan clan1 = Clan.get(clan2.warClanname);
                    Char char1 = Client.getPlayer(clan1.main_name);

                    if (clan1 != null && _char.ctypeClan == 4) {
                        if (clan1.delayWarClan > 0) {
                            if (char1 != null) {
                                final WarClan warClan = WarClan.setWarClan();
                                final TileMap tile2 = warClan.maps[0].getSlotZone(_char);
                                final TileMap tile1 = warClan.maps[0].getSlotZone(char1);

                                // được mời thì là  _char và là phe trắng
                                _char.tileMap.Exit(_char);
                                _char.cx = (short) (tile2.map.template.goX + 210);
                                _char.cy = tile2.map.template.goY;
                                tile2.Join(_char);

                                // mời thì là char1 và là phe đen
                                char1.tileMap.Exit(char1);
                                char1.cx = tile1.map.template.goX;
                                char1.cy = tile1.map.template.goY;
                                tile1.Join(char1);

                                clan1.delayWarClan = 0;
                                clan2.delayWarClan = 0;

                                clan1.warId = warClan.warClanID;
                                clan2.warId = warClan.warClanID;

                                clan1.typeWar = 5;
                                clan2.typeWar = 4;

                                clan1.warClanname = clan2.name;
                                clan2.warClanname = clan1.name;

                                warClan.clanBlack = clan1;
                                warClan.clanWhite = clan2;

                                warClan.isWait = true;
                            } else {
                                Service.ServerMessage(_char, "Tộc trưởng đối phương đã offline");
                            }
                        } else {
                            Service.ServerMessage(_char, "Lời mời đã hết hạn");
                            return;
                        }
                    }
                }
            }
            case -123 -> {
                if (_char != null && _char.clan.clanManor != null) {
                    ClanManor manor = _char.clan.clanManor;
                    manor.memberAcceptManor.add(_char.cName);
                    final Map map = ClanManor.getMap(manor, (short) 80);
                    final TileMap tile = map.getSlotZone(_char);
                    if (tile == null) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 24));
                    } else {
                        _char.tileMap.Exit(_char);
                        _char.cx = tile.map.template.goX;
                        _char.cy = tile.map.template.goY;
                        tile.Join(_char);
                    }
                }
            }
        }
    }
}
