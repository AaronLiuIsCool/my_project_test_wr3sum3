const express = require('express')
const router = express.Router();
const multer = require('multer')

// Create an upload instance and receive a single file
const upload = multer({ storage: multer.memoryStorage() }).single('file')

async function resumeSDK(res, fileObj){
  const axios = require('axios')
  const url = "http://www.resumesdk.com/api/parse";
  const base64str = fileObj.buffer.toString('base64'); // 转成base64字符串

  const args = { uid: 2009090, pwd: '7CX50B', file_name: fileObj.originalname, file_cont: base64str }; // 构建参数

  try {
    const resp = await axios.post(url, args);
    return res.status(200).json(resp.data);
  } catch (err) {
      // Handle Error Here
      console.error(err);
  }
}

// Setup thePOSTroute to upload a file
router.post("/upload", function (req, res) {
  upload(req, res, async function (err) {
    if (err instanceof multer.MulterError) {
      return res.status(500).json(err);
    } else if (err) {
      return res.status(500).json(err);
    }
    resumeSDK(res, req.file);
  });
});

module.exports = router;