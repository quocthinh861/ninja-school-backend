 package com.Server.ninja.Server;

import com.Server.ninja.option.SkillOption;
import com.Server.ninja.template.SkillTemplate;

public class Skill
{
    protected SkillTemplate template;
    protected short skillId;
    protected byte point;
    protected byte level;
    protected int coolDown;
    protected long lastTimeUseThisSkill;
    protected short dx;
    protected short dy;
    protected byte maxFight;
    protected short manaUse;
    protected SkillOption[] options;
    protected boolean paintCanNotUseSkill;
    protected static final short[] arrItemBook;
    protected static final byte[] arrSkillId;
    
    protected static byte ClassBook(final short itemId) {
        if ((itemId >= 40 && itemId <= 48) || itemId == 311 || itemId == 375 || itemId == 397 || itemId == 552 || itemId == 558) {
            return 1;
        }
        if ((itemId >= 49 && itemId <= 57) || itemId == 312 || itemId == 376 || itemId == 398 || itemId == 553 || itemId == 559) {
            return 2;
        }
        if ((itemId >= 58 && itemId <= 66) || itemId == 313 || itemId == 377 || itemId == 399 || itemId == 554 || itemId == 560) {
            return 3;
        }
        if ((itemId >= 67 && itemId <= 75) || itemId == 314 || itemId == 378 || itemId == 400 || itemId == 555 || itemId == 561) {
            return 4;
        }
        if ((itemId >= 76 && itemId <= 84) || itemId == 315 || itemId == 379 || itemId == 401 || itemId == 556 || itemId == 562) {
            return 5;
        }
        if ((itemId >= 85 && itemId <= 93) || itemId == 316 || itemId == 380 || itemId == 402 || itemId == 557 || itemId == 563) {
            return 6;
        }
        return 0;
    }
    
    protected static byte indexItemBox(final short itemTemplateId) {
        for (byte i = 0; i < Skill.arrItemBook.length; ++i) {
            if (Skill.arrItemBook[i] == itemTemplateId) {
                return i;
            }
        }
        return -1;
    }
    
    protected static byte itemSkillTemplateId(final short itemTemplateId) {
        for (byte i = 0; i < Skill.arrSkillId.length; ++i) {
            if (i == indexItemBox(itemTemplateId)) {
                return Skill.arrSkillId[i];
            }
        }
        return -1;
    }
    
    @Override
    protected Skill clone() {
        final Skill skill = new Skill();
        skill.template = this.template;
        skill.skillId = this.skillId;
        skill.point = this.point;
        skill.level = this.level;
        skill.coolDown = this.coolDown;
        skill.lastTimeUseThisSkill = this.lastTimeUseThisSkill;
        skill.dx = this.dx;
        skill.dy = this.dy;
        skill.maxFight = this.maxFight;
        skill.manaUse = this.manaUse;
        skill.options = new SkillOption[this.options.length];
        for (byte i = 0; i < skill.options.length; ++i) {
            skill.options[i] = new SkillOption(this.options[i].param, this.options[i].optionTemplate.id);
        }
        skill.paintCanNotUseSkill = this.paintCanNotUseSkill;
        return skill;
    }
    
    protected boolean isSkill30() {
        return this.template.skills[0].level == 30;
    }
    
    protected boolean isSkill40() {
        return this.template.skills[0].level == 40;
    }
    
    protected boolean isSkill50() {
        return this.template.skills[0].level == 50;
    }
    
    protected boolean isCloneSkill() {
        return this.template.id >= 67 && this.template.id <= 72;
    }
    
    static {
        arrItemBook = new short[] { 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 311, 312, 313, 314, 315, 316, 375, 376, 377, 378, 379, 380, 547, 552, 553, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563 };
        arrSkillId = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 66, 73, 78, 75, 76, 74, 77, 79, 83, 81, 82, 80, 84 };
    }
}
