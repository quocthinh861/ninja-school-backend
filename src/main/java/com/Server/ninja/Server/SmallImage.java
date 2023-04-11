 package com.Server.ninja.Server;

import com.Server.io.Message;

public class SmallImage
{
    protected static void reciveImage(final User user, final int id) {
        Message msg = null;
        try {
            byte zoom = user.session.zoomLevel;
            if (zoom < 1) {
                zoom = 1;
            }
            else if (zoom > 4) {
                zoom = 4;
            }
            final byte[] ab = NinjaUtil.getFile("assets/x" + zoom + "/icon/" + id + ".png");
            if (ab != null) {
                msg = Service.messageNotMap((byte)(-115));
                msg.writer().writeInt(id);
                NinjaUtil.writeByteArray(msg, ab);
                user.session.sendMessage(msg);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
}
