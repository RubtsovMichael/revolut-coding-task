package rubtsov.revolut.application;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class App {

    public static final String BASE_URI = "http://localhost:8080/api/";

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = startServer(new AppConfig());
        System.out.println("Server started at " + BASE_URI + ". Hit enter to stop it.");
        System.in.read();
        httpServer.shutdownNow();
    }

    public static HttpServer startServer(AppConfig appConfig) {
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), appConfig);
    }

}
