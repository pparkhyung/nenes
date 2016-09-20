package server.boot;

import server.echo.EchoServer;

public class Boot {

    public Boot() throws Exception {
        System.out.println("server bootstrap 시작");
        new EchoServer().start();
    }

}
