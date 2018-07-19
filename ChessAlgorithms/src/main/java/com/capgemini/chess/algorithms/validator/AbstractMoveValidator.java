package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public abstract class AbstractMoveValidator {

	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	private int changeX;
	private int changeY;
	private Board board;

	public abstract boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType);

	public boolean checkIfRoadToPieceDestinationIsEmpty(Coordinate from, Coordinate to, Board board)
			throws InvalidMoveException {
		this.fromX = from.getX();
		this.fromY = from.getY();
		this.toX = to.getX();
		this.toY = to.getY();
		this.changeX = fromX - toX;
		this.changeY = fromY - toY;
		this.board = board;

		if (fromX != toX && fromY != toY) {
			diagonalMove();
		}

		else if (changeX != 0 && changeY == 0) {
			horizontalMove();
		}

		else if (changeX == 0 && changeY != 0) {
			verticalMove();
		}

		return true;
	}

	private void horizontalMove() throws InvalidMoveException {
		int change = changeX > 0 ? -1 : 1;

		for (int x = fromX + change; x != toX; x = x + change) {
			Coordinate coordinate = new Coordinate(x, toY);
			if (this.board.getPieceAt(coordinate) != null) {
				throw new InvalidMoveException();
			}
		}
	}

	private void verticalMove() throws InvalidMoveException {
		int change = changeY > 0 ? -1 : 1;

		for (int y = fromY + change; y != toY; y = y + change) {
			Coordinate coordinate = new Coordinate(toX, y);
			if (this.board.getPieceAt(coordinate) != null) {
				throw new InvalidMoveException();
			}
		}
	}

	private void diagonalMove() throws InvalidMoveException {
		changeX = changeX > 0 ? -1 : 1;
		changeY = changeY > 0 ? -1 : 1;
		for (int x = fromX + changeX, y = fromY + changeY; x != toX && y != toY; x = x + changeX, y = y + changeY) {
			Coordinate coordinate = new Coordinate(x, y);
			if(this.board.getPieceAt(coordinate) != null) {
				throw new InvalidMoveException();
			}
		}
	}
}
