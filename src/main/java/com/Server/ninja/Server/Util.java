 package com.Server.ninja.Server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.text.NumberFormat;
import java.util.Locale;

public class Util
{
    private static boolean debug;
    private static final Locale locale;
    private static final NumberFormat en;
    private static final Random rand;
    private static final SimpleDateFormat dateFormat;
    private static final String alpha = "abcdefghijklmnopqrstuvwxyz";
    private static final String alphaUpperCase;
    private static final String digits = "0123456789";
    private static final String specials = "~=+%^*/()[]{}/!@#$?|";
    private static final String ALPHA_NUMERIC;
    private static final String ALL;
    
    protected static String randomAlphaNumeric(final int numberOfCharactor) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfCharactor; ++i) {
            final int number = nextInt(0, Util.ALPHA_NUMERIC.length() - 1);
            final char ch = Util.ALPHA_NUMERIC.charAt(number);
            sb.append(ch);
        }
        return sb.toString();
    }
    
    protected static Date getDate(final String dateString) {
        synchronized (Util.dateFormat) {
            try {
                return Util.dateFormat.parse(dateString);
            }
            catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    
    protected static long TimeDay(final int nDays) {
        return System.currentTimeMillis() + nDays * 86400000L;
    }
    
    protected static long TimeHours(final int nHours) {
        return System.currentTimeMillis() + nHours * 3600000L;
    }
    
    protected static long TimeMinutes(final int nMinutes) {
        return System.currentTimeMillis() + nMinutes * 60000L;
    }
    
    protected static long TimeSeconds(final long nSeconds) {
        return System.currentTimeMillis() + nSeconds * 1000L;
    }
    
    protected static long TimeMillis(final long nMillis) {
        return System.currentTimeMillis() + nMillis;
    }
    
    protected static Date DateDay(final int nDays) {
        final Date dat = new Date();
        dat.setTime(dat.getTime() + nDays * 86400000L);
        return dat;
    }
    
    protected static String toDateString(final Date date) {
        synchronized (Util.dateFormat) {
            try {
                return Util.dateFormat.format(date);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    
    protected static Date DateHours(final int nHours) {
        final Date dat = new Date();
        dat.setTime(dat.getTime() + nHours * 3600000L);
        return dat;
    }
    
    protected static Date DateMinutes(final int nMinutes) {
        final Date dat = new Date();
        dat.setTime(dat.getTime() + nMinutes * 60000L);
        return dat;
    }
    
    protected static Date DateSeconds(final long nSeconds) {
        final Date dat = new Date();
        dat.setTime(dat.getTime() + nSeconds * 1000L);
        return dat;
    }
    
    protected static String getFormatNumber(final long num) {
        return Util.en.format(num);
    }
    
    protected static boolean checkNumInt(final String num) {
        return Pattern.compile("^[0-9]+$").matcher(num).find();
    }
    
    protected static int ubyte(final byte b) {
        return b & 0xFF;
    }
    
    public static int ushort(final short s) {
        return s & 0xFFFF;
    }
    
    protected static String parseString(final String str, final String wall) {
        return str.contains(wall) ? str.substring(str.indexOf(wall) + 1) : null;
    }
    
    protected static boolean CheckString(final String str, final String c) {
        return Pattern.compile(c).matcher(str).find();
    }
    
    protected static String strSQL(final String str) {
        return str.replaceAll("['\"\\\\%]", "\\\\$0");
    }
    
    public static int nextInt(final int x1, final int x2) {
        int to = x2;
        int from = x1;
        if (x2 < x1) {
            to = x1;
            from = x2;
        }
        return from + Util.rand.nextInt(to + 1 - from);
    }
    
    public static int nextInt(final int max) {
        return Util.rand.nextInt(max);
    }
    
    protected static String getStrTime(final long time) {
        if (time >= 86400000L) {
            return time / 86400000L + " ng\u00e0y";
        }
        if (time >= 3600000L) {
            return time / 3600000L + " gi\u1edd";
        }
        if (time >= 60000L) {
            return time / 60000L + " ph\u00fat";
        }
        if (time >= 1000L) {
            return time / 1000L + " gi\u00e2y";
        }
        return time / 1000.0f + " gi\u00e2y";
    }
    
    protected static synchronized Calendar Calendar(final long time) {
        final Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(time);
        return rightNow;
    }
    
    protected static void setDebug(final boolean v) {
        Util.debug = v;
    }
    
    protected static void log(final String v) {
        if (Util.debug) {
            System.out.println(v);
        }
    }
    
    static {
        Util.debug = false;
        locale = new Locale("vi");
        en = NumberFormat.getInstance(Util.locale);
        rand = new Random();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        alphaUpperCase = "abcdefghijklmnopqrstuvwxyz".toUpperCase();
        ALPHA_NUMERIC = "abcdefghijklmnopqrstuvwxyz" + Util.alphaUpperCase + "0123456789";
        ALL = "abcdefghijklmnopqrstuvwxyz" + Util.alphaUpperCase + "0123456789" + "~=+%^*/()[]{}/!@#$?|";
    }
    
    public static void WriteLog (String str){
        try {
            String filename = "log.txt";
            FileWriter fw = new FileWriter(filename,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write( "\"" + str + "\"," + "\n");
            bw.close();
            fw.close();
        } catch (Exception ignored) {
        }
    }
}
