package ru.romanov.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayListImplTest {
	private ArrayListImpl<Integer> list;

	@BeforeEach
	void setUp() {
		list = new ArrayListImpl<>();
	}

	@Test
	@DisplayName("sort method when list is empty")
	void testSort_When_ListIsEmpty_Then_ListIsStillEmpty() {
		list.sort(Comparator.naturalOrder());
		assertEquals(0, list.size());
	}

	@Test
	@DisplayName("sort method")
	void testSort_When_ListHas_MultipleElements_Then_ListIsSorted_InCorrectOrder() {
		list.add(3);
		list.add(1);
		list.add(2);
		list.sort(Comparator.naturalOrder());
		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(3, list.get(2));
	}

	@Test
	@DisplayName("clear list test")
	void testClear_When_ListIsNotEmpty_Then_AllElementsRemoved() {
		list.add(1);
		list.add(2);
		list.add(3);
		list.clear();
		assertEquals(0, list.size());
	}

	@Test
	@DisplayName("iterator test when list is not empty then return true")
	void testIterator_When_ListIsNotEmpty_Then_IteratorHasNext_ReturnsTrue() {
		list.add(1);
		Iterator<Integer> iterator = list.iterator();
		assertTrue(iterator.hasNext());
	}

	@Test
	@DisplayName("iterator test when list is empty then return false")
	void testIterator_When_ListIsEmpty_Then_IteratorHasNext_ReturnsFalse() {
		Iterator<Integer> iterator = list.iterator();
		assertFalse(iterator.hasNext());
	}

	@Test
	@DisplayName("iterator return value")
	void testIterator_When_ListIsNotEmpty_ThenIteratorNext_ReturnsElement() {
		list.add(1);
		Iterator<Integer> iterator = list.iterator();
		assertEquals(1, iterator.next());
	}

	@Test
	@DisplayName("iterator test when iterator out of index from arr then throw exception")
	void testIterator_When_NoMoreElements_Then_IteratorNext_Throws_NoSuchElementException() {
		list.add(1);
		Iterator<Integer> iterator = list.iterator();
		iterator.next();
		assertThrows(NoSuchElementException.class, iterator::next);
	}

	@Test
	@DisplayName("remove element")
	void testRemove_When_ValidIndexAndListIsNotEmpty_Then_ElementRemoved() {
		int element = 5;
		list.add(element);
		list.remove(0);
		assertEquals(0, list.size());
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
	}

	@Test
	@DisplayName("remove element from list when index is not greater then zero")
	void testRemove_When_InvalidIndex_Then_Throws_IndexOutOfBoundsException() {
		int invalidIndex = -1;
		assertThrows(IndexOutOfBoundsException.class, () -> list.remove(invalidIndex));
	}

	@Test
	@DisplayName("remove element from list when index is invalid")
	void testRemove_When_IndexGreaterThan_OrEqualToSize_Then_Throws_IndexOutOfBoundsException() {
		int index = 1;
		list.add(5);
		assertThrows(IndexOutOfBoundsException.class, () -> list.remove(index));
	}

	@Test
	@DisplayName("add element to list when list is empty")
	void testAdd_When_ListIsEmpty_Then_ElementAddedToList() {
		int element = 5;
		list.add(element);
		assertEquals(1, list.size());
		assertEquals(element, list.get(0));
	}

	@Test
	@DisplayName("add element to list when list is not peresent")
	void testAdd_When_ListIsNotEmpty_Then_ElementsAddedToList() {
		int element1 = 5;
		int element2 = 10;
		list.add(element1);
		list.add(element2);
		assertEquals(2, list.size());
		assertEquals(element1, list.get(0));
		assertEquals(element2, list.get(1));
	}

	@Test
	@DisplayName("add element to list when list if full")
	void testAdd_When_List_IsFull_Then_ElementAddedToList() {
		int initialCapacity = 10;
		list = new ArrayListImpl<>(initialCapacity);
		for (int i = 0; i < initialCapacity; i++) {
			list.add(i);
		}
		int newElement = 20;
		list.add(newElement);
		assertEquals(initialCapacity + 1, list.size());
		assertEquals(newElement, list.get(initialCapacity));
	}

	@Test
	@DisplayName("add element with index")
	void testAdd_WithIndex_When_ListIsNotEmpty_Then_ElementAdded() {
		int index = 0;
		int oldElement = 5;
		int newElement = 10;
		list.add(oldElement);
		assertEquals(oldElement, list.get(index));
		list.add(newElement, index);
		assertEquals(newElement, list.get(index));
	}

	@Test
	@DisplayName("add element with invalid index")
	void testAdd_When_InvalidIndex_Then_Throws_IndexOutOfBoundsException() {
		int invalidIndex = -1;
		int element = 5;
		assertThrows(IndexOutOfBoundsException.class, () -> list.add(element, invalidIndex));
	}

	@Test
	@DisplayName("get size of list")
	void testSize_When_ListIsNotEmpty_Then_ReturnNumberOfElementsAdded() {
		int element1 = 5;
		int element2 = 10;
		list.add(element1);
		list.add(element2);
		assertEquals(2, list.size());
	}

	@Test
	@DisplayName("get element")
	void testGet_When_ListIsNotEmptyAndIndexIsValid_Then_ReturnElement() {
		int element = 5;
		list.add(element);
		assertEquals(element, list.get(0));
	}

	@Test
	@DisplayName("get element when list is empty")
	void testGet_When_ListIsEmpty_Then_Throws_IndexOutOfBoundsException() {
		int index = 0;
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
	}

	@Test
	@DisplayName("get element with invalid index")
	void testGet_When_IndexIsNegative_Then_Throws_IndexOutOfBoundsException() {
		int negativeIndex = -1;
		list.add(5);
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(negativeIndex));
	}

	@Test
	@DisplayName("get element with index of greater then list size")
	void testGet_When_IndexIsGreaterThanSize_Then_Throws_IndexOutOfBoundsException() {
		int index = 1;
		list.add(5);
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
	}
}