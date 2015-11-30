

/**
 * appelé lors du click sur rpn
 */
$('#rpnSubmit').click(function (e) {
	
	var rpn = $('#rpn').val();
	
		$.ajax({
			type : 'get',
			url :  urlRoot+'/rpn/compute?input='+rpn.replace(/\+/g, 'u'),
			contentType : 'application/json',
			async : false,
			success : function(data) {
				console.log("Calling rpn ok");
				$('#rpn').val(data.output);
			},
			error : function(data) {
				console.log("Error while rpn");		
			}
		});
});


/**
 * appelé lors du click sur fizzbuzz
 */
$('#fizzBuzzSubmit').click(function (e) {
	
	var fizzBuzz = $('#fizzBuzz').val();
	
	if(!isNaN(fizzBuzz)){
		$.ajax({
			type : 'get',
			url :  urlRoot+'/fizzBuzz/fizzBuzz?input='+fizzBuzz,
			contentType : 'application/json',
			async : false,
			success : function(data) {
				console.log("Calling fizzBuzz ok");
				$('#fizzBuzz').val(data.output);
			},
			error : function(data) {
				console.log("Error while fizzBuzz");		
			}
		});
	}else{
		console.log("Not managed");
		$('#fizzBuzz').val("Not managed");
	}
});


/**
 * appelé lors du click sur romanConverter
 */
$('#romanConverter').click(function (e) {
	
	var roman = $('#roman').val();
	
	if(!isNaN(roman)&&roman>0&&roman<2000&&roman % 1 == 0){
		$.ajax({
			type : 'get',
			url :  urlRoot+'/romanconverter/decimalToRoman?decimal='+roman,
			contentType : 'application/json',
			async : false,
			success : function(data) {
				console.log("Calling romanToDecimal ok");
				$('#roman').val(data.romanValue);
			},
			error : function(data) {
				console.log("Error while romanToDecimal");		
			}
		});
	}else{
		console.log("Not managed");
		$('#roman').val("Not managed");
	}
});

/**
 * appelé lors du click sur fooBarQix
 */
$('#fooBarQix').click(function (e) {
	$.ajax({
		type : 'get',
		url :  urlRoot+'/foobarqix/returnFooBarQix',
		contentType : 'application/json',
		async : false,
		success : function(data) {
			console.log("Calling returnFooBarQix ok");
			$('#fooBarQixResult').empty()
			$.each(data, function( index, value ) {
				$('#fooBarQixResult').append( index+1 +' : '+ value );
			});
			
		},
		error : function(data) {
			console.log("Error while returnFooBarQix");		
		}
	});
});


/**
 * appelé lors du click sur une checkbox
 */
$('.tricTracCheckbox').click(function (e) {
	// si une ancienne cellule a été sélectionnée on efface sa sélection
	if(tricTracSelectedRow!=null&&tricTracSelectedColumn!=null){
		e.currentTarget.parentElement.parentElement.rows[tricTracSelectedRow].children[tricTracSelectedColumn].firstChild.checked=false;
		// eviter bug affichage lors d un double click
		if(e.currentTarget.parentElement.rowIndex==tricTracSelectedRow&& e.currentTarget.cellIndex==tricTracSelectedColumn){
			e.currentTarget.parentElement.parentElement.rows[tricTracSelectedRow].children[tricTracSelectedColumn].firstChild.checked=true;
		}
	}	
	
	tricTracSelectedRow = e.currentTarget.parentElement.rowIndex;
	tricTracSelectedColumn = e.currentTarget.cellIndex;
	
	console.log("field selected, row : " + tricTracSelectedRow + "; column : " + tricTracSelectedColumn);

});

/**
 * appelé lors du click sur le bouton validation
 */
$("#validate").click(function(){
	if(tricTracSelectedRow!=null&&tricTracSelectedColumn!=null){
		$.ajax({
			type : 'get',
			url :  urlRoot+'/trictrac/validate?player='+tricTracPlayer+'&row='+tricTracSelectedRow+'&column='+tricTracSelectedColumn,
			contentType : 'application/json',
			async : false,
			success : function(data) {
				console.log("sending validate ok");
				tricTracPlayer= !tricTracPlayer; 
				displayTricTrac(data);
			},
			error : function(data) {
				console.log("Error while validate");		
			}
		});
		tricTracSelectedRow=null;
		tricTracSelectedColumn=null;
	}
});

/**
 * appelé lors du click sur le bouton reset
 */
$("#reset").click(function(){
	$('#fooBarQixResult').empty();
	$('#roman').val('');
	$('#fizzBuzz').val('');
	$('#rpn').val('');
	resetTricTrac();
});

/**
 * Methode generique d appel au service de reset
 */
function resetTricTrac(){
	$.ajax({
		type : 'get',
		url :  urlRoot+'/trictrac/reset',
		contentType : 'application/json',
		async : false,
		success : function(data) {
			console.log("sending reset ok");
			displayTricTrac(data);
		},
		error : function(data) {
			console.log("Error while reset");		
		}
	});
	tricTracSelectedRow=null;
	tricTracSelectedColumn=null;
	tricTracPlayer = false;
}

/**
 * methode generique d affichage du trictrac. 
 * @param data
 */
function displayTricTrac(data){
	$('.tricTracCheckbox').each(function() {
		var cell = this;
		var row = cell.parentElement.rowIndex;
		var column = cell.cellIndex;
		console.log("setting field, row : " + row + "; column : " + column + "; value : " + data.tricTracTab[row][column]);
		cell.innerHTML= '';
		if(data.tricTracTab[row][column]!=null){
			if(data.tricTracTab[row][column]==true){
				cell.innerHTML = 'X';
			}else{
				cell.innerHTML = 'O';
			}
		}else{
			var input = document.createElement("input");
			var typeAttribute = document.createAttribute("type");
			typeAttribute.value = "checkbox";
			
			input.setAttributeNode(typeAttribute);
			
			cell.appendChild(input);
		}
		
	});
	
	if(data.gameOver){
		$("#message").empty();
		
		var message = '';
		
		if(data.winner!=null){
			var winner;
			resetTricTrac();
			if(data.winner){
				winner='player 2';
			}else{
				winner='player 1';
			}
			message = 'The winner is ' + winner;
		}else{
			message = 'The game is over';
		}
		$("#message").append(message);
	}
	
}

var urlRoot = "http://127.0.0.1:8080/exosSgcib/rw";
var tricTracSelectedRow = null;
var tricTracSelectedColumn = null;
var tricTracPlayer = false; // false = joueur 1, true = joueur 2