imp:<?
foreach ($accounts as $accountid => $row) {
	print '<a href="/users/import/'.$row['ebayuserid'].'/" target="_blank">'
		. $row['ebayuserid']. '</a> ';
}
?>
<a href="https://signin.sandbox.ebay.com/ws/eBayISAPI.dll?SignIn&runame=Yoshihiro_Watan-Yoshihir-1b29-4-nstdpc">regacc</a>

<br><br>

<div>
<input type="button" value="全て" onclick="chkall();">
<input type="button" value="リセット" onclick="unchkall();">

<input type="button" value="出品"   onclick="submititems();">
<input type="button" value="コピー" onclick="copyitems();">
<input type="button" value="更新"   onclick="update();">
<input type="button" value="削除"   onclick="delete();">

</div>

<div id="paging"></div>

<form id="filter">
<input type="hidden" name="offset" value="0">
<input type="hidden" name="limit" value="100">
<table class="items">
<tr id="r0">
<th>&nbsp;</th>
<th>ID</th>
<th>画像</th>
<th>タイトル</th>
<th>eBayアカウント</th>
<th nowrap>eBay商品ID</th>
<th nowrap>終了日</th>
<th nowrap>開始価格</th>
</tr>

<tr>
<td></td>
<td><input type="text" name="itemid" size="4"></td>
<td></td>
<td><input type="text" name="title" size="30"></td>
<td>
<select name="accountid" onchange="filter();">
<option value=""></option>
<?
foreach ($accounts as $accountid => $row) {
	echo '<option value="'.$accountid.'">'.$row['ebayuserid'].'</option>';
}	
?>
</select>
</td>
<td><input type="text" name="itemid" size="6"></td>
<td></td>
<td></td>
</tr>
<tbody id="tbdy"></tbody>
</table>
</form>

<div style="width=300px;" id="debug"></div>


<div id="templatewrap" style="display:none;">
<div id="template" style="display:none;">
<table class="detail">
<tr><td>ei</td><td>[ebayitemid]</td></tr>
<tr><td>開始日時</td><td>[starttime]</td></tr>
<tr><td>終了日時</td><td>[endtime]</td></tr>
<tr><td>開始価格</td><td class="edit startprice">[startprice]</td></tr>
<tr><td>タイトル</td><td class="edit title">[title]</td></tr>
<tr><td>説明</td><td class="edit description">[description]</td></tr>
<tr><td>画像</td><td class=""><img src="[galleryurl]"><br>[galleryurl]</td></tr>
</table>
</div>
</div>


<div id="form" style="display:none;">
<form name="itemform">
<table class="detail">
<tr>
	<td>**ei**</td>
	<td>
		[ebayitemid]
	</td>
</tr>
<tr>
	<td>**st**</td>
	<td>[starttime]</td>
</tr>
<tr>
	<td>**st**</td>
	<td>[endtime]</td>
</tr>
<tr>
	<td>**title**</td>
	<td class="formtitle">
	<input class="form" name="title" type="text" size="50">
	</td>
</tr>
<tr>
	<td>**title**</td>
</tr>
<tr>
	<td>**description**</td>
	<td class="formdescription">
	<textarea class="form" name="description" cols="90" rows="5"></textarea>
	</td>
</tr>
</table>
</form>
</div>
