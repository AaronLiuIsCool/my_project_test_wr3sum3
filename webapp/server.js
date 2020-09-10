require('dotenv').config();

const express = require('express');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const compression = require('compression');
// const helmet = require('helmet');
// const cors = require('cors');
const path = require('path');

const translateRouter = require('./router/translate')

// Setup default port
const PORT = process.env.PORT || 80;
// Create express app
const app = express();

// Implement middleware
// app.use(cors());
// app.use(helmet());
app.use(compression());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(bodyParser.json());

app.use(express.static(path.join(__dirname, 'build')));
app.use('/v1/translate', translateRouter);
app.get('/*', function (req, res) {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});

// Implement route for errors
app.use((err, req, res, next) => {
  console.error(err.stack)
  res.status(500).send('Something broke!');
})

// Start express app
app.listen(PORT, function () {
  console.log(`Server is running on: ${PORT}`);
})
