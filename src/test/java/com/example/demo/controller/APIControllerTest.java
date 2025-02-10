package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.component.JwtTokenUtil;
import com.example.demo.model.DemoModel;
import com.example.demo.model.UserModelRequestEntity;
import com.example.demo.service.DemoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Collection;
import java.util.Map;

@WebMvcTest(APIController.class)
//@WithMockUser(username = "ivan", roles = {"USER"})
class APIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DemoService demoService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private APIController apiController;

    private String token;
    private String admin_token;

    @BeforeEach
    void setup() {
        // 初始化必要的 MockBean
        // 生成測試用 JWT Token
        token = jwtTokenUtil.generateToken("ivan", List.of("ROLE_USER"));
        admin_token = jwtTokenUtil.generateToken("admin", List.of("ROLE_USER", "ROLE_ADMIN"));
    }

    @Test
    @WithMockUser(username = "ivan", roles = {"USER"})
    void testSayHello() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk()) // status code 200
                .andExpect(content().string("Hello, Swagger!"));
    }

    @Test
    @WithMockUser(username = "ivan", roles = {"USER"})
    void testGetUserByID() throws Exception {
        DemoModel demoModel = new DemoModel(); // 假設 DemoModel 有無參數建構函式
        demoModel.setId(1);
        demoModel.setName("ivan");

        Mockito.when(demoService.getUserByID(1)).thenReturn(demoModel);

        mockMvc.perform(get("/api/users/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)) // 添加 JWT Token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("ivan"));
    }

    @Test
    @WithMockUser(username = "ivan", roles = {"USER"})
    void testGetUserByID_NotFound() throws Exception {
        Mockito.when(demoService.getUserByID(100)).thenReturn(null);

        mockMvc.perform(get("/api/users/100")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSetUserNameByID() throws Exception {
        // Create a custom AuthenticationManager
        AuthenticationManager authenticationManager = authentication -> {
            if ("admin".equals(authentication.getName()) && "password".equals(authentication.getCredentials())) {
                return new UsernamePasswordAuthenticationToken(
                    "admin",
                    "password",
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        };

        // Mock the DemoService behavior
        Mockito.when(demoService.updateUserName(1, "NewName")).thenReturn(true);

        // Initialize the controller with the mocked AuthenticationManager
        APIController apiController = new APIController();
        apiController.setAuthenticationManager(authenticationManager);
        apiController.setDemoService(demoService);

        Map<String, Boolean> response = apiController.setUserNameByID(1, "NewName");
        assertNotNull(response);
        assertTrue(response.get("success"));
        Mockito.verify(demoService).updateUserName(1, "NewName");
    }

    @Test
    void testLogin_Success() throws Exception {
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication realAuthentication = new UsernamePasswordAuthenticationToken("ivan", "password", authorities);

        AuthenticationManager authenticationManager = auth -> {
            if ("ivan".equals(auth.getPrincipal()) && "password".equals(auth.getCredentials())) {
                return realAuthentication;
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        };

        APIController apiController = new APIController();
        apiController.setAuthenticationManager(authenticationManager);

        // 模擬 UserModelRequestEntity
        UserModelRequestEntity user = new UserModelRequestEntity();
        user.setUsername("ivan");
        user.setPassword("password");

        // 測試控制器方法
        ResponseEntity<?> response = apiController.login(user, null, null, null);

        // 驗證返回值
        assertEquals(HttpStatus.OK, response.getStatusCode()); // 確認狀態碼是 200 OK
        assertTrue(response.getBody() instanceof Map); // 確認返回的 body 是 Map
        assertEquals("Login successful", ((Map<?, ?>) response.getBody()).get("message")); // 確認返回消息
    }

    @Test
    void testLogin_InvalidInput() throws Exception {
        // Create a real UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken("ivan", "password");

        // Mock AuthenticationManager to return the token
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(token);

        UserModelRequestEntity user = new UserModelRequestEntity();
        user.setPassword("wrongpassword");
        ResponseEntity<?> response = apiController.login(user, null, null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Username can't be null or empty"));
    }

    @Test
    void testLogin_Unauthorized() throws Exception {
        // Create a mock Authentication object
        // Mock AuthenticationManager to throw an exception for invalid credentials
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
            .thenThrow(new RuntimeException("Invalid credentials"));

        UserModelRequestEntity user = new UserModelRequestEntity();
        user.setUsername("ivan");
        user.setPassword("wrongpassword");
        ResponseEntity<?> response = apiController.login(user, null, null, null);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testLogout() throws Exception {
        // 模擬 session 行為
        Mockito.when(request.getSession(false)).thenReturn(session);

        // 調用控制器的 logout 方法
        ResponseEntity<?> responseEntity = apiController.logout(request, response);

        // 驗證回應
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Logout successful", ((Map<?, ?>) responseEntity.getBody()).get("message"));

        // 驗證 session 的失效
        Mockito.verify(session).invalidate(); // 確保 session 被調用失效
    }
}
