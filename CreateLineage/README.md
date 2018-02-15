### Create Lineage

This is a process to get lineage for each taxid.

* writeLineageToDB
 * count all taxid in a HashMap map1
 * get parent_taxid(number) lineage for each key in mapl, and put result in a newMap
 * write result in newMap to database, table mind_lineage_number
 
* writeResult
  * for lineage in each row in mind_lineage_number, get related number for each parent_taxid
  * count type for each parent_taxid in a newMap
  * for six types of names, just get one name as a part in the result lineage, and there is a priority order
    * unique name
    * scientific name
    * genbank common name
    * common name
    * equivalent name
    * synonym
  * then insert each result lineage to database, table mind_lineage
  
