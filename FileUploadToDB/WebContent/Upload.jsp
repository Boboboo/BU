<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>


<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
  <meta name="description" content="MIND-VisANT Web">

  <link rel="icon" href="../../favicon.ico">

  <title>File Upload to Database</title>


  <!-- styles -->
  <!-- Bootstrap core CSS -->
    <!--
    <link href="stylesheets/bootstrap.min.css" rel="stylesheet">
  -->
  <!-- <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"> -->
  <link rel="stylesheet" href="stylesheets/bootstrap3.3.6.min.css">
  <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
  <link href="stylesheets/ie10-viewport-bug-workaround.css" rel="stylesheet">
  <!-- Custom styles for this template -->

  <!-- <link href="stylesheets/sticky-footer-navbar.css" rel="stylesheet"> -->
  <!-- <link rel="stylesheet" href="http://code.jquery.com/ui/1.8.23/themes/base/jquery-ui.css" type="text/css" media="all" /> -->
  <!-- <link rel="stylesheet" href="stylesheets/nv.d3.css"> -->
  <link rel="stylesheet" href="stylesheets/ng-tags-input.min.css" />
  <link rel="stylesheet" href="stylesheets/ng-tags-input.bootstrap.min.css" />
  <link href="stylesheets/v0.9.css" rel="stylesheet" media="screen"> 
  <!-- <link href="http://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css" rel="stylesheet"> -->
  
</head>
<body ng-controller='mainCtrl'>
	
	
  <!-- Fixed navbar -->
  <nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">
          <!-- <span style="font-size:26px;cursor:pointer;align=center" onclick="toggleToolBox(); return false">&#9776;</span> -->
          <button type="button" class="btn btn-primary btn-sm" onclick="toggleToolBox(); return false" data-toggle="tooltip" title="Show/hide the Toolbox">
            <span class="glyphicon glyphicon glyphicon-wrench" aria-hidden="true"></span>
          </button>
          <span class="label label-primary">MIND<sup>v0.85</sup></span>
          <div class="db_full_name" >-<B>M</B>icrobial <B>I</B>nteraction <B>N</B>etwork <B>D</B>atabase </div>
        </a>
      </div>
      
      <a data-toggle="modal" data-target="#fileOpen"> Upload </a>
    </div>
  </nav> 
   
  <footer id="footer-navigation" class="navbar navbar-inverse navbar-fixed-bottom">
        <div class="container-fluid container-footer">
          <div class="navbar-header">
            <ul class="nav navbar-nav navbar-left">
                <li><a><span class="MIND_VisANT">© 2017 MIND-VisANT |</span>
                       N:<span class="badge" ng-bind="node_total"></span>, 
                       SN:<span class="badge">0</span> | 
                       E:<span class="badge" ng-model="edge_total">{{edge_total}}</span>, 
                       SE:<span class="badge">0</span>
                       <span class="MIND_VisANT">|{{statusMsg}}</span>
                   </a>
               </li>

            </ul>
          </div>
        </div>
    </footer>

    <div class="modal fade" id="fileOpen" tabindex="-1" role="dialog" aria-labelledby="VisANTOpenFile">
      <div class="modal-dialog" role="document">
	      <form method="post" action="uploadServlet" enctype="multipart/form-data">
		        <div class="modal-content">
		          <div class="modal-header">
		            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		            <h4 class="modal-title" id="vmodelid">Open File.....</h4>
		          </div>
		          <div class="modal-body">
		            <input type="file" onchange="loadInputFile(this)"  name="fileForUpload"/>
		            <div class="upload-drop-zone" id="drop-zone" ondrop="drop_handler(event, this);" ondragover="dragover_handler(event, this);" ondragleave="dragleave_handler(event, this); ">
		              Just drag and drop files here
		            </div>
		          </div>
		          <div class="modal-footer">
		            <!-- <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
		            <!-- <button type="button" class="btn btn-primary">Upload</button> -->
						<input type="submit" class="btn btn-primary" value="Submit"> 
		          </div>
		        </div>
	      </form>
      </div>
    </div>          
    <div class="modal fade" id="modalLoadAll" tabindex="-1" role="dialog" aria-labelledby="VisANTLoadAll">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">Load all interactions of the context: <b>{{current_cid}}</b></h4>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group">
                <label for="recipient-name" class="control-label">Recipient:</label>
                <input type="text" class="form-control" id="recipient-name">
              </div>
              <div class="form-group">
                <label for="message-text" class="control-label">Message:</label>
                <textarea class="form-control" id="message-text"></textarea>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary" ng-click="loadAllLinks(current_cid, 'both')" >Load</button>
          </div>
        </div>
      </div>
    </div>            
                                  
                                   
  <!-- scripts -->
  <!-- <script src="http://code.jquery.com/jquery-1.11.2.min.js" type="text/javascript"></script> -->
  <!-- <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script> -->
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <!-- used by draggable resizable functions below -->
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script src="javascripts/bootstrap.min.js"></script>
  <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
  <script src="javascripts/ie10-viewport-bug-workaround.js"></script>
    <!--  
    <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.12/angular.min.js"></script>
      <script src='https://cdnjs.cloudflare.com/ajax/libs/angular-nvd3/1.0.7/angular-nvd3.min.js'></script>
    -->    
    <script src="javascripts/nvd3/angular.js"></script>
    <!-- <script src="javascripts/nvd3/d3.js"></script> -->
    <!-- <script src="javascripts/nvd3/nv.d3.js"></script>  -->
    <script src="https://d3js.org/d3.v4.min.js"></script> 
    <script src="javascripts/ng-tags-input.min.js"></script>
    <script src="javascripts/FileSaver.min.js"></script>
    <script src="javascripts/v0.9.js"></script>
    <script>
      $(document).ready(function(){
        $('[data-toggle="tooltip"]').tooltip(); 
      });

      function toggleToolBox() {
        //console.log(document.getElementById("accordion").style.width);
        if (document.getElementById("accordion").style.width === "210px") closeToolBox();
          else openToolBox();
      }
      function closeToolBox() {
          document.getElementById("accordion").style.width = "0px";
          document.getElementById("graphHolder").style.left = "0px";
      }
      function openToolBox() {
          document.getElementById("accordion").style.width = "210px";
      }
      function pinToolBox() {
          //document.getElementById("accordion").style.width = "210px";
          if (document.getElementById("graphHolder").style.width === "210px") document.getElementById("graphHolder").style.left = "0px";
          else document.getElementById("graphHolder").style.left = "210px";
      }

      function drop_handler(ev, mele) {
          ev.preventDefault();ev.stopPropagation();
          console.log("drag-drop-->"+mele.id);
          mele.style.visibility="hidden";
          //document.getElementById("graphHolder").style.visibility="visible";
          // If dropped items aren't files, reject them
          var dt = ev.dataTransfer;
          if (mele.id=="drop-zone") {
            mele.className = 'upload-drop-zone';
          }else mele.className = 'graph-container';
          
          
          if (dt.items) {
            // Use DataTransferItemList interface to access the file(s)
            for (var i=0; i < dt.items.length; i++) {
              if (dt.items[i].kind == "file") {
                var f = dt.items[i].getAsFile();
                console.log("... file[" + i + "].name = " + f.name);
                angular.element(mele).scope().loadFile(f);
                if (mele.id=="drop-zone") mele.innerHTML="<h2>"+f.name+"</h2>";
              }
            }
          } else {
            // Use DataTransfer interface to access the file(s)
            for (var k=0; k < dt.files.length; k++) {
              console.log("--> file[" + k + "].name = " + dt.files[k].name);
              angular.element(mele).scope().loadFile(dt.files[k]);
              if (mele.id=="drop-zone") mele.innerHTML="<h2>"+dt.files[k].name+"</h2>";
            }  
          }
        }
        function dragover_handler(ev, mele) {
          //console.log("dragover-->"+mele.id);
          ev.preventDefault();ev.stopPropagation();
          if (mele.id=="drop-zone") mele.className = 'upload-drop-zone drop';
          else mele.className = 'graph-container drop';
          mele.style.visibility="hidden";
          document.getElementById("graphUploader").style.visibility="visible";
          // Prevent default select and drag behavior
        }
        /*function dragend_handler(ev, mele) {
          console.log("dragEnd");
          // Remove all of the drag data
          var dt = ev.dataTransfer;
          if (dt.items) {
            // Use DataTransferItemList interface to remove the drag data
            for (var i = 0; i < dt.items.length; i++) {
              dt.items.remove(i);
            }
          } else {
            // Use DataTransfer interface to remove the drag data
            ev.dataTransfer.clearData();
          }
        }*/
        function dragenter_handler(ev, mele) {
          //console.log("dragenter-->"+mele.id);
          ev.preventDefault();ev.stopPropagation();
          //mele.style.visibility="hidden";
          document.getElementById("graphUploader").style.visibility="visible";
        }
        function dragleave_handler(ev, mele) {
          //console.log("dragleave-->"+mele.id);
          if (mele.id=="drop-zone") mele.className = 'upload-drop-zone';
          else {
            /* document.getElementById("graphUploader").style.visibility="visible"; */
            mele.style.visibility="hidden";
          }
        }
        function loadInputFile(ele){
          angular.element(ele).scope().loadFile(ele.files[0]);
        }

        //following is to make the modal window draggable and resizable
        $('.modal-content').resizable({
            //alsoResize: ".modal-dialog",
            minHeight: 320, minWidth: 300
        });
        $('.modal-dialog').draggable();

        $('#fileOpen').on('show.bs.modal', function () {
            $(this).find('.modal-body').css({
                'max-height':'100%'
            });
        });
    </script>
  </body>

</html>