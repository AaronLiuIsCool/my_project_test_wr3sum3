db.createView(
  "resume_matched_jobs_view",
  "resume_matched_jobs",
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
      localField: "matchedJobs",
      foreignField: "_id",
      as: "matchedJobs"
    }
  }, {
    $project: {
      _id: 1,
      resume: {
        _id: 1,
        resumeId: 1,
        userId: 1
      },
      matchedJobs: {
        $filter: {
          input: "$matchedJobs",
          as: "matchedJob",
          cond: {$eq: ["$$matchedJob.isActive", true]}
        }
      }
    }
  }]
);
