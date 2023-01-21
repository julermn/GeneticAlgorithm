import java.util.function.ToDoubleFunction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class GeneticAlgorithm {
    private Random random = new Random();
    // A probability of crossover
    private double p_c;
    // A probability of mutation
    private double p_m;
    // A length of chromosome
    private int length;
    // A list of population
    private List<String> population = new ArrayList<>();
    // A list of population's fitnesses
    private List<Double> fitnesses = new ArrayList<>();

    // Generating a random chromosome
    public String generate(int length) {
        this.length = length;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            sb.append(random.nextInt(2) + "");
        }
        return sb.toString();
    }

    // Generating a random chromosome
    private String generate() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            sb.append(random.nextInt(2) + "");
        }
        return sb.toString();
    }

    // Selection of fittest
    private String[] select() {
        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < population.size(); i++) {
            map.put(population.get(i), fitnesses.get(i));
        }
        return map.entrySet().stream().sorted(Entry.comparingByValue(Comparator.naturalOrder()))
                .limit(2).map(i -> i.getKey()).toArray(String[]::new);
    }

    // A mutation can occur at every bit along each new chromosome
    private String mutate(String chromosome) {
        double prob = 0;
        StringBuilder sb = new StringBuilder();
        for (char ch : chromosome.toCharArray()) {
            prob = Math.random();
            if (prob <= p_m) {
                ch = ch == '0' ? '1' : '0';
            }
            sb.append(ch + "");
        }
        return sb.toString();
    }

    // A crossover occurs between two new chromosomes.
    // That means at some random bit along the length of the chromosome,
    // we cut off the rest of the chromosome and switch it with the cut off part of
    // the other one.
    public String[] crossover(String chromosome1, String chromosome2) {
        double prob = Math.random();
        String[] arr = { chromosome1, chromosome2 };
        if (prob <= p_c) {
            int probInd = random.nextInt(chromosome1.length() - 1);
            String buf = arr[0];
            arr[0] = arr[0].substring(0, probInd) + arr[1].substring(probInd);
            arr[1] = arr[1].substring(0, probInd) + buf.substring(probInd);
        }
        return arr;
    }

    public String run(ToDoubleFunction<String> fitness, int length, double p_c,
            double p_m) {
        return this.run(fitness, length, p_c, p_m, 100);
    }

    // Running of evolution
    public String run(ToDoubleFunction<String> fitness, int length, double p_c,
            double p_m, int iterations) {
        this.p_c = p_c;
        this.p_m = p_m;
        this.length = length;
        String[] children = new String[2];
        double[] chFit = new double[2];
        // Creating new population
        for (int j = 0; j < 10; j++) {
            population.add(generate());
            fitnesses.add(fitness.applyAsDouble(population.get(j)));
        }
        // Iterations of population's evolution
        for (int i = 1; i <= iterations; i++) {
            if (fitnesses.contains(0.0)) {
                break;
            }
            List<String> newPopulation = new ArrayList<>();
            List<Double> newFitnesses = new ArrayList<>();
            while (newPopulation.size() < population.size()) {
                // Generating two children from population
                children = select();
                children = crossover(children[0], children[1]);
                children[0] = mutate(children[0]);
                children[1] = mutate(children[1]);
                for (int j = 0; j < children.length; j++) {
                    chFit[j] = fitness.applyAsDouble(children[j]);
                }
                newPopulation.add(children[0]);
                newPopulation.add(children[1]);
                newFitnesses.add(chFit[0]);
                newFitnesses.add(chFit[1]);
            }
            population = newPopulation;
            fitnesses = newFitnesses;
        }
        double min = fitnesses.stream().mapToDouble(i -> i).min().getAsDouble();
        int indMin = fitnesses.indexOf(min);
        return population.get(indMin);
    }
}