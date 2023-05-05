package com.example;

public class TreeNode {
    private String bFunction;
    private String letter;
    private TreeNode right;
    private TreeNode left;
    private String order;

    TreeNode(String bFunc, String letter, String order) {
        this.letter = letter;
        this.bFunction = bFunc;
        this.right = null;
        this.left = null;
        this.order = order;
    }


    public String getbFunction() {
        return bFunction;
    }

    public void setbFunction(String bFunction) {
        this.bFunction = bFunction;
    }


    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }


    public TreeNode getRight() {
        return right;
    }


    public void setRight(TreeNode right) {
        this.right = right;
    }


    public TreeNode getLeft() {
        return left;
    }


    public void setLeft(TreeNode left) {
        this.left = left;
    }


    public String getOrder() {
        return order;
    }


    public void setOrder(String order) {
        this.order = order;
    }
}
