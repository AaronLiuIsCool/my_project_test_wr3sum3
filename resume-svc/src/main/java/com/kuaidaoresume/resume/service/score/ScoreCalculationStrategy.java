package com.kuaidaoresume.resume.service.score;

import io.micrometer.core.instrument.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public interface ScoreCalculationStrategy<T> {

    int calculateScore(T entity);

    static int getScoreConfig(String key, int defaultValue) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("score/scores.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            String value = properties.getProperty(key);
            return StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : defaultValue;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
