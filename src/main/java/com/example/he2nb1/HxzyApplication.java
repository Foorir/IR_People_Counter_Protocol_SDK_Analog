package com.example.he2nb1;

import com.example.he2nb1.server.NettyTcpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HxzyApplication extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HxzyApplication.class, args);
    }


    @Autowired
    NettyTcpServer nettyTcpServer;


    @Override
    public void run(String... args) throws Exception {
        nettyTcpServer.startListen();
    }
}
