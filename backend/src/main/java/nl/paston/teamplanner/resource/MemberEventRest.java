package nl.paston.teamplanner.resource;

import javax.ws.rs.Path;
import javax.ws.rs.core.UriBuilder;

import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;

import nl.paston.teamplanner.model.MemberEvent;

@Path("member-events")
public class MemberEventRest extends AbstractRest<MemberEvent> {

    @Override
    MemberEvent findById(Long id) {
        return MemberEvent.findById(id);
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
        return Search.session(em).search(MemberEvent.class).predicate(f -> f.matchAll()).toQuery();
    }

}
