package com.kuaidaoresume.resume.utils;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class ResumeDescriptionUtilTest {

    @Test
    public void whenSplitByBulletWithStarBullets_thenReturnPoints() {
        StringBuilder builder = new StringBuilder();
        String point = "hello world";
        String point1 = "HELLO WORLD.";
        builder.append(String.format("* %s\n", point));
        builder.append(String.format("* %s\n", point1));
        Collection<String> bullets = ResumeDescriptionUtil.splitByBullet(builder.toString());
        assertThat(bullets.size(), is(2));
        assertThat(bullets, containsInAnyOrder(point, point1));
    }

    @Test
    public void whenSplitByBulletWithStarBulletsNotFollowingByASpace_thenReturnPoints() {
        StringBuilder builder = new StringBuilder();
        String point = "hello world";
        String point1 = "HELLO WORLD.";
        builder.append(String.format("*%s\n", point));
        builder.append(String.format("*%s\n", point1));
        Collection<String> bullets = ResumeDescriptionUtil.splitByBullet(builder.toString());
        assertThat(bullets.size(), is(2));
        assertThat(bullets, containsInAnyOrder(point, point1));
    }

    @Test
    public void whenSplitByBulletWithNumberBullets_thenReturnPoints() {
        StringBuilder builder = new StringBuilder();
        String point = "hello world";
        String point1 = "HELLO WORLD.";
        builder.append(String.format("1 %s\n", point));
        builder.append(String.format("2 %s\n", point1));
        Collection<String> bullets = ResumeDescriptionUtil.splitByBullet(builder.toString());
        assertThat(bullets.size(), is(2));
        assertThat(bullets, containsInAnyOrder(point, point1));
    }

    @Test
    public void whenSplitByBulletWithNumberBulletsNotFollowingByASpace_thenReturnPoints() {
        StringBuilder builder = new StringBuilder();
        String point = "hello world";
        String point1 = "HELLO WORLD.";
        builder.append(String.format("1%s\n", point));
        builder.append(String.format("2%s\n", point1));
        Collection<String> bullets = ResumeDescriptionUtil.splitByBullet(builder.toString());
        assertThat(bullets.size(), is(2));
        assertThat(bullets, containsInAnyOrder(point, point1));
    }

    @Test
    public void whenSplitByBulletWithNumberAndDotBullets_thenReturnPoints() {
        StringBuilder builder = new StringBuilder();
        String point = "hello world";
        String point1 = "HELLO WORLD.";
        builder.append(String.format("1. %s\n", point));
        builder.append(String.format("2. %s\n", point1));
        Collection<String> bullets = ResumeDescriptionUtil.splitByBullet(builder.toString());
        assertThat(bullets.size(), is(2));
        assertThat(bullets, containsInAnyOrder(point, point1));
    }

    @Test
    public void whenSplitByBulletWithNumberAndDotBulletsNotFollowingByASpace_thenReturnPoints() {
        StringBuilder builder = new StringBuilder();
        String point = "hello world";
        String point1 = "HELLO WORLD.";
        builder.append(String.format("1.%s\n", point));
        builder.append(String.format("2.%s\n", point1));
        Collection<String> bullets = ResumeDescriptionUtil.splitByBullet(builder.toString());
        assertThat(bullets.size(), is(2));
        assertThat(bullets, containsInAnyOrder(point, point1));
    }

    @Test
    public void whenSplitByBulletWithOneBullet_thenReturnPoints() {
        StringBuilder builder = new StringBuilder();
        String point = "hello world";
        builder.append(String.format("1.%s\n", point));
        Collection<String> bullets = ResumeDescriptionUtil.splitByBullet(builder.toString());
        assertThat(bullets.size(), is(1));
        assertThat(bullets, containsInAnyOrder(point));
    }
}