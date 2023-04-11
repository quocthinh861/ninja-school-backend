package com.Server.ninja.Server;

import java.util.Random;

import com.Server.ninja.template.MapTemplate;
import com.Server.ninja.template.MobTemplate;
import lombok.val;

public class Menu {
    private static final int[] arrLevelGift;
    protected static void Menu(final Char _char, final byte npcId, byte menuId, final byte optionId) {
        Util.log("npcId:" + npcId + " menuId:" + menuId + " optionId:" + optionId);
        final Npc npc = TileMap.NPCNear(_char, npcId);
        if (Task.isTaskNPC(_char, npcId) && npc != null) {
            if (_char.ctaskIndex == -1) {
                --menuId;
                if (menuId == -1) {
                    Task.Task(_char, npcId);
                }
            } else if (Task.isFinishTask(_char)) {
                --menuId;
                if (menuId == -1) {
                    Task.FinishTask(_char, npcId);
                }
            } else if (_char.ctaskId == 1) {
                --menuId;
                if (menuId == -1) {
                    Task.doTask(_char, npcId, menuId, optionId);
                }
            } else if (_char.ctaskId == 7) {
                --menuId;
                if (menuId == -1) {
                    Task.doTask(_char, npcId, menuId, optionId);
                }
            } else if (_char.ctaskId == 13) {
                --menuId;
                if (menuId == -1) {
                    switch (_char.ctaskIndex) {
                        case 1 -> {
                            final Map map = MapServer.getMapServer(56);
                            final TileMap tile = map.getFreeArea();
                            if (tile == null) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 24));
                            } else {
                                _char.tileMap.Exit(_char);
                                _char.cx = tile.map.template.goX;
                                _char.cy = tile.map.template.goY;
                                tile.Join(_char);
                                Robot.gI().callBot(_char.user.player,0,(short) 950,(short) 240, (byte) 3);
                            }
                        }
                        case 2 -> {// Haru
                            final Map map = MapServer.getMapServer(0);
                            final TileMap tile = map.getFreeArea();
                            if (tile == null) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 24));
                            } else {
                                _char.tileMap.Exit(_char);
                                _char.cx = tile.map.template.goX;
                                _char.cy = tile.map.template.goY;
                                tile.Join(_char);
                                Robot.gI().callBot(_char.user.player,1,(short) 950,(short) 240, (byte) 3);
                            }
                        }
                        case 3 -> { // Hiro
                            final Map map = MapServer.getMapServer(73);
                            final TileMap tile = map.getFreeArea();
                            if (tile == null) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 24));
                            } else {
                                _char.tileMap.Exit(_char);
                                _char.cx = tile.map.template.goX;
                                _char.cy = tile.map.template.goY;
                                tile.Join(_char);
                                Robot.gI().callBot(_char.user.player,2,(short) 950,(short) 240, (byte) 3);
                            }
                        }
                        default -> {
                        }
                    }
                }
            }
        }
        if (npc != null) {
            switch (npcId) {
                case 0 -> {
                    if (_char.menuType == 1 && _char.tileMap.map.isTestDunMap()) {
                        if (menuId == 0) {
                            synchronized (TestDun.LOCK) {
                                for (int i = TestDun.arrTestDun.size() - 1; i >= 0; --i) {
                                    final TestDun test = TestDun.arrTestDun.get(i);
                                    if (test != null && test.testdunID == _char.tileMap.map.testDun.testdunID) {
                                        TestDun.arrTestDun.remove(i);
                                        test.CLOSE();
                                    }
                                }
                            }
                        } else if (menuId == 1) {
                            Service.openTextBoxUI(_char, Text.get(0, 298), (short) 498);
                        } else if (menuId == 2) {
                        }
                    } else {
                        if (menuId == 0) {
                            Service.openUI(_char, (byte) 2, null, null);
                        } else if (menuId == 1) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                return;
                            }
                            if (optionId == 0) {
                                if (!_char.cClanName.isEmpty() || _char.clan != null) {
                                    Service.openUISay(_char, npcId, Text.get(0, 187));
                                } else if (_char.user.luong < 30000) {
                                    Service.openUISay(_char, npcId, String.format(Text.get(0, 188), 30000));
                                } else if (_char.cLevel < 40) {
                                    Service.openUISay(_char, npcId, String.format(Text.get(0, 189), 40));
                                } else {
                                    Service.openTextBoxUI(_char, Text.get(0, 190), (short) 500);
                                }
                            } else {
                                if (optionId != 1) {
                                    return;
                                }
                                if (_char.cLevel < 40) {
                                    Service.openUISay(_char, npcId, Text.get(0, 33));
                                    return;
                                }
                                if (_char.clan == null) {
                                    Service.openUISay(_char, npcId, "Hoạt động này chỉ dành cho người đã gia nhập gia tộc.");
                                    return;
                                }
                                if (_char.clan.clanManor == null && !_char.clan.main_name.equals(_char.cName)) {
                                    Service.openUISay(_char, npcId, Text.get(0, 249));
                                    return;
                                }
                                if (_char.clan.clanManor == null && _char.clan.openDun <= 0) {
                                    Service.openUISay(_char, npcId, Text.get(0, 250));
                                    return;
                                }
                                final Clan clan = _char.clan;
                                synchronized (clan.LOCK_CLAN) {
                                    if (clan.clanManor == null && clan.openDun > 0) {
                                        ClanManor.setClanManor(clan);
                                        final Map map = ClanManor.getMap(clan.clanManor, (short) 80);
                                        final TileMap tile = map.getSlotZone(_char);
                                        _char.tileMap.Exit(_char);
                                        _char.cx = tile.map.template.goX;
                                        _char.cy = tile.map.template.goY;
                                        tile.Join(_char);
                                        _char.user.player.ItemBagAdd(new Item(null, (short) 260, 1, 5400, true, (byte) 0, 0));
                                        Service.ServerMessage(_char, "Bạn nhận được chìa khoá cơ quan.");
                                    } else if (clan.clanManor != null) {
                                        if (_char.clan.clanManor.memberAcceptManor.contains(_char.cName) || _char.clan.assist_name.equals(_char.cName) || _char.clan.main_name.equals(_char.cName)) {
                                            final Map map = ClanManor.getMap(clan.clanManor, (short) 80);
                                            final TileMap tile = map.getSlotZone(_char);
                                            if (tile == null) {
                                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 24));
                                            } else {
                                                _char.tileMap.Exit(_char);
                                                _char.cx = tile.map.template.goX;
                                                _char.cy = tile.map.template.goY;
                                                tile.Join(_char);
                                            }
                                        } else {
                                            Service.openUISay(_char, npcId, "Bạn chưa được mời tham gia Lãnh Địa Gia Tộc.");
                                        }
                                    }
                                }
                            }
                        } else if (menuId == 2) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                return;
                            }
                            if (optionId == 0) {
                                Service.reviewPB(_char);
                                return;
                            }
                            if (optionId == 1) {
                                BackCave.JoinCave(_char, (byte) 0, npcId);
                                return;
                            }
                            if (optionId == 2) {
                                BackCave.JoinCave(_char, (byte) 1, npcId);
                                return;
                            }
                            if (optionId == 3) {
                                BackCave.JoinCave(_char, (byte) 2, npcId);
                                return;
                            }
                            if (optionId == 4) {
                                BackCave.JoinCave(_char, (byte) 3, npcId);
                                return;
                            }
                            if (optionId == 5) {
                                BackCave.JoinCave(_char, (byte) 4, npcId);
                                return;
                            }
                            if (optionId == 6) {
                                BackCave.JoinCave(_char, (byte) 5, npcId);
                                return;
                            }
                        } else if (menuId == 3) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                return;
                            }
                            if (optionId == 0) {
                                Service.openTextBoxUI(_char, Text.get(0, 293), (short) 499);
                            }
                            if (optionId == 1) {
                                Service.testDunList(_char, TestDun.arrTestDun);
                            }
                            if (optionId == 3) {
                                final NJTalent nj = NJTalent.ninja_talent;
                                if (nj == null) {
                                    Service.openUISay(_char, npcId, "Ninja tài năng mở vào 19h50-21h hàng ngày");
                                    return;
                                }
                                final Map map3 = NJTalent.getMap(nj, NJTalent.mapChuanBi);
                                if (map3 != null) {
                                    final TileMap tileMap2 = map3.getSlotZone(_char);
                                    if (tileMap2 != null) {
                                        _char.tileMap.Exit(_char);
                                        _char.cx = map3.template.goX;
                                        _char.cy = map3.template.goY;
                                        tileMap2.Join(_char);
                                    } else {
                                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                    }
                                }
                            }
                            if (optionId == 4) {
                                Service.AlertMessage(_char, "Ninja Tài Năng", Top.getStringBXH(_char, 6));
                                return;
                            }
                        }
                    }
                    //break;
                }
                case 1 ->  {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.openUI(_char, (byte) (21 - _char.cgender), null, null);
                            return;
                        }
                        if (optionId == 1) {
                            Service.openUI(_char, (byte) (23 - _char.cgender), null, null);
                            return;
                        }
                        if (optionId == 2) {
                            Service.openUI(_char, (byte) (25 - _char.cgender), null, null);
                            return;
                        }
                        if (optionId == 3) {
                            Service.openUI(_char, (byte) (27 - _char.cgender), null, null);
                            return;
                        }
                        if (optionId == 4) {
                            Service.openUI(_char, (byte) (29 - _char.cgender), null, null);
                            return;
                        }
                    }
                }
                case 2 -> {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.openUI(_char, (byte) 16, null, null);
                            return;
                        }
                        if (optionId == 1) {
                            Service.openUI(_char, (byte) 17, null, null);
                            return;
                        }
                        if (optionId == 2) {
                            Service.openUI(_char, (byte) 18, null, null);
                            return;
                        }
                        if (optionId == 3) {
                            Service.openUI(_char, (byte) 19, null, null);
                            return;
                        }
                    } else {
                        if (menuId == 1) {
                            DanhVong.doNVDV(_char, npcId, optionId);
                        }
                    }
                    //break;
                }
                case 3 ->  {
                    if (menuId == 0) {
                        Service.openUI(_char, (byte) 7, null, null);
                        return;
                    }
                    if (menuId == 1) {
                        Service.openUI(_char, (byte) 6, null, null);
                        return;
                    }
                    if (_char.ctaskId == 0 && _char.ctaskIndex == 0) {
                        _char.uptaskMaint();
                        Service.openUISay(_char, npcId, Talk.getTask(0, 1));
                    }
                    Service.openUISay(_char, npcId, Talk.get(0, npcId));
                }
                case 4 ->  {
                    if (menuId == 0) {
                        Service.openUI(_char, (byte) 9, null, null);
                        return;
                    }
                    if (menuId == 1) {
                        Service.openUI(_char, (byte) 8, null, null);
                        return;
                    }
                    if (menuId != 2) {
                        return;
                    }
                    if (_char.ctaskId == 0 && _char.ctaskIndex == 1) {
                        _char.uptaskMaint();
                        Service.openUISay(_char, npcId, Talk.getTask(0, 2));
                    }
                    Service.openUISay(_char, npcId, Talk.get(0, npcId));
                }
                case 5 -> {
                    if (menuId == 0) {
                        switch (optionId) {
                            case 0: {
                                Service.openUIBox(_char);
                                break;
                            }
                            case 1: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }
                                // bộ sưu tập
                                Service.openMenuBST(_char);
                                break;
                            }
                            case 2: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }
                                // cải trang
                                Service.openMenuCaiTrang(_char);
                                break;
                            }
                            case 3: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }
                                if (_char.typeCaiTrang != -1) {
                                    _char.typeCaiTrang = -1;
                                    Service.updateInfoMe(_char);
                                    Player.updateInfoPlayer(_char);
                                }
                                break;
                            }
                        }
                    }
                    else if (menuId == 1) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                        }
                        _char.mapLTDId = _char.tileMap.mapID;
                        Service.openUISay(_char, npcId, Text.get(0, 39));
                    } else if (menuId == 2) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                        }
                        if (optionId == 0) {
                            if (_char.cLevel < 60) {
                                Service.ServerMessage(_char, Text.get(0, 127));
                                return;
                            }
                            final Map map2 = MapServer.getMapServer(139);
                            if (map2 != null) {
                                final TileMap tileMap = map2.getSlotZone(_char);
                                if (tileMap == null) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                } else {
                                    _char.tileMap.Exit(_char);
                                    _char.cx = tileMap.map.template.goX;
                                    _char.cy = tileMap.map.template.goY;
                                    tileMap.Join(_char);
                                }
                            }
                        } else {
                            if (optionId == 1) {
                                Service.openUISay(_char, npcId, Text.get(0, 126));
                                return;
                            }
                        }
                    } else {
                        if (menuId != 3) {
                            return;
                        }
                        if (_char.ctaskId == 0 && _char.ctaskIndex == 4) {
                            _char.uptaskMaint();
                            Service.openUISay(_char, npcId, Talk.getTask(0, 5));
                            return;
                        }
                        Service.openUISay(_char, npcId, Talk.get(0, npcId));
                    }
                    //break;
                }
                case 6 -> {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.openUI(_char, (byte) 10, null, null);
                            return;
                        }
                        if (optionId == 1) {
                            Service.openUI(_char, (byte) 31, null, null);
                            return;
                        }
                        if (optionId == 2) {
                            Service.openUISay(_char, npcId, Text.get(0, 76));
                            return;
                        }
                    } else if (menuId == 1) {
                        if (optionId == 0) {
                            Service.openUI(_char, (byte) 12, null, null);
                            return;
                        }
                        if (optionId == 1) {
                            Service.openUI(_char, (byte) 11, null, null);
                            return;
                        }
                    } else {
                        if (menuId == 2) {
                            Service.openUI(_char, (byte) 13, null, null);
                            return;
                        }
                        if (menuId == 3) {
                            Service.openUI(_char, (byte) 33, null, null);
                            return;
                        }
                        if (menuId == 4) {
                            Service.openUI(_char, (byte) 46, null, null);
                            return;
                        }
                        if (menuId == 5) {
                            Service.openUI(_char, (byte) 47, null, null);
                            return;
                        }
                        if (menuId == 6) {
                            Service.openUI(_char, (byte) 49, null, null);
                            return;
                        }
                        if (menuId == 7) {
                            Service.openUI(_char, (byte) 50, null, null);
                            return;
                        }
                        if (menuId == 8) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                return;
                            }
                            if (optionId == 0) {
                                if (_char.arrItemViThu == null) {
                                    Service.ServerMessage(_char, "Phải sử dụng vĩ thú vĩnh viễn mới có thể nâng.");
                                    return;
                                }
                                if (_char.arrItemViThu != null && _char.arrItemViThu[4].expires > 0) {
                                    Service.ServerMessage(_char, "Phải sử dụng vĩ thú vĩnh viễn mới có thể nâng.");
                                    return;
                                } else {
                                    if (_char.arrItemViThu[4].upgrade %10 != 0) {
                                        Service.ServerMessage(_char, "Vĩ thú chưa đạt mốc");
                                        return;
                                    }
                                    if (_char.arrItemViThu[4].upgrade == 20 || _char.arrItemViThu[4].upgrade == 40
                                            || _char.arrItemViThu[4].upgrade == 60 || _char.arrItemViThu[4].upgrade == 80
                                            || _char.arrItemViThu[4].upgrade == 90 || _char.arrItemViThu[4].upgrade > 100) {
                                        Service.ServerMessage(_char, "Vĩ thú chưa đạt mốc");
                                        return;
                                    }
                                    _char.arrItemViThu[4].upTNViThu(_char, (byte) 1, _char.arrItemViThu[4].upgrade);
                                    Service.ServerMessage(_char, "Đã cộng 1 điểm vào 4 loại tiềm năng");
                                    _char.user.player.upGold(-5, (byte) 2);
                                    Player.LoadViThu(_char);
                                }
                            }
                            if (optionId == 1) {
                                ViThu.Evolution(_char);
                                return;
                            }
                        }
                        if (_char.ctaskId == 0 && _char.ctaskIndex == 2) {
                            _char.uptaskMaint();
                            Service.openUISay(_char, npcId, Talk.getTask(0, 3));
                            return;
                        }
                        Service.openUISay(_char, npcId, Talk.get(0, npcId));
                    }
                    //break;
                }
                case 7 -> {
                    if (menuId == 0) {
                        if (_char.ctaskId == 0 && _char.ctaskIndex == 5) {
                            _char.uptaskMaint();
                            Service.openUISay(_char, npcId, Talk.getTask(0, 6));
                            return;
                        }
                        Service.openUISay(_char, npcId, Talk.get(0, npcId));
                    } else {
                        if (menuId > 0 && menuId <= Map.arrLang.length) {
                            final Map map2 = MapServer.getMapServer(Map.arrLang[menuId - 1]);
                            if (map2 != null) {
                                final TileMap tileMap = map2.getSlotZone(_char);
                                if (tileMap == null) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                } else if (Task.isLockChangeMap2(tileMap.mapID, _char.ctaskId)) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 84));
                                } else {
                                    _char.tileMap.Exit(_char);
                                    _char.cx = tileMap.map.template.goX;
                                    _char.cy = tileMap.map.template.goY;
                                    tileMap.Join(_char);
                                }
                            }
                        }
                    }
                    //break;
                }
                case 8 ->  {
                    if (menuId >= 0 && menuId < Map.arrTruong.length) {
                        final Map map2 = MapServer.getMapServer(Map.arrTruong[menuId]);
                        if (map2 != null) {
                            final TileMap tileMap = map2.getSlotZone(_char);
                            if (tileMap == null) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                            } else if (Task.isLockChangeMap2(tileMap.mapID, _char.ctaskId)) {
                                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 84));
                            } else {
                                _char.tileMap.Exit(_char);
                                _char.cx = tileMap.map.template.goX;
                                _char.cy = tileMap.map.template.goY;
                                tileMap.Join(_char);
                            }
                        }
                    }
                    if (menuId != 0) {
                        return;
                    }
                    if (_char.ctaskId == 0 && _char.ctaskIndex == 5) {
                        _char.uptaskMaint();
                        Service.openUISay(_char, npcId, Talk.getTask(0, 6));
                        return;
                    }
                    Service.openUISay(_char, npcId, Talk.get(0, npcId));
                }
                case 9 -> {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.AlertMessage(_char, Text.get(0, 160), Top.getStringBXH(_char, 0));
                            return;
                        }
                        if (optionId == 1) {
                            Service.AlertMessage(_char, Text.get(0, 161), Top.getStringBXH(_char, 1));
                            return;
                        }
                        if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 162), Top.getStringBXH(_char, 2));
                            return;
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, Text.get(0, 163), Top.getStringBXH(_char, 3));
                            return;
                        }
                    } else if (menuId == 1) {
                        if (optionId == 0) {
                            Admission.Admission(_char, npcId, (byte) 1);
                            return;
                        }
                        if (optionId == 1) {
                            Admission.Admission(_char, npcId, (byte) 2);
                            return;
                        }
                    } else {
                        if (menuId == 2) {
                            Char.clearPoint(_char, optionId, npcId);
                            return;
                        }
                        if (menuId == 3) {
                            if (_char.ctaskId == 8 && _char.ctaskIndex == 1) {
                                _char.uptaskMaint();
                                Service.openUISay(_char, npcId, Talk.getTask(0, 112));
                                return;
                            }
                            Service.openUISay(_char, npcId, Talk.get(0, npcId));
                        } else {
                            if (menuId == 5) {
                                Service.AlertMessage(_char, Text.get(0, 185), Top.getStringBXH(_char, 4));
                                return;
                            }
                        }
                    }
                    //break;
                }
                case 10 -> {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.AlertMessage(_char, Text.get(0, 160), Top.getStringBXH(_char, 0));
                            return;
                        }
                        if (optionId == 1) {
                            Service.AlertMessage(_char, Text.get(0, 161), Top.getStringBXH(_char, 1));
                            return;
                        }
                        if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 162), Top.getStringBXH(_char, 2));
                            return;
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, Text.get(0, 163), Top.getStringBXH(_char, 3));
                            return;
                        }
                    } else if (menuId == 1) {
                        if (optionId == 0) {
                            Admission.Admission(_char, npcId, (byte) 3);
                            return;
                        }
                        if (optionId == 1) {
                            Admission.Admission(_char, npcId, (byte) 4);
                            return;
                        }
                    } else {
                        if (menuId == 2) {
                            Char.clearPoint(_char, optionId, npcId);
                            return;
                        }
                        if (menuId == 3) {
                            if (_char.ctaskId == 8 && _char.ctaskIndex == 2) {
                                _char.uptaskMaint();
                                Service.openUISay(_char, npcId, Talk.getTask(0, 113));
                                return;
                            }
                            Service.openUISay(_char, npcId, Talk.get(0, npcId));
                        } else {
                            if (menuId == 5) {
                                Service.AlertMessage(_char, Text.get(0, 185), Top.getStringBXH(_char, 4));
                                return;
                            }
                        }
                    }
                    //break;
                }
                case 11 -> {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.AlertMessage(_char, Text.get(0, 160), Top.getStringBXH(_char, 0));
                            return;
                        }
                        if (optionId == 1) {
                            Service.AlertMessage(_char, Text.get(0, 161), Top.getStringBXH(_char, 1));
                            return;
                        }
                        if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 162), Top.getStringBXH(_char, 2));
                            return;
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, Text.get(0, 163), Top.getStringBXH(_char, 3));
                            return;
                        }
                    } else if (menuId == 1) {
                        if (optionId == 0) {
                            Admission.Admission(_char, npcId, (byte) 5);
                            return;
                        }
                        if (optionId == 1) {
                            Admission.Admission(_char, npcId, (byte) 6);
                            return;
                        }
                    } else {
                        if (menuId == 2) {
                            Char.clearPoint(_char, optionId, npcId);
                            return;
                        }
                        if (menuId == 3) {
                            if (_char.ctaskId == 8 && _char.ctaskIndex == 3) {
                                _char.uptaskMaint();
                                Service.openUISay(_char, npcId, Talk.getTask(0, 114));
                                return;
                            }
                            Service.openUISay(_char, npcId, Talk.get(0, npcId));
                        } else {
                            if (menuId == 5) {
                                Service.AlertMessage(_char, Text.get(0, 185), Top.getStringBXH(_char, 4));
                                return;
                            }
                        }
                    }
                    //break;
                }
                case 12 ->  {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.AlertMessage(_char, "Trưởng làng", "Dùng các phím Q,W,E,A,D: Di chuyển nhân vật\nHoặc các phím Lên,Trái,Phải: Di chuyển nhân vật\nPhím Spacebar hoặc phím Enter: Tấn công hoặc hành động\nPhím F1: Menu,Phím F2: Đổi mục tiêu, phím 6,7: Dùng bình HP,MP\nPhím 0: Chat,Phím P: Đổi kỹ năng,Phím 1,2,3,4,5: Sử dụng kỹ năng được gán trước trong mục Kỹ Năng");
                            return;
                        }
                        if (optionId == 1) {
                            Service.AlertMessage(_char, "Trưởng làng", "Kiếm, Kunai, Đao: Ưu tiên tăng sức mạnh(sức đánh) --> thể lực(HP) --> Thân pháp(Né đòn, chính xác) --> Charka(MP).\n\nTiêu, Cung, Quạt: Ưu tiên tăng Charka(Sức đánh, MP) -->thể lực(HP)--> Thân pháp(Né đòn, chính xác). Không tăng SM.");
                            return;
                        }
                        if (optionId == 2) {
                            Service.AlertMessage(_char, "Trưởng làng", "Pk thường: trạng thái hòa bình.\n\nPk phe: không đánh được người cùng nhóm hay cùng bang hội. Giết người không lên điểm hiếu chiến.\n\nPk đồ sát: có thể đánh tất cả người chơi. Giết 1 người sẽ lên 1 điểm hiếu chiến.\n\nĐiểm hiếu chiến cao sẽ không sử dụng bình HP, MP, Thức ăn.\n\nTỷ thí: chọn người chơi, chọn tỷ thí, chờ người đó đồng ý.\n\nCừu Sát: Chọn người chơi khác, chọn cừu sát, điểm hiếu chiến tăng 2.");
                            return;
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, "Trưởng làng", "Bạn có thể tạo một nhóm tối đa 6 người chơi.\n\nNhững người trong cùng nhóm sẽ được nhận thêm x% điểm kinh nghiệm từ người khác.\n\nNhững người cùng nhóm sẽ cùng được vật phẩm, thành tích nếu cùng chung nhiệm vụ.\n\nĐể mời người vào nhóm, chọn người đó, và chọn mời vào nhóm. Để quản lý nhóm, chọn Menu/Tính năng/Nhóm.");
                            return;
                        }
                        if (optionId == 4) {
                            Service.AlertMessage(_char, "Trưởng làng", "Đá dùng để nâng cấp trang bị. Bạn có thể mua từ cửa hàng hoặc nhặt khi đánh quái.Nâng cấp đá nhằm mục đích nâng cao tỉ lệ thành công khi nâng cấp trang bị cấp cao.Để luyện đá, bạn cần tìm Kenshinto.\n\nĐể đảm bảo thành công 100%, 4 viên đá cấp thấp sẽ luyện thành 1 viên đá cấp cao hơn.");
                            return;
                        }
                        if (optionId == 5) {
                            Service.AlertMessage(_char, "Trưởng làng", "Nâng cấp trang bị nhằm mục đích gia tăng các chỉ số cơ bản của trang bị. Có các cấp trang bị sau +1, +2, +3... tối đa +16.Để thực hiện, bạn cần gặp NPC Kenshinto. Sau đó, tiến hành chọn vật phẩm và số lượng đá đủ để nâng cấp. Lưu ý, trang bị cấp độ 5 trở lên nâng cấp thất bại sẽ bị giảm cấp độ.\n\nBạn có thể tách một vật phẩm đã nâng cấp và thu lại 50% số đá đã dùng để nâng cấp trang bị đó.");
                            return;
                        }
                        if (optionId == 6) {
                            Service.AlertMessage(_char, "Trưởng làng", "Khi tham gia các hoạt động trong game bạn sẽ nhận được điểm hoạt động. Qua một ngày điểm hoạt động sẽ bị trừ dần (nếu từ 1-49 trừ 1, 50-99 trừ 2, 100-149 trừ 3...). Mỗi tuần bạn sẽ có cơ hội đổi Yên sang Xu nếu có đủ điểm hoạt động theo vêu cầu của NPC Okanechan.\n\nMột tuần một lần duy nhất được đối tối đa 70.000 Yên = 70.000 xu.");
                            return;
                        }
                    }
                    if (menuId == 1) {
                        Random generator = new Random();
                        int value = generator.nextInt(3);
                        if (value == 0) {
                            Service.openUISay(_char, npcId, "Làng Tone là ngôi làng cổ xưa, đã có từ rất lâu.");
                            return;
                        }
                        if (value == 1) {
                            Service.openUISay(_char, npcId, "Đi thưa, về trình, nhé các con");
                            return;
                        }
                        if (value == 2) {
                            Service.openUISay(_char, npcId, "Ta là Tajima, mọi việc ở đây đều do ta quản lý.");
                            return;
                        }
                    }
                    if (menuId == 2) {
                        if (_char.ctaskIndex == -1) {
                            Service.openUISay(_char, npcId, "Con chưa nhận nhiệm vụ nào cả.");
                            return;
                        }
                        Service.openUISay(_char, npcId, "Ta đã hủy vật phẩm và nhiệm vụ lần này cho con.");
                        _char.clearTask();
                        Service.getTask(_char);
                    }
                    if (menuId == 3) {
                        try {
                            _char.tileMap.lock.lock("Đổi phân thân");
                            try {
                                Player.toNhanban(_char);
                            } finally {
                                _char.tileMap.lock.unlock();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (menuId == 4) {
                        Player player = null;
                        if (!_char.isHuman && _char.user != null) {
                            player = _char.user.player;
                        }
                        try {
                            _char.tileMap.lock.lock("Đổi chủ thân");
                            try {
                                Player.toChar(_char);
                            } finally {
                                _char.tileMap.lock.unlock();
                            }
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (player != null) {
                            Player.CallNhanban(player.tileMap, player);
                        }
                    }
                }
                case 14, 15, 16 -> {
                    if (_char.ctaskId == 15) {
                        if (menuId == 0) {
                            _char.uptaskMaint();
                            _char.user.player.ItemBagUses((short) 214, 1);
                        }
                    } else if (_char.ctaskId == 20 && npcId == 15) {
                        if (menuId == 0) {
                            final Map task = MapServer.getMapServer(74);
                            if (task != null) {
                                final TileMap tile = task.getFreeArea();
                                if (tile == null) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                } else {
                                    _char.tileMap.Exit(_char);
                                    _char.cx = tile.map.template.goX;
                                    _char.cy = tile.map.template.goY;
                                    _char.timeTask = System.currentTimeMillis() + 180000L;
                                    Service.mapTime(_char, 180);
                                    tile.Join(_char);
                                }
                            }
                        }
                    }
                }
                case 17 -> {
                    if (_char.ctaskId == 17) {
                        if (menuId == 0) {
                            Robot.gI().callBot(_char.user.player, 3, _char.cx, _char.cy, (byte) 0);
                        }
                    }
                }
                case 18, 23 -> {
                    if (menuId == -1) {
                        if (optionId == 0) {
                            if ((_char.ctaskId == 19 || _char.ctaskId == 23) && _char.ctaskIndex == 0) {
                                Task.getItemTask(_char);
                            }
                        }
                    }
                }
                case 24 -> {
                    if (_char.menuType == 1) {
                        if (_char.ctaskId == 0 && _char.ctaskIndex == 3) {
                            _char.uptaskMaint();
                            Service.openUISay(_char, npcId, Talk.getTask(0, 4));
                            return;
                        }
                        Service.openUISay(_char, npcId, Talk.get(0, npcId));
                    } else if (menuId == 0) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                            return;
                        }
//                        if (_char.user.isTester == 0) {
//                            Service.openUISay(_char, npcId, "Hãy kiếm 6789 lượng và đến NPC Vua Hùng xác thực.");
//                            break;
//                        }
                        if (_char.user.luong < 500) {
                            Service.openUISay(_char, npcId, Text.get(0, 154));
                            return;
                        }
                        _char.user.player.upGold(-500L, (byte) 1);
                        if (optionId == 0) {
                            _char.user.player.upCoin(5000000L, (byte) 1);
                            return;
                        }
                        if (optionId == 1) {
                            _char.user.player.upCoinLock(5500000L, (byte) 2);
                            return;
                        }
                    } else {
                        if (menuId == 2) {
                            try {
                                final int level = Menu.arrLevelGift[optionId];
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                } else if (_char.user.player.LevelGift.contains(level)) {
                                    Service.openUISay(_char, npcId, Text.get(0, 164));
                                } else if (level > _char.user.player.cLevel) {
                                    Service.openUISay(_char, npcId, Text.get(0, 165));
                                } else {
                                    switch (level) {
                                        case 10: {
                                            _char.user.player.upCoinLock(10000L, (byte) 2);
                                            _char.user.player.upCoinLock(10000000L, (byte) 2);
                                            break;
                                        }
                                        case 20: {
                                            if (_char.user.player.ItemBagSlotNull() < 1) {
                                                Service.openUISay(_char, npcId, String.format(Text.get(0, 151), 1));
                                                return;
                                            }
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 240, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.upGold(10L, (byte) 1);
                                            _char.user.player.upCoinLock(20000000L, (byte) 2);
                                            break;
                                        }
                                        case 30: {
                                            if (_char.user.player.ItemBagSlotNull() < 8) {
                                                Service.openUISay(_char, npcId, String.format(Text.get(0, 151), 2));
                                                return;
                                            }
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 241, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 5, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 7, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 7, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 7, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 15, 500, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 20, 500, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 539, 1, -1, true, (byte) 0, 0));

                                            _char.user.player.upCoinLock(30000000L, (byte) 2);
                                            break;
                                        }
                                        case 40: {
                                            if (_char.user.player.ItemBagSlotNull() < 2) {
                                                Service.openUISay(_char, npcId, String.format(Text.get(0, 151), 2));
                                                return;
                                            }
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 242, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 6, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.upCoinLock(40000000L, (byte) 2);
                                            break;
                                        }
                                        case 50: {
                                            if (_char.user.player.ItemBagSlotNull() < 2) {
                                                Service.openUISay(_char, npcId, String.format(Text.get(0, 151), 2));
                                                return;
                                            }
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 269, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.ItemBagAdd(new Item(null, (short) 7, 1, -1, true, (byte) 0, 0));
                                            _char.user.player.upCoinLock(50000000L, (byte) 2);
                                            break;
                                        }
                                    }
                                    _char.user.player.LevelGift.add(level);
                                }
                            } catch (Exception e3) {
                            }
                        }
                        if (menuId == 3) {
                            if (_char.ctaskId == 0 && _char.ctaskIndex == 3) {
                                _char.uptaskMaint();
                                Service.openUISay(_char, npcId, Talk.getTask(0, 4));
                            }
                            Service.openUISay(_char, npcId, Talk.get(0, npcId));
                        } else if (menuId == 4) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                return;
                            }
                            if (optionId == 0) {
                                Service.openTextBoxUI(_char, Text.get(0, 255), (short) 501);
                                return;
                            }
                            if (optionId == 1) {
                                Service.openTextBoxUI(_char, Text.get(0, 255), (short) 502);
                                return;
                            }
                            if (optionId == 2) {
                                Service.openTextBoxUI(_char, Text.get(0, 255), (short) 503);
                                return;
                            }
                            if (optionId == 3) {
                                DiamondSwap.viewDiamond(_char);
                                return;
                            }
                            if (optionId == 4) {
                                DiamondSwap.BangGia(_char);
                                return;
                            }
                        } else if (menuId == 5) {
                            if (!_char.isHuman) {
                                Service.ServerMessage(_char, Text.get(0, 310));
                                return;
                            }
                            if (optionId == 0) {
                                Service.openTextBoxUI(_char, "Nhập tên người nhận :", (short) 998);
                                return;
                            }
                            if (optionId == 1) {
                                DiamondSwap.viewGiftCard(_char, npcId);
                                return;
                            }
                        }
                    }
                }
                case 25 -> {
                    MobTemplate mob;
                    MapTemplate map;
                    if (_char.menuType == 1 && _char.tileMap.map.isChienTruong()) {
                        final ChienTruong ct = ChienTruong.chien_truong;
                        if (menuId == 0) {
                            synchronized (ct.CHIENTRUONG_LOCK) {
                                TileMap.getMapLTD(_char);
                            }
                        } else if (menuId == 1) {
                            Service.reviewChienTruong(_char, ct);
                        }
                    }
                    if (menuId == 0) {
                    }
                    if (menuId == 1) {
                        switch (optionId) {
                            case 0:
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.cLevel < 20) {
                                    Service.openUISay(_char, npcId, "Phải đạt cấp độ 20 mới có thể nhận nhiệm vụ.");
                                    break;
                                }

                                if (_char.taskHangNgay[5] != 0) {
                                    Service.openUISay(_char, npcId, "Nhiệm vụ trước đó chưa hoàn thành, hãy mau chóng đi hoàn thành và nhận lấy phần thưởng.");
                                    break;
                                }

                                if (_char.countTaskHangNgay >= 20) {
                                    Service.openUISay(_char, npcId, "Con đã hoàn thành hết nhiệm vụ của ngày hôm nay, hãy quay lại vào ngày mai.");
                                    break;
                                }

                                mob = Service.getMobIdByLevel(_char.cLevel);
                                if (mob != null) {
                                    map = Service.getMobMapId(mob.mobTemplateId);
                                    if (map != null) {
                                        _char.taskHangNgay[0] = 0;
                                        _char.taskHangNgay[1] = 0;
                                        _char.taskHangNgay[2] = Util.nextInt(10, 25);
                                        _char.taskHangNgay[3] = mob.mobTemplateId;
                                        _char.taskHangNgay[4] = map.mapID;
                                        _char.taskHangNgay[5] = 1;
                                        _char.countTaskHangNgay++;
                                        Service.getTaskOrder(_char, (byte) 0);
                                        Service.openUISay(_char, (short) npcId, "Đây là nhiệm vụ thứ " + _char.taskHangNgay[6] + "/20 trong ngày của con.");
                                    }
                                }
                                break;
                            case 1: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }
                                if (_char.taskHangNgay[5] == 0) {
                                    Service.openUISay(_char, (short) npcId, "Con chưa nhận nhiệm vụ nào cả!");
                                    return;
                                }

                                _char.taskHangNgay[5] = 0;
                                _char.taskHangNgay = new int[]{-1, -1, -1, -1, -1, 0, _char.countTaskHangNgay};
                                Service.clearTaskOrder(_char, (byte) 0);
                                Service.openUISay(_char, (short) npcId, "Con đã huỷ nhiệm vụ lần này, hãy cố gắng lần sau.");
                                break;
                            }
                            case 2: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.taskHangNgay[5] == 0) {
                                    Service.openUISay(_char, (short) npcId, "Con chưa nhận nhiệm vụ nào cả!");
                                    return;
                                }

                                if (_char.user.player.ItemBagSlotNull() < 1) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                                    break;
                                }

                                if (_char.taskHangNgay[1] < _char.taskHangNgay[2]) {
                                    Service.openUISay(_char, (short) npcId, "Con chưa hoàn thành nhiệm vụ ta giao!");
                                    return;
                                }
                                if (_char.countTaskHangNgay == 10 || _char.countTaskHangNgay == 20) {
                                    _char.user.player.upGold(Util.nextInt(200, 300), (byte) 2);
                                    
                                    _char.pointHoatDong += 3;
                                    Service.ServerMessage(_char, "Bạn nhận được 3 điểm hoạt động.");
                                }
                                if ((_char.ctaskId == 30 && _char.ctaskIndex == 1) || (_char.ctaskId == 39 && _char.ctaskIndex == 3)) {
                                    _char.uptaskMaint();
                                }
                                _char.taskHangNgay[5] = 0;
                                _char.taskHangNgay = new int[]{-1, -1, -1, -1, -1, 0, _char.countTaskHangNgay};
                                Service.clearTaskOrder(_char, (byte) 0);
                                Service.openUISay(_char, (short) npcId, "Con hãy nhận lấy phần thưởng của mình.");
                                break;
                            }

                            case 3: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.taskHangNgay[4] != -1) {
                                    final Map map2 = MapServer.getMapServer(_char.taskHangNgay[4]);
                                    if (map2 != null) {
                                        final TileMap tileMap = map2.getSlotZone(_char);
                                        if (tileMap == null) {
                                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                        } else {
                                            _char.tileMap.Exit(_char);
                                            _char.cx = tileMap.map.template.goX;
                                            _char.cy = tileMap.map.template.goY;
                                            tileMap.Join(_char);
                                        }
                                    }
                                }
                                Service.openUISay(_char, (short) npcId, "Con chưa nhận nhiệm vụ nào cả!");
                                break;
                            }
                            default:
                                break;
                        }
                    }
                    if (menuId == 2) {
                        switch (optionId) {
                            case 0: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.cLevel < 30) {
                                    Service.openUISay(_char, npcId, "Phải đạt cấp độ 30 mới có thể nhận nhiệm vụ.");
                                    break;
                                }

                                if (_char.taskTaThu[5] != 0) {
                                    Service.openUISay(_char, npcId, "Nhiệm vụ trước đó chưa hoàn thành, hãy mau chóng đi hoàn thành và nhận lấy phần thưởng.");
                                    break;
                                }

                                if (_char.countTaskTaThu == 0) {
                                    Service.openUISay(_char, npcId, "Con đã hoàn thành hết nhiệm vụ của ngày hôm nay, hãy quay lại vào ngày mai.");
                                    break;
                                }

                                mob = Service.getMobIdTaThu(_char.cLevel);
                                if (mob != null) {
                                    map = Service.getMobMapIdTaThu(mob.mobTemplateId);
                                    if (map != null) {
                                        _char.taskTaThu[0] = 1;
                                        _char.taskTaThu[1] = 0;
                                        _char.taskTaThu[2] = 1;
                                        _char.taskTaThu[3] = mob.mobTemplateId;
                                        _char.taskTaThu[4] = map.mapID;
                                        _char.taskTaThu[5] = 1;
                                        _char.countTaskTaThu--;
                                        Service.getTaskOrder(_char, (byte) 1);
                                        Service.openUISay(_char, (short) npcId, "Hãy hoàn thành nhiệm vụ và trở về đây nhận thưởng.");
                                    }
                                }
                                break;
                            }
                            case 1: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.taskTaThu[5] == 0) {
                                    Service.openUISay(_char, (short) npcId, "Con chưa nhận nhiệm vụ nào cả!");
                                    return;
                                }

                                Service.clearTaskOrder(_char, (byte) 1);
                                _char.taskTaThu[5] = 0;
                                _char.taskTaThu = new int[]{-1, -1, -1, -1, -1, 0, _char.countTaskTaThu};
                                Service.openUISay(_char, (short) npcId, "Con đã huỷ nhiệm vụ lần này.");
                                break;
                            }

                            case 2: {
                                if (!_char.isHuman) {
                                    Service.ServerMessage(_char, Text.get(0, 310));
                                    break;
                                }

                                if (_char.taskTaThu[5] == 0) {
                                    Service.openUISay(_char, (short) npcId, "Con chưa nhận nhiệm vụ nào cả!");
                                    return;
                                }

                                if (_char.taskTaThu[1] < _char.taskTaThu[2]) {
                                    Service.openUISay(_char, (short) npcId, "Con chưa hoàn thành nhiệm vụ ta giao!");
                                    return;
                                }

                                if (_char.user.player.ItemBagSlotNull() < 1) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                                    break;
                                }

                                _char.taskTaThu[5] = 0;
                                _char.taskTaThu = new int[]{-1, -1, -1, -1, -1, 0, _char.countTaskTaThu};
                                Service.clearTaskOrder(_char, (byte) 1);
                                int quantity = 0;
                                if (_char.cLevel < 60) {
                                    quantity = 2;
                                } else if (_char.cLevel >= 60 && _char.cLevel < 80) {
                                    quantity = 3;
                                } else if (_char.cLevel >= 80) {
                                    quantity = 5;
                                }
                                if ((_char.ctaskId == 30 && _char.ctaskIndex == 2) || (_char.ctaskId == 39 && _char.ctaskIndex == 1)) {
                                    _char.uptaskMaint();
                                }
                                _char.user.player.ItemBagAdd(new Item(null, (short) 251, quantity, -1, false, (byte) 0, 0));
                                _char.user.player.upGold(50, (byte) 2);
                                _char.user.player.upCoin(50000, (byte) 2);
                                _char.pointHoatDong += 3;
                                Service.ServerMessage(_char, "Bạn nhận được 3 điểm hoạt động.");
                                Service.openUISay(_char, (short) npcId, "Con hãy nhận lấy phần thưởng của mình.");
                                break;
                            }
                            default:
                                break;
                        }
                    }
//                    if (menuId != 2) {
//                        break;
//                    }
                    if (menuId == 3) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                        }
                        if (optionId == 0 || optionId == 1) {
                            final ChienTruong ct = ChienTruong.chien_truong;
                            if (ct == null) {
                                Service.openUISay(_char, npcId, Text.get(0, 330));
                            } else {
                                synchronized (ct.CHIENTRUONG_LOCK) {
                                    if (!ct.isBaoDanh && !ct.aCharBlack.contains(_char.user.player.playerId) && !ct.aCharWhite.contains(_char.user.player.playerId)) {
                                        Service.openUISay(_char, npcId, Text.get(0, 350));
                                    } else if (optionId == 0 && ct.aCharBlack.contains(_char.user.player.playerId)) {
                                        Service.openUISay(_char, npcId, Text.get(0, 351));
                                    } else if (optionId == 1 && ct.aCharWhite.contains(_char.user.player.playerId)) {
                                        Service.openUISay(_char, npcId, Text.get(0, 352));
                                    } else {
                                        short mapGo = -1;
                                        if (optionId == 0) {
                                            mapGo = 98;
                                            if (!ct.aCharWhite.contains(_char.user.player.playerId)) {
                                                _char.user.player.pointChienTruong = 0;
                                                ct.aCharWhite.add(_char.user.player.playerId);
                                            }
                                        } else if (optionId == 1) {
                                            mapGo = 104;
                                            if (!ct.aCharBlack.contains(_char.user.player.playerId)) {
                                                _char.user.player.pointChienTruong = 0;
                                                ct.aCharBlack.add(_char.user.player.playerId);
                                            }
                                        }
                                        if (mapGo != -1) {
                                            final Map map3 = ChienTruong.getMap(ct, mapGo);
                                            if (map3 != null) {
                                                final TileMap tileMap2 = map3.getSlotZone(_char);
                                                if (tileMap2 != null) {
                                                    _char.tileMap.Exit(_char);
                                                    _char.cx = map3.template.goX;
                                                    _char.cy = map3.template.goY;
                                                    tileMap2.Join(_char);
                                                    Top.sortTop(5, _char.cName, ChienTruong.TITLE_CT[_char.getCT()], _char.pointChienTruong, new int[]{(_char.cTypePk == 4) ? 0 : ((_char.cTypePk == 5) ? 1 : -1)});
                                                } else {
                                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (optionId == 2) {
                            final ChienTruong ct = ChienTruong.chien_truong;
                            if (ct == null) {
                                Service.openUISay(_char, npcId, Text.get(0, 330));
                            } else {
                                Service.reviewChienTruong(_char, ct);
                            }
                        }
                    }
                    if (menuId == 4) {
                        if (SevenBeasts.isRun) {
                            if (SevenBeasts.isStart) {
                                if (SevenBeasts.checkBaoDanh(_char)) {
                                    SevenBeasts sb = SevenBeasts.getSevenBeasts(_char);
                                    if (sb != null) {
                                        SevenBeasts.joinMap(sb, _char);
                                    } else {
                                        GameCanvas.startOKDlg(_char.user.session, "Có lỗi gì đó xảy ra!");
                                    }
                                } else {
                                    Service.openUISay(_char, npcId, "Đã hết thời gian báo danh.");
                                }
                            } else if (SevenBeasts.isBaoDanh) {
                                if (SevenBeasts.checkBaoDanh(_char)) {
                                    SevenBeasts sb = SevenBeasts.getSevenBeasts(_char);
                                    if (sb != null) {
                                        SevenBeasts.joinMap(sb, _char);
                                    } else {
                                        GameCanvas.startOKDlg(_char.user.session, "Có lỗi gì đó xảy ra!");
                                    }
                                } else if (_char.cLevel < 50) {
                                    Service.openUISay(_char, npcId, "Yêu cầu trình độ cấp 50.");
                                } else if (_char.party == null) {
                                    Service.openUISay(_char, npcId, "Bạn chưa có nhóm, hãy đi triệu tập thành viên để tham gia.");
                                } else if (!_char.party.aChar.get(0).cName.equals(_char.cName)) {
                                    Service.openUISay(_char, npcId, "Chỉ có nhóm trưởng mới có thể mở cửa Thất thú ải.");
                                } else if (_char.party.numPlayer < 1) {
                                    Service.openUISay(_char, npcId, "Nhóm phải có đủ 6 thành viên mới có thể tham gia.");
                                } else if (_char.party.checkLevelParty(_char)) {
                                    Service.openUISay(_char, npcId, "Có thành viên trong nhóm không đủ cấp độ yêu cầu.");
                                } else if (SevenBeasts.checkBaoDanh(_char.party)) {
                                    Service.openUISay(_char, npcId, "Có thành viên trong nhóm đã báo danh rồi.");
                                } else {
                                    SevenBeasts sBeasts = new SevenBeasts();
                                    SevenBeasts.arrChar.addAll(_char.party.aChar);
                                    sBeasts.setSevenBeasts(_char);
                                    sBeasts.sendTB(_char, null, null, 3);
                                    Service.openUISay(_char, npcId, "Báo danh thành công.");
                                }
                            }
                        } else {
                            Service.openUISay(_char, npcId, Text.get(0, 408));
                        }
                    }
                }
                case 26 -> {
                    if (menuId == 0) {
                        Service.openUI(_char, (byte) 14, null, null);
                    }
                    if (menuId == 1) {
                        Service.openUI(_char, (byte) 15, null, null);
                    }
                    if (menuId == 2) {
                        Service.openUI(_char, (byte) 32, null, null);
                    }
                    if (menuId == 3) {
                        Service.openUI(_char, (byte) 34, null, null);
                    }
                }
                case 27 ->  {
                    if (menuId == 0) {
                        TileMap tile = _char.tileMap;
                        _char.clan.clanManor.openTru(_char, tile.mapID, tile.map);
                    }
                }
                case 28 ->  {
                    if (menuId == 0) {
                        if (optionId == 0) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[0]);
                        }
                        if (optionId == 1) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[1]);
                        }
                        if (optionId == 2) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[2]);
                        }
                        if (optionId == 3) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[3]);
                        }
                        if (optionId == 4) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[4]);
                        }
                        if (optionId == 5) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[5]);
                        }
                        if (optionId == 6) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[6]);
                        }
                        if (optionId == 7) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[7]);
                        }
                        if (optionId == 8) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[8]);
                        }
                        if (optionId == 9) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[9]);
                        }
                        if (optionId == 10) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[10]);
                        }
                        if (optionId == 11) {
                            Service.loadItemAuction(_char, optionId, ItemStands.arrItemStands[11]);
                        }
                    } else {
                        if (menuId != 1) {
                            return;
                        }
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                        }
                        Service.openUI(_char, (byte) 36, null, null);
                    }
                    //break;
                }
                case 29 ->  {
                    if (menuId != 0) {
                        return;
                    }
                    if (optionId == 0) {
                        BiKip.LuyenBiKip(_char);
                    }
                    if (optionId == 1) {
                        final Item it = _char.ItemBody[15];
                        if (it == null) {
                            Service.ServerMessage(_char, Text.get(0, 389));
                        } else if (it.upgrade >= 10) {
                            Service.ServerMessage(_char, Text.get(0, 390));
                        } else {
                            Service.openUIConfirmID(_char, String.format(Text.get(0, 388), it.template.name, BiKip.goldUps[it.upgrade], BiKip.SL_NL[it.upgrade], GameScr.itemTemplates[457].name, BiKip.percents[it.upgrade], "%"), (byte) (-125));
                        }
                    }
                    if (optionId == 2) {
                        Service.AlertMessage(_char, Text.get(0, 130), Text.get(0, 385));
                    }
                }
                case 30 ->  {
                    if (menuId == 0) {
                        Service.openUI(_char, (byte) 38, null, null);
                    } else if (menuId == 1) {
                        Service.openTextBoxUI(_char, Text.get(0, 51), (short) 504);
                    } else if (menuId == 2) {
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                        }
                        if (optionId == 0) {
                            final Lucky lucky = Lucky.arrLucky[0];
                            Service.AlertLuck(_char.user.player, lucky);
                        } else if (optionId == 1) {
                            Service.openTextBoxUI(_char, Text.get(0, 66), (short) 100);
                        } else if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 66), Text.get(0, 69));
                        }
                        _char.menuType = 0;
                    } else {
                        if (menuId != 3) {
                            return;
                        }
                        if (!_char.isHuman) {
                            Service.ServerMessage(_char, Text.get(0, 310));
                        }
                        if (optionId == 0) {
                            final Lucky lucky = Lucky.arrLucky[1];
                            Service.AlertLuck(_char.user.player, lucky);
                        } else if (optionId == 1) {
                            Service.openTextBoxUI(_char, Text.get(0, 67), (short) 100);
                        } else if (optionId == 2) {
                            Service.AlertMessage(_char, Text.get(0, 66), Text.get(0, 70));
                        }
                        _char.menuType = 1;
                    }
                    //break;
                }
                case 31 ->  {
                    if (menuId != 0) {
                        return;
                    }
                    if (!_char.isHuman) {
                        Service.ServerMessage(_char, Text.get(0, 310));
                    }
                    if (GameScr.vEvent != 1 || npc.type == 15) {
                    }
                    if (_char.user.player.ItemBagQuantity((short) 310) < 1) {
                        Service.ServerMessage(_char, Text.get(0, 174));
                    }
                    Event.Lighting(_char, npc);
                }
                case 32 ->  {
                    if (_char.tileMap != null && _char.tileMap.mapID == 117) {
                        if (menuId == 0) {
                            Service.openTextBoxUI(_char, "Nhập xu cược", (short) 515);
                        } else if (menuId == 1) {
                            TileMap.getMapLTD(_char);
                        }
                    } else if (_char.tileMap != null && (_char.tileMap.mapID == 118 || _char.tileMap.mapID == 119)) {
                        if (menuId == 0) {
                            TileMap.getMapLTD(_char);
                        } else if (menuId == 1) {
                            Service.AlertMessage(_char, "Tổng kết", String.format("%s : Tích luỹ %s điểm\n" +
                                    "%s : Tích luỹ %s điểm", _char.clan.warClan.clanBlack.name, _char.clan.warClan.pointBlack,
                                    _char.clan.warClan.clanWhite.name, _char.clan.warClan.pointWhite));
                        }
                    }
                    if (menuId == 0) {
                    }
                    if (menuId == 1) {
                        Clan clan = Clan.get(_char.cClanName);
                        if (clan != null && clan.warClan != null) {
                            short mapId = 22;
                            if (_char.clan.typeWar == 4) {
                                mapId = 118;
                            }
                            if (_char.clan.typeWar == 5) {
                                mapId = 119;
                            }
                            final Map map = WarClan.getMap(clan.warClan, mapId);
                            if (map != null) {
                                final TileMap tile = map.getSlotZone(_char);
                                if (tile == null) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                } else {
                                    _char.tileMap.Exit(_char);
                                    _char.cx = tile.map.template.goX;
                                    _char.cy = tile.map.template.goY;
                                    tile.Join(_char);
                                }
                            }
                        } else if (clan != null) {
                            if (clan.main_name.equals(_char.cName)) {
                                Service.openTextBoxUI(_char, "Nhập tên gia tộc đối thủ", (short) 514);
                            } else {
                                Service.openUISay(_char, npcId, "Chức năng chỉ dành cho tộc trưởng.");
                            }
                        } else {
                            Service.openUISay(_char, npcId, "Chức năng chỉ dành cho người có gia tộc.");
                        }
                    }
                    if (menuId == 4) {
                        if (optionId == 0) {
                            Service.openUI(_char, (byte) 43, null, null);
                        }
                        if (optionId == 1) {
                            Service.openUI(_char, (byte) 44, null, null);
                        }
                        if (optionId == 2) {
                            Service.openUI(_char, (byte) 45, null, null);
                        }
                        if (optionId == 3) {
                            Service.AlertMessage(_char, Text.get(0, 130), Text.get(0, 131));
                        }
                    }
                }
                case 33 ->  {
                    if (!_char.isHuman) {
                        Service.ServerMessage(_char, Text.get(0, 310));
                    }
                    Event.doEventMenu(_char, menuId, optionId, npcId);
                }
                case 34 ->  {
                    if (GameScr.vEvent == 3) {
                        Event.cayThong(_char, menuId, optionId, npcId);
                    }
                    if (GameScr.vEvent == 4) {
                        Event.hoaDao(_char, menuId, optionId, npcId);
                    }
                }
                case 36 ->  {
                    switch (menuId) {
                        case 0: {
                            String str = "";
                            if (GameScr.qua_top == 0) {
                                Service.openUISay(_char, npcId, "Chưa tới thời gian nhận quà.");
                                return;
                            }
                            RewardTop.Reward(_char, npcId);
                            break;
                        }
                        case 1: {
                            Service.openTextBoxUI(_char, Text.get(0, 51), (short) 504);
                            break;
                        }
                        default:
                            break;
                    }
                }
                case 37 ->  {
                    switch (menuId) {
                        case 0: {
                            Service.AlertMessage(_char, "Thành tích", "- Bạn có " + _char.pointTalent + " điểm tài năng");
                            break;
                        }
                        case 1: {
                            final NJTalent nj = NJTalent.ninja_talent;
                            synchronized (nj.NJ_LOCK) {
                                final Map ltd = MapServer.getMapServer(_char.mapLTDId);
                                if (ltd != null) {
                                    final TileMap tile = ltd.getSlotZone(_char);
                                    if (tile == null) {
                                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 9));
                                    } else {
                                        _char.tileMap.Exit(_char);
                                        _char.cx = tile.map.template.goX;
                                        _char.cy = tile.map.template.goY;
                                        tile.Join(_char);
                                    }
                                }
                            }
                            break;
                        }
                        case 2: {
                            Service.AlertMessage(_char, "Hướng dẫn", "");
                            break;
                        }
                    }
                }
            }
        }
    }

    protected static void MenuNew(final Char _char, final byte npcId, final byte menuId, final byte optionId) {
        switch (_char.menuType + 128) {
            case 0: {
                if (menuId == 0) {
                    _char.menuType = -127;
                    Service.openUIMenuNew(_char, new String[]{Text.get(0, 71), Text.get(0, 72)});
                    break;
                }
                if (menuId == 1) {
                    _char.menuType = -126;
                    Service.openUIMenuNew(_char, new String[]{Text.get(0, 71), Text.get(0, 72)});
                    break;
                }
                break;
            }
            case 1: {
                if (menuId == 0) {
                    final Lucky lucky = Lucky.arrLucky[0];
                    Service.AlertLuck(_char.user.player, lucky);
                    break;
                }
                if (menuId == 1) {
                    Service.AlertMessage(_char, Text.get(0, 66), Text.get(0, 69));
                    break;
                }
                break;
            }
            case 2: {
                if (menuId == 0) {
                    final Lucky lucky = Lucky.arrLucky[1];
                    Service.AlertLuck(_char.user.player, lucky);
                    break;
                }
                if (menuId == 1) {
                    Service.AlertMessage(_char, Text.get(0, 66), Text.get(0, 70));
                    break;
                }
                break;
            }
            case 3: {
                Admin.controller(_char, menuId);
                break;
            }
            case 4: {
                if (menuId == 0) {
                    if (_char.user.player.ItemBagQuantity((short) 251) < 300) {
                        Service.ServerMessage(_char, "Không đủ mảnh giấy vụn");
                    } else {
                        _char.user.player.ItemBagUses((short) 251, 300);
                        Item it = new Item(null, (short) 253, 1, -1, false, (byte) 0, 0);
                        _char.user.player.ItemBagAddQuantity(it);
                    }
                    break;
                }
                if (menuId == 1) {
                    if (_char.user.player.ItemBagQuantity((short) 251) < 250) {
                        Service.ServerMessage(_char, "Không đủ mảnh giấy vụn");
                    } else {
                        _char.user.player.ItemBagUses((short) 251, 250);
                        Item it = new Item(null, (short) 252, 1, -1, false, (byte) 0, 0);
                        _char.user.player.ItemBagAddQuantity(it);
                    }
                    break;
                }
                break;
            }
        }
    }

    protected static void openMenu(final Char _char, final short npcTemplateId) {
        String[] Arrcaption = null;
        _char.menuType = 0;
        if (Task.isTaskNPC(_char, npcTemplateId)) {
            Arrcaption = new String[]{null};
            if (_char.ctaskIndex == -1) {
                Arrcaption[0] = GameScr.taskTemplates[_char.ctaskId].name;
            } else if (Task.isFinishTask(_char)) {
                Arrcaption[0] = Text.get(0, 12);
            } else if (_char.ctaskIndex >= 0 && _char.ctaskIndex <= 4 && _char.ctaskId == 1) {
                Arrcaption[0] = GameScr.taskTemplates[_char.ctaskId].name;
            } else if (_char.ctaskIndex >= 1 && _char.ctaskIndex <= 15 && _char.ctaskId == 7) {
                Arrcaption[0] = GameScr.taskTemplates[_char.ctaskId].name;
            } else if (_char.ctaskIndex >= 1 && _char.ctaskIndex <= 3 && _char.ctaskId == 15) {
                Arrcaption[0] = "Giao thư";
            } else if (_char.ctaskIndex == 1 && _char.ctaskId == 20) {
                Arrcaption[0] = "Vào trong hang";
            } else if (_char.ctaskIndex == 1 && _char.ctaskId == 17) {
                Arrcaption[0] = "Về đi cu ơi";
            } else if (_char.ctaskIndex == 3 && _char.ctaskId == 0) {
                _char.menuType = 1;
                Arrcaption[0] = "Nói chuyện";
                Service.openUIMenuNew(_char, Arrcaption);
                return;
            }
            // menu npc nv
            else if (_char.ctaskIndex >= 1 && _char.ctaskIndex <= 3 && _char.ctaskId == 13) {
                Arrcaption[0] = GameScr.taskTemplates[_char.ctaskId].name;
            }
        }
        if (_char.tileMap != null && _char.tileMap.map.isTestDunMap() && npcTemplateId == 0 && !_char.tileMap.map.testDun.isFinght) {
            _char.menuType = 1;
            Service.openUIMenuNew(_char, new String[]{Text.get(0, 295), Text.get(0, 296), Text.get(0, 297)});
        } else if (_char.tileMap != null && _char.tileMap.map.isChienTruong() && npcTemplateId == 25) {
            _char.menuType = 1;
            Service.openUIMenuNew(_char, new String[]{Text.get(0, 295), Text.get(0, 335)});
        } else if (_char.tileMap != null && _char.tileMap.mapID == 22 && npcTemplateId == 24) {
            _char.menuType = 1;
            Service.openUIMenuNew(_char, null);
        } else if (_char.tileMap != null && _char.tileMap.map.isWarClanMap() && npcTemplateId == 32) {
            _char.menuType = 1;
            if (_char.tileMap.mapID == 117) {
                Service.openUIMenuNew(_char, new String[]{"Đặt cược", "Rời khỏi nơi này"});
            }
            if (_char.tileMap.mapID == 118 || _char.tileMap.mapID == 119) {
                Service.openUIMenuNew(_char, new String[]{"Rời khỏi nơi này", "Tổng kết"});
            }
        } else {
            Service.openUIMenu(_char, Arrcaption);
        }
    }

    static {
        arrLevelGift = new int[]{10, 20, 30, 40, 50};
    }
}
