package hr.fer.zemris.java.custom.collections;

public class ArrayBackendIndexedCollection {
	private int size;
	private int capacity;
	private int defaultCapacity = 16;
	private Object[] elements;
	
	public ArrayBackendIndexedCollection() {
		elements = new Object[defaultCapacity];
		capacity = defaultCapacity;
		size = 0;
	}
	
	public ArrayBackendIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException("Argument > 1");
		} else {
			elements = new Object[initialCapacity];
			capacity = initialCapacity;
			size = 0;
		}
	}
	
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}	
	}
	
	public int size() {
		return size;
	}
	
	private void resize() {
		capacity = capacity * 2;
		Object[] tmp = new Object[capacity];
System.out.println(tmp.length);	
		for(int i = 0; i < size; i++) {
			tmp[i] = elements[i];
		}
		elements = tmp;
	}
	public void add(Object value) {
		if(value == null) {
			throw new IllegalArgumentException("Null");
		}
		
		if(size == capacity) {
			resize();
		}
		elements[size] = value;
		size++;
	}
	
	public Object get(int index) {
		if(index > (size)) {
			throw new IndexOutOfBoundsException();
		}
		return elements[index];
	}
	
	public void remove(int index) {
		if(index > size || index < 0 ) {
			throw new IndexOutOfBoundsException();
		}
		
		for(int i = index; i < size; i++) {
			elements[i] = elements[i+1];
		}
		elements[size] = null;
		size--;
	}
	
	public void insert(Object value, int position) {
		if (position < 0 || position > size + 1) {
			throw new IndexOutOfBoundsException();
		}
		resize();
		
		for(int i = size + 1; i > position; i--) {
			elements[i] = elements[i-1];
		}
		elements[position] = value;
	}
	
	public int indexOf(Object value) {
		for (int i = 0; i <= size; i++) {
			if(elements[i].equals(value)) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean contains(Object value) {
		boolean flag = false;
		for (int i = 0; i < size; i++) {
			if(elements[i].equals(value)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public void clear() {
		for(int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
	}	
}
