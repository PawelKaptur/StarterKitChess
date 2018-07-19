package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class PawnValidator extends AbstractMoveValidator {

	private static final int STARTING_Y_POSITION_FOR_WHITE_PAWN = 1;
	private static final int STARTING_Y_POSITION_FOR_BLACK_PAWN = 6;

	private int changeX;
	private int changeY;
	private MoveType moveType;
	private Coordinate from;

	@Override
	public boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		this.changeX = Math.abs(from.getX() - to.getX());
		this.changeY = from.getY() - to.getY();
		this.moveType = moveType;
		this.from = from;

		if (piece.getColor().equals(Color.WHITE)) {
			return checkWhitePawn();
		} else {
			return checkBlackPawn();
		}
	}

	private boolean checkWhitePawn() {
		if (moveType == MoveType.ATTACK) {
			if (changeX == 0 && changeY == -1) {
				return true;
			} else if (from.getY() == STARTING_Y_POSITION_FOR_WHITE_PAWN && changeX == 0
					&& (changeY == -1 || changeY == -2)) {
				return true;
			}
			return false;
		} else {
			if (changeX == 1 && changeY == -1) {
				return true;
			}
			return false;
		}
	}

	private boolean checkBlackPawn() {
		if (moveType == MoveType.ATTACK) {
			if (changeX == 0 && changeY == 1) {
				return true;
			} else if (from.getY() == STARTING_Y_POSITION_FOR_BLACK_PAWN && changeX == 0
					&& (changeY == 1 || changeY == 2)) {
				return true;
			}
			return false;

		} else {
			if (changeX == 1 && changeY == 1) {
				return true;
			}
			return false;
		}
	}
}
