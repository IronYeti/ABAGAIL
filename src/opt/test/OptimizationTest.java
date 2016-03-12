package opt.test;

import java.util.Arrays;
import java.util.HashMap;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.SingleCrossOver;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 *
 * 
 */

class Analyze_Optimization_Test implements Runnable {
//	private Thread t;
	private String problem;
	private String algorithm;
	private int iterations;
	private HashMap<String, Double> params;
	
	Analyze_Optimization_Test(String problem, String algorithm, int iterations, HashMap<String, Double> params) {
		this.problem = problem;
		this.algorithm = algorithm;
		this.iterations = iterations;
		this.params = params;
	}

	public void run() {
		try {
			EvaluationFunction ef = null;
			Distribution odd = null;
			NeighborFunction nf = null;
			MutationFunction mf = null;
			CrossoverFunction cf = null;
			Distribution df = null;
			int[] ranges = null;
			switch (this.problem) {
				case "count_ones":
					ranges = new int[this.params.get("N").intValue()];
					Arrays.fill(ranges, 2);
					ef = new CountOnesEvaluationFunction();
					odd = new DiscreteUniformDistribution(ranges);
					nf = new DiscreteChangeOneNeighbor(ranges);
					mf = new DiscreteChangeOneMutation(ranges);
					cf = new UniformCrossOver();
					df = new DiscreteDependencyTree(.1, ranges);
					break;
				case "four_peaks":
					ranges = new int[this.params.get("N").intValue()];
					Arrays.fill(ranges, 2);
					ef = new FourPeaksEvaluationFunction(this.params.get("T").intValue());
					odd = new DiscreteUniformDistribution(ranges);
					nf = new DiscreteChangeOneNeighbor(ranges);
					mf = new DiscreteChangeOneMutation(ranges);
					cf = new SingleCrossOver();
					df = new DiscreteDependencyTree(.1, ranges);
					break;
			}
			HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
			GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef,odd,mf,cf);
			ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef,odd,df);

			String results = "";
			double optimal_value = -1;
			
			switch (this.algorithm) {
				case ("RHC"):
					RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
//					for (int i = 0; i <= this.iterations; i++) {
//						results += rhc.train() + "\n";
//					}
					optimal_value = ef.value(rhc.getOptimal());
					break;
				case ("SA"):
					SimulatedAnnealing sa = new SimulatedAnnealing(
							params.get("SA_initial_temperature"),
							params.get("SA_cooling_factor"),
							hcp
					);
//					for (int i = 0; i <= this.iterations; i++) {
//						results += sa.train() + "\n";
//					}
					optimal_value = ef.value(sa.getOptimal());
					break;
				case ("GA"):
					StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(
							params.get("GA_population").intValue(),
							params.get("GA_mate_number").intValue(),
							params.get("GA_mutate_number").intValue(),
							gap
					);
//					for (int i = 0; i <= this.iterations; i++) {
//						results += ga.train() + "\n";
//					}
					optimal_value = ef.value(ga.getOptimal());
					break;
				case ("MIMIC"):
					MIMIC mimic = new MIMIC(
							params.get("MIMIC_samples").intValue(),
							params.get("MIMIC_to_keep").intValue(),
							pop
					);
					results = "";
//					for (int i = 0; i <= this.iterations; i++) {
//						results += mimic.train() + "\n";
//					}
					optimal_value = ef.value(mimic.getOptimal());
					break;
			}
//			results += "\n" +
//					"Problem: " + this.problem + "\n" +
//					"Algorithm: " + this.algorithm + "\n" +
//					"Iterations: " + this.iterations + "\n" +
//					"Optimal Value: " + optimal_value + "\n" +
//					"";
			results += //"\n" +
					this.problem + "\t" + this.algorithm + 
					"\t" + this.iterations + "\t" + this.params.get("N").intValue() + "\t" +
					optimal_value + "";
			System.out.println(results);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
//	public void start() {
//		if (t == null)
//		{
//			t = new Thread (this);
//			t.start();
//		}
//	}
}

public class OptimizationTest {
	public static void main(String[] args) {

		System.out.println("problem\talgo\titer\tN\tOptimum\n");

		// Count Ones Test
		HashMap<String, Double> count_one_test_params = new HashMap<>();
		count_one_test_params.put("SA_initial_temperature", 10.);
		count_one_test_params.put("SA_cooling_factor", .95);
		count_one_test_params.put("GA_population", 20.);
		count_one_test_params.put("GA_mate_number", 20.);
		count_one_test_params.put("GA_mutate_number", 5.);
		count_one_test_params.put("MIMIC_samples", 50.);
		count_one_test_params.put("MIMIC_to_keep", 10.);
		
		int[] N = {10,20};
		int[] iterations = {10,20,30};
		String[] algorithms = {"RHC","SA","GA","MIMIC"};
		for (int i = 0; i < algorithms.length; i++) {
			for (int j = 0; j < N.length; j++) {
				count_one_test_params.put("N", (double)N[j]);
				for (int k = 0; k < iterations.length; k++){
					new Analyze_Optimization_Test(
							"count_ones",
							algorithms[i],
							iterations[k],
							count_one_test_params
//					).start();
					).run();
				}
			}
		}
		// Four Peaks Test
		HashMap<String, Double> four_peaks_test_params = new HashMap<>();
		four_peaks_test_params.put("SA_initial_temperature", 1E11);
		four_peaks_test_params.put("SA_cooling_factor", .95);
		four_peaks_test_params.put("GA_population", 20.);
		four_peaks_test_params.put("GA_mate_number", 20.);
		four_peaks_test_params.put("GA_mutate_number", 5.);
		four_peaks_test_params.put("MIMIC_samples", 50.);
		four_peaks_test_params.put("MIMIC_to_keep", 10.);
		
		N = new int[] {10,20};
		iterations = new int[] {10,20,30};
		for (int i = 0; i < algorithms.length; i++) {
			for (int j = 0; j < N.length; j++) {
				four_peaks_test_params.put("N", (double)N[j]);
				four_peaks_test_params.put("T", (double)N[j]/5);
				for (int k = 0; k < iterations.length; k++){
					new Analyze_Optimization_Test(
							"four_peaks",
							algorithms[i],
							iterations[k],
							four_peaks_test_params
//					).start();
					).run();
				}
			}
		}
	}
}
