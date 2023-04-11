package com.Server.ninja.Server;

import com.Server.io.MySQL;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author KHONG MINH TIEN
 */
public class RewardTop {
    
    protected static void Reward(final Char _char,short npcId) {
        try {
            final MySQL mySQL = new MySQL(1);
            try {
                final ResultSet red = mySQL.stat.executeQuery("SELECT * FROM `reward` WHERE `username` LIKE '" + _char.cName + "' LIMIT 1;");
                if (red.first()) {
                    final boolean isCheckbag = red.getBoolean("bagCount");
                    final JSONArray listItem = (JSONArray)JSONValue.parseWithException(red.getString("listItem"));
                    if (isCheckbag && listItem.size() > _char.user.player.ItemBagSlotNull()) {
                        GameCanvas.startOKDlg(_char.user.session, String.format(Text.get(0, 151), listItem.size()));
                    } else {
                        for (int i = 0; i < listItem.size(); ++i) {
                            final JSONObject item = (JSONObject)listItem.get(i);
                            final byte itemType = Byte.parseByte(item.get("type").toString());
                            final String itemValue = item.get("value").toString();
                            if (itemType == 0) {
                                final Item it = Item.parseItem(itemValue);
                                if (it != null) {
                                    if (it.template.isUpToUp) {
                                        _char.user.player.ItemBagAddQuantity(it);
                                    }
                                    else {
                                        _char.user.player.ItemBagAdd(it);
                                    }
                                }
                            }
                            else if (itemType == 1) {
                                final int yen = Integer.parseInt(itemValue);
                                _char.user.player.upCoinLock(yen, (byte)2);
                            }
                            else if (itemType == 2) {
                                final int xu = Integer.parseInt(itemValue);
                                _char.user.player.upCoin(xu, (byte)1);
                            }
                            else if (itemType == 3) {
                                final int luong = Integer.parseInt(itemValue);
                                _char.user.player.upGold(luong, (byte)2);
                            }
                            else if (itemType == 4) {
                                final long exp = Integer.parseInt(itemValue);
                                _char.updateExp(exp);
                            }
                        }
                        mySQL.stat.executeUpdate("DELETE FROM reward WHERE username LIKE '" + _char.cName + "';");
                    }
                } else {
                    Service.openUISay(_char, npcId, "Bạn không có trong danh sách nhận quà hoặc bạn đã nhận quà rồi.");
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
