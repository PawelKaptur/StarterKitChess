package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class Context {
	private AbstractMoveValidator abstractMoveValidator;
	
	public Context(AbstractMoveValidator abstractMoveValidator){
		this.abstractMoveValidator = abstractMoveValidator;
	}
	
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType){
		return abstractMoveValidator.checkIfPieceCanMoveTo(piece, from, to, moveType);	
	}
	
	public boolean checkIfRoadToPieceDestinationIsEmpty(Coordinate from, Coordinate to, Board board) throws InvalidMoveException{
		return abstractMoveValidator.checkIfRoadToPieceDestinationIsEmpty(from, to, board);	
	}
}
