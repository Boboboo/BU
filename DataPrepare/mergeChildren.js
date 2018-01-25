//merge children if they have the same taxid


//read original nodes json data from path
var array = require("/Users/air/Desktop/nodes.json");

var output = [];
array.forEach(function(x) {
    var existing = output.filter(function(v, i) {
      return v.taxid == x.taxid;  
    });
    
    if (existing.length) {
      var existingIndex = output.indexOf(existing[0]);
      output[existingIndex].children = output[existingIndex].children.concat(x.children);
    } else {
        if (typeof x.children == 'string'){
            x.children = [x.children];
        }
        output.push(x);        
    } 
});

console.log("Here is the output result: ");
console.log(output);

 
//save to file as updatedNodes.json 
const fs = require('fs');
const content = JSON.stringify(output);

fs.writeFile("updatedNodes.json", content, 'utf8', function (err) {
    if (err) {
        return console.log(err);
    }
    console.log("The file was saved!");
});



//here is the test data set
// var array=[
//     {
//         "children": [
//           "dummySpecies1",
//           "dummySpecies2"
//         ],
//         "id": "dummyId1",
//         "lineage": "dummyLineage1,dummyLineage2,dummyLineage3",
//         "name": "dummyName",
//         "taxid": "1",
//         "taxlevel": "dummyTaxlevel"
//       },
//     {
//         "children": [
//           "OTU_97.2771",
//           "OTU_97.7348",
//           "OTU_97.19188"
//         ],
//         "id": "species6",
//         "lineage": "p__Bacteroidetes;c__Bacteroidia;o__Bacteroidales;f__Prevotellaceae;g__Prevotella;s__",
//         "name": "Prevotella",
//         "taxid": "838",
//         "taxlevel": "genus"
//       },
//       {
//         "children": [
//           "species6"
//         ],
//         "id": "genus6",
//         "lineage": "p__Bacteroidetes;c__Bacteroidia;o__Bacteroidales;f__Prevotellaceae;g__Prevotella",
//         "name": "Prevotella",
//         "taxid": "838",
//         "taxlevel": "genus"
//       },
//       {
//         "children": [
//           "species7",
//           "species8"
//         ],
//         "id": "genus6",
//         "lineage": "p__Bacteroidetes;c__Bacteroidia;o__Bacteroidales;f__Prevotellaceae;g__Prevotella",
//         "name": "Prevotella",
//         "taxid": "838",
//         "taxlevel": "genus"
//       },
//       {"children": [
//         "dummySpecies3",
//         "dummySpecies4"
//       ],
//       "id": "dummyId2",
//       "lineage": "dummyLineage1,dummyLineage2,dummyLineage3",
//       "name": "dummyName",
//       "taxid": "1",
//       "taxlevel": "dummyTaxlevel"
//     }
// ];

//here is the link about something to solve this kind of problem:
//https://stackoverflow.com/questions/33850412/merge-javascript-objects-in-array-with-same-key

//here is the out put:
// [ 
//   { children: 
//     [ 'dummySpecies1',
//       'dummySpecies2',
//       'dummySpecies3',
//       'dummySpecies4' ],
//    id: 'dummyId1',
//    lineage: 'dummyLineage1,dummyLineage2,dummyLineage3',
//    name: 'dummyName',
//    taxid: '1',
//    taxlevel: 'dummyTaxlevel' 
//  },
//  { children: 
//     [ 'OTU_97.2771',
//       'OTU_97.7348',
//       'OTU_97.19188',
//       'species6',
//       'species7',
//       'species8' ],
//    id: 'species6',
//    lineage: 'p__Bacteroidetes;c__Bacteroidia;o__Bacteroidales;f__Prevotellaceae;g__Prevotella;s__',
//    name: 'Prevotella',
//    taxid: '838',
//    taxlevel: 'genus' 
//   } 
// ]


