package org.mai.grubenkom.lab3;

public class Main {
    public static void main(String[] args) {
        TokenSearcher tokenSearcher = new TokenSearcher();
        tokenSearcher.search("src/main/resources/lorem.txt", " SON-L", 0, 0);
    }
}
