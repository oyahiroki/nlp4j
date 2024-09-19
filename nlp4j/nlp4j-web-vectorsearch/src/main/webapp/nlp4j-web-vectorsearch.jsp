<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ja">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<link rel="icon" href="https://nlp4j.org/favicon.ico">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap 4.5 CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
	integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2"
	crossorigin="anonymous">

<!-- DataTables CSS -->
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.23/css/jquery.dataTables.min.css"/>
<!-- DataTables Bootstrap 4 CSS -->
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.23/css/dataTables.bootstrap4.min.css"/>
<!-- DataTables responsive -->
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/responsive/2.2.7/css/responsive.bootstrap4.min.css">
	
<!-- Optional JavaScript; choose one of the two! -->

<!-- Option 1: jQuery and Bootstrap Bundle (includes Popper) -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
	integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
	crossorigin="anonymous"></script>

<!-- Option 2: jQuery, Popper.js, and Bootstrap JS
   <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
   <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
   <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js" integrity="sha384-w1Q4orYjBQndcko6MimVbzY0tgp4pWB4lZ7lr30WKz0vr/aWKhXdBNmNb5D92v7s" crossorigin="anonymous"></script>
-->

<!-- DataTables JS -->
<script src="https://cdn.datatables.net/1.10.23/js/jquery.dataTables.min.js" ></script>
<!-- DataTables responsive JS -->
<script src="https://cdn.datatables.net/responsive/2.2.7/js/dataTables.responsive.min.js" ></script>
<!-- DataTables Bootstrap 4 JS -->
<script src="https://cdn.datatables.net/1.10.23/js/dataTables.bootstrap4.min.js" ></script>

<!-- Chart.js Released under the MIT license -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js" integrity="sha256-R4pqcOYV8lt7snxMQO/HSbVCFRPMdrhAFMH+vr9giYI=" crossorigin="anonymous"></script>

<!-- jQuery -->
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.3.1.min.js"></script>

<script type="text/javascript" class="init">

// テキストを送信してベクトル化・ベクトルDB(Solr)に格納する(POSTリクエスト)
function posttext(){
	printMessage(""+ getCurrentDateTime2() + " : Processing ...");
	const q = $("#q").val();
	console.log(q);
	const requestObj = {text:q};
	console.log(requestObj);
	var callbackOnSuccess = function(data, requestObj,metadata){
		console.log(data);
	};
	var metadata = {};
	$.ajax({
		type : "POST",
		url : "./ragpost.wss",
		contentType : 'application/json;charset=utf-8',
		dataType : 'json',
		data : JSON.stringify(requestObj),
		async : true,
		success : function(data) {
			if (callbackOnSuccess) {callbackOnSuccess(data, requestObj,metadata);}
		} // ,
	})
	.done(function(data) {})
	.fail(
			function(data) {
//				console.log("failed");
//				console.log(data);
//				if (data == null) {
//					console.log(data.responseJSON);
//					alert("" + data.responseJSON.status_code + ": "
//							+ data.responseJSON.error.message)
//				} else {
//					// alert("failed");
//					console.log("failed");
//				}
			})
	.always(function(data) {
				console.log("response");
				console.log(data);
				printMessage(""+ getCurrentDateTime2() + " : Processing ... done");
	})
	;
}

// キーワードサーチ
function keywordsearch(){
	$("#nlp_result_tbody").empty();
	printMessage(""+ getCurrentDateTime2() + " : Searching ... ");
	const q = $("#q").val();
	const requestObj = {text:q,select:"score,id,text_txt_ja"};
	let callbackOnSuccess = function(data, requestObj,metadata){
		$("#nlp_result_tbody").empty();
		console.log(data);
		if("solr"===data.type){
			if(data.response.value.length == 0){
				$("#nlp_result").append("<tr>" + "<td></td><td>not found</td>" + "</tr>");
			}
			data.response.value.forEach(function(doc){
				$("#nlp_result").append("<tr>" + "<td>" + (doc.score) + "</td><td>" + doc.text_txt_ja + "</td>" + "</tr>");
			});
		}
		return;
	};
	var metadata = {};
	$.ajax({
		type : "POST",
		url : "./keywordsearch.wss",
		contentType : 'application/json;charset=utf-8',
		dataType : 'json',
		data : JSON.stringify(requestObj),
		async : true,
		success : function(data) {
			if (callbackOnSuccess) {
				callbackOnSuccess(data, requestObj,metadata);
			}
			printMessage(""+ getCurrentDateTime2() + " : Searching ... done");
		} // ,
	}).done(function(data) {
	}).fail(
			function(data) {
				console.log("failed");
				console.log(data);
				if (data == null) {
					console.log(data.responseJSON);
					alert("" + data.responseJSON.status_code + ": "
							+ data.responseJSON.error.message)
				} else {
					// alert("failed");
					console.log("failed");
				}
			}).always(function(data) {
				console.log("response");
				console.log(data);
	});
}


// テキストを送信してベクトル検索する(POSTリクエスト)
function vectorsearch(){
	$("#nlp_result_tbody").empty();
	printMessage(""+ getCurrentDateTime2() + " : Searching ... ");
	const q = $("#q").val();
	const requestObj = {text:q};
	var callbackOnSuccess = function(data, requestObj,metadata){
		$("#nlp_result_tbody").empty();
		if(data.response.docs.length==0){
			$("#nlp_result").append("<tr>" + "<td></td><td>not found</td>" + "</tr>");
		}
		data.response.docs.forEach(function(doc){
			$("#nlp_result").append("<tr>" + "<td>" + (doc.score) + "</td><td>" + doc.text_txt_ja + "</td>" + "</tr>");
		});
	};
	var metadata = {};
	$.ajax({
		type : "POST",
		url : "./vectorsearch.wss",
		contentType : 'application/json;charset=utf-8',
		dataType : 'json',
		data : JSON.stringify(requestObj),
		async : true,
		success : function(data) {
			if (callbackOnSuccess) {
				callbackOnSuccess(data, requestObj,metadata);
			}
			printMessage(""+ getCurrentDateTime2() + " : Searching ... done");
		} // ,
	}).done(function(data) {
	}).fail(
			function(data) {
				console.log("failed");
				console.log(data);
				if (data == null) {
					console.log(data.responseJSON);
					alert("" + data.responseJSON.status_code + ": "
							+ data.responseJSON.error.message)
				} else {
					// alert("failed");
					console.log("failed");
				}
			}).always(function(data) {
				console.log("response");
				console.log(data);
	});
}

function getCurrentDateTime() {
	let now = new Date();
	let year = now.getFullYear();
	let month = now.getMonth() + 1; // 月は0から始まるので1を加える
	let day = now.getDate();
	let hour = now.getHours();
	let minute = now.getMinutes();
	let second = now.getSeconds();
	// ゼロ埋めする関数
	const zeroPad = (num, places) => String(num).padStart(places, '0');
	return "" + zeroPad(year, 4) + zeroPad(month, 2) + zeroPad(day, 2) + zeroPad(hour, 2) + zeroPad(minute, 2) + zeroPad(second, 2);
}
function getCurrentDateTime2() {
	let now = new Date();
	let year = now.getFullYear();
	let month = now.getMonth() + 1; // 月は0から始まるので1を加える
	let day = now.getDate();
	let hour = now.getHours();
	let minute = now.getMinutes();
	let second = now.getSeconds();
	// ゼロ埋めする関数
	const zeroPad = (num, places) => String(num).padStart(places, '0');
	return "" + zeroPad(year, 4) + "-" + zeroPad(month, 2) + "-" + zeroPad(day, 2) + " " + zeroPad(hour, 2) + ":" + zeroPad(minute, 2) + ":" + zeroPad(second, 2);
}

function printMessage(s){
	$("#system_message").empty();
	$("#system_message").text(s);
}

$(document).ready(function() {
	{
		$("#btn_posttext").click(posttext); // POST
		$("#btn_keywordsearch").click(keywordsearch); // Keyword Search
		$("#btn_vectorsearch").click(vectorsearch); // Vector Search
	}
	{
		$("#q").keypress(function(e){
		    if(e.which == 13) {
		    	// df();
		        // return false;
		    }
		});
	}
	printMessage(""+ getCurrentDateTime2() + " : Loaded.");
	
    // テキストエリアの自動リサイズ
    $('#q').on('input', function () {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });
} );

</script>

<title>NLP4J-VectorSearch</title>

</head>
<body>

	<nav class="navbar navbar-dark bg-primary mb-2">
		<ul class="navbar-nav flex-row flex-wrap bd-navbar-nav pt-2 py-md-0">
			<li class="nav-item col-6 col-md-auto">
				<a class="nav-link p-2" href="https://nlp4j.org/" onclick="" target="_blank">NLP4J</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link active" id="item1-tab" data-toggle="tab" role="tab" aria-controls="item1" aria-selected="true">NLP4J-VectorSearch</a>
			</li>
		</ul>
	</nav>
	
			<!-- input type="text" class="form-control" id="q2" placeholder="Input text in Japanese Language" value="今日はいい天気です。"  -->
			
	<div class="input-group justify-content-center mb-5">
		<div class="col-sm-6">
			<textarea class="form-control" id="q" placeholder="Input text in Japanese Language" rows="1" style="resize: none; overflow:hidden;">今日はいい天気です。</textarea>
		</div>
		<div class="col-sm-auto">
		<span class="input-group-btn">
			<button type="button" class="btn btn-primary" id="btn_posttext">POST</button>
			<button type="button" class="btn btn-primary" id="btn_keywordsearch">Keyword Search</button>
			<button type="button" class="btn btn-primary" id="btn_vectorsearch">Vector Search</button>
		</span>
		</div>
	</div>
	
	
<div class="container">
  <div class="row">
    <div class="col"></div> <!-- 左側のスペース -->
    <div class="col-10">
    <span id="system_message"></span>
    </div>
    <div class="col"></div> <!-- 右側のスペース -->
  </div>
  
  
  <div class="row">
    <div class="col"></div> <!-- 左側のスペース -->
    <div class="col-10">
		<div class="card">
		    <div class="card-header">文書検索結果</div>
		    <div class="card-body">
				<div style="height: 480px; overflow-y: scroll;">
				<table class="table table-striped border" id="nlp_result">
					<thead>
					  <tr><th>Score</th><th>Document</th></tr>
					</thead>
					<tbody id="nlp_result_tbody">
					</tbody>
				  <!-- tr><td>Yamada</td><td>16</td></tr -->
				<!-- tr><td>Suzuki</td><td>26</td></tr -->
				<!-- tr><td>Tanaka</td><td>36</td></tr -->
					</table>
				</div>
		    </div>
		</div>
    </div>
    <div class="col"></div> <!-- 右側のスペース -->
  </div>
  
</div>

</body>
</html>