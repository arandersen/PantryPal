package project;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MyServer {

  // initialize server port and hostname
  private static final int SERVER_PORT = 8100;
  private static final String SERVER_HOSTNAME = "0.0.0.0";

  public static void main(String[] args) throws IOException {
    // create a thread pool to handle requests
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    // create a server
    HttpServer server = HttpServer.create(
        new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
        0);

    HttpHandler RequestHandler = new RequestHandler();
    server.createContext("/", RequestHandler);

    server.setExecutor(threadPoolExecutor);

    server.start();

    System.out.println("Server started on port " + SERVER_PORT);
  }
}