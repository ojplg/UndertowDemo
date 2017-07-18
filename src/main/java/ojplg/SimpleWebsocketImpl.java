package ojplg;

import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import java.util.function.Consumer;

public class SimpleWebsocketImpl implements SimpleWebsocket {

    private final WebSocketChannel channel;

    public SimpleWebsocketImpl(WebSocketChannel channel){
        this.channel = channel;
    }

    @Override
    public void sendMessage(String message) {
        WebSockets.sendText(message, channel, null);
    }

    @Override
    public void onMessageReceived(Consumer<String> receiver) {
        channel.getReceiveSetter().set(new WebSocketListener(receiver));

    }
}
