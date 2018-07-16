package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.BoardManager;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class MoveValidator {

	//ta metoda bedzie sprawdyala kilka innych metod, a mozliwosci ruchu przeniose
	public boolean moveValidation(Piece piece, Coordinate from, Coordinate to, MoveType moveType) throws InvalidMoveException {
		PieceType pieceType = piece.getType();
		
		switch (pieceType) {
		case KNIGHT:
			if (Math.abs(from.getX() - to.getX()) == 1 && Math.abs(from.getY() - to.getY()) == 2) {
				return true;
			} else if (Math.abs(from.getX() - to.getX()) == 2 && Math.abs(from.getX() - to.getX()) == 1) {
				return true;
			} else {
				throw new InvalidMoveException();
			}
		case ROOK:
			if(Math.abs(from.getX() - to.getX()) == 0 && Math.abs(from.getY() - to.getY()) > 0){
				return true;
			}
			else if(Math.abs(from.getX() - to.getX()) > 0 && Math.abs(from.getY() - to.getY()) ==  0){
				return true;
			}
			else{
				throw new InvalidMoveException();
			}
		case KING:
			if (Math.abs(from.getX() - to.getX()) == 1 && Math.abs(from.getY() - to.getY()) == 1) {
				return true;
			}
			else if (Math.abs(from.getX() - to.getX()) == 0 && Math.abs(from.getY() - to.getY()) == 1) {
				return true;
			}
			else if (Math.abs(from.getX() - to.getX()) == 1 && Math.abs(from.getY() - to.getY()) == 0) {
				return true;
			}
			else{
				throw new InvalidMoveException();
			}
		case BISHOP:
			if (Math.abs(from.getX() - to.getX()) == Math.abs(from.getY() - to.getY())) {
				return true;
			}
			else{
				throw new InvalidMoveException();
			}
		case QUEEN:
			if(from.getX() - to.getX() == 0 && Math.abs(from.getY() - to.getY()) > 0){
				return true;
			}
			else if(from.getX() - to.getX() > 0 && Math.abs(from.getY() - to.getY()) ==  0){
				return true;
			}
			else if (Math.abs(from.getX() - to.getX()) == Math.abs(from.getY() - to.getY())) {
				return true;
			}
			else{
				throw new InvalidMoveException();
			}
		case PAWN:
			if(piece.getColor().equals(Color.WHITE)){
				if (Math.abs(from.getX() - to.getX()) == 0 && from.getY() - to.getY() == -1) {
					return true;
				}
				else if(from.getY() == 1 && from.getX() - to.getX() == 0 && (from.getY() - to.getY() == -1 || from.getY() - to.getY() == -2)){
					return true;
				}
				else{
					throw new InvalidMoveException();
				}
			}
			else{
				if(Math.abs(from.getX() - to.getX()) == 0 && from.getY() - to.getY() == 1){
					return true;
				}
				else if(from.getY() == 6 && from.getX() - to.getX() == 0 && (from.getY() - to.getY() == 1 || from.getY() - to.getY() == 2)){
					return true;
				}
				else{
					throw new InvalidMoveException();
				}
			}
		default:
			throw new InvalidMoveException();
		}
//		if (piece.getType().equals(PieceType.KNIGHT)) {
//			if (from.getX() - to.getX() == Math.abs(1) && from.getX() - to.getX() == Math.abs(2)) {
//				return true;
//			} else if (from.getX() - to.getX() == Math.abs(2) && from.getX() - to.getX() == Math.abs(1)) {
//				return true;
//			} else {
//				return false;
//			}
//		}
	}
	
	
	public boolean EmptyRoad(Coordinate from, Coordinate to, Board board) throws InvalidMoveException{
		int fromX = from.getX();
		int fromY = from.getY();
		int toX = to.getX();
		int toY = to.getY();
		int changeX = Math.abs(fromX - toX);
		int changeY = Math.abs(fromY - toY);
		Piece piece = null;
		
		//ruchy na skos
		if(fromX != toX && fromY != toY){
			int change = Math.abs(from.getX() - to.getX());
			//ruch na skos prawo-gora
			if(fromX < toX && fromY < toY){
				for(int x = fromX + 1, y = fromY + 1; x < toX && y < toY; x++, y++){
					Coordinate coordinate = new Coordinate(x, y);
					if(board.getPieceAt(coordinate) != null){
						throw new InvalidMoveException();
					}
				}
			}
			
			//ruch na skos prawo-dol
			if(fromX < toX && fromY > toY){
				for(int x = fromX + 1, y = fromY - 1; x < toX && y > toY; x++, y--){
					Coordinate coordinate = new Coordinate(x, y);
					//System.out.println("x: " + x + " y: " + y);
					if(board.getPieceAt(coordinate) != null){
						throw new InvalidMoveException();
					}
				}
			}
			
			//ruch na skos lewo-dol
			if(fromX > toX && fromY > toY){
				for(int x = fromX - 1, y = fromY - 1; x > toX && y > toY; x--, y--){
					Coordinate coordinate = new Coordinate(x, y);
					if(board.getPieceAt(coordinate) != null){
						throw new InvalidMoveException();
					}
				}
			}
			
			//ruch na skos lewo-gora
			if(fromX > toX && fromY < toY){
				for(int x = fromX - 1, y = fromY + 1; x > toX && y < toY; x--, y++){
					Coordinate coordinate = new Coordinate(x, y);
					if(board.getPieceAt(coordinate) != null){
						throw new InvalidMoveException();
					}
				}
			}
			
		}
		
		//ruchy w poziomie
		if(fromX != toX && changeY == 0){
			int change = Math.abs(from.getX() - to.getX());
			//ruch na prawo
			if(fromX < toX){
				for(int x = fromX + 1; x < toX; x++){
					Coordinate coordinate = new Coordinate(x, toY);
					if(board.getPieceAt(coordinate) != null){
						throw new InvalidMoveException();
					}
				}
			}
			
			//ruch na lewo
			if(fromX > toX){
				for(int x = fromX - 1; x > toX; x--){
					Coordinate coordinate = new Coordinate(x, toY);
					if(board.getPieceAt(coordinate) != null){
						throw new InvalidMoveException();
					}
				}
			}
		}
		
		//ruchy w pionie
		if(changeX == 0 && fromY != toY){
			int change = Math.abs(from.getY() - to.getY());
			//ruch w gore
			if(fromY < toY){
				for(int y = fromY + 1; y < toY; y++){
					Coordinate coordinate = new Coordinate(toX, y);
					if(board.getPieceAt(coordinate) != null){
						throw new InvalidMoveException();
					}
				}
			}
			
			//ruch na dol
			if(fromY > toY){
				for(int y = fromY - 1; y > toY; y--){
					Coordinate coordinate = new Coordinate(toX, y);
					if(board.getPieceAt(coordinate) != null){
						throw new InvalidMoveException();
					}
				}
			}
		}
		
		return true;
	}

}
