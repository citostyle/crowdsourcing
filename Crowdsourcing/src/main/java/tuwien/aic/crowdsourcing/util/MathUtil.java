package tuwien.aic.crowdsourcing.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MathUtil {

    public static int[] quartiles(List<Integer> values) {
        if (values.size() < 3)
            // throw new IllegalArgumentException("This method is not designed to handle lists with fewer than 3 elements.");
            return new int[0];

        int median = median(values);

        List<Integer> lowerHalf = getValuesLessThan(values, median, true);
        List<Integer> upperHalf = getValuesGreaterThan(values, median, true);

        return new int[] { median(lowerHalf), median, median(upperHalf) };
    }

    public static List<Integer> getValuesGreaterThan(List<Integer> values, int limit, boolean orEqualTo) {
        List<Integer> modValues = new ArrayList<Integer>();

        for (int value : values)
            if (value > limit || (value == limit && orEqualTo))
                modValues.add(value);

        return modValues;
    }

    public static List<Integer> getValuesLessThan(List<Integer> values, int limit, boolean orEqualTo) {
        List<Integer> modValues = new ArrayList<Integer>();

        for (int value : values)
            if (value < limit || (value == limit && orEqualTo))
                modValues.add(value);

        return modValues;
    }

    public static int median(List<Integer> values) {
        Collections.sort(values);

        if (values.size() % 2 == 1)
            return values.get((values.size() + 1) / 2 - 1);
        else {
            double lower = values.get(values.size() / 2 - 1);
            double upper = values.get(values.size() / 2);

            return (int) ((lower + upper) / 2.0);
        }
    }

}
