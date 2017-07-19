package ojplg;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class WebSocketsManager implements WebSocketConnectionCallback {

    private final List<SimpleWebsocket> sockets = Collections.synchronizedList(new ArrayList<>());
    private final Consumer<String> onMessageHandler;
    private int heartbeatCount;
    private boolean running = true;

    public WebSocketsManager(Consumer<String> onMessageHandler) {
        this.onMessageHandler = onMessageHandler;
    }

    public void broadcastGlobalMessage(String message){
        sockets.forEach(s->s.sendMessage(message));
    }

    public int currentOpenSocketsCount(){
        return sockets.size();
    }

    private void addSocket(SimpleWebsocket socket){
        sockets.add(socket);
        socket.onSocketClose(sockets::remove);
    }

    @Override
    public void onConnect(WebSocketHttpExchange webSocketHttpExchange, WebSocketChannel webSocketChannel) {
        System.out.println("New web socket connection from " + webSocketChannel.getPeerAddress());
        SimpleWebsocketImpl webSocket = new SimpleWebsocketImpl(webSocketChannel);
        addSocket(webSocket);
        webSocket.onMessageReceived(onMessageHandler);
        webSocketChannel.resumeReceives();
        webSocket.sendMessage("Welcome to the service!");
    }

    public void startHeartbeats(){
        Thread thread = new Thread(() -> {
            while(running) {
                heartbeatCount++;
                System.out.println("Heartbeat count is " + heartbeatCount + " and there are " + currentOpenSocketsCount() + " open channels");
                broadcastGlobalMessage("Server heartbeat " + heartbeatCount);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    // ignore
                }
            }
        });
        thread.start();
        System.out.println("Heartbeats started");
    }

}
