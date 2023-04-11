package com.Server.io;

import java.net.URL;

public class API_Card
{
    private static String APIURL;
    private static String APIKEY;
    private static String UserName;
    private static int[] Arrdenominations;
    private static String[] TypeCard;
    
    public static void NapThe(final String cardCode, final String serialCard, final int typeCard, final int denominations) {
        try {
            final URL obj = new URL(String.format(API_Card.APIURL, API_Card.UserName, API_Card.TypeCard[typeCard], cardCode, API_Card.APIKEY, "", serialCard, API_Card.Arrdenominations[denominations]));
            System.out.println(obj);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        API_Card.APIURL = "https://thecaosieure.com/gachthe?account=%d&cardType=%d&cardCode=%d&APIKey=%d&transId=%d&cardSerial=%d&cardAmount=%d";
        API_Card.APIKEY = "XOXHZYCVAHVMGJNRDNYSMCMJVHFTFGIV";
        API_Card.UserName = "banthuhuong";
        API_Card.Arrdenominations = new int[] { 10000 };
        API_Card.TypeCard = new String[] { "Viettel" };
    }
}
