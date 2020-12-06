package com.game.service;


import com.game.controller.GameInterface;
import com.game.domain.Result;
import com.game.net.ChessLocation;
import com.game.net.ConnectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

/**
 * @author VIRIYA
 * @create 2020/10/24 10:40
 */
public class NetworkService {

    private static final Logger logger = LoggerFactory.getLogger("com.game.service");

    private static NetworkService networkService;
    private Socket client;
    private Socket server;

    private boolean isSuccessConnect = false;

    private boolean isFirstPlay = false;
    private String ip;
    private int port;

    private GameInterface.FlushBoardCallback flushBoardCallback;
    private GameInterface.HitMessageCallback hitMessageCallback;


    private NetworkService() {

    }

    public static NetworkService getInstance() {
        if (networkService == null) {
            networkService = new NetworkService();
        }
        return networkService;
    }

    public void addFlushBoardCallBack(GameInterface.FlushBoardCallback flushBoardCallback) {
        this.flushBoardCallback = flushBoardCallback;
    }

    public void addHitMessageCallback(GameInterface.HitMessageCallback hitMessageCallback) {
        this.hitMessageCallback = hitMessageCallback;
    }


    public void startListen(Integer port) {
        // start TCP listen server
        new TcpReceiveServer(port).start();
    }


    private class TcpReceiveServer extends Thread {

        private int port;

        public TcpReceiveServer(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            /*
             * tip: new ServerSocket(port,3) refuse connect when beyond 3 in connect number
             */
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                // get socket from queue
                server = serverSocket.accept();
                // new thread for deal with connect of socket
                new HandlerThread(server).start();
            } catch (Exception e) {
                logger.error("Server runtime exception: " + e.getMessage());
            }
        }
    }


    public boolean send(Object message) {
        try {
            ObjectOutputStream  oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }


    private class HandlerThread extends Thread {

        private Socket sock;

        public HandlerThread(Socket socket) {
            this.sock = socket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    InputStream inputStream = sock.getInputStream();
                    // here blocked when without message come here
                    if (inputStream.available() > 0) {
                        ObjectInputStream ios = new ObjectInputStream(inputStream);
                        // read data from client
                        Object message = ios.readObject();
                        logger.info("recv message : " + message);

                        // message class type : 1. ChessLocation 2. Result
                        if (message instanceof ChessLocation) {
                            flushBoardCallback.call(((ChessLocation) message).getPoint(), ((ChessLocation) message).getChessEnumType());
                        } else if (message instanceof Result) {
                            // call back game result dialog
                            hitMessageCallback.call((Result) message);
                        } else {
                            // TODO: How judge who is first player
                            // response client
                            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
                            oos.writeObject("ACK");
                            oos.flush();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Server runtime exception: " + e.getMessage());
                // close socket ???
            }
        }

    }


    public void config(ConnectMessage message) {
        this.ip = message.ip();
        this.port = message.port();
        // don't need
        this.isFirstPlay = message.firstPlay();
    }

    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    /**
     * only connect server of another client
     * connect after send a flag that who first play
     * always first play who first connect another
     *
     * @return true if connect succeed
     */
    public boolean connect() {

        if (isSuccessConnect) {
            logger.info("already connect!");
            return true;
        }

        try {
            client = new Socket(ip, port);
            out = new ObjectOutputStream(client.getOutputStream());
            out.writeObject("First");
            out.flush();

            logger.info("client create finished");

            in = new ObjectInputStream(client.getInputStream());
            Object obj = in.readObject();
            isSuccessConnect = obj != null;
            return isSuccessConnect;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("connect exception: " + e.getMessage());
            isSuccessConnect = false;
            return false;
        }
    }

    public boolean getConnectResult() {
        return isSuccessConnect;
    }

    public boolean getFirstPlay() {
        return isFirstPlay;
    }

    public void setFirstPlay(boolean firstPlay) {
        this.isFirstPlay = firstPlay;
    }

}
