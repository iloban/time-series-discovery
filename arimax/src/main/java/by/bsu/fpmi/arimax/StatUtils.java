package by.bsu.fpmi.arimax;

import by.bsu.fpmi.arimax.model.Moment;
import by.bsu.fpmi.arimax.model.TimeSeries;
import by.bsu.fpmi.arimax.model.TimeSeriesBundle;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public final class StatUtils {
    private StatUtils() {
    }

    public static double calcNumerator(TimeSeries timeSeries) {
        double mean = getMean(timeSeries);
        double standardDeviation = calcStandardDeviation(timeSeries, mean);
        double minCumulativeDeviation = Double.MAX_VALUE;
        double maxCumulativeDeviation = Double.MIN_VALUE;
        List<Moment> moments = timeSeries.getMoments();
        for (int t = 1; t <= moments.size(); t++) {
            double cumulativeDeviation = calcCumulativeDeviation(moments, mean, t);
            if (minCumulativeDeviation > cumulativeDeviation) {
                minCumulativeDeviation = cumulativeDeviation;
            }
            if (maxCumulativeDeviation < cumulativeDeviation) {
                maxCumulativeDeviation = cumulativeDeviation;
            }
        }
        double spread = maxCumulativeDeviation - minCumulativeDeviation;
        return Math.log10(spread / standardDeviation);
    }

    public static double calcDenominator(int segmentLength) {
        return Math.log10(segmentLength / 2);
    }

    public static TimeSeriesBundle getBundle(TimeSeries timeSeries) {
        double mean = getMean(timeSeries);
        double variance = getVariance(timeSeries, mean);
        TimeSeries acfSeries = calcACFSeries(timeSeries, mean, variance);
        TimeSeries pacfSeries = getPACFSeries(timeSeries.getTitle(), acfSeries);
        return new TimeSeriesBundle(timeSeries, acfSeries, pacfSeries);
    }

    public static TimeSeries calcACFSeries(TimeSeries timeSeries, double mean, double variance) {
        List<Moment> moments = timeSeries.getMoments();
        List<Moment> result = new ArrayList<>(moments.size());
        for (int k = 0; k < moments.size(); k++) {
            result.add(new Moment(k, getACF(moments, k, mean, variance)));
        }
        return new TimeSeries(result, "ACF of " + timeSeries.getTitle());
    }

    public static TimeSeries getPACFSeries(String timeSeriesTitle, TimeSeries acfSeries) {
        // Declaration
        List<Moment> acfMoments = acfSeries.getMoments();
        List<Moment> result = new ArrayList<>(acfMoments.size());

        List<Double> oldFies;
        List<Double> newFies = new ArrayList<>();

        // Initialization
        result.add(acfMoments.get(1));
        newFies.add(acfMoments.get(1).getValue());

        // Computation
        for (int k = 2; k < acfMoments.size(); k++) {
            Moment moment = new Moment(k, getPACF(acfMoments, k, newFies));
            result.add(moment);

            oldFies = newFies;
            newFies = getNewFies(oldFies, k, moment.getValue());
        }

        // Result
        return new TimeSeries(result, "PACF of " + timeSeriesTitle);
    }

    private static List<Double> getNewFies(List<Double> oldFies, int k, double newFi) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < k - 1; i++) {
            result.add(oldFies.get(i) - newFi * oldFies.get(k - i - 2));
        }
        result.add(newFi);
        return result;
    }

    private static double getPACF(List<Moment> acfMoments, int k, List<Double> fies) {
        double numeratorSum = 0;
        double denominatorSum = 0;
        for (int i = 0; i < k - 1; i++) {
            numeratorSum += fies.get(i) * acfMoments.get(k - i - 2).getValue();
            denominatorSum += fies.get(i) * acfMoments.get(i).getValue();
        }
        return (acfMoments.get(k - 1).getValue() - numeratorSum) / (1 - denominatorSum);
    }

    public static double getACF(List<Moment> moments, int k, double mean, double variance) {
        int n = moments.size();
        double numerator = 0;
        double denominator = (n - k) * variance;
        for (int i = 0; i < n - k; i++) {
            numerator += (moments.get(i).getValue() - mean) * (moments.get(i + k).getValue() - mean);
        }
        return numerator / denominator;
    }

    public static double getMean(TimeSeries timeSeries) {
        List<Moment> moments = timeSeries.getMoments();
        double sum = 0;
        for (Moment moment : moments) {
            sum += moment.getValue();
        }
        return sum / moments.size();
    }

    public static double getVariance(TimeSeries timeSeries, double mean) {
        List<Moment> moments = timeSeries.getMoments();
        double sumOfSquares = 0;
        for (Moment moment : moments) {
            double value = moment.getValue();
            sumOfSquares += (value - mean) * (value - mean);
        }
        return sumOfSquares / (moments.size() - 1);
    }

    public static double calcStandardDeviation(TimeSeries timeSeries, double mean) {
        return Math.sqrt(getVariance(timeSeries, mean));
    }

    public static double calcCumulativeDeviation(List<Moment> segment, double mean, int t) {
        double sum = 0;
        for (int i = 0; i < t; i++) {
            Moment moment = segment.get(i);
            sum += moment.getValue() - mean;
        }
        return sum;
    }

    public static Pair<Double, Double> calcLineRegressionCoefficientsByOLS(List<Double> xs, List<Double> ys) {
        double c1 = 0;
        double c2 = 0;
        double g1 = 0;
        double g2 = 0;
        int count = xs.size() + 1;
        for (int i = 0; i < xs.size(); i++) {
            double x = xs.get(i);
            double y = ys.get(i);
            c1 += x * x;
            c2 += x;
            g1 += x * y;
            g2 += y;
        }
        return Pair.of((count * g1 - c2 * g2) / (count * c1 - c2 * c2), (g2 * c1 - g1 * c2) / (count * c1 - c2 * c2));
    }

    public static double calcFractalDimension(double hurst) {
        return 2 - hurst;
    }

    public static double calcCorrelationParameter(double hurst) {
        return 2 * (1 - hurst);
    }

    public static double calcSpectralIndex(double hurst) {
        return 2 * hurst + 1;
    }

    public static double calcFractalMeasure(double hurst) {
        return 3 - 2 * hurst;
    }

    public static TimeSeries getRegion(TimeSeries timeSeries, int leftBound, int rightBound) {
        return new TimeSeries(new ArrayList<>(timeSeries.getMoments().subList(leftBound, rightBound)),
                "Region[" + leftBound + "; " + rightBound + "] of " + timeSeries.getTitle());
    }
}
