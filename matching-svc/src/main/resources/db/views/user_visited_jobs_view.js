db.createView(
  "user_visited_jobs_view",
  "user_visited_jobs",
  [{
    $lookup: {
      from: "jobs",
      localField: "visitedJobs",
      foreignField: "_id",
      as: "visitedJobs"
    }
  }, {
    $project: {
      _id: 1,
      userId: 1,
      visitedJobs: 1
    }
  }]
);
