 package com.Server.ninja.Server;

import com.Server.io.API_Card;
import com.Server.ninja.option.ItemOption;
import com.Server.ninja.template.ItemOptionTemplate;
import lombok.val;

import java.util.ArrayList;

public class Player extends Char
{
    protected int xu;
    protected int xuBox;
    protected int yen;
    protected byte bagCount;
    protected ArrayList<Short> bagOpen;
    protected Item[] ItemBag;
    protected Item[] ItemBST;// bộ sưu tập KMT
    protected Item[] ItemCaiTrang;// bộ sưu tập KMT

    protected Item[] ItemBox;
    protected ArrayList<Friend> vFriend;
    protected ArrayList<Integer> LevelGift;
    protected int epoint;
    protected int tradeCharId;
    protected int tradeCharIdwait;
    protected int delayTrade;
    protected int delayCancelTrade;
    protected boolean isTrade;
    protected Item[] itemTrade;
    protected int tradeCoin;
    protected byte tradeType;
    protected int delayLiveGold;
    protected String viewPlayer;
    protected int menuCaiTrang = 0;// bộ sưu tập KMT
    protected String resigerUname;
    protected String resigerPass;

    protected Player(final User user) {
        super(user);
        this.xu = 0;
        this.xuBox = 0;
        this.yen = 0;
        this.bagCount = 30;
        this.bagOpen = new ArrayList<Short>();
        this.ItemBag = null;
        this.ItemBox = null;
        this.ItemCaiTrang = null;
        this.ItemBST = null;
        this.vFriend = new ArrayList<Friend>();
        this.LevelGift = new ArrayList<Integer>();
        this.epoint = 0;
        this.tradeCharId = -9999;
        this.tradeCharIdwait = -9999;
        this.delayTrade = 0;
        this.delayCancelTrade = 0;
        this.isTrade = false;
        this.itemTrade = new Item[12];
        this.tradeCoin = 0;
        this.tradeType = 0;
        this.delayLiveGold = 0;
        this.viewPlayer = null;
    }
    
    protected Char getMyChar() {
        if (this.isToNhanban) {
            return this.Nhanban;
        }
        return this;
    }
    
    protected byte ItemBagSlotNull() {
        byte num = 0;
        for (Item item : this.ItemBag) {
            if (item == null) {
                ++num;
            }
        }
        return num;
    }
    
    protected byte ItemBoxSlotNull() {
        byte num = 0;
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            if (this.ItemBox[i] == null) {
                ++num;
            }
        }
        return num;
    }
    
    protected Item ItemBag(final byte index) {
        try {
            return this.ItemBag[index];
        }
        catch (Exception e) {
            return null;
        }
    }
    
    protected Item ItemBox(final byte index) {
        try {
            return this.ItemBox[index];
        }
        catch (Exception e) {
            return null;
        }
    }
    
    protected int ItemBagQuantity(final short itemplateid) {
        int quantity = 0;
        for (byte i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.template.id == itemplateid) {
                quantity += item.quantity;
            }
        }
        return quantity;
    }
    
    protected int ItemBagQuantityLock(final short itemplateid) {
        int quantity = 0;
        for (byte i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.template.id == itemplateid && item.isLock == true) {
                quantity += item.quantity;
            }
        }
        return quantity;
    }
    
    protected int ItemBagQuantityUnLock(final short itemplateid) {
        int quantity = 0;
        for (byte i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.template.id == itemplateid && item.isLock == false) {
                quantity += item.quantity;
            }
        }
        return quantity;
    }
    
    protected Item ItemBag(final short itemplateid) {
        for (byte i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.template.id == itemplateid) {
                return item;
            }
        }
        return null;
    }
    
    public byte ItemBagIndex(final short itemplateid, final boolean lock) {
        for (byte i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.template.id == itemplateid && item.isLock == lock) {
                return i;
            }
        }
        return -1;
    }
    
    public byte ItemBoxIndex(final short itemplateid, final boolean lock) {
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            final Item item = this.ItemBox[i];
            if (item != null && item.template.id == itemplateid && item.isLock == lock) {
                return i;
            }
        }
        return -1;
    }
    
    protected byte ItemBagIndexNull() {
        for (byte i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item == null) {
                return i;
            }
        }
        return -1;
    }
    
    protected byte ItemBoxIndexNull() {
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            final Item item = this.ItemBox[i];
            if (item == null) {
                return i;
            }
        }
        return -1;
    }
    
    protected static void ItemBagSort(final Player _char) {
        if (_char.user.player.menuCaiTrang !=0) {
            return;
        }
        try {
            for (byte i = 0; i < _char.ItemBag.length; ++i) {
                if (_char.ItemBag[i] != null && !_char.ItemBag[i].isExpires && _char.ItemBag[i].template.isUpToUp) {
                    for (byte j = (byte)(i + 1); j < _char.ItemBag.length; ++j) {
                        if (_char.ItemBag[j] != null && !_char.ItemBag[i].isExpires && _char.ItemBag[j].template.id == _char.ItemBag[i].template.id && _char.ItemBag[j].isLock == _char.ItemBag[i].isLock) {
                            final Item item = _char.ItemBag[i];
                            item.quantity += _char.ItemBag[j].quantity;
                            _char.ItemBag[j] = null;
                        }
                    }
                }
            }
            for (byte i = 0; i < _char.ItemBag.length; ++i) {
                if (_char.ItemBag[i] == null) {
                    for (byte j = (byte)(i + 1); j < _char.ItemBag.length; ++j) {
                        if (_char.ItemBag[j] != null) {
                            _char.ItemBag[i] = _char.ItemBag[j];
                            _char.ItemBag[i].indexUI = i;
                            _char.ItemBag[j] = null;
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception ex) {}
        Service.BagSort(_char);
    }
    
    protected static void ItemBoxSort(final Player _char) {
        if (TileMap.NPCNear(_char, (short)5) == null || _char.menuCaiTrang != 0) {
            return;
        }
        try {
            for (byte i = 0; i < _char.ItemBox.length; ++i) {
                if (_char.ItemBox[i] != null && !_char.ItemBox[i].isExpires && _char.ItemBox[i].template.isUpToUp) {
                    for (byte j = (byte)(i + 1); j < _char.ItemBox.length; ++j) {
                        if (_char.ItemBox[j] != null && !_char.ItemBox[i].isExpires && _char.ItemBox[j].template.id == _char.ItemBox[i].template.id && _char.ItemBox[j].isLock == _char.ItemBox[i].isLock) {
                            final Item item = _char.ItemBox[i];
                            item.quantity += _char.ItemBox[j].quantity;
                            _char.ItemBox[j] = null;
                        }
                    }
                }
            }
            for (byte i = 0; i < _char.ItemBox.length; ++i) {
                if (_char.ItemBox[i] == null) {
                    for (byte j = (byte)(i + 1); j < _char.ItemBox.length; ++j) {
                        if (_char.ItemBox[j] != null) {
                            _char.ItemBox[i] = _char.ItemBox[j];
                            _char.ItemBox[i].indexUI = i;
                            _char.ItemBox[j] = null;
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception ex) {}
        Service.BoxSort(_char);
    }
    
    protected static void BoxIn(final Player _char, final int coin) {
        if (coin > 0 && coin <= _char.xu) {
            if (coin + (long)_char.xuBox > 2000000000L) {
                GameCanvas.startOKDlg(_char.user.session, String.format(Text.get(0, 37), 2000000000 - _char.xuBox));
            }
            else {
                _char.xuBox += coin;
                _char.xu -= coin;
                Service.BoxIn(_char, coin);
            }
        }
    }
    
    protected static void BoxOut(final Player _char, final int coin) {
        if (coin > 0 && coin <= _char.xuBox) {
            if (coin + (long)_char.xu > 2000000000L) {
                GameCanvas.startOKDlg(_char.user.session, String.format(Text.get(0, 38), 2000000000 - _char.xu));
            }
            else {
                _char.xuBox -= coin;
                _char.xu += coin;
                Service.BoxOut(_char, coin);
            }
        }
    }
    
    protected static void inputNumSplit(final Player _char, final byte indexItem, final int numSplit) {
        final Item item = _char.ItemBag(indexItem);
        if (item != null && numSplit > 0) {
            if (item.quantity > numSplit) {
                final byte indexUI = _char.ItemBagIndexNull();
                if (indexUI != -1) {
                    final Item item2 = item.clone();
                    item2.quantity = numSplit;
                    item2.indexUI = indexUI;
                    _char.ItemBag[indexUI] = item2;
                    final Item item3 = item;
                    item3.quantity -= numSplit;
                    Service.ItemBagADD(_char, item2);
                    Service.ItemUseUptoup(_char, indexItem, numSplit);
                }
                else {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
            }
            else {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 16));
            }
        }
    }
    
    protected boolean ItemBagAdd(final Item item) {
        try {
            final byte indexUI = this.ItemBagIndexNull();
            this.ItemBag[indexUI] = item;
            item.indexUI = indexUI;
            item.typeUI = 3;
            Service.ItemBagADD(this, item);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    protected boolean ItemBagAdd(final byte indexUI, final Item item) {
        try {
            this.ItemBag[indexUI] = item;
            item.indexUI = indexUI;
            item.typeUI = 3;
            Service.ItemBagADD(this, item);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    protected boolean ItemBagAddQuantity(final Item item) {
        if (!item.isExpires) {
            try {
                final byte indexUI = this.ItemBagIndex(item.template.id, item.isLock);
                final Item item2 = this.ItemBag[indexUI];
                item2.quantity += item.quantity;
                item.indexUI = indexUI;
                Service.ItemBagADDQuantity(this, item);
                return true;
            }
            catch (Exception e) {
                return this.ItemBagAdd(item);
            }
        }
        return this.ItemBagAdd(item);
    }
    
    protected boolean ItemBagClear(final byte indexUI) {
        try {
            this.ItemBag[indexUI] = null;
            Service.ItemBagClear(this, indexUI);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    protected boolean ItemBagUsesHalloween(final short itemtemplateid, final int quantity, final boolean isLock) {
        int num = 0;
        for (byte i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.template.id == itemtemplateid && item.isLock == isLock) {
                if (num + item.quantity >= quantity) {
                    this.ItemBagUseHalloween(i, quantity - num,isLock);
                    break;
                }
                num += item.quantity;
                this.ItemBagUseHalloween(i, item.quantity,isLock);
            }
        }
        return false;
    }
    
    protected boolean ItemBagUseHalloween(final byte indexUI, final int quantity, final boolean isLock) {
        try {
            final Item item = this.ItemBag[indexUI];
            if (item.quantity > quantity && item.isLock == isLock ) {
                final Item item2 = item;
                item2.quantity -= quantity;
            }
            else {
                this.ItemBag[indexUI] = null;
            }
            Service.ItemUseUptoup(this, indexUI, quantity);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    protected boolean ItemBagUse(final byte indexUI, final int quantity) {
        try {
            final Item item = this.ItemBag[indexUI];
            if (item.quantity > quantity) {
                final Item item2 = item;
                item2.quantity -= quantity;
            }
            else {
                this.ItemBag[indexUI] = null;
            }
            Service.ItemUseUptoup(this, indexUI, quantity);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    protected boolean ItemBagUses(final short itemtemplateid, final int quantity) {
        int num = 0;
        for (byte i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.template.id == itemtemplateid) {
                if (num + item.quantity >= quantity) {
                    this.ItemBagUse(i, quantity - num);
                    break;
                }
                num += item.quantity;
                this.ItemBagUse(i, item.quantity);
            }
        }
        return false;
    }
    
    protected static void itemBodyToBag(final Char _char, final byte indexitem) {
        try {
            final Item item = _char.ItemBody[indexitem];
            final byte indexUI = _char.user.player.ItemBagIndexNull();
            if (item != null) {
                if (indexUI != -1) {
                    _char.user.player.ItemBag[indexUI] = item;
                    item.typeUI = 3;
                    item.indexUI = indexUI;
                    _char.ItemBody[indexitem] = null;
                    ThoiTrang.removeThoiTrang(_char, item.template.id);
                    Service.itemBodyToBag(_char, indexitem, indexUI);
                    if (item.template.type == 10) {
                        Service.MELoadThuNuoi(_char, _char.mobMe = null);
                        updateThuNuoiPlayer(_char);
                    }
                    if (item.isItemBodyEffect()) {
                        removeEffect(_char, Effect.itemEffectId(item.template.id));
                    }
                    Service.updateInfoMe(_char);
                    updateInfoPlayer(_char);
                    if (_char.ItemBody[10] != null) {
                        updateThuNuoiPlayer(_char);
                    }
                    if (item.template.type == 12) {
                        updateCoatPlayer(_char);
                    }
                    if (item.template.type == 13) {
                        updateGiaTocPlayer(_char);
                    }
                }
                else {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
            }
        }
        catch (Exception ex) {}
    }
    
    protected static void itemBoxToBag(final Char _char, final byte indexitem) {
        if (TileMap.NPCNear(_char, (short)5) == null) {
            return;
        }
        try {
            switch (_char.user.player.menuCaiTrang) {
                case 0: {
                    final Item item = _char.user.player.ItemBox[indexitem];
                    if (item != null) {
                        byte indexUI = -1;
                        if (!item.isExpires) {
                            _char.user.player.ItemBagIndex(item.template.id, item.isLock);
                        }
                        if (indexUI == -1 || !item.template.isUpToUp) {
                            indexUI = _char.user.player.ItemBagIndexNull();
                        }
                        if (indexUI != -1) {
                            _char.user.player.ItemBox[indexitem] = null;
                            if (_char.user.player.ItemBag[indexUI] == null) {
                                _char.user.player.ItemBag[indexUI] = item;
                                item.typeUI = 3;
                                item.indexUI = indexUI;
                            } else {
                                final Item item2 = _char.user.player.ItemBag[indexUI];
                                item2.quantity += item.quantity;
                            }
                            Service.itemBoxToBag(_char, indexitem, indexUI);
                        } else {
                            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                        }
                    }
                    break;
                }
                // bộ sưu tập Khổng Minh Tiến
                case 1: {
                    final Item itemCT = _char.user.player.ItemCaiTrang[10];
                    if (itemCT == null) {
                        for (int i = 0; i < _char.user.player.ItemBST.length; i++) {
                            if(_char.user.player.ItemBST[i] == null) {
                                Service.ServerMessage(_char,"Bạn chưa đủ điểm bộ sưu tập để sử dụng.");
                                return;
                            }
                        }
                        short itemId = (short) (_char.cgender == 1 ? 711:714);
                        Item items = new Item(ItemOption.arrOptionDefault(itemId,(byte) 0),itemId,1,-1,true,(byte) 0,0);
                        _char.user.player.ItemCaiTrang[10] = items;
                        _char.user.player.ItemCaiTrang[10].upgradeCT( (byte) 1,itemId);
                        _char.user.player.ItemCaiTrang[10].indexUI = 10;
                        Service.ServerMessage(_char,"Tạo thành công cải trang " + items.template.name);
                    } else {
                        if(_char.user.player.ItemCaiTrang[10].upgrade >= 10) {
                            Service.ServerMessage(_char,"Cải trang đã được nâng cấp tối đa.");
                        } else {
                            byte upgradeTemp = 16;
                            for (int j = 0; j <= 8; j++) {
                                if(_char.user.player.ItemBST[j] == null) {
                                    return;
                                }
                                if(upgradeTemp > _char.user.player.ItemBST[j].upgrade) {
                                    upgradeTemp = _char.user.player.ItemBST[j].upgrade;
                                }
                            }
                            if (upgradeTemp <= _char.user.player.ItemCaiTrang[10].upgrade) {
                                Service.ServerMessage(_char,"Bạn chưa đủ điểm bộ sưu tập để nâng cấp.");
                            } else {
                                short itemId = (short) (_char.cgender == 1 ? 711:714);
                                byte updatenext = (byte) (upgradeTemp - _char.user.player.ItemCaiTrang[10].upgrade);
                                _char.user.player.ItemCaiTrang[10].upgradeCT( updatenext,itemId);
                                _char.user.player.ItemCaiTrang[10].indexUI = 10;
                                Service.ServerMessage(_char, String.format(Text.get(0,401),_char.user.player.ItemCaiTrang[10].template.name,upgradeTemp));
                            }
                        }
                    }
                    break;
                }
                case 2: {
                    Item useCT = _char.user.player.ItemCaiTrang[indexitem];
                    _char.typeCaiTrang = indexitem;
                    Service.updateInfoMe(_char);
                    updateInfoPlayer(_char);
                    GameCanvas.startOKDlg(_char.user.session,"Sử dụng " + useCT.template.name);
                    break;
                }
            }
        }
        catch (Exception ex) {}
    }
    
    protected static void itemBagToBox(final Char _char, final byte indexitem) {
        if (TileMap.NPCNear(_char, (short)5) == null || _char.user.player.menuCaiTrang != 0) {
            return;
        }
        try {
            final Item item = _char.user.player.ItemBag[indexitem];
            if (item != null) {
                byte indexUI = -1;
                if (!item.isExpires) {
                    _char.user.player.ItemBoxIndex(item.template.id, item.isLock);
                }
                if (indexUI == -1 || !item.template.isUpToUp) {
                    indexUI = _char.user.player.ItemBoxIndexNull();
                }
                if (item.isTypeTask()) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 53));
                }
                else if (indexUI != -1) {
                    _char.user.player.ItemBag[indexitem] = null;
                    if (_char.user.player.ItemBox[indexUI] == null) {
                        _char.user.player.ItemBox[indexUI] = item;
                        item.typeUI = 4;
                        item.indexUI = indexUI;
                    }
                    else {
                        final Item item2 = _char.user.player.ItemBox[indexUI];
                        item2.quantity += item.quantity;
                    }
                    Service.itemBagToBox(_char, indexitem, indexUI);
                }
                else {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 36));
                }
            }
        }
        catch (Exception ex) {}
    }
    
    protected static void itemMonToBag(final Char _char, final byte indexitem) {
        try {
            final Item item = _char.ItemMounts[indexitem];
            final byte indexUI = _char.user.player.ItemBagIndexNull();
            if (item != null) {
                if (indexitem == 4 && (_char.ItemMounts[0] != null || _char.ItemMounts[1] != null || _char.ItemMounts[2] != null || _char.ItemMounts[3] != null)) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 23));
                }
                else if (indexUI != -1) {
                    _char.user.player.ItemBag[indexUI] = item;
                    item.typeUI = 3;
                    item.indexUI = indexUI;
                    _char.ItemMounts[indexitem] = null;
                    if (item.isExpires) {
                        _char.isThuCuoiHetHan = false;
                    }
                    ThoiTrang.removeThoiTrang(_char, item.template.id);
                    Service.ItemMonToBag(_char, indexitem, indexUI);
                    LoadThuCuoi(_char);
                }
                else {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
            }
        }
        catch (Exception ex) {}
    }
    protected static void itemViThuToBag(final Char _char, final byte indexitem) {
        try {
            final Item item = _char.arrItemViThu[indexitem];
            final byte indexUI = _char.user.player.ItemBagIndexNull();
            if (item != null) {
                if (indexitem == 4 && (_char.arrItemViThu[0] != null || _char.arrItemViThu[1] != null || _char.arrItemViThu[2] != null || _char.arrItemViThu[3] != null)) {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 411));
                }
                else if (indexUI != -1) {
                    _char.user.player.ItemBag[indexUI] = item;
                    item.typeUI = 3;
                    item.indexUI = indexUI;
                    _char.arrItemViThu[indexitem] = null;
                    if (indexitem == 4) {
                        _char.mobViThu = null;
                    }
                    updateViThu(_char);
                    LoadViThu(_char);
                }
                else {
                    GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
                }
            }
        }
        catch (Exception ex) {}
    }
    protected static void PlayerLoadHP(final Char _char) {
        try {
            for (int i = 0; i < _char.tileMap.numPlayer; ++i) {
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null && _char.tileMap.aCharInMap.get(i).charID != _char.charID) {
                    Service.PlayerLoadHP(_char.tileMap.aCharInMap.get(i), _char.charID, _char.cHP);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    protected static void updateInfoPlayer(final Char _char) {
        try {
            for (int i = 0; i < _char.tileMap.numPlayer; ++i) {
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null && _char.tileMap.aCharInMap.get(i).charID != _char.charID) {
                    Service.updateInfoPlayer(_char.tileMap.aCharInMap.get(i), _char);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    protected static void updateThuNuoiPlayer(final Char _char) {
        try {
            for (int i = 0; i < _char.tileMap.numPlayer; ++i) {
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null && _char.tileMap.aCharInMap.get(i).charID != _char.charID) {
                    Service.PlayerLoadThuNuoi(_char.tileMap.aCharInMap.get(i), _char.charID, _char.mobMe);
                }
            }
        }
        catch (Exception ex) {}
    }
    protected static void updateCoatPlayer(final Char _char) {
        try {
            for (int i = 0; i < _char.tileMap.numPlayer; ++i) {
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null && _char.tileMap.aCharInMap.get(i).charID != _char.charID) {
                    Service.PlayerLoadAoChoang(_char.tileMap.aCharInMap.get(i), _char.charID, _char.cHP, _char.cMaxHP(), _char.coat());
                }
            }
        }
        catch (Exception ex) {}
    }
    
    protected static void updateGiaTocPlayer(final Char _char) {
        try {
            for (int i = 0; i < _char.tileMap.numPlayer; ++i) {
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null && _char.tileMap.aCharInMap.get(i).charID != _char.charID) {
                    Service.PlayerLoadGiaToc(_char.tileMap.aCharInMap.get(i), _char.charID, _char.cHP, _char.cMaxHP(), _char.glove());
                }
            }
        }
        catch (Exception ex) {}
    }
    
    protected static void LoadThuCuoi(final Char _char) {
        Service.updateInfoMe(_char);
        try {
            for (int i = 0; i < _char.tileMap.numPlayer; ++i) {
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null) {
                    Service.LoadThuCuoi(_char.tileMap.aCharInMap.get(i), _char.charID, _char.ItemMounts);
                }
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null && _char.charID != _char.tileMap.aCharInMap.get(i).charID) {
                    Service.updateInfoPlayer(_char.tileMap.aCharInMap.get(i), _char);
                }
            }
        }
        catch (Exception ex) {}
    }

    protected static void LoadViThu(final Char _char) {
        Service.updateInfoMe(_char);
        try {
            for (int i = 0; i < _char.tileMap.numPlayer; ++i) {
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null) {
                    Service.onVithuInfo(_char.tileMap.aCharInMap.get(i), _char.charID, _char.arrItemViThu);
                }
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null && _char.charID != _char.tileMap.aCharInMap.get(i).charID) {
                    Service.updateInfoPlayer(_char.tileMap.aCharInMap.get(i), _char);
                }
            }
        }
        catch (Exception ex) {}
    }
    protected static void updateViThu(final Char _char) {
        try {
            for (int i = 0; i < _char.tileMap.numPlayer; ++i) {
                if (_char.tileMap.aCharInMap.get(i).user != null && _char.tileMap.aCharInMap.get(i).user.session != null) {
                    Service.onChangeVithu(_char.tileMap.aCharInMap.get(i), _char.charID, _char.mobViThu);
                }
            }
        }
        catch (Exception ex) {}
    }
    // nâng cấp trang bị
    protected static void Uppearl(final Char _char, final Item[] items, final boolean isCoin) {
        final byte indexUI = _char.user.player.ItemBagIndexNull();
        if (indexUI == -1) {
            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
        }
        else {
            int crys = 0;
            for (byte i = 0; i < items.length; ++i) {
                final Item item = items[i];
                if (item != null) {
                    crys += GameScr.crystals[item.template.id];
                }
            }
            short id = 0;
            for (byte j = 0; j < GameScr.crystals.length; ++j) {
                if (crys > GameScr.crystals[j]) {
                    id = (short)(j + 1);
                }
            }
            if (id > 11) {
                id = 11;
            }
            final int percen = crys * 100 / GameScr.crystals[id];
            if (percen < 40) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 79));
            }
            else {
                if (isCoin) {
                    if (GameScr.coinUpCrystals[id] > _char.user.player.xu) {
                        return;
                    }
                    _char.user.player.upCoin(-GameScr.coinUpCrystals[id], (byte)0);
                }
                else {
                    if (GameScr.coinUpCrystals[id] > _char.user.player.xu + (long)_char.user.player.yen) {
                        return;
                    }
                    if (_char.user.player.yen >= GameScr.coinUpCrystals[id]) {
                        _char.user.player.upCoinLock(-GameScr.coinUpCrystals[id], (byte)1);
                    }
                    else {
                        final int coin = GameScr.coinUpCrystals[id] - _char.user.player.yen;
                        _char.user.player.upCoinLock(-_char.user.player.yen, (byte)1);
                        _char.user.player.upCoin(-coin, (byte)0);
                    }
                }
                final boolean success = Util.nextInt(1, 100) <= percen;
                Item item2;
                if (success) {
                    item2 = new Item(null, id, 1, -1, true, (byte)0, 0);
                }
                else {
                    item2 = new Item(null, (short)(id - 1), 1, -1, true, (byte)0, 0);
                }
                for (byte k = 0; k < items.length; ++k) {
                    if (items[k] != null) {
                        _char.user.player.ItemBag[items[k].indexUI] = null;
                    }
                }
                _char.user.player.ItemBag[indexUI] = item2;
                item2.indexUI = indexUI;
                item2.typeUI = 3;
                if (isCoin) {
                    Service.Uppearl(_char, (byte)(success ? 1 : 0), indexUI, item2);
                }
                else {
                    Service.UppearlLock(_char, (byte)(success ? 1 : 0), indexUI, item2);
                }
            }
        }
    }
    // đập đồ
    protected static void UpGrade(final Char _char, final Item item, final Item[] items, final boolean isGold) {
        boolean keep = false;
        int crys = 0;
        int gold = 0;
        for (byte i = 0; i < items.length; ++i) {
            final Item item2 = items[i];
            if (item2 != null) {
                if (item2.isTypeCrystal()) {
                    crys += GameScr.crystals[item2.template.id];
                }
                else {
                    if (item2.template.id != 475 && (item2.template.id != 242 || item.upgrade >= 8) && (item2.template.id != 284 || item.upgrade >= 12) && (item2.template.id != 285 || item.upgrade >= 14)) {
                        GameCanvas.startOKDlg(_char.user.session, Text.get(0, 78));
                        return;
                    }
                    keep = true;
                }
            }
        }
        int coins;
        int percen;
        if (item.isTypeWeapon()) {
            coins = GameScr.coinUpWeapons[item.upgrade];
            percen = crys * 100 / GameScr.upWeapon[item.upgrade];
            if (percen > GameScr.maxPercents[item.upgrade]) {
                percen = GameScr.maxPercents[item.upgrade];
            }
        }
        else if (item.isTypeClothe()) {
            coins = GameScr.coinUpClothes[item.upgrade];
            percen = crys * 100 / GameScr.upClothe[item.upgrade];
            if (percen > GameScr.maxPercents[item.upgrade]) {
                percen = GameScr.maxPercents[item.upgrade];
            }
        }
        else {
            coins = GameScr.coinUpAdorns[item.upgrade];
            percen = crys * 100 / GameScr.upAdorn[item.upgrade];
            if (percen > GameScr.maxPercents[item.upgrade]) {
                percen = GameScr.maxPercents[item.upgrade];
            }
        }
        if (isGold) {
            percen *= (int)1.5;
            gold = GameScr.goldUps[item.upgrade];
        }
        if (coins > _char.user.player.yen + (long)_char.user.player.xu || gold > _char.user.luong) {
            return;
        }
        _char.user.player.upGold(-gold, (byte)0);
        if (coins <= _char.user.player.yen) {
            _char.user.player.upCoinLock(-coins, (byte)1);
        }
        else {
            final int coin = coins - _char.user.player.yen;
            _char.user.player.upCoinLock(-_char.user.player.yen, (byte)1);
            _char.user.player.upCoin(-coin, (byte)0);
        }
        for (byte i = 0; i < items.length; ++i) {
            final Item item2 = items[i];
            if (item2 != null) {
                _char.user.player.ItemBag[item2.indexUI] = null;
            }
        }
        final boolean success = Util.nextInt(1, Util.nextInt(50, 100)) <= percen;
        item.isLock = true;
        Util.log("dap do percen " + percen);
        if (success) {
            if (item.upgrade < item.getUpMax()) {
                item.upgradeNext((byte)1);
                if (_char.ctaskId == 12 && _char.ctaskIndex == 1 && item.template.type == 1) {
                    _char.uptaskMaint();
                }
                if (_char.ctaskId == 12 && _char.ctaskIndex == 2 && (item.template.type == 3 || item.template.type == 9 || item.template.type == 5 ||item.template.type == 7)) {
                    _char.uptaskMaint();
                }
                if (_char.ctaskId == 12 && _char.ctaskIndex == 3 && (item.template.type == 2 || item.template.type == 4 || item.template.type == 6 ||item.template.type == 8)) {
                    _char.uptaskMaint();
                }
            }
        }
        else if (!keep) {
            item.upgradeNext((byte)(-(item.upgrade - item.KeepUpgrade())));
        }
        Service.upGrade(_char, (byte)(success ? 1 : 0), item.upgrade);
    }
    
    protected static void splitItem(final Char _char, final Item item) {
        int num = 0;
        if (item.isTypeWeapon()) {
            for (byte i = 0; i < item.upgrade; ++i) {
                num += GameScr.upWeapon[i];
            }
        }
        else if (item.isTypeClothe()) {
            for (byte i = 0; i < item.upgrade; ++i) {
                num += GameScr.upClothe[i];
            }
        }
        else {
            for (byte i = 0; i < item.upgrade; ++i) {
                num += GameScr.upAdorn[i];
            }
        }
        num /= 2;
        final Item[] arrItem = new Item[24];
        byte num2 = 0;
        for (int n = GameScr.crystals.length - 1; n >= 0; --n) {
            if (num >= GameScr.crystals[n]) {
                arrItem[num2] = new Item(null, (short)n, 1, -1, true, (byte)0, 0);
                num -= GameScr.crystals[n];
                ++n;
                ++num2;
            }
        }
        if (num2 > _char.user.player.ItemBagSlotNull()) {
            GameCanvas.startOKDlg(_char.user.session, Text.get(0, 15));
        }
        else {
            for (byte j = 0; j < arrItem.length; ++j) {
                final Item item2 = arrItem[j];
                if (item2 != null) {
                    final byte indexUI = _char.user.player.ItemBagIndexNull();
                    _char.user.player.ItemBag[indexUI] = item2;
                    item2.indexUI = indexUI;
                    item2.typeUI = 3;
                }
            }
            item.upgradeNext((byte)(-item.upgrade));
            Service.splitItem(_char, num2, arrItem);
        }
    }
    
    protected static void doConvertUpgrade(final Char _char, final Item item1, final Item item2, final Item item3) {
        if (item1.template.type != item2.template.type || item2.template.level < item1.template.level) {
            Service.ServerMessage(_char, Text.get(0, 80));
        }
        else if ((item1.upgrade > 10 && item3.template.id == 269) || (item1.upgrade > 13 && item3.template.id == 270)) {
            Service.ServerMessage(_char, Text.get(0, 81));
        }
        else {
            item1.isLock = true;
            item2.isLock = true;
            final byte upgrade = item1.upgrade;
            item1.upgradeNext((byte)(-item1.upgrade));
            item2.upgradeNext(upgrade);
            Service.ConvertUpgrade(_char, item1, item2);
            _char.user.player.ItemBagUse((byte)item3.indexUI, 1);
        }
    }
    
    protected synchronized void upCoin(long num, final byte type) {
        final long xunew = this.xu + num;
        if (xunew > 2000000000L) {
            num = 2000000000L - this.xu;
        }
        else if (xunew < -2000000000L) {
            num = -2000000000L - this.xu;
        }
        this.xu += (int)num;
        if (type == 1) {
            Service.upCoinBag(this, (int)num);
        }
        else if (type == 2) {
            Service.inputCoinClan(this);
        }
    }
    
    protected synchronized void upCoinLock(long num, final byte type) {
        final long yennew = this.yen + num;
        if (yennew > 2000000000L) {
            num = 2000000000L - this.yen;
        }
        else if (yennew < -2000000000L) {
            num = -2000000000L - this.yen;
        }
        this.yen += (int)num;
        if (type == 1) {
            Service.upCoinLock(this, (int)num);
        }
        else if (type == 2) {
            Service.upCoinLock(this, (int)num);
            Service.ServerMessage(this, String.format(Text.get(0, 73), num));
        }
    }
    
    protected synchronized void upGold(long num, final byte type) {
        final long luongnew = this.user.luong + num;
        if (luongnew > 2000000000L) {
            num = 2000000000L - this.user.luong;
        }
        else if (luongnew < -2000000000L) {
            num = -2000000000L - this.user.luong;
        }
        final User user = this.user;
        user.luong += (int)num;
        if (type == 1) {
            Service.loadGold(this);
        }
        else if (type == 2) {
            Service.upGold(this, (int)num);
        }
    }
    
    protected void addFriend(final String playerName) {
        try {
            final Player player = Client.getPlayer(playerName);
            if (player != null) {
                for (short i = 0; i < this.vFriend.size(); ++i) {
                    final Friend friend = this.vFriend.get(i);
                    if (friend.friendName.equals(playerName)) {
                        Service.ServerMessage(this.getMyChar(), String.format(Text.get(0, 41), player.cName));
                        return;
                    }
                }
                byte type = 0;
                for (short j = 0; j < player.vFriend.size(); ++j) {
                    final Friend friend2 = player.vFriend.get(j);
                    if (friend2.friendName.equals(this.cName)) {
                        type = (friend2.type = 1);
                        break;
                    }
                }
                final Friend friend = new Friend(playerName, type);
                this.vFriend.add(friend);
                if (friend.type == 1) {
                    Service.addFriend(player, this.cName, friend.type);
                }
                else {
                    Service.FriendInvate(player, this.cName);
                }
                Service.addFriend(this.getMyChar(), friend.friendName, friend.type);
                if (this.getMyChar().ctaskId == 11 && this.getMyChar().ctaskIndex == 1) {
                    this.getMyChar().uptaskMaint();
                }
            }
            else {
                Service.ServerMessage(this.getMyChar(), Text.get(0, 40));
            }
        }
        catch (Exception ex) {}
    }
    
    protected void addEnemis(final String playerName) {
        try {
            int num = 0;
            for (short i = 0; i < this.vFriend.size(); ++i) {
                final Friend friend = this.vFriend.get(i);
                if (friend.type == 2) {
                    ++num;
                }
                if (friend.friendName.equals(playerName)) {
                    friend.type = 2;
                    return;
                }
            }
            short i = 0;
            while (this.vFriend.size() > 0 && num >= GameScr.max_Enemies) {
                final Friend friend = this.vFriend.get(i);
                if (friend.type == 2) {
                    this.vFriend.remove(i);
                    break;
                }
                ++i;
            }
            this.vFriend.add(new Friend(playerName, (byte)2));
        }
        catch (Exception ex) {}
    }
    
    protected static void removeFriend(final Player _char, final String name) {
        try {
            for (short i = 0; i < _char.vFriend.size(); ++i) {
                final Friend friend = _char.vFriend.get(i);
                if (friend.friendName.equals(name)) {
                    _char.vFriend.remove(i);
                    if (friend.type == 2) {
                        Service.removeEnemies(_char, name);
                    }
                    else {
                        Service.removeFriend(_char, name);
                    }
                    return;
                }
            }
        }
        catch (Exception ex) {}
    }
    
    protected static void tradeInvite(final Char _char, final int charId) {
        if (MapServer.notTrade(_char.tileMap.mapID)) {
            Service.ServerMessage(_char, Text.get(0, 300));
        }
        else if (!_char.user.player.isTrade) {
            if (_char.user.player.delayTrade > 0) {
                GameCanvas.startOKDlg(_char.user.session, Text.get(0, 44));
            }
            else {
                final Char player = _char.findCharInMap(charId);
                if (player != null && player.user != null) {
                    if (Math.abs(_char.cx - player.cx) < 60 && Math.abs(_char.cy - player.cy) < 40) {
                        if (!player.user.player.isTrade) {
                            if (player.user.player.tradeCharIdwait == -9999 || _char.findCharInMap(player.user.player.tradeCharIdwait) == null) {
                                _char.user.player.delayTrade = 30000;
                                _char.user.player.tradeCharId = player.charID;
                                player.user.player.delayCancelTrade = 30000;
                                player.user.player.tradeCharIdwait = _char.charID;
                                Service.TradeInvite(player, _char.charID);
                            }
                        }
                    }
                }
                else {
                    Service.ServerMessage(_char, Text.get(0, 40));
                }
            }
        }
    }
    
    protected static void tradeAccept(final Char _char, final int playerMapId) {
        if (!_char.user.player.isTrade) {
            final Char player = _char.findCharInMap(playerMapId);
            if (player != null && _char.user.player.tradeCharIdwait == playerMapId) {
                if (Math.abs(_char.cx - player.cx) < 60 && Math.abs(_char.cy - player.cy) < 40) {
                    if (!player.user.player.isTrade) {
                        if (player.user.player.tradeCharId == _char.charID) {
                            player.user.player.isTrade = true;
                            player.user.player.delayTrade = 0;
                            player.user.player.tradeCharIdwait = -9999;
                            _char.user.player.isTrade = true;
                            _char.user.player.tradeCharIdwait = -9999;
                            _char.user.player.delayTrade = 0;
                            _char.user.player.tradeCharId = player.charID;
                            Service.openUITrade(player, _char.cName);
                            Service.openUITrade(_char, player.cName);
                        }
                    }
                }
                else {
                    Service.ServerMessage(_char, Text.get(0, 40));
                }
            }
        }
    }
    
    protected static void endTrade(final Char _char) {
        final Char player = _char.findCharInMap(_char.user.player.tradeCharId);
        if (player != null) {
            for (byte i = 0; i < player.user.player.itemTrade.length; ++i) {
                player.user.player.itemTrade[i] = null;
            }
            player.user.player.tradeCoin = 0;
            player.user.player.tradeType = 0;
            player.user.player.tradeCharId = -9999;
            player.user.player.tradeCharIdwait = -9999;
            player.user.player.delayCancelTrade = 0;
            if (player.user.player.isTrade) {
                Service.CancelTrade(player);
                player.user.player.isTrade = false;
            }
        }
        for (byte i = 0; i < _char.user.player.itemTrade.length; ++i) {
            _char.user.player.itemTrade[i] = null;
        }
        _char.user.player.tradeCoin = 0;
        _char.user.player.tradeType = 0;
        _char.user.player.tradeCharId = -9999;
        _char.user.player.tradeCharIdwait = -9999;
        _char.user.player.delayCancelTrade = 0;
        if (_char.user.player.isTrade) {
            Service.CancelTrade(_char);
            _char.user.player.isTrade = false;
        }
    }
    
    protected static int ItemUseLimit(final Char _char, final short itemTemplateId) {
        try {
            for (short i = 0; i < _char.ItemUseLimit.size(); ++i) {
                final Limit limit = _char.ItemUseLimit.get(i);
                if (limit != null && limit.id == itemTemplateId) {
                    return limit.limit;
                }
            }
        }
        catch (Exception ex) {}
        return -1;
    }
    
    protected static void nextItemUseLimit(final Char _char, final short itemTemplateId) {
        try {
            for (short i = 0; i < _char.ItemUseLimit.size(); ++i) {
                final Limit limit = _char.ItemUseLimit.get(i);
                if (limit != null && limit.id == itemTemplateId) {
                    final Limit limit3 = limit;
                    ++limit3.limit;
                    return;
                }
            }
            final Limit limit2 = new Limit(itemTemplateId, 1, Limit.getisItemDelNextDay(itemTemplateId));
            _char.ItemUseLimit.add(limit2);
        }
        catch (Exception ex) {}
    }
    
    protected static void CallNhanban(final TileMap tileMap, final Char _char) {
        _char.isBatNhanban = true;
        _char.Nhanban.isNhanban = true;
        _char.Nhanban.user = null;
        _char.Nhanban.cHP = _char.Nhanban.cMaxHP();
        _char.Nhanban.cMP = _char.Nhanban.cMaxMP();
        _char.Nhanban.statusMe = 1;
        _char.Nhanban.ctaskId = (byte)GameScr.tasks.length;
        if (_char.Nhanban.myskill != null) {
            _char.Nhanban.CSkill[0] = _char.Nhanban.myskill.template.id;
        }
        final short mobTemplateId = Mob.itemMob(_char.Nhanban.ItemBody[10]);
        if (mobTemplateId > 0) {
            _char.Nhanban.mobMe = new Mob(_char.Nhanban.tileMap, (short)(-1), mobTemplateId, (byte)1, 0, 0, _char.cx, (short)(_char.cy - 40), (byte)5, (byte)0, Mob.arrMobTemplate[mobTemplateId].isBoss, -1);
        }
        final short x2 = (short)Util.nextInt(_char.cx - 60, _char.cx + 60);
        final short y2 = _char.cy;
        _char.Nhanban.cx = x2;
        _char.Nhanban.cy = y2;
        tileMap.Join(_char.Nhanban);
    }
    
    protected static void toNhanban(final Char _char) {
        if (_char.isHuman && _char.Nhanban != null && _char.isBatNhanban) {
            final TileMap tileMap = _char.tileMap;
            final Char clone = _char.Nhanban;
            tileMap.removeChar(clone);
            for (int i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.charID == _char.charID) {
                    tileMap.aCharInMap.set(i, clone);
                    break;
                }
            }
            if (_char.party != null) {
                _char.party.removePlayer(_char.charID);
            }
            _char.isToNhanban = true;
            clone.user = _char.user;
            clone.isNhanban = false;
            try {
                synchronized (_char.aEff) {
                    for (short j = (short)(_char.aEff.size() - 1); j >= 0; --j) {
                        final Effect eff = _char.aEff.get(j);
                        if (eff != null) {
                            Service.MeRemoveEfect(_char, eff);
                            if (eff.type == 0) {
                                _char.aEff.remove(j);
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {}
            final int charId = _char.charID;
            _char.charID = clone.charID;
            clone.charID = charId;
            clone.cHP = clone.cMaxHP();
            clone.cMP = clone.cMaxMP();
            clone.statusMe = 1;
            clone.cx = _char.cx;
            clone.cy = _char.cy;
            Service.updateInfoMe(clone);
            try {
                for (byte k = 0; k < clone.aEff.size(); ++k) {
                    final Effect effect = clone.aEff.get(k);
                    if (effect != null) {
                        Service.MeAddEfect(clone, effect);
                    }
                }
            }
            catch (Exception ex2) {}
            Service.loadRMS(clone, clone.KSkill, "KSkill");
            Service.loadRMS(clone, clone.OSkill, "OSkill");
            Service.loadRMS(clone, clone.CSkill, "CSkill");
            Service.LoadThuCuoi(clone, clone.charID, clone.ItemMounts);
            Service.MELoadThuNuoi(clone, clone.mobMe);
            updateInfoPlayer(clone);
            for (int l = 0; l < tileMap.numPlayer; ++l) {
                if (tileMap.aCharInMap.get(l).user != null && tileMap.aCharInMap.get(l).user.session != null && clone.charID != tileMap.aCharInMap.get(l).charID) {
                    Service.PlayerLoadAoChoang(tileMap.aCharInMap.get(l), clone.charID, clone.cHP, clone.cMaxHP(), clone.coat());
                    Service.PlayerLoadGiaToc(tileMap.aCharInMap.get(l), clone.charID, clone.cHP, clone.cMaxHP(), clone.glove());
                    Service.PlayerLoadThuNuoi(tileMap.aCharInMap.get(l), clone.charID, clone.mobMe);
                    Service.LoadThuCuoi(tileMap.aCharInMap.get(l), clone.charID, clone.ItemMounts);
                    Service.onChangeVithu(tileMap.aCharInMap.get(l), clone.charID, clone.mobViThu);
                }
            }
        }
    }

    protected static void inviteManor(Char _char, String name) {
        Char char1 = Client.getPlayer(name);
        if (char1 != null && !_char.clan.clanManor.memberAcceptManor.contains(char1.cName)) {
            TileMap tile = char1.tileMap;
            if (!tile.map.isMapTalent() && !tile.map.isMapTalent() && !tile.map.isWarClanMap() && !tile.map.isChienTruong()
                    && !tile.map.isTestDunMap() && !tile.map.isClanManor() && !tile.map.isBackCaveMap()) {
                Service.openUIConfirmID(char1, "Bạn được mời tham gia Lãnh Địa Gia Tộc, bạn có muốn tham gia?", (byte) -123);
            } else {
                Service.ServerMessage(_char, "Không thể mời người chơi này.");
            }
        }
    }
    protected static void toChar(final Char clone) {
        if (!clone.isHuman) {
            final TileMap tileMap = clone.tileMap;
            final Char _char = clone.user.player;
            _char.isBatNhanban = false;
            for (int i = 0; i < tileMap.numPlayer; ++i) {
                final Char player = tileMap.aCharInMap.get(i);
                if (player != null && player.charID == clone.charID) {
                    tileMap.aCharInMap.set(i, _char);
                    break;
                }
            }
            if (clone.party != null) {
                clone.party.removePlayer(clone.charID);
            }
            clone.isNhanban = true;
            try {
                synchronized (clone.aEff) {
                    for (short j = (short)(clone.aEff.size() - 1); j >= 0; --j) {
                        final Effect eff = clone.aEff.get(j);
                        if (eff != null) {
                            Service.MeRemoveEfect(clone, eff);
                            if (eff.type == 0) {
                                clone.aEff.remove(j);
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {}
            final int charId = clone.charID;
            clone.charID = _char.charID;
            _char.charID = charId;
            _char.cHP = _char.cMaxHP();
            _char.cMP = _char.cMaxMP();
            _char.statusMe = 1;
            _char.cx = clone.cx;
            _char.cy = clone.cy;
            Service.updateInfoMe(_char);
            try {
                for (byte k = 0; k < _char.aEff.size(); ++k) {
                    final Effect effect = _char.aEff.get(k);
                    if (effect != null) {
                        Service.MeAddEfect(_char, effect);
                    }
                }
            }
            catch (Exception ex2) {}
            Service.loadRMS(_char, _char.KSkill, "KSkill");
            Service.loadRMS(_char, _char.OSkill, "OSkill");
            Service.loadRMS(_char, _char.CSkill, "CSkill");
            Service.LoadThuCuoi(_char, _char.charID, _char.ItemMounts);
            Service.MELoadThuNuoi(_char, _char.mobMe);
            _char.isToNhanban = false;
            clone.user = null;
            updateInfoPlayer(_char);
            for (int l = 0; l < tileMap.numPlayer; ++l) {
                if (tileMap.aCharInMap.get(l).user != null && tileMap.aCharInMap.get(l).user.session != null && clone.charID != tileMap.aCharInMap.get(l).charID) {
                    Service.PlayerLoadAoChoang(tileMap.aCharInMap.get(l), _char.charID, _char.cHP, _char.cMaxHP(), _char.coat());
                    Service.PlayerLoadGiaToc(tileMap.aCharInMap.get(l), _char.charID, _char.cHP, _char.cMaxHP(), _char.glove());
                    Service.PlayerLoadThuNuoi(tileMap.aCharInMap.get(l), _char.charID, _char.mobMe);
                    Service.LoadThuCuoi(tileMap.aCharInMap.get(l), _char.charID, _char.ItemMounts);
                    Service.onChangeVithu(tileMap.aCharInMap.get(l), _char.charID, _char.mobViThu);
                }
            }
        }
    }
    
    protected void close() {
        if (this.getMyChar().party != null) {
            this.getMyChar().party.removePlayer(this.getMyChar().charID);
        }
        if (this.tileMap != null) {
            this.tileMap.Exit(this.getMyChar());
        }
    }
}
