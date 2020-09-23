/************************************************************************************************
 *
 * Make sure this is in sync with src/main/java/com/kuaidaoresume/common/auth/AuthConstant.java
 *
 ************************************************************************************************/

// AUTHORIZATION_HEADER is the http request header
// key used for accessing the internal authorization.
const AUTHORIZATION_HEADER = "Authorization";
// AUTHORIZATION_ANONYMOUS_WEB is set as the Authorization header to denote that
// a request is being made bu an unauthenticated web user
const AUTHORIZATION_ANONYMOUS_WEB = "kdr-anonymous";
// AUTHORIZATION_SUPPORT_USER is set as the Authorization header to denote that
// a request is being made by a kuaidaoresume team member
const AUTHORIZATION_SUPPORT_USER = "kdr-support";
// AUTHORIZATION_SUPERPOWERS_SERVICE is set as the Authorization header to
// denote that a request is being made by the dev-only superpowers service
const AUTHORIZATION_SUPERPOWERS_SERVICE = "superpowers-service";
// AUTHORIZATION_AUTHENTICATED_USER is set as the Authorization header to denote that
// a request is being made by an authenticated web user
const AUTHORIZATION_AUTHENTICATED_USER = "kdr-authenticated";

module.exports = {
  AUTHORIZATION_HEADER,
  AUTHORIZATION_ANONYMOUS_WEB,
  AUTHORIZATION_SUPPORT_USER,
  AUTHORIZATION_SUPERPOWERS_SERVICE,
  AUTHORIZATION_AUTHENTICATED_USER
};
