package com.f4d4.server.rulers;

import com.f4d4.general.facility.*;
import com.f4d4.server.managers.SocketServer;
import com.f4d4.server.tools.PasswordHashing;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;

import static com.f4d4.server.managers.SocketServer.log;

public class DatabaseRuler {
    private Connection connection;
    private String DB_HOST = "pg";
    private String DB_NAME = "studs";
    private String DB_USER = "postgres";
    private String DB_PASSWORD = "KosPrav1979";
    private String URL = "jdbc:postgresql://" + DB_HOST + "/" + DB_NAME;
    private final String ADD_TICKET =
            "insert into ticket (name , x_coordinate , y_coordinate , price , discount , ticket_type ," +
                    " creation_date , event_name, minage , event_type , user_id , login) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?);";

    private final String LAST_TICKET = "SELECT * FROM ticket ORDER BY id DESC LIMIT 1;";

    private final String UPDATE_TICKET = "update ticket set name = ?, x_coordinate = ? , y_coordinate = ? , price  = ?, discount  = ?," +
            " ticket_type  = ?, event_name = ?, minage = ? , event_type = ? where id = ? ;";

    private final String CHECK_IF_USERID_IS_CORRECT = "select user_id from ticket where id=?;";

    private final String GET_USER_ID = "select id from users where login = ?;";
    private final String INSERT_USER = "insert into users(login , password_hash , salt) values(?,?,?);";

    private final String LOAD_DB = " select * from ticket;";

    private final String CHECK_USER = "select * from users where login = ?;";
    private final String CHECK_USER_SALT = "select salt from users where login = ?;";

    private final String REMOVE_FIRST_TICKET = "DELETE FROM ticket WHERE id = (SELECT MIN(id) FROM ticket);";
    private final String REMOVE_BY_ID = "delete from ticket where id = ?;";
    private final String CLEAR = "delete from ticket where user_id = ?;";

    public void connect() {
        try {
            connection = DriverManager.getConnection(URL);
            log.info("Подключение к базе данных успешно");
        } catch (SQLException e) {
            log.info("Ошибка подключения к базе данных");
            System.exit(1);
        }
    }


    public PriorityQueue<Ticket> loadCollection() {
        PriorityQueue<Ticket> result = new PriorityQueue<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(LOAD_DB);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int x = resultSet.getInt("x_coordinate");
                double y = resultSet.getDouble("y_coordinate");
                long price = resultSet.getLong("price");
                int discount = resultSet.getInt("discount");
                TicketType type = TicketType.valueOf(resultSet.getString("ticket_type"));
                String creationDate = resultSet.getString("creation_date");
                long eventID = resultSet.getLong("event_id");
                String eventName = resultSet.getString("event_name");
                long minAge = resultSet.getLong("minage");
                EventType eventType = EventType.valueOf(resultSet.getString("event_type"));
                int userId = resultSet.getInt("user_id");
                String login = resultSet.getString("login");
                result.add(new Ticket(id, name, new Coordinates(x, y), price, discount, type, creationDate,
                        new Event(eventID, eventName, minAge, eventType), userId, login));
            }
        } catch (SQLException e) {
            log.warn("Не удалось полностью загрузить коллекцию");
        }
        return result;
    }

    public Ticket getLastTicket() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(LAST_TICKET);
        if (resultSet.next()) {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            int x = resultSet.getInt("x_coordinate");
            double y = resultSet.getDouble("y_coordinate");
            long price = resultSet.getLong("price");
            int discount = resultSet.getInt("discount");
            TicketType type = TicketType.valueOf(resultSet.getString("ticket_type"));
            String creationDate = resultSet.getString("creation_date");
            long eventID = resultSet.getLong("event_id");
            String eventName = resultSet.getString("event_name");
            long minAge = resultSet.getLong("minage");
            EventType eventType = EventType.valueOf(resultSet.getString("event_type"));
            int userId = resultSet.getInt("user_id");
            String login = resultSet.getString("login");
            return new Ticket(id, name, new Coordinates(x, y), price, discount, type, creationDate,
                    new Event(eventID, eventName, minAge, eventType), userId, login);
        }
        return null;
    }

    public void insertUser(String login, String password, String salt) throws SQLException {
        PreparedStatement addUser = connection.prepareStatement(INSERT_USER);
        addUser.setString(1, login);
        addUser.setString(2, password);
        addUser.setString(3, salt);
        addUser.executeUpdate();
    }

    public boolean checkUser(String login, String password) throws SQLException {
        PreparedStatement checkStatement = connection.prepareStatement(CHECK_USER);
        checkStatement.setString(1, login);
        ResultSet resultSetUser = checkStatement.executeQuery();
        if (resultSetUser.next()) {
            var passwordHash = resultSetUser.getString("password_hash");
            var salt = resultSetUser.getString("salt");
            var hashPasswordWithSalt = PasswordHashing.hashPasswordWithSalt(password, salt);
            var result = passwordHash.equals(hashPasswordWithSalt);
            return result;
        } else {
            return false;
        }

    }

    public int getUserID(String login) throws SQLException {
        PreparedStatement checkStatement = connection.prepareStatement(GET_USER_ID);
        checkStatement.setString(1, login);
        ResultSet resultSet = checkStatement.executeQuery();
        if (resultSet.next()) {
            var result = resultSet.getInt("id");
            return result;
        } else {
            return -1;
        }
    }

    public void insertTicket(Ticket ticket, String login) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_TICKET);
        String name = ticket.getName();
        int x = ticket.getXcoordinate();
        double y = ticket.getYcoordinate();
        long price = ticket.getPrice();
        int discount = ticket.getDiscount();
        String ticketType = String.valueOf(ticket.getTicketType());
        String crationDate = ticket.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME);
        String eventName = ticket.getEventName();
        long minAge = ticket.getEventMinage();
        String eventType = String.valueOf(ticket.getEventType());
        int userId = ticket.getUser_id();
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, x);
        preparedStatement.setDouble(3, y);
        preparedStatement.setLong(4, price);
        preparedStatement.setInt(5, discount);
        preparedStatement.setString(6, ticketType);
        preparedStatement.setString(7, crationDate);
        preparedStatement.setString(8, eventName);
        preparedStatement.setLong(9, minAge);
        preparedStatement.setString(10, eventType);
        preparedStatement.setInt(11, userId);
        preparedStatement.setString(12, login);
        preparedStatement.executeUpdate();
    }

    public void updateTicket(Ticket ticket, long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TICKET);
        String name = ticket.getName();
        int x = ticket.getXcoordinate();
        double y = ticket.getYcoordinate();
        long price = ticket.getPrice();
        int discount = ticket.getDiscount();
        String ticketType = String.valueOf(ticket.getTicketType());
        String eventName = ticket.getEventName();
        long minAge = ticket.getEventMinage();
        String eventType = String.valueOf(ticket.getEventType());
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, x);
        preparedStatement.setDouble(3, y);
        preparedStatement.setLong(4, price);
        preparedStatement.setInt(5, discount);
        preparedStatement.setString(6, ticketType);
        preparedStatement.setString(7, eventName);
        preparedStatement.setLong(8, minAge);
        preparedStatement.setString(9, eventType);
        preparedStatement.setLong(10, id);
        preparedStatement.executeUpdate();
    }

    public void removeTicketById(long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_BY_ID);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    public int isCorrectID(long id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_IF_USERID_IS_CORRECT);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("user_id");
        } else {
            return -1;
        }
    }

    public void removeFirstTicket() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(REMOVE_FIRST_TICKET);
    }

    public void clear(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(CLEAR);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }


}
