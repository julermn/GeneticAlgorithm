import java.util.function.ToDoubleFunction;

public class App {
    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        int length = 35;
        // The test generate a random binary string of "length" digits
        // This algorithm must discover that string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) Math.floor(Math.random() * 2));
        }
        String s = sb.toString();
        // The score of a chromosome is the number of bits that differ from the goal
        // string "s".
        ToDoubleFunction<String> f = str -> {
            double d = 0;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != s.charAt(i)) {
                    d++;
                }
            }
            return d;
        };
        System.out.println(s);
        // The crossover probability is 0.6 and the mutation probability is 0.02.
        System.out.println(ga.run(f, 35, 0.6, 0.02));
    }
}
