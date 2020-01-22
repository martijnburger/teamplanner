package nl.paston.teamplanner.resource;

import java.net.URI;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import nl.paston.teamplanner.model.Event;
import nl.paston.teamplanner.model.MemberEvent;

@Path("events")
public class EventRest extends AbstractRest<Event> {

    @Inject
    public EventRest(EntityManager em) {
        super(em);
    }

    @Override
    Event findById(Long id) {
        return Event.findById(id);
    }

    @Override
    UriBuilder getUri() {
        return UriBuilder.fromResource(EventRest.class);
    }

    @Override
    String getTableName() {
        return "Event";
    }

    @Override
    SearchQuery<Event> getSearchQuery(String simpleQueryString) {
        if (simpleQueryString == null || "".equals(simpleQueryString.trim())) {
            return Search.session(em).search(Event.class).predicate(SearchPredicateFactory::matchAll).toQuery();
        }
        return Search.session(em).search(Event.class).predicate(f -> f.simpleQueryString().field("name").matching(simpleQueryString)).toQuery();
    }

    @GET
    @Path("{id}/members")
    public Response findMemberEventsById(@PathParam("id") final Long id) {
        String hql = "SELECT me FROM MemberEvent me WHERE me.event.id = " + id + "ORDER BY me.id";
        PanacheQuery<MemberEvent> entities = MemberEvent.find(hql);
        return Response.ok(entities.list()).build();
    }

    @POST
    @Path("{id}/members")
    public Response createMemberEventById(@PathParam("id") final Long id, final MemberEvent memberEvent) {
        memberEvent.id = null;
        memberEvent.event.id = id;
        em.persist(memberEvent);
        final URI uri = getUri().path("member-events/{id}").build(memberEvent.id);
        return Response.created(uri).type(MediaType.TEXT_PLAIN).build();

    }

}