package com.kuaidaoresume.matching.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "jobs")
@CompoundIndexes({
    @CompoundIndex(def = "{'location.country' : 1, 'location.city': 1, 'postDate': -1}"),
    @CompoundIndex(def = "{'location.country' : 1, 'location.city': 1, 'relevantMajors': 1, 'postDate': -1}"),
    @CompoundIndex(def = "{'location.country' : 1, 'location.city': 1, 'keywords': 1, 'postDate': -1}"),
    @CompoundIndex(def = "{'location.country' : 1, 'location.city': 1, 'relevantMajors': 1, 'keywords': 1, 'postDate': -1}")
})
public class Job {

    @Id
    private String id;

    @NotNull
    @Indexed(unique=true)
    private String jobUuid;

    @NotNull
    @TextIndexed(weight=40)
    private String title;

    @NotNull
    @TextIndexed(weight=30)
    private String companyName;

    private Location location;

    private String jobType;

    private String employmentType;

    private Compensation compensation;

    @TextIndexed(weight=30)
    private Collection<String> relevantMajors;

    @NotNull
    private LocalDateTime postDate;

    @NotNull
    @Indexed(direction = IndexDirection.DESCENDING)
    private Instant createdAt;

    @NotNull
    private boolean isActive;

    private Collection<String> keywords;
}
