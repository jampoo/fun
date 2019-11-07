import javafx.util.Pair;

import javax.print.DocFlavor;
import java.util.*;
import java.util.function.BiFunction;

public class Solution {
    private static final String ASC = "asc";
    private static final String DESC = "desc";

    public static <T> void assertEqual(T expected, T actual) {
        if (expected == null && actual == null || actual != null && actual.equals(expected)) {
            System.out.println("PASSED");
        } else {
            throw new AssertionError("Expected:\n  " + expected + "\nActual:\n  " + actual + "\n");
        }
    }

    // 1
    public static Map<String, Integer> minByKey(String key, List<Map<String, Integer>> records) {
        if (key == null || key.isEmpty() || records == null || records.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Integer> ans = records.get(0);
        for (int i = 1; i < records.size(); i++) {
            Map<String, Integer> curr = records.get(i);
            if (ans.getOrDefault(key, 0) > curr.getOrDefault(key, 0)) {
                ans = curr;
            }
        }
        return ans;
    }

    public static void testMinByKey() {
        List<Map<String, Integer>> example1 = Arrays.asList(
                Maps.of("a", 1, "b", 2),
                Maps.of("a", 2)
        );
        List<Map<String, Integer>> example2 = Arrays.asList(example1.get(1), example1.get(0));
        List<Map<String, Integer>> example3 = Arrays.asList(Maps.of());
        List<Map<String, Integer>> example4 = Arrays.asList(
                Maps.of("a", -1),
                Maps.of("b", -1)
        );
        List<Map<String, Integer>> example5 = Arrays.asList(
                Maps.of("a", -1),
                Maps.of("b", -1)
        );

        System.out.println("minByKey");
        assertEqual(example1.get(0), minByKey("a", example1));
        assertEqual(example2.get(1), minByKey("a", example2));
        assertEqual(example1.get(1), minByKey("b", example1));
        assertEqual(example3.get(0), minByKey("a", example3));
        assertEqual(example4.get(1), minByKey("b", example4));
        assertEqual(example4.get(0), minByKey("a", example4));

        assertEqual(new HashMap<>(),minByKey("", example1));
        assertEqual(new HashMap<>(), minByKey("a", new ArrayList<>()));
        assertEqual(example1.get(0), minByKey("c", example1));
    }

    // 2.a
    public static Map<String, Integer> firstByKey(String key, String direction, List<Map<String, Integer>> records) {
        if (key == null || key.isEmpty() || records==null || records.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, Integer> ans = records.get(0);
        for (int i=1; i<records.size(); i++) {
            Map<String, Integer> curr = records.get(i);
            if (direction.equals(ASC) && ans.getOrDefault(key, 0) > curr.getOrDefault(key, 0)) {
                ans = curr;
            } else if (direction.equals(DESC) && ans.getOrDefault(key, 0) < curr.getOrDefault(key, 0)) {
                ans = curr;
            }
        }
        return ans;
    }

    public static void testFirstByKey() {
        List<Map<String, Integer>> example1 = Arrays.asList(Maps.of("a", 1));
        List<Map<String, Integer>> example2 = Arrays.asList(
                Maps.of("b", 1),
                Maps.of("b", -2),
                Maps.of("a", 10)
        );
        List<Map<String, Integer>> example3 = Arrays.asList(
                Maps.of(),
                Maps.of("a", 10, "b", -10),
                Maps.of(),
                Maps.of("a", 3, "c", 3)
        );

        System.out.println("firstByKey");
        assertEqual(example1.get(0), firstByKey("a", "asc", example1));
        assertEqual(example2.get(0), firstByKey("a", "asc", example2));  // example2.get(1) ok too
        assertEqual(example2.get(2), firstByKey("a", "desc", example2));
        assertEqual(example2.get(1), firstByKey("b", "asc", example2));
        assertEqual(example2.get(0), firstByKey("b", "desc", example2));
        assertEqual(example3.get(1), firstByKey("a", "desc", example3));
    }

    // 2.b
    public static Map<String, Integer> minByKey_usingFirstByKey(String key, List<Map<String, Integer>> records) {
        return firstByKey(key, ASC, records);
    }

    // 2.b test
    public static void testMinByKey_usingFirstByKey() {
        List<Map<String, Integer>> example1 = Arrays.asList(
                Maps.of("a", 1, "b", 2),
                Maps.of("a", 2)
        );
        List<Map<String, Integer>> example2 = Arrays.asList(example1.get(1), example1.get(0));
        List<Map<String, Integer>> example3 = Arrays.asList(Maps.of());
        List<Map<String, Integer>> example4 = Arrays.asList(
                Maps.of("a", -1),
                Maps.of("b", -1)
        );

        System.out.println("minByKey_usingFirstByKey");
        assertEqual(example1.get(0), minByKey_usingFirstByKey("a", example1));
        assertEqual(example2.get(1), minByKey_usingFirstByKey("a", example2));
        assertEqual(example1.get(1), minByKey_usingFirstByKey("b", example1));
        assertEqual(example3.get(0), minByKey_usingFirstByKey("a", example3));
        assertEqual(example4.get(1), minByKey_usingFirstByKey("b", example4));
    }

    // 3.a
    class RecordComparator implements Comparator<Map<String, Integer>> {
        private String key;
        private String direction;

        public RecordComparator(String key, String direction) {
            if (key == null || direction == null || !(direction.equals(ASC) || direction.equals(DESC))) {
                throw new IllegalArgumentException();
            }
            this.key = key;
            this.direction = direction;
        }

        @Override
        public int compare(Map<String, Integer> r1, Map<String, Integer> r2) {
            if (r1 == null || r2 == null) {
                throw new IllegalArgumentException();
            }
            int val1 = r1.getOrDefault(key, 0);
            int val2 = r2.getOrDefault(key, 0);
            if (val1 == val2) {
                return 0;
            } else if (val1 < val2) {
                return direction.equals(ASC) ? -1 : 1;
            } else {
                // val1 > val2
                return direction.equals(ASC) ? 1 : -1;
            }
        }
    }

    // 3.a test
    public void testRecordComparator() {
        System.out.println("recordComparator");
        RecordComparator cmp = new RecordComparator("a", "asc");
        assertEqual(cmp.compare(Maps.of("a", 1), Maps.of("a", 2)), -1);
        assertEqual(cmp.compare(Maps.of("a", 2), Maps.of("a", 1)), 1);
        assertEqual(cmp.compare(Maps.of("a", 1), Maps.of("a", 1)), 0);
    }

    // 3.b
    public Map<String, Integer> minByKey_usingRecordComparator(String key, List<Map<String, Integer>> records) {
        RecordComparator recordComparator = new RecordComparator(key, ASC);
        return Collections.min(records, recordComparator);
    }

    public static void testMinByKey(BiFunction<String, List<Map<String,
            Integer>>, Map<String, Integer>> func) {
        List<Map<String, Integer>> example1 = Arrays.asList(
                Maps.of("a", 1, "b", 2),
                Maps.of("a", 2)
        );
        List<Map<String, Integer>> example2 = Arrays.asList(example1.get(1), example1.get(0));
        List<Map<String, Integer>> example3 = Arrays.asList(Maps.of());
        List<Map<String, Integer>> example4 = Arrays.asList(
                Maps.of("a", -1),
                Maps.of("b", -1)
        );

        assertEqual(example1.get(0), func.apply("a", example1));
        assertEqual(example2.get(1), func.apply("a", example2));
        assertEqual(example1.get(1), func.apply("b", example1));
        assertEqual(example3.get(0), func.apply("a", example3));
        assertEqual(example4.get(1), func.apply("b", example4));
    }

    // 3.b test
    public void testMinByKey_usingRecordComparator() {
        System.out.println("minByKey_usingRecordComparator");
        testMinByKey(Solution::minByKey_usingFirstByKey);
    }

    static class RecordComparatorII implements Comparator<Map<String,
            Integer>> {
        private LinkedHashMap<String, String> sortOrder;
        public RecordComparatorII(LinkedHashMap<String, String> sortOrder) {
            if (sortOrder == null || sortOrder.isEmpty()) {
                throw new IllegalArgumentException();
            }
            for (String dir : sortOrder.values()) {
                if (!dir.equals(ASC) && !dir.equals(DESC)) {
                    throw new IllegalArgumentException();
                }
            }
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(Map<String, Integer> r1, Map<String, Integer> r2) {
            if (r1 == null || r2 == null) {
                throw new IllegalArgumentException();
            }

            for(Map.Entry<String, String> entry : sortOrder.entrySet()) {
                String key = entry.getKey();
                String dir = entry.getValue();

                int v1 = r1.getOrDefault(key, 0);
                int v2 = r2.getOrDefault(key, 0);

                if (v1 == v2) {
                    continue;
                }

                if (dir.equals(ASC)) {
                    return v1 < v2 ? -1 : 1;
                } else if (dir.equals(DESC)) {
                    return v1 < v2 ? 1 : -1;
                }
            }
            return 0;
        }
    }

    // 4.a
    public static Map<String, Integer> firstBySortOrder(LinkedHashMap<String,
            String> sortOrder, List<Map<String, Integer>> records) {
        if (records == null || records.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return Collections.min(records, new RecordComparatorII(sortOrder));
    }

    // 4.a test
    public static void testFirstBySortOrder() {
        System.out.println("firstBySortOrder");

        LinkedHashMap<String, String> sortOrder1 = new LinkedHashMap<>();
        sortOrder1.put("a", "desc");

        LinkedHashMap<String, String> sortOrder2 = new LinkedHashMap<>();
        sortOrder2.put("b", "asc");
        sortOrder2.put("a", "asc");

        LinkedHashMap<String, String> sortOrder3 = new LinkedHashMap<>();
        sortOrder3.put("b", "asc");
        sortOrder3.put("a", "asc");

        assertEqual(Maps.of("a", 6), firstBySortOrder(sortOrder1, Arrays.asList(Maps.of("a", 5), Maps.of("a", 6))));
        assertEqual(Maps.of("a", -4, "b", 9), firstBySortOrder(sortOrder2, Arrays.asList(Maps.of("a", -5, "b", 10), Maps.of("a", -4, "b", 9))));
        assertEqual(Maps.of("a", -5, "b", 10), firstBySortOrder(sortOrder3, Arrays.asList(Maps.of("a", -5, "b", 10), Maps.of("a", -4, "b", 10))));
    }

    // 4.b
    public static Map<String, Integer> minByKey_usingFirstBySortOrder(String key, List<Map<String, Integer>> records) {
        LinkedHashMap<String, String> sortOrder = new LinkedHashMap<>();
        sortOrder.put(key, ASC);
        return firstBySortOrder(sortOrder, records);
    }

    // 4.b test
    public static void testMinByKey_usingFirstBySortOrder() {
        System.out.println("minByKey_usingFirstBySortOrder");
        testMinByKey(Solution::minByKey_usingFirstBySortOrder);
    }

    // 5.a
    public static Map<String, Integer> lastBySortOrder(LinkedHashMap<String,
            String> sortOrder, List<Map<String, Integer>> records) {
        return firstBySortOrder(reverseSortOrder(sortOrder), records);
    }

    // 5.a test
    public static void testLastBySortOrder() {
        System.out.println("lastBySortOrder");

        LinkedHashMap<String, String> sortOrder1 = new LinkedHashMap<>();
        sortOrder1.put("a", "desc");

        LinkedHashMap<String, String> sortOrder2 = new LinkedHashMap<>();
        sortOrder2.put("b", "asc");
        sortOrder2.put("a", "asc");

        LinkedHashMap<String, String> sortOrder3 = new LinkedHashMap<>();
        sortOrder3.put("b", "asc");
        sortOrder3.put("a", "asc");

        assertEqual(Maps.of("a", 6), lastBySortOrder(sortOrder1,
                Arrays.asList(Maps.of("a", 6), Maps.of("a", 6))));
        assertEqual(Maps.of("a", -5, "b", 10), lastBySortOrder(sortOrder2,
                Arrays.asList(Maps.of("a", -5, "b", 10), Maps.of("a", -4, "b", 9))));
        assertEqual(Maps.of("a", -4, "b", 10), lastBySortOrder(sortOrder3,
                Arrays.asList(Maps.of("a", -5, "b", 10), Maps.of("a", -4, "b", 10))));
    }

    private static LinkedHashMap<String, String> reverseSortOrder(LinkedHashMap<String, String> sortOrder) {
        LinkedHashMap<String, String> reverseOrder = new LinkedHashMap<>();

        for (Map.Entry<String, String> order : sortOrder.entrySet()) {
            String dir = (order.getValue().equals(DESC)) ? ASC : DESC;
            reverseOrder.put(order.getKey(), dir);
        }
        return reverseOrder;
    }

    // 6
    public static List<Map<String, Integer>> firstKBySortOrder(LinkedHashMap<String,
            String> sortOrder, int k, List<Map<String, Integer>> records) {
        if (k < 0 || records == null || sortOrder == null) {
            throw new IllegalArgumentException();
        }

        RecordComparatorII comparator = new RecordComparatorII(sortOrder);

        Collections.sort(records, comparator);
        List<Map<String, Integer>> res = new LinkedList<>();
        for (int i=0; i<k; i++) {
            res.add(records.get(i));
        }
        return res;
    }

    // test 6
    public static void testFirstKBySortOrder() {
        System.out.println("firstKBySortOrder");

        List<Map<String, Integer>> records = Arrays.asList(
                Maps.of("a", 1, "b", 2),
                Maps.of("a", 1, "b", 3),
                Maps.of("a", 0, "b", 5));

        LinkedHashMap<String, String> sortOrder = new LinkedHashMap<>();
        sortOrder.put("a", "asc");
        sortOrder.put("b", "desc");

        assertEqual(
                Arrays.asList(
                        Maps.of("a", 0, "b", 5),
                        Maps.of("a", 1, "b", 3)),
                firstKBySortOrder(sortOrder, 2, records));
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        // 1
        testMinByKey();
        // 2.a
        testFirstByKey();
        // 2.b
        testMinByKey_usingFirstByKey();

        // 3.a
        solution.testRecordComparator();
        // 3.b
        solution.testMinByKey_usingRecordComparator();

        // 4.a
        testFirstBySortOrder();

        // 4.b
        testMinByKey_usingFirstBySortOrder();

        // 5.a
        testLastBySortOrder();

        // 6
        testFirstKBySortOrder();
    }
}

class Maps {
    public static <K, V> Map<K, V> of() {
        return new HashMap<K, V>();
    }

    public static <K, V> Map<K, V> of(K k1, V v1) {
        Map<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        return map;
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Map<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V> LinkedHashMap<K, V> ordered(K k1, V v1) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();
        map.put(k1, v1);
        return map;
    }

    public static <K, V> LinkedHashMap<K, V> ordered(K k1, V v1, K k2, V v2) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> LinkedHashMap<K, V> ordered(K k1, V v1, K k2, V v2, K k3, V v3) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }
}
