package com.octopus.rest.query;

import com.octopus.rest.query.repository.entity.SearchableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "test_table")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestTableEntity implements SearchableEntity {

    @Id
    private String id;

    private String someField;

    private boolean active;

}
