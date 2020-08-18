db.createView(
  "job_bookmarked_view",
  "job_bookmarked",
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
      bookmarkedByUsers: 1
    }
  }]
);
