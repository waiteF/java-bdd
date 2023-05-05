package com.example;

public class App {
    private static final String B_FUNCTION = "BCU!G!Q!Q!B!LM+LD!NGNSXKQ+LS!X!SIIL!W+JW+WLBUWGU!G!DU!WJ+!XJ+X!JK!D+JGW+XMMU!J!MJKQWS!BWC+U!WEJ!EUXE+BGJDJ+KLGEJ+ND!UD!UB+KEWUCEGEDDDC+!X!QB!BE+L!W!DIN!WXU!Q+!S!JUD!Q+CSE+BBLWIQIB+W!WUB!N!JXWJ!I";
    private static final String ORDER = "EKLJQWSBMNGXUCDI";

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree(B_FUNCTION, ORDER);
        tree.printTree();
    }
}
