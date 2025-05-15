package adduct;

public class MassTransformation {
    public static double toMonoisotopicMass(double mz, int charge, double adductMass) {
        return (mz * charge) - adductMass;
    }

    public static double toMz(double monoMass, int charge, double adductMass) {
        return (monoMass + adductMass) / charge;
    }
}
