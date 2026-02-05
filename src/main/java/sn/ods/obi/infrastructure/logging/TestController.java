package sn.ods.obi.infrastructure.logging;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
public class TestController {

    @GetMapping("/error")
    public String triggerError() {
        throw new RuntimeException("This is a test exception");
    }
}
