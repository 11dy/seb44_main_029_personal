//package com.example.server.health;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/health")
//public class HealthCheckController {
//    @GetMapping("/check")
//    public ResponseEntity<String> healthCheck(){
//        return ResponseEntity.ok().body("health check done");
//    }
//}
// alb 의 상태코드를 404로 설정.