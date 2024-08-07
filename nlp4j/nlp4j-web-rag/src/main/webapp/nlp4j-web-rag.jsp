<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="ja">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
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

function nlp(){
	
	const q = $("#q").val();
	console.log(q);
	const requestObj = {text:q};
	console.log(requestObj);
	
	var callbackOnSuccess = function(data, requestObj,metadata){
		console.log(data);
//		$("#nlp_result_tbody").empty();
//		// $('#nlp_result > tbody:last').append("<tr><th>開始位置</th><th>終了位置</th><th>品詞</th><th>正規形</th><th>表出文字</th></tr>");
//		data.keywords.forEach(function(kw){
//			$("#nlp_result").append("<tr>" + "<td>" + (kw.upos != null ? kw.upos : "---") + "</td><td>" + kw.facet + "</td><td>" + kw.lex + "</td><td>" + kw.str + "</td></tr>");
//		});
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
			if (callbackOnSuccess) {
				callbackOnSuccess(data, requestObj,metadata);
			}
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
	})
	;
}

function ragquery(){
	$("#nlp_result_tbody").empty();
	const q = $("#q").val();
	console.log(q);
	const requestObj = {text:q};
	console.log(requestObj);
	
	console.log("OK");
	var callbackOnSuccess = function(data, requestObj,metadata){
		console.log(data);
		$("#nlp_result_tbody").empty();
		// $('#nlp_result > tbody:last').append("<tr><th>開始位置</th><th>終了位置</th><th>品詞</th><th>正規形</th><th>表出文字</th></tr>");
//    	data.keywords.forEach(function(kw){
//			$("#nlp_result").append("<tr>" + "<td>" + (kw.upos != null ? kw.upos : "---") + "</td><td>" + kw.facet + "</td><td>" + kw.lex + "</td><td>" + kw.str + "</td></tr>");
//		});
		data.response.docs.forEach(function(doc){
			console.log(doc);
			// $("#nlp_result").append("<tr>" + "<td>" + (kw.upos != null ? kw.upos : "---") + "</td><td>" + kw.facet + "</td><td>" + kw.lex + "</td><td>" + kw.str + "</td></tr>");
			$("#nlp_result").append("<tr>" + "<td>" + (doc.score) + "</td><td>" + doc.text_txt_ja + "</td>" + "</tr>");
		});
	};
	var metadata = {};
	
	$.ajax({
		type : "POST",
		url : "./ragquery.wss",
		contentType : 'application/json;charset=utf-8',
		dataType : 'json',
		data : JSON.stringify(requestObj),
		async : true,
		success : function(data) {
			if (callbackOnSuccess) {
				callbackOnSuccess(data, requestObj,metadata);
			}
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

$(document).ready(function() {
	{
		var df = nlp;
		$("#btn_nlp").click(df);
	}
	{
		var rq = ragquery;
		$("#btn_ragquery").click(rq);
	}
	{
		$("#q").keypress(function(e){
		    if(e.which == 13) {
		    	// df();
		        // return false;
		    }
		});
	}
	
    // テキストエリアの自動リサイズ
    $('#q').on('input', function () {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });
	
	
	
	
} );

</script>

<title>NLP4J-RAG</title>

</head>
<body>

	<nav class="navbar navbar-dark bg-primary mb-2">
		<ul class="navbar-nav flex-row flex-wrap bd-navbar-nav pt-2 py-md-0">
			<li class="nav-item col-6 col-md-auto">
				<a class="nav-link p-2" href="https://nlp4j.org/" onclick="" target="_blank">NLP4J</a>
			</li>
			<li class="nav-item">
			  <a class="nav-link active" id="item1-tab" data-toggle="tab" role="tab" aria-controls="item1" aria-selected="true">NLP4J-RAG</a>
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
			<button type="button" class="btn btn-primary" id="btn_nlp">POST</button>
			<button type="button" class="btn btn-primary" id="btn_ragquery">Query</button>
		</span>
		</div>
	</div>
	
	
<div class="container">
  <div class="row">
    <div class="col"></div> <!-- 左側のスペース -->
    <div class="col-10">
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
    <div class="col"></div> <!-- 右側のスペース -->
  </div>
</div>




</body>
</html>