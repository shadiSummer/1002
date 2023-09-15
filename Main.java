package GA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	private static final int NUM_GENERATIONS = 30;
	
	    public static void main(String[] args) {
	        List<Chromosome> initialPopulation = readGenesFromFile();
	        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(initialPopulation);

	        for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
	            geneticAlgorithm.evolve();

	            // Print the current population
	            Population currentPopulation = geneticAlgorithm.getPopulation();
	            System.out.println("Generation: " + generation);
	            for (Chromosome chromosome : currentPopulation.getPopulation()) {
	                System.out.println("Chromosome: " + Arrays.toString(chromosome.genes));
	            }

	            // Print the fittest chromosome in the current generation
	            System.out.println("Fittest Chromosome: " + Arrays.toString(currentPopulation.getFittestChromosome().genes));
	        }
	    }
	    
	    private static List<Chromosome> readGenesFromFile() {
	        List<Chromosome> initialPopulation = new ArrayList<>();
	        try (BufferedReader br = new BufferedReader(new FileReader("genes.txt"))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                line = line.trim();
	                if (!line.isEmpty()) {
	                    String[] values = line.split(",");
	                    int pathID = Integer.parseInt(values[0]);
	                    int a = Integer.parseInt(values[1]);
	                    int b = Integer.parseInt(values[2]);
	                    initialPopulation.add(new Chromosome(pathID, a, b));
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return initialPopulation;
	    }

}
