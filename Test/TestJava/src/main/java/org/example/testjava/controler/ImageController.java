package org.example.testjava.controler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ImageController {
    @GetMapping("/hello")
    public ResponseEntity<Message> hello() {
        Message message = new Message("Hello from Java!");
        return ResponseEntity.ok(message);
    }

    @PostMapping("/echo")
    public ResponseEntity<Message> echo(@RequestBody Message inputMessage) {
        Message message = new Message("Echo: " + inputMessage.getContent());
        return ResponseEntity.ok(message);
    }
}

