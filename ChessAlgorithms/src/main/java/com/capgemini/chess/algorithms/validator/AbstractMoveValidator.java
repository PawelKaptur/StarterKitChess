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
	
	
	// Piece prawdopodobnie nie bedzie potrzebny, z moveType pomyslec bo to
	// tylko dla pionka zrobione, piece tez dla pionka
	public abstract boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType);

	public boolean checkIfRoadToPieceDestinationIsEmpty(Coordinate from, Coordinate to, Board board)
			throws InvalidMoveException {
		this.fromX = from.getX();
		this.fromY = from.getY();
		this.toX = to.getX();
		this.toY = to.getY();
		this.changeX = fromX - toX;
		this.changeY = fromY - toY;
		
		// ruchy na skos
		if (fromX != toX && fromY != toY) {
			int change = Math.abs(from.getX() - to.getX());
			// ruch na skos prawo-gora
			if (fromX < toX && fromY < toY) {
				for (int x = fromX + 1, y = fromY + 1; x < toX && y < toY; x++, y++) {
					Coordinate coordinate = new Coordinate(x, y);
					if (board.getPieceAt(coordinate) != null) {
						throw new InvalidMoveException();
					}
				}
			}

			// ruch na skos prawo-dol
			if (fromX < toX && fromY > toY) {
				for (int x = fromX + 1, y = fromY - 1; x < toX && y > toY; x++, y--) {
					Coordinate coordinate = new Coordinate(x, y);
					if (board.getPieceAt(coordinate) != null) {
						throw new InvalidMoveException();
					}
				}
			}

			// ruch na skos lewo-dol
			if (fromX > toX && fromY > toY) {
				for (int x = fromX - 1, y = fromY - 1; x > toX && y > toY; x--, y--) {
					Coordinate coordinate = new Coordinate(x, y);
					if (board.getPieceAt(coordinate) != null) {
						throw new InvalidMoveException();
					}
				}
			}

			// ruch na skos lewo-gora
			if (fromX > toX && fromY < toY) {
				for (int x = fromX - 1, y = fromY + 1; x > toX && y < toY; x--, y++) {
					Coordinate coordinate = new Coordinate(x, y);
					if (board.getPieceAt(coordinate) != null) {
						throw new InvalidMoveException();
					}
				}
			}

		}

		if (changeX != 0 && changeY == 0) {
			horizontalMove(board);
		}

		else if (changeX == 0 && changeY != 0) {
			verticalMove(board);
		}
		
		return true;
	}
	
	private void horizontalMove(Board board) throws InvalidMoveException{
		int change = changeX > 0 ? -1 : 1;

		for (int x = fromX + change; x != toX; x = x + change) {
			Coordinate coordinate = new Coordinate(x, toY);
			if (board.getPieceAt(coordinate) != null) {
				throw new InvalidMoveException();
			}
		}
	}
	
	private void verticalMove(Board board) throws InvalidMoveException{
		int change = changeY > 0 ? -1 : 1;

		for (int y = fromY + change; y != toY; y = y + change) {
			Coordinate coordinate = new Coordinate(toX, y);
			if (board.getPieceAt(coordinate) != null) {
				throw new InvalidMoveException();
			}
		}
	}
}
