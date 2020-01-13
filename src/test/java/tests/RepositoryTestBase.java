package tests;

import dto.Base;
import org.junit.After;
import org.junit.Before;
import repositories.Repository;

public abstract class RepositoryTestBase<DTOType extends Base, TRepository extends Repository<DTOType>> {

    TRepository repository;

    @Before
    public void before() {
        repository = Create();
        if (repository != null) {
            repository.beginTransaction();
        }
    }

    @After
    public void after() {
        if (repository != null) {
            repository.rollbackTransaction();
            //repository.commitTransaction();
        }
    }

    protected abstract TRepository Create();
}
