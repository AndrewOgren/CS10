import java.util.Comparator;

public class TreeComparator implements Comparator<BinaryTree<Data>> {

	@Override
	public int compare(BinaryTree<Data> o1, BinaryTree<Data> o2) throws ClassCastException {
		if ((o1.getData().getFrequency() < o2.getData().getFrequency())){
			return -1;
		}
		else if(o1.getData().getFrequency() > o2.getData().getFrequency()){
			return 1;	
		}
		else {
			return 0;
		}
	}
	
}
