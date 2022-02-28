import java.util.*;
import java.util.Map.Entry;

public class Utiles {
    private static final Random rnd = new Random();

    static int randomRange(int minInclusive, int maxInclusive) {
        return minInclusive + rnd.nextInt(maxInclusive - minInclusive + 1);
    }
    public static HashMap<String, Float> sortByValue(HashMap<String, Float> hashMap) {
        // https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/

        // Create a list from elements of HashMap
        List<Map.Entry<String, Float>> list =
                new LinkedList<>(hashMap.entrySet());

        // Sort the list
        list.sort(Entry.comparingByValue());

        // put data from sorted list to hashmap
        HashMap<String, Float> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Float> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
