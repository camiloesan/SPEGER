package mx.uv.fei.dao;

import mx.uv.fei.logic.AccessAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;

import static org.junit.jupiter.api.Assertions.*;

class AccessAccountDAOTest {
    @BeforeEach
    void setUp() throws SQLException {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy");
        accessAccount.setUserType("profesor");
        accessAccountDAO.addAdminAccessAccount(accessAccount);
        var accessAccount2 = new AccessAccount();
        accessAccount2.setUsername("dummy2");
        accessAccount2.setUserPassword("dummy2");
        accessAccount2.setUserType("profesor");
        accessAccountDAO.addAdminAccessAccount(accessAccount2);
    }

    @AfterEach
    void tearDown() throws SQLException {
        var accessAccountDAO = new AccessAccountDAO();
        accessAccountDAO.deleteAccessAccountByUsername("dummy");
        accessAccountDAO.deleteAccessAccountByUsername("dummy2");
    }
    @Test
    void testAddAccessAccountSucces() throws SQLException {
        AccessAccount accessAccount = new AccessAccount();
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();

        accessAccount.setUsername("BDMG");
        accessAccount.setUserPassword("contrasenaBDMG");
        accessAccount.setUserType("Estudiante");

        accessAccountDAO.addAdminAccessAccount(accessAccount);
    }
    @Test
    void testAddAccessAccountWrongUserType() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy");
        accessAccount.setUserType("car");
        assertThrows(SQLTransientConnectionException.class, () -> accessAccountDAO.addAdminAccessAccount(accessAccount));
    }

    @Test
    void testAddAccessAccountUserAlreadyExists() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy");
        accessAccount.setUserType("profesor");
        assertThrows(SQLException.class, () -> accessAccountDAO.addAdminAccessAccount(accessAccount));
    }

    @Test
    void testAddAccessAccountUsernameTooLong() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount();
        accessAccount.setUsername("1234567890123456");
        accessAccount.setUserPassword("12345");
        accessAccount.setUserType("administrador");
        assertThrows(SQLException.class, () -> accessAccountDAO.addAdminAccessAccount(accessAccount));
    }

    @Test
    void testAddAccessAccountPasswordTooLong() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("123456789012345678");
        accessAccount.setUserType("administrador");
        assertThrows(SQLException.class, () -> accessAccountDAO.addAdminAccessAccount(accessAccount));
    }

    @Test
    void testAreCredentialsValid() {
        var accessAccountDAO = new AccessAccountDAO();
        assertDoesNotThrow(() -> accessAccountDAO.areCredentialsValid("dummy", "dummy"));
    }
}