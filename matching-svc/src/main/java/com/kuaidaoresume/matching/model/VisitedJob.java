package com.kuaidaoresume.matching.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "visited_jobs")
public class VisitedJob {
    @Id
    private String id;

    @NotNull
    @Indexed(unique=true)
    private String jobUuid;

    @NotNull
    @DBRef
    private Collection<Resume> visitedResumes;
}
