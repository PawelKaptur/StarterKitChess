package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public abstract class AbstractMoveValidator {

	// Piece prawdopodobnie nie bedzie potrzebny, z moveType pomyslec bo to
	// tylko dla pionka zrobione, piece tez dla pionka
	public abstract boolean checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType);

	public boolean checkIfRoadToPieceDestinationIsEmpty(Coordinate from, Coordinate to, Board board)
			throws InvalidMoveException {
		int fromX = from.getX();
		int fromY = from.getY();
		int toX = to.getX();
		int toY = to.getY();
		int changeX = Math.abs(fromX - toX);
		int changeY = Math.abs(fromY - toY);
		

		// int changeOfX = from.getX() - to.getX();

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

		// ruch w poziomie
		if (fromX != toX && changeY == 0) {
			int changeOfX = from.getX() - to.getX();
			int change = changeOfX > 0 ? -1 : 1;

			for (int x = fromX + change; x != toX; x = x + change) {
				Coordinate coordinate = new Coordinate(x, toY);
				if (board.getPieceAt(coordinate) != null) {
					throw new InvalidMoveException();
				}
			}
		}

		// ruch w pionie
		if (changeX == 0 && fromY != toY) {
			int changeOfY = from.getY() - to.getY();
			int change = changeOfY > 0 ? -1 : 1;

			for (int y = fromY + change; y != toY; y = y + change) {
				Coordinate coordinate = new Coordinate(toX, y);
				if (board.getPieceAt(coordinate) != null) {
					throw new InvalidMoveException();
				}
			}
		}
		
		return true;
	}
}
