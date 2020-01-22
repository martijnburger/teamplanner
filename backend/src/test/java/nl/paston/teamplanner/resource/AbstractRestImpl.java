package nl.paston.teamplanner.resource;

import javax.persistence.EntityManager;
import javax.ws.rs.core.UriBuilder;

import org.hibernate.search.engine.search.query.SearchQuery;

public class AbstractRestImpl extends AbstractRest<TestEntity> {

    public AbstractRestImpl(EntityManager em) {
        super(em);
    }

    @Override
    TestEntity findById(Long id) {
        return null;
    }

    @Override
    UriBuilder getUri() {
        return null;
    }

    @Override
    String getTableName() {
        return null;
    }

    @Override
    SearchQuery<TestEntity> getSearchQuery(String simpleQueryString) {
        return null;
    }


    
}