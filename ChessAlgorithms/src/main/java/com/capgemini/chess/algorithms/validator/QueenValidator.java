package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class QueenValidator extends AbstractMoveValidator {

	@Override
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		if (from.getX() - to.getX() == 0 && Math.abs(from.getY() - to.getY()) > 0) {
			return true;
		} else if (from.getX() - to.getX() > 0 && Math.abs(from.getY() - to.getY()) == 0) {
			return true;
		} else if (Math.abs(from.getX() - to.getX()) == Math.abs(from.getY() - to.getY())) {
			return true;
		}
		return false;
	}

}
