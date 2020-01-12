import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementFiller {

    void fillStatementParameters(PreparedStatement preparedStatement) throws SQLException;
}
