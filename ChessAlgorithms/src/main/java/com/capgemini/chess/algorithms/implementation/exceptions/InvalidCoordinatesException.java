package com.capgemini.chess.algorithms.implementation.exceptions;

public class InvalidCoordinatesException extends InvalidMoveException {

	private static final long serialVersionUID = 6668028571356911885L;

	public InvalidCoordinatesException() {
		super("Invalid coordinates!");
	}
	
	public InvalidCoordinatesException(String message) {
		super("Invalid coordinates! " + message);
	}

}
