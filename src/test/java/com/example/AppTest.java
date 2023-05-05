import com.example.BinaryTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

class AppTest {
    private final static int BDD_USE_COUNT = 50;
    private final static int Generate_COUNT = 100;
    private final static int hashCode_COUNT = 100;
    private final static String ALPHABET = "ABCDEFGHIJKLMN";

    private static int DEFAULT_TEST_COUNT = 100;
    private static int failure = 0;


    @Test
    void testBddUse() {
        double percentageSum = 0;
        for (int i = 0; i < BDD_USE_COUNT; i++) {
            String bFunction = DNF.generatePretty(ALPHABET, 20, ALPHABET.length() + 1);
            System.out.println(bFunction);
            BinaryTree tree = new BinaryTree(bFunction, ALPHABET);
            // System.out.println(tree.nodeCount + " - " + tree.order.length());
            double percentage = reductionPercentage(tree.nodeCount, tree.order.length());
            printPercentage(percentage);
            testOneBddUse(tree);
            percentageSum += percentage;
        }
        System.out.println("Tested " + BDD_USE_COUNT + " randomly generated Trees");
        System.out.println("With  different " + ALPHABET.length() + " variables");
        System.out.println("Averange Reduction rate : " + percentageSum / BDD_USE_COUNT);
        System.out.println("Errors ocured : " + failure);
    }


    double reductionPercentage(int nodeCount, int variablesCount) {
        int maxNodeCount = (int) Math.pow(2, variablesCount);
        double reductionPercentage = (maxNodeCount - nodeCount) * 100 / maxNodeCount;
        return reductionPercentage;
    }


    void printPercentage(double percent) {

        String TEXT_RED = "\u001B[31m";
        String TEXT_GREEN = "\u001B[32m";
        String TEXT_YELLOW = "\u001B[33m";
        String color;
        if (percent > 90)
            color = TEXT_GREEN;
        else if (percent > 75)
            color = TEXT_YELLOW;
        else
            color = TEXT_RED;

        System.out.println(percent + color);
    }


    void testOneBddUse(BinaryTree tree) {
        char result = ' ';
        char alternativeResult = ' ';
        int stateVariations = (int) Math.pow(2, tree.getOrder().length());
        String arguments;

        for (int i = 0; i < stateVariations; i++) {
            arguments = Integer.toBinaryString(i);
            int arg_len = arguments.length();
            if (arg_len < tree.getOrder().length()) {
                for (int j = 0; j < tree.getOrder().length() - arg_len; j++)
                    arguments = "0" + arguments;
            }
            alternativeResult = DNF.substituteAllVariables(arguments, tree.root.getbFunction(), tree.root.getOrder());
            result = tree.bddUse(arguments, tree.root);
            if (result != alternativeResult) {
                // Prints Failue
                System.out.println(arguments + "|\t:" + tree.root.getbFunction() + ":\t[" +
                        result + "]\t[" + alternativeResult + "]");
                failure++;
            } else {
                assertEquals(result, alternativeResult);
            }

        }

    }

    @Test
    void testGenegatePretty() {
        System.out.println("Alphabet : " + ALPHABET);
        for (int i = 0; i < Generate_COUNT; i++) {
            System.out.println(DNF.generatePretty(ALPHABET, 10, ALPHABET.length() + 1));
        }
    }

    /**
     * Prints test of substitution in random boolean function
     * 
     * @param iterations_count = 10
     * @param alphabet         = "ABCD"
     */
    void GenerationSubstitution_Test(Integer iterations_count, String alphabet) {
        // Default values
        if (iterations_count == null) {
            iterations_count = 10;
        }
        if (alphabet == null) {
            alphabet = "ABCD";
        }

        Random rand = new Random();
        String function;
        String substituted_funciton;
        String letter;

        for (int i = 0; i < iterations_count; i++) {
            function = DNF.generatePretty(alphabet, 4, 4);
            letter = String.valueOf(alphabet.charAt(rand.nextInt(alphabet.length())));

            System.out.println(
                    function + "\thash() = " + DNF.hashCode(function, alphabet) + "\t" + letter + " = 0");
            substituted_funciton = DNF.substituteVariable(false, letter, function, alphabet);
            System.out.println(
                    substituted_funciton + "\t\thash() = " + DNF.hashCode(substituted_funciton, alphabet));

            System.out.println(
                    function + "\thash() = " + DNF.hashCode(function, alphabet) + "\t" + letter + " = 1");
            substituted_funciton = DNF.substituteVariable(true, letter, function, alphabet);
            System.out.println(
                    substituted_funciton + "\t\thash() = " + DNF.hashCode(substituted_funciton, alphabet));

            System.out.println("-----------------------------------------------");
        }
    }

    @Test
    void testHashCode() {
        String function = "";
        String hash = "";

        System.out.println("Alphabet : " + ALPHABET + "\n");
        for (int i = 0; i < hashCode_COUNT; i++) {
            function = (DNF.generateDNF(ALPHABET, 10));
            hash = DNF.hashCode(function, ALPHABET).toString();
            System.out.println(function);
            System.out.println("Hash: " + hash);
            System.out.println("--------------------------------");
        }
    }

    @Test
    void testGenerateDNF() {
        System.out.println("Alphabet : " + ALPHABET);
        for (int i = 0; i < Generate_COUNT; i++) {
            System.out.println(DNF.generateDNF(ALPHABET, null));
        }
    }

    @Test
    void testTimeBddUse() {
        System.out.println("Testing FULL use of Trees with " + ALPHABET.length() + " of different variables");

        for (int i = 0; i < BDD_USE_COUNT; i++) {
            String Bfunction = DNF.generatePretty(ALPHABET, 10, ALPHABET.length() + 1);
            BinaryTree Tree = new BinaryTree(Bfunction, ALPHABET);
            Instant start = Instant.now();
            testOneBddUse(Tree);
            Instant end = Instant.now();
            System.out.println(Duration.between(start, end).toMillis() + "ms");

        }
    }

    @Test
    void testMemoryBddCreate() {
        System.out.println("Testing memory. Creation of Trees with " + ALPHABET.length() + " of different variables");
        Runtime runtime = Runtime.getRuntime();

        for (int i = 0; i < BDD_USE_COUNT; i++) {
            String Bfunction = DNF.generatePretty(ALPHABET, 10, ALPHABET.length() + 1);
            new BinaryTree(Bfunction, ALPHABET);
            long memory = runtime.totalMemory() - runtime.freeMemory();
            System.out.println(memory + " bytes");

        }
    }

    @Test
    void testTimeBddCreate() {
        System.out.println("Testing creation of Trees with " + ALPHABET.length() + " of different variables");

        for (int i = 0; i < BDD_USE_COUNT; i++) {
            String Bfunction = DNF.generatePretty(ALPHABET, 10, ALPHABET.length() + 1);
            Instant start = Instant.now();
            new BinaryTree(Bfunction, ALPHABET);
            Instant end = Instant.now();
            System.out.println(Duration.between(start, end).toMillis() + "ms");

        }
    }
}
