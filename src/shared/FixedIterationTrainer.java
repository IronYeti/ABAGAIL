package shared;

/**
 * A fixed iteration trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FixedIterationTrainer implements Trainer {
    
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

    public FixedIterationTrainer(Trainer t, int iter, int N) {
        trainer = t;
        iterations = iter;
        n = N;
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        double sum = 0;
        double val = 0;
        int cur_score = 0;
        int max_score = 0;

        switch(n) {
	        case (10): max_score = 18;
	        case (20): max_score = 37;
	        case (30): max_score = 56;
	        case (40): max_score = 75;
	        case (50): max_score = 94;
	        case (60): max_score = 113;
	        case (70): max_score = 132;
	        case (80): max_score = 151;
	        case (90): max_score = 170;
	        case (100): max_score = 189;
	    }

        for (int i = 0; i < iterations; i++) {
        	val = trainer.train();
//            sum += val;
        	if (val > cur_score) {
        		cur_score = (int)val;
        	}
        	if (cur_score >= max_score) {
        		break;
        	}
        }
        
        System.out.println("cur_score = " + cur_score + ", iterations = " + iterations);
        
		return cur_score;
//        return sum / iterations;
    }
    
    public int getIterations() {
//    	System.out.println("Iterations = " + iterations);
        return iterations;
    }

}
