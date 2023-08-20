package ru.mukhutdinov.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mukhutdinov.demo.controller.pojo.MailingRequest;
import ru.mukhutdinov.demo.controller.pojo.MovingRequest;
import ru.mukhutdinov.demo.service.MailingService;
import ru.mukhutdinov.demo.service.MovingService;

@RestController
public class Controller {
    @Autowired
    private MailingService mailingService;
    @Autowired
    private MovingService movingService;

    @PostMapping(value = "/registration", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity.BodyBuilder registration(@RequestBody MailingRequest mailingRequest) throws Exception {
        mailingService.saveMailing(mailingRequest);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @PostMapping(value = "/departure", consumes = MediaType.ALL_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity.BodyBuilder arrival(@RequestBody MovingRequest movingRequest) throws Exception {
        movingService.fixSending(movingRequest);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @PostMapping(value = "/arrival", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity.BodyBuilder departure(@RequestBody MovingRequest movingRequest) throws Exception {
        movingService.fixArrival(movingRequest);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping(value = "/getPath", consumes = MediaType.ALL_VALUE, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> getPath(@RequestBody String mailing) throws Exception {
        return ResponseEntity.ok(movingService.getInfo(mailing));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

}
