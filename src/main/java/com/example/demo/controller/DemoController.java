package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.DemoModel;
import com.example.demo.service.DemoService;

import jakarta.validation.Valid;



@RestController
public class DemoController {
    @Autowired
    private DemoService demoService;
    // private Object demoRepository;

        @GetMapping("/hello")
        public String hello() {
            return "Hello Ivan!";
        }

        @GetMapping("/model")
        public ResponseEntity<DemoModel> getModel(
                @RequestParam int id,
                @RequestParam(required = false, defaultValue = "Test") String name) {
            DemoModel model = new DemoModel(id, name);
            return new ResponseEntity<>(model, HttpStatus.OK);
        }

        @PostMapping("/model/register")
        // 泛型內放問號指可以傳入任何型別
        public ResponseEntity<?> register(
            @Valid
            @RequestBody Map<String, Object> jsonBody,
            BindingResult result) {
            if (result.hasErrors()) {
                return new ResponseEntity<>("Validation errors: " + result.getAllErrors(), HttpStatus.BAD_REQUEST);
            }
            // Save the model to the database
            String name = (String) jsonBody.get("name");
            if (name == null) {
                throw new RuntimeException("name cannot be null");
            }
            DemoModel savedModel = demoService.createUserByName(name);
            return new ResponseEntity<>(savedModel, HttpStatus.CREATED);
        }

        @GetMapping("/model/{name}")
        public DemoModel getUserByName(@PathVariable String name) {
            DemoModel demoModel = this.demoService.getUserByName(name);
            if (demoModel == null) {
                throw new RuntimeException("demoModel cannot be null");
            }
            return demoService.getUserByName(name);
        }

        @ExceptionHandler(Exception.class)
        public String handleException(Exception e, Model model) {
            return String.format("error: %s", e.getMessage());
        }

        // @GetMapping("/model/{name}")
        // public ResponseEntity<DemoModel> getUserByName(@PathVariable String name) {
        //     DemoModel demoModel = this.demoService.getUserByName(name);
        //     return new ResponseEntity<>(demoModel, HttpStatus.OK);
        // }

        // @GetMapping("/model/{name}")
        // public DemoModel createUserByName(String name) {
        //     DemoModel model = new DemoModel();
        //     model.setName(name);
        //     return this.demoRepository.save(model);
        // }
}