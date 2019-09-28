package com.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.controller.chesslib.game.*;
import com.controller.chesslib.game.Event.Event;
import com.controller.chesslib.move.*;
import com.controller.chesslib.pgn.*;
import com.controller.chesslib.util.*;
import com.controller.chesslib.*;

/**
 * Servlet implementation class Engine
 */
public class Engine extends HttpServlet {
	static Move bestmove;
	static int d = 4;
	private static final long serialVersionUID = 1L;
	static int counter =0;
	static ArrayList<Double> alphabeta;
	static {
		alphabeta = new ArrayList<Double>();
		for (int i = 0; i < d; i++) {
			alphabeta.add(null);
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Engine() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fen = (String) request.getParameter("fen");
		fen = "r1bqkb1r/pppp1ppp/2n2n2/4p1N1/2B1P3/8/PPPP1PPP/RNBQK2R b KQkq -";
		Board board = new Board();
		board.loadFromFen(fen);
		boolean isMax = board.getSideToMove() == Enum.valueOf(Side.class, "WHITE") ? true : false;
		try {
			System.out.println(Minimax(d, board, isMax));
			System.out.println(bestmove);
		} catch (MoveGeneratorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.getWriter().append(bestmove.toString().substring(0, 2) + "-" + bestmove.toString().substring(2));
	}

	static double Minimax(int depth, Board board, boolean isMax) throws MoveGeneratorException {
		// remove alpha and beta from ArrayList before exiting scope
		double minmax_score;

		if (depth == 1) {
			// call evaluate on all moves and determine minmax_score

			if (isMax) {
//				first element is alpha
				double score;
				minmax_score = -1000000;
				MoveList moves = MoveGenerator.generateLegalMoves(board);
				System.out.println(counter++ +"----"+depth);

				for (Move move : moves) {
					if (!checkCut(depth, isMax)) {
						board.doMove(move);

						score = Evaluate(board);
						if (alphabeta.get(0) == null) {
							alphabeta.add(0, score);
						} else if (alphabeta.get(0) < score) {
							alphabeta.remove(0);
							alphabeta.add(0, score);
						}
						board.undoMove();
						if (score > minmax_score) {
							minmax_score = score;
						}
					} else {
						break;
					}
				}
			} else {
//				first element is beta
				double score;
				minmax_score = +1000000;
				MoveList moves = MoveGenerator.generateLegalMoves(board);
				System.out.println(counter++ +"----"+depth);
				for (Move move : moves) {
					if (!checkCut(depth, isMax)) {
						board.doMove(move);

						score = Evaluate(board);
						if (alphabeta.get(0) == null) {
							alphabeta.add(0, score);
						} else if (alphabeta.get(0) > score) {
							alphabeta.remove(0);
							alphabeta.add(score);
						}
						if (score < minmax_score) {
							minmax_score = score;
						}
						board.undoMove();
					} else {
						break;
					}
				}
			}
			alphabeta.remove(depth - 1);
			alphabeta.add(depth - 1, null);
			return minmax_score;
		} else {
			// call minimax on all moves with int depth = depth - 1
			if (isMax) {
				double score;
				minmax_score = -100000;
				MoveList moves = MoveGenerator.generateLegalMoves(board);
				System.out.println(counter++ +"----"+depth);
				for (Move move : moves) {
					if (!checkCut(depth, isMax)) {
						board.doMove(move);
						score = Minimax(depth - 1, board, !isMax);
						if (alphabeta.get(depth - 1) == null) {
							alphabeta.add(score);
						} else if (alphabeta.get(depth - 1) < score) {
							alphabeta.remove(depth - 1);
							alphabeta.add(depth - 1, score);
						}

						if (score > minmax_score) {
							minmax_score = score;
							if (d == depth) {
								bestmove = move;
							}
						}
						board.undoMove();
					}
					else {
						break;
					}
				}
			} else {
				minmax_score = 1000000;
				MoveList moves = MoveGenerator.generateLegalMoves(board);
				System.out.println(counter++ +"----"+depth);
				double score;
				for (Move move : moves) {
					if (!checkCut(depth, isMax)) {
						board.doMove(move);
						score = Minimax(depth - 1, board, !isMax);
						if (alphabeta.get(depth - 1) == null) {
							alphabeta.add(score);
						} else if (alphabeta.get(depth - 1) > score) {
							alphabeta.remove(depth - 1);
							alphabeta.add(depth - 1, score);
						}

						if (score < minmax_score) {
							minmax_score = score;
							if (d == depth) {
								bestmove = move;
							}
						}
						board.undoMove();
					}
					else {
						break;
					}
				}
			}
			alphabeta.remove(depth - 1);
			alphabeta.add(depth - 1, null);
			return minmax_score;
		}
	}

	private static boolean checkCut(int depth, boolean isMax) {
		if (isMax) {
			for (int i = depth; i < alphabeta.size(); i += 2) {
				if (alphabeta.get(i) != null && alphabeta.get(depth - 1) != null
						&& alphabeta.get(depth - 1) >= alphabeta.get(i)) {
					System.out.println("--------");
					return true;
				}
			}
		} else {
			for (int i = depth; i < alphabeta.size(); i += 2) {
				if (alphabeta.get(i) != null && alphabeta.get(depth - 1) != null
						&& alphabeta.get(depth - 1) <= alphabeta.get(i)) {
					System.out.println("--------");
					return true;
				}
			}
		}

		return false;
	}

	// takes board object input and returns double evaluation
	static double Evaluate(Board board) {
		double evaluation = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				String square = (char) (65 + x) + Integer.toString(8 - y);
				Piece piece = board.getPiece(Enum.valueOf(Square.class, square));
				evaluation += getPieceValue(piece, x, y);
			}
		}
		return evaluation;
	}

	// takes piece object and returns its double value
	static double getPieceValue(Piece piece, int x, int y) {
		// y is along y axis and x is along x axis
		double[][] pawnEval = { { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, { 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0 },
				{ 1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 1.0, 1.0 }, { 0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5 },
				{ 0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0 }, { 0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5 },
				{ 0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5 }, { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 } };

		double[][] knightEval = { { -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0 },
				{ -4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0 }, { -3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0 },
				{ -3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0 }, { -3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0 },
				{ -3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0 }, { -4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0 },
				{ -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0 } };

		double[][] bishopEval = { { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0 },
				{ -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0 }, { -1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0 },
				{ -1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0 }, { -1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0 },
				{ -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0 }, { -1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0 },
				{ -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0 } };

		double[][] rookEval = { { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, { 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5 },
				{ -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 }, { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
				{ -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 }, { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
				{ -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 }, { 0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0 } };

		double[][] queenEval = { { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0 },
				{ -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0 }, { -1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0 },
				{ -0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5 }, { 0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5 },
				{ -1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0 }, { -1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0 },
				{ -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0 } };

		double[][] kingEval = {

				{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 }, { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
				{ -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 }, { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
				{ -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0 }, { -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0 },
				{ 2.0, 2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 2.0 }, { 2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0 } };

		if (piece.getPieceSide() == Side.WHITE) {
			if (piece.getPieceType() == PieceType.PAWN) {
				return (10 + pawnEval[y][x]);
			} else if (piece.getPieceType() == PieceType.ROOK) {
				return (50 + rookEval[y][x]);
			} else if (piece.getPieceType() == PieceType.KNIGHT) {
				return 30 + knightEval[y][x];
			} else if (piece.getPieceType() == PieceType.BISHOP) {
				return 30 + bishopEval[y][x];
			} else if (piece.getPieceType() == PieceType.QUEEN) {
				return 90 + queenEval[y][x];
			} else if (piece.getPieceType() == PieceType.KING) {
				return (900 + kingEval[y][x]);
			} else {
				return 0.0;
			}
		} else {
			if (piece.getPieceType() == PieceType.PAWN) {
				return -(10 + pawnEval[7 - y][x]);
			} else if (piece.getPieceType() == PieceType.ROOK) {
				return -(50 + rookEval[7 - y][x]);
			} else if (piece.getPieceType() == PieceType.KNIGHT) {
				return -(30 + knightEval[7 - y][x]);
			} else if (piece.getPieceType() == PieceType.BISHOP) {
				return -(30 + bishopEval[7 - y][x]);
			} else if (piece.getPieceType() == PieceType.QUEEN) {
				return -(90 + queenEval[7 - y][x]);
			} else if (piece.getPieceType() == PieceType.KING) {
				return -(900 + kingEval[7 - y][x]);
			} else {
				return 0.0;
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
