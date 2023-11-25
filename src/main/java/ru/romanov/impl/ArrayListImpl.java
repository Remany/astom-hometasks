package ru.romanov.impl;

import ru.romanov.contract.ListContract;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import java.util.NoSuchElementException;

/**
 * Implementation of the ListContract using an ArrayList-based approach.
 *
 * @param <E> the type of elements in the list
 */
public class ArrayListImpl<E> implements ListContract<E> {
	/**
	 * The default error message for NoSuchElementException when attempting to access an element beyond the iteration.
	 */
	private static final String NO_SUCH_ARGUMENT_EXCEPTION_MESSAGE = "No more elements in the iteration";
	/**
	 * Default capacity for the array.
	 */
	private static final int DEFAULT_CAPACITY = 10;

	/**
	 * An array to store elements of type E.
	 */
	private E[] values;

	/**
	 * The current size of the array, representing the number of elements it contains.
	 */
	private int size;

	/**
	 * Constructs an ArrayList with the default capacity.
	 */
	@SuppressWarnings("unchecked")
	public ArrayListImpl() {
		values = (E[]) new Object[DEFAULT_CAPACITY];
	}

	/**
	 * Constructs an ArrayList with the specified capacity.
	 *
	 * @param capacity the initial capacity of the list
	 * @throws IllegalArgumentException if the capacity is less than or equal to 0
	 */
	@SuppressWarnings("unchecked")
	public ArrayListImpl(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity should be a positive");
		}
		values = (E[]) new Object[capacity];
	}

	private void checkIndex(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	}

	/**
	 * Adds the specified element to the end of the list.
	 *
	 * @param e the element to add
	 */
	@Override
	public void add(E e) {
		if (size() == values.length) {
			E[] newValues = Arrays.copyOf(values, size() * 2);
			System.arraycopy(values, 0, newValues, 0, size());
			values = newValues;
		}
		values[size()] = e;
		size++;
	}

	/**
	 * Adds the specified element at the specified index in the list.
	 *
	 * @param e     the element to add
	 * @param index the index at which to add the element
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public void add(E e, int index) {
		checkIndex(index);

		if (size() == values.length) {
			E[] newValues = Arrays.copyOf(values, size() * 2);
			System.arraycopy(values, 0, newValues, 0, index);
			newValues[index] = e;
			System.arraycopy(values, index, newValues, index + 1, size() - index);
			values = newValues;
		} else {
			if (size() - index >= 0) {
				System.arraycopy(values, index, values, index + 1, size() - index);
			}
			values[index] = e;
		}
	}

	/**
	 * Returns the size of the list.
	 *
	 * @return the size of the list
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Returns the element at the specified index in the list.
	 *
	 * @param index the index of the element to retrieve
	 * @return the element at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public E get(int index) {
		checkIndex(index);
		return values[index];
	}

	/**
	 * Removes the element at the specified index in the list.
	 *
	 * @param index the index of the element to remove
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public void remove(int index) {
		checkIndex(index);

		System.arraycopy(values, index + 1, values, index, size() - index - 1);
		values[size() - 1] = null;
		size--;
	}

	/**
	 * Clears the list, removing all elements.
	 */
	@Override
	public void clear() {
		Arrays.fill(values, null);
		size = 0;
	}

	/**
	 * Sorts the elements of the list using the specified comparator.
	 *
	 * @param comparator the comparator to use for sorting
	 */
	@Override
	public void sort(Comparator<? super E> comparator) {
		Arrays.sort(values, 0, size(), comparator);
	}

	/**
	 * Returns an iterator over the elements in the list.
	 *
	 * @return an iterator over the elements in the list
	 */
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private int counter = 0;

			/**
			 * Returns true if there are more elements in the iteration.
			 *
			 * @return true if there are more elements in the iteration
			 */
			@Override
			public boolean hasNext() {
				return counter < size();
			}

			/**
			 * Returns the next element in the iteration.
			 *
			 * @return the next element in the iteration
			 * @throws NoSuchElementException if there are no more elements to iterate
			 */
			@Override
			public E next() {
				if (!hasNext()) {
					throw new NoSuchElementException(NO_SUCH_ARGUMENT_EXCEPTION_MESSAGE);
				}
				return values[counter++];
			}
		};
	}
}
