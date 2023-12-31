require("dotenv").config();

const express = require("express");
const bodyParser = require("body-parser");
const cookieParser = require("cookie-parser");
const compression = require("compression");
const helmet = require("helmet");
const path = require("path");

const translateRouter = require('./server/router/translate');
const resumeSDKRouter = require("./server/router/resumeSDK");
const imageUploadRoutes = require("./server/router/imageUpload");
const { authCheck } = require('./server/auth');

// Setup default port
const PORT = process.env.PORT || 80;
// Create express app
const app = express();

// Implement middleware
app.use(helmet.frameguard());
app.use(helmet.noSniff());
app.use(helmet.xssFilter());

app.use(compression());

app.use(cookieParser());

app.use(bodyParser.json({ limit: '20mb' }));
app.use(bodyParser.urlencoded({ limit: '20mb', extended: true }));

app.use(express.static(path.join(__dirname, "build")));
app.use('/v1/translate', authCheck, translateRouter);
app.use("/v1/resumeSDK", authCheck, resumeSDKRouter);
app.use("/v1/image", authCheck, imageUploadRoutes);

app.get('/*', function (req, res) {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});

// Implement route for errors
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send("Something broke!");
});

// Start express app
app.listen(PORT, function () {
  console.log(`Server is running on: ${PORT}`);
});
