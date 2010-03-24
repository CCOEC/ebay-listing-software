<?xml version="1.0" encoding="Shift_JIS" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/"> 
	<xsl:apply-templates/>
</xsl:template> 

<xsl:template match="/root"> 

	<table cellspacing="0" cellpadding="4" class="list" name="tbl1">
	<tr align="center">
		<td class="title">No.</td>
		<td class="title">����</td>
		<td class="title">�D��x</td>
		<td class="title">��</td>
		<td class="title">�^�C�g��</td>
		<td class="title">�ŏI�X�V��</td>
		<td class="title">����</td>
	</tr>

	<xsl:for-each select="./task">
	<tr align="center">
		<td class="list"><xsl:value-of select="./@id"/></td>
		<td class="list"><xsl:value-of select="./@category"/></td>
		<td class="list"><xsl:value-of select="./@priority"/></td>
		<td class="list"><xsl:value-of select="./@status"/></td>
		<td class="list" align="left"><xsl:value-of select="./title"/></td>
		<td class="list"><xsl:value-of select="./@mod_date"/></td>
		<td class="list">
			<a href="" onclick="document.all('aaa').style.display='none';return false;">close</a>
			<a href="" onclick="document.all('aaa').style.display='';return false;">open</a>
		</td>
	</tr>
	</xsl:for-each>

	</table>
	
</xsl:template> 

</xsl:stylesheet>
