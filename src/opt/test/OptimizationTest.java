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
	private Thread t;
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
			}
			HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
			GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef,odd,mf,cf);
			ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef,odd,df);

			String results = "";
			double optimal_value = -1;
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void start() {
		if (t == null)
		{
			t = new Thread (this);
			t.start();
		}
	}
}
