package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidCoordinatesException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class MyTests {
	
	@Test
	public void shouldThrowInvalidCoordinatesExceptionWhilePerformingMoveInvalidIndexOutOfBoundWithNegativeBounds()
			throws InvalidMoveException {
		// given
		BoardManager boardManager = new BoardManager();

		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(77, 5), new Coordinate(7, 6));
		} catch (InvalidCoordinatesException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}
}
