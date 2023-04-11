 package com.Server.ninja.Server;

import com.Server.ninja.template.NpcTemplate;

public class Npc
{
    protected byte type;
    protected short cx;
    protected short cy;
    protected int npcId;
    protected int index;
    protected int Lighttime;
    protected boolean isQC;
    protected int delayQC;
    protected byte indexQC;
    protected NpcTemplate template;
    protected static NpcTemplate[] arrNpcTemplate;
    
    protected Npc(final int npcId, final short cx, final short cy, final int templateId, final byte type) {
        this.isQC = false;
        this.delayQC = 5000;
        this.indexQC = 0;
        this.index = npcId;
        this.npcId = npcId;
        this.cx = cx;
        this.cy = cy;
        this.template = Npc.arrNpcTemplate[templateId];
        this.type = type;
        if (templateId == 24) {
            this.isQC = true;
        }
    }
}
