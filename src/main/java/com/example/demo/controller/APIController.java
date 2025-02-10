package com.example.demo.controller;

import java.util.stream.Collectors;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.DemoModel;
import com.example.demo.model.Post;
import com.example.demo.model.UserDTO;
import com.example.demo.model.PostDTO;
import com.example.demo.model.UserModelRequestEntity;
import com.example.demo.service.DemoService;
import com.example.demo.service.PostService;
import com.example.demo.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Tag(name = "Authentication", description = "Operations for authentication")
@RequestMapping("/api")
public class APIController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PostService postService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setDemoService(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Swagger!";
    }

    @Operation(summary = "Get user model by ID", description = "Get user model with posts by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user model with posts successfully"),
            @ApiResponse(responseCode = "404", description = "User ID not found") // bad request
    })
    @GetMapping("/users/{id:\\d+}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserDTO getUserWithPostsByID(@PathVariable int id,
                                        @RequestParam(name = "posts", required = false, defaultValue = "false") boolean includePosts) {
        UserDTO userdto = null;
        if (includePosts == true) {
            userdto = this.usersService.findUserWithPosts(id);
        } else {
            userdto = this.usersService.getUserByID(id);
        }
        if(userdto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the id can't be found");
        }
        return userdto;
    }

    @Operation(summary = "Get user model by Name", description = "Get user model by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user model successfully"),
    })
    @GetMapping("/users/{name:[a-zA-Z]+}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Map<String, Boolean> getUserByName(@PathVariable String name) {
        boolean isSuccess = this.usersService.findUserByUsername(name);
        return Map.of("success", isSuccess);
    }

    @PatchMapping("/admin/users/{id:\\d+}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<String, Boolean> setUserNameByID(@PathVariable int id, @RequestParam String name) {
        boolean isSuccess = this.demoService.updateUserName(id, name);
        return Map.of("success", isSuccess);
    }

    @PatchMapping("/admin/users/{name:[a-zA-Z]+}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<String, Boolean> setUserPasswordByUsername(@PathVariable String name, @RequestParam String password) {
        boolean isSuccess = this.usersService.updatePassword(name, password);
        return Map.of("success", isSuccess);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @ModelAttribute UserModelRequestEntity modelBody,
                                    BindingResult result,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        if (result != null && result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                                            .stream()
                                            .map(ObjectError::getDefaultMessage)
                                            .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(Map.of("errors", errorMessages));
        }

        String username = modelBody.getUsername();
        String password = modelBody.getPassword();
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username can't be null or empty"));
        }

        try {
            // 驗證用戶
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 獲取當前會話並使其失效
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 清理 SecurityContext
        SecurityContextHolder.clearContext();

        // 刪除 Cookie（如果需要）
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 立即失效
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody PostDTO postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }
}