/*
 * Copy from http://www.fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM.ScriptTable
 */

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LoginDialogDriver {

    private final String username, password;
    private String message;
    private int loginAttempts;

    public LoginDialogDriver(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean loginWithUsernameAndPassword(String userName, String password) {
        ++loginAttempts;
        boolean result = this.username.equals(userName) && this.password.equals(password);
        message = String.format(result ? "%s logged in." : "%s not logged in.", this.username);
        return result;
    }

    public String loginMessage() {
        return message;
    }

    public int numberOfLoginAttempts() {
        return loginAttempts;
    }
}
