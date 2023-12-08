package ru.romanov.tasks.task1.impl;

import lombok.Getter;
import lombok.Setter;
import ru.romanov.tasks.task1.contract.DequeContract;
import ru.romanov.tasks.task1.contract.ListContract;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of a doubly linked list that implements both DequeContract and ListContract.
 *
 * @param <E> the type of elements in the list
 */
public class LinkedListImpl<E> implements DequeContract<E>, ListContract<E> {
	/**
	 * The default error message for NoSuchElementException when attempting to access an element beyond the iteration.
	 */
	private static final String NO_SUCH_ELEMENT_EXCEPTION_MESSAGE = "No more elements in the iteration";
	/**
	 * The current size of the data structure, representing the number of elements it contains.
	 */
	private int size;

	/**
	 * The last node in the linked structure.
	 *
	 * @param <E> the type of elements held in the node.
	 */
	private Node<E> lastNode;

	/**
	 * The first node in the linked structure.
	 *
	 * @param <E> the type of elements held in the node.
	 */
	private Node<E> firstNode;

	/**
	 * Constructs an empty linked list.
	 */
	public LinkedListImpl() {
		firstNode = new Node<>(null, null, null);
		lastNode = new Node<>(null, firstNode, null);
		firstNode.setNext(lastNode);
	}

	private void checkIndex(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	}

	private void checkSize() {
		if (size == 0) {
			throw new NoSuchElementException("List is empty");
		}
	}

	/**
	 * Adds the specified element to the end of the list.
	 *
	 * @param e the element to add
	 */
	@Override
	public void addLast(E e) {
		Node<E> prev = lastNode;
		Node<E> newNode = new Node<>(e, prev, null);
		lastNode = newNode;
		if (prev == null) {
			firstNode = newNode;
		} else {
			prev.next = newNode;
		}
		size++;
	}

	/**
	 * Adds the specified element to the beginning of the list.
	 *
	 * @param e the element to add
	 */
	@Override
	public void addFirst(E e) {
		Node<E> next = firstNode;
		Node<E> newNode = new Node<>(e, null, next);
		firstNode = newNode;
		if (next == null) {
			lastNode = newNode;
		} else {
			next.prev = newNode;
		}
		size++;
	}

	/**
	 * Returns the last element in the list.
	 *
	 * @return the last element in the list
	 * @throws NoSuchElementException if the list is empty
	 */
	@Override
	public E getLast() {
		checkSize();
		return lastNode.getCurr();
	}

	/**
	 * Returns the first element in the list.
	 *
	 * @return the first element in the list
	 * @throws NoSuchElementException if the list is empty
	 */
	@Override
	public E getFirst() {
		checkSize();
		return firstNode.getCurr();
	}


	/**
	 * Adds the specified element to the end of the list.
	 *
	 * @param e the element to add
	 */
	@Override
	public void add(E e) {
		Node<E> newNode = new Node<>(e, lastNode, null);
		if (size == 0) {
			firstNode = newNode;
		} else {
			lastNode.setNext(newNode);
		}
		lastNode = newNode;
		size++;
	}

	/**
	 * Adds the specified element at the specified position in the list.
	 *
	 * @param e     the element to add
	 * @param index the position at which to add the element
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	@Override
	public void add(E e, int index) {
		checkIndex(index);

		if (index == 0) {
			addFirst(e);
		} else if (index == size) {
			addLast(e);
		} else {
			Node<E> prevNode = getNode(index - 1);
			Node<E> nextNode = prevNode.getNext();
			Node<E> newNode = new Node<>(e, prevNode, nextNode);
			prevNode.setNext(newNode);
			nextNode.setPrev(newNode);
			size++;
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
		return node(index).getCurr();
	}

	private Node<E> node(int index) {
		Node<E> x;
		if (index < (size >> 1)) {
			x = firstNode;
			for (int i = 0; i < index; i++) {
				x = x.next;
			}
		} else {
			x = lastNode;
			for (int i = size - 1; i > index; i--) {
				x = x.prev;
			}
		}
		return x;
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

		if (index == 0) {
			firstNode = firstNode.getNext();
			if (firstNode != null) {
				firstNode.setPrev(null);
			}
		} else {
			Node<E> curr = getNode(index);
			curr.getPrev().setNext(curr.getNext());
			if (curr.getNext() != null) {
				curr.getNext().setPrev(curr.getPrev());
			} else {
				lastNode = curr.getPrev();
			}
		}
		size--;
	}

	/**
	 * Clears the list.
	 */
	@Override
	public void clear() {
		firstNode = null;
		lastNode = null;
		size = 0;
	}

	/**
	 * Sorts the elements of the list using the specified comparator.
	 *
	 * @param comparator the comparator to use for sorting
	 */
	@Override
	public void sort(Comparator<? super E> comparator) {
		boolean swapped;
		Node<E> curr;
		Node<E> next = null;
		if (size == 0) {
			return;
		}
		do {
			swapped = false;
			curr = firstNode;
			while (curr.getNext() != next) {
				if (comparator.compare(curr.getCurr(), curr.getNext().getCurr()) > 0) {
					E temp = curr.getCurr();
					curr.setCurr(curr.getNext().getCurr());
					curr.getNext().setCurr(temp);
					swapped = true;
				}
				curr = curr.getNext();
			}
			next = curr;
		} while (swapped);
	}

	/**
	 * Gets the node at the specified index.
	 *
	 * @param index the index of the node to retrieve
	 * @return the node at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	private Node<E> getNode(int index) {
		checkIndex(index);

		Node<E> curr = firstNode;
		for (int i = 0; i < index; i++) {
			curr = curr.getNext();
		}
		return curr;
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
				return counter < size;
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
					throw new NoSuchElementException(NO_SUCH_ELEMENT_EXCEPTION_MESSAGE);
				}
				return get(counter++);
			}
		};
	}

	/**
	 * Returns a descending iterator over the elements in the list.
	 *
	 * @return a descending iterator over the elements in the list
	 */
	@Override
	public Iterator<E> descendingIterator() {
		return new Iterator<E>() {
			private int counter = size - 1;

			/**
			 * Returns true if there are more elements in the descending iteration.
			 *
			 * @return true if there are more elements in the descending iteration
			 */
			@Override
			public boolean hasNext() {
				return counter >= 0;
			}

			/**
			 * Returns the next element in the descending iteration.
			 *
			 * @return the next element in the descending iteration
			 * @throws NoSuchElementException if there are no more elements to iterate
			 */
			@Override
			public E next() {
				if (!hasNext()) {
					throw new NoSuchElementException(NO_SUCH_ELEMENT_EXCEPTION_MESSAGE);
				}
				return get(counter--);
			}
		};
	}

	/**
	 * Node class represents a node in the linked list.
	 *
	 * @param <E> the type of element in the node
	 */
	@Setter
	@Getter
	static class Node<E> {
		private E curr;
		private Node<E> next;
		private Node<E> prev;

		/**
		 * Constructs a node with the specified element, previous node, and next node.
		 *
		 * @param curr the element in the node
		 * @param prev the previous node
		 * @param next the next node
		 */
		Node(E curr, Node<E> prev, Node<E> next) {
			this.curr = curr;
			this.prev = prev;
			this.next = next;
		}
	}
}
