package nl.paston.teamplanner.resource;

import javax.ws.rs.core.UriBuilder;

import nl.paston.teamplanner.model.MemberEvent;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MemberEventRestTest {

    EntityManager em;
    MemberEventRest mer = new MemberEventRest(em);

    @Test
    public void getUriTest_ok() {
        assertEquals(mer.getUri().build().getPath(), "member-events");
    }
    
    @Test
    public void getTableNameTest_ok() {
        assertEquals(mer.getTableName(), "MemberEvent");
    }

    @Test
    public void findByIdTest_ok() {
        MemberEvent testMemberEvent = new MemberEvent();
        testMemberEvent.name = "foo";
        em = mock(EntityManager.class);
        mer = new MemberEventRest(em);
        when(em.find(MemberEvent.class, 1L)).thenReturn(testMemberEvent);
        assertEquals(mer.findById(1L).name, "foo"); 
    }
    
}