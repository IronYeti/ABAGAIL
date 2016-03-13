package opt.test;

import java.util.Arrays;

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
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;
import shared.ThresholdTrainer;
import shared.ConvergenceTrainer;
/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksTest {
//    /** The n value */
//    private static final int N = 50;
    /** The t value */
//    private static final int T = N / 10;
//    /** Iterations */
    private static final int iter = 200000;
    
    public static void main(String[] args) {
//        System.out.println("Starting ...");
        System.out.println("N\tRHC\tSA\tGA\tMIMIC");
    	for (int N = 10; N <= 70; N += 10) {
//    		for (int repetition = 0; repetition < 10; repetition += 1) {
        		int T = N / 10;
        		int[] ranges = new int[N];
		        Arrays.fill(ranges, 2);
		        EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
		        Distribution odd = new DiscreteUniformDistribution(ranges);
		        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
		        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
		        CrossoverFunction cf = new SingleCrossOver();
		        Distribution df = new DiscreteDependencyTree(.1, ranges); 
		        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
		        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
		        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
		        
		        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
		      FixedIterationTrainer fit = new FixedIterationTrainer(rhc, iter, N); // 200000
		      fit.train();
//		        ThresholdTrainer tt = new ThresholdTrainer(rhc);
//		        ConvergenceTrainer tt = new ConvergenceTrainer(rhc);
//		        tt.train();
//		        int RHCresult = (int)(ef.value(rhc.getOptimal()));
		        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
//		        int RHCiter = tt.getIterations();
		
		        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
		        fit = new FixedIterationTrainer(sa, iter, N); // 200000
		        fit.train();
//		        tt = new ThresholdTrainer(sa);
//		        tt = new ConvergenceTrainer(sa);
//		        tt.train();
//		        int SAresult = (int)(ef.value(sa.getOptimal()));
		//        System.out.println("SA: " + ef.value(sa.getOptimal()));
//		        int SAiter = tt.getIterations();
		        
		        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
//		        fit = new FixedIterationTrainer(ga, 1000); // 1000
//		        fit.train();
//		        tt = new ThresholdTrainer(ga);
//		        tt = new ConvergenceTrainer(ga);
//		        tt.train();
//		        int GAresult = (int)(ef.value(ga.getOptimal()));
		//        System.out.println("GA: " + ef.value(ga.getOptimal()));
//		        int GAiter = tt.getIterations();
		        
		        MIMIC mimic = new MIMIC(200, 20, pop);
//		        fit = new FixedIterationTrainer(mimic, 1000); // 1000
//		        fit.train();
//		        tt = new ThresholdTrainer(mimic);
//		        tt = new ConvergenceTrainer(mimic);
//		        tt.train();
//		        int max_score = 0;
//		        switch(N) {
//			        case (10): max_score = 18;
//			        case (20): max_score = 37;
//			        case (30): max_score = 56;
//			        case (40): max_score = 75;
//			        case (50): max_score = 94;
//			        case (60): max_score = 113;
//			        case (70): max_score = 132;
//			        case (80): max_score = ;
//			        case (90): max_score = 18;
//			        case (100): max_score = 18;
		        }
//		        int RHCiter = 0;
//		        int RHCresult = 0;
//		        while (RHCresult < max_score) {
//		        	RHCiter += 1;
//		        	RHCresult = (int)(ef.value(rhc.getOptimal()));
//		        	if (RHCiter > 1000000) {
//		        		System.out.println("Maxed out for RHC");
//		        		break;
//		        	}
//		        }

//		        int SAiter = 0;
//		        int SAresult = 0;
//		        while (SAresult < max_score) {
//		        	SAiter += 1;
//		        	SAresult = (int)(ef.value(sa.getOptimal()));
//		        	System.out.println("SA = " + SAresult);
//		        	if (SAiter > 1000000) {
//		        		System.out.println("Maxed out for SA");
//		        		break;
//		        	}
//		        }

//		        int GAiter = 0;
//		        int GAresult = 0;
//		        while (GAresult < max_score) {
//		        	GAiter += 1;
//		        	GAresult = (int)(ef.value(ga.getOptimal()));
//		        	if (GAiter > 1000000) {
//		        		System.out.println("Maxed out for GA");
//		        		break;
//		        	}
//		        }

//		        int MIMICiter = 0;
//		        int MIMICresult = 0;
//		        while (MIMICresult < max_score) {
//		        	MIMICiter += 1;
//		        	MIMICresult = (int)(ef.value(mimic.getOptimal()));
//		        	if (MIMICiter > 1000) {
//		        		System.out.println("Maxed out for MIMIC");
//		        		break;
//		        	}
//		        }
//		        System.out.println("Max Iterations for MIMIC = " + MIMICiterations);

		        //?		        int MIMICresult = (int)(ef.value(mimic.getOptimal()));
//		        int MIMICiter = tt.getIterations();
//		        case (N = 10):
//		        	if MIMICresult < 18)
		        
		//        System.out.println("The 4 Peaks Optimization values:");
		//        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
		//        System.out.println("SA: " + ef.value(sa.getOptimal()));
		//        System.out.println("GA: " + ef.value(ga.getOptimal()));
		//        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
		
//		        System.out.println(N + "\t" + repetition + "\t" + RHCiter + "\t"+ SAiter + "\t"+ GAiter + "\t"+ MIMICiter);
//		        System.out.println(N + "\t" + repetition + "\t" + RHCresult + "\t"+ SAresult + "\t"+ GAresult + "\t"+ MIMICresult);
//		        System.out.println(N + "\t" + RHCresult + "\t"+ SAresult + "\t"+ GAresult + "\t"+ MIMICresult);
//		        System.out.println(N + "\t" + RHCiter + "\t"+ SAiter + "\t"+ GAiter + "\t"+ MIMICiter);
        	}
//       	}
//    }
}
