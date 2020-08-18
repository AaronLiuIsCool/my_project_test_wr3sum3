db.createView(
  "user_bookmarked_jobs_view",
  "user_bookmarked_jobs",
  [{
    $lookup: {
      from: "jobs",
      localField: "bookmarkedJobs",
      foreignField: "_id",
      as: "bookmarkedJobs"
    }
  }, {
    $project: {
      _id: 1,
      userId: 1,
      bookmarkedJobs: 1
    }
  }]
);
