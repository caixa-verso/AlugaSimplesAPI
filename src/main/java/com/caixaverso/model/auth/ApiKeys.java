package com.caixaverso.model.auth;

import io.vertx.core.impl.ConcurrentHashSet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApiKeys {
    public static final Map<String, Papeis> API_KEYS = new ConcurrentHashMap<>();
}
