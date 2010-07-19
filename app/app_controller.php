<?php

class AppController extends Controller {
	
	var $uses = array('User');
	
	
	/**
	 * return array of Site => ID
	 */
	function sitedetails()
	{
		/* load xml */
		$xml = file_get_contents(ROOT.'/data/apixml/SiteDetails.xml');
		$xmlobj = simplexml_load_string($xml);
		$ns = $xmlobj->getDocNamespaces();
		$xmlobj->registerXPathNamespace('ns', $ns['']);
		
		foreach ($xmlobj->SiteDetails as $o) {
			$site = $o->Site.'';
			$siteid = $o->SiteID.'';
			
			$data[$site] = $siteid;
		}
		
		return $data;
	}
	
	
	/**
	 * get column name of items table.
	 */
	function getitemcols()
	{
		$res = $this->Db->query("DESC items;");
		foreach ($res as $i => $row) {
			if (preg_match('/@/', $row['COLUMNS']['Field'])) {
				$f['`'.$row['COLUMNS']['Field'].'`'] = $row;
			} else {
				$f[$row['COLUMNS']['Field']] = $row;
			}
		}
		
		return $f;
	}
	
	/**
	 * shorthand function of mysql_real_escape_string()
	 */
	function mres($string)
	{
		return mysql_real_escape_string($string);
	}
	
	
	function readbz2xml($file)
	{
		$xml = $this->bzread($file);
		$xmlobj = simplexml_load_string($xml);
		$ns = $xmlobj->getDocNamespaces();
		$xmlobj->registerXPathNamespace('ns', $ns['']);
		
		return $xmlobj;
	}
	
	
	function bzread($file)
	{
		$bz = bzopen($file, "r") or die("Couldn't open $file");
		
		$decompressed_file = '';
		while (!feof($bz)) {
			$decompressed_file .= bzread($bz, 4096);
		}
		bzclose($bz);
		
		return $decompressed_file;		
	}
}

?>
