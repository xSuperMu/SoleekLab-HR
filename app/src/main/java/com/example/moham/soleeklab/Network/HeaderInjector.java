package com.example.moham.soleeklab.Network;

import java.util.HashMap;

public interface HeaderInjector {

    String KEY_CONTENT_TYPE_HEADER = "Content-Type";
    String KEY_AUTH_HEADER = "Authorization";
    String KEY_Bearer = "Bearer ";
    String APPLICATION_JSON = "application/json";

    HashMap<String, String> getHeaders();
}
