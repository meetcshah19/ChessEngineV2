<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chess Engine</title>
<style>
		@import
			url(https://)fonts.googleapis.com/css?
			family=Exo:100,200,400);
		body
		{
			background-repeat: no-repeat;
			background-size: cover;
			background-color: #FFD88B;
			background-image: url(https://previews.123rf.com/images/1stchoice/1stchoice1506/1stchoice150600018/41591970-white-grey-background-in-soft-grunge-style-concrete-texture.jpg);
			opacity: 1;
		}
		h1
		{
			text-align: center;
			font-size: 80px;
			color: #7F7373;
			font-family: 'Exo', sans-serif;
		}
		.myBoard
		{
			margin-left: 400px;
		}
		.button
		{
			border: none;
			padding: 15px 150px;
			border-radius: 25px;
			background-color: blue;
			align-self: center;
			font-size: 20px;
			color: white
			text-align: center;
			display: inline-block;
			cursor: pointer;

		}
		.button1
		{
			margin: 20px 230px 20px 220px;

		}
		.button2
		{
			margin: 20px 666px;
		}
	</style>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="js/chessboard-1.0.0.js"></script>
<script src="js/chess.js"></script>
<link rel="stylesheet" href="css/chessboard-1.0.0.css">

</head>
<body>
	
	
	
	
	
	
	
	
	
	
	
	
	
	<h1> WELCOME <%=request.getSession().getAttribute("username")%> </h1>
	<table>
		<tr>
			<td>
			<pre>                                                .</pre>
			</td>
			<td>
	<div id="myBoard" style="width: 550px" ></div>
	</td></tr></table>
	<br><br>
	<table>
		<tr>
<td>
	<button type="button" class="button button2" onclick="window.location.href='theme/analysis.jsp'">Analysis</button>
</td>
</tr>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	<script>
		var board = null
		var $board = $('#myBoard')
		var game = new Chess()
		var squareToHighlight = null
		var squareClass = 'square-55d63'
		
		
		
		
		function removeHighlights(color) {
			$board.find('.' + squareClass).removeClass('highlight-' + color)
		}

		function onDragStart(source, piece, position, orientation) {
			// do not pick up pieces if the game is over
			if (game.game_over())
				return false

				// only pick up pieces for White
			if (piece.search(/^b/) !== -1)
				return false
		}

		function makeRandomMove() {
			
			
			var possibleMoves = game.moves({
				verbose : true
			})
			callServletwithAjax();
			/* // game over
			if (possibleMoves.length === 0)
				return

			

			var randomIdx = Math.floor(Math.random() * possibleMoves.length)
			var move = possibleMoves[randomIdx]
			game.move(move.san)

			// highlight black's move
			removeHighlights('black')
			$board.find('.square-' + move.from).addClass('highlight-black')
			squareToHighlight = move.to

			// update the board to the new position
			board.position(game.fen()) */
		}

		function onDrop(source, target) {
			// see if the move is legal
			
			var move = game.move({
				from : source,
				to : target,
				promotion : 'q' // NOTE: always promote to a queen for example simplicity
			})

			// illegal move
			if (move === null)
				return 'snapback'

				// highlight white's move
			removeHighlights('white')
			$board.find('.square-' + source).addClass('highlight-white')
			$board.find('.square-' + target).addClass('highlight-white')

			// make random move for black
			if(game.in_checkmate()){
				var pgn = game.pgn()
				console.log("57165761");
				console.log(pgn);
				var xmlhttp=new XMLHttpRequest();
				xmlhttp.open("GET", "http://localhost:8080/ChessEngine/SaveGame?pgn="+pgn,true);
				xmlhttp.send();

			}

			else if(game.in_draw() | game.in_stalemate() | game.in_threefold_repetition()){
				var pgn = game.pgn()
			}

			else{
			window.setTimeout(makeRandomMove, 250)
		    }		}

		function onMoveEnd() {
			$board.find('.square-' + squareToHighlight).addClass(
					'highlight-black')
		}

		// update the board position after the piece snap
		// for castling, en passant, pawn promotion
		function onSnapEnd() {
			board.position(game.fen())
		}

		function onMouseoverSquare(square, piece) {
			// get list of possible moves for this square
			var moves = game.moves({
				square : square,
				verbose : true
			})

			// exit if there are no moves available for this square
			if (moves.length === 0)
				return

			
 // highlight the square they moused over
			greySquare(square)

			// highlight the possible squares for this piece
			for (var i = 0; i < moves.length; i++) {
				greySquare(moves[i].to)
			}
		}

		function onMouseoutSquare(square, piece) {
			removeGreySquares()
		}
		var whiteSquareGrey = '#a9a9a9'
		var blackSquareGrey = '#696969'

		function removeGreySquares() {
			$('#myBoard .square-55d63').css('background', '')
		}

		function greySquare(square) {
			var $square = $('#myBoard .square-' + square)

			var background = whiteSquareGrey
			if ($square.hasClass('black-3c85d')) {
				background = blackSquareGrey
			}

			$square.css('background', background)
		}

		var config = {
			draggable : true,
			position : 'start',
			onDragStart : onDragStart,
			onDrop : onDrop,
			onMouseoutSquare : onMouseoutSquare,
			onMouseoverSquare : onMouseoverSquare,
			onMoveEnd : onMoveEnd,
			onSnapEnd : onSnapEnd
		}
		function callServletwithAjax(){
			var xmlhttp=new XMLHttpRequest();
			xmlhttp.onreadystatechange=function()
			{
			    if (xmlhttp.readyState==4 && xmlhttp.status==200)
			    {
			         if(xmlhttp.responseText.split(".").length == 2 && xmlhttp.responseText.split(".")[0] == 'mate'){
				         	//user is mated
				         	console.log(xmlhttp.responseText.split(".")[1]);
				         	board.move(xmlhttp.responseText.split(".")[1]);
				            game.move(xmlhttp.responseText.split(".")[1], {sloppy: true});
				         	var pgn = game.pgn()
				        	xmlhttp=new XMLHttpRequest();
							xmlhttp.open("GET", "http://localhost:8080/ChessEngine/SaveGame?pgn="+pgn,true);
							xmlhttp.send();

				         }
				         else if(xmlhttp.responseText.split(".").length == 2 && xmlhttp.responseText.split(".")[0] == 'draw'){
				         	//engine plays move to draw
				         	board.move(xmlhttp.responseText.split(".")[1]);
				            game.move(xmlhttp.responseText.split(".")[1], {sloppy: true});
				         	var pgn = chess.pgn()
				        	xmlhttp=new XMLHttpRequest();
							xmlhttp.open("GET", "http://localhost:8080/ChessEngine/SaveGame?pgn="+pgn,true);
							xmlhttp.send();

				         }
				         else{
				        	 console.log(xmlhttp.responseText);
				         board.move(xmlhttp.responseText);
				         game.move(xmlhttp.responseText, {sloppy: true});
					    }
			    }
			}; 
			
			
			
			var params="fen="+game.fen();
			xmlhttp.open("GET", "http://localhost:8080/ChessEngine/Engine?"+params,true);
			xmlhttp.send();
						
		}

		board = Chessboard('myBoard', config)
	</script>
	
</body>
</html>