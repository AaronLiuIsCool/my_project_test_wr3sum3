package com.kuaidaoresume.resume.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeDescriptionUtil {

    public static Collection<String> splitByBullet(String description) {
        Pattern bulletPattern = Pattern.compile("[\\*\\d+][\\.]?\\s?(.+)\\n?");
        List<String> bullets = new ArrayList<>();
        Matcher matcher = bulletPattern.matcher(description);
        while (matcher.find()) {
            bullets.add(matcher.group(1));
        }
        return bullets;
    }
}
