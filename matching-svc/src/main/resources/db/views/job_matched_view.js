db.createView(
  "job_matched_view",
  "job_matched",
  [{
    $lookup: {
      from: "jobs",
      localField: "job",
      foreignField: "_id",
      as: "job"
    }
  }, {
    $lookup: {
      from: "resumes",
      localField: "matchedByResumes",
      foreignField: "_id",
      as: "matchedByResumes"
    }
  }, {
    $project: {
      _id: 1,
      job: 1,
      matchedByResumes: {
        _id: 1,
        resumeId: 1,
        userId: 1
      }
    }
  }]
);
