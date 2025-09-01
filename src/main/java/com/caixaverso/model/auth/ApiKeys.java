package com.caixaverso.model.auth;

import io.vertx.core.impl.ConcurrentHashSet;

import java.util.HashSet;
import java.util.Set;

public class ApiKeys {
    public static final Set<String> API_KEYS = new ConcurrentHashSet<>();
}
