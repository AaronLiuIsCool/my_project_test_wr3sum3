const express = require('express')
const AWS = require('aws-sdk');
const { Translate } = require('@google-cloud/translate').v2;

const router = express.Router();

const awsTranslate = new AWS.Translate({
  accessKeyId: process.env.AWS_ACCESS_KEY_ID,
  secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
  region: process.env.region,
});

router.post('/aws', (req, res) => {
  const { text, from, to } = req.body;
  const params = {
    SourceLanguageCode: from,
    TargetLanguageCode: to,
    Text: text,
  };

  awsTranslate.translateText(params, (err, data) => {
    if (err) {
      return res.send(err);
    }

    res.json(data);
  });
});

const googleTranslate = new Translate();

router.post('/gcp', (req, res) => {
  const { text, from, to } = req.body;
  googleTranslate.translate(text, to).then((response) => {
    let [translations] = response;
    translations = Array.isArray(translations) ? translations : [translations];
    return res.json(translations);
  }, (error) => {
    return res.send(error);
  });
});


module.exports = router;
