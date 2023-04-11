 package com.Server.ninja.Server;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;


public class Member
{
    protected int playerId;
    protected String cName;
    protected byte nClass;
    protected int cLevel;
    protected byte typeClan;
    protected int pointClan;
    protected int pointClanWeek;
    
    protected Member(final int playerId, final String cName, final byte nClass, final int cLevel, final int pointClan) {
        this.cName = "";
        this.typeClan = -1;
        this.pointClan = 0;
        this.pointClanWeek = 0;
        this.playerId = playerId;
        this.cName = cName;
        this.nClass = nClass;
        this.cLevel = cLevel;
        this.pointClan = pointClan;
    }
    
    private Member() {
        this.cName = "";
        this.typeClan = -1;
        this.pointClan = 0;
        this.pointClanWeek = 0;
    }
    
    protected static Member parseMember(final String s) {
        try {
            final Member member = new Member();
            final JSONArray jarr = (JSONArray)JSONValue.parseWithException(s);
            member.playerId = Integer.parseInt(jarr.get(0).toString());
            member.cName = jarr.get(1).toString();
            member.nClass = Byte.parseByte(jarr.get(2).toString());
            member.cLevel = Integer.parseInt(jarr.get(3).toString());
            member.typeClan = Byte.parseByte(jarr.get(4).toString());
            member.pointClan = Integer.parseInt(jarr.get(5).toString());
            member.pointClanWeek = Integer.parseInt(jarr.get(6).toString());
            return member;
        }
        catch (ParseException | NumberFormatException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String toString() {
        String a = "[" + this.playerId;
        a = a + ",\"" + this.cName + "\"";
        a = a + "," + this.nClass;
        a = a + "," + this.cLevel;
        a = a + "," + this.typeClan;
        a = a + "," + this.pointClan;
        a = a + "," + this.pointClanWeek;
        return a + "]";
    }
}
