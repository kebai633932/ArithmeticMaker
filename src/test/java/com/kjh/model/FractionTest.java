package com.kjh.model;

import org.junit.Test;

public class FractionTest {
    @Test
    public void main() {
        CreateFraction createFraction = new CreateFraction();

        for (int i = 0; i < 100; i++) {
            createFraction.createFractionProblem(100);
        }
    }
}
