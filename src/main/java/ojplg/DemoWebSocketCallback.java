package ojplg;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import java.util.function.Consumer;


public class DemoWebSocketCallback implements WebSocketConnectionCallback {

    private final Consumer<String> onMessageHandler;
    private final WebSocketsManager manager;

    public DemoWebSocketCallback(WebSocketsManager manager, Consumer<String> onMessageHandler){
        System.out.println("Constructing a DemoWebSocketCallback");
        this.onMessageHandler = onMessageHandler;
        this.manager = manager;
    }

    @Override
    public void onConnect(WebSocketHttpExchange webSocketHttpExchange, WebSocketChannel webSocketChannel) {
        System.out.println("New web socket connection from " + webSocketChannel.getPeerAddress());
        SimpleWebsocketImpl webSocket = new SimpleWebsocketImpl(webSocketChannel);
        manager.addSocket(webSocket);
        webSocket.onMessageReceived(onMessageHandler);
        webSocketChannel.resumeReceives();
        webSocket.sendMessage("Welcome to the service!");
    }

}
