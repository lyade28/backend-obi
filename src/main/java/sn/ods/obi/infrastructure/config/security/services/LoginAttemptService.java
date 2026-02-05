package sn.ods.obi.infrastructure.config.security.services;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author G2k R&D
 */

@Service
public class LoginAttemptService {
    public static final Logger LOGGER = LogManager.getLogger(LoginAttemptService.class);
    public static final Integer DURATION_TIME = 5;
    private final LoadingCache<String, Integer> loadingCache;

    public LoginAttemptService() {
        this.loadingCache = CacheBuilder.newBuilder().expireAfterWrite(DURATION_TIME, TimeUnit.MINUTES).build(new CacheLoader<>() {
            @Override
            public @NotNull Integer load(@NotNull String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(final String key) {
        loadingCache.invalidate(key);
    }

    public void loginFailed(String key) {
        int attempts = 0;
        try {
            attempts = loadingCache.get(key);
        } catch (ExecutionException e) {
            LOGGER.log(Level.valueOf("context"), e);
        }
        attempts++;
        loadingCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
            return loadingCache.get(key) >= 3;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
