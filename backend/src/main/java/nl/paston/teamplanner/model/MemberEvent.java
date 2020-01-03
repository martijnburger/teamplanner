package nl.paston.teamplanner.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@JsonInclude(Include.NON_NULL)
public class MemberEvent extends PanacheEntity {

    @GenericField
    public String name;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "event_id")
    @JsonBackReference
    public Event event;

    @Enumerated(EnumType.STRING)
    @GenericField
    public Availablity available;

    @Enumerated(EnumType.STRING)
    @GenericField
    public Plannability planned;

    @GenericField
    public String comment;

    public enum Availablity { AVAILABLE, NOT_AVAILABLE, WAITING }
    
    public enum Plannability { ACCEPTED, REJECTED, SKIPPED, WAITING }

}