 package com.Server.ninja.Server;

import com.Server.PartMount.Frame;
import com.Server.PartMount.PartLeg;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import com.Server.ninja.template.ItemTemplate;
import com.Server.ninja.template.ItemOptionTemplate;
import com.Server.ninja.template.SkillTemplate;
import com.Server.ninja.option.SkillOption;
import com.Server.ninja.template.SkillOptionTemplate;
import com.Server.ninja.template.MobTemplate;
import com.Server.ninja.template.NpcTemplate;
import com.Server.ninja.template.MapTemplate;
import com.Server.ninja.template.TaskTemplate;
import com.Server.ninja.template.EffectTemplate;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import com.Server.io.MySQL;
import com.Server.PartMount.PartMount;
import com.Server.PartMount.PartMountNew;
import com.Server.PartMount.PartNormal;
import java.util.Arrays;
import org.json.simple.JSONObject;

public class Init
{
    protected static void init() {
        int i = 0;
        try {
            final MySQL mySQL = new MySQL(0);
            ResultSet res = mySQL.stat.executeQuery("SELECT * FROM `task`;");
            if (res.last()) {
                GameScr.tasks = new byte[res.getRow()][];
                GameScr.mapTasks = new byte[GameScr.tasks.length][];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final JSONArray jarrT = (JSONArray)JSONValue.parse(res.getString("tasks"));
                final JSONArray jarrM = (JSONArray)JSONValue.parse(res.getString("mapTasks"));
                GameScr.tasks[i] = new byte[jarrT.size()];
                GameScr.mapTasks[i] = new byte[jarrM.size()];
                for (int j = 0; j < GameScr.tasks[i].length; ++j) {
                    GameScr.tasks[i][j] = Byte.parseByte(jarrT.get(j).toString());
                    GameScr.mapTasks[i][j] = Byte.parseByte(jarrM.get(j).toString());
                }
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `exp`;");
            if (res.last()) {
                GameScr.exps = new long[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.exps[i] = res.getLong("exp");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `crystal`;");
            if (res.last()) {
                GameScr.crystals = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.crystals[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `upclothe`;");
            if (res.last()) {
                GameScr.upClothe = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.upClothe[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `upadorn`;");
            if (res.last()) {
                GameScr.upAdorn = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.upAdorn[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `upweapon`;");
            if (res.last()) {
                GameScr.upWeapon = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.upWeapon[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `coinupcrystal`;");
            if (res.last()) {
                GameScr.coinUpCrystals = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.coinUpCrystals[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `coinupclothe`;");
            if (res.last()) {
                GameScr.coinUpClothes = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.coinUpClothes[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `coinupadorn`;");
            if (res.last()) {
                GameScr.coinUpAdorns = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.coinUpAdorns[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `coinupweapon`;");
            if (res.last()) {
                GameScr.coinUpWeapons = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.coinUpWeapons[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `goldup`;");
            if (res.last()) {
                GameScr.goldUps = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.goldUps[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `maxpercent`;");
            if (res.last()) {
                GameScr.maxPercents = new int[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                GameScr.maxPercents[i] = res.getInt("number");
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `effecttemplate`;");
            if (res.last()) {
                Effect.effTemplates = new EffectTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final EffectTemplate effectTemplate = new EffectTemplate();
                effectTemplate.id = res.getByte("id");
                effectTemplate.type = res.getByte("type");
                effectTemplate.iconId = res.getShort("iconId");
                effectTemplate.name = res.getString("name");
                Effect.effTemplates[i] = effectTemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `tasktemplate`;");
            if (res.last()) {
                GameScr.taskTemplates = new TaskTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final TaskTemplate taskTemplate = new TaskTemplate();
                taskTemplate.taskId = res.getShort("taskId");
                taskTemplate.name = res.getString("name");
                taskTemplate.detail = res.getString("detail");
                final JSONArray subNames = (JSONArray)JSONValue.parse(res.getString("subNames"));
                taskTemplate.subNames = new String[subNames.size()];
                taskTemplate.counts = new short[taskTemplate.subNames.length];
                for (byte k = 0; k < taskTemplate.subNames.length; ++k) {
                    taskTemplate.subNames[k] = subNames.get((int)k).toString();
                    taskTemplate.counts[k] = -1;
                }
                final JSONArray counts = (JSONArray)JSONValue.parse(res.getString("counts"));
                for (byte l = 0; l < counts.size(); ++l) {
                    taskTemplate.counts[l] = Short.parseShort(counts.get((int)l).toString());
                }
                GameScr.taskTemplates[i] = taskTemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `npctemplate`;");
            if (res.last()) {
                Npc.arrNpcTemplate = new NpcTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final NpcTemplate npcTemplate = new NpcTemplate();
                npcTemplate.npcTemplateId = res.getByte("id");
                npcTemplate.name = res.getString("name");
                npcTemplate.headId = res.getShort("headId");
                npcTemplate.bodyId = res.getShort("bodyId");
                npcTemplate.legId = res.getShort("legId");
                final JSONArray jarr = (JSONArray)JSONValue.parse(res.getString("menu"));
                npcTemplate.menu = new String[jarr.size()][];
                for (int j = 0; j < npcTemplate.menu.length; ++j) {
                    final JSONArray jarr2 = (JSONArray)jarr.get(j);
                    npcTemplate.menu[j] = new String[jarr2.size()];
                    for (int k2 = 0; k2 < npcTemplate.menu[j].length; ++k2) {
                        npcTemplate.menu[j][k2] = jarr2.get(k2).toString();
                    }
                }
                Npc.arrNpcTemplate[i] = npcTemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `maptemplate`;");
            if (res.last()) {
                GameScr.mapTemplates = new MapTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final MapTemplate mapTemplate = new MapTemplate();
                mapTemplate.mapID = res.getShort("id");
                mapTemplate.mapVersion = res.getByte("mapVersion");
                mapTemplate.mapName = res.getString("mapName");
                mapTemplate.mapDescription = res.getString("mapDescription");
                final JSONArray WminX = (JSONArray)JSONValue.parse(res.getString("WminX"));
                final JSONArray WminY = (JSONArray)JSONValue.parse(res.getString("WminY"));
                final JSONArray WmaxX = (JSONArray)JSONValue.parse(res.getString("WmaxX"));
                final JSONArray WmaxY = (JSONArray)JSONValue.parse(res.getString("WmaxY"));
                final JSONArray WgoX = (JSONArray)JSONValue.parse(res.getString("WgoX"));
                final JSONArray WgoY = (JSONArray)JSONValue.parse(res.getString("WgoY"));
                final JSONArray WmapID = (JSONArray)JSONValue.parse(res.getString("WmapID"));
                mapTemplate.WmapID = new short[WmapID.size()];
                mapTemplate.WminX = new short[mapTemplate.WmapID.length];
                mapTemplate.WminY = new short[mapTemplate.WmapID.length];
                mapTemplate.WmaxX = new short[mapTemplate.WmapID.length];
                mapTemplate.WmaxY = new short[mapTemplate.WmapID.length];
                mapTemplate.WgoX = new short[mapTemplate.WmapID.length];
                mapTemplate.WgoY = new short[mapTemplate.WmapID.length];
                for (byte m = 0; m < mapTemplate.WmapID.length; ++m) {
                    mapTemplate.WminX[m] = Short.parseShort(WminX.get((int)m).toString());
                    mapTemplate.WminY[m] = Short.parseShort(WminY.get((int)m).toString());
                    mapTemplate.WmaxX[m] = Short.parseShort(WmaxX.get((int)m).toString());
                    mapTemplate.WmaxY[m] = Short.parseShort(WmaxY.get((int)m).toString());
                    mapTemplate.WgoX[m] = Short.parseShort(WgoX.get((int)m).toString());
                    mapTemplate.WgoY[m] = Short.parseShort(WgoY.get((int)m).toString());
                    mapTemplate.WmapID[m] = Short.parseShort(WmapID.get((int)m).toString());
                }
                final JSONArray itemMap = (JSONArray)JSONValue.parse(res.getString("itemMap"));
                mapTemplate.itemMap = new short[itemMap.size()];
                for (byte j2 = 0; j2 < mapTemplate.itemMap.length; ++j2) {
                    mapTemplate.itemMap[j2] = Short.parseShort(itemMap.get((int)j2).toString());
                }
                final JSONArray ItemTreeBehind = (JSONArray)JSONValue.parse(res.getString("ItemTreeBehind"));
                mapTemplate.ItemTreeBehind = new ItemTree[ItemTreeBehind.size()];
                for (byte j3 = 0; j3 < mapTemplate.ItemTreeBehind.length; ++j3) {
                    final JSONArray tree = (JSONArray)ItemTreeBehind.get((int)j3);
                    mapTemplate.ItemTreeBehind[j3] = new ItemTree(Short.parseShort(tree.get(0).toString()), Short.parseShort(tree.get(1).toString()));
                    mapTemplate.ItemTreeBehind[j3].idTree = Short.parseShort(tree.get(2).toString());
                }
                final JSONArray ItemTreeBetwen = (JSONArray)JSONValue.parse(res.getString("ItemTreeBetwen"));
                mapTemplate.ItemTreeBetwen = new ItemTree[ItemTreeBetwen.size()];
                for (byte j4 = 0; j4 < mapTemplate.ItemTreeBetwen.length; ++j4) {
                    final JSONArray tree2 = (JSONArray)ItemTreeBetwen.get((int)j4);
                    mapTemplate.ItemTreeBetwen[j4] = new ItemTree(Short.parseShort(tree2.get(0).toString()), Short.parseShort(tree2.get(1).toString()));
                    mapTemplate.ItemTreeBetwen[j4].idTree = Short.parseShort(tree2.get(2).toString());
                }
                final JSONArray ItemTreeFront = (JSONArray)JSONValue.parse(res.getString("ItemTreeFront"));
                mapTemplate.ItemTreeFront = new ItemTree[ItemTreeFront.size()];
                for (byte j5 = 0; j5 < mapTemplate.ItemTreeFront.length; ++j5) {
                    final JSONArray tree3 = (JSONArray)ItemTreeFront.get((int)j5);
                    mapTemplate.ItemTreeFront[j5] = new ItemTree(Short.parseShort(tree3.get(0).toString()), Short.parseShort(tree3.get(1).toString()));
                    mapTemplate.ItemTreeFront[j5].idTree = Short.parseShort(tree3.get(2).toString());
                }
                final JSONArray jarrType = (JSONArray)JSONValue.parse(res.getString("npcType"));
                final JSONArray jarrX = (JSONArray)JSONValue.parse(res.getString("npcX"));
                final JSONArray jarrY = (JSONArray)JSONValue.parse(res.getString("npcY"));
                final JSONArray jarrID = (JSONArray)JSONValue.parse(res.getString("npcID"));
                mapTemplate.npcType = new byte[jarrType.size()];
                mapTemplate.npcX = new short[mapTemplate.npcType.length];
                mapTemplate.npcY = new short[mapTemplate.npcType.length];
                mapTemplate.npcID = new byte[mapTemplate.npcType.length];
                for (int j6 = 0; j6 < mapTemplate.npcType.length; ++j6) {
                    mapTemplate.npcType[j6] = Byte.parseByte(jarrType.get(j6).toString());
                    mapTemplate.npcX[j6] = Short.parseShort(jarrX.get(j6).toString());
                    mapTemplate.npcY[j6] = Short.parseShort(jarrY.get(j6).toString());
                    mapTemplate.npcID[j6] = Byte.parseByte(jarrID.get(j6).toString());
                }
                final JSONArray mobID = (JSONArray)JSONValue.parse(res.getString("mobID"));
                final JSONArray mobLevel = (JSONArray)JSONValue.parse(res.getString("mobLevel"));
                final JSONArray mobX = (JSONArray)JSONValue.parse(res.getString("mobX"));
                final JSONArray mobY = (JSONArray)JSONValue.parse(res.getString("mobY"));
                final JSONArray mobStatus = (JSONArray)JSONValue.parse(res.getString("mobStatus"));
                final JSONArray moblevelBoss = (JSONArray)JSONValue.parse(res.getString("moblevelBoss"));
                final JSONArray mobRefreshTime = (JSONArray)JSONValue.parse(res.getString("mobRefreshTime"));
                mapTemplate.mobID = new int[mobID.size()];
                mapTemplate.mobLevel = new short[mapTemplate.mobID.length];
                mapTemplate.mobX = new short[mapTemplate.mobID.length];
                mapTemplate.mobY = new short[mapTemplate.mobID.length];
                mapTemplate.mobStatus = new byte[mapTemplate.mobID.length];
                mapTemplate.moblevelBoss = new byte[mapTemplate.mobID.length];
                mapTemplate.mobRefreshTime = new int[mapTemplate.mobID.length];
                for (int j7 = 0; j7 < mapTemplate.mobID.length; ++j7) {
                    mapTemplate.mobID[j7] = Integer.parseInt(mobID.get(j7).toString());
                    mapTemplate.mobLevel[j7] = Short.parseShort(mobLevel.get(j7).toString());
                    mapTemplate.mobX[j7] = Short.parseShort(mobX.get(j7).toString());
                    mapTemplate.mobY[j7] = Short.parseShort(mobY.get(j7).toString());
                    mapTemplate.mobStatus[j7] = Byte.parseByte(mobStatus.get(j7).toString());
                    mapTemplate.moblevelBoss[j7] = Byte.parseByte(moblevelBoss.get(j7).toString());
                    mapTemplate.mobRefreshTime[j7] = Integer.parseInt(mobRefreshTime.get(j7).toString());
                }
                mapTemplate.tileID = res.getByte("tileID");
                mapTemplate.bgID = res.getByte("bgID");
                mapTemplate.typeMap = res.getByte("typeMap");
                mapTemplate.numZone = res.getByte("numZone");
                mapTemplate.goX = res.getShort("goX");
                mapTemplate.goY = res.getShort("goY");
                GameScr.mapTemplates[i] = mapTemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `mobtemplate`;");
            if (res.last()) {
                Mob.arrMobTemplate = new MobTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final MobTemplate mobTemplate = new MobTemplate();
                mobTemplate.mobTemplateId = res.getShort("mobTemplateId");
                mobTemplate.type = res.getByte("type");
                mobTemplate.name = res.getString("name");
                mobTemplate.hp = res.getInt("hp");
                mobTemplate.isBoss = res.getBoolean("isBoss");
                mobTemplate.rangeMove = res.getByte("rangeMove");
                mobTemplate.speed = res.getByte("speed");
                mobTemplate.isAttack = res.getBoolean("isAttack");
                Mob.arrMobTemplate[i] = mobTemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `skilloptiontemplate`;");
            if (res.last()) {
                GameScr.sOptionTemplates = new SkillOptionTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final short id = res.getShort("id");
                final String name = res.getString("name");
                GameScr.sOptionTemplates[i] = new SkillOptionTemplate(id, name);
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `skill`;");
            if (res.last()) {
                GameScr.skills = new Skill[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final Skill skill = new Skill();
                skill.skillId = res.getShort("skillId");
                skill.point = res.getByte("point");
                skill.level = res.getByte("level");
                skill.coolDown = res.getInt("coolDown");
                skill.dx = res.getShort("dx");
                skill.dy = res.getShort("dy");
                skill.maxFight = res.getByte("maxFight");
                skill.manaUse = res.getShort("manaUse");
                final JSONArray jarr = (JSONArray)JSONValue.parse(res.getString("options"));
                skill.options = new SkillOption[jarr.size()];
                for (short j8 = 0; j8 < skill.options.length; ++j8) {
                    final JSONArray jarr2 = (JSONArray)jarr.get((int)j8);
                    skill.options[j8] = new SkillOption(Integer.parseInt(jarr2.get(0).toString()), Short.parseShort(jarr2.get(1).toString()));
                }
                GameScr.skills[i] = skill;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `skilltemplate`;");
            if (res.last()) {
                GameScr.skillTemplates = new SkillTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final SkillTemplate stemplate = new SkillTemplate();
                stemplate.id = res.getByte("id");
                stemplate.classId = res.getByte("classId");
                stemplate.name = res.getString("name");
                stemplate.maxPoint = res.getByte("maxPoint");
                stemplate.type = res.getByte("type");
                stemplate.iconId = res.getShort("iconId");
                stemplate.description = res.getString("description");
                final JSONArray jarr = (JSONArray)JSONValue.parse(res.getString("skills"));
                stemplate.skills = new Skill[jarr.size()];
                for (short j8 = 0; j8 < stemplate.skills.length; ++j8) {
                    stemplate.skills[j8] = GameScr.skills[Short.parseShort(jarr.get((int)j8).toString())];
                    GameScr.skills[Short.parseShort(jarr.get((int)j8).toString())].template = stemplate;
                }
                GameScr.skillTemplates[i] = stemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `nclass`;");
            if (res.last()) {
                GameScr.nClasss = new NClass[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final NClass nclass = new NClass();
                nclass.classId = res.getByte("classId");
                nclass.name = res.getString("name");
                final JSONArray jarr = (JSONArray)JSONValue.parse(res.getString("skillTemplates"));
                nclass.skillTemplates = new SkillTemplate[jarr.size()];
                for (short j8 = 0; j8 < nclass.skillTemplates.length; ++j8) {
                    nclass.skillTemplates[j8] = GameScr.skillTemplates[Byte.parseByte(jarr.get((int)j8).toString())];
                }
                GameScr.nClasss[i] = nclass;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `itemoptiontemplate`;");
            if (res.last()) {
                GameScr.iOptionTemplates = new ItemOptionTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final ItemOptionTemplate iotemplate = new ItemOptionTemplate();
                iotemplate.id = res.getInt("id");
                iotemplate.name = res.getString("name");
                iotemplate.type = res.getByte("type");
                GameScr.iOptionTemplates[i] = iotemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `itemtemplate`;");
            if (res.last()) {
                GameScr.itemTemplates = new ItemTemplate[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final ItemTemplate itemTemplate = new ItemTemplate();
                itemTemplate.id = res.getShort("id");
                itemTemplate.type = res.getByte("type");
                itemTemplate.gender = res.getByte("gender");
                itemTemplate.name = res.getString("name");
                itemTemplate.description = res.getString("description");
                itemTemplate.level = res.getInt("level");
                itemTemplate.iconID = res.getShort("iconID");
                itemTemplate.part = res.getShort("part");
                itemTemplate.isUpToUp = res.getBoolean("isUpToUp");
                GameScr.itemTemplates[i] = itemTemplate;
                ++i;
            }
            res.close();
            res = mySQL.stat.executeQuery("SELECT * FROM `itemstore`;");
            GameScr.itemStores = new HashMap<Byte, Item[]>();
            i = 0;
            while (res.next()) {
                final byte type = res.getByte("type");
                final JSONArray itemStores = (JSONArray)JSONValue.parse(res.getString("itemStores"));
                final Item[] arrItem = new Item[itemStores.size()];
                for (short j9 = 0; j9 < arrItem.length; ++j9) {
                    arrItem[j9] = Item.parseItem(itemStores.get(j9).toString());
                }
                GameScr.itemStores.put(type, arrItem);
                ++i;
            }
            res.close();
            
            res = mySQL.stat.executeQuery("SELECT * FROM `part_head_jump`;");
            if (res.last()) {
                GameScr.head_Jump = new PartNormal[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final PartNormal normal = new PartNormal();
                final short id = res.getShort("id");
                final short icon = res.getShort("icon");
                final JSONArray frame = (JSONArray) JSONValue.parse(res.getString("Frame"));
                normal.idPart = id;
                normal.icon = icon;
                normal.charPart = new PartMount[frame.size()];
                for (short j9 = 0; j9 < frame.size(); ++j9) {
                    PartMount partMount = new PartMount();
                    JSONObject o = (JSONObject) frame.get(j9);
                    partMount.idItem = ((Long) o.get("idItem")).shortValue();
                    partMount.dx = ((Long) o.get("dx")).shortValue();
                    partMount.dy = ((Long) o.get("dy")).shortValue();
                    normal.charPart[j9] = partMount;
                }
                GameScr.head_Jump[i] = normal;
                ++i;
            }
            res.close();
            
            res = mySQL.stat.executeQuery("SELECT * FROM `part_head_normal`;");
            if (res.last()) {
                GameScr.head_Normal = new PartNormal[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final PartNormal normal = new PartNormal();
                final short id = res.getShort("id");
                final short icon = res.getShort("icon");
                final JSONArray frame = (JSONArray) JSONValue.parse(res.getString("Frame"));
                normal.idPart = id;
                normal.icon = icon;
                normal.charPart = new PartMount[frame.size()];
                for (short j9 = 0; j9 < frame.size(); ++j9) {
                    PartMount partMount = new PartMount();
                    JSONObject o = (JSONObject) frame.get(j9);
                    partMount.idItem = ((Long) o.get("idItem")).shortValue();
                    partMount.dx = ((Long) o.get("dx")).shortValue();
                    partMount.dy = ((Long) o.get("dy")).shortValue();
                    normal.charPart[j9] = partMount;
                }
                GameScr.head_Normal[i] = normal;
                ++i;
            }
            res.close();
            
            res = mySQL.stat.executeQuery("SELECT * FROM `part_head_boc_dau`;");
            if (res.last()) {
                GameScr.head_Boc_Dau = new PartNormal[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final PartNormal normal = new PartNormal();
                final short id = res.getShort("id");
                final short icon = res.getShort("icon");
                final JSONArray frame = (JSONArray) JSONValue.parse(res.getString("Frame"));
                normal.idPart = id;
                normal.icon = icon;
                normal.charPart = new PartMount[frame.size()];
                for (short j9 = 0; j9 < frame.size(); ++j9) {
                    PartMount partMount = new PartMount();
                    JSONObject o = (JSONObject) frame.get(j9);
                    partMount.idItem = ((Long) o.get("idItem")).shortValue();
                    partMount.dx = ((Long) o.get("dx")).shortValue();
                    partMount.dy = ((Long) o.get("dy")).shortValue();
                    normal.charPart[j9] = partMount;
                }
                GameScr.head_Boc_Dau[i] = normal;
                ++i;
            }
            res.close();
            
            res = mySQL.stat.executeQuery("SELECT * FROM `part_body_jump`;");
            if (res.last()) {
                GameScr.body_Jump = new PartNormal[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final PartNormal normal = new PartNormal();
                final short id = res.getShort("id");
                final short icon = res.getShort("icon");
                final JSONArray frame = (JSONArray) JSONValue.parse(res.getString("Frame"));
                normal.idPart = id;
                normal.icon = icon;
                normal.charPart = new PartMount[frame.size()];
                for (short j9 = 0; j9 < frame.size(); ++j9) {
                    PartMount partMount = new PartMount();
                    JSONObject o = (JSONObject) frame.get(j9);
                    partMount.idItem = ((Long) o.get("idItem")).shortValue();
                    partMount.dx = ((Long) o.get("dx")).shortValue();
                    partMount.dy = ((Long) o.get("dy")).shortValue();
                    normal.charPart[j9] = partMount;
                }
                GameScr.body_Jump[i] = normal;
                ++i;
            }
            res.close();
            
            res = mySQL.stat.executeQuery("SELECT * FROM `part_body_normal`;");
            if (res.last()) {
                GameScr.body_Normal = new PartNormal[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final PartNormal normal = new PartNormal();
                final short id = res.getShort("id");
                final short icon = res.getShort("icon");
                final JSONArray frame = (JSONArray) JSONValue.parse(res.getString("Frame"));
                normal.idPart = id;
                normal.icon = icon;
                normal.charPart = new PartMount[frame.size()];
                for (short j9 = 0; j9 < frame.size(); ++j9) {
                    PartMount partMount = new PartMount();
                    JSONObject o = (JSONObject) frame.get(j9);
                    partMount.idItem = ((Long) o.get("idItem")).shortValue();
                    partMount.dx = ((Long) o.get("dx")).shortValue();
                    partMount.dy = ((Long) o.get("dy")).shortValue();
                    normal.charPart[j9] = partMount;
                }
                GameScr.body_Normal[i] = normal;
                ++i;
            }
            res.close();
            
            res = mySQL.stat.executeQuery("SELECT * FROM `part_body_boc_dau`;");
            if (res.last()) {
                GameScr.body_Boc_Dau = new PartNormal[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final PartNormal normal = new PartNormal();
                final short id = res.getShort("id");
                final short icon = res.getShort("icon");
                final JSONArray frame = (JSONArray) JSONValue.parse(res.getString("Frame"));
                normal.idPart = id;
                normal.icon = icon;
                normal.charPart = new PartMount[frame.size()];
                for (short j9 = 0; j9 < frame.size(); ++j9) {
                    PartMount partMount = new PartMount();
                    JSONObject o = (JSONObject) frame.get(j9);
                    partMount.idItem = ((Long) o.get("idItem")).shortValue();
                    partMount.dx = ((Long) o.get("dx")).shortValue();
                    partMount.dy = ((Long) o.get("dy")).shortValue();
                    normal.charPart[j9] = partMount;
                }
                GameScr.body_Boc_Dau[i] = normal;
                ++i;
            }
            res.close();
            
            res = mySQL.stat.executeQuery("SELECT * FROM `part_leg`;");
            if (res.last()) {
                GameScr.Legs = new PartLeg[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final PartLeg partLeg = new PartLeg();
                final short id = res.getShort("id");
                final short icon = res.getShort("icon");
                partLeg.idPart = id;
                partLeg.idImg = icon;
                GameScr.Legs[i] = partLeg;
                ++i;
            }
            res.close();
            
            res = mySQL.stat.executeQuery("SELECT * FROM `part_mount_new`;");
            if (res.last()) {
                GameScr.mount_New = new PartMountNew[res.getRow()];
                res.beforeFirst();
            }
            i = 0;
            while (res.next()) {
                final PartMountNew mountNew = new PartMountNew();
                final short idItem = res.getShort("idItem");
                final JSONArray frame = (JSONArray)JSONValue.parse(res.getString("Frame"));
                mountNew.Frame = new Frame[6];
                for (short k = 0; k < 6; ++k) {
                    final Frame f = Frame.parseFrame(frame.get(k).toString());
                    mountNew.Frame[k] = f;
                }
                mountNew.idItem = idItem;
                GameScr.mount_New[i] = mountNew;
                ++i;
            }
            res.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    public static void ReLoadItemShore() {
        int i;
        try {
            System.out.println("START RELOAD");
            final MySQL mySQL = new MySQL(0);
            ResultSet res = mySQL.stat.executeQuery("SELECT * FROM `itemstore`;");
            GameScr.itemStores.clear();
            GameScr.itemStores = new HashMap<Byte, Item[]>();
            i = 0;
            while (res.next()) {
                final byte type = res.getByte("type");
                final JSONArray itemStores = (JSONArray)JSONValue.parse(res.getString("itemStores"));
                final Item[] arrItem = new Item[itemStores.size()];
                for (short j9 = 0; j9 < arrItem.length; ++j9) {
                    arrItem[j9] = Item.parseItem(itemStores.get((int)j9).toString());
                }
                GameScr.itemStores.put(type, arrItem);
                
                ++i;
            }
            res.close();
            System.out.println("RELOAD COMPLETE");
            Client.alertServer("Cập nhật trạng thái item SHOP. Vui lòng chuyển tab qua lại để cập nhật SHOP tránh bị lỗi.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RELOAD ERROR");
            System.exit(0);
        }
    }
}
