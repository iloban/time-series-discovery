package by.bsu.fpmi.arimax.model;

public final class ArimaModel {
    private final int p;
    private final int d;
    private final int q;

    public ArimaModel(int p, int d, int q) {
        this.p = p;
        this.d = d;
        this.q = q;
    }

    public int getP() {
        return p;
    }

    public int getD() {
        return d;
    }

    public int getQ() {
        return q;
    }

    @Override public String toString() {
        return "(" + p + ", " + d + ", " + q + ")";
    }
}