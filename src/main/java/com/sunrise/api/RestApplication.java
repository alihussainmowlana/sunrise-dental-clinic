package com.sunrise.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Jakarta REST Application configuration.
 * Defines the root application path for all RESTful API endpoints.
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
}