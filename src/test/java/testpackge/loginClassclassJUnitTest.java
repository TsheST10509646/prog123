/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package testpackge;
import com.mycompany.poeloginandres.loginClassclass;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author tshep
 */
public class loginClassclassJUnitTest {
    
    @Test
    public void testCorrectUsernameMessage() {
       
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
                
        );

        user.registerUser();
        user.setLoginDetails("kyl_1", "Ch&&sec@ke99!");

        assertEquals(
                "Welcome Kyle, Smith it is great to see you.",
                user.returnLoginStatus()
        );
    }

    @Test
    public void testIncorrectUsernameMessage() {
        loginClassclass user = new loginClassclass(
                "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"
        );

        assertEquals(
                "Username is not correctly formatted; please ensure that "
                + "your username contains an underscore and is no more "
                + "than five characters in length.",
                user.registerUser()
        );
    }

    @Test
    public void testCorrectPasswordMessage() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertEquals(
                "Password successfully captured.",
                user.checkPasswordComplexity()
        );
    }

    @Test
    public void testIncorrectPasswordMessage() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "password", "+27838968976"
        );

        assertEquals(
                "Password is not correctly formatted; please ensure that "
                + "the password contains at least eight characters, "
                + "a capital letter, a number, and a special character.",
                user.registerUser()
        );
    }

    @Test
    public void testCorrectCellPhoneMessage() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertEquals(
                "Cell number successfully captured.",
                user.checkCellPhoneNumber()
        );
    }

    @Test
    public void testIncorrectCellPhoneMessage() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "08966553"
        );

        assertEquals(
                "Cell number is incorrectly formatted or does not contain "
                + "an international code; please correct the number "
                + "and try again.",
                user.registerUser()
        );
    }

    // BOOLEAN TESTS: assertTrue and assertFalse

    @Test
    public void testLoginSuccessful() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        user.registerUser();
        user.setLoginDetails("kyl_1", "Ch&&sec@ke99!");

        assertTrue(user.loginUser());
    }

    @Test
    public void testLoginFailed() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        user.registerUser();
        user.setLoginDetails("kyl_1", "wrongPassword");

        assertFalse(user.loginUser());
    }

    @Test
    public void testUsernameCorrectlyFormatted() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertTrue(user.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        loginClassclass user = new loginClassclass(
                "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"
        );

        assertFalse(user.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexity() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertTrue(user.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "password", "+27838968976"
        );

        assertFalse(user.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "+27838968976"
        );

        assertTrue(user.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        loginClassclass user = new loginClassclass(
                "kyl_1", "Ch&&sec@ke99!", "08966553"
        );

        assertFalse(user.checkCellPhoneNumber());
    }
}
    


    


    

