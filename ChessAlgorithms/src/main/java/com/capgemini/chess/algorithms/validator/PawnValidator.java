package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class PawnValidator extends AbstractMoveValidator {

	@Override
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		int changeX = Math.abs(from.getX() - to.getX());
		int changeY = from.getY() - to.getY();

		int startingYPositionOfWhitePawn = 1;
		int startingYPostitionOfBlackPawn = 6;

		if (piece.getColor().equals(Color.WHITE)) {
			if (moveType == MoveType.ATTACK) {
				if (changeX == 0 && changeY == -1) {
					return true;
				} else if (from.getY() == startingYPositionOfWhitePawn && changeX == 0
						&& (changeY == -1 || changeY == -2)) {
					return true;
				}
				return false;

			} else if (moveType == MoveType.CAPTURE) {
				if (changeX == 1 && changeY == -1) {
					return true;
				}
				return false;
			}
		} else {
			if (moveType == MoveType.ATTACK) {
				if (changeX == 0 && changeY == 1) {
					return true;
				} else if (from.getY() == startingYPostitionOfBlackPawn && changeX == 0
						&& (changeY == 1 || changeY == 2)) {
					return true;
				}
				return false;

			} else if (moveType == MoveType.CAPTURE) {
				if (changeX == 1 && changeY == 1) {
					return true;
				}
				return false;
			}
		}
		return false;
	}
}
