package ojplg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebSocketsManager {

    private final List<SimpleWebsocket> sockets = Collections.synchronizedList(new ArrayList<>());
    private int heartbeatCount;

    public void broadcastGlobalMessage(String message){
        sockets.forEach(s->s.sendMessage(message));
    }

    public int currentOpenSocketsCount(){
        return sockets.size();
    }

    public void addSocket(SimpleWebsocket socket){
        sockets.add(socket);
        socket.onSocketClose( ws -> this.sockets.remove(ws));
    }

    public void startHeartbeats(){
        Thread thread = new Thread(() -> {
            while(true) {
                heartbeatCount++;
                System.out.println("Heartbeat count is " + heartbeatCount + " and there are " + currentOpenSocketsCount() + " open channels");
                broadcastGlobalMessage("Server heartbeat " + heartbeatCount);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        });
        thread.start();
        System.out.println("Heartbeats started");
    }
}