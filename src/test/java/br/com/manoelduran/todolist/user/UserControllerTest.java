package br.com.manoelduran.todolist.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    @Autowired
    private UserController userController;

    @Mock
    @Autowired
    private IUserRepository userRepository;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCreateUser_Success() {
        // Arrange

        UserModel userModel = new UserModel();

        userModel.setUsername("testUser");
        userModel.setName("Test User");
        userModel.setPassword("password123");
        // var ok =
        // when(userRepository.findByUsername(userModel.getUsername())).thenReturn(userModel);
        System.out.println(userModel);
        // Act
        ResponseEntity response = userController.create(userModel);
        System.out.println(response);
        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(201);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().equals(UserModel.class));
        assertThat(response.getBody()).isInstanceOf(UserModel.class);
    }
}
