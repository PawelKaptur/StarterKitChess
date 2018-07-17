package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class BishopValidator extends AbstractMoveValidator {

	
	@Override
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		if (Math.abs(from.getX() - to.getX()) == Math.abs(from.getY() - to.getY())) {
			return true;
		} 
		
		return false;
	}

}
