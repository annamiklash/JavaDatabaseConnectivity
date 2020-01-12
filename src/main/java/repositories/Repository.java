package repositories;

import dto.Base;

import java.sql.Connection;

public interface Repository<DTOType extends Base> {

    Connection getConnection();

    void add(DTOType dto);

    void update(DTOType dto);

    void addOrUpdate(DTOType dto);

    void delete(DTOType dto);

    DTOType findById(int id);

    void beginTransaction();

    void commitTransaction();

    void rollbackTransaction();

    int getCount();

    boolean exists(DTOType dto);
}
