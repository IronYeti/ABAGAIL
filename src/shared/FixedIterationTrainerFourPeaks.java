package shared;

/**
 * A fixed iteration trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FixedIterationTrainerFourPeaks implements Trainer {
    
    /**
     * The inner trainer
     */
    private Trainer trainer;
    
    /**
     * The number of iterations to train
     */
    private int iterations;
    
    /**
     * Make a new fixed iterations trainer
     * @param t the trainer
     * @param iter the number of iterations
     */
    private int n;
    private int act_iterations;
    private int cur_score;
    
    public FixedIterationTrainerFourPeaks(Trainer t, int iter) {
        trainer = t;
        iterations = iter;
    }

    public FixedIterationTrainerFourPeaks(Trainer t, int iter, int N) {
        trainer = t;
        iterations = iter;
        n = N;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
//        double sum = 0;
        double val = 0;
        cur_score = 0;
        int max_score = 0;
        act_iterations = 0;
        switch(n) {
	        case 10: max_score = 18; break;
	        case 20: max_score = 37; break;
	        case 30: max_score = 56; break;
	        case 40: max_score = 75; break;
	        case 50: max_score = 94; break;
	        case 60: max_score = 113; break;
	        case 70: max_score = 132; break;
	        case 80: max_score = 151; break;
	        case 90: max_score = 170; break;
	        case 100: max_score = 189; break;
        	default: break;
	    }

//        System.out.println("training for N = " + n + " & max_score = " + max_score);

        for (int i = 0; i < iterations; i++) {
        	val = trainer.train();
        	act_iterations += 1;
//            sum += val;
        	if (val > cur_score) {
        		cur_score = (int)val;
//        		System.out.println("cur_score bumped to " + cur_score);
        	}
        	if (cur_score >= max_score) {
//        		System.out.println("Best score found early");
        		break;
        	}
        }
        
//        System.out.println("cur_score = " + cur_score + ", act_iterations = " + act_iterations);
        
		return cur_score;
//        return sum / iterations;
    }
    
    public int getIterations() {
//    	System.out.println("Iterations = " + iterations);
        return act_iterations;
    }

    public int getBestScore() {
//    	System.out.println("Iterations = " + iterations);
        return cur_score;
    }

}
