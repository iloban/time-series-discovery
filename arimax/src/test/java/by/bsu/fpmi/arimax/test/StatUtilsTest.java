package by.bsu.fpmi.arimax.test;

import by.bsu.fpmi.arimax.StatUtils;
import by.bsu.fpmi.arimax.model.Moment;
import by.bsu.fpmi.arimax.model.TimeSeries;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StatUtilsTest {
    private static final double DELTA = 10E-5;

    @Test
    public void testMean() {
        List<Moment> series = new ArrayList<>();
        series.add(new Moment(1));
        series.add(new Moment(2));
        series.add(new Moment(3));

        double mean = StatUtils.getMean(new TimeSeries(series, "test"));

        assertEquals(mean, 2, DELTA);
    }
}
