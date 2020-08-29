const express = require('express');

// Import middleware
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const compression = require('compression');
// const helmet = require('helmet');
const path = require('path');

// Setup default port
const PORT = process.env.PORT || 80;
// Create express app
const app = express();

// Implement middleware
// app.use(helmet());
app.use(compression());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(bodyParser.json());

console.log(path.join(__dirname, 'build'));
app.use(express.static(path.join(__dirname, 'build')));
app.get('/*', function (req, res) {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});

const router = express.Router();
router.get('');

module.exports = router;

// Implement route for errors
app.use((err, req, res, next) => {
  console.error(err.stack)
  res.status(500).send('Something broke!');
})
// Start express app
app.listen(PORT, function () {
  console.log(`Server is running on: ${PORT}`);
})
