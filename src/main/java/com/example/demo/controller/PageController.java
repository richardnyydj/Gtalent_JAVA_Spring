package com.example.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/page")
public class PageController {
    @GetMapping("/")
    public String test(
        @RequestParam(value = "message", required = false, defaultValue = "您好Java Spring!") String message,
        @RequestParam(value = "description", required = false, defaultValue = "我是描述!") String description,
        Model model
    ) {
        model.addAttribute("message", message);
        model.addAttribute("description", description);
        return "index";
    }

    @GetMapping("/card")
    public String card(
        Model model
    ) {
        return "card";
    }

    @GetMapping("/example")
    public String example(
        Model model
    ) {
        return "example";
    }

    @GetMapping("/jquery")
    public String jquery(
        Model model
    ) {
        return "jquery";
    }

    @GetMapping("ajax")
    public String ajax(
        Model model
    ) {
        return "ajax";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}
