package nl.paston.teamplanner.resource;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.UriBuilder;

import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;

import nl.paston.teamplanner.model.MemberEvent;

@Path("member-events")
public class MemberEventRest extends AbstractRest<MemberEvent> {

    @Inject
    public MemberEventRest(EntityManager em) {
        super(em);
    }

    @Override
    MemberEvent findById(Long id) {
        return em.find(MemberEvent.class, id);
    }

    @Override
    UriBuilder getUri() {
        return UriBuilder.fromResource(MemberEventRest.class);
    }

    @Override
    String getTableName() {
        return "MemberEvent";
    }

    @Override
    SearchQuery<MemberEvent> getSearchQuery(String simpleQueryString) {
        return Search.session(em).search(MemberEvent.class).where(f -> f.matchAll()).toQuery();
    }

}
