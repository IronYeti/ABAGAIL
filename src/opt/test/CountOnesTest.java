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
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainerCountOnes;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class CountOnesTest {
    /** The n value */
//    private static final int N = 80;
    private static final int iter = 10000;
    
    public static void main(String[] args) {
		System.out.println("N\tRHCs\tRHCi\tSAs\tSAi\tGAs\tGAi\tMIMICs\tMIMICi");
    	for (int N = 10; N <= 100; N += 10) {
    		int[] ranges = new int[N];
	        Arrays.fill(ranges, 2);
	        EvaluationFunction ef = new CountOnesEvaluationFunction();
	        Distribution odd = new DiscreteUniformDistribution(ranges);
	        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
	        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
	        CrossoverFunction cf = new UniformCrossOver();
	        Distribution df = new DiscreteDependencyTree(.1, ranges); 
	        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
	        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
	        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
	        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
	        FixedIterationTrainerCountOnes fit = new FixedIterationTrainerCountOnes(rhc, iter, N);
	        fit.train();
	//        System.out.println(ef.value(rhc.getOptimal()));
//	        System.out.println(N + "\t" + "RHC:\t" + fit.getBestScore() + "\t" + fit.getIterations());
	        int RHCscore = fit.getBestScore();
	        int RHCiter  = fit.getIterations();
	        
	        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
	        fit = new FixedIterationTrainerCountOnes(sa, iter, N);
	        fit.train();
	//        System.out.println(ef.value(sa.getOptimal()));
//	        System.out.println(N + "\t" + "SA:\t" + fit.getBestScore() + "\t" + fit.getIterations());
	        int SAscore = fit.getBestScore();
	        int SAiter  = fit.getIterations();
	        
	        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(20, 20, 0, gap);
	        fit = new FixedIterationTrainerCountOnes(ga, iter, N);
	        fit.train();
	//        System.out.println(ef.value(ga.getOptimal()));
//	        System.out.println(N + "\t" + "GA:\t" + fit.getBestScore() + "\t" + fit.getIterations());
	        int GAscore = fit.getBestScore();
	        int GAiter  = fit.getIterations();
	        
	        MIMIC mimic = new MIMIC(50, 10, pop);
	        fit = new FixedIterationTrainerCountOnes(mimic, iter/10, N);
	        fit.train();
	//        System.out.println(ef.value(mimic.getOptimal()));
//	        System.out.println(N + "\t" + "MIMIC:\t" + fit.getBestScore() + "\t" + fit.getIterations());
	        int MIMICscore = fit.getBestScore();
	        int MIMICiter  = fit.getIterations();

    		System.out.println(N + "\t" + RHCscore + "\t" + RHCiter + "\t"
    				+ SAscore + "\t" + SAiter + "\t"
    				+ GAscore + "\t" + GAiter + "\t"
    				+ MIMICscore + "\t" + MIMICiter);

    	}
	}
}