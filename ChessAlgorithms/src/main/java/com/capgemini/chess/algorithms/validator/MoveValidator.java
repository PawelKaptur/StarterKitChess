package com.capgemini.chess.algorithms.validator;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class MoveValidator {

	public boolean moveValidation(Piece piece, Coordinate from, Coordinate to) throws InvalidMoveException {
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

}
