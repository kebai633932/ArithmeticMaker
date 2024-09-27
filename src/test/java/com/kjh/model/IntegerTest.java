package com.kjh.model;

import org.junit.Test;

public class IntegerTest {

    @Test
    public void main() {
        CreateInteger createInteger = new CreateInteger();
        for(int i = 0; i < 100; i++) {
            createInteger.createIntegerProblem(100);
        }
    }
}
