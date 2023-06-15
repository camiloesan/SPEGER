package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IUser;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a set of functions involving manipulation and authentication of users using the database
 */
public class UserDAO implements IUser {
    private static final Logger logger = Logger.getLogger(UserDAO.class);

    /**
     * @param accessAccount admin access account
     * @return rows affected (1 or 0) if the admin user was added or not.
     * @throws SQLException if the user couldn't be added or there was a problem connecting to the database.
     */
    @Override
    public int addAdminUser(AccessAccount accessAccount) throws SQLException {
        String query = "insert into CuentasAcceso(nombreUsuario, contrasena, correoInstitucional, tipoUsuario) values (?,SHA2(?, 256),?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, accessAccount.getUsername());
        preparedStatement.setString(2, accessAccount.getUserPassword());
        preparedStatement.setString(3, accessAccount.getUserEmail());
        preparedStatement.setString(4, "Administrador");

        int result;
        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();
        return result;
    }

    /**
     * @param accessAccount new accessAccount to add
     * @param student new student to add
     * @return successful transaction
     * @throws SQLException if the transaction was not successful or was a problem connecting to the database.
     */
    @Override
    public boolean addStudentUserTransaction(AccessAccount accessAccount, Student student) throws SQLException {
        String firstQuery = "insert into CuentasAcceso(nombreUsuario, contrasena, correoInstitucional, tipoUsuario) " +
                "values (?,SHA2(?, 256),?,?)";
        String secondQuery = "insert into Estudiantes(matricula, nombre, apellidos, " +
                "nombreUsuario) values (?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        int resultFirstQuery = 0;
        int resultSecondQuery = 0;

        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUsername());
            firstPreparedStatement.setString(2, accessAccount.getUserPassword());
            firstPreparedStatement.setString(3, accessAccount.getUserEmail());
            firstPreparedStatement.setString(4, "Estudiante");
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, student.getStudentID());
            secondPreparedStatement.setString(2, student.getName());
            secondPreparedStatement.setString(3, student.getLastName());
            secondPreparedStatement.setString(4, accessAccount.getUsername());
            resultFirstQuery = firstPreparedStatement.executeUpdate();
            resultSecondQuery = secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            try {
                connection.rollback();
                logger.error(sqlException);
            } catch (SQLException sqlException1) {
                logger.fatal(sqlException1);
            }
        } finally {
            databaseManager.closeConnection();
        }
        return resultFirstQuery > 0 && resultSecondQuery > 0;
    }

    /**
     * @param accessAccount new accessAccount to add
     * @param professor new professor to add
     * @return successful transaction
     * @throws SQLException if the transaction was not successful or was a problem connecting to the database.
     */
    @Override
    public boolean addProfessorUserTransaction(AccessAccount accessAccount, Professor professor) throws SQLException {
        String firstQuery = "insert into CuentasAcceso(nombreUsuario, contrasena, correoInstitucional, tipoUsuario) " +
                "values (?,SHA2(?, 256), ?, ?)";
        String secondQuery = "insert into Profesores(nombre, apellidos, grado, nombreUsuario) " +
                "values (?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        int resultFirstQuery = 0;
        int resultSecondQuery = 0;
        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUsername());
            firstPreparedStatement.setString(2, accessAccount.getUserPassword());
            firstPreparedStatement.setString(3, accessAccount.getUserEmail());
            firstPreparedStatement.setString(4, accessAccount.getUserType());
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, professor.getProfessorName());
            secondPreparedStatement.setString(2, professor.getProfessorLastName());
            secondPreparedStatement.setString(3, professor.getProfessorDegree());
            secondPreparedStatement.setString(4, accessAccount.getUsername());
            resultFirstQuery = firstPreparedStatement.executeUpdate();
            resultSecondQuery = secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            connection.rollback();
            logger.error(sqlException);
        } finally {
            databaseManager.closeConnection();
        }
        return resultFirstQuery > 0 && resultSecondQuery > 0;
    }

    /**
     * @param username username to modify(primary key)
     * @param accessAccount new accessAccount
     * @param student new student user
     * @return successful transaction
     * @throws SQLException if the transaction was not successful or was a problem connecting to the database.
     */
    @Override
    public boolean modifyStudentUserTransaction(String username, AccessAccount accessAccount, Student student)
            throws SQLException {
        String firstQuery = "update CuentasAcceso set contrasena=(SHA2(?, 256)), correoInstitucional=(?) ,tipoUsuario=(?) " +
                "where nombreUsuario=(?) and tipoUsuario!=(?)";
        String secondQuery = "update Estudiantes set matricula=(?), nombre=(?), apellidos=(?) " +
                "where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        int resultFirstQuery = 0;
        int resultSecondQuery = 0;
        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUserPassword());
            firstPreparedStatement.setString(2, accessAccount.getUserEmail());
            firstPreparedStatement.setString(3, accessAccount.getUserType());
            firstPreparedStatement.setString(4, username);
            firstPreparedStatement.setString(5, "Administrador");
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, student.getStudentID());
            secondPreparedStatement.setString(2, student.getName());
            secondPreparedStatement.setString(3, student.getLastName());
            secondPreparedStatement.setString(4, username);
            resultFirstQuery = firstPreparedStatement.executeUpdate();
            resultSecondQuery = secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            connection.rollback();
            logger.error(sqlException);
        } finally {
            databaseManager.closeConnection();
        }
        return resultFirstQuery > 0 && resultSecondQuery > 0;
    }

    /**
     * @param username username to modify
     * @param accessAccount new accessAccount data
     * @param professor new professor data
     * @return successful transaction
     * @throws SQLException if the transaction was not successful or was a problem connecting to the database.
     */
    @Override
    public boolean modifyProfessorUserTransaction(String username, AccessAccount accessAccount, Professor professor)
            throws SQLException {
        String firstQuery = "update CuentasAcceso set contrasena=(SHA2(?, 256)), correoInstitucional = (?) ,tipoUsuario=(?) " +
                "where nombreUsuario=(?)";
        String secondQuery = "update Profesores set nombre=(?), apellidos=(?), grado=(?) " +
                "where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        int resultFirstQuery = 0;
        int resultSecondQuery = 0;
        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUserPassword());
            firstPreparedStatement.setString(2, accessAccount.getUserEmail());
            firstPreparedStatement.setString(3, accessAccount.getUserType());
            firstPreparedStatement.setString(4, username);
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, professor.getProfessorName());
            secondPreparedStatement.setString(2, professor.getProfessorLastName());
            secondPreparedStatement.setString(3, professor.getProfessorDegree());
            secondPreparedStatement.setString(4, username);
            resultFirstQuery = firstPreparedStatement.executeUpdate();
            resultSecondQuery = secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            connection.rollback();
            logger.error(sqlException);
        } finally {
            databaseManager.closeConnection();
        }
        return resultFirstQuery > 0 && resultSecondQuery > 0;
    }

    /**
     * @param username username to delete
     * @return rows affected
     * @throws SQLException if there was a problem connecting to the database.
     */
    @Override
    public int deleteUserByUsername(String username) throws SQLException {
        String query = "delete from CuentasAcceso where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        int result;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param username username to validate
     * @param password password to validate username with
     * @return username matches password
     * @throws SQLException if there was a problem connecting to the database.
     */
    @Override
    public boolean areCredentialsValid(String username, String password) throws SQLException {
        boolean isValid;
        String query = "select 1 from CuentasAcceso where nombreUsuario=(?) and contrasena=(SHA2(?, 256))";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        isValid = resultSet.next();
        databaseManager.closeConnection();

        return isValid;
    }

    /**
     * @param username username for searching usertype
     * @return user type of the provided username
     * @throws SQLException if there was a problem connecting to the database or getting the data from a column.
     */
    @Override
    public String getAccessAccountTypeByUsername(String username) throws SQLException {
        String query = "select tipoUsuario from CuentasAcceso where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        String type = "null";
        while (resultSet.next()) {
            type = resultSet.getString("tipoUsuario");
        }

        return type;
    }

    /**
     * @return return a list with ALL the access accounts in the database
     * @throws SQLException if there was a problem connecting to the database or getting the data from a column.
     */
    @Override
    public List<AccessAccount> getAccessAccountsList() throws SQLException {
        String query = "select ID_usuario, nombreUsuario, tipoUsuario, correoInstitucional from CuentasAcceso";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<AccessAccount> accessAccountList = new ArrayList<>();
        while(resultSet.next()) {
            AccessAccount accessAccount = new AccessAccount();
            accessAccount.setUserId(resultSet.getInt("ID_usuario"));
            accessAccount.setUsername(resultSet.getString("nombreUsuario"));
            accessAccount.setUserType(resultSet.getString("tipoUsuario"));
            accessAccount.setUserEmail(resultSet.getString("correoInstitucional"));
            accessAccountList.add(accessAccount);
        }

        return accessAccountList;
    }

    /**
     * @param accessAccount access account to add to database.
     * @return rows affected (1 or 0) if the user was added or not.
     * @throws SQLException if the user couldn't be added or there was a problem connecting to the database.
     */
    @Override
    public int addUser(AccessAccount accessAccount) throws SQLException {
        String query = "insert into CuentasAcceso(nombreUsuario, contrasena, correoInstitucional, tipoUsuario) values (?,SHA2(?, 256),?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, accessAccount.getUsername());
        preparedStatement.setString(2, accessAccount.getUserPassword());
        preparedStatement.setString(3, accessAccount.getUserEmail());
        preparedStatement.setString(4, accessAccount.getUserType());

        int result;
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public boolean isUserTaken(String username) throws SQLException {
        String query = "select 1 from CuentasAcceso where nombreUsuario = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        return resultSet.next();
    }

    @Override
    public boolean isEmailTaken(String email) throws SQLException {
        String query = "select 1 from CuentasAcceso where correoInstitucional = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        return resultSet.next();
    }
    
    /**
     * @param username professor username to get all the account details
     * @return Professor with account information
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public Professor getProfessorAccount(String username) throws SQLException {
        String sqlQuery = "SELECT CA.correoInstitucional, P.nombre, P.apellidos, P.grado FROM Profesores P INNER JOIN " +
                "CuentasAcceso CA on P.nombreUsuario = CA.nombreUsuario WHERE P.nombreUsuario = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        Professor professorAccount = new Professor();
        if (resultSet.next()){
            professorAccount.setEmail(resultSet.getString("correoInstitucional"));
            professorAccount.setProfessorName(resultSet.getString("nombre"));
            professorAccount.setProfessorLastName(resultSet.getString("apellidos"));
            professorAccount.setProfessorDegree(resultSet.getString("grado"));
        }
        return professorAccount;
    }
    
    /**
     * @param username student username to get all the account details
     * @return Student with account information
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public Student getStudentAccount(String username) throws SQLException {
        String sqlQuery = "SELECT CA.correoInstitucional, E.matricula, E.nombre, E.apellidos FROM Estudiantes E INNER JOIN" +
                " CuentasAcceso CA on E.nombreUsuario = CA.nombreUsuario WHERE E.nombreUsuario = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        Student studentAccount = new Student();
        if (resultSet.next()) {
            studentAccount.setEmail(resultSet.getString("correoInstitucional"));
            studentAccount.setStudentID(resultSet.getString("matricula"));
            studentAccount.setName(resultSet.getString("nombre"));
            studentAccount.setLastName(resultSet.getString("apellidos"));
        }
        return studentAccount;
    }
}
