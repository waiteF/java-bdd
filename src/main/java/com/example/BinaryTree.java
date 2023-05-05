package com.example;

import java.math.BigInteger;
public class BinaryTree {
    TreeNode root = null;
    String order = "";
    int nodeCount = 0;
    private static final TreeNode ONE = new TreeNode("1", "", "");
    private static final TreeNode ZERO = new TreeNode("0", "", "");

    // bbd_create
    BinaryTree(String bFunction, String order) {
        // Creating: Root, "1", "0"
        this.order = order;
        String prettierFunction = DNF.substituteVariable(true, "Z", bFunction, "Z" + order);
        this.root = new TreeNode(prettierFunction, String.valueOf(order.charAt(0)), order);
        this.nodeCount += 3;

        // Creating other levels
        for (int i = 2; i <= order.length() + 1; i++) {
            NodeObject[] nodeTable = new NodeObject[(int) Math.pow(2, (i - 1))];
            createLvl(i, 1, nodeTable, this.root);
        }

    }
    private void createLvl(int lvl, int current, NodeObject[] table, TreeNode root) {
        // case 1 - its Leaf - then return
        if (root == null) {
            System.out.println(null + " :: " + lvl + " :: " + current);
            return;
        }
        if (root == ONE || root == ZERO)
            return;

        String newOrder = this.root.getOrder().substring(lvl - 2);
        String substituteLetter = String.valueOf(this.root.getOrder().charAt(lvl - 2));
        String lvlLetter;
        String lvlOrder;

        if (this.root.getOrder().length() >= lvl) {
            lvlLetter = String.valueOf(this.root.getOrder().charAt(lvl - 1));
            lvlOrder = this.root.getOrder().substring(lvl - 1);
        } else {
            lvlOrder = this.root.getOrder().substring(lvl - 2);
            lvlLetter = String.valueOf(this.root.getOrder().charAt(lvl - 2));
        }
        // case 2 - we reach last level
        if (root.getLetter().equals(substituteLetter)) {

            String function = DNF.substituteVariable(false, substituteLetter, root.getbFunction(), newOrder);
            if (function.equals("0")) {
                root.setLeft(ZERO);
            } else if (function.equals("1")) {
                root.setLeft(ONE);
            } else if (!function.contains(lvlLetter)) {
                root.setLeft(null);
            } else
                root.setLeft(insertTable(table, function, lvlOrder, lvlLetter));

            function = DNF.substituteVariable(true, substituteLetter, root.getbFunction(), newOrder);
            if (function.equals("0")) {
                root.setRight(ZERO);
            } else if (function.equals("1")) {
                root.setRight(ONE);
            } else if (!function.contains(lvlLetter)) {
                root.setRight(null);
            } else
                root.setRight(insertTable(table, function, lvlOrder, lvlLetter));

        } else {
            if (root.getLeft() == null) {
                String function = DNF.substituteVariable(false, root.getLetter(), root.getbFunction(), root.getOrder());
                if (function.equals("0")) {
                    root.setLeft(ZERO);
                } else if (function.equals("1")) {
                    root.setLeft(ONE);
                } else if (!function.contains(lvlLetter)) {
                    root.setLeft(null);
                } else {
                    root.setLeft(insertTable(table, function, lvlOrder, lvlLetter));
                }
            } else
                createLvl(lvl, current + 1, table, root.getLeft());

            if (root.getRight() == null) {
                String function = DNF.substituteVariable(true, root.getLetter(), root.getbFunction(), root.getOrder());
                if (function.equals("0")) {
                    root.setRight(ZERO);
                } else if (function.equals("1")) {
                    root.setRight(ONE);
                } else if (!function.contains(lvlLetter)) {
                    root.setRight(null);
                } else {
                    root.setRight(insertTable(table, function, lvlOrder, lvlLetter));
                }
            } else {
                createLvl(lvl, current + 1, table, root.getRight());
            }
        }
    }

    private TreeNode insertTable(NodeObject[] table, String bFunction, String order, String letter) {
        if (bFunction.equals("1"))
            return ONE;
        else if (bFunction.equals("0"))
            return ZERO;

        BigInteger newNodeHash = DNF.hashCode(bFunction, order);

        // Search if there is existing Node with such a Hashcode, then return exsisting
        // one
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i].getHash().equals(newNodeHash)
                    && table[i].getNode().getbFunction().equals(bFunction)) {
                return table[i].getNode();
            }
        }

        // Or creates a new one and return it
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                this.nodeCount++;
                table[i] = new NodeObject(newNodeHash, new TreeNode(bFunction, letter, order));
                return table[i].getNode();
            }
        }
        return null;

    }


    public void printTree() {
        for (int i = 1; i <= this.order.length(); i++) {
            printLvl(i, 1, this.root);
            System.out.println("\n-------------------------------");
        }
        System.out.println(
                "\t" + "[" + BinaryTree.ZERO.getbFunction() + "]" + "\t" + "[" + BinaryTree.ONE.getbFunction() + "]");

        int nodesMaxCount = ((int) (Math.pow(2, this.order.length())));
        float count = this.nodeCount;
        double reductionRate = (count / nodesMaxCount);
        System.out.println("Count of node in the Tree      : " + this.nodeCount);
        System.out.println("Count of node without reduction: " + nodesMaxCount);
        System.out.println("Reduction efficiency: " + reductionRate);
        System.out.println("================================================================================");
    }

    private void printLvl(int lvl, int current, TreeNode root) {
        if (lvl == current) {
            System.out.print("[" + root.getbFunction() + "] ");
        } else {
            if (root.getbFunction().equals("1") || root.getbFunction().equals("0"))
                return;

            printLvl(lvl, current + 1, root.getLeft());
            printLvl(lvl, current + 1, root.getRight());
        }
    }

    // bdd_use
    public char bddUse(String arguments, TreeNode root) {
        char result = '-';
        if (root.getbFunction().equals("1"))
            return '1';
        else if (root.getbFunction().equals("0"))
            return '0';
        else {
            int diff = arguments.length() - root.getOrder().length();
            if (arguments.charAt(diff) == '1') {
                result = bddUse(arguments.substring(1), root.getRight());
            } else if (arguments.charAt(diff) == '0') {
                result = bddUse(arguments.substring(1), root.getLeft());
            }
        }

        return result;

    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String newOrder) {
        order = newOrder;
    }

}
