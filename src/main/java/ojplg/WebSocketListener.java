package ojplg;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;

import java.util.function.Consumer;

class WebSocketListener extends AbstractReceiveListener {

    private final Consumer<String> onMessage;

    WebSocketListener(Consumer<String> onMessage){
        this.onMessage = onMessage;
    }

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
        final String messageData = message.getData();
        System.out.println("Received message from browser.  Channel was: "
                + channel + " Message: " + messageData);
        onMessage.accept(messageData);
    }

}