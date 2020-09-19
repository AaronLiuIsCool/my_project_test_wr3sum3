exports = function() {
  
    const jobsCollection = context.services.get("<REPLACE_WITH_TARGET_CLUSTER_NAME>").db("kuaidaoresume-matching").collection("jobs");

    const expiryInSecs = 30 * 24 * 60 * 60; //30 days   

    const query =  {"createdAt" : {$lte: new Date(Date.now() - expiryInSecs * 1000)}};

    const action = {
      "$set": {
        "isActive": false,
      }
    };
    const options = { "upsert": false };

    jobsCollection.updateMany(query, action, options)
      .then(result => {
        const { matchedCount, modifiedCount } = result;
        if(matchedCount && modifiedCount) {
          console.log(`Successfully updated jobs.`);
        }
      })
      .catch(err => console.error(`Failed to update the job: ${err}`));
      
};
