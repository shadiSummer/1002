# 1002
This repository is created for all the developments made during TUBITAK 1002 project.
This Java program demonstrates the use of a genetic algorithm to solve a path selection in a network with MPTCP transport protocol optimization problem. The goal is to find the fittest chromosome in a population by evolving and evaluating the individuals over multiple generations.

## Problem Description

The problem involves optimizing a population of chromosomes, where each chromosome consists of three genes. The values of the genes are read from a text file called `gene_values.txt`, which contains 250 lines. Each line represents a gene and consists of three numbers: gene value, bandwidth, and delay.

The fitness of each chromosome is calculated using the formula: `0.6 * a + 0.4 * b`, where `a` is the total sum of the bandwidth of the three genes in the chromosome, and `b` is the difference between the delays of the genes.
