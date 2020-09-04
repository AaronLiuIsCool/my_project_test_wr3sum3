package com.kuaidaoresume.matching.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "resumes")
@CompoundIndexes({
    @CompoundIndex(def = "{'location.country' : 1, 'location.city': 1, 'majors': 1}"),
    @CompoundIndex(def = "{'location.country' : 1, 'location.city': 1, 'keywords': 1}"),
    @CompoundIndex(def = "{'location.country' : 1, 'location.city': 1, 'majors': 1, 'keywords': 1}"),
})
public class Resume {

    @Id
    private String id;

    @NotNull
    @Indexed(unique=true)
    private String resumeUuid;

    @NotNull
    @Indexed
    private String userId;

    private String alias;

    @NotNull
    private Location location;

    private List<String> majors;

    private Collection<String> keywords;
}
