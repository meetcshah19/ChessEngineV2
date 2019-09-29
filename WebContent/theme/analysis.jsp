<!DOCTYPE html>
<html lang="en">
  <head>
  <script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="assets/js/chessboard-1.0.0.js"></script>
<script src="assets/js/chess.js"></script>
<link rel="stylesheet" href="assets/css/chessboard-1.0.0.css">
  
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="assets/img/favicon.png">

    <title>FLATTY - Free Bootstrap 3 Landing Page</title>

    <!-- Bootstrap core CSS -->
    <link href="assets/css/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="assets/css/main.css" rel="stylesheet">

    <!-- Fonts from Google Fonts -->
	<link href='http://fonts.googleapis.com/css?family=Lato:300,400,900' rel='stylesheet' type='text/css'>
    
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          
          <a class="navbar-brand" href="#" style="font-size:40px;color:black;'"><b>Welcome UserName</b></a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
   
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

	<div id="headerwrap">
	<div id="myBoard" style="width: 550px;;margin-left:450px;" ></div>
	<h1 align="center" id="move">E2-E4</h1>

	<br>
	<br>
	<br>
	
	</div><!-- /headerwrap -->
	
		<div class="container">
		<hr>
		<div class="row centered">
			<div class="col-lg-6 col-lg-offset-3">
				  <button class="btn btn-warning btn-lg" onclick="callServletwithAjax()">Evaluate</button>
				  <button  class="btn btn-warning btn-lg">Previous Game</button>
				  <button  class="btn btn-warning btn-lg" onclick = "backwardarrow()"> <- </button>
				  <button  class="btn btn-warning btn-lg" onclick="forwardarrow()">-></button>
			</div>
			<div class="col-lg-3"></div>
		</div><!-- /row -->
		<hr>
		
	</div><!-- /container -->
	

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
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
				xmlhttp.open("GET", "http://chessengine.app.sdslabs.tech/ChessEngine/SaveGame?pgn="+pgn,true);
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
							xmlhttp.open("GET", "http://chessengine.app.sdslabs.tech/ChessEngine/SaveGame?pgn="+pgn,true);
							xmlhttp.send();

				         }
				         else if(xmlhttp.responseText.split(".").length == 2 && xmlhttp.responseText.split(".")[0] == 'draw'){
				         	//engine plays move to draw
				         	board.move(xmlhttp.responseText.split(".")[1]);
				            game.move(xmlhttp.responseText.split(".")[1], {sloppy: true});
				         	var pgn = chess.pgn()
				        	xmlhttp=new XMLHttpRequest();
							xmlhttp.open("GET", "http://chessengine.app.sdslabs.tech/ChessEngine/SaveGame?pgn="+pgn,true);
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
		var n=0;
		game.load_pgn(' 1. e4 Nc6 2. Ke2 Nd4  3. Kd3 e5 4. Kc4 d6 5. Kd5 Be6');
		var config = {
			draggable : true,
			position : game.fen(),
			onDragStart : onDragStart,
			onDrop : onDrop,
			onMouseoutSquare : onMouseoutSquare,
			onMouseoverSquare : onMouseoverSquare,
			onMoveEnd : onMoveEnd,
			onSnapEnd : onSnapEnd
		}
		board = Chessboard('myBoard', config)
		var movearray = game.history()
		game = new Chess();
		board.position(game.fen())
		console.log(movearray)
		function forwardarrow(){
			console.log('in')
			if(n < movearray.length){
			n++;
			console.log(n);
			game.move(movearray[n-1]);
			board.position(game.fen());
			}
		}	
		
		function backwardarrow(){
			if(n != 0){
			n--;
			game.undo();
			board.position(game.fen());
		    }
		}
		
		
		
		function callServletwithAjax(){
			if(n%2==0){
			console.log('in');
			var xmlhttp=new XMLHttpRequest();
			xmlhttp.onreadystatechange=function()
			{
				document.getElementById("move").innerHTML=xmlhttp.responseText;
			}; 
			
			
			var params="fen="+game.fen()+"?";
			xmlhttp.open("GET", "http://chessengine.app.sdslabs.tech/ChessEngine/Engine?"+params,true);
			xmlhttp.send();
			}else{
				document.getElementById("move").innerHTML=movearray[n];
			}
		}
		
		
		
		
		
		
	</script>
  </body>
</html>
