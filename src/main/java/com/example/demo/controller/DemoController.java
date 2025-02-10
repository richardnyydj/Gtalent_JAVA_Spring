package com.example.demo.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.DemoModel;
import com.example.demo.model.DemoModelRequestEntity;
import com.example.demo.service.DemoService;

import jakarta.validation.Valid;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

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
    public ResponseEntity<?> register(
        @Valid @ModelAttribute DemoModelRequestEntity modelBody,
        BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>("Validation errors: " + result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        String name = modelBody.getName();
        if (name == null) {
            throw new RuntimeException("name can't be null");
        }
        System.out.println(name);
        // Save the model to the database
        DemoModel savedModel = demoService.createUserByName(name);
        return new ResponseEntity<>(savedModel, HttpStatus.CREATED);
    }

    @PostMapping("/model/check-username")
    public Map<String, Boolean> checkUsername(@RequestParam String username) {
        boolean isAvailable = !demoService.checkUserName(username.toLowerCase());
        return Map.of("isAvailable", isAvailable);
    }


    @GetMapping("/model/{name}")
    public ResponseEntity<DemoModel> getUserByName(@PathVariable String name) {

        // 有值狀態下回傳，沒有值的話拋出例外
        return this.demoService.getUserByName(name)
                .map(model -> new ResponseEntity<>(model, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("demoModel can't be null for name: " + name));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);

            return new ResponseEntity<>("Successfully uploaded - " + file.getOriginalFilename(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload " + file.getOriginalFilename(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String fileName) {
        try {
            // Define the file path
            Path filePath = Paths.get("uploads/").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the resource is accessible
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        return String.format("error: %s", e.getMessage());
    }
}
