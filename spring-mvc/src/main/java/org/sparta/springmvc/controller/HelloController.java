package org.sparta.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class HelloController {
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "담!!!";// 나중에는 여기서 어떤건지 찾음 지금은 body
    }

    @GetMapping("get")
    @ResponseBody
    public String get() {
        return "Get Method 요청!";
    }

    @PostMapping("post")
    @ResponseBody
    public String post() {
        return "Post Method 요청!";
    }

    @PutMapping("/put")
    @ResponseBody
    public String put() {
        return "Put Method 요청!";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public String delete() {
        return "Delete Method 요청!";
    }
}
