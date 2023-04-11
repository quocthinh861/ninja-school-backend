// 
// Decompiled by Procyon v0.5.36
// 

package com.Server.ninja.template;

public class ItemTemplate
{
    public short id;
    public byte type;
    public byte gender;
    public String name;
    public String description;
    public int level;
    public short iconID;
    public short part;
    public boolean isUpToUp;
    
    public boolean isTypeWeapon() {
        return this.type == 1;
    }
    
    public boolean isItemClass0() {
        return this.id == 194 || this.id == 94 || this.id == 95 || this.id == 96 || this.id == 97 || this.id == 98 || this.id == 331 || this.id == 369 || this.id == 506 || this.id == 632;
    }
    
    public boolean isItemClass1() {
        return this.id == 194 || this.id == 94 || this.id == 95 || this.id == 96 || this.id == 97 || this.id == 98 || this.id == 331 || this.id == 369 || this.id == 506 || this.id == 632;
    }
    
    public boolean isItemClass2() {
        return this.id == 114 || this.id == 115 || this.id == 116 || this.id == 117 || this.id == 118 || this.id == 332 || this.id == 370 || this.id == 507 || this.id == 633;
    }
    
    public boolean isItemClass3() {
        return this.id == 99 || this.id == 100 || this.id == 101 || this.id == 102 || this.id == 103 || this.id == 333 || this.id == 371 || this.id == 508 || this.id == 634;
    }
    
    public boolean isItemClass4() {
        return this.id == 109 || this.id == 110 || this.id == 111 || this.id == 112 || this.id == 113 || this.id == 334 || this.id == 372 || this.id == 509 || this.id == 635;
    }
    
    public boolean isItemClass5() {
        return this.id == 104 || this.id == 105 || this.id == 106 || this.id == 107 || this.id == 108 || this.id == 335 || this.id == 373 || this.id == 510 || this.id == 636;
    }
    
    public boolean isItemClass6() {
        return this.id == 119 || this.id == 120 || this.id == 121 || this.id == 122 || this.id == 123 || this.id == 336 || this.id == 374 || this.id == 511 || this.id == 637;
    }
}
