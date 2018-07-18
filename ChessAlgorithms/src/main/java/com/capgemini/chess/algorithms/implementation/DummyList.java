package com.capgemini.chess.algorithms.implementation;

import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Piece;

public class DummyList {
	  private Map<Coordinate, Piece> dummyList;

	  public DummyList(DummyList another) {
	    this.dummyList = another.dummyList;
	  }
}
