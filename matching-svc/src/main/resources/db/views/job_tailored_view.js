db.createView(
  "job_tailored_view",
  "job_tailored",
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
      localField: "tailoredByResumes",
      foreignField: "_id",
      as: "tailoredByResumes"
    }
  }, {
    $project: {
      _id: 1,
      job: 1,
      tailoredByResumes: {
        _id: 1,
        resumeId: 1,
        userId: 1
      }
    }
  }]
);
