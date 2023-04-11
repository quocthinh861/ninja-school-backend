 package com.Server.ninja.Server;

import java.sql.ResultSet;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import com.Server.io.MySQL;

public class GiftCode
{
    protected static void inputCode(final Char _char, final String code) {
        try {
            final MySQL mySQL = new MySQL(1);
            try {
                final ResultSet red = mySQL.stat.executeQuery("SELECT * FROM `giftcode` WHERE `code` LIKE '" + Util.strSQL(code) + "' LIMIT 1;");
                if (red.first()) {
                    String text = Text.get(0, 51) + ": " + code + "\n- " + Text.get(0, 146) + "\n";
                    final byte type = red.getByte("type");
                    int limit = red.getInt("limit");
                    final boolean isDelete = red.getBoolean("Delete");
                    final boolean isCheckbag = red.getBoolean("bagCount");
                    final JSONArray listUser = (JSONArray)JSONValue.parseWithException(red.getString("listUser"));
                    final JSONArray listItem = (JSONArray)JSONValue.parseWithException(red.getString("listItem"));
                    if (limit == 0) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 144));
                    }
                    else {
                        if (type == 1) {
                            for (int i = 0; i < listUser.size(); ++i) {
                                final int playerId = Integer.parseInt(listUser.get(i).toString());
                                if (playerId == _char.user.userId) {
                                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 145));
                                    return;
                                }
                            }
                        }
                        if (isCheckbag && listItem.size() > _char.user.player.ItemBagSlotNull()) {
                            GameCanvas.startOKDlg(_char.user.session, String.format(Text.get(0, 151), listItem.size()));
                        }
                        else {
                            for (int i = 0; i < listItem.size(); ++i) {
                                final JSONObject item = (JSONObject)listItem.get(i);
                                final byte itemType = Byte.parseByte(item.get((Object)"type").toString());
                                final String itemValue = item.get((Object)"value").toString();
                                if (itemType == 0) {
                                    final Item it = Item.parseItem(itemValue);
                                    if (it != null) {
                                        if (it.template.isUpToUp) {
                                            _char.user.player.ItemBagAddQuantity(it);
                                        }
                                        else {
                                            _char.user.player.ItemBagAdd(it);
                                        }
                                        text += it.template.name;
                                    }
                                }
                                else if (itemType == 1) {
                                    final int yen = Integer.parseInt(itemValue);
                                    _char.user.player.upCoinLock(yen, (byte)2);
                                    text = text + Util.getFormatNumber(yen) + Text.get(0, 147);
                                }
                                else if (itemType == 2) {
                                    final int xu = Integer.parseInt(itemValue);
                                    _char.user.player.upCoin(xu, (byte)1);
                                    text = text + Util.getFormatNumber(xu) + Text.get(0, 148);
                                }
                                else if (itemType == 3) {
                                    final int luong = Integer.parseInt(itemValue);
                                    _char.user.player.upGold(luong, (byte)2);
                                    text = text + Util.getFormatNumber(luong) + Text.get(0, 149);
                                }
                                else if (itemType == 4) {
                                    final long exp = Integer.parseInt(itemValue);
                                    _char.updateExp(exp);
                                    text = text + Util.getFormatNumber(exp) + Text.get(0, 150);
                                }
                                if (i < listItem.size() - 1) {
                                    text += ", ";
                                }
                            }
                            if (isDelete) {
                                mySQL.stat.executeUpdate("DELETE form `giftcode` WHERE `code` LIKE '" + Util.strSQL(code) + "';");
                            }
                            else {
                                if (limit != -1) {
                                    --limit;
                                }
                                listUser.add((Object)_char.user.userId);
                                mySQL.stat.executeUpdate("UPDATE `giftcode` SET `limit` = " + limit + ", `listUser` = '" + listUser.toJSONString() + "' WHERE `code` LIKE '" + Util.strSQL(code) + "';");
                            }
                            Service.AlertMessage(_char, Text.get(0, 51), text);
                        }
                    }
                }
                else {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 142));
                }
            }
            finally {
                mySQL.close();
            }
        }
        catch (Exception e) {
            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 143));
            e.printStackTrace();
        }
    }
}
