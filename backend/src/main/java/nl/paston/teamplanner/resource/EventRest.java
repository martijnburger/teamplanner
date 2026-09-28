package nl.paston.teamplanner.resource;

import java.net.URI;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;

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
    Class<Event> getEntityClass() {
        return Event.class;
    }

    @Override
    void copyFields(Event source, Event target) {
        // Members are managed through their own endpoints
        target.name = source.name;
        target.date = source.date;
        target.planned = source.planned;
    }

    @Override
    SearchQuery<Event> getSearchQuery(String simpleQueryString) {
        if (simpleQueryString == null || "".equals(simpleQueryString.trim())) {
            return Search.session(em).search(Event.class).where(f -> f.matchAll()).toQuery();
        }
        return Search.session(em).search(Event.class).where(f -> f.simpleQueryString().field("name").matching(simpleQueryString)).toQuery();
    }

    @GET
    @Path("{id}/members")
    public Response findMemberEventsById(@PathParam("id") final Long id) {
        PanacheQuery<MemberEvent> entities = MemberEvent.find("event.id = ?1 order by id", id);
        return Response.ok(entities.list()).build();
    }

    @POST
    @Path("{id}/members")
    @Transactional
    public Response createMemberEventById(@PathParam("id") final Long id, final MemberEvent memberEvent) {
        final Event event = findById(id);
        if (event == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        memberEvent.id = null;
        memberEvent.event = event;
        event.members.add(memberEvent);
        em.persist(memberEvent);
        final URI uri = UriBuilder.fromResource(MemberEventRest.class).path("{id}").build(memberEvent.id);
        return Response.created(uri).type(MediaType.TEXT_PLAIN).build();
    }

}