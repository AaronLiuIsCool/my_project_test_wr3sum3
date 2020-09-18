package com.kuaidaoresume.common.auth;

/**
 * Auth constant
 *
 * @author Aaron Liu
 */
public class AuthConstant {

    public static final String COOKIE_NAME = "kuaidaoresume-kdr";
    // header set for internal user id
    public static final String CURRENT_USER_HEADER = "kdr-current-user-id";
    // AUTHORIZATION_HEADER is the http request header
    // key used for accessing the internal authorization.
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // AUTHORIZATION_ANONYMOUS_WEB is set as the Authorization header to denote that
    // a request is being made bu an unauthenticated web user
    public static final String AUTHORIZATION_ANONYMOUS_WEB = "kdr-anonymous";
    // AUTHORIZATION_BOT_SERVICE is set as the Authorization header to denote that
    // a request is being made by the bot microservice
    public static final String AUTHORIZATION_BOT_SERVICE = "bot-service";
    // AUTHORIZATION_ACCOUNT_SERVICE is set as the Authorization header to denote that
    // a request is being made by the account service
    public static final String AUTHORIZATION_ACCOUNT_SERVICE = "account-service";
    // AUTHORIZATION_SUPPORT_USER is set as the Authorization header to denote that
    // a request is being made by a kuaidaoresume team member
    public static final String AUTHORIZATION_SUPPORT_USER = "kdr-support";
    // AUTHORIZATION_SUPERPOWERS_SERVICE is set as the Authorization header to
    // denote that a request is being made by the dev-only superpowers service
    public static final String AUTHORIZATION_SUPERPOWERS_SERVICE = "superpowers-service";
    // AUTHORIZATION_WWW_SERVICE is set as the Authorization header to denote that
    // a request is being made by the www login / signup system
    public static final String AUTHORIZATION_WWW_SERVICE = "www-service";
    // AUTH_WHOAMI_SERVICE is set as the Authorization heade to denote that
    // a request is being made by the whoami microservice
    public static final String AUTHORIZATION_WHOAMI_SERVICE = "whoami-service";
    // AUTHORIZATION_AUTHENTICATED_USER is set as the Authorization header to denote that
    // a request is being made by an authenticated web user
    public static final String AUTHORIZATION_AUTHENTICATED_USER = "kdr-authenticated";
    // AUTHORIZATION_ICAL_SERVICE is set as the Authorization header to denote that
    // a request is being made by the ical service
    public static final String AUTHORIZATION_ICAL_SERVICE = "ical-service";
    // AUTH ERROR Messages
    public static final String ERROR_MSG_DO_NOT_HAVE_ACCESS = "You do not have access to this service";
    public static final String ERROR_MSG_MISSING_AUTH_HEADER = "Missing Authorization http header";
    // AUTHORIZATION_RESUME_SERVICE is set as Authorization header to denote that
    // a request is being made by resume service
    public static final String AUTHORIZATION_RESUME_SERVICE = "resume-service";
    // AUTHORIZATION_RESUME_SERVICE is set as Authorization header to denote that
    // a request is being made by job service
    public static final String AUTHORIZATION_JOB_SERVICE = "job-service";
    // AUTHORIZATION_MATCHING_SERVICE is set as Authorization header to denote that
    // a request is being made by matching service
    public static final String AUTHORIZATION_MATCHING_SERVICE = "matching-service";

}
