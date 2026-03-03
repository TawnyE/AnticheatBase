package me.nik.anticheatbase.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A BetterStream utility class
 * <p>
 * Updated to leverage modern Java Streams which are highly optimized in Java 17.
 */
public final class BetterStream {

    private BetterStream() {
    }

    public static <T> boolean anyMatch(final Collection<T> data, final Predicate<T> condition) {
        return data != null && data.stream().anyMatch(condition);
    }

    public static <T> boolean anyMatch(final T[] data, final Predicate<T> condition) {
        return data != null && Stream.of(data).anyMatch(condition);
    }

    public static <T> boolean allMatch(final Collection<T> data, final Predicate<T> condition) {
        return data != null && data.stream().allMatch(condition);
    }

    public static <T> boolean allMatch(final T[] data, final Predicate<T> condition) {
        return data != null && Stream.of(data).allMatch(condition);
    }

    public static <T> List<T> applyAndGet(final Collection<T> data, final Function<T, T> action) {
        if (data == null || action == null) return List.of();
        return data.stream().map(action).collect(Collectors.toList());
    }

    public static <T> double mapToDoubleMin(final Collection<T> data, final Function<T, Double> action) {
        if (data == null || data.isEmpty() || action == null) return 0D;
        return data.stream().mapToDouble(action::apply).min().orElse(0D);
    }

    public static <T> double mapToDoubleMax(final Collection<T> data, final Function<T, Double> action) {
        if (data == null || data.isEmpty() || action == null) return 0D;
        return data.stream().mapToDouble(action::apply).max().orElse(0D);
    }

    public static <T> List<T> filter(final Collection<T> data, final Predicate<T> filter) {
        if (data == null || filter == null) return List.of();
        return data.stream().filter(filter).collect(Collectors.toList());
    }

    public static <T> List<T> filter(final T[] data, final Predicate<T> filter) {
        if (data == null || filter == null) return List.of();
        return Stream.of(data).filter(filter).collect(Collectors.toList());
    }

    public static <T> Collection<T> distinct(final Collection<T> data) {
        return data == null ? List.of() : new HashSet<>(data);
    }

    public static int getDuplicates(final Collection<?> data) {
        if (data == null || data.isEmpty()) return 0;
        return data.size() - (int) data.stream().distinct().count();
    }

    public static double getMaximumDouble(final Collection<Double> nums) {
        return nums == null ? 0.0D : nums.stream().mapToDouble(d -> d).max().orElse(0.0D);
    }

    public static int getMaximumInt(final Collection<Integer> nums) {
        return nums == null ? 0 : nums.stream().mapToInt(i -> i).max().orElse(0);
    }

    public static long getMaximumLong(final Collection<Long> nums) {
        return nums == null ? 0L : nums.stream().mapToLong(l -> l).max().orElse(0L);
    }

    public static double getMinimumDouble(final Collection<Double> nums) {
        return nums == null ? 0.0D : nums.stream().mapToDouble(d -> d).min().orElse(0.0D);
    }

    public static int getMinimumInt(final Collection<Integer> nums) {
        return nums == null ? 0 : nums.stream().mapToInt(i -> i).min().orElse(0);
    }

    public static long getMinimumLong(final Collection<Long> nums) {
        return nums == null ? 0L : nums.stream().mapToLong(l -> l).min().orElse(0L);
    }

    public static double getSumDouble(final Collection<Double> nums) {
        return nums == null ? 0D : nums.stream().mapToDouble(d -> d).sum();
    }

    public static int getSumInt(final Collection<Integer> nums) {
        return nums == null ? 0 : nums.stream().mapToInt(i -> i).sum();
    }

    public static long getSumLong(final Collection<Long> nums) {
        return nums == null ? 0L : nums.stream().mapToLong(l -> l).sum();
    }

    public static double getAverageDouble(final Collection<Double> nums) {
        return nums == null || nums.isEmpty() ? 0D : getSumDouble(nums) / nums.size();
    }

    public static double getAverageInt(final Collection<Integer> nums) {
        return nums == null || nums.isEmpty() ? 0D : (double) getSumInt(nums) / nums.size();
    }
}
