const express = require('express')
const router = express.Router();
const axios = require('axios');

async function resumeSDK(res, data) {
  const axios = require('axios')
  const url = "http://www.resumesdk.com/api/parse";
  const base64 = data.base64;
  const fileName = data.fileName;

  const args = { uid: process.env.RESUMESDK_UID, pwd: process.env.RESUMESDK_PWD, file_name: fileName, file_cont: base64 }; // 构建参数

  try {
    const resp = await axios.post(url, args);
    return res.status(200).json(resp.data);
  } catch (err) {
    // Handle Error Here
    console.error(err);
  }
}

// Setup the POST route to upload a file
router.post("/upload", function (req, res) {
  resumeSDK(res, req.body);
});

module.exports = router;