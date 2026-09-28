package nl.paston.teamplanner.resource;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.util.common.SearchException;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class AbstractRest<T extends PanacheEntity> {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String DEFAULT_SORT_BY = "id";

    abstract T findById(Long id);

    abstract UriBuilder getUri();

    abstract String getTableName();

    abstract SearchQuery<T> getSearchQuery(String simpleQueryString);

    abstract Class<T> getEntityClass();

    /** Copies the fields a client may change with PUT or PATCH from {@code source} to {@code target}. */
    abstract void copyFields(T source, T target);

    protected EntityManager em;

    @Inject
    ObjectMapper mapper;

    public AbstractRest(EntityManager em) {
        this.em = em;
    }

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
        pageSize = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
        pageNumber = pageNumber > 0 ? pageNumber : 1;
        if (search == null)
            search = "";
        try {
            // Use the search, Luke!
            List<T> list = getSearchQuery(search).fetchHits((pageNumber - 1) * pageSize, pageSize);
            long count = getSearchQuery(search).fetchTotalHitCount();
            int pageCount = pageCount(count, pageSize);
            return createEntitiesResponse(list, search, pageSize, pageNumber, count, pageCount);
        } catch (SearchException ex) {
            Log.info("Method readAll threw a SearchException", ex);
            return Response.status(Status.CONFLICT).type(MediaType.TEXT_PLAIN).entity("Search pattern not accepted.").build();
        } catch (Exception ex) {
            Log.error("Method readTreeWithView cannot map!", ex);
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
        final T managed = findById(id);
        if (managed == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        copyFields(entity, managed);
        // Serialize inside the transaction, lazy associations cannot be loaded after it
        return Response.ok(mapper.valueToTree(managed)).build();
    }

    @PATCH
    @Path("{id}")
    @Transactional
    public Response modify(@PathParam("id") final Long id, final ObjectNode changes) {
        final T managed = findById(id);
        if (managed == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            // Apply the changes to a detached copy, then copy only the changeable fields back
            final T copy = mapper.treeToValue(mapper.valueToTree(managed), getEntityClass());
            final T patched = mapper.readerForUpdating(copy).readValue(changes);
            copyFields(patched, managed);
        } catch (IOException | IllegalArgumentException ex) {
            return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build();
        }
        return Response.ok(mapper.valueToTree(managed)).build();
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

    static int pageCount(final long count, final int pageSize) {
        return Math.toIntExact((count + pageSize - 1) / pageSize);
    }

    private String buildUrlString(final String search, final int pageSize, final int pageNumber) {
        return getUri().queryParam("search", search).queryParam("pageSize", pageSize)
                .queryParam("pageNumber", pageNumber).build().toString();
    }

    public Response createEntitiesResponse(List<?> list, String search, int pageSize, int pageNumber, long count,
            int pageCount) {
        final ObjectNode json = mapper.createObjectNode();
        json.set("items", mapper.valueToTree(list));
        json.put("count", count);
        json.put("pageSize", pageSize);
        json.put("pageCount", pageCount);
        if (pageNumber > 1) {
            json.put("previousPage", buildUrlString(search, pageSize, pageNumber - 1));
        }
        if (pageNumber < pageCount) {
            json.put("nextPage", buildUrlString(search, pageSize, pageNumber + 1));
        }
        return Response.ok(json).build();
    }

}