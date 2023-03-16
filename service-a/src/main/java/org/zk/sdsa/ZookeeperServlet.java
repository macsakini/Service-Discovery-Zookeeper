package org.zk.sdsa;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;

public class ZookeeperServlet extends HttpServlet {
    int port = 9000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
        RequestDispatcher view = request.getRequestDispatcher("/Users/mac/Documents/zksds/service-a/src/main/java/org/zk/sdsa/templates/index.html");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    protected void startServer() throws Exception {
        Server server = new Server(port);

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");

        ServletHolder servletHolder = new ServletHolder(new ZookeeperServlet());
        handler.addServlet(servletHolder, "/");

        server.setHandler(handler);
        server.start();
        server.join();
    }
}
