package ojplg;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;

public class WebSocketListener extends AbstractReceiveListener {

    public WebSocketListener(){
        System.out.println("Constructing a web socket listener");
    }

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
        final String messageData = message.getData();
        System.out.println("Received message from browser.  Channel was: "
                + channel + " Message: " + messageData);
    }


}