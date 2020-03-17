package com.aofeng.controlmanage.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketPool {
    private static final ConcurrentHashMap<String, ClientSocket> ONLINE_SOCKET_MAP = new ConcurrentHashMap<>();

    public static void add(ClientSocket clientSocket) {
        if (clientSocket != null && !clientSocket.getKey().isEmpty())
            ONLINE_SOCKET_MAP.put(clientSocket.getKey(), clientSocket);
    }

    public static void remove(String key) {
        if (!key.isEmpty()) ONLINE_SOCKET_MAP.remove(key);
    }

    public static ClientSocket getCLientSocket(String key) {
        return ONLINE_SOCKET_MAP.get(key);
    }

    public static List<String> getAllKeys() {
        List<String> result=new ArrayList<>();
        for (Map.Entry<String, ClientSocket> entry :ONLINE_SOCKET_MAP.entrySet()) {
            result.add(entry.getValue().getKey());
        }
        return result;
    }
}
