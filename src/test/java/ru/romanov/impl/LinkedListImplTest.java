package ru.romanov.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinkedListImplTest {
	private LinkedListImpl<Integer> linkedList;

	@BeforeEach
	void setUp() {
		linkedList = new LinkedListImpl<>();
	}

	@Test
	@DisplayName("when index within range then return element")
	void testGet_When_IndexWithinRange_Then_ReturnElement() {
		int element1 = 5;
		int element2 = 10;
		linkedList.add(element1);
		linkedList.add(element2);
		assertEquals(element1, linkedList.get(0), "Element at index 0 should be the first one we added");
		assertEquals(element2, linkedList.get(1), "Element at index 1 should be the second one we added");
	}

	@Test
	@DisplayName("when index negative then throw Exception")
	void testGet_When_IndexNegative_Then_ThrowException() {
		int element = 5;
		linkedList.add(element);
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(-1), "Should throw IndexOutOfBoundsException when index is negative");
	}

	@Test
	@DisplayName("when index greater than size then throw Exception")
	void testGet_When_IndexGreaterThanSize_Then_ThrowException() {
		int element = 5;
		linkedList.add(element);
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(2), "Should throw IndexOutOfBoundsException when index is greater than size");
	}

	@Test
	@DisplayName("add some elements to list")
	void testAdd_When_AddingAtMiddle_Then_AddedAtMiddle() {
		int element1 = 5;
		int element2 = 10;
		int element3 = 15;
		linkedList.add(element1);
		linkedList.add(element2);
		linkedList.add(element3, 1);
		assertEquals(3, linkedList.size(), "Size of list should be 3 after adding three elements");
		assertEquals(element1, linkedList.get(0), "Element at index 0 should be the first one we added");
		assertEquals(element3, linkedList.get(1), "Element at index 1 should be the third one we added");
		assertEquals(element2, linkedList.get(2), "Element at index 2 should be the second one we added");
	}

	@Test
	@DisplayName("add element with index when index is invalid")
	void testAdd_When_AddingAtInvalidIndex_Then_Throws_IndexOutOfBoundsException() {
		int element = 5;
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.add(element, 1), "Should throw IndexOutOfBoundsException when index is greater than size");
	}

	@Test
	@DisplayName("add last element to empty list")
	void testAddLast_When_ListIsEmpty_Then_ElementAdded() {
		int element = 5;
		linkedList.addLast(element);
		assertEquals(1, linkedList.size(), "Size of list should be 1 after adding one element");
		assertEquals(element, linkedList.get(0), "Element at index 0 should be the one we added");
	}

	@Test
	@DisplayName("get last element from list when is empty then throw exception")
	void testGet_LastWhenListIsEmpty_Then_Throws_NoSuchElementException() {
		assertThrows(NoSuchElementException.class, () -> linkedList.getLast(), "Should throw NoSuchElementException when list is empty");
	}

	@Test
	@DisplayName("get first element from list")
	void testGetFirstWhenListNotEmptyThenReturnFirstElement() {
		int element = 5;
		linkedList.addFirst(element);
		assertEquals(element, linkedList.getFirst(), "First element should be the one we added");
	}

	@Test
	@DisplayName("get first element when list is empty then throw exception")
	void testGetFirst_When_ListEmpty_Then_Throws_NoSuchElementException() {
		assertThrows(NoSuchElementException.class, () -> linkedList.getFirst(), "Should throw NoSuchElementException when list is empty");
	}

	@Test
	@DisplayName("add one element when list is empty")
	void testAdd_When_ListIsEmpty_Then_ElementAddedToList() {
		int element = 5;
		linkedList.add(element);
		assertEquals(1, linkedList.size(), "Size of list should be 1 after adding one element");
		assertEquals(element, linkedList.get(0), "Element at index 0 should be the one we added");
	}

	@Test
	@DisplayName("add some elements to list")
	void testAdd_When_ListIsNotEmpty_Then_ElementAddedToList() {
		int element1 = 5;
		int element2 = 10;
		linkedList.add(element1);
		linkedList.add(element2);
		assertEquals(2, linkedList.size(), "Size of list should be 2 after adding two elements");
		assertEquals(element1, linkedList.get(0), "Element at index 0 should be the first one we added");
		assertEquals(element2, linkedList.get(1), "Element at index 1 should be the second one we added");
	}

	@Test
	@DisplayName("remove element")
	void testRemoveElement_When_ListHasMultiplyElements_Then_ElementWasRemoved() {
		linkedList.add(1);
		linkedList.add(2);
		linkedList.add(3);
		linkedList.remove(1);
		assertEquals(2, linkedList.size());
		assertEquals(1, linkedList.getFirst());
		assertEquals(3, linkedList.getLast());
	}

	@Test
	@DisplayName("remove element when index not found")
	void testRemoveElement_When_IndexOfElementWasNotFound_Then_Throws_IndexOutOfBoundsException() {
		linkedList.add(1);
		linkedList.add(2);
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.remove(2));
	}

	@Test
	@DisplayName("clear list")
	void testClearOfList_Then_ListIsEmpty() {
		linkedList.add(1);
		linkedList.add(2);
		linkedList.add(3);
		linkedList.add(4);
		linkedList.clear();
		assertEquals(0, linkedList.size());
	}

	@Test
	@DisplayName("sort method")
	void testSort_Then_ListShouldBeSorted() {
		linkedList.add(3);
		linkedList.add(1);
		linkedList.add(2);
		Comparator<Integer> comparator = Comparator.naturalOrder();
		linkedList.sort(comparator);
		assertEquals(3, linkedList.size());
		assertEquals(1, linkedList.getFirst());
		assertEquals(2, linkedList.get(1));
		assertEquals(3, linkedList.getLast());
	}
}
