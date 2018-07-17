package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class KnightValidator extends AbstractMoveValidator {

	@Override
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		//porobic na poczatku zmienne i wrzucic te from.get() itd dla przejrzystosci
		
		if (Math.abs(from.getX() - to.getX()) == 1 && Math.abs(from.getY() - to.getY()) == 2) {
			return true;
		} else if (Math.abs(from.getX() - to.getX()) == 2 && Math.abs(from.getX() - to.getX()) == 1) {
			return true;
		}
		
		return false;
	}
}
