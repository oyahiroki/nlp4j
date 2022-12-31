<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="ja">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!--  meta http-equiv="Content-Type" content="text/html; charset=UTF-8" -->
<meta charset="UTF-8">
<title>【非公式】 コメダ珈琲店 年末年始営業時間 2023</title>
<meta name="description" content="${DESCRIPTION}">
<meta property="og:title" content="${TITLE}"/>
<meta property="og:description" content="${DESCRIPTION}"/>
<meta property="og:image" content="https://nlp4j.org/favicon.png"/>
<meta property="og:type" content="article"/>
<meta name="twitter:card" content="summary">

<!-- bootstrap 5 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous" />

<link href="" rel="stylesheet" crossorigin="anonymous" />

<!-- DataTables -->
<script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.11.4/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.11.4/js/dataTables.bootstrap5.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.0.1/css/bootstrap.min.css" />
<link rel="stylesheet" href="https://cdn.datatables.net/1.11.4/css/dataTables.bootstrap5.min.css" />
<!-- /DataTables -->


<!-- コメダデータ -->
<script type="text/javascript" charset="UTF-8" src="komeda2023.js"></script>


<style type="text/css" >
.table-wrap {
  width: 100%;
  height: 800px;
  overflow-y: auto;
}
div{
  margin-bottom:8px;
}
</style>

<link rel="icon" href="https://nlp4j.org/wp-content/uploads/2021/09/nlp4j-favicon64.png" sizes="32x32" />
<link rel="icon" href="https://nlp4j.org/wp-content/uploads/2021/09/nlp4j-favicon64.png" sizes="192x192" />
<link rel="apple-touch-icon" href="https://nlp4j.org/wp-content/uploads/2021/09/nlp4j-favicon64.png" />
<meta name="msapplication-TileImage" content="https://nlp4j.org/wp-content/uploads/2021/09/nlp4j-favicon64.png" />

<script>

data = [
    {
        "name":       "Tiger Nixon",
        "position":   "System Architect",
        "salary":     "$3,120",
        "start_date": "2011/04/25",
        "office":     "Edinburgh",
        "extn":       "5421"
    },
    {
        "name":       "Garrett Winters",
        "position":   "Director",
        "salary":     "$5,300",
        "start_date": "2011/07/25",
        "office":     "Edinburgh",
        "extn":       "8422"
    }
];

data = new Array();

var filter_todofuken = new Array();

var todofuken = "";

data_komeda.forEach(function(d,idx){
	// console.log(d);
	{
		var tdfk = d["都道府県"];
		if(tdfk.length>0){
			todofuken = tdfk;
		}
		if(filter_todofuken.includes(tdfk) == false){
			filter_todofuken.push(tdfk);
		}
	}
	var o = new Object();
	o.idx = (idx+1);
	o.todofuken = d["都道府県"];
	{
		if(o.todofuken == null || o.todofuken.length == 0){
			o.todofuken = todofuken;
		}
	}
	o.shichoson = d["市"];
	o.tenmei = d["店舗名"];
	o.tel = d["TEL"];
	o.eigyojikan = ""
		+ "12/28 "+ d["1228_1"] + " - " + d["1228_2"] + "<br/>"
		+ "12/29 "+ d["1229_1"] + " - " + d["1229_2"] + "<br/>"
		+ "12/30 "+ d["1230_1"] + " - " + d["1230_2"] + "<br/>"
		+ "12/31 "+ d["1231_1"] + " - " + d["1231_2"] + "<br/>"
		+ "1/1 "+ d["0101_1"] + " - " + d["0101_2"] + "<br/>"
		+ "1/2 "+ d["0102_1"] + " - " + d["0102_2"] + "<br/>"
		+ "1/3 "+ d["0103_1"] + " - " + d["0103_2"] + "<br/>"
		+ "1/4 "+ d["0104_1"] + " - " + d["0104_2"] + "<br/>"
		;
	// 	o.eigyojikan = "";
	o.data1 =
		""
	+ o.tenmei + "<br/>" 
	+ "【電話番号】<br/>"
	+ "<a href='tel:" + o.tel + "'>☎ " + o.tel + "</a>" + "<br/>"
	+ "【所在地】<br/>"
	+ o.todofuken + " "
	+ "<a target='_blank' href='https://www.google.com/maps/place/" + encodeURI( o.shichoson ) + "'>" + o.shichoson + "</a><br/>" 
	+ "【営業時間】<br/>"
	+ o.eigyojikan;
	data.push(o);
});

$(document).ready(function() {
	
	filter_todofuken.forEach(function(d,idx){
		$("#filter1").append($("<option>").html(""+d).val(""+d));
	});
	
	$("#filter1").change(function(){
		// 選択された値
		var v = $(this).val();
		// リセット
		if(v == "-"){
			$("#filter2").empty();
			$("#filter2").append($("<option>").html("市区町村を指定しない").val("-"));
			$('#tbl_data').dataTable().fnClearTable();
			$('#tbl_data').dataTable().fnAddData(data);
			return;
		}
		// リセット以外
		// フィルターされた店舗
		var komeda2 = new Array();
		// フィルターされた市区町村
		var filter2 = new Array();
		data.forEach(function(d,idx){
			if(d["todofuken"] == v){
				// console.log(d["shichoson"]);
				d.idx = (idx+1);
				komeda2.push(d);
				if(filter2.includes(d["shichoson"])==false){
					filter2.push(d["shichoson"]);
				}
			}
		});
		console.log(filter2);
		if(filter2.length>0){
			$("#filter2").empty();
			$("#filter2").append($("<option>").html("市区町村を指定しない").val("-"));
			filter2.forEach(function(d,idx){
				$("#filter2").append($("<option>").html(""+d).val(""+d));
			});
		}
		console.log(komeda2);
		$('#tbl_data').dataTable().fnClearTable();
		$('#tbl_data').dataTable().fnAddData(komeda2);
	});
	
	$("#filter2").change(function(){
		var todofuken = $("#filter1").val();
		// 選択された値
		var v = $(this).val();
		// リセット
		if(v == "-"){
			// フィルターされた店舗
			var komeda2 = new Array();
			// フィルターされた市区町村
			data.forEach(function(d,idx){
				if(d["todofuken"] == todofuken){
					// console.log(d["shichoson"]);
					d.idx = (idx+1);
					komeda2.push(d);
				}
			});
			console.log(komeda2);
			$('#tbl_data').dataTable().fnClearTable();
			$('#tbl_data').dataTable().fnAddData(komeda2);
			return;
		}
		// リセット以外
		// フィルターされた店舗
		var komeda2 = new Array();
		data.forEach(function(d,idx){
			if(d["shichoson"] == v){
				// console.log(d["shichoson"]);
				d.idx = (idx+1);
				komeda2.push(d);
			}
		});
		console.log(komeda2);
		$('#tbl_data').dataTable().fnClearTable();
		$('#tbl_data').dataTable().fnAddData(komeda2);
	});

	table = $('#tbl_data').DataTable( {
		dom: "<iptp>",
	    columns: [
	        { data: 'idx', title: '#' },
//	        { data: 'todofuken', title: '都道府県' },
//	        { data: 'shichoson', title: '市町村' },
	        { data: 'data1', title: '店情報' },
//	        { data: 'eigyojikan', title: '営業時間' },
	    ],
	    data: data,
	    initComplete: function(){
	    	this.api()
	    	.columns()
	    	.every(function(){
	    		var column = this;
	    		var select = $("<select><option value=''></option></select>")
	    		.appendTo($(column.footer()).empty())
	    		.on('change',function(){
	    			var val = $.fn.dataTable.util.escapeRegex($(this).val());
	    			column.search(val ? "^" + val + "$" : "", true, false).draw();
	    		});
	    		console.log(select);
	    		column.data().unique().sort()
	    		.each(function(d,j){
	    			// console.log(j);
	    			select.append('<option value="' + d + '">' + d + '</option>');
	    		});
	    	});
	    },
	} );	
	
	// $('#example').DataTable();
} );
</script>

</head>
<body>

<h1>コメダ珈琲店 年末年始営業時間 2023</h1>

<select id="filter1">
	<option value="-">都道府県を指定しない</option>
</select>

<select id="filter2">
	<option value="-">市区町村を指定しない</option>
</select>

<table id="tbl_data" class="table table-striped" style="width:100%">
<thead></thead>
<tbody></tbody>
<tfoot></tfoot>
</table>

<!-- table id="example" class="table table-striped" style="width:100%">
        <thead>
            <tr>
                <th>Name</th>
                <th>Position</th>
                <th>Office</th>
                <th>Age</th>
                <th>Start date</th>
                <th>Salary</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Tiger Nixon</td>
                <td>System Architect</td>
                <td>Edinburgh</td>
                <td>61</td>
                <td>2011/04/25</td>
                <td>$320,800</td>
            </tr>
            <tr>
                <td>Garrett Winters</td>
                <td>Accountant</td>
                <td>Tokyo</td>
                <td>63</td>
                <td>2011/07/25</td>
                <td>$170,750</td>
            </tr>
            <tr>
                <td>Ashton Cox</td>
                <td>Junior Technical Author</td>
                <td>San Francisco</td>
                <td>66</td>
                <td>2009/01/12</td>
                <td>$86,000</td>
            </tr>
            <tr>
                <td>Cedric Kelly</td>
                <td>Senior Javascript Developer</td>
                <td>Edinburgh</td>
                <td>22</td>
                <td>2012/03/29</td>
                <td>$433,060</td>
            </tr>
            <tr>
                <td>Airi Satou</td>
                <td>Accountant</td>
                <td>Tokyo</td>
                <td>33</td>
                <td>2008/11/28</td>
                <td>$162,700</td>
            </tr>
            <tr>
                <td>Brielle Williamson</td>
                <td>Integration Specialist</td>
                <td>New York</td>
                <td>61</td>
                <td>2012/12/02</td>
                <td>$372,000</td>
            </tr>
            <tr>
                <td>Herrod Chandler</td>
                <td>Sales Assistant</td>
                <td>San Francisco</td>
                <td>59</td>
                <td>2012/08/06</td>
                <td>$137,500</td>
            </tr>
            <tr>
                <td>Rhona Davidson</td>
                <td>Integration Specialist</td>
                <td>Tokyo</td>
                <td>55</td>
                <td>2010/10/14</td>
                <td>$327,900</td>
            </tr>
            <tr>
                <td>Colleen Hurst</td>
                <td>Javascript Developer</td>
                <td>San Francisco</td>
                <td>39</td>
                <td>2009/09/15</td>
                <td>$205,500</td>
            </tr>
            <tr>
                <td>Sonya Frost</td>
                <td>Software Engineer</td>
                <td>Edinburgh</td>
                <td>23</td>
                <td>2008/12/13</td>
                <td>$103,600</td>
            </tr>
            <tr>
                <td>Jena Gaines</td>
                <td>Office Manager</td>
                <td>London</td>
                <td>30</td>
                <td>2008/12/19</td>
                <td>$90,560</td>
            </tr>
            <tr>
                <td>Quinn Flynn</td>
                <td>Support Lead</td>
                <td>Edinburgh</td>
                <td>22</td>
                <td>2013/03/03</td>
                <td>$342,000</td>
            </tr>
            <tr>
                <td>Charde Marshall</td>
                <td>Regional Director</td>
                <td>San Francisco</td>
                <td>36</td>
                <td>2008/10/16</td>
                <td>$470,600</td>
            </tr>
            <tr>
                <td>Haley Kennedy</td>
                <td>Senior Marketing Designer</td>
                <td>London</td>
                <td>43</td>
                <td>2012/12/18</td>
                <td>$313,500</td>
            </tr>
            <tr>
                <td>Tatyana Fitzpatrick</td>
                <td>Regional Director</td>
                <td>London</td>
                <td>19</td>
                <td>2010/03/17</td>
                <td>$385,750</td>
            </tr>
            <tr>
                <td>Michael Silva</td>
                <td>Marketing Designer</td>
                <td>London</td>
                <td>66</td>
                <td>2012/11/27</td>
                <td>$198,500</td>
            </tr>
            <tr>
                <td>Paul Byrd</td>
                <td>Chief Financial Officer (CFO)</td>
                <td>New York</td>
                <td>64</td>
                <td>2010/06/09</td>
                <td>$725,000</td>
            </tr>
            <tr>
                <td>Gloria Little</td>
                <td>Systems Administrator</td>
                <td>New York</td>
                <td>59</td>
                <td>2009/04/10</td>
                <td>$237,500</td>
            </tr>
            <tr>
                <td>Bradley Greer</td>
                <td>Software Engineer</td>
                <td>London</td>
                <td>41</td>
                <td>2012/10/13</td>
                <td>$132,000</td>
            </tr>
            <tr>
                <td>Dai Rios</td>
                <td>Personnel Lead</td>
                <td>Edinburgh</td>
                <td>35</td>
                <td>2012/09/26</td>
                <td>$217,500</td>
            </tr>
            <tr>
                <td>Jenette Caldwell</td>
                <td>Development Lead</td>
                <td>New York</td>
                <td>30</td>
                <td>2011/09/03</td>
                <td>$345,000</td>
            </tr>
            <tr>
                <td>Yuri Berry</td>
                <td>Chief Marketing Officer (CMO)</td>
                <td>New York</td>
                <td>40</td>
                <td>2009/06/25</td>
                <td>$675,000</td>
            </tr>
            <tr>
                <td>Caesar Vance</td>
                <td>Pre-Sales Support</td>
                <td>New York</td>
                <td>21</td>
                <td>2011/12/12</td>
                <td>$106,450</td>
            </tr>
            <tr>
                <td>Doris Wilder</td>
                <td>Sales Assistant</td>
                <td>Sydney</td>
                <td>23</td>
                <td>2010/09/20</td>
                <td>$85,600</td>
            </tr>
            <tr>
                <td>Angelica Ramos</td>
                <td>Chief Executive Officer (CEO)</td>
                <td>London</td>
                <td>47</td>
                <td>2009/10/09</td>
                <td>$1,200,000</td>
            </tr>
            <tr>
                <td>Gavin Joyce</td>
                <td>Developer</td>
                <td>Edinburgh</td>
                <td>42</td>
                <td>2010/12/22</td>
                <td>$92,575</td>
            </tr>
            <tr>
                <td>Jennifer Chang</td>
                <td>Regional Director</td>
                <td>Singapore</td>
                <td>28</td>
                <td>2010/11/14</td>
                <td>$357,650</td>
            </tr>
            <tr>
                <td>Brenden Wagner</td>
                <td>Software Engineer</td>
                <td>San Francisco</td>
                <td>28</td>
                <td>2011/06/07</td>
                <td>$206,850</td>
            </tr>
            <tr>
                <td>Fiona Green</td>
                <td>Chief Operating Officer (COO)</td>
                <td>San Francisco</td>
                <td>48</td>
                <td>2010/03/11</td>
                <td>$850,000</td>
            </tr>
            <tr>
                <td>Shou Itou</td>
                <td>Regional Marketing</td>
                <td>Tokyo</td>
                <td>20</td>
                <td>2011/08/14</td>
                <td>$163,000</td>
            </tr>
            <tr>
                <td>Michelle House</td>
                <td>Integration Specialist</td>
                <td>Sydney</td>
                <td>37</td>
                <td>2011/06/02</td>
                <td>$95,400</td>
            </tr>
            <tr>
                <td>Suki Burks</td>
                <td>Developer</td>
                <td>London</td>
                <td>53</td>
                <td>2009/10/22</td>
                <td>$114,500</td>
            </tr>
            <tr>
                <td>Prescott Bartlett</td>
                <td>Technical Author</td>
                <td>London</td>
                <td>27</td>
                <td>2011/05/07</td>
                <td>$145,000</td>
            </tr>
            <tr>
                <td>Gavin Cortez</td>
                <td>Team Leader</td>
                <td>San Francisco</td>
                <td>22</td>
                <td>2008/10/26</td>
                <td>$235,500</td>
            </tr>
            <tr>
                <td>Martena Mccray</td>
                <td>Post-Sales support</td>
                <td>Edinburgh</td>
                <td>46</td>
                <td>2011/03/09</td>
                <td>$324,050</td>
            </tr>
            <tr>
                <td>Unity Butler</td>
                <td>Marketing Designer</td>
                <td>San Francisco</td>
                <td>47</td>
                <td>2009/12/09</td>
                <td>$85,675</td>
            </tr>
            <tr>
                <td>Howard Hatfield</td>
                <td>Office Manager</td>
                <td>San Francisco</td>
                <td>51</td>
                <td>2008/12/16</td>
                <td>$164,500</td>
            </tr>
            <tr>
                <td>Hope Fuentes</td>
                <td>Secretary</td>
                <td>San Francisco</td>
                <td>41</td>
                <td>2010/02/12</td>
                <td>$109,850</td>
            </tr>
            <tr>
                <td>Vivian Harrell</td>
                <td>Financial Controller</td>
                <td>San Francisco</td>
                <td>62</td>
                <td>2009/02/14</td>
                <td>$452,500</td>
            </tr>
            <tr>
                <td>Timothy Mooney</td>
                <td>Office Manager</td>
                <td>London</td>
                <td>37</td>
                <td>2008/12/11</td>
                <td>$136,200</td>
            </tr>
            <tr>
                <td>Jackson Bradshaw</td>
                <td>Director</td>
                <td>New York</td>
                <td>65</td>
                <td>2008/09/26</td>
                <td>$645,750</td>
            </tr>
            <tr>
                <td>Olivia Liang</td>
                <td>Support Engineer</td>
                <td>Singapore</td>
                <td>64</td>
                <td>2011/02/03</td>
                <td>$234,500</td>
            </tr>
            <tr>
                <td>Bruno Nash</td>
                <td>Software Engineer</td>
                <td>London</td>
                <td>38</td>
                <td>2011/05/03</td>
                <td>$163,500</td>
            </tr>
            <tr>
                <td>Sakura Yamamoto</td>
                <td>Support Engineer</td>
                <td>Tokyo</td>
                <td>37</td>
                <td>2009/08/19</td>
                <td>$139,575</td>
            </tr>
            <tr>
                <td>Thor Walton</td>
                <td>Developer</td>
                <td>New York</td>
                <td>61</td>
                <td>2013/08/11</td>
                <td>$98,540</td>
            </tr>
            <tr>
                <td>Finn Camacho</td>
                <td>Support Engineer</td>
                <td>San Francisco</td>
                <td>47</td>
                <td>2009/07/07</td>
                <td>$87,500</td>
            </tr>
            <tr>
                <td>Serge Baldwin</td>
                <td>Data Coordinator</td>
                <td>Singapore</td>
                <td>64</td>
                <td>2012/04/09</td>
                <td>$138,575</td>
            </tr>
            <tr>
                <td>Zenaida Frank</td>
                <td>Software Engineer</td>
                <td>New York</td>
                <td>63</td>
                <td>2010/01/04</td>
                <td>$125,250</td>
            </tr>
            <tr>
                <td>Zorita Serrano</td>
                <td>Software Engineer</td>
                <td>San Francisco</td>
                <td>56</td>
                <td>2012/06/01</td>
                <td>$115,000</td>
            </tr>
            <tr>
                <td>Jennifer Acosta</td>
                <td>Junior Javascript Developer</td>
                <td>Edinburgh</td>
                <td>43</td>
                <td>2013/02/01</td>
                <td>$75,650</td>
            </tr>
            <tr>
                <td>Cara Stevens</td>
                <td>Sales Assistant</td>
                <td>New York</td>
                <td>46</td>
                <td>2011/12/06</td>
                <td>$145,600</td>
            </tr>
            <tr>
                <td>Hermione Butler</td>
                <td>Regional Director</td>
                <td>London</td>
                <td>47</td>
                <td>2011/03/21</td>
                <td>$356,250</td>
            </tr>
            <tr>
                <td>Lael Greer</td>
                <td>Systems Administrator</td>
                <td>London</td>
                <td>21</td>
                <td>2009/02/27</td>
                <td>$103,500</td>
            </tr>
            <tr>
                <td>Jonas Alexander</td>
                <td>Developer</td>
                <td>San Francisco</td>
                <td>30</td>
                <td>2010/07/14</td>
                <td>$86,500</td>
            </tr>
            <tr>
                <td>Shad Decker</td>
                <td>Regional Director</td>
                <td>Edinburgh</td>
                <td>51</td>
                <td>2008/11/13</td>
                <td>$183,000</td>
            </tr>
            <tr>
                <td>Michael Bruce</td>
                <td>Javascript Developer</td>
                <td>Singapore</td>
                <td>29</td>
                <td>2011/06/27</td>
                <td>$183,000</td>
            </tr>
            <tr>
                <td>Donna Snider</td>
                <td>Customer Support</td>
                <td>New York</td>
                <td>27</td>
                <td>2011/01/25</td>
                <td>$112,000</td>
            </tr>
        </tbody>
        <tfoot>
            <tr>
                <th>Name</th>
                <th>Position</th>
                <th>Office</th>
                <th>Age</th>
                <th>Start date</th>
                <th>Salary</th>
            </tr>
        </tfoot>
    </table -->


</body>
</html>