package com.game.service;


import com.game.controller.GameInterface;
import com.game.net.ConnectParams;
import com.game.net.protocol.ConnectBehavior;
import com.game.net.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

/**
 * @author VIRIYA
 * @create 2020/10/24 10:40
 *
 * TODO: SOCKET THREAD WILL CLOSED WHEN APPLICATION CLOSED
 */
public class NetworkService implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger("com.game.service");

    private static NetworkService networkService;
    private Socket client;
    private Socket server;

    private boolean isSuccessConnect = false;

    private String ip;
    private int port;
    private boolean isFirst = false;

    private GameInterface.FlushBoardCallback flushBoardCallback;
    private GameInterface.HitMessageCallback hitMessageCallback;
    private GameInterface.FirstPlayCallback firstPlayCallback;


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

    public void addFirstCallback(GameInterface.FirstPlayCallback firstPlayCallback) {
        this.firstPlayCallback = firstPlayCallback;
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
                new ProtocolReceiveHandlerThread(server).start();
            } catch (Exception e) {
                logger.error("Server runtime exception: " + e.getMessage());
            }
        }
    }


    public boolean send(Protocol message) {
        try {
            specifySend(client, message);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }


    private class ProtocolReceiveHandlerThread extends Thread {

        private Socket sock;

        public ProtocolReceiveHandlerThread(Socket socket) {
            this.sock = socket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    InputStream inputStream = sock.getInputStream();
                    // here blocked when without message come here
                    // TODO: refactor it: get system resources is too more!!!
                    if (inputStream.available() > 0) {
                        ObjectInputStream ios = new ObjectInputStream(inputStream);
                        // read data from client
                        Protocol message = (Protocol) ios.readObject();
                        logger.info("recv message : " + message);

                        message.needExecutor(networkService);
                        message.socket(sock);
                        message.execute();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Server runtime exception: " + e.getMessage());
                // close socket ???
            }
        }

    }


    /**
     * send message to another player
     */
    public void specifySend(Socket socket, Protocol message) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message);
        oos.flush();
    }


    public void config(ConnectParams message) {
        this.ip = message.ip();
        this.port = message.port();
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
            specifySend(client, new ConnectBehavior());
            logger.info("client create finished");

            in = new ObjectInputStream(client.getInputStream());
            Object obj = in.readObject();
            isSuccessConnect = obj != null;
            return isSuccessConnect;

        } catch (Exception e) {
            logger.error("connect exception: " + e.getMessage());
            isSuccessConnect = false;
            return false;
        }
    }

    public boolean getConnectResult() {
        return isSuccessConnect;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public GameInterface.FlushBoardCallback getFlushBoardCallback() {
        return flushBoardCallback;
    }

    public GameInterface.HitMessageCallback getHitMessageCallback() {
        return hitMessageCallback;
    }

    public GameInterface.FirstPlayCallback getFirstPlayCallback() {
        return firstPlayCallback;
    }
}
