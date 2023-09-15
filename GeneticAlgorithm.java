package GA;

import java.util.ArrayList;
import java.util.List;

class GeneticAlgorithm {
	private static final int TOURNAMENT_SIZE = 3;
    private static final double MUTATION_RATE = 0.25;
    private static final int NUM_GENERATIONS = 30;
    private static final int ELITISM_COUNT = 1;
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

            while (newPopulation.size() < PopulationUtils.POPULATION_SIZE) {
                Chromosome parent1 = PopulationUtils.selectTournamentParent(population);
                Chromosome parent2 = PopulationUtils.selectTournamentParent(population);

                Chromosome child = PopulationUtils.crossover(parent1, parent2);
                PopulationUtils.mutate(child);

                newPopulation.add(child);
            }

            population = new Population(newPopulation);
        }
    }

    public Population getPopulation() {
        return population;
    }
    
    public static double getMutationRate() {
        return MUTATION_RATE;
    }
    
    public static int getTournamentSize() {
        return TOURNAMENT_SIZE;
    }
}
