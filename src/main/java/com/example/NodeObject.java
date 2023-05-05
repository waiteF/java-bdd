package com.example;

import java.math.BigInteger;


public class NodeObject {
    private BigInteger hash;
    private TreeNode node;

    NodeObject(BigInteger key, TreeNode value) {
        this.setHash(key);
        this.setNode(value);
    }


    public TreeNode getNode() {
        return node;
    }


    public void setNode(TreeNode node) {
        this.node = node;
    }


    public BigInteger getHash() {
        return hash;
    }

    public void setHash(BigInteger key) {
        this.hash = key;
    }
}
