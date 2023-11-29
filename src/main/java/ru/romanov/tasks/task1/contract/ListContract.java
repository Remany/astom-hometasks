package ru.romanov.tasks.task1.contract;

import java.util.Comparator;

/**
 * The ListContract interface represents a contract for a list data structure.
 * It extends the Iterable interface, allowing the list to be iterated over.
 *
 * @param <E> the type of elements in the list
 */
public interface ListContract<E> extends Iterable<E> {
	void add(E e);

	void add(E e, int index);

	int size();

	E get(int index);

	void remove(int index);

	void clear();

	void sort(Comparator<? super E> comparator);
}
