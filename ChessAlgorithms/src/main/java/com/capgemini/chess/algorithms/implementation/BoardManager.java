package com.capgemini.chess.algorithms.implementation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidCoordinatesException;
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

	private Map<Coordinate, Piece> whitePieces = new HashMap<>();
	private Map<Coordinate, Piece> blackPieces = new HashMap<>();

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
		if (blackPieces.size() == 0 && whitePieces.size() == 0) {
			addPiecesToLists();
		}

		checkIfCoordinatesAreOutOfBound(from, to);

		Piece piece = checkIfPieceIsInCoordinateFrom(from);

		Color nextMoveColor = calculateNextMoveColor();

		checkIfPieceIsOurs(piece, nextMoveColor);
		checkIfPieceInCoordinateToIsOpponents(to, nextMoveColor);

		MoveType moveType = setMoveType(to, nextMoveColor);
		Context context = returningContext(piece);

		checkIfPieceCanMoveTo(piece, from, to, moveType);

		Move move = setMove(piece, from, to, moveType);

		checkIfRoadToPieceDestinationIsEmpty(piece, from, to, context);

		updateLists(move);
		
		if (isKingInCheck(nextMoveColor)) {
			addPiecesToLists();
			throw new KingInCheckException();
		}
		
		return move;
	}

	private void checkIfRoadToPieceDestinationIsEmpty(Piece piece, Coordinate from, Coordinate to, Context context)
			throws InvalidMoveException {
		if (!piece.getType().equals(PieceType.KNIGHT)) {
			context.checkIfRoadToPieceDestinationIsEmpty(from, to, board);
		}
	}

	private Move setMove(Piece piece, Coordinate from, Coordinate to, MoveType moveType) {
		Move move = new Move();
		move.setFrom(from);
		move.setTo(to);
		move.setMovedPiece(piece);
		move.setType(moveType);
		return move;
	}

	private void checkIfPieceCanMoveTo(Piece piece, Coordinate from, Coordinate to, MoveType moveType)
			throws InvalidMoveException {
		Context context = returningContext(piece);
		if (!context.checkIfPieceCanMoveTo(piece, from, to, moveType)) {
			throw new InvalidMoveException();
		}
	}

	private MoveType setMoveType(Coordinate to, Color nextMoveColor) {
		if (board.getPieceAt(to) != null && board.getPieceAt(to).getColor() != nextMoveColor) {
			return MoveType.CAPTURE;
		}

		return MoveType.ATTACK;
	}

	private void checkIfPieceInCoordinateToIsOpponents(Coordinate to, Color nextMoveColor) throws InvalidMoveException {
		Color colorOfDestinationPiece = null;

		if (board.getPieceAt(to) != null) {
			colorOfDestinationPiece = board.getPieceAt(to).getColor();
		}

		if (colorOfDestinationPiece != null && colorOfDestinationPiece.equals(nextMoveColor)) {
			throw new InvalidMoveException();
		}
	}

	private void checkIfPieceIsOurs(Piece piece, Color nextMoveColor) throws InvalidMoveException {
		if (!piece.getColor().equals(nextMoveColor)) {
			throw new InvalidMoveException();
		}
	}

	private Piece checkIfPieceIsInCoordinateFrom(Coordinate from) throws InvalidMoveException {
		Piece piece = board.getPieceAt(from);
		if (piece == null) {
			throw new InvalidMoveException();
		}

		return piece;
	}

	private void checkIfCoordinatesAreOutOfBound(Coordinate from, Coordinate to) throws InvalidMoveException {
		double middleOfPossibleCoordinate = 3.5;
		boolean checkIfFromCoordinateisValid = Math
				.abs(from.getX() - middleOfPossibleCoordinate) > middleOfPossibleCoordinate
				|| Math.abs(to.getX() - middleOfPossibleCoordinate) > middleOfPossibleCoordinate;
		boolean checkIfToCoordinateisValid = Math
				.abs(from.getY() - middleOfPossibleCoordinate) > middleOfPossibleCoordinate
				|| Math.abs(to.getY() - middleOfPossibleCoordinate) > middleOfPossibleCoordinate;
		if (checkIfFromCoordinateisValid || checkIfToCoordinateisValid) {
			throw new InvalidCoordinatesException();
		}

	}

	private boolean isKingInCheck(Color kingColor) throws InvalidMoveException {
		// TODO please add implementation here
		if (whitePieces.size() == 0 && blackPieces.size() == 0) {
			addPiecesToLists();
		}
		
		Coordinate kingCoordinate = null;
		if (kingColor.equals(Color.WHITE)) {
			kingCoordinate = getCoordinatesByKing(whitePieces, Piece.WHITE_KING);
		} else {
			kingCoordinate = getCoordinatesByKing(blackPieces, Piece.BLACK_KING);
		}
	
		if (kingCoordinate == null) {
			return false;
		}
		
		return checkingIsKingInCheck(kingCoordinate, kingColor);
	}

	private boolean isAnyMoveValid(Color nextMoveColor) throws InvalidMoveException {

		// TODO please add implementation here
		Map<Coordinate, Piece> ourPieces;
		Map<Coordinate, Piece> opponentPieces;
		if (nextMoveColor.equals(Color.WHITE)) {
			ourPieces = whitePieces;
			opponentPieces = blackPieces;
		} else {
			ourPieces = blackPieces;
			opponentPieces = whitePieces;
		}

		return checkIsAnyMoveValid(ourPieces, opponentPieces);
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

	private void addPiecesToLists() {
		whitePieces = new HashMap<Coordinate, Piece>();
		blackPieces = new HashMap<Coordinate, Piece>();
		for (int x = 0; x < Board.SIZE; x++) {
			for (int y = 0; y < Board.SIZE; y++) {
				Coordinate coordinate = new Coordinate(x, y);
				Piece piece = board.getPieceAt(coordinate);
				if (piece != null && piece.getColor().equals(Color.WHITE)) {
					whitePieces.put(coordinate, piece);
				} else if (piece != null && piece.getColor().equals(Color.BLACK)) {
					blackPieces.put(coordinate, piece);
				}
			}
		}
	}

	private void updateLists(Move move) {
		if (move.getMovedPiece().getColor().equals(Color.WHITE)) {
			whitePieces.put(move.getTo(), move.getMovedPiece());
			whitePieces.remove(move.getFrom());
			blackPieces.remove(move.getTo());
		} else {
			blackPieces.put(move.getTo(), move.getMovedPiece());
			blackPieces.remove(move.getFrom());
			whitePieces.remove(move.getTo());
		}
	}

	private boolean checkingIsKingInCheck(Coordinate kingCoordinate, Color kingColor) {
		if (kingColor.equals(Color.WHITE)) {
			return checkingIsWhiteKingInCheck(kingCoordinate);
		} else {
			return checkingIsBlackKingInCheck(kingCoordinate);
		}
	}

	// 2 nastpene metody pewnie mozna jakos zuniwersalizowac
	private boolean checkingIsWhiteKingInCheck(Coordinate kingCoordinate){
		Context context = null;
		for (Map.Entry<Coordinate, Piece> entry : blackPieces.entrySet()) {
			Coordinate coordinate = entry.getKey();
			Piece piece = entry.getValue();
			context = returningContext(piece);
			
			try {
				checkIfPieceInCoordinateToIsOpponents(kingCoordinate, Color.WHITE);
			} catch (InvalidMoveException e1) {
				
			}
			
			MoveType moveType = setMoveType(kingCoordinate, Color.WHITE);
			if (context.checkIfPieceCanMoveTo(piece, coordinate, kingCoordinate, moveType)) {
				try {
					Board fakeBoard = creatingFakeBoard(piece, coordinate, kingCoordinate);
					context.checkIfRoadToPieceDestinationIsEmpty(coordinate, kingCoordinate, fakeBoard);
					return true;
				} catch (InvalidMoveException e) {
					continue;
				}
			}
		}
		return false;
	}

	private boolean checkingIsBlackKingInCheck(Coordinate kingCoordinate) {
		Context context = null;
		for (Map.Entry<Coordinate, Piece> entry : whitePieces.entrySet()) {
			Coordinate coordinate = entry.getKey();
			Piece piece = entry.getValue();
			context = returningContext(piece);
			
			try {
				checkIfPieceInCoordinateToIsOpponents(kingCoordinate, Color.BLACK);
			} catch (InvalidMoveException e1) {
				
			}
			
			MoveType moveType = setMoveType(kingCoordinate, Color.BLACK);
			if (context.checkIfPieceCanMoveTo(piece, coordinate, kingCoordinate, moveType)) {
				try {
					Board fakeBoard = creatingFakeBoard(piece, coordinate, kingCoordinate);
					context.checkIfRoadToPieceDestinationIsEmpty(coordinate, kingCoordinate, fakeBoard);
					return true;
				} catch (InvalidMoveException e) {
					continue;
				}
			}
		}
		return false;
	}

	private boolean checkIsAnyMoveValid(Map<Coordinate, Piece> ourPieces, Map<Coordinate, Piece> opponentPieces)
			throws InvalidMoveException {
		Context context = null;
		boolean pieceCanGoSomewhereOnBoard = false;
		for (Map.Entry<Coordinate, Piece> entry : ourPieces.entrySet()) {
			Coordinate coordinateFrom = entry.getKey();
			Piece piece = entry.getValue();
			context = returningContext(piece);
			pieceCanGoSomewhereOnBoard = canPiecesGoSomewhereOnBoard(context, coordinateFrom, piece, opponentPieces);
			if (pieceCanGoSomewhereOnBoard) {
				return true;
			}
		}
		return false;
	}

	private boolean canPiecesGoSomewhereOnBoard(Context context, Coordinate coordinateFrom, Piece piece,
			Map<Coordinate, Piece> opponentPieces) throws InvalidMoveException {
		for (int x = 0; x < Board.SIZE; x++) {
			for (int y = 0; y < Board.SIZE; y++) {
				Coordinate coordinateTo = new Coordinate(x, y);
				Color color = piece.getColor();
			
				try {
					checkIfPieceInCoordinateToIsOpponents(coordinateTo, color);
				} catch (InvalidMoveException e1) {
					
				}
				MoveType moveType = setMoveType(coordinateTo, color);
				
				if (context.checkIfPieceCanMoveTo(piece, coordinateFrom, coordinateTo, moveType)) {
					Move move = setMove(piece, coordinateFrom, coordinateTo, moveType);
					updateLists(move);
					if (piece.getType().equals(PieceType.KING)) {
						if (checkIfNewPositionOfKingWillBeChecked(coordinateTo, opponentPieces)) {
							addPiecesToLists();
							continue;
						}
						return true;
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean checkIfNewPositionOfKingWillBeChecked(Coordinate coordinateTo,
			Map<Coordinate, Piece> opponentPieces) throws InvalidMoveException {
		Context context;
		Piece piece;
		Coordinate coordinateFrom;

		for (Map.Entry<Coordinate, Piece> opponentEntry : opponentPieces.entrySet()) {
			coordinateFrom = opponentEntry.getKey();
			piece = opponentEntry.getValue();
			context = returningContext(piece);
			Color color;
			if(piece.getColor().equals(Color.BLACK)){
				color = Color.WHITE;
			}
			else{
				color = Color.BLACK;
			}
			
			try {
				checkIfPieceInCoordinateToIsOpponents(coordinateTo, color);
			} catch (InvalidMoveException e1) {
				
			}
			
			MoveType moveType = setMoveType(coordinateTo, color);

			if (context.checkIfPieceCanMoveTo(piece, coordinateFrom, coordinateTo, moveType)) {
				try {
					Board fakeBoard = creatingFakeBoard(piece, coordinateFrom, coordinateTo);
					context.checkIfRoadToPieceDestinationIsEmpty(coordinateFrom, coordinateTo, fakeBoard);
					return true;
				} catch (InvalidMoveException e) {
					continue;
				}
			}
		}
		return false;
	}

	private Board creatingFakeBoard(Piece piece, Coordinate from, Coordinate to) {
		Board fakeBoard = new Board();
		for (Coordinate coordinate : whitePieces.keySet()) {
			fakeBoard.setPieceAt(whitePieces.get(coordinate), coordinate);
		}

		for (Coordinate coordinate : blackPieces.keySet()) {
			fakeBoard.setPieceAt(blackPieces.get(coordinate), coordinate);
		}

		fakeBoard.setPieceAt(piece, to);
		fakeBoard.setPieceAt(null, from);

		return fakeBoard;
	}

	public static <Coordinate, Piece> Coordinate getCoordinatesByKing(Map<Coordinate, Piece> map, Piece value) {
		
		for (Entry<Coordinate, Piece> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return (Coordinate) entry.getKey();
			}
		}
		return null;
	}
}
