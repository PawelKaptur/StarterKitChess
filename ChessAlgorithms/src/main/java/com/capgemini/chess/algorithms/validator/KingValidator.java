package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class KingValidator extends AbstractMoveValidator {

	@Override
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		int changeX = Math.abs(from.getX() - to.getX());
		int changeY = Math.abs(from.getY() - to.getY());
		
		if (changeX == 1 && changeY == 1) {
			return true;
		} else if (changeX == 0 && changeY == 1) {
			return true;
		} else if (changeX == 1 && changeY == 0) {
			return true;
		}
		
		return false;
	}
}
