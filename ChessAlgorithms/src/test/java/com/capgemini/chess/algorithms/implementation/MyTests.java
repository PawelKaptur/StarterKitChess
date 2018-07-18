package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidCoordinatesException;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class MyTests {
	
	@Test
	public void shouldThrowInvalidCoordinatesExceptionWhilePerformingMoveInvalidIndexOutOfBoundWithNegativeBounds()
			throws InvalidMoveException {
		// given
		BoardManager boardManager = new BoardManager();

		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(77, 5), new Coordinate(7, 6));
		} catch (InvalidCoordinatesException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldThrowInvalidMoveExceptionBecauseNotYourPiece() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(5, 4));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 4), new Coordinate(5, 4));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldThrowInvalidMoveExceptionBecauseKingWillBeInCheck() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(2,0), new Coordinate(1,0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldMoveKing() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boardManager.performMove(new Coordinate(2,0), new Coordinate(3,0));
	

		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 0)));
		assertEquals(Piece.WHITE_KING, boardManager.getBoard().getPieceAt(new Coordinate(3, 0)));
	}
	
	@Test
	public void shouldThrowKingInCheckException() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(3, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(2,0), new Coordinate(3,1));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldAssertCheckMate() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(3, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.CHECK_MATE, boardState);
	}
	
	@Test
	public void shouldAssertRegular() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(3, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.REGULAR, boardState);
	}
	
	private Move createDummyMove(Board board) {
		
		Move move = new Move();
		
		if (board.getMoveHistory().size() % 2 == 0) {
			board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
			move.setMovedPiece(Piece.WHITE_ROOK);
		}
		else {
			board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 0));
			move.setMovedPiece(Piece.BLACK_ROOK);
		}
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 0));
		move.setType(MoveType.ATTACK);
		board.setPieceAt(null, new Coordinate(0, 0));
		return move;
	}
}
