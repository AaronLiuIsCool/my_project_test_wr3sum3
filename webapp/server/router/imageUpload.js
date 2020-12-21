const express = require("express");
const router = express.Router();
const S3_BUCKET_NAME = "kuaidao-img";
const S3_BUCKET_REGION = "ca-central-1";

var aws = require("aws-sdk");

aws.config.update({
  accessKeyId: process.env.AWS_ACCESS_KEY_ID,
  secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
  region: S3_BUCKET_REGION
});

const s3 = new aws.S3();

router.post("/upload", function (req, res) {
  const { fileName, base64, fileType } = req.body;
  const buf = new Buffer(
    base64.replace(/^data:image\/\w+;base64,/, ""),
    "base64"
  );

  const params = {
    Bucket: S3_BUCKET_NAME,
    Key: fileName,
    ACL: "public-read",
    Body: buf,
    ContentEncoding: "base64",
    ContentType: fileType,
  };

  s3.putObject(params, function (err) {
    if (err) {
      return res.status(400).json({ status: "failed", error: err });
    } else {

      const response = {
        success: true,
        url: `http://${S3_BUCKET_NAME}.s3.${S3_BUCKET_REGION}.amazonaws.com/${fileName}`,
      };
      return res.status(200).json(response);
    }
  });
});

module.exports = router;
