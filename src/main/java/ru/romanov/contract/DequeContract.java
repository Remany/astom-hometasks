package ru.romanov.contract;

import java.util.Iterator;

/**
 * The DequeContract interface represents a contract for a deque (double-ended queue) data structure.
 * It defines the methods that a deque implementation must provide.
 *
 * @param <E> the type of elements in the deque
 */
public interface DequeContract<E> {
	void addLast(E e);

	void addFirst(E e);

	E getLast();

	E getFirst();

	Iterator<E> descendingIterator();
}
