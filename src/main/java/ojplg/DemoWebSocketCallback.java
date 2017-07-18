package ojplg;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class DemoWebSocketCallback implements WebSocketConnectionCallback {


    private final List<SimpleWebsocket> sockets = new ArrayList<>();
    private int count = 0;

    private final Consumer<String> onMessageHandler;

    public DemoWebSocketCallback(Consumer<String> onMessageHandler){
        System.out.println("Constructing a DemoWebSocketCallback");
        this.onMessageHandler = onMessageHandler;
        Thread thread = new Thread(() -> {
            while(true) {
                count++;
                System.out.println("Server count is " + count + " and there are " + sockets.size() + " open channels");
                for (SimpleWebsocket socket : sockets) {
                    socket.sendMessage("Count is now " + count + ".");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }

        });
        thread.start();
        System.out.println("Counting started");
    }

    @Override
    public void onConnect(WebSocketHttpExchange webSocketHttpExchange, WebSocketChannel webSocketChannel) {
        System.out.println("New web socket connection from " + webSocketChannel.getPeerAddress());
        SimpleWebsocketImpl webSocket = new SimpleWebsocketImpl(webSocketChannel);
        webSocket.onMessageReceived(onMessageHandler);
        sockets.add(webSocket);
        webSocketChannel.resumeReceives();
        webSocket.sendMessage("Welcome to the service!");
    }

}
