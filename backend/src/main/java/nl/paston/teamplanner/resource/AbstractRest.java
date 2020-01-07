package nl.paston.teamplanner.resource;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.util.common.SearchException;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import nl.paston.teamplanner.model.Views;

@Slf4j
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class AbstractRest<T extends PanacheEntity> {

    public static final int defaultPageSize = 20;
    public static final String defaultSortBy = "id";

    abstract T findById(Long id);

    abstract UriBuilder getUri();

    abstract String getTableName();

    abstract SearchQuery<T> getSearchQuery(String simpleQueryString);

    @Inject
    EntityManager em;

    @Inject
    ObjectMapper mapper;

    @Transactional
    void onStart(@Observes StartupEvent ev) throws InterruptedException {
        Search.session(em).massIndexer().startAndWait();
    }

    @POST
    @Transactional
    public Response create(final T entity) {
        entity.id = null;
        em.persist(entity);
        final URI uri = getUri().path("{id}").build(entity.id);
        return Response.created(uri).type(MediaType.TEXT_PLAIN).build();
    }

    @GET
    public Response readAll(@QueryParam("search") String search, @QueryParam("pageSize") int pageSize,
            @QueryParam("pageNumber") int pageNumber) {
        pageSize = pageSize > 0 ? pageSize : defaultPageSize;
        pageNumber = pageNumber > 0 ? pageNumber : 1;
        if (search == null)
            search = "";
        try {
            // Use the search, Luke!
            List<T> list = getSearchQuery(search).fetchHits((pageNumber - 1) * pageSize, pageSize);
            long count = getSearchQuery(search).fetchTotalHitCount();
            int pageCount = (Math.toIntExact(count) + pageSize + 1) / pageSize;
            return createEntitiesResponse(list, search, pageSize, pageNumber, count, pageCount);
        } catch (SearchException ex) {
            log.info("Method readAll threw a SearchException", ex);
            return Response.status(Status.CONFLICT).type(MediaType.TEXT_PLAIN).entity("Search pattern not accepted.").build();
        } catch (IOException ex) {
            log.error("Method readTreeWithView cannot map!", ex);
            return Response.status(Status.SERVICE_UNAVAILABLE).type(MediaType.TEXT_PLAIN).entity("Please contact the administrator.").build();
        }
    }

    @GET
    @Path("{id}")
    public Response read(@PathParam("id") final Long id) {
        final T entity = findById(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") final Long id, final T entity) {
        T managed = findById(id);
        if (managed == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        managed = entity;
        managed.persist();
        return Response.ok().build();
    }

    @PATCH
    @Path("{id}")
    @Transactional
    public Response modify(@PathParam("id") final Long id, final T entity) {
        T managed = findById(id);
        if (managed == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        // Itterate over fields
        managed = entity;
        managed.persist();
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") final Long id) {
        final T managed = findById(id);
        if (managed == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        managed.delete();
        return Response.noContent().build();
    }

    private String buildUrlString(final String pattern, final int pageSize, final int pageNumber) {
        return getUri().queryParam("pattern", pattern).queryParam("pageSize", pageSize)
                .queryParam("pageNumber", pageNumber).build().toString();
    }

    // This is a workaround for issue
    // https://github.com/FasterXML/jackson-databind/issues/1687
    private JsonNode readTreeWithView(final Class<?> serializationView, final Object value) throws IOException {
        return mapper.readTree(mapper.writerWithView(serializationView).writeValueAsString(value));
    }

    public Response createEntitiesResponse(List<?> list, String pattern, int pageSize, int pageNumber, long count,
            int pageCount) throws IOException {
        final ObjectNode json = mapper.createObjectNode();
        json.set("items", readTreeWithView(Views.Public.class, list));
        json.put("count", count);
        json.put("pageSize", pageSize);
        json.put("pageCount", pageCount);
        if (pageNumber > 1) {
            json.put("previousPage", buildUrlString(pattern, pageSize, pageNumber - 1));
        }
        if (pageNumber < pageCount) {
            json.put("nextPage", buildUrlString(pattern, pageSize, pageNumber + 1));
        }
        return Response.ok(json).build();
    }

}