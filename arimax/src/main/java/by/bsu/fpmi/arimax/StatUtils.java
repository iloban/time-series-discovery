package by.bsu.fpmi.arimax;

import by.bsu.fpmi.arimax.model.Moment;
import by.bsu.fpmi.arimax.model.TimeSeries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public final class StatUtils {
    private StatUtils() {
    }

    public static double calcNumerator(List<Moment> segment) {
        double mean = calcMean(segment);
        double standardDeviation = calcStandardDeviation(segment, mean);
        double minCumulativeDeviation = Double.MAX_VALUE;
        double maxCumulativeDeviation = Double.MIN_VALUE;
        for (int t = 1; t <= segment.size(); t++) {
            double cumulativeDeviation = calcCumulativeDeviation(segment, mean, t);
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

    public static double calcDenominator(List<Moment> segment) {
        return Math.log10(segment.size() / 2);
    }

    public static TimeSeries calcACFSeries(TimeSeries timeSeries) {
        List<Moment> moments = timeSeries.getMoments();
        double mean = calcMean(moments);
        double dispersion = calcDispersion(moments, mean);
        List<Moment> result = new ArrayList<>(moments.size());
        for (int k = 0; k < moments.size(); k++) {
            result.add(new Moment(k, calcACF(moments, k, mean, dispersion)));
        }
        return new TimeSeries(result, "ACF of " + timeSeries.getTitle());
    }

    public static TimeSeries calcPACFSeries(TimeSeries timeSeries) {
        List<Moment> moments = timeSeries.getMoments();
        double mean = calcMean(moments);
        double dispersion = calcDispersion(moments, mean);
        List<Moment> result = new ArrayList<>(moments.size());
        for (int k = 0; k < moments.size(); k++) {
            result.add(new Moment(k, calcPACF(moments, k, mean, dispersion)));
        }
        return new TimeSeries(result, "PACF of " + timeSeries.getTitle());
    }

    private static double calcPACF(List<Moment> moments, int k, double mean, double variance) {
        int n = moments.size();
        double numerator = 0;
        double denominator = (n - k) * variance;
        for (int i = 0; i < n - k; i++) {
            numerator += (moments.get(i).getValue() - mean) * (moments.get(i + k).getValue() - mean);
        }
        return numerator / denominator;
    }

    public static double calcACF(List<Moment> moments, int k, double mean, double variance) {
        int n = moments.size();
        double numerator = 0;
        double denominator = (n - k) * variance;
        for (int i = 0; i < n - k; i++) {
            numerator += (moments.get(i).getValue() - mean) * (moments.get(i + k).getValue() - mean);
        }
        return numerator / denominator;
    }

    public static double calcMean(List<Moment> segment) {
        double sum = 0;
        for (Moment moment : segment) {
            sum += moment.getValue();
        }
        return sum / segment.size();
    }

    public static double calcDispersion(List<Moment> segment, double mean) {
        double squareSum = 0;
        for (Moment moment : segment) {
            double value = moment.getValue();
            squareSum += (value - mean) * (value - mean);
        }
        return squareSum / segment.size(); // TODO: maybe -1 needed?
    }

    public static double calcStandardDeviation(List<Moment> segment, double mean) {
        return Math.sqrt(calcDispersion(segment, mean));
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
                "Region of " + timeSeries.getTitle());
    }
}
