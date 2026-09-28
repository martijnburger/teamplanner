package nl.paston.teamplanner.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@JsonInclude(Include.NON_NULL)
@Indexed
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