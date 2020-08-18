db.createView(
  "resume_tailored_jobs_view",
  "resume_tailored_jobs",
  [{
    $lookup: {
      from: "resumes",
      localField: "resume",
      foreignField: "_id",
      as: "resume"
    }
  }, {
    $lookup: {
      from: "jobs",
      localField: "tailoredToJob",
      foreignField: "_id",
      as: "tailoredToJob"
    }
  }, {
    $project: {
      _id: 1,
      resume: {
        _id: 1,
        resumeId: 1,
        userId: 1
      },
      tailoredToJob: 1
    }
  }]
);
