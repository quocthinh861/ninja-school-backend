// 
// Decompiled by Procyon v0.5.36
// 

package com.Server.ninja.option;

import com.Server.ninja.Server.GameScr;
import com.Server.ninja.template.SkillOptionTemplate;

public class SkillOption
{
    public int param;
    public SkillOptionTemplate optionTemplate;
    
    public SkillOption(final int param, final short id) {
        this.param = param;
        this.optionTemplate = GameScr.sOptionTemplates[id];
    }
    
    public static int timeMaintain(final short id) {
        switch (id) {
            case 24: {
                return 2000;
            }
            case 25: {
                return 1500;
            }
            case 26: {
                return 1000;
            }
            case 27: {
                return 5000;
            }
            case 34: {
                return 4000;
            }
            case 35: {
                return 3000;
            }
            case 36: {
                return 2000;
            }
            default: {
                return 0;
            }
        }
    }
}
