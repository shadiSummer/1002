package GA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import GA.GeneticAlgorithm;


class PopulationUtils {
    public static final int POPULATION_SIZE = 50;

    public static Chromosome selectTournamentParent(Population population) {
        List<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < GeneticAlgorithm.getTournamentSize(); i++) { // Use the getter method
            tournament.add(population.getPopulation().get(new Random().nextInt(population.getPopulation().size())));
        }
        return PopulationUtils.getFittestChromosome(tournament);
    }


    public static Chromosome getFittestChromosome(List<Chromosome> chromosomes) {
        Chromosome fittest = chromosomes.get(0);
        for (Chromosome chromosome : chromosomes) {
            if (chromosome.fitness > fittest.fitness) {
                fittest = chromosome;
            }
        }
        return fittest;
    }

    public static Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        int[] childGenes = new int[3];
        
        for (int i = 0; i < 3; i++) {
            if (Math.random() < 0.5) {
                childGenes[i] = parent1.genes[i];
            } else {
                childGenes[i] = parent2.genes[i];
            }
        }

        return new Chromosome(childGenes[0], childGenes[1], childGenes[2]);
    }


    public static void mutate(Chromosome chromosome) {
        for (int i = 0; i < chromosome.genes.length; i++) {
            if (Math.random() < GeneticAlgorithm.getMutationRate()) { // Use the getter method
                chromosome.genes[i] = new Random().nextInt(250) + 1;
            }
        }
    }

    
}
