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
    /** The n value */
    private static final int N = 70;
    /** The t value */
    private static final int T = N / 10;
    /** Iterations */
    private static final int iter = 200000;
    
    public static void main(String[] args) {
        System.out.println("N: " + N);
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
//      FixedIterationTrainer fit = new FixedIterationTrainer(rhc, iter); // 200000
//      fit.train();
//        ThresholdTrainer tt = new ThresholdTrainer(rhc);
        ConvergenceTrainer tt = new ConvergenceTrainer(rhc);
        tt.train();
//        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
        System.out.println("RHC: " + tt.getIterations());
        
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
//      fit = new FixedIterationTrainer(sa, iter); // 200000
//      fit.train();
//        tt = new ThresholdTrainer(sa);
        tt = new ConvergenceTrainer(sa);
        tt.train();
//        System.out.println("SA: " + ef.value(sa.getOptimal()));
        System.out.println("SA: " + tt.getIterations());
        
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
//        fit = new FixedIterationTrainer(ga, iter); // 1000
//        fit.train();
//        tt = new ThresholdTrainer(ga);
        tt = new ConvergenceTrainer(ga);
        tt.train();
//        System.out.println("GA: " + ef.value(ga.getOptimal()));
        System.out.println("GA: " + tt.getIterations());
        
        MIMIC mimic = new MIMIC(200, 20, pop);
//        fit = new FixedIterationTrainer(mimic, iter); // 1000
//        fit.train();
//        tt = new ThresholdTrainer(mimic);
        tt = new ConvergenceTrainer(mimic);
        tt.train();
//        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
        System.out.println("MIMIC: " + tt.getIterations());
    }
}
