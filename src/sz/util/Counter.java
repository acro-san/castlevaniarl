package sz.util;

import java.io.Serializable;

@Deprecated
public class Counter implements Serializable {

	// It's a frickin int.
	private int value;
	
	public Counter(int initialValue){
		value = initialValue;
	}

	public void reduce() {
		value--;
	}

	public boolean isOver() {
		return value < 0;
	}
	
	public void increase(){
		value++;
	}
	
	public int getCount() {
		return value;
	}
	
	public void reset() {
		value = 0;
	}
}