package ojplg;

import java.util.function.Consumer;

public interface SimpleWebsocket {

    void sendMessage(String message);
    void onMessageReceived(Consumer<String> receiver);

}
