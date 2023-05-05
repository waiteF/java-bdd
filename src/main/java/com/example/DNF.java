package com.example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;


public class DNF {

    private static final Random rand = new Random();


    private static String convertToString(String[] stringArray) {
        String result = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringArray.length; i++) {
            sb.append("+" + stringArray[i]);
        }
        result = sb.toString();
        result = result.substring(1);
        return result;
    }


    private static String[] removeDuplicates(String[] conjunction) {
        ArrayList<String> uniqueNodes = new ArrayList<>();

        for (int i = 0; i < conjunction.length; i++) {
            // Push if there wasn`t such before
            if (!uniqueNodes.contains(conjunction[i])) {
                uniqueNodes.add(conjunction[i]);
            }
        }
        return uniqueNodes.toArray(new String[uniqueNodes.size()]);
    }


    private static String prettyConjunction(String bFunction, String order) {
        String result = "";
        for (int i = 0; i < order.length(); i++) {
            if (bFunction.contains("!" + order.charAt(i))) {
                result += "!" + order.charAt(i);
            } else if (bFunction.contains(String.valueOf(order.charAt(i)))) {
                result += String.valueOf(order.charAt(i));
            }
        }
        return result;
    }


    private static String[] pretty(String[] conjunction, String order) {
        String[] result = conjunction;

        // Make prettier every conjunction
        for (int i = 0; i < result.length; i++) {
            result[i] = prettyConjunction(result[i], order);
        }

        // Bubble sort to put biggest conjunction in beggining of array
        String buf;
        for (int i = 0; i < result.length - 1; i++) {
            for (int j = 0; j < result.length - i - 1; j++) {
                if (result[j].replace("!", "").length() <= result[j + 1].replace("!", "")
                        .length()) {
                    buf = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = buf;
                }
            }
        }

        result = removeDuplicates(result);
        return result;
    }

    public static String substituteVariable(boolean state, String letter, String bFunction, String order) {
        if (bFunction.equals("1"))
            return "1";
        else if (bFunction.equals("0"))
            return "0";

        // Preparing---------------------
        String result = "";
        String[] subFunction = bFunction.split("\\+");

        subFunction = pretty(subFunction, order);

        // Substitution--------------------
        if (state) { // If A = 1
            for (int i = 0; i < subFunction.length; i++) {
                // If there is !A - forget this conjunction
                if (subFunction[i].contains("!" + letter)) {
                    // Nothing
                } // If there is A - replace with "1", push to result
                else if (subFunction[i].equals(letter))
                    return "1";
                else if (subFunction[i].contains(letter)) {
                    result += "+" + subFunction[i].replaceAll(letter, "");
                } // If there are not A or !A - push to result
                else
                    result += "+" + subFunction[i];
            }
        } else { // If A = 0
            for (int i = 0; i < subFunction.length; i++) {
                // If there is !A
                if (subFunction[i].contains("!" + letter)) {
                    // If there is !A*A == 0
                    if (subFunction[i].replaceAll("!" + letter, "1").contains(letter)) {
                        // Nothing
                    } // If single !A - replace with "1", push to result
                    else if (subFunction[i].equals("!" + letter))
                        return "1";
                    else {
                        result += "+" + subFunction[i].replaceAll("!" + letter, "");
                    }
                } // If there is A - forget this conjunction
                else if (subFunction[i].contains(letter)) {
                    // Nothing
                } // If there are not A or !A - push to result
                else
                    result += "+" + subFunction[i];
            }
        }

        if (result.length() == 0) {
            return "0";
        } else {
            result = result.substring(1);
        }

        // Removes duplicates after substitution of variable

        result = convertToString(pretty(result.split("\\+"), order));

        return result;
    }


    public static BigInteger hashCode(String bFunc, String order) {
        if (bFunc.equals("1"))
            return BigInteger.valueOf(0);
        else if (bFunc.equals("0"))
            return BigInteger.valueOf(1);

        BigInteger res = BigInteger.valueOf(1);
        int conjunctionCode = 0;

        String[] conjunction = bFunc.split("\\+");

        for (int i = 0; i < conjunction.length; i++) {
            conjunctionCode = 0;
            for (int j = 0; j < order.length(); j++) {
                if (conjunction[i].contains("!" + order.charAt(j))) {
                    conjunctionCode += 2 * Math.pow(3, j);
                } else if (conjunction[i].contains(String.valueOf(order.charAt(j)))) {
                    conjunctionCode += Math.pow(3, j);
                }
            }
            if (conjunctionCode == 0)
                conjunctionCode = 1;

            res = res.multiply(BigInteger.valueOf(conjunctionCode));
        }

        int digits = conjunction.length;
        BigInteger sub = (BigInteger.valueOf(10l)).pow(res.toString().length());
        sub = sub.multiply(BigInteger.valueOf(digits));
        res = res.add(sub);
        return res;
    }


    public static String generatePretty(String alphabet, int conjunctionsMaxCount, int conjunctionMaxLength) {
        String result = "";
        conjunctionsMaxCount = rand.nextInt(conjunctionsMaxCount);
        for (int i = 0; i < conjunctionsMaxCount; i++) {
            int conjunctionLength = rand.nextInt(conjunctionMaxLength);
            if (conjunctionLength > 0) {
                result += "+";
                for (int j = 0; j < conjunctionLength; j++) {
                    result += ((rand.nextBoolean()) ? "!" : "") + alphabet.charAt(rand.nextInt(alphabet.length()));
                }
            }
        }

        if (result.length() <= 1)
            return ((rand.nextBoolean()) ? "!" : "") + alphabet.charAt(rand.nextInt(alphabet.length()));
        else {
            result = result.substring(1);
            return result;
        }
    }

    public static char substituteAllVariables(String variablesValue, String bFunction, String order) {
        String result = bFunction;
        String letter;

        if (variablesValue.length() != order.length()) {
            System.out.println("ERR");
            return '9';
        }

        for (int i = 0; i < order.length(); i++) {
            letter = String.valueOf(order.charAt(i));

            if (variablesValue.charAt(i) == '1') {
                result = result.replaceAll("!" + letter, "0");
                result = result.replaceAll(letter, "1");
            } else {
                result = result.replaceAll("!" + letter, "1");
                result = result.replaceAll(letter, "0");
            }
        }

        String[] conjunction = result.split("\\+");

        for (int i = 0; i < conjunction.length; i++) {
            if (!conjunction[i].contains("0"))
                return '1';

        }

        return '0';
    }

    public static String generateDNF(String alphabet, Integer conjunctionsCount) {
        String result = "";
        String conjunction = "";
        if (conjunctionsCount == null)
            conjunctionsCount = rand.nextInt(alphabet.length() + 1);

        for (int i = 0; i < conjunctionsCount; i++) {
            int conjunctionLength = rand.nextInt(alphabet.length());
            if (conjunctionLength > 0) {
                conjunction = generateConjunction(alphabet);
                if (conjunction.length() != 0)
                    result += "+" + conjunction;
            }
        }

        if (result.length() <= 1)
            return ((rand.nextBoolean()) ? "!" : "") + alphabet.charAt(rand.nextInt(alphabet.length()));
        else
            return result.substring(1);
    }

    private static String generateConjunction(String alphabet) {
        String result = "";
        for (int j = 0; j < alphabet.length(); j++) {
            if (rand.nextBoolean())
                result += ((rand.nextBoolean()) ? "!" : "") + alphabet.charAt(j);
        }

        return result;
    }

}
