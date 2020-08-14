package com.kuaidaoresume.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kuaidaoresume.job.model.Job;
import com.kuaidaoresume.job.model.Location;
import com.kuaidaoresume.job.model.Major;

import java.util.List;
import java.util.Optional;


@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

// TODO:List<Job> findJobsByKeywords(List<String> Keywords);

        List<Job> findByLocationIn(List<Location> locations);

        List<Job> findByMajorsIn(List<Major> majors);

        Optional<Job> findByUrl(String Url);
}
