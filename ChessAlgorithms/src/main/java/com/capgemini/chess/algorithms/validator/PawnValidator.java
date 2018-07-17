package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class PawnValidator extends AbstractMoveValidator {

	@Override
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		if (piece.getColor().equals(Color.WHITE)) {
			if (moveType == MoveType.ATTACK) {
				if (Math.abs(from.getX() - to.getX()) == 0 && from.getY() - to.getY() == -1) {
					return true;
				} else if (from.getY() == 1 && from.getX() - to.getX() == 0
						&& (from.getY() - to.getY() == -1 || from.getY() - to.getY() == -2)) {
					return true;
				}
				return false;

			} else if (moveType == MoveType.CAPTURE) {
				if (Math.abs(from.getX() - to.getX()) == 1 && from.getY() - to.getY() == -1) {
					return true;
				}
				return false;
			}
		} else {
			if (moveType == MoveType.ATTACK) {
				if (Math.abs(from.getX() - to.getX()) == 0 && from.getY() - to.getY() == 1) {
					return true;
				} else if (from.getY() == 6 && from.getX() - to.getX() == 0
						&& (from.getY() - to.getY() == 1 || from.getY() - to.getY() == 2)) {
					return true;
				}
				return false;

			} else if (moveType == MoveType.CAPTURE) {
				if (Math.abs(from.getX() - to.getX()) == 1 && from.getY() - to.getY() == 1) {
					return true;
				}
				return false;
			}
		}
		return false;
	}
}
