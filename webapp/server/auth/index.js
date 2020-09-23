const {
  AUTHORIZATION_HEADER,
  AUTHORIZATION_AUTHENTICATED_USER,
  AUTHORIZATION_SUPPORT_USER,
  AUTHORIZATION_SUPERPOWERS_SERVICE
} = require('./constants');

const ALLOWED_AUTH_HEADERS = [
  AUTHORIZATION_AUTHENTICATED_USER,
  AUTHORIZATION_SUPPORT_USER,
  AUTHORIZATION_SUPERPOWERS_SERVICE
];

function authCheck (req, res, next) {
  const requestAuthHeader = req.headers[AUTHORIZATION_HEADER];

  if (!requestAuthHeader || !ALLOWED_AUTH_HEADERS.some(authHeader => authHeader === requestAuthHeader)) {
    return res.json({
      success: false,
      message: "not authorized"
    });
  }

  next();
}

module.exports = {authCheck};
