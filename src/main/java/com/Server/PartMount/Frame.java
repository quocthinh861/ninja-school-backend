package com.Server.PartMount;

import java.util.Arrays;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 *
 * @author KHONG MINH TIEN
 */
public class Frame {
    public byte count;
    public short[] idIcon;
    
    public static Frame parseFrame(final String s) {
        try {
            final Frame frame = new Frame();
            final JSONArray jframe = (JSONArray)JSONValue.parseWithException(s);
            final byte count = Byte.parseByte(jframe.get(0).toString());
            final JSONArray img = (JSONArray)jframe.get(1);
            frame.idIcon = new short[count];
            frame.count = count;
            for (short i = 0; i < count; ++i) {
                frame.idIcon[i] = Short.parseShort(img.get(i).toString());
            }
            return frame;
        }
        catch (ParseException | NumberFormatException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
}
