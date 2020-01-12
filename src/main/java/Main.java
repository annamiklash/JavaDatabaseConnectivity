import dto.User;
import mappers.Mapper;
import mappers.impl.UserIdMapper;
import mappers.impl.UsersMapper;

import java.sql.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        final User user = new User(null, "maksin", "pacin");
        Integer newUserId = execute("INSERT INTO public.users(login, password) VALUES (?, ?) RETURNING ID",
                preparedStatement -> {
                    preparedStatement.setString(1, user.getLogin());
                    preparedStatement.setString(2, user.getPassword());
                }, new UserIdMapper());

        System.out.println("Saved user id = " + newUserId);

        List<User> userList =  execute("select id, login, password FROM public.users", preparedStatement -> {}, new UsersMapper());

        System.out.println("Users: " + userList.toString());

    }

    public static <DTO> DTO execute(String sql, PreparedStatementFiller preparedStatementFiller, Mapper<DTO> mapper) {
        try {
            Class.forName("org.postgresql.Driver");
            final Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/utp_10", "koconok", "koconok");

            final PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatementFiller.fillStatementParameters(preparedStatement);

            return mapper.map(preparedStatement.executeQuery());
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        return null;
    }
}
