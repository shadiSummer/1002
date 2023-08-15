package GA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Population {
    private static final int ELITISM_COUNT = 1;
    private List<Chromosome> population;

    public Population(List<Chromosome> population) {
        this.population = population;
    }

    public Chromosome getFittestChromosome() {
        Chromosome fittest = population.get(0);
        for (Chromosome chromosome : population) {
            if (chromosome.fitness > fittest.fitness) {
                fittest = chromosome;
            }
        }
        return fittest;
    }

    public List<Chromosome> getPopulation() {
        return population;
    }

    public void performTournamentSelection(int tournamentSize) {
        List<Chromosome> newPopulation = new ArrayList<>();
        
        for (int i = 0; i < ELITISM_COUNT; i++) {
            newPopulation.add(getFittestChromosome());
        }

        while (newPopulation.size() < population.size()) {
            List<Chromosome> tournament = new ArrayList<>();
            for (int i = 0; i < tournamentSize; i++) {
                tournament.add(population.get(new Random().nextInt(population.size())));
            }
            newPopulation.add(PopulationUtils.getFittestChromosome(tournament));
        }

        population = newPopulation;
    }
}
