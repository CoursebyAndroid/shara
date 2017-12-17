package com.example.leshchenko.socketchat;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Root on 16.12.2017.
 */

public class Xz {
    private String ip;
    private int port;
    private Socket socket;


    public Xz(String ip, int port) {
        this.ip = ip;
        this.port = port;

    }
}
