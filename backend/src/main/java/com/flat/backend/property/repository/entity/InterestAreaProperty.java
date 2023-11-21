package com.flat.backend.property.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flat.backend.user.repository.entity.User;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class InterestAreaProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    @JoinColumn(name = "property_id")
    @ManyToOne
    @JsonIgnore
    private Property property;
}
