 package hr.fer.zemris.java.custom.collections;

public class ObjectStack {
	private ArrayBackendIndexedCollection stack = new ArrayBackendIndexedCollection();
	
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	public int size() {
		return stack.size();
	}
	
	public void push(Object value) {
		if(value == null) {
			throw new IllegalArgumentException("Null reference is not allowed on the stack");
		}
		stack.add(value);
	}
	
	public Object pop() {
		int lastElementIndex = stack.size() - 1;
		if(lastElementIndex < 0) {
			throw new EmptyStackException("Stog nema niti jednog elementa");
		}
		
		Object retval = stack.get(lastElementIndex);
		stack.remove(lastElementIndex);
		
		return retval;
	}
	
	public Object peek() {
		int lastElementIndex = stack.size() - 1;
		if(lastElementIndex < 0) {
			throw new EmptyStackException("Stog nema niti jednog elementa");
		}
		return stack.get(lastElementIndex);
	}
	
	void clear() {
		stack.clear();
	}
	
}
