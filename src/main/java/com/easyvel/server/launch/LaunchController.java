package com.easyvel.server.launch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/launch")
@RestController
public class LaunchController {

    @Value("${easyvel.info.version}")
    private String version;

    @GetMapping("/version")
    public String getVersion() {
        return version;
    }
}
