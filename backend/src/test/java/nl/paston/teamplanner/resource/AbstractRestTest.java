package nl.paston.teamplanner.resource;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class AbstractRestTest {

    EntityManager em = mock(EntityManager.class);
    AbstractRestImpl ari = new AbstractRestImpl(em);

    @Test
    public void create_ok() {
        TestEntity entity = new TestEntity();
        // when(em.persist(entity)).doAnswer(invocation -> {
        //     Object[] args = invocation.getArguments();
        //     ((TestEntity) args[0]).id = 1L;
        //     return null;
        // });
    }

}