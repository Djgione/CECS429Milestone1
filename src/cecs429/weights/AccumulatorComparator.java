package cecs429.weights;

import java.util.Comparator;

public class AccumulatorComparator implements Comparator<Accumulator> {

	@Override
	public int compare(Accumulator o1, Accumulator o2) {
		// TODO Auto-generated method stub
		if(o1.getaValue() == o2.getaValue()) {
			return 0;
		}
		else if(o1.getaValue() < o2.getaValue()) {
			return 1;
		}
		else {
			return -1;
		}
			
	}

}
