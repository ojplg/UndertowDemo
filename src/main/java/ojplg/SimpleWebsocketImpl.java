package ojplg;

import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import java.util.function.Consumer;

public class SimpleWebsocketImpl implements SimpleWebsocket {

    private final WebSocketChannel channel;

    public SimpleWebsocketImpl(WebSocketChannel channel){
        this.channel = channel;
        channel.addCloseTask(
                this::onClose
        );
    }

    @Override
    public void sendMessage(String message) {
        WebSockets.sendText(message, channel, null);
    }

    @Override
    public void onMessageReceived(Consumer<String> receiver) {
        channel.getReceiveSetter().set(new WebSocketListener(receiver));
    }

    private void onClose(WebSocketChannel closed){
        System.out.println("Closed " + channel + " also known as " + closed);
    }
}
