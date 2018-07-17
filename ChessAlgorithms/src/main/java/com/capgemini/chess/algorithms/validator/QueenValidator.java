package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class QueenValidator extends AbstractMoveValidator {
	//zobaczyc czy nie uzywac tutaj validatora dla wiezy i gonca

	@Override
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		int changeX = Math.abs(from.getX() - to.getX());
		int changeY = Math.abs(from.getY() - to.getY());
		
		if (changeX == 0 && changeY > 0) {
			return true;
		} else if (changeX > 0 && changeY == 0) {
			return true;
		} else if (changeX == changeY) {
			return true;
		}
		return false;
	}

}
