# 1002
This repository is created for all the developments made during TUBITAK 1002 project.
This Java program demonstrates the use of a genetic algorithm to solve a path selection in a network with MPTCP transport protocol optimization problem. The goal is to find the fittest chromosome in a population by evolving and evaluating the individuals over multiple generations.

## Problem Description

The problem involves optimizing a population of chromosomes, where each chromosome consists of three genes. The values of the genes are read from a text file called `gene_values.txt`, which contains 250 lines. Each line represents a gene and consists of three numbers: gene value, bandwidth, and delay.

The fitness of each chromosome is calculated using the formula: `0.6 * a + 0.4 * b`, where `a` is the total sum of the bandwidth of the three genes in the chromosome, and `b` is the difference between the delays of the genes.


## Configuration

You can adjust the following parameters in the code to customize the genetic algorithm's behavior:

- `TOURNAMENT_SIZE`: The number of chromosomes to compete in tournament selection.
- `MUTATION_RATE`: The probability of mutation for each gene in a chromosome.
- `NUM_GENERATIONS`: The total number of generations for the evolution process.
- `ELITISM_COUNT`: The number of best chromosomes to carry over to the next generation.
- `POPULATION_SIZE`: The size of the population in each generation.


## genes.txt
Here is a sample of the data in the genes.txt file. 
First number represents the ID of a particular path
Second number represents the bandwidth of that path.
Third number represents the delay of that path.

31,42,93
102,82,100
57,222,94
280,128,94
258,176,94
189,85,73
113,112,72
89,298,100

## Output

The program outputs the progress and results of the genetic algorithm, including:

- The initial population with fitness values.
- The evolution process, showing the fitness values of the best chromosomes in each generation.
- The final population with fitness values.
- The fittest chromosome and its fitness value after the evolution.

## Example Usage

1. Ensure you have Java installed on your system.
2. Compile the source code with `javac *.java`.
3. Create a `genes.txt` file with gene data.
4. Run the program with `java Main`.


*** Feel free to modify and adapt the code to suit your needs.***
