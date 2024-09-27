package com.kjh.controller;

import org.junit.Test;
import java.io.IOException;

public class ProducerControllerTest {
    @Test
    public void testGenerateArithmeticProblem() throws IOException {
        ProducerController producerController = new ProducerController();
        producerController.generateArithmeticProblem(10, 100);
    }
}
