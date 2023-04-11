 package com.Server.ninja.Server;

import com.Server.ninja.template.EffectTemplate;

public class Effect
{
    protected static EffectTemplate[] effTemplates;
    protected int timeStart;
    protected int timeLenght;
    protected short param;
    protected EffectTemplate template;
    protected byte type;
    protected int[] params;
    
    protected Effect() {
    }
    
    protected Effect(final byte templateId, final int timeStart, final int timeLenght, final short param) {
        this.template = Effect.effTemplates[templateId];
        this.timeStart = timeStart;
        this.timeLenght = timeLenght;
        this.param = param;
    }
    
    protected static int itemEffectTimeLength(final short itemTemplateId) {
        switch (itemTemplateId) {
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 565: {
                return 3000;
            }
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 29: {
                return 1800000;
            }
            case 30:
            case 249:
            case 250: {
                return 259200000;
            }
            case 248: {
                return 18000000;
            }
            case 275:
            case 276:
            case 277:
            case 278: {
                return 600000;
            }
            case 409:
            case 410:
            case 567: {
                return 86400000;
            }
            case 537: {
                return 7200000;
            }
            case 538: {
                return 18000000;
            }
            case 539:
            case 540: {
                return 3600000;
            }
            case 606: {
                return 28800000;
            }
            default: {
                return 0;
            }
        }
    }
    
    protected static short itemEffectParam(final short itemTemplateId) {
        switch (itemTemplateId) {
            case 13: {
                return 25;
            }
            case 14: {
                return 90;
            }
            case 15: {
                return 230;
            }
            case 16: {
                return 400;
            }
            case 17: {
                return 650;
            }
            case 23: {
                return 3;
            }
            case 24: {
                return 20;
            }
            case 25: {
                return 30;
            }
            case 26:
            case 249: {
                return 40;
            }
            case 27:
            case 250: {
                return 50;
            }
            case 29:
            case 30: {
                return 60;
            }
            case 248: {
                return 2;
            }
            case 275: {
                return 500;
            }
            case 276: {
                return 500;
            }
            case 277: {
                return 100;
            }
            case 278: {
                return 1000;
            }
            case 409: {
                return 75;
            }
            case 410: {
                return 90;
            }
            case 539: {
                return 3;
            }
            case 540: {
                return 4;
            }
            case 565: {
                return 1500;
            }
            case 567: {
                return 120;
            }
            case 606: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
    
    protected static byte itemEffectId(final short itemTemplateId) {
        switch (itemTemplateId) {
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 565: {
                return 21;
            }
            case 23: {
                return 0;
            }
            case 24: {
                return 1;
            }
            case 25: {
                return 2;
            }
            case 26:
            case 249: {
                return 3;
            }
            case 27:
            case 250: {
                return 4;
            }
            case 248: {
                return 22;
            }
            case 29:
            case 30: {
                return 28;
            }
            case 275: {
                return 24;
            }
            case 276: {
                return 25;
            }
            case 277: {
                return 26;
            }
            case 278: {
                return 27;
            }
            case 409: {
                return 30;
            }
            case 410: {
                return 31;
            }
            case 537: {
                return 41;
            }
            case 538: {
                return 40;
            }
            case 539: {
                return 32;
            }
            case 540: {
                return 33;
            }
            case 567: {
                return 35;
            }
            case 569: {
                return 36;
            }
            case 606: {
                return 43;
            }
            case 772:
            case 773: {
                return 42;
            }
            default: {
                return -1;
            }
        }
    }
}
