package com.capgemini.chess.algorithms.implementation;

import java.util.Arrays;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;
import com.capgemini.chess.algorithms.validator.BishopValidator;
import com.capgemini.chess.algorithms.validator.Context;
import com.capgemini.chess.algorithms.validator.KingValidator;
import com.capgemini.chess.algorithms.validator.KnightValidator;
import com.capgemini.chess.algorithms.validator.PawnValidator;
import com.capgemini.chess.algorithms.validator.QueenValidator;
import com.capgemini.chess.algorithms.validator.RookValidator;

/**
 * Class for managing of basic operations on the Chess Board.
 *
 * @author Michal Bejm
 *
 */
public class BoardManager {

	private Board board = new Board();

	public BoardManager() {
		initBoard();
	}

	public BoardManager(List<Move> moves) {
		initBoard();
		for (Move move : moves) {
			addMove(move);
		}
	}

	public BoardManager(Board board) {
		this.board = board;
	}

	/**
	 * Getter for generated board
	 *
	 * @return board
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Performs move of the chess piece on the chess board from one field to
	 * another.
	 *
	 * @param from
	 *            coordinates of 'from' field
	 * @param to
	 *            coordinates of 'to' field
	 * @return move object which includes moved piece and move type
	 * @throws InvalidMoveException
	 *             in case move is not valid
	 */
	public Move performMove(Coordinate from, Coordinate to) throws InvalidMoveException {

		Move move = validateMove(from, to);

		addMove(move);

		return move;
	}

	/**
	 * Calculates state of the chess board.
	 *
	 * @return state of the chess board
	 * @throws InvalidMoveException
	 */
	public BoardState updateBoardState() throws InvalidMoveException {

		Color nextMoveColor = calculateNextMoveColor();

		boolean isKingInCheck = isKingInCheck(nextMoveColor);
		boolean isAnyMoveValid = isAnyMoveValid(nextMoveColor);

		BoardState boardState;
		if (isKingInCheck) {
			if (isAnyMoveValid) {
				boardState = BoardState.CHECK;
			} else {
				boardState = BoardState.CHECK_MATE;
			}
		} else {
			if (isAnyMoveValid) {
				boardState = BoardState.REGULAR;
			} else {
				boardState = BoardState.STALE_MATE;
			}
		}
		this.board.setState(boardState);
		return boardState;
	}

	/**
	 * Checks threefold repetition rule (one of the conditions to end the chess
	 * game with a draw).
	 *
	 * @return true if current state repeated at list two times, false otherwise
	 */
	public boolean checkThreefoldRepetitionRule() {

		// there is no need to check moves that where before last capture/en
		// passant/castling
		int lastNonAttackMoveIndex = findLastNonAttackMoveIndex();
		List<Move> omittedMoves = this.board.getMoveHistory().subList(0, lastNonAttackMoveIndex);
		BoardManager simulatedBoardManager = new BoardManager(omittedMoves);

		int counter = 0;
		for (int i = lastNonAttackMoveIndex; i < this.board.getMoveHistory().size(); i++) {
			Move moveToAdd = this.board.getMoveHistory().get(i);
			simulatedBoardManager.addMove(moveToAdd);
			boolean areBoardsEqual = Arrays.deepEquals(this.board.getPieces(),
					simulatedBoardManager.getBoard().getPieces());
			if (areBoardsEqual) {
				counter++;
			}
		}

		return counter >= 2;
	}

	/**
	 * Checks 50-move rule (one of the conditions to end the chess game with a
	 * draw).
	 *
	 * @return true if no pawn was moved or not capture was performed during
	 *         last 50 moves, false otherwise
	 */
	public boolean checkFiftyMoveRule() {

		// for this purpose a "move" consists of a player completing his turn
		// followed by his opponent completing his turn
		if (this.board.getMoveHistory().size() < 100) {
			return false;
		}

		for (int i = this.board.getMoveHistory().size() - 1; i >= this.board.getMoveHistory().size() - 100; i--) {
			Move currentMove = this.board.getMoveHistory().get(i);
			PieceType currentPieceType = currentMove.getMovedPiece().getType();
			if (currentMove.getType() != MoveType.ATTACK || currentPieceType == PieceType.PAWN) {
				return false;
			}
		}

		return true;
	}

	// PRIVATE

	private void initBoard() {

		this.board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 7));
		this.board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(1, 7));
		this.board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(2, 7));
		this.board.setPieceAt(Piece.BLACK_QUEEN, new Coordinate(3, 7));
		this.board.setPieceAt(Piece.BLACK_KING, new Coordinate(4, 7));
		this.board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(5, 7));
		this.board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(6, 7));
		this.board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(7, 7));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(x, 6));
		}

		this.board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		this.board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(1, 0));
		this.board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(2, 0));
		this.board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(3, 0));
		this.board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		this.board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(5, 0));
		this.board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(6, 0));
		this.board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(7, 0));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(x, 1));
		}
	}

	private void addMove(Move move) {

		addRegularMove(move);

		if (move.getType() == MoveType.CASTLING) {
			addCastling(move);
		} else if (move.getType() == MoveType.EN_PASSANT) {
			addEnPassant(move);
		}

		this.board.getMoveHistory().add(move);
	}

	private void addRegularMove(Move move) {
		Piece movedPiece = this.board.getPieceAt(move.getFrom());
		this.board.setPieceAt(null, move.getFrom());
		this.board.setPieceAt(movedPiece, move.getTo());

		performPromotion(move, movedPiece);
	}

	private void performPromotion(Move move, Piece movedPiece) {
		if (movedPiece == Piece.WHITE_PAWN && move.getTo().getY() == (Board.SIZE - 1)) {
			this.board.setPieceAt(Piece.WHITE_QUEEN, move.getTo());
		}
		if (movedPiece == Piece.BLACK_PAWN && move.getTo().getY() == 0) {
			this.board.setPieceAt(Piece.BLACK_QUEEN, move.getTo());
		}
	}

	private void addCastling(Move move) {
		if (move.getFrom().getX() > move.getTo().getX()) {
			Piece rook = this.board.getPieceAt(new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() + 1, move.getTo().getY()));
		} else {
			Piece rook = this.board.getPieceAt(new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() - 1, move.getTo().getY()));
		}
	}

	private void addEnPassant(Move move) {
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		this.board.setPieceAt(null, lastMove.getTo());
	}

	private Move validateMove(Coordinate from, Coordinate to) throws InvalidMoveException, KingInCheckException {
		// TODO please add implementation here
		// zrobic osobna metode z tego, sprawdzenie czy nie jest poza plansza
		if (from.getX() > 7 || from.getY() > 7 || to.getX() > 7 || to.getY() > 7) {
			throw new InvalidMoveException();
		}

		Piece piece = board.getPieceAt(from);

		// sprawdzenie czy to nie jest puste pole
		if (piece == null) {
			throw new InvalidMoveException();
		}

		Color nextMoveColor = calculateNextMoveColor();
		// osobna metoda sprawdzenie czy to jest twoja figura
		if (!piece.getColor().equals(calculateNextMoveColor())) {
			throw new InvalidMoveException();
		}

		//dopisac jeszcze zeby nie dalo sie zbic krola i to przetestowac
		
		MoveType moveType;
		if (board.getPieceAt(to) != null && board.getPieceAt(to).getColor() != nextMoveColor) {
			moveType = MoveType.CAPTURE;
		} else {
			moveType = MoveType.ATTACK;
		}

		Context context = returningContext(piece);

		if (!context.checkIfPieceCanMoveTo(piece, from, to, moveType)) {
			throw new InvalidMoveException();
		}

		Move move = new Move();
		move.setFrom(from);
		move.setTo(to);
		move.setMovedPiece(piece);
		move.setType(moveType);

		if (!piece.getType().equals(PieceType.KNIGHT)) {
			context.checkIfRoadToPieceDestinationIsEmpty(from, to, board);
		}

		// sprawdzic czy krol jest szachowany
		if (isKingInCheck(nextMoveColor)) {
			throw new KingInCheckException();
		}

		return move;
	}

	private boolean isKingInCheck(Color kingColor) throws InvalidMoveException {
		// TODO please add implementation here

		Coordinate kingCoordinate = new Coordinate(0, 0);

		// jakos to przerobic
		outer: for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Coordinate coordinate = new Coordinate(x, y);
				Piece piece = board.getPieceAt(coordinate);
				if (piece != null) {
					if (piece.getType().equals(PieceType.KING) && piece.getColor().equals(kingColor)) {
						kingCoordinate = coordinate;
						break outer;
					}
				}
			}
		}
		Context context = null;

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Coordinate coordinate = new Coordinate(x, y);
				Piece piece = board.getPieceAt(coordinate);
				if (piece != null) {
					context = returningContext(piece);
					if (piece.getColor() != kingColor) {
						if (context.checkIfPieceCanMoveTo(piece, coordinate, kingCoordinate, MoveType.CAPTURE)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean isAnyMoveValid(Color nextMoveColor) throws InvalidMoveException {

		// TODO please add implementation here
		Context context = null;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Coordinate coordinate = new Coordinate(x, y);
				Piece piece = board.getPieceAt(coordinate);
				if (piece != null && piece.getColor().equals(nextMoveColor)) {
					context = returningContext(piece);
					for (int i = 0; i < 8; i++) {
						for (int j = 0; j < 8; j++) {
							Coordinate coordinateTo = new Coordinate(i, j);
							if (context.checkIfPieceCanMoveTo(piece, coordinate, coordinateTo, MoveType.ATTACK)) {
								if (piece.getType().equals(PieceType.KING)) {
									for (int a = 0; a < 8; a++) {
										for (int b = 0; b < 8; b++) {
											coordinate = new Coordinate(a, b);
											if (board.getPieceAt(coordinate) != null
													&& !board.getPieceAt(coordinate).getColor().equals(nextMoveColor)) {
												piece = board.getPieceAt(coordinate);
												context = returningContext(piece);
												if (context.checkIfPieceCanMoveTo(piece, coordinate, coordinateTo,
														MoveType.CAPTURE)) {
													return false;
												}
											}
										}
									}
								} else {
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;

	}

	private Color calculateNextMoveColor() {
		if (this.board.getMoveHistory().size() % 2 == 0) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}

	private int findLastNonAttackMoveIndex() {
		int counter = 0;
		int lastNonAttackMoveIndex = 0;

		for (Move move : this.board.getMoveHistory()) {
			if (move.getType() != MoveType.ATTACK) {
				lastNonAttackMoveIndex = counter;
			}
			counter++;
		}

		return lastNonAttackMoveIndex;
	}

	private Context returningContext(Piece piece) {
		if (piece.getType().equals(PieceType.BISHOP)) {
			return new Context(new BishopValidator());
		} else if (piece.getType().equals(PieceType.ROOK)) {
			return new Context(new RookValidator());
		} else if (piece.getType().equals(PieceType.KING)) {
			return new Context(new KingValidator());
		} else if (piece.getType().equals(PieceType.QUEEN)) {
			return new Context(new QueenValidator());
		} else if (piece.getType().equals(PieceType.KNIGHT)) {
			return new Context(new KnightValidator());
		} else {
			return new Context(new PawnValidator());
		}
	}

}
