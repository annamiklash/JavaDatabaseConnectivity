package mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<DTO> {

    DTO map(ResultSet resultSet) throws SQLException;

}
