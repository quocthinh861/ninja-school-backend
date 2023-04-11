 package com.Server.ninja.Server;

import com.Server.io.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Client {
    protected static int sizeClients;
    protected static final ArrayList<Session_ME> clients;
    protected static final Object LOCK;

    protected static void chatServer(final String whochat, final String chat) {
        try {
            final Message msg = new Message((byte) (-21));
            try {
                msg.writer().writeUTF(whochat);
                msg.writer().writeUTF(chat);
                synchronized (Client.LOCK) {
                    for (int i = 0; i < Client.clients.size(); ++i) {
                        if (Client.clients.get(i).user != null && Client.clients.get(i).user.player != null) {
                            Client.clients.get(i).sendMessage(msg);
                        }
                    }
                }
            } finally {
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void alertServer(final String str) {
        try {
            final Message msg = new Message((byte) (-25));
            try {
                msg.writer().writeUTF(str);
                synchronized (Client.LOCK) {
                    for (int i = 0; i < Client.clients.size(); ++i) {
                        if (Client.clients.get(i).user != null && Client.clients.get(i).user.player != null) {
                            Client.clients.get(i).sendMessage(msg);
                        }
                    }
                }
            } finally {
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void startOKDlgServer(final String info) {
        try {
            final Message msg = new Message((byte) (-26));
            try {
                msg.writer().writeUTF(info);
                synchronized (Client.LOCK) {
                    for (int i = 0; i < Client.clients.size(); ++i) {
                        if (Client.clients.get(i).user != null && Client.clients.get(i).user.player != null) {
                            Client.clients.get(i).sendMessage(msg);
                        }
                    }
                }
            } finally {
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void joinClient(final Session_ME cl) {
        synchronized (Client.LOCK) {
            if (!Client.clients.contains(cl)) {
                Client.clients.add(cl);
                ++Client.sizeClients;
            }
        }
    }

    protected static void removeClinet(final Session_ME cl) {
        synchronized (Client.LOCK) {
            if (Client.clients.contains(cl)) {
                Client.clients.remove(cl);
                --Client.sizeClients;
                System.err.println("Disconnect client : " + cl);
            }
        }
    }

    protected static int sizeUser() {
        int size = 0;
        synchronized (Client.LOCK) {
            for (int i = 0; i < Client.clients.size(); ++i) {
                if (Client.clients.get(i).user != null) {
                    ++size;
                }
            }
        }
        return size;
    }

    protected static Player getPlayer(final String cName) {
        synchronized (Client.LOCK) {
            for (int i = 0; i < Client.clients.size(); ++i) {
                if (Client.clients.get(i).user != null && Client.clients.get(i).user.player != null && Client.clients.get(i).user.player != null && Client.clients.get(i).user.player.cName.equals(cName)) {
                    return Client.clients.get(i).user.player;
                }
            }
        }
        return null;
    }

    protected static Player getPlayer(final int charID) {
        synchronized (Client.LOCK) {
            for (int i = 0; i < Client.clients.size(); ++i) {
                if (Client.clients.get(i).user != null && Client.clients.get(i).user.player != null && Client.clients.get(i).user.player != null && Client.clients.get(i).user.player.charID == charID) {
                    return Client.clients.get(i).user.player;
                }
            }
        }
        return null;
    }

    protected static User getUser(final String uname) {
        synchronized (Client.LOCK) {
            for (int i = 0; i < Client.clients.size(); ++i) {
                if (Client.clients.get(i).user != null && Client.clients.get(i).user.userName.equals(uname)) {
                    return Client.clients.get(i).user;
                }
            }
        }
        return null;
    }

    private static final String DATE_FORMAT_FILE = "dd_MMM_yyyy";

    public static void writeIp(String ip) {
        try {
            Calendar calender = Calendar.getInstance();
            Date date = calender.getTime();
            SimpleDateFormat dateFormatFile;
            dateFormatFile = new SimpleDateFormat(DATE_FORMAT_FILE);
            String str = Util.toDateString(Date.from(Instant.now()));
            File f = new File("log/");
            if (!f.exists()) {
                f.mkdirs();
            }
            File file = new File("log/" + dateFormatFile.format(date) + "_logip.txt");
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(ip + " Login vào " + str);
            bw.newLine();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static {
        clients = new ArrayList<>();
        LOCK = new Object();
    }
}
