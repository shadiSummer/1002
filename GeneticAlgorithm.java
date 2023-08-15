package GA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GeneticAlgorithm {
    public static final int TOURNAMENT_SIZE = 3;
    static final double MUTATION_RATE = 0.25;
    public static final int NUM_GENERATIONS = 30;
    private static final int ELITISM_COUNT = 1;
    private static final int POPULATION_SIZE = 100; // Specify the desired population size

    private Population population;

    public GeneticAlgorithm(List<Chromosome> initialPopulation) {
        population = new Population(initialPopulation);
    }

    public void evolve() {
        for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
            List<Chromosome> newPopulation = new ArrayList<>();

            for (int i = 0; i < ELITISM_COUNT; i++) {
                newPopulation.add(population.getFittestChromosome());
            }

            while (newPopulation.size() < population.getPopulation().size()) {
                Chromosome parent1 = PopulationUtils.selectTournamentParent(population);
                Chromosome parent2 = PopulationUtils.selectTournamentParent(population);

                Chromosome child = PopulationUtils.crossover(parent1, parent2);
                PopulationUtils.mutate(child);

                newPopulation.add(child);
            }

            population = new Population(newPopulation);
        }
    }

    private static Chromosome generateRandomChromosome() {
        List<Integer> availablePathIDs = new ArrayList<>();
        for (int i = 1; i <= 250; i++) {
            availablePathIDs.add(i);
        }

        Collections.shuffle(availablePathIDs);
        int[] genes = new int[3];
        for (int i = 0; i < 3; i++) {
            genes[i] = availablePathIDs.get(i);
        }
        return new Chromosome(genes[0], genes[1], genes[2]);
    }
    
    public Population getPopulation() {
        return population;
    }
}
