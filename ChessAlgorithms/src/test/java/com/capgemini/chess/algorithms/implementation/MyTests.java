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
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

public class MyTests {

	Board board = new Board();

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
		// Board board = new Board();
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
		// Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(2, 0), new Coordinate(1, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldMoveKing() throws InvalidMoveException {
		// given
		// Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 4));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		boardManager.performMove(new Coordinate(2, 0), new Coordinate(3, 0));

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
			boardManager.performMove(new Coordinate(2, 0), new Coordinate(3, 1));
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

	@Test
	public void shouldMoveKingBehindPiece() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 4));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(1, 2));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		boardManager.performMove(new Coordinate(2, 0), new Coordinate(1, 0));

		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 0)));
		assertEquals(Piece.WHITE_KING, boardManager.getBoard().getPieceAt(new Coordinate(1, 0)));
	}

	@Test
	public void shouldThrowKingInCheckExceptionWhenMovingFigure() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 4));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(1, 2));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(1, 0));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(2, 3));
		} catch (KingInCheckException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldThrowKingInCheckExceptionWhenMovingKing() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(0, 2));
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(0, 1));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(2, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(2, 1), new Coordinate(2, 0));
		} catch (KingInCheckException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldMovePieceToCapture() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 1));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(1, 1));

		// when
		BoardManager boardManager = new BoardManager(board);

		boardManager.performMove(new Coordinate(2, 1), new Coordinate(1, 1));

		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 1)));
		assertEquals(Piece.BLACK_ROOK, boardManager.getBoard().getPieceAt(new Coordinate(1, 1)));
	}

	@Test
	public void shouldThrowKingInCheckExceptionWhenMovingBlackPiece() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(1, 3));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 2));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(1, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(4, 2));
		} catch (KingInCheckException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldThrowKingInCheckExceptionWhenMovingBlackFigure() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(2, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 6));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(2, 6));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(4, 4));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 6), new Coordinate(2, 6));
		} catch (KingInCheckException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldThrowInvalidMoveExceptionForWhiteBishopRightUpMove() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 3));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 4), new Coordinate(3, 4));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldThrowInvalidMoveExceptionForWhiteBishopRightDownMove() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(4, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(5, 3));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 4), new Coordinate(6, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldThrowInvalidMoveExceptionForWhiteQueenLeftDownMove() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(4, 4));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(3, 3));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 4), new Coordinate(2, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldThrowInvalidMoveExceptionForWhiteQueenLeftUpMove() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(4, 4));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(3, 5));

		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 4), new Coordinate(2, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldMoveWhitePawnForTwoSpaces() throws InvalidMoveException {
		// given
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(1, 1));

		// when
		BoardManager boardManager = new BoardManager(board);
		boardManager.performMove(new Coordinate(1, 1), new Coordinate(1, 3));

		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(1, 1)));
		assertEquals(Piece.WHITE_PAWN, boardManager.getBoard().getPieceAt(new Coordinate(1, 3)));
	}

	@Test
	public void shouldThrowInvalidMoveExceptionBecauseWhitePawnIsBlocked() throws InvalidMoveException {
		// given
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(1, 1));
		board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(1, 2));

		// when
		BoardManager boardManager = new BoardManager(board);

		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(1, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}

	@Test
	public void shouldThrowInvalidMoveExceptionBecauseWhitePawnIsNotInStratingPosition() throws InvalidMoveException {
		// given
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(2, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);

		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(2, 2), new Coordinate(2, 4));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldThrowInvalidMoveExceptionBecauseWhitePawnCanCaptureInFront() throws InvalidMoveException {
		// given
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(2, 2));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(2, 3));
		
		// when
		BoardManager boardManager = new BoardManager(board);

		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(2, 2), new Coordinate(2, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldThrowInvalidMoveExceptionBecauseWhitePawnCanNotGoBack() throws InvalidMoveException {
		// given
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(2, 2));

		// when
		BoardManager boardManager = new BoardManager(board);

		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(2, 2), new Coordinate(2, 1));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		// then
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldAssertEqualsForBothPieces() throws InvalidMoveException {
		// given
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(1, 1));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(6, 6));
		board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(2, 2));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(5, 5));
		// when
		BoardManager boardManager = new BoardManager(board);

		boolean exceptionThrown = false;
		
		boardManager.performMove(new Coordinate(1, 1), new Coordinate(2, 2));
		boardManager.performMove(new Coordinate(6, 6), new Coordinate(5, 5));
		
		// then
		assertEquals(Piece.WHITE_PAWN, board.getPieceAt(new Coordinate(2, 2)));
		assertNull(board.getPieceAt(new Coordinate(1, 1)));
		assertEquals(Piece.BLACK_PAWN, board.getPieceAt(new Coordinate(5, 5)));
		assertNull(board.getPieceAt(new Coordinate(6, 6)));
	}

	private Move createDummyMove(Board board) {

		Move move = new Move();

		if (board.getMoveHistory().size() % 2 == 0) {
			board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
			move.setMovedPiece(Piece.WHITE_ROOK);
		} else {
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
