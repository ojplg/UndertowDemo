package ojplg;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletInfo;
import io.undertow.websockets.WebSocketProtocolHandshakeHandler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class UndertowDemo {

    public static void main(String[] args){

        try {
            System.out.println("Beginning configuration");

            // Create a minimum DeploymentInfo. These settings are required.
            DeploymentInfo deploymentInfo = Servlets.deployment()
                    .setClassLoader(UndertowDemo.class.getClassLoader())
                    .setContextPath("/")
                    .setDeploymentName("sv.war");

            // Create a way to access static resources like HTML files
            Path staticPath = Paths.get("target", "classes");
            PathResourceManager staticResources = new PathResourceManager(staticPath, 100);
            deploymentInfo.setResourceManager(staticResources)
                    .addWelcomePage("index.html");

            // Create a servlet info for dynamic content
            // Using a factory allows us to own servlet creation
            // and set collaborators into the servlet implementation
            ServletInfo servletInfo = Servlets.servlet("MyServlet", MySillyServlet.class, new SillyServletFactory());
            servletInfo.addMapping("/myservlet/*");
            servletInfo.addInitParam(MySillyServlet.MESSAGE, "Something from the factory");
            deploymentInfo.addServlet(servletInfo);

            // Deploy and start the servlet container with the static content
            // and the dynamic servlet
            DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
            manager.deploy();
            HttpHandler httpHandler = manager.start();

            // Create a web socket handler
            // Similar to the servlet creation, we use a factory mechanism
            // to allow control over the creation process for web socket connections
            Consumer<String> receiver = (msg) -> { System.out.println("I got a message " + msg);};
            WebSocketsManager socketsManager = new WebSocketsManager();
            socketsManager.startHeartbeats();
            WebSocketProtocolHandshakeHandler webSocketHandler = Handlers.websocket(new DemoWebSocketCallback(socketsManager, receiver));

            // Attach the web socket handler to the handled paths
            PathHandler myPathHandler = Handlers.path(httpHandler);
            myPathHandler.addPrefixPath("/ws", webSocketHandler);

            // Create and start the server with all three working:
            // 1. static content handler
            // 2. dynamic servlet
            // 3. web socket support
            Undertow server = Undertow.builder()
                    .addHttpListener(8080, "localhost")
                    .setHandler(myPathHandler)
                    .build();
            System.out.println("Starting undertow server ... ");
            server.start();

        } catch (Exception ex){
            System.out.println("BAD " + ex);
            ex.printStackTrace();
            System.exit(-1);
        }
    }

}
