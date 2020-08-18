db.createView(
  "job_visited_view",
  "job_visited",
  [{
    $lookup: {
      from: "jobs",
      localField: "job",
      foreignField: "_id",
      as: "job"
    }
  }, {
    $project: {
      _id: 1,
      job: 1,
      visitedByUsers: 1
    }
  }]
);
