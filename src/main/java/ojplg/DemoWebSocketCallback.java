package ojplg;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import java.util.ArrayList;
import java.util.List;



public class DemoWebSocketCallback implements WebSocketConnectionCallback {

    private final List<WebSocketChannel> channelList = new ArrayList<>();
    private int count = 0;

    public DemoWebSocketCallback(){
        System.out.println("Constructing a DemoWebSocketCallback");
        Thread thread = new Thread(() -> {
            while(true) {
                count++;
                System.out.println("Server count is " + count + " and there are " + channelList.size() + " open channels");
                for (WebSocketChannel channel : channelList) {
                    WebSockets.sendText("Count is " + count, channel, null);
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
        webSocketChannel.getReceiveSetter().set(new WebSocketListener());
        channelList.add(webSocketChannel);
        WebSockets.sendText("Hello!", webSocketChannel, null);
        webSocketChannel.resumeReceives();
    }

}
