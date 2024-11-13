package com.example.Advanced_server_Ostrogotskaya.userServiceTests;

import com.example.Advanced_server_Ostrogotskaya.ConstantTest;
import com.example.Advanced_server_Ostrogotskaya.dto.request.PutUserRequest;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PublicUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.PutUserResponse;
import com.example.Advanced_server_Ostrogotskaya.dto.response.common.BaseSuccessResponse;
import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import com.example.Advanced_server_Ostrogotskaya.errors.CustomException;
import com.example.Advanced_server_Ostrogotskaya.mapper.UserMapper;
import com.example.Advanced_server_Ostrogotskaya.repositories.AuthRepository;
import com.example.Advanced_server_Ostrogotskaya.repositories.UserRepository;
import com.example.Advanced_server_Ostrogotskaya.security.CustomUserDetails;
import com.example.Advanced_server_Ostrogotskaya.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTests extends ConstantTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserEntity user;

    @Test
    @DisplayName("getting all users")
    void getAllUsers() {
        UserEntity user1 = new UserEntity();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@example.com");
        user1.setName("User One");
        user1.setAvatar("avatar1.png");
        user1.setRole("USER");
        UserEntity user2 = new UserEntity();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@example.com");
        user2.setName("User Two");
        user2.setAvatar("avatar2.png");
        user2.setRole("USER");

        List<UserEntity> userEntities = Arrays.asList(user1, user2);
        PublicUserResponse response1 = new PublicUserResponse();
        response1.setId(user1.getId());
        response1.setEmail(user1.getEmail());
        response1.setName(user1.getName());
        response1.setAvatar(user1.getAvatar());
        response1.setRole(user1.getRole());

        PublicUserResponse response2 = new PublicUserResponse();
        response2.setId(user2.getId());
        response2.setEmail(user2.getEmail());
        response2.setName(user2.getName());
        response2.setAvatar(user2.getAvatar());
        response2.setRole(user2.getRole());

        List<PublicUserResponse> publicUserResponses = Arrays.asList(response1, response2);
        when(authRepository.findAll()).thenReturn(userEntities);
        when(userMapper.toPublicUserResponseList(userEntities)).thenReturn(publicUserResponses);
        List<PublicUserResponse> response = userService.getAllUsers();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(publicUserResponses.size(), response.size());
        Assertions.assertEquals(publicUserResponses, response);
    }

    @Test
    @DisplayName("Getting User Info By Id")
    void getUserInfoById() {
        PublicUserResponse expectedResponse = publicUserResponse(user);
        when(authRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toPublicUser(user)).thenReturn(expectedResponse);
        PublicUserResponse response = userService.getUserInfoById(user.getId());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedResponse, response);
    }

    @BeforeEach
    public void setup() {
        UUID authenticatedUserId = UUID.randomUUID();
        user = createUser(authenticatedUserId);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication mockAuthentication = mock(Authentication.class);
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockAuthentication.getPrincipal()).thenReturn(userDetails);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);
    }

    @Test
    @DisplayName("User not found")
    void getUserInfoById_UserNotFound() {
        when(authRepository.findById(user.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.getUserInfoById(user.getId()));
    }

    @Test
    @DisplayName("Getting user info")
    void getUserInfo() {
        PublicUserResponse expectedResponse = publicUserResponse(user);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        userDetails.getUsername();
        SecurityContextHolder.getContext().setAuthentication(new
                UsernamePasswordAuthenticationToken(userDetails, null));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toPublicUser(user)).thenReturn(expectedResponse);
        PublicUserResponse response = userService.getUserInfo();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("User not found if")
    void getUserInfo_UserNotFoundIf() {
        when(authRepository.findById(user.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.getUserInfoById(user.getId()));
    }

    @Test
    @DisplayName("Replace user")
    void replaceUser() {
        PutUserRequest putUserRequest = new PutUserRequest();
        PutUserResponse expectedResponse = new PutUserResponse();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toPutUser(user)).thenReturn(expectedResponse);
        PutUserResponse response = userService.replaceUser(putUserRequest);
        verify(userRepository).findById(user.getId());
        verify(userMapper).updateUserFromRequest(putUserRequest, user);
        verify(userRepository).save(user);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("User not found")
    void replaceUser_UserNotFound() {
        CustomUserDetails userDetails = new CustomUserDetails(user);
        userDetails.getUsername();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomException.class, () -> {
            userService.replaceUser(new PutUserRequest());
        });
    }

    @Test
    @DisplayName("uuoi")
    void updateUser() {
        when(userRepository.findById(user.getId()))
            .thenReturn(Optional.of(user));
        BaseSuccessResponse response = userService.deleteUser();
        verify(userRepository)
                .findById(user.getId());
        verify(userRepository)
                .delete(user);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Delete user not found")
    void deleteUser_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomException.class, () -> {
            userService.deleteUser();
        });
    }

    private PublicUserResponse publicUserResponse(UserEntity user) {
        PublicUserResponse publicUserResponse = new PublicUserResponse();
        publicUserResponse.setId(user.getId());
        publicUserResponse.setEmail(user.getEmail());
        publicUserResponse.setName(user.getName());
        publicUserResponse.setAvatar(user.getAvatar());
        publicUserResponse.setRole(user.getRole());
        return publicUserResponse;
    }

    private UserEntity createUser(UUID userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setAvatar(AVATAR);
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setRole(ROLE);
        return user;
    }
}
