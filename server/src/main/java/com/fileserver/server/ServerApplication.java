package com.fileserver.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ServerApplication {

    @RequestMapping("/")
    String home() {
        return "Hello File Uploder Server";
    }


    @RequestMapping("/uploadFile")
    String uploadFile(@RequestParam("filePath") String paths) {
        return paths;
    }


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
    @Bean
    public NettyFileServer fileUploadServer() throws Exception {
        NettyFileServer fileUploadServer = new NettyFileServer();
        fileUploadServer.init();
        return fileUploadServer;
    }
}
