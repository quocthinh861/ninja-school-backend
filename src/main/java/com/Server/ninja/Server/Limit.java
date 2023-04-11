 package com.Server.ninja.Server;

public class Limit
{
    protected int id;
    protected int limit;
    protected boolean isDelNextDay;
    protected static Limit[] arrItemUseLimit;
    
    protected Limit(final int id, final int limit, final boolean isDelNextDay) {
        this.id = id;
        this.limit = limit;
        this.isDelNextDay = isDelNextDay;
    }
    
    protected static int getMaxItemLimit(final short itemTemplateId) {
        for (short i = 0; i < Limit.arrItemUseLimit.length; ++i) {
            final Limit limit = Limit.arrItemUseLimit[i];
            if (limit != null && limit.id == itemTemplateId) {
                return limit.limit;
            }
        }
        return -1;
    }
    
    protected static boolean getisItemDelNextDay(final short itemTemplateId) {
        for (short i = 0; i < Limit.arrItemUseLimit.length; ++i) {
            final Limit limit = Limit.arrItemUseLimit[i];
            if (limit != null && limit.id == itemTemplateId) {
                return limit.isDelNextDay;
            }
        }
        return false;
    }
    
    static {
        Limit.arrItemUseLimit = new Limit[] { new Limit(252, 3, false), new Limit(253, 8, false), new Limit(280, 1, true), new Limit(298, 100, true), new Limit(299, 100, true), new Limit(300, 100, true), new Limit(301, 100, true), new Limit(308, 10, false), new Limit(309, 10, false), new Limit(705, 6, true) , new Limit(268, 2, true)};
    }
}
