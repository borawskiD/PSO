package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Task {
    public final int MIN = -100;
    public final int MAX = 100;
    public final int SIZE_OF_POPULATION = 100;
    public final Double ALPHA = 0.5;
    public final Double BETA = 0.5;
    public final Double SIGMA = 0.2;
    private final double EPSILON = 0.0000000000001;
    private int numberOfIterations = 0;
    private Double[][] speed = new Double[SIZE_OF_POPULATION][4];
    private Double[][] prevSpeed = new Double[SIZE_OF_POPULATION][4];
    private ArrayList<Double[]> xTop = new ArrayList<>(SIZE_OF_POPULATION);
    private ArrayList<Double[]> population;
    private Double[] G;
    public void run(){
        population = generatePopulation(SIZE_OF_POPULATION);
        initValues();
        do{
            System.out.println("Iteration number: " + (numberOfIterations+1));
            G = findGPoint();
            moveSamples();
            findXTop();
            numberOfIterations++;
            System.out.println("---");
        }while (!timeToStop());

    }


    public boolean timeToStop(){
        boolean stop = true;
        for(int i = 0; i<100; i++){
            Double[] current = speed[i];
            Double[] previous = prevSpeed[i];
            if((Math.abs(current[0] - previous[0]) > EPSILON) || (Math.abs(current[1] - previous[1]) > EPSILON) || (Math.abs(current[2] - previous[2]) > EPSILON) || (Math.abs(current[3] - previous[3]) > EPSILON)){
                stop = false;
                break;
            }
        }
        return stop;
    }
    private Double[][] copySpeed(Double[][] speed){
        Double[][] output = new Double[100][4];
        for(int i = 0; i<100; i++){
            output[i] = speed[i].clone();
        }
        return output;
    }
    private void moveSamples(){
        prevSpeed = copySpeed(speed);
        for(int i = 0; i< SIZE_OF_POPULATION; i++){
            generateVector(i);
        }
    }
    private void generateVector(int index){
        Double[] vector = new Double[4];
        Double[] newPoint = new Double[4];
        for(int i = 0; i<4; i++){
            if(numberOfIterations == 0){
                vector[i] = speed[index][i] + ALPHA  * (G[i] - population.get(index)[i]) + BETA  * (xTop.get(index)[i] - population.get(index)[i]);
                newPoint[i] = population.get(index)[i] + vector[i];
            } else{
                vector[i] = SIGMA * speed[index][i] + ALPHA  * (G[i] - population.get(index)[i]) + BETA  * (xTop.get(index)[i] - population.get(index)[i]);
                newPoint[i] = population.get(index)[i] + vector[i];
            }
        }
        population.set(index,newPoint);
        speed[index] = vector;
    }
    private void initValues(){
        //set the first values of x* to first samples (because that's the best values) and zeros for V (initial speed)
        xTop.addAll(population);
        for (int i = 0; i < SIZE_OF_POPULATION; i++) {
            speed[i][0] = 0.0;
            speed[i][1] = 0.0;
            speed[i][2] = 0.0;
            speed[i][3] = 0.0;
        }
    }
    private void findXTop(){
        for(int i = 0; i<population.size(); i++){
            if (F(xTop.get(i)) > F(population.get(i))){
                xTop.set(i, population.get(i));
            }
        }
    }
    private Double[] findGPoint(){
        Double min = F(xTop.get(0));
        Double[] bestSample = xTop.get(0);
        for(int i = 1; i < xTop.size(); i++){
            if(min > F(xTop.get(i))){
                bestSample = xTop.get(i);
                min = F(xTop.get(i));
            }
        }
        System.out.println("Current minimum = " + min + " for sample:");
        displaySample(bestSample);
        return bestSample;
    }
    private Double F(Double[] sample){
        Double x1 = sample[0];
        Double x2 = sample[1];
        Double x3 = sample[2];
        Double x4 = sample[3];
        return Math.pow(x1,3) - 2 * Math.pow(x1,2) * Math.pow(x2,3) + 3 * Math.pow(x2,3) * Math.pow(x3,2) - 3 * Math.pow(x3,2) + 4 * x1*x2*Math.pow(x4,3) - 2 * x2 * Math.pow(x3,2) + 10;
    }
    private ArrayList<Double[]> generatePopulation(int SIZE){
        ArrayList<Double[]> output = new ArrayList<>();
        for(int i = 0; i<SIZE; i++){
            output.add(generateSample());
            displaySample(output.get(i));
        }
        return output;
    }
    private void displaySample(Double[] sample){
        for (Double aDouble : sample) {
            System.out.print(aDouble + " ");
        }
        System.out.println("");
    }
    private Double[] generateSample(){
        Random r = new Random();
        double x1 = MIN + (MAX - MIN) * r.nextDouble();
        double x2 = MIN + (MAX - MIN) * r.nextDouble();
        double x3 = MIN + (MAX - MIN) * r.nextDouble();
        double x4 = MIN + (MAX - MIN) * r.nextDouble();
        return new Double[]{x1,x2,x3,x4};
    }
}
