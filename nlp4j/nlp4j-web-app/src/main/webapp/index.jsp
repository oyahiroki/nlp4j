<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>NLP4J</title>
<link rel="icon" href="https://nlp4j.org/favicon.ico">
<meta name="description" content="NLP4J">
<meta property="og:title" content="NLP4J" />
<meta property="og:description" content="NLP4J" />
<meta property="og:image" content="https://nlp4j.org/favicon.png" />
<meta property="og:type" content="article" />
<meta name="twitter:card" content="summary">

<!-- bootstrap 5 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous" />

<link href="" rel="stylesheet" crossorigin="anonymous" />

<!-- DataTables -->
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/1.11.4/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/1.11.4/js/dataTables.bootstrap5.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.0.1/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.11.4/css/dataTables.bootstrap5.min.css" />
<!-- /DataTables -->


<style type="text/css">
.table-wrap {
	width: 100%;
	height: 800px;
	overflow-y: auto;
}

div {
	margin-bottom: 8px;
}
</style>

<script>
	$(document).ready(function() {
		console.log("hi");
		$("#btn1").click(function(event) {
			event.preventDefault();
			// console.log($("#txtinput").val());
			// $("#out").html("... Loading ...");
			var data = new Object();
			data.param1 = $("#txtinput").val();
			postText(data, processData);
		});
	});
	function processData(data) {
		console.log(data);
		$("#out").html("param1=" + data.param1 + ",date=" + data.date);
	}
	function postText(d, callbackOnSuccess, metadata) {
		$.ajax({
			type : "POST",
			contentType : 'application/x-www-form-urlencoded',
			url : "./helloservlet3",
			data : d,
			dataType : "json",
			processData : true,
			async : true,
			success : function(x) {
				if (callbackOnSuccess) {
					callbackOnSuccess(x);
				}
			} // ,
		}).done(function(data) {
			console.log("done");
		}).fail(function(data) {
			console.log("failed");
		}).always(function(data) {
			console.log("response");
		});
	}
</script>

</head>


<body>

	<nav class="navbar navbar-dark bg-primary mb-2">
		<ul id="main_navbar"
			class="navbar-nav flex-row flex-wrap bd-navbar-nav pt-2 py-md-0">
			<li class="nav-item col-6 col-md-auto"><a id="link_home"
				class="nav-link p-2" href="." onclick="">NLP4J Console</a></li>
		</ul>
	</nav>

	<div class="container mt-5">
		<h1>NLP4J 日本語</h1>
		<h2>サブタイトル</h2>
		<p>テキスト本文</p>
	</div>

	<div class="col-12" style="margin-left: 5%; margin-right: 5%;">
		<div class="card-columns">
			<div class="card" style="max-width: 25rem;">
				<!-- img class="card-img-top" src="" alt="" -->
				<h4 class="card-header">AAA</h4>
				<div class="card-body">
					<p class="card-text">
						BBB<br> <a href="https://www.ibm.com" target="_blank">IBM</a><span
							class="ui-icon ui-icon-extlink"></span>
					</p>
					<a class="btn btn-primary" href="https://www.ibm.com"
						class="button btn-privary">Open</a>
				</div>
			</div>
		</div>
		<div class="mb-3">
			<input type="text" id="txt1" placeholder="text" value="test"></input>
		</div>
		<button type="button" class="btn btn-primary" id="btn1">クリック</button>
	</div>


</body>

</html>
