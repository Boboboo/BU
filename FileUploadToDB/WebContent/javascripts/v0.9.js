

angular.module('VisANTApp', ['ngTagsInput']).controller('mainCtrl', function($scope, $http, $window) {

  'use strict';
  // $scope.formData = {};
  // $scope.todoData = {};
  // $scope.species2query  = 'Roseburia';
  $scope.file2load='';
  $scope.tax_rank='Mixed';
  $scope.ccs=[];//used to store contexts
  $scope.statusMsg="Right-dragging to PAN, mouse-wheel to ZOOM, double-clicking to query...";
  $scope.nodeData={};
  //$scope.theNetwork=new VisAntNetwork(d3.select("#graphHolder"));
  $scope.theNetwork=new VisAntNetwork($('#graphHolder'), $scope);
  $scope.layoutManager=new LayoutManager();
  d3.select(window).on('resize', function(){ $scope.theNetwork.resize();});
  $scope.current_cid='C3001';
  $scope.node_total=0;$scope.edge_total=0;
  $scope.isLabel_on=$scope.theNetwork.isLabelOn;
  $scope.isAuto_fit=$scope.theNetwork.isAutoFit;
  $scope.loadHrefLink = function(hhh){ $window.open(hhh, '_blank'); };
  $scope.toggleContext=function(mid){$scope.theNetwork.toggleContext(mid);};
  $scope.loadAllLinks=function(mid, rLevel){
    if (mid.startsWith("C3") && !rLevel) $('#modalLoadAll').modal('show');
    else $scope.theNetwork.queryInteractions("/vserver/DAI?command=mind_get_all_links&contextid="+mid, null, $scope.theNetwork);
    //<a data-toggle="modal" data-target="#fileOpen" >Open...</a>
  };
  $scope.queryInteraction=function(){
    var str="";
    for (var i = 0; i < $scope.tags.length; i++) {
      str += $scope.tags[i].name + ",";
      $scope.theNetwork.queryInteractions("/vserver/DAI?command=mind_get_link&name="+$scope.tags[i].tax_id, $scope.tags[i].tax_id, $scope.theNetwork);
    }
  };
  $scope.tags = [{"name":"Klebsiella pneumoniae","tax_id":573}];
  //console.log($scope.theNetwork);
  
  $scope.theNetwork.queryContexts("/vserver/DAI?command=mind_get_context", $scope.theNetwork);
  
  $scope.loadTags = function(query) {
      //console.log('/api/v1/ints_tax/' +query);
      var res=$http.get('vserver/DAI?command=mind_search_node&name=' +query);
      return res;
  };
  $scope.changeEdgeCutoff=function(){
    $scope.theNetwork.drawGraph(d3.values($scope.theNetwork.vNodes), $scope.theNetwork.getVisibleEdges());
    //console.log("changed....");
  };
  $scope.changeContextColor=function(ccid){
    $scope.theNetwork.drawGraph(d3.values($scope.theNetwork.vNodes), $scope.theNetwork.getVisibleEdges());
  };
  $scope.mng_save_svg=function(mfFormat){
    if (!$scope.theNetwork.ioMan) $scope.theNetwork.ioMan=new IOManager($scope.theNetwork);
    $scope.theNetwork.ioMan.saveFile(mfFormat);
  };
  $scope.mng_forceLayout=function(){$scope.layoutManager.forceLayout($scope.theNetwork);};
  $scope.mng_circleLayout=function(){$scope.layoutManager.circleLayout($scope.theNetwork, null, null);};
  $scope.mng_invertSelection=function(){$scope.theNetwork.invertSelection();};
  $scope.mng_deleteSelected=function(){$scope.theNetwork.deleteSelected();};
  $scope.loadFile=function(fff){
    //http://luxiyalu.com/angular-all-about-inputfile/
    console.log("loading..."+ fff.name);
    var reader = new FileReader();
    reader.readAsText(fff);
    reader.onload = function () {
      var mroot=JSON.parse(reader.result);
      var glyph=$scope.theNetwork;
      //glyph.fullDom.statusMsg.innerHTML="finished loading......"
      var hname=mroot.body_site.toLowerCase();
      var catt={}; catt.habitat=mroot.body_site;
      catt.disease_state=mroot.disease_state; catt.experiment_info=mroot.experiment_info;
      catt.host=mroot.host; catt.interaction_type=mroot.interaction_type;
      catt.pval_cutoff=mroot.pval_cutoff; catt.pvalue_method=mroot.pvalue_method;
      catt.pubmed_id=mroot.pubmed_id; catt.condition=mroot.condition;
      var tcid=glyph.getMatchedContext(catt);
      var gLinkType=[0,0];//fromType, toType
      if (mroot.interaction_type){
        if (mroot.interaction_type==="binding") gLinkType=[0,1];
      }

      var otu_nodes={}, newAdded=[];
      for (var i=0;i<mroot.nodes.length;i++){
        //console.log(mroot.nodes[i]);
        var uid=mroot.nodes[i].taxid;
        if (typeof uid === "undefined" || uid<0) {
          console.log(mroot.nodes[i].id+" do not have tax id");
          uid=mroot.nodes[i].name;
        }
        var sNode=glyph.addNodeByName(uid, newAdded);
        sNode.data.name=mroot.nodes[i].name;
        sNode.data.dType=mroot.nodes[i].taxlevel.toLowerCase();
        //sNode.data.lineage=mroot.nodes[i].lineage;
        sNode.data.habitat=mroot.body_site;
        otu_nodes[mroot.nodes[i].id]=sNode;
      }
      mroot.links.forEach(function(link) {
        var reg=/,/;
        var ssns=link.source.split(reg);
        var ttns=link.target.split(reg);
        var tLinkType=gLinkType;
        if (link.interaction_type){//local specification of the link type
          if (link.interaction_type==="correlation") tLinkType=[0,0];
            else if (link.interaction_type==="binding") tLinkType=[0,1];
        }
        for (var i=0; i<ssns.length;i++){
          var sNode=otu_nodes[ssns[i]];
          for (var j=0;j<ttns.length;j++){
            var tNode=otu_nodes[ttns[j]];
            var edge=new VEdge(sNode, tNode);
            edge.id=createEdgeID(edge.source, edge.target);
            glyph.maxEdgeWeight=Math.max(glyph.maxEdgeWeight, Math.abs(link.weight));
            glyph.minEdgeWeight=Math.min(glyph.minEdgeWeight, Math.abs(link.weight));

            var ln=new Linkage(sNode.getLabel(), tNode.getLabel(), tcid.cid, link.weight,tLinkType[0], tLinkType[1]);
            if (typeof glyph.vEdges[edge.id] === 'undefined') {//new edge
              //edge.score=link.weight;
              edge.habitat=hname;//temp solution, we are not considering cross-habitation link
              //console.log(edge.weight);
              glyph.vEdges[edge.id]=edge;
              edge.addLink(ln);
              //edge.ori=link;
            }else {glyph.vEdges[edge.id].addLink(ln);}//console.log("----");console.log(glyph.vEdges[edge.id].ori);console.log(link);console.log(glyph.vEdges[edge.id]);}
          }
        }        
      });
      if (newAdded.length>0) glyph.fullDom.layoutManager.circleLayout(glyph, newAdded, null);
      glyph.drawGraph(d3.values(glyph.vNodes), glyph.getVisibleEdges());
    };
  };
  /*function parseFile(){
        var doesColumnExist = false;
        var data = d3.csv.parse(reader.result, function(d){
          doesColumnExist = d.hasOwnProperty("someColumn");
          return d;   
        });
        console.log(doesColumnExist);
      }*/

  
  $scope.label_onoff = function() {
    var graph=$scope.theNetwork;
    graph.drawGraph(d3.values(graph.vNodes), graph.getVisibleEdges());
  };
  $scope.autoFit_onoff = function() {$scope.theNetwork.isAutoFit=!$scope.theNetwork.isAutoFit;};
  $scope.clearNetwork = function() {
    if (confirm("Clear the whole network??")) {
      //$scope.theNetwork.svg.selectAll('*').remove();//remove all visual SVG components
      //Object.keys ignores properties in the prototype chain
      var graph=$scope.theNetwork;
      Object.keys(graph.vNodes).forEach(function(key) { delete graph.vNodes[key]; });
      Object.keys(graph.vEdges).forEach(function(key) { delete graph.vEdges[key]; });
      $scope.theNetwork.svg_nodes.selectAll('*').remove();
      $scope.theNetwork.svgPaths.selectAll('*').remove();
      $scope.node_total=0; $scope.edge_total=0;
      //console.log("Nodes: "+Object.keys($scope.theNetwork.vNodes).length);
    }    
  };
  $scope.zooming = function(zStep) {
    $scope.theNetwork.stepZooming(zStep);
    /*$scope.theNetwork.zoomer.currentZoomScale+=zStep;
    $scope.theNetwork.svg.attr("transform", d3.zoomIdentity.scale($scope.theNetwork.zoomer.currentZoomScale));*/
    //$scope.theNetwork.zoomer.scaleBy($scope.theNetwork.svg.transition().duration(350), 1+zStep);
    //$scope.theNetwork.svg.call($scope.theNetwork.zoomer.transform, d3.zoomIdentity.scale(1+zStep));
    
  };
  $scope.reSet = function() {
    $scope.theNetwork.reSet(true);
  };
  $scope.fit2page = function() {$scope.theNetwork.fit2page();};
});

var VisAntNetwork=function(netDom, ng_scope){
  this.vEdges={};this.vNodes={};//used to store data of nodes and edges, public variable started with this.
  this.maxEdgeWeight=0;this.minEdgeWeight=0; this.edgeWCutoff=0; this.nContextCutoff=1;
  this.graphDom=netDom;this.fullDom=ng_scope;
  this.width=netDom.width(); this.height=netDom.height(); 
  this.minZoom=0.1; this.maxZoom=10;
  //this.force = d3.layout.forceSimulation().force().charge(-100).linkDistance(80).size([this.width, this.height]); 
  var theNetwork=this;//this will not work in closure of zoomer, defined as var, it is a private varble
  this.zoomer=d3.zoom().scaleExtent([this.minZoom, this.maxZoom]).on("start", function(){
      console.log(d3.event.transform);
      console.log("------------------------------------------");
      d3.event.transform.k=theNetwork.zoomer.currentZoomScale;//not perfect solution, need a better fix
      //d3.event.transform.x=theNetwork.zoomer.currentPan[0];d3.event.transform.y=theNetwork.zoomer.currentPan[1];
      //console.log(d3.event.transform);
      console.log("----> "+theNetwork.zoomer.currentPan);
    }).on("end", function () {
      theNetwork.zoomer.currentZoomScale=d3.event.transform.k;
      theNetwork.zoomer.currentPan=[d3.event.transform.x,d3.event.transform.y];
      console.log("end----> "+theNetwork.zoomer.currentPan);
    }).on("zoom", function () {
      //if (d3.event.sourceEvent.which === 1){ //initiate on right mouse button only
        console.log(d3.event.transform);
        theNetwork.svg.attr("transform", d3.event.transform);
        /*theNetwork.zoomer.currentZoomScale=d3.event.transform.k; 
        theNetwork.zoomer.currentPan=[d3.event.transform.x, d3.event.transform.y];*/
        //console.log(d3.event.transform);
    }).filter(function(){ return (event.button === 0||event.button === 2); /*wheel & right mouse button*/});
  this.zoomer.currentZoomScale=1.0;this.zoomer.currentPan=[0,0];
  this.topDrag= d3.drag().on("start", function (d) {
      d3.event.sourceEvent.stopPropagation();
    }).on("drag", function(d, i) {
      if (d3.event.sourceEvent.which ===1){ //initiate on left mouse button only
        //theNetwork.svg.attr("transform", "translate(" + d3.event.x+","+d3.event.y + ")");
        //d3.select(this).attr("cx", d.x = d3.event.x).attr("cy", d.y = d3.event.y);
      }
   });//what this topDrag is doing??
  this.nodeDrag= d3.drag().on("start", function (d) {d3.event.sourceEvent.stopPropagation();})
    .on("drag", function(d, i) {
      d.x += d3.event.dx;   d.y += d3.event.dy;   theNetwork.updateGraph();
     //console.log(d.x+"-->"+(d.x*theNetwork.zoomer.currentZoomScale)+"--->"+theNetwork.zoomer.translate()[0]);
     //d3.select(d3.select(this).select(".vnode")[0][0]).attr("cx", d.x).attr("cy", d.y);//works, but selection [0][0] is not good, aedge will also need to change
   });
  //d3.select("body").on("keydown.brush", this.keydowned).on("keyup.brush", this.keyupped);//not working, need to understand more on call and key handling
  this.svgRoot=d3.select("#graphHolder").append("svg").attr("width", this.width).attr("height", this.height);
  this.arrowData = [
      { id: 0, name: 'circle', path: 'M 0, 0  m -5, 0  a 5,5 0 1,0 10,0  a 5,5 0 1,0 -10,0', viewbox: '-6 -6 12 12' },
      { id: 1, name: 'square', path: 'M 0,0 m -5,-5 L 5,-5 L 5,5 L -5,5 Z', viewbox: '-5 -5 10 10' },
      { id: 2, name: 'arrow', path: 'M 0,0 m -5,-5 L 5,0 L -5,5 Z', viewbox: '-5 -5 10 10' },
      { id: 2, name: 's_arrow', path: 'M 0,0 m 7,-5 L 17,0 L 7,5 Z', viewbox: '7 -5 10 10' },
      { id: 3, name: 'stub', path: 'M 0,0 m 3,-5 L 1,-5 L 1,5 L 3,5 Z', viewbox: '3 -5 2 10' },
      { id: 3, name: 's_stub', path: 'M 0,0 m 15,-5 L 13,-5 L 13,5 L 15,5 Z', viewbox: '15 -5 2 10' }
    ];
    this.svgRoot.append("defs").selectAll('marker')
    .data(this.arrowData)
    .enter()
    .append('svg:marker')
      .attr('id', function(d){ return 'vm_' + d.name;})
      .attr('markerHeight', 12)
      .attr('markerWidth', 10)
      .attr('markerUnits', 'userSpaceOnUse')
      .attr('orient', 'auto')
      .attr('refX', 8)
      .attr('refY', 0)
      .attr('viewBox', function(d){ return d.viewbox;})
      .append('svg:path')
        .attr('d', function(d){ return d.path;});
        //.attr('fill', function(d,i) { return color(i);});
   /*this.svgRoot.append("defs").selectAll("marker")
       .data(["suit", "licensing", "resolved"])
     .enter().append("marker")
       .attr("id", function(d) { return d; })
       .attr("viewBox", "0 -5 10 10")
       .attr("refX", 12)
       .attr("refY", 0)
       .attr("markerWidth", 4)
       .attr("markerHeight", 4)
       .attr("orient", "auto")
     .append("path")
       .attr("d", "M0,-5L10,0L0,5");   */
  /*this.svgRoot.append("defs").selectAll("marker")
      .data(["suit", "licensing", "resolved"])
    .enter().append("marker")
      .attr("id", function(d) { return d; })
      .attr("viewBox", "0 -5 10 10")
      .attr("refX", 15)
      .attr("refY", 0)
      .attr("markerWidth", 6)
      .attr("markerHeight", 6)
      .attr("orient", "auto")
    .append("path")
      .attr("d", "M0,-5L10,0L0,5 L10,0 L0, -5")
      .style("stroke", "#4679BD")
      .style("opacity", "0.6");*/
  this.svg=this.svgRoot.append("g").on("mousedown", function(){
      if (d3.event.button===0) d3.event.stopImmediatePropagation(); // left mouse down, stop zoom, https://bl.ocks.org/mbostock/6140181
      //console.log(d3.event.button); console.log("-->"); console.log(d3.event.ctrlKey);
    }).call(this.zoomer).call(this.topDrag).on("dblclick.zoom", null).on("contextmenu", function (d, i) {d3.event.preventDefault();});
  this.svg.append("rect").attr("width", this.width).attr("height", this.height)
                  .style("fill", "none").style("pointer-events", "all");
  this.svg=this.svg.append("g");//this g applies the zoomer
  this.svgPaths=this.svg.append("g").attr("class", "link");//this g applies to the path, all path elements are under this group
  this.brush = this.svg.append("g").attr("class", "brush");
  this.svg_nodes = this.svg.append("g").attr("class", "node");
  this.isLabelOn=false; this.isAutoFit=false;
  this.nIndex=0; this.contexts={};
  this._cContext=d3.scaleOrdinal(d3.schemeCategory20b);
  this.tip_div = d3.select("body").append("div").attr("class", "tooltip").style("opacity", 0);
};

VisAntNetwork.prototype = {
  getNIndex: function(){return this.nIndex++;},
  defaultNodeSize: 6,
  /*addContext: function(ct){
    if (!this.contexts[ct.cid]) {
      this.conetxts[ct.cid]=ct; ct.color=this._cContext(ct.cid);
    }
  },
  getContext: function(cid){return this.contexts[cid];},*/
  getMatchedContext: function (atts){
    var matched=null, cArray=Object.values(this.contexts);
    for (var i = 0; i < cArray.length; i++) {//use for loop so we can use break and continue
    //Object.keys(all).forEach(function(key) { //for each existing context
      if (cArray[i].getCNum(cArray[i].cid)<7000) {continue;}//only compare used loaded contexts
      matched=cArray[i]; 
      if ( matched.habitat!==atts.habitat ) matched=null;
      if ( matched!==null && matched.disease_state!==atts.disease_state) matched=null; 
      if ( matched!==null && matched.experiment_info!==atts.experiment_info) matched=null;
      if ( matched!==null && matched.host!==atts.host) matched=null; 
      if ( matched!==null && matched.interaction_type!==atts.interaction_type) matched=null;
      if ( matched!==null && matched.pval_cutoff!==atts.pval_cutoff) matched=null; 
      if ( matched!==null && matched.pvalue_method!==atts.pvalue_method) matched=null;
      if ( matched!==null && matched.pubmed_id!==atts.pubmed_id) matched=null; 
      if ( matched!==null && matched.condition!==atts.condition) matched=null;
      if (matched!==null) {break;}
    }
    if (!matched) {
      matched=new InterContext(); matched.color=this._cContext(matched.cid);//set the color
      Object.keys(atts).forEach(function(key) { matched[key]=atts[key]; });//copy atts
      this.contexts[matched.cid]=matched; //add to the system
      this.fullDom.ccs.push(matched);
      //console.log(matched);
    }
    return matched;
  },
  removeUserAddedContext: function (){
    var matched=null, cArray=Object.values(this.contexts);
    for (var i = 0; i < this.fullDom.ccs.length; i++) {//use for loop so we can use break and continue
      var tc=this.fullDom.ccs[i];
      if (tc.getCNum(tc.cid)<7000) {
        this.fullDom.ccs.pop(tc);
        //this.contexts[matched.cid];
      }
    }//only compare used loaded contexts
  },
  reSet:function(blDraw){
    //this.zoomer.translate([0, 0]).scale(1).event(this.svg); 
    this.svg.attr("transform", d3.zoomIdentity);
    this.zoomer.currentZoomScale=1; this.zoomer.currentPan[0]=0; this.zoomer.currentPan[1]=0 ;
    //console.log(tNetwork.svg);
    if (blDraw) {//reset button from UI
      VisAntNetwork.prototype.defaultNodeSize=6;
      this.drawGraph(d3.values(this.vNodes), this.getVisibleEdges());
    }
  },
  stepZooming: function(sStep) {
    this.zoomer.currentZoomScale+=sStep;
    if (this.zoomer.currentZoomScale>this.maxZoom) this.zoomer.currentZoomScale=this.maxZoom;
    if (this.zoomer.currentZoomScale<this.minZoom) this.zoomer.currentZoomScale=this.minZoom;
    var t=d3.zoomIdentity.translate(this.zoomer.currentPan[0],this.zoomer.currentPan[1]).scale(this.zoomer.currentZoomScale);
    this.svg.attr("transform", t);
    console.log("as----->");
    console.log(t);
    //$scope.theNetwork.zoomer.scaleBy($scope.theNetwork.svg.transition().duration(350), 1+zStep);
    //$scope.theNetwork.svg.call($scope.theNetwork.zoomer.transform, d3.zoomIdentity.scale(1+zStep));
    
  },
  resize: function () {
    this.width=this.graphDom.width();this.height=this.graphDom.height();
    this.svgRoot.attr("width", this.width).attr("height", this.height);
  },
  updateLabel: function(ele, d){
    ele.attr("dx", 12).attr("dy", ".35em").text(function(d) { return d.drawLabel(); })
       .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
  },
  invertSelection: function(){
    this.svg_nodes.selectAll(".vnode").select("circle").classed("selected", function(d) {return (d.selected = !d.selected);});
  },
  deleteSelected: function(){
    var glyph=this;
    Object.keys(glyph.vEdges).forEach(function(key) { 
      var ve=glyph.vEdges[key];
      if (ve.source.selected ||ve.target.selected) delete glyph.vEdges[key]; 
    });
    Object.keys(glyph.vNodes).forEach(function(key) { 
      if (glyph.vNodes[key].selected) {
        //console.log("deleting---->"+glyph.vNodes[key])
        delete glyph.vNodes[key]; 
      }
    });
    glyph.drawGraph(d3.values(glyph.vNodes), glyph.getVisibleEdges());
  },
  nudge: function(dx, dy) {
    var tNetwork=this;
    tNetwork.svg_nodes.selectAll(".vnode").select("circle").filter(function(d) { return d.selected; })
        //.attr("cx", function(d) { return d.x += dx; }).attr("cy", function(d) { return d.y += dy; });
        .each(function (dd, i) {
          var tNode=d3.select(this);
          tNode.attr("cx", dd.x += dx ).attr("cy", dd.y += dy );
          if (tNetwork.fullDom.isLabel_on||dd.blLabelOn)
              d3.select(this.parentNode).select("text").attr("transform", "translate(" + dd.x + "," + dd.y + ")" );
        });
    //glyph.svg_paths.filter(function(d) { return d.source.selected||d.target.selected; })
    //did not understand why above line did not work
    tNetwork.svg.selectAll("path").filter(function(d) { return d.source.selected||d.target.selected; })
        .attr("d", function(d) {return d.pathString(1);});
  },
  updateGraph: function (){//for only position change and label on/off, no new nodes or edges
    var tNetwork=this;
    tNetwork.svgPaths.selectAll("path").attr("d", function(d) {return d.pathString(1);});//VEdge always starts with 1, the rest is handled by VEdgeDup
    //this.svg.selectAll(".vnode").attr("cx", function(d) { return d.x; }).attr("cy", function(d) { return d.y; });
    tNetwork.svg_nodes.selectAll(".vnode")
        //.attr("cx", function(d) { return d.x += dx; }).attr("cy", function(d) { return d.y += dy; });
        .each(function (dd, i) {
          var tNode=d3.select(this).select("circle");
          /*dd.x=Math.max(dd.width()/2, Math.min(tNetwork.width - dd.width()/2, dd.x));
          dd.y=Math.max(dd.height()/2, Math.min(tNetwork.height - dd.height()/2, dd.y));*/
          tNode.attr("cx", dd.x ).attr("cy", dd.y);
          if (tNetwork.fullDom.isLabel_on||dd.blLabelOn)
              d3.select(this).select("text").attr("transform", "translate(" + dd.x + "," + dd.y + ")" );
        });
    
  },
  keydowned: function() {
    if (!d3.event.metaKey) {
      switch (d3.event.keyCode) {
        case 38: this.nudge( 0, -1); break; // UP
        case 40: this.nudge( 0, +1); break; // DOWN
        case 37: this.nudge(-1,  0); break; // LEFT
        case 39: this.nudge(+1,  0); break; // RIGHT
      }
    }
    this.shiftKey = d3.event.shiftKey || d3.event.metaKey;
  },
  keyupped: function() {
    this.shiftKey = d3.event.shiftKey || d3.event.metaKey;
  },
  /*brushstarted: function() {
    console.log(this);
    if (d3.event.sourceEvent.type !== "end") {

      this.svg_nodes.selectAll(".vnode").select("circle").classed("selected", function(d) {
        
        //console.log(d.selected+" "+d.previouslySelected+" "+ shiftKey+" "+ d.selected);
        d.selected = d.previouslySelected;
        d.previouslySelected = shiftKey && d.selected;
        //debugger;
        return d.previouslySelected;
        //return d.selected = d.previouslySelected = shiftKey && d.selected;
        //console.log(d.selected+" "+d.previouslySelected+" "+ shiftKey+" "+ d.selected);
        //return xtest;
      });
    }
  },*/
  /*brushed: function() {
    if (d3.event.sourceEvent.type !== "end") {
      var selection = d3.event.selection;
      console.log("--->"+this);
      this.svg_nodes.classed("selected", function(d) {
        return d.selected = d.previouslySelected ^
            (selection != null
            && selection[0][0] <= d.x && d.x < selection[1][0]
            && selection[0][1] <= d.y && d.y < selection[1][1]);
      });
    }
  },

  brushended: function() {if (d3.event.selection != null) { d3.select(this).call(d3.event.target.move, null);}},*/

  /*mousedowned: function(d) {
    console.log(this);
    if (shiftKey) {
      d3.select(this).classed("selected", d.selected = !d.selected);
      d3.event.stopImmediatePropagation();
    } else if (!d.selected) {
      node.classed("selected", function(p) { return p.selected = d === p; });
    }
  },
  dragged: function(d) { nudge(d3.event.dx, d3.event.dy);},*/
};

VisAntNetwork.prototype.fit2page=function (w, h, sx, sy, nns) {
  if  (!arguments.length) {w=this.width;h=this.height;sx=0;sy=0;nns=this.vNodes;}
  var i=0, cxmin=0, cxmax=0, cymin=0, cymax=0;
  var glyph=this; glyph.reSet();
  //console.log(scale+","+xPan+"<--->"+yPan+","+(sx+w));
  Object.keys(glyph.vNodes).forEach(function(key) { 
    var vn= glyph.vNodes[key]; 
    //vn.size(Math.round(vn.size()*scale));
    cxmax=(i===0)?vn.nodeBound(1, true):Math.max(cxmax, vn.nodeBound(1, true));
    cxmin=(i===0)?vn.nodeBound(-1, true):Math.min(cxmin, vn.nodeBound(-1, true));
    cymax=(i===0)?vn.nodeBound(1, false):Math.max(cymax, vn.nodeBound(1, false));
    cymin=(i===0)?vn.nodeBound(-1, false):Math.min(cymin, vn.nodeBound(-1, false));
    i++;
  });
  var mxscale=d3.scaleLinear().domain([cxmin, cxmax]).range([sx, sx+w]),  myscale=d3.scaleLinear().domain([cymin, cymax]).range([sy, sy+h]);
  //VisAntNetwork.prototype.defaultNodeSize=Math.round(VisAntNetwork.prototype.defaultNodeSize*scale);
  Object.keys(glyph.vNodes).forEach(function(key) { 
    var vn= glyph.vNodes[key]; vn.x=mxscale(vn.x); vn.y=myscale(vn.y);
  });
  this.drawGraph(d3.values(this.vNodes), this.getVisibleEdges());
};

VisAntNetwork.prototype.getVisibleEdges=function () {
    var glyph=this, veArray=[], map=this.vEdges;
    for (var key in map) {
      var ve=map[key], evLinks=ve.getVisibeLinks(glyph);
      if (evLinks.length>=glyph.nContextCutoff) {
        for (var i=0;i<evLinks.length;i++){
          //console.log("--->"+evLinks[i].aIndex);
          var dup=new VEdgeDup(ve, i, evLinks[i].aIndex, evLinks.length);
          veArray.push(dup);
          //console.log(dup);
        }
      }
    }
    //for (var k=0;k<veArray.length;k++) console.log(veArray[k]);
    /*console.log("total edge:" + veArray.length+"  total links:"+ve.links.length);
    console.log(ve);*/
    return veArray;
};

VisAntNetwork.prototype.addNodeByName=function(name, newNodes) {
    //return nnData[name] || (nnData[name] = {name: name});
  if (typeof this.vNodes[name] === 'undefined'){//not defined before
  //if (!(name in nnData)){//not good because it also include build in function such as toString
    //nnData[name] = {name: name, x:50, y:50};
    this.vNodes[name] = new VNode(name);
    this.vNodes[name].index=this.getNIndex();
    newNodes.push(this.vNodes[name]);
  } 
  return this.vNodes[name];
};
VisAntNetwork.prototype.toggleContext=function(cid){
  this.contexts[cid].visible=!this.contexts[cid].visible;
  this.svgPaths.selectAll('*').remove();//have to remove all SVG links as otherwise the edge color will have problem, this may impact the performance
  this.drawGraph(d3.values(this.vNodes), this.getVisibleEdges());
};
VisAntNetwork.prototype.queryContexts= function(rUrl, graph){
  d3.json(rUrl, function(error, ccs) {
    if (error) throw error;
    ccs.forEach(function(ctext) {
      var catt=graph.contexts[ctext.id];
      if  (typeof catt=== 'undefined') {//new context from DB, add to the system
        catt= new InterContext(ctext.id); 
        catt.color=graph._cContext(catt.cid);
        graph.contexts[ctext.id]=catt; graph.fullDom.ccs.push(catt);
      }
      catt.disease_state=ctext.disease_state; catt.habitat=ctext.body_site; catt.pubmed_id=ctext.pubmed_id;
      catt.total_link=ctext.total_link; catt.experiment_info=ctext.experiment_info;catt.host=ctext.host;
      catt.interaction_type=ctext.interaction_type;catt.pvalue_method=ctext.pvalue_method;catt.condition=ctext.condition;
      catt.pval_cutoff=(ctext.pval_cutoff>0)?ctext.pval_cutoff:""; 
    });
  });
};

VisAntNetwork.prototype.queryInteractions= function(rUrl, query_name, graph){
  //d3.csv("data/graph.csv", function(error, links) {
  var newAdded=[];
  //TODO: in case the searched query is also the new node
  d3.json(rUrl, function(error, links) {
    if (error) throw error;
    // Create nodes for each unique source and target.
    if (links.length === 0){
      console.log(query_name+" has no interactions");
      return;
    }
    var oldNum=Object.keys(graph.vEdges).length;
    links.forEach(function(link) {
      var sNode=graph.addNodeByName(link.bio_entity_1, newAdded);
      sNode.data.name=link.entity_1_name;sNode.data.dType=link.entity_1_type;
      var tNode=graph.addNodeByName(link.bio_entity_2, newAdded);
      tNode.data.name=link.entity_2_name;tNode.data.dType=link.entity_2_type;
      var edge=new VEdge(sNode, tNode);
      edge.id=createEdgeID(edge.source, edge.target);
      graph.maxEdgeWeight=Math.max(graph.maxEdgeWeight, Math.abs(link.weight));
      graph.minEdgeWeight=Math.min(graph.minEdgeWeight, Math.abs(link.weight));
      //console.log(link.id);
      if (typeof graph.vEdges[edge.id] === 'undefined' ) {//new edge
        sNode.habitat=link.habitat_1;
        tNode.habitat=link.habitat_2;
        edge.habitat=link.habitat_1;//temp solution, we are not considering cross-habitation link
       /* if (typeof graph.contexts[link.method_id] === 'undefined' ){//new context, add
          var catt=new InterContext(link.method_id); 
          catt.disease_state=link.disease; catt.habitat=link.habitat_1; catt.pubmed_id=link.literature_id;
          catt.color=graph._cContext(catt.cid);
          graph.contexts[link.method_id]=catt; graph.fullDom.ccs.push(catt);
        }*/
        //var tcid=graph.getMatchedContext(catt);
        var ln=new Linkage(sNode.getLabel(), tNode.getLabel(), link.contextID, link.weight);
        //if (link.p_value_fdr<0) {console.log(link.p_value_fdr+"  "+ln.toType);}
        edge.addLink(ln);
        //console.log(edge);
        graph.vEdges[edge.id]=edge;
      }
    });
    //console.log(graph.maxEdgeWeight+","+graph.minEdgeWeight);
    if (query_name!==null) graph.vNodes[query_name].data.isLinkLoaded=true;
    // Extract the array of nodes from the map by name.
    //console.log("------------>>"+newAdded.length);
    if (query_name===null) graph.fullDom.layoutManager.circleLayout(graph, newAdded, null);
      else graph.fullDom.layoutManager.explortoryLayout(graph, newAdded, graph.vNodes[query_name]);
    //xxx.node_total=Object.keys(graph.vEdges).length;
    if (oldNum==Object.keys(graph.vEdges).length){
      console.log("no new interactions");
    }else {
      if (graph.isAutoFit) graph.fit2page();
      graph.drawGraph(d3.values(graph.vNodes), graph.getVisibleEdges());
    }
  });
};

VisAntNetwork.prototype.drawGraph=function (nodes, links){
  //console.log("..................");
  // Create the link lines.
  var glyph=this;
  svg=glyph.svg;
  //force=glyph.forceSimu;
  this.fullDom.node_total=nodes.length;
  this.fullDom.edge_total=links.length;
  this.fullDom.$applyAsync();//force stattus bar to be refreshed
 
  glyph.svgPaths.selectAll('*').remove();//remove all paths and nodes, ootherwise error prone
  glyph.svg_nodes.selectAll('*').remove();
  glyph.svg_paths = glyph.svgPaths.selectAll("path").data(links);
  glyph.svg_paths.exit().remove();
    //Inserts link element before the first DOM element with class node, otherwise, new line will appear above existing node
    /*svg_paths.enter().insert("path", ".node").attr("class", function(d){return (d.score()<0)?"link negative":"link";})
                      .style("stroke-width", function(d) { 
                        //console.log(d.score(0));
                         return ((Math.abs(d.score())-glyph.minEdgeWeight)*(3.5-0.1)/(glyph.maxEdgeWeight-glyph.minEdgeWeight)+0.1);//page p1
                        })
                      .style("stroke",function(d) { return d.edgeColor()|| d.getAutoColor(0, glyph);})//glyph.habitates[ return d.habitat].color;})
                      .attr("d", function(d) {return d.pathString(1);});*/
  glyph.svg_paths.enter().insert("path", ".node")
            .each(function (d, i) {
              var theEdge=d3.select(this);
              //theEdge.attr("class", "link");
              if (d.score()<0) theEdge.style("stroke-dasharray", "0, 2, 1");
              theEdge.style("stroke-width", ((Math.abs(d.score())-glyph.minEdgeWeight)*(3.5-0.1)/(glyph.maxEdgeWeight-glyph.minEdgeWeight)+0.1));
              theEdge.style("stroke",  d.edgeColor()|| d.getAutoColor(0, glyph));
              theEdge.attr("d",  d.pathString(1));
              //if (d.toType()!==0) theEdge.attr("marker-end", "url(#arrow)");
              if (d.toType()===1) theEdge.attr("marker-end", "url(#vm_arrow)");
                //else if (d.toType()===3) theEdge.attr("marker-end", "url(#vm_stub)");
              //if (d.fromType()===3) theEdge.attr("marker-start", "url(#vm_s_stub)");
              /*theEdge.on("mouseover", function(d) {
                //console.log("mouse over :"+d);
                    glyph.tip_div.transition()
                       .duration(200)
                       .style("opacity", 0.9);
                     glyph.tip_div.html("d.tips")
                       .style("left", (d3.event.pageX) + "px")
                       .style("top", (d3.event.pageY - 28) + "px");
                     })
                   .on("mouseout", function(d) {
                     glyph.tip_div.transition()
                       .duration(500)
                       .style("opacity", 0);
                     });*/
            });
  
  // Create the node circles.
    //update
  //following line is temp solution
  //glyph.svg_nodes = svg.selectAll("circle").data(nodes);
  //enter
  glyph.svg_nodes.selectAll("vnode").data(nodes).enter().append("g")
            .attr("class", "vnode")
            .on("mousedown", mousedowned).call(d3.drag().on("drag", dragged))//.call(glyph.nodeDrag)

  /*svg_nodes.append("circle").attr("class", "vnode")
            .style("fill", function(d) { return d.nodeColor();})
            .style("stroke", function(d) { return d.borderColor();})
            .attr("cx", function(d) { return d.x; })
            .attr("cy", function(d) { return d.y; })
            .attr("r", function(d) { return d.size(); })
            .on("dblclick",function(d){ if (!d.data.isLinkLoaded) glyph.queryInteractions("/api/v1/ints_tax/"+d.name, d.name, glyph); });*/
            //.call(glyph.myDrag);
  /*svg_nodes.append("circle").attr("class", "vnode")
            .each(function (d, i) {
              var theNode=d3.select(this);
              theNode.style("fill", d.nodeColor());theNode.style("stroke", d.borderColor());
              theNode.attr("cx", d.x);theNode.attr("cy", d.y);
              theNode.attr("r", d.size());
              theNode.append("title").text( d.tips);//for tooltip
              theNode.on("dblclick",function(){ if (!d.data.isLinkLoaded) glyph.queryInteractions("/api/v1/ints_tax/"+d.name, d.name, glyph); });
            });*/
            .each(function (d, i) {
   //svg_nodes.each(function (d, i) {
            var theNode=d3.select(this);
            theNode.on("dblclick",function(){ if (!d.data.isLinkLoaded) glyph.queryInteractions("/vserver/DAI?command=mind_get_link&name="+d.name, d.name, glyph); });
                   //.on("click", function(){d.blSelected=!d.blSelected; glyph.drawSelection(theNode, d);});
            theNode.append("circle")
              //.attr("class", "vnode")
              .classed("selected", function(d) { return d.selected;})
              .style("fill", d.nodeColor())
              //.style("stroke", d.borderColor()) 
              .attr("cx", d.x)
              .attr("cy", d.y)
              .attr("r", d.size())
              .on("mouseover", function(d) {
                     glyph.tip_div.transition()
                       .duration(200)
                       .style("opacity", 0.9);
                     glyph.tip_div.html(d.tips)
                       .style("left", (d3.event.pageX) + "px")
                       .style("top", (d3.event.pageY - 28) + "px");
                     })
                   .on("mouseout", function(d) {
                     glyph.tip_div.transition()
                       .duration(500)
                       .style("opacity", 0);
                     });
              //.append("title").text( d.tips);//for tooltip
            //if (!d.selected) theNode.select("circle").style("stroke", d.borderColor()) ;
            //if (d.selected) console.log("drawing--->"+d.selected);

            if (glyph.fullDom.isLabel_on||d.blLabelOn){
              glyph.updateLabel(theNode.append("text"), d);
                /*theNode.append("text")
                    .attr("dx", 12)
                    .attr("dy", ".35em")
                    .text(function() { return d.name; })
                    .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });*/
            }

              
            });         
  glyph.brush.call(d3.brush().extent([[0, 0], [glyph.width, glyph.height]])//.handleSize(2)
        .on("start", brushstarted)
        .on("brush", brushed)
        .on("end", brushended));

  function brushstarted () {
    if (d3.event.sourceEvent.type !== "end") {
      glyph.svg_nodes.selectAll(".vnode").select("circle").classed("selected", function(d) {
        //console.log(d.selected+" "+d.previouslySelected+" "+ shiftKey+" "+ d.selected);
        d.selected = d.previouslySelected;
        d.previouslySelected = glyph.shiftKey && d.selected;
        //debugger;
        return d.previouslySelected;
        //return d.selected = d.previouslySelected = shiftKey && d.selected;
        //console.log(d.selected+" "+d.previouslySelected+" "+ shiftKey+" "+ d.selected);
        //return xtest;
      });
    }
  }
  function brushed () {
    if (d3.event.sourceEvent.type !== "end") {
      var selection = d3.event.selection;
      glyph.svg_nodes.selectAll(".vnode").select("circle").classed("selected", function(d) {
        return d.selected = d.previouslySelected ^
            (selection != null
            && selection[0][0] <= d.x && d.x < selection[1][0]
            && selection[0][1] <= d.y && d.y < selection[1][1]);
      });
    }
  }
  function brushended() {if (d3.event.selection != null) { d3.select(this).call(d3.event.target.move, null);}}
  function mousedowned(d) {
    if (glyph.shiftKey) {
      d3.select(this).select("circle").classed("selected", d.selected = !d.selected);
      d3.event.stopImmediatePropagation();
    } else if (!d.selected) {
      glyph.svg_nodes.selectAll(".vnode").select("circle").classed("selected", function(p) { return p.selected = d === p; });
    }
    //console.log("md:"+d.selected)
  }
  function dragged (d) { glyph.nudge(d3.event.dx, d3.event.dy);}
  
  //exit    svg_nodes.exit().remove();
    //add node label
  //glyph.forceLayout(this.fullDom);
};
/*VisAntNetwork.prototype.drawSelection=function (sNode, d){
  if (d.blSelected){
    sNode.append("rect").attr("class", "sel_node").attr("x", d.x-d.width()/2-2).attr("y", d.y-d.height()/2-2);
    sNode.append("rect").attr("class", "sel_node").attr("x", d.x+d.width()/2).attr("y", d.y-d.height()/2-2);
    sNode.append("rect").attr("class", "sel_node").attr("x", d.x+d.width()/2).attr("y", d.y+d.height()/2);
    sNode.append("rect").attr("class", "sel_node").attr("x", d.x-d.width()/2-2).attr("y", d.y+d.height()/2);
  }else sNode.selectAll("rect").remove();
};*/
var LayoutManager=function(){};
LayoutManager.prototype.forceLayout=function(glyph){
  var mmsvg=glyph.svg;//tick is closure, this willnot work there.
  if (!glyph.forceSimu) glyph.forceSimu = d3.forceSimulation()//.force("charge", d3.forceManyBody().strength(-100))
      .force("center", d3.forceCenter(glyph.width/2, glyph.height/2)).stop();//.linkDistance(80);//.size([glyph.width, glyph.height]); 
    else glyph.forceSimu.alpha(0.3);
  var nArray=d3.values(glyph.vNodes);
  var k = Math.sqrt(nArray.length / (glyph.width * glyph.height));//-10/k for charge
  glyph.forceSimu.force("charge",d3.forceManyBody().strength(-0.3/k));//.gravity(100 * k);
  glyph.forceSimu.nodes(nArray)
      .force("link", d3.forceLink(glyph.getVisibleEdges()).distance(80))
      .on("tick", function(){glyph.updateGraph();})
      .on("end", function(){console.log("force layout ends");})//if (glyph.isAutoFit) glyph.fit2page();
      .restart();
  //function tick() {glyph.updateGraph(); }
};

LayoutManager.prototype.circleLayout=function(glyph, todoLayout, center){//, cwidth, cheight, center){
  
  var mmsvg=glyph.svg;//tick is closure, this willnot work there.
  var lManager=this;
  var nkeys=Object.keys(glyph.vNodes);
  var nodeNum=nkeys.length;
  var nodes2Layout=[];
  var cwidth=glyph.width, cheight=glyph.height;
  if (todoLayout!==null) nodes2Layout=todoLayout;//todoLayout is used for batch load
  else {
    nkeys.forEach(function(nKey) {
      var tn=glyph.vNodes[nKey];
      if (lManager.isLayoutable(tn)) nodes2Layout.push(tn);
    });
  }
  var cx=cwidth/2, cy=cheight/2;
  if (nodes2Layout.length===0) return;
  if (center!==null) { cx=center.x; cy=center.y;}
  if (nodes2Layout.length<=1) {
    nodes2Layout[0].x=cx;
    nodes2Layout[0].y=cy;
      //if (cn instanceof GNode) ((GNode)cn).circleLayout();
    return; //one node, what the helll for layout?
  }
  var rr=this.calRadius(nodes2Layout.length), angle=2.0*Math.PI/nodes2Layout.length;
  for (var i=0;i<nodes2Layout.length;i++){
      var vvn=nodes2Layout[i];
      vvn.x=cx+rr*Math.cos(angle*i);
      vvn.y=cy+rr*Math.sin(angle*i);
  }         
  glyph.updateGraph();
};

LayoutManager.prototype.explortoryLayout=function(glyph, todoLayout, startNode){//, cwidth, cheight, center){
  if (todoLayout.length<1)  return;
  var mmsvg=glyph.svg;//tick is closure, this willnot work there.
  var lManager=this;
  var cwidth=glyph.width, cheight=glyph.height;
  var cx=cwidth/2, cy=cheight/2;
  var angle, iniAngle, visRatio, visCount;
  var nodes2Layout=[];
  for (var i=0;i<todoLayout.length;i++){
      if (todoLayout[i]!=startNode) {
        todoLayout[i].axisNode=startNode;
        nodes2Layout.push(todoLayout[i]);
      }else {
        startNode.x=cx;
        startNode.y=cy;
        startNode.axisNode=null;
      }
  }
  if (! startNode.axisNode ){//start node is also new node, put in the center of network
    this.circleLayout(glyph, nodes2Layout, startNode);
  }else {
    var x2=startNode.x, y2=startNode.y, x1=startNode.axisNode.x, y1=startNode.axisNode.y;
    var dis=Math.sqrt( Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2) );
    var rr=this.calRadius(nodes2Layout.length);
    var offset=20;//additial radious when nod number is small
    if (rr<50) offset=30;
    //console.log(rr+"---"+nodes2Layout.length);
    startNode.x =x1+(x2-x1)*(rr+dis+offset)/dis;
    startNode.y=y1+(y2-y1)*(rr+dis+offset)/dis;
    //console.log(x1+","+startNode.x);
    if (nodes2Layout.length===1){//one node, can not do circlelayout as it will put node in teh center
       nodes2Layout[0].x=x1+(x2-x1)*(2*rr+dis+offset)/dis;
       nodes2Layout[0].y=y1+(y2-y1)*(2*rr+dis+offset)/dis;
    }else this.circleLayout(glyph, nodes2Layout, startNode);
  }
  
};
LayoutManager.prototype.calRadius=function(nodeNum){
    var mimR=20, h=20;  //h shall be node size, set to 10 temporary
    if (nodeNum<6) return mimR;
    var angle=2*Math.PI/nodeNum;
    var r=h/Math.sin(angle);
    //if (Math.sin(angle)===0) console.log("sin(angle)=0" );
    if (r>mimR) return r*1.2;
      else return mimR;
};
LayoutManager.prototype.isLayoutable=function(vn){
    if(vn.isVisible()){// && vn.getGroup()==null){
      return true;
    }
    return false;
};

function VNode(name) {
  this.data =this.isNumber(name)? {tax_id:name}:{name:name};
  this.data.links=[]; this.data.isLinkLoaded=false;
  this.blVisible=true;this.blLabelOn=false;this.blSelected=false;
  this.x=0;this.y=0;
  this.selected = false;this.previouslySelected = false;//for brush
  //this.name=this.data.name;
}
VNode.prototype = {
  constructor: VNode,
  //my:function(){return this.data.name;},
  isNumber : function(n) { return !isNaN(parseFloat(n)) && isFinite(n);}, 
  nodeColor: function (nc){if (nc) {this_nColor=nc; return;} return this._nColor||this.getAutoColor();},
  borderColor: function(bc){if (bc) {this._bColor=bc; return;} return this._bColor || d3.rgb(this.nodeColor()).darker().toString();},
  getAutoColor: function(){
    if (this.data.isLinkLoaded) return "#ce6dbd"; 
    if (this.data.dType){
      if (!VNode._nColors) VNode._nColors=d3.scaleOrdinal(d3.schemeCategory20);//d3.scale.category10();
      return VNode._nColors(this.data.dType);
    }else {
      if (!VNode._defaultColor) VNode._defaultColor="#95b196";
      return VNode._defaultColor;
    }
  },
  drawLabel: function (blIgnoreDup){return (this._dLabel)?this._dLabel:this.data.name;},
  getLabel: function (blIgnoreDup){return (this.data.tax_id)?this.data.tax_id:this.data.name;},
  size: function(s){if (s) {this._size=s; return;} return this._size||VisAntNetwork.prototype.defaultNodeSize;},
  width: function(s){if (!arguments.length) return this._width||this.size()*2; this._width=s;  },
  height: function(s){if (!arguments.length) return this._height||this.size()*2; this._height=s;  },
  isVisible: function(){ return this.blVisible;},
  get name(){return this.data.tax_id;},//for d3 only, use getLabel in general
  get tips(){
    var my_tip="Name: <b>"+this.data.name+"</b>";//+"("+this.x+","+this.y+")";
    if (typeof this.data.tax_id !=="undefined") my_tip +="<br>NCBI taxonomy ID: <b>"+this.data.tax_id+"</b>";
    if (typeof this.data.dType !=="undefined") my_tip +="<br>Taxonomic rank: <b>"+this.data.dType+"</b>";
    if (typeof this.data.lineage !=="undefined") my_tip +="<br>Lineage: <b>"+this.data.lineage+"</b>";
    return my_tip;
  },
  drawLabel: function(s){ if (!arguments.length) return this._drawLabel||this.data.name; this._drawLabel=s;},
  nodeBound: function(pos, x){//atm, just ignore labels
    if (x) return this.x+pos*this.width()/2;
      else return this.y+pos*this.height()/2;
  },
};

function VEdgeDup(tve, di, ai, num){//this class wraps the VEdge class for the design of mutiple edges between two nodes
  this.ve=tve;this.dupIndex=di; this.arrayIndex=ai; this.num=num;//dupIndex points to the ith visible line, arrayIndex points to the index of Links array in VEDge
  this.source=tve.source;this.target=tve.target;//required for D3 force layout
}
VEdgeDup.prototype = {//this class is just designed for D3 temp solution, do not need to be saved
  edgeColor: function(ec){ return this.ve.edgeColor(ec);},
  score: function(){return this.ve.links[this.arrayIndex].weight;},
  getAutoColor: function(i, glyph){ return this.ve.getAutoColor(this.arrayIndex, glyph);},
  toType: function(){ return this.ve.links[this.arrayIndex].toType;},
  fromType: function(){ return this.ve.links[this.arrayIndex].fromType;},
  pathString: function(i){return this.ve.pathString(this.dupIndex, this.num);},//num: total number of visible links
};
function VEdge(s, t) {
  this.source = s; this.target=t;//for d3
  this.links=[];
  //this.name=this.data.name;
}
VEdge.prototype = {
  constructor: VEdge,
  score: function(){return this.links[0].weight;},
  addLink: function(ln){
    if (this.links.length===0) this.links.push(ln);
      else {
        var blFound=false;
        for (var i=0;i<this.links.length;i++){
          if (ln.isSame(this.links[i])) {blFound=true; break;}
        }
        if (!blFound) this.links.push(ln);
      }
  },
  edgeColor: function(ec){
    if (ec) {this._eColor=ec; return;}
    return this._eColor;
  },
  getAutoColor: function(i, glyph){ 
    if (this.links[i]){ return glyph.contexts[this.links[i].cid].color;}
  },
  isVisible: function (glyph){
    return (this.source.isVisible() && this.target.isVisible() &&  getVisibeLinks(glyph).length>0);
  },
  getVisibeLinks: function (glyph){
    var lls=[];
    for (var i=0; i<this.links.length;i++){
      if (this.links[i].isVisible(glyph)){
        this.links[i].aIndex=i;
        lls.push(this.links[i]);
      }
    }
    return lls;
  },
  pathString: function (i, n){
    var tnum=n;i=i+1;
    if ( tnum%2 !==0){//odd number of links
      if (i===1) return "M" + this.source.x + "," + this.source.y + "L" + this.target.x + "," + this.target.y;
      else if (i%2===0) return getCurve(this.source, this.target, (i-1)/2, tnum);
      else return getCurve(this.target, this.source, (i-1)/2, tnum);
    }else  {
      //console.log("@@@ ->"+getCurve(this.source, this.target)+"\n"+getCurve(this.target, this.source));
      if (i%2 !==0) return getCurve(this.source, this.target, i/2, tnum);
        else return getCurve(this.target, this.source, i/2, tnum);
    }
    function getCurve(s, t, xi, tn){
        var dx = t.x-s.x, dy = t.y-s.y,dr = Math.sqrt(dx * dx + dy * dy)/((1-(Math.ceil(xi)/tn))-(tn%2===0?0.1:0));
        return "M" + s.x + "," + s.y + "A" + dr + "," + dr + " 0 0,1 " + t.x + "," + t.y;
    }
  },
};

function Linkage(f, t, mid, w, ft, tt) {
  this.fromName=f; this.toName=t; this.cid=mid; this.weight=w; this.fromType=(ft)?ft:0;this.toType=(tt)?tt:0;
  if (w && w<0) {this.fromType=3; this.toType=3; }
}
Linkage.prototype = {
  isSame: function(ln){
    return (this.fromName===ln.fromName && this.toName===ln.toName && this.cid===ln.cid && this.weight===ln.weight && this.toType===ln.toType && this.fromType===ln.fromType);
  },
  isVisible: function (glyph){
    return (glyph.contexts[this.cid].visible && this.weight &&  Math.abs(this.weight)>=glyph.edgeWCutoff);
  },
};
function InterContext(mid, desc) {
  //if mid is missing, it is null 
  //this.mid=!mid ? "M"+this.mCounter():mid;
  this.cid=mid || "C"+this.mCounter();
  this.desc=desc; this.visible=true; this.host="Homo Sapiens"; 
  this.dbDisabled=(this.getCNum(this.cid)>=7000)?true:false;
}
InterContext.prototype = {
  constructor: InterContext,
  mCounter: function (){
    //if (!InterContext._mc) InterContext._mc=3000; //InterContext._mc inital is NaN if not declared
    if (typeof InterContext._mc==="undefined") InterContext._mc=7000;//check whether _mc is desclared 
    else InterContext._mc++;
    return InterContext._mc;//_mc is a static variable
  },
  getHtmlTip: function(){
    /*var cstr="<b>ContextID</b>:"+this.cid+"<br><b>Host</b>:"+this.host+"<br><b>Disease state</b>:"+this.disease_state;
    cstr+="<br><b>Habitat</b>:"+this.habitat+"<br>Experiment info</b>:"+this.experment_info;*/
    var cstr="Context:"+this.cid+", "+this.host+", "+this.disease_state;
    cstr+=", "+this.habitat+((typeof this.experiment_info !=='undefined' && this.experiment_info.length>1)?", Experiment info:"+this.experiment_info:"");
    cstr+=(typeof this.interaction_type !=='undefined'  && this.interaction_type.length>1)?", "+this.interaction_type:"";
    cstr+=(typeof this.pval_cutoff !=='undefined' && this.pval_cutoff>0)?", "+"pval_cutoff:"+this.pval_cutoff.toFixed(6).replace(/\.?0*$/,''):"";
    cstr+=(typeof this.pvalue_method !=='undefined' && this.pvalue_method.length>1)?", "+"pvalue_method:"+this.pvalue_method:"";
    cstr+=(typeof this.condition !=='undefined'  && this.condition.length>1)?", "+this.condition:"";
    return cstr;
  },
  getTitle: function(){
    var tstr=this.habitat+", "+this.disease_state+", "+this.host+" ...";
    return tstr;
  },
  tip4All: function(mmid){
    //console.log(Number.parseInt(mmid.substr(1), 10));
    //var mn=Number.parseInt(mmid.substr(1), 10);
    return (this.getCNum(mmid)<7000)?"Click to load all "+this.total_link+ " links":"";
  },
  getCNum: function(mmid){ return Number.parseInt(mmid.substr(1), 10);},
  getPubMedURL: function(){ return "https://www.ncbi.nlm.nih.gov/pubmed/"+this.pubmed_id;},
};

function IOManager(curNetwork) {this.mglyph=curNetwork;}
IOManager.prototype = {
  saveFile: function (fType){
    var svgString = this.getSVGString(this.mglyph.svgRoot.node());
    if (fType==="png"){
      this.svgString2Image( svgString, this.mglyph.width, this.mglyph.height, 'png', save ); // passes Blob and filesize String to the callback
    }else if (fType==="svg"){saveAs(new Blob([svgString], {type: "image/svg+xml;charset=utf-8"}), 'VWebSVG.'+fType);}

    function save( dataBlob, filesize ){
      saveAs( dataBlob, 'VWebPNG.'+fType ); // FileSaver.js function
    }
  },
  // Below are the functions that handle actual exporting:
  // getSVGString ( svgNode ) and svgString2Image( svgString, width, height, format, callback )
  getSVGString: function ( svgNode ) {
    svgNode.setAttribute('xlink', 'http://www.w3.org/1999/xlink');
    var cssStyleText = getCSSStyles( svgNode );
    appendCSS( cssStyleText, svgNode );
    var serializer = new XMLSerializer();
    var svgString = serializer.serializeToString(svgNode);
    svgString = svgString.replace(/(\w+)?:?xlink=/g, 'xmlns:xlink='); // Fix root xlink without namespace
    svgString = svgString.replace(/NS\d+:href/g, 'xlink:href'); // Safari NS namespace fix

    return svgString;

    function getCSSStyles( parentElement ) {
      var selectorTextArr = [];

      // Add Parent element Id and Classes to the list
      selectorTextArr.push( '#'+parentElement.id );
      for (var c = 0; c < parentElement.classList.length; c++)
        if ( !contains('.'+parentElement.classList[c], selectorTextArr) )
          selectorTextArr.push( '.'+parentElement.classList[c] );

      // Add Children element Ids and Classes to the list
      var nodes = parentElement.getElementsByTagName("*");
      for (var i = 0; i < nodes.length; i++) {
        var id = nodes[i].id;
        if ( !contains('#'+id, selectorTextArr) ) selectorTextArr.push( '#'+id );

        var classes = nodes[i].classList;
        for (var k = 0; k < classes.length; k++)
          if ( !contains('.'+classes[k], selectorTextArr) ) selectorTextArr.push( '.'+classes[k] );
      }
      // Extract CSS Rules
      var extractedCSSText = "";
      for (var ii = 0; ii < document.styleSheets.length; ii++) {
        var s = document.styleSheets[ii];
        try { if(!s.cssRules) continue; } catch( e ) {
          if(e.name !== 'SecurityError') throw e; // for Firefox
          continue;
        }
        var cssRules = s.cssRules;
        for (var r = 0; r < cssRules.length; r++) {
          if ( contains( cssRules[r].selectorText, selectorTextArr ) )  extractedCSSText += cssRules[r].cssText;
        }
      }
      
      return extractedCSSText;

      function contains(str,arr) { return arr.indexOf( str ) === -1 ? false : true;}
    }

    function appendCSS( cssText, element ) {
      var styleElement = document.createElement("style");
      styleElement.setAttribute("type","text/css"); 
      styleElement.innerHTML = cssText;
      var refNode = element.hasChildNodes() ? element.children[0] : null;
      element.insertBefore( styleElement, refNode );
    }
  },
  svgString2Image: function ( svgString, width, height, mformat, callback ) {
    var format = mformat ? mformat : 'png';//default to png file
    var imgsrc = 'data:image/svg+xml;base64,'+ btoa( unescape( encodeURIComponent( svgString ) ) ); // Convert SVG string to data URL
    var canvas = document.createElement("canvas");
    var context = canvas.getContext("2d"); canvas.width = width; canvas.height = height;
    var image = new Image();
    image.onload = function() {
      context.clearRect ( 0, 0, width, height ); context.drawImage(image, 0, 0, width, height);
      canvas.toBlob( function(blob) {
        var filesize = Math.round( blob.length/1024 ) + ' KB';
        if ( callback ) callback( blob, filesize );
      });  
    };
    image.src = imgsrc;
  },
};

function createEdgeID(source, target){ return Math.min(target.index, source.index)+"__"+Math.max(target.index,source.index);}
