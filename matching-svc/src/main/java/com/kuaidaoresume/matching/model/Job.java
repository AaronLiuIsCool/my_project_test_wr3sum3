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
    @CompoundIndex(def = "{'location.country' : 1, 'location.city': 1, 'keywords': 1, 'postDate': -1}")
})
public class Job {

    @Id
    private String id;

    @NotNull
    @Indexed(unique=true)
    private String jobUuid;

    @NotNull
    @TextIndexed(weight=35)
    private String title;

    @NotNull
    @TextIndexed(weight=25)
    private String companyName;

    private Location location;

    private String jobType;

    private String employmentType;

    private Compensation compensation;

    private String url;

    @TextIndexed(weight=20)
    private Collection<String> relevantMajors;

    @TextIndexed(weight=20)
    private Collection<Keyword> keywords;

    @NotNull
    @Indexed(direction = IndexDirection.DESCENDING)
    private LocalDateTime postDate;

    @NotNull
    private Instant createdAt;

    @NotNull
    private boolean isActive;

}
