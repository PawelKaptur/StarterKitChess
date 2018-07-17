package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class BishopValidator extends AbstractMoveValidator {

	
	@Override
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		int changeX = Math.abs(from.getX() - to.getX());
		int changeY = Math.abs(from.getY() - to.getY());
		
		if (changeX == changeY) {
			return true;
		} 
		
		return false;
	}

}
