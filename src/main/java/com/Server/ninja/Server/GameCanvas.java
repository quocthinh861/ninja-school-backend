 package com.Server.ninja.Server;

import com.Server.io.Message;
import lombok.val;

import javax.swing.ImageIcon;
import java.awt.Image;

public class GameCanvas
{
    public static Image loadImage(final String path) {
        Image result = null;
        try {
            final ImageIcon icon = new ImageIcon("");
            result = icon.getImage();
        }
        catch (Exception ex) {
            Util.log("ERROR LOAD:" + path);
        }
        return result;
    }

    protected static void EndWait(final Char _char, final String text) {
        Message msg = null;
        try {
            msg = Service.messageSubCommand((byte)(-89));
            msg.writer().writeUTF(text);
            _char.user.session.sendMessage(msg);
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

    protected static void callEffect(final Char _char, final short id) {
        Message msg = null;
        try {
            msg = Service.messageSubCommand((byte)(-78));
            msg.writer().writeShort(id);
            _char.user.session.sendMessage(msg);
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

    protected static void callEffectBall(final Char _char) {
        Message msg = null;
        try {
            msg = Service.messageSubCommand((byte)(-58));
            _char.user.session.sendMessage(msg);
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

    protected static void callEffectBall1(final Char _char) {
        Message msg = null;
        try {
            msg = Service.messageSubCommand((byte)(-57));
            _char.user.session.sendMessage(msg);
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

    protected static void addInfoDlg(final Char _char, final String s) {
        Message msg = null;
        try {
            msg = Service.messageNotMap((byte)(-86));
            msg.writer().writeUTF(s);
            _char.user.session.sendMessage(msg);
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

    protected static void startOKDlg(final Session_ME session, final String info) {
        Message msg = null;
        try {
            msg = new Message((byte)(-26));
            msg.writer().writeUTF(info);
            session.sendMessage(msg);
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

    protected static void ServerAlert(final Player _char, final String str) {
        Message msg = null;
        try {
            if (str.length() > 0) {
                msg = new Message((byte)(-25));
                msg.writer().writeUTF(str);
                _char.user.session.sendMessage(msg);
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

    protected static void getEffectAuto(final Char _char, final short id, final short x, final short y, final byte loopCount, final short time) {
        Message msg = null;
        try {
            msg = new Message((byte)122);
            msg.writer().writeByte(1);
            msg.writer().writeByte(id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(loopCount);
            msg.writer().writeShort(time);
            _char.user.session.sendMessage(msg);
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
    protected static void getEffect(final Char _char, final byte b, final int vId, final short id, final int timelive, final int miliSecondWait, final boolean isHead) {
        Message msg = null;
        try {
            msg = new Message((byte)125);
            msg.writer().writeByte(0);
            msg.writer().writeByte(b);
            if (b == 1) {
                msg.writer().writeByte(vId);
            }
            else {
                msg.writer().writeInt(vId);
            }
            msg.writer().writeShort(id);
            msg.writer().writeInt(timelive);
            msg.writer().writeByte(miliSecondWait);
            msg.writer().writeByte(isHead ? 1 : 0);
            _char.user.session.sendMessage(msg);
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

    protected static void getImgEffect(final Session_ME session, final short id) {
        Message msg = null;
        try {
            final byte[] ab = NinjaUtil.getFile("cache/Effect/x" + session.zoomLevel + "/ImgEffect/ImgEffect " + id + ".png");
            if (ab != null) {
                msg = new Message((byte)125);
                msg.writer().writeByte(1);
                msg.writer().writeByte(id);
                NinjaUtil.writeByteArray(msg, ab);
                session.sendMessage(msg);
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

    protected static void getDataEffect(final Session_ME session, final short id) {
        Message msg = null;
        try {
            final byte[] ab = NinjaUtil.getFile("cache/Effect/x" + session.zoomLevel + "/DataEffect/" + id);
            if (ab != null) {
                msg = new Message((byte)125);
                msg.writer().write(ab);
                session.sendMessage(msg);
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

    public static void sendDataEffAuto(final Char _char, final int type, final short id) {
        Message msg = null;
        Session_ME session = _char.user.session;
        try {
            msg = new Message(122);
            switch (type) {
                case 0: {
                    msg.writer().writeByte(2);
                    msg.writer().writeByte(id);
                    final byte[] data = NinjaUtil.getFile("cache/Effauto/x" + session.zoomLevel + "/Img/" + id + ".png");
                    msg.writer().writeInt(data.length);
                    msg.writer().write(data);
                    break;
                }
                case 1: {
                    msg.writer().writeByte(3);
                    msg.writer().writeByte(id);
                    final byte[] data = NinjaUtil.getFile("cache/Effauto/x" + session.zoomLevel + "/Data/" + id);
                    msg.writer().writeShort(data.length);
                    msg.writer().write(data);
                    break;
                }
            }
            session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }
}
