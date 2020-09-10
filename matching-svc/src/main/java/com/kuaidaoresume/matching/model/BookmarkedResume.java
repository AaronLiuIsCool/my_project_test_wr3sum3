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
@Document(collection = "bookmarked_resumes")
public class BookmarkedResume {

    @Id
    private String id;

    @NotNull
    @Indexed(unique=true)
    private String resumeUuid;

    @NotNull
    @DBRef
    private Collection<Job> bookmarkedJobs;
}
