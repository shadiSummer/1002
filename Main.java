package GA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Chromosome {
    private int[][] genes;
    private double fitness;

    public Chromosome(int[][] genes) {
        this.genes = genes;
        fitness = 0;
    }

    public int[][] getGenes() {
        return genes;
    }

    public double getFitness() {
        return fitness;
    }

    public void calculateFitness(int maxBandwidth, int maxDelay) {
        int totalBandwidth = 0;
        int delayDifference = Math.abs(genes[0][2] - genes[1][2]) + Math.abs(genes[1][2] - genes[2][2]); //delay difference of subflows(genes of a Chromosome)

        for (int i = 0; i < genes.length; i++) {
            totalBandwidth += genes[i][1];
        }

        double normalizedBandwidth = (double) totalBandwidth / (maxBandwidth * genes.length);
        double normalizedDelay = (double) delayDifference / (maxDelay * 2);

        fitness = 0.6 * normalizedBandwidth + 0.4 * normalizedDelay; //formula used for fitness
    }

    public Chromosome crossover(Chromosome partner) {  //two-point crossover
        int[][] childGenes = new int[genes.length][3];

        Random rand = new Random();
        int crossoverPoint1 = rand.nextInt(genes.length);
        int crossoverPoint2 = rand.nextInt(genes.length);

        int start = Math.min(crossoverPoint1, crossoverPoint2);
        int end = Math.max(crossoverPoint1, crossoverPoint2);

        for (int i = start; i <= end; i++) {
            childGenes[i][0] = genes[i][0];
            childGenes[i][1] = partner.genes[i][1];
            childGenes[i][2] = partner.genes[i][2];
        }

        for (int i = 0; i < genes.length; i++) {
            if (i < start || i > end) {
                childGenes[i][0] = partner.genes[i][0];
                childGenes[i][1] = genes[i][1];
                childGenes[i][2] = genes[i][2];
            }
        }

        return new Chromosome(childGenes);
    }

    public void mutate(double mutationRate) { // Mutation = 0.25
        Random rand = new Random();
        for (int i = 0; i < genes.length; i++) {
            if (rand.nextDouble() < mutationRate) {
                genes[i][0] = rand.nextInt(250) + 1;
                genes[i][1] = rand.nextInt(250) + 1;
                genes[i][2] = rand.nextInt(250) + 1;
            }
        }
    }

    public String toString(boolean isFittest) {
        StringBuilder sb = new StringBuilder();
        if (isFittest) {
            sb.append("\033[0;31m"); // Set color to red for fittest individual
        }

        sb.append("Chromosome: ");
        for (int i = 0; i < genes.length; i++) {
            sb.append("(").append(genes[i][0]).append(", ").append(genes[i][1]).append(", ").append(genes[i][2]).append(") ");
        }
        sb.append("Fitness: ").append(fitness);

        if (isFittest) {
            sb.append("\033[0m"); // Reset color
        }

        return sb.toString();
    }
}

class Population {
    private Chromosome[] individuals;

    public Population(int populationSize, int[][] genePool) {
        individuals = new Chromosome[populationSize];
        for (int i = 0; i < populationSize; i++) {
            individuals[i] = generateRandomChromosome(genePool);
        }
    }

    private Chromosome generateRandomChromosome(int[][] genePool) {
        Random rand = new Random();
        int[][] genes = new int[genePool.length][3];
        Set<Integer> usedGeneIndices = new HashSet<>();

        for (int i = 0; i < genes.length; i++) {
            int geneIndex = rand.nextInt(genePool.length);
            while (usedGeneIndices.contains(geneIndex)) {
                geneIndex = rand.nextInt(genePool.length);
            }

            genes[i][0] = genePool[geneIndex][0];
            genes[i][1] = genePool[geneIndex][1];
            genes[i][2] = genePool[geneIndex][2];

            usedGeneIndices.add(geneIndex);
        }

        return new Chromosome(genes);
    }

    public Chromosome[] getIndividuals() {
        return individuals;
    }

    public void evaluatePopulation(int maxBandwidth, int maxDelay) {
        for (Chromosome individual : individuals) {
            individual.calculateFitness(maxBandwidth, maxDelay);
        }
    }

    public Chromosome tournamentSelection(int tournamentSize) {  // 
        Random rand = new Random();
        Population tournament = new Population(tournamentSize, individuals[0].getGenes());

        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = rand.nextInt(individuals.length);
            tournament.getIndividuals()[i] = individuals[randomIndex];
        }

        return tournament.getFittestIndividual();
    }

    public Population evolvePopulation(double mutationRate, int tournamentSize, boolean elitism) {
        Population newPopulation = new Population(individuals.length, individuals[0].getGenes());

        int elitismOffset = 0;
        if (elitism) {
            Chromosome fittestIndividual = getFittestIndividual();
            newPopulation.getIndividuals()[0] = fittestIndividual;
            elitismOffset = 1;
        }

        for (int i = elitismOffset; i < individuals.length; i++) {
            Chromosome parent1 = tournamentSelection(tournamentSize);
            Chromosome parent2 = tournamentSelection(tournamentSize);

            Chromosome child = parent1.crossover(parent2);
            child.mutate(mutationRate);

            newPopulation.getIndividuals()[i] = child;
        }

        return newPopulation;
    }

    public Chromosome getFittestIndividual() {
        Chromosome fittest = individuals[0];
        for (int i = 1; i < individuals.length; i++) {
            if (individuals[i].getFitness() > fittest.getFitness()) {
                fittest = individuals[i];
            }
        }
        return fittest;
    }

    public int getFittestGeneIndex() {
        Chromosome fittestIndividual = getFittestIndividual();
        int[][] genes = fittestIndividual.getGenes();
        double maxFitness = 0;

        // Calculate the maximum fitness value of the fittest gene
        for (int i = 0; i < genes.length; i++) {
            double geneFitness = (0.6 * genes[i][1] + 0.4 *
                    (Math.abs(genes[i][2] - genes[(i + 1) % genes.length][2]) +
                            Math.abs(genes[(i + 1) % genes.length][2] -
                                    genes[(i + 2) % genes.length][2])));
            if (geneFitness > maxFitness) {
                maxFitness = geneFitness;
            }
        }

        int fittestGeneIndex = -1;
        double fittestGeneFitness = 0.0;

        // Find the fittest gene and normalize its fitness value
        for (int i = 0; i < genes.length; i++) {
            double geneFitness = (0.6 * genes[i][1] + 0.4 *
                    (Math.abs(genes[i][2] - genes[(i + 1) % genes.length][2]) +
                            Math.abs(genes[(i + 1) % genes.length][2] -
                                    genes[(i + 2) % genes.length][2])));

            // Normalize the fitness value based on the maximum fitness value of the fittest gene
            geneFitness /= maxFitness;

            if (geneFitness > fittestGeneFitness) {
                fittestGeneIndex = i;
                fittestGeneFitness = geneFitness;
            }
        }

        return fittestGeneIndex;
    }
}

public class Main {
    public static void main(String[] args) {
    	
        int seed = 123; // Set a specific seed value
        Random rand = new Random(seed); // To get the same value each time
    	
        int populationSize = 150;
        int geneLength = 3;
        int tournamentSize = 5;
        double mutationRate = 0.25; // set the mutation rate here
        int numberOfGenerations = 30; // set the number of generation considered
        boolean elitism = true;

        int[][] genePool = readGeneValuesFromFile("gene_values.txt"); //gets the values of the path, bandwidth and delay
        if (genePool == null) {
            System.out.println("Failed to read gene values from file.");
            return;
        }

        Population population = new Population(populationSize, genePool);

        for (int generation = 1; generation <= numberOfGenerations; generation++) {
            population.evaluatePopulation(250, 250); // Assuming maximum bandwidth and delay are 250

            System.out.println("Generation " + generation + ":");
            System.out.println("----------------------------");
            System.out.println("Fitness values:");

            Chromosome[] individuals = population.getIndividuals();
            Chromosome fittestIndividual = population.getFittestIndividual();
            for (int i = 0; i < individuals.length; i++) {
                individuals[i].calculateFitness(250, 250); // Assuming maximum bandwidth and delay are 250
                System.out.println(individuals[i].toString(individuals[i] == fittestIndividual));
            }

            System.out.println();

            population = population.evolvePopulation(mutationRate, tournamentSize, elitism);
        }

        Chromosome fittestInLastPopulation = population.getFittestIndividual();

        System.out.println("Fittest in the Last Population:");
        System.out.println(fittestInLastPopulation.toString(true));

        int fittestGeneIndex = population.getFittestGeneIndex();
        int[][] genes = fittestInLastPopulation.getGenes();
        double fittestGeneFitness = (0.6 * genes[fittestGeneIndex][1] + 0.4 *
                (Math.abs(genes[fittestGeneIndex][2] - genes[(fittestGeneIndex + 1) % genes.length][2]) +
                        Math.abs(genes[(fittestGeneIndex + 1) % genes.length][2] -
                                genes[(fittestGeneIndex + 2) % genes.length][2])));

        System.out.println("Fittest Gene in the Last Population:");
        System.err.println("Gene: (" + genes[fittestGeneIndex][0] + ", " +
                genes[fittestGeneIndex][1] + ", " +
                genes[fittestGeneIndex][2] + ")");
       // System.out.println("Normalized Fitness: " + fittestGeneFitness);
    }


    private static int[][] readGeneValuesFromFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            List<int[]> geneList = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3) {
                    int geneValue = Integer.parseInt(values[0].trim());
                    int bandwidth = Integer.parseInt(values[1].trim());
                    int delay = Integer.parseInt(values[2].trim());

                    geneList.add(new int[]{geneValue, bandwidth, delay});
                }
            }

            reader.close();

            return geneList.toArray(new int[0][]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
