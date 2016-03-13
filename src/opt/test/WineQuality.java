package opt.test;

import dist.*;
import opt.*;
import opt.example.*;
import opt.ga.*;
import shared.*;
import func.nn.backprop.*;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights to a neural network that is classifying red wine quality being from 1 to 10 
 * (1=worst 10 = best) 
 *
 * by Asher Cornelius
 * original @author Hannah Lau
 * @version 1.0
 */
public class WineQuality {
    private static Instance[] instances = initializeInstances();
    private static Instance[] instances_test = initializeInstancesTest();

    //private static int inputLayer = 11, hiddenLayer = 6, outputLayer = 1, trainingIterations = 1000;
//    private static int inputLayer = 15, hiddenLayer = 7, outputLayer = 1, trainingIterations = 100;
    private static int networkCount = 3;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    
    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(instances);
    private static DataSet set_test = new DataSet(instances_test);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[networkCount];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[networkCount];
    private static String[] oaNames = {"RHC", "SA", "GA"};
    private static String results = "";
    private static int PA_display_qty = 0;
    private static DecimalFormat df = new DecimalFormat("0.000");
    private static Boolean Debug = false;
    public static void main(String[] args) {

        int inputLayer = 11, hiddenLayer = 6, outputLayer = 1; //, trainingIterations = 100;

        for(int trainingIterations = 600; trainingIterations<=1000; trainingIterations += 100) {
        	for (int repetitions = 0; repetitions < 10; repetitions += 1) {
	        	for(int i = 0; i < oa.length; i++) {
		            networks[i] = factory.createClassificationNetwork(
		                new int[] {inputLayer, hiddenLayer, outputLayer});
		            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
		        }
		
		        oa[0] = new RandomizedHillClimbing(nnop[0]);
		        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
		        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);
		
//		    	System.out.println("\nTraining iterations: " + trainingIterations);
		    	results = "";
		        for(int i = 2; i < 3; i++) {
//		        for(int i = 0; i < oa.length; i++) {
		            if (Debug) {
		            	System.out.print("Algorithm: " + oaNames[i] + " - Training the NN...");
		            };
		            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
		            train(oa[i], networks[i], oaNames[i], trainingIterations); //trainer.train();
		        	if (Debug) {
		        		System.out.println(" Complete...");
		        	}
		            end = System.nanoTime();
		            trainingTime = end - start;
		            trainingTime /= Math.pow(10,9);
		
		            Instance optimalInstance = oa[i].getOptimal();
		            networks[i].setWeights(optimalInstance.getData());
		
		            double predicted, actual;
//		            System.out.println("Evaluating the Training data...");
//		            start = System.nanoTime();
//		            for(int j = 0; j < instances.length; j++) {
//		                networks[i].setInputValues(instances[j].getData());
//		                networks[i].run();
//		
//		                actual = Double.parseDouble(instances[j].getLabel().toString()); // was 'predicted'
//		                predicted = Double.parseDouble(networks[i].getOutputValues().toString()); // was 'actual'
//		                if (Debug && j < PA_display_qty) {
//		                	System.out.println("Predicted & Actual =  " + predicted + " & " + actual);
//		                };
//		                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
//		            }
//		            end = System.nanoTime();
//		            testingTime = end - start;
//		            testingTime /= Math.pow(10,9);
//		            results = "Train Results: "
//		            		+ trainingIterations + "\t" + oaNames[i] + "\t" 
//		                    + df.format(correct/(correct+incorrect)*100) + "%\t"
//		                    + df.format(trainingTime) + "\t"
//		                    + df.format(testingTime);
//		            System.out.println(results);

		            /*
		              now see how the TEST data performs
		            */
//		            System.out.println("Evaluating the TEST data...");
		            start = System.nanoTime();
		            for(int j = 0; j < instances_test.length; j++) {
		                networks[i].setInputValues(instances_test[j].getData());
		                networks[i].run();
		
		                actual = Double.parseDouble(instances_test[j].getLabel().toString()); // was 'predicted'
		                predicted = Double.parseDouble(networks[i].getOutputValues().toString()); // was 'actual'
//		                if (Debug && j < PA_display_qty) {
//		                	System.out.println("Predicted & Actual =  " + predicted + " & " + actual);
//		                };
		                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
		            }
		            end = System.nanoTime();
		            testingTime = end - start;
		            testingTime /= Math.pow(10,9);

//		            System.out.println();
		            results = oaNames[i] + "\t"
		            		+ trainingIterations + "\t"
		            		+ (repetitions + 1) + "\t"
		                    + df.format(correct/(correct+incorrect)*100) + "%\t"
		                    + df.format(trainingTime) + "\t"
		                    + df.format(testingTime);
		            System.out.println(results);
		        } // end i
	        } // end repetitions
        }  // end training iterations

//        System.out.println(results);
//        System.out.println(results);
    }

    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName, int trainingIterations) {
//        System.out.println("\nError results for " + oaName + "\n---------------------------");

        for(int i = 0; i < trainingIterations; i++) {
            oa.train();

            double error = 0;
            for(int j = 0; j < instances.length; j++) {
                network.setInputValues(instances[j].getData());
                network.run();

                Instance output = instances[j].getLabel(), example = new Instance(network.getOutputValues());
                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                error += measure.value(output, example);
            }
//            System.out.println(df.format(error));
//            System.out.print(".");

//            System.out.println("Error\t" + i + "\t" + error);
        }
    }

    private static Instance[] initializeInstances() {

    	System.out.println("Processing training data...");
    	int attributeCount = 11;
        int instanceCount = 1279;
        double[][][] attributes = new double[instanceCount][][];

        try {
//            BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/winequality-red.txt")));
            BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/winequality-red-train-normalized.txt")));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[attributeCount]; // 11 attributes
                attributes[i][1] = new double[1];

                for(int j = 0; j < attributeCount; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                attributes[i][1][0] = Double.parseDouble(scan.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            // classifications range from 0 to 30; split into 0 - 14 and 15 - 30
            instances[i].setLabel(new Instance(attributes[i][1][0] < 6 ? 0 : 1));
            //instances[i].setLabel(new Instance((int)(attributes[i][1][0])));
        }

        return instances;
    }
    
    private static Instance[] initializeInstancesTest() {

    	System.out.println("Processing testing data...");
        int attributeCount = 11;
        int instanceCount = 320;
        double[][][] attributes_test = new double[instanceCount][][];

        try {
//            BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/winequality-red-test.txt")));
            BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/winequality-red-test-normalized.txt")));
            //BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/creditapp-TRAIN.txt")));

            for(int i = 0; i < attributes_test.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes_test[i] = new double[2][];
                attributes_test[i][0] = new double[attributeCount]; // 11 attributes
                attributes_test[i][1] = new double[1];

                for(int j = 0; j < attributeCount; j++)
                    attributes_test[i][0][j] = Double.parseDouble(scan.next());

                attributes_test[i][1][0] = Double.parseDouble(scan.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances_test = new Instance[attributes_test.length];

        for(int i = 0; i < instances_test.length; i++) {
            instances_test[i] = new Instance(attributes_test[i][0]);
            // classifications range from 0 to 30; split into 0 - 14 and 15 - 30
            instances_test[i].setLabel(new Instance(attributes_test[i][1][0] < 6 ? 0 : 1));
            //instances[i].setLabel(new Instance((int)(attributes[i][1][0])));
        }

        return instances_test;
    }
}
