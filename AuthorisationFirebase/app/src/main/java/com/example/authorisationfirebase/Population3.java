package com.example.authorisationfirebase;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Population3 {

    private Individual3[] population;
    private double populationFitness = -1;

    /**
     * Initializes blank population of individuals
     *
     * @param populationSize
     *            The size of the population
     */
    public Population3(int populationSize) {
        // Initial population
        this.population = new Individual3[populationSize];
    }

    /**
     * Initializes population of individuals
     *
     * @param populationSize
     *            The size of the population
     * @param chromosomeLength
     *            The length of the individuals chromosome
     */
    public Population3(int populationSize, int chromosomeLength) {
        // Initial population
        this.population = new Individual3[populationSize];

        // Loop over population size
        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
            // Create individual
            Individual3 individual = new Individual3(chromosomeLength);
            // Add individual to population
            this.population[individualCount] = individual;
        }
    }

    /**
     * Get individuals from the population
     *
     * @return individuals Individuals in population
     */
    public Individual3[] getIndividuals() {
        return this.population;
    }

    /**
     * Find fittest individual in the population
     *
     * @param offset
     * @return individual Fittest individual at offset
     */
    public Individual3 getFittest(int offset) {
        // Order population by fitness
        Arrays.sort(this.population, new Comparator<Individual3>() {
            @Override
            public int compare(Individual3 o1, Individual3 o2) {
                if (o1.getFitness() > o2.getFitness()) {
                    return -1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                }
                return 0;
            }
        });

        // Return the fittest individual
        return this.population[offset];
    }

    /**
     * Set population's fitness
     *
     * @param fitness
     *            The population's total fitness
     */
    public void setPopulationFitness(double fitness) {
        this.populationFitness = fitness;
    }

    /**
     * Get population's fitness
     *
     * @return populationFitness The population's total fitness
     */
    public double getPopulationFitness() {
        return this.populationFitness;
    }

    /**
     * Get population's size
     *
     * @return size The population's size
     */
    public int size() {
        return this.population.length;
    }

    /**
     * Set individual at offset
     *
     * @param individual
     * @param offset
     * @return individual
     */
    public Individual3 setIndividual(int offset, Individual3 individual) {
        return population[offset] = individual;
    }

    /**
     * Get individual at offset
     *
     * @param offset
     * @return individual
     */
    public Individual3 getIndividual(int offset) {
        return population[offset];
    }

    /**
     * Shuffles the population in-place
     *
     * @return void
     */
    public void shuffle() {
        Random rnd = new Random();
        for (int i = population.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Individual3 a = population[index];
            population[index] = population[i];
            population[i] = a;
        }
    }

}
