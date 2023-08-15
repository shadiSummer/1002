package GA;

import java.util.Arrays;
import java.util.Collections;

class Chromosome {
    int[] genes;
    double fitness;

    public Chromosome(int pathID, int a, int b) {
        this.genes = new int[] { pathID, a, b };
        this.fitness = calculateFitness();
    }

    private double calculateFitness() {
        int aSum = Arrays.stream(genes).sum();
        int maxGene = Arrays.stream(genes).max().orElse(0);
        int minGene = Arrays.stream(genes).min().orElse(0);
        
        double a = aSum / 3.0;
        double c = Math.abs(maxGene - minGene);
        return 0.6 * a + 0.4 * c;
    }
}



