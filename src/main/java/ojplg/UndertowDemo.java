package ojplg;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.core.InMemorySessionManagerFactory;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.WebSocketProtocolHandshakeHandler;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.websocket;


public class UndertowDemo {

    public static void main(String[] args){

        try {
            System.out.println("Starting");

            Path staticPath = Paths.get("target", "classes");
            PathResourceManager staticResources = new PathResourceManager(staticPath, 100);

            DeploymentInfo servletBuilder = Servlets.deployment()
                    .setClassLoader(UndertowDemo.class.getClassLoader())
                    .setContextPath("/")
                    .setDeploymentName("sv.war")
                    .setSessionManagerFactory(new InMemorySessionManagerFactory())
                    .setResourceManager(staticResources)
                    .addServlet(Servlets.servlet("MyServlet", MySillyServlet.class)
                            .addInitParam("message", "Somehing silly to show the user")
                            .addMapping("/myservlet"));

            DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
            manager.deploy();

            PathHandler myPathHandler = Handlers.path(manager.start());

            WebSocketProtocolHandshakeHandler webSocketHandler = Handlers.websocket(new DemoWebSocketCallback());

            myPathHandler.addPrefixPath("/ws", webSocketHandler);

            Undertow server = Undertow.builder()
                    .addHttpListener(8080, "localhost")
                    .setHandler(myPathHandler)
                    .build();

            server.start();
        } catch (Exception ex){
            System.out.println("BAD " + ex);
            ex.printStackTrace();
            System.exit(-1);
        }

    }

}
