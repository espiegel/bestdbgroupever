package db;

public class ProcessDetails {
	private volatile boolean flgSet = false;
	private volatile int _min;
	private volatile int _max;
	private volatile int _curr;
	
	public synchronized boolean isSet() {
		return flgSet;
	}
	
	public synchronized void set(int Min, int Max) {
		_curr=_min=Min;
		_max=Max;
		flgSet=true;
	}
	
	// BEGIN: only valid if isSet==true
	public synchronized int getMax() {
		return _max;
	}
	public synchronized int getMin() {
		return _min;
	}
	public synchronized void setCurr(int curr) {
		_curr=curr;
	}
	public synchronized int getCurr() {
		return _curr;
	}
	//END
}
