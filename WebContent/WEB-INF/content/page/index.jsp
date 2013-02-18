<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ListersIn - <s:text name="eBayListingSoftware"/></title>

<s:if test="#request.locale.language=='ja'">
<meta name="keywords" content="eBay,出品,ツール,一括出品,オークション">
<meta name="description" content="ListersInはeBay出品ツールです。シンプルな画面で操作が簡単です。無料デモをお試しください！">
</s:if>
<s:else>
<meta name="keywords" content="eBay,listing,software,seller,tool">
<meta name="description" content="ListersIn is an eBay listing software. Simple, easy and Gmail like user interface. Try free demo account before sign up!">
</s:else>
<link rel="stylesheet" type="text/css" href="/css/index.css">
<link rel="shortcut icon" href="/img/favicon.png">
<link rel="canonical" href="http://listers.in/"/>
<script type="text/javascript" src="/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/js/jquery.dump.js"></script>
<script type="text/javascript" src="/js/index.js"></script>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-32099440-1']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
</head>
<body>

<div id="container">
  
  <div id="header" class="clearfix">
	  <div id="logo">
	    <a href="/"><img src="/img/logo.png" alt="ListersIn" /></a>
	  </div>
    <div id="header_right">
      <a href="/?request_locale=en">English</a> | <a href="/?request_locale=ja">日本語</a>
    </div>
  </div>
  
  <div id="content" class="clearfix">
	  
	  <div id="leftpane">
	    <div style="margin-bottom:10px;">
		    <h1>ListersIn - <s:text name="eBayListingSoftware"/></h1>
	    </div>
      
			<img src="/img/topimage.jpg" width="500" />
			
      <h2><s:text name="Features"/></h2>
	    <ul class="features">
        <s:if test="#request.locale.language=='ja'">
		      <li>複数のeBayアカウントを管理できます。</li>
		      <li>複数のeBayアカウントから質問メッセージに返答することができます。</li>
					<li>落札されずに終了した商品を自動際出品する際に、"Best Offer"(値引き交渉)オプションを付けられます。</li>
		      <li>1商品に12枚の写真を一度にアップし、eBayのサーバへ送信できます。</li>
		      <li>シンプルで使いやすい画面です。</li>
		      <li>タイトルが重複している商品を見つけることができます。</li>
          <li>現在のところ無料です。</li>
        </s:if>
        <s:else>
		      <li>Manage multiple eBay accounts.</li>
		      <li>Reply to messages from multiple eBay accounts.</li>
					<li>Add "Best Offer" option when unsold item is auto relisted.</li>
		      <li>Upload 12 pictures for one item to eBay server at once.</li>
		      <li>Simple and easy user interface.</li>
		      <li>Find duplicate items by title.</li>
          <li>FREE.</li>
        </s:else>
 	    </ul>
	    
      <h2><s:text name="Notice"/></h2>
	    <ul class="notice">
        <s:if test="#request.locale.language=='ja'">
					<li>この出品ツールは現在開発中です。まだバグがあるかも知れません。</li>
					<li>このツールをお試し頂く際にはご注意下さい。</li>
					<li>実際に出品する前に「Test Auction」カテゴリにテスト出品されることをお勧めします。</li>
        </s:if>
        <s:else>
					<li>This listing tool is under development. There still may be some bugs.</li>
					<li>Please be careful about using this seller software.</li>
					<li>Please list to "Test Auction" category before you create actual listings.</li>
        </s:else>
      </ul>
      
      <h2><s:text name="Feedback"/></h2>
	    <ul class="feedback">
        <s:if test="#request.locale.language=='ja'">
					<li>あなたからのコメントをお待ちしています！</li>
					<li>
						あなたのコメントやご提案を、是非
						<a href="http://forum.listers.in/" target="_blank">こちらのフォーラム</a>
						へご投稿下さい。
					</li>
					<li>または support [at] listers.in へメールでお知らせ下さい。</li>
        </s:if>
        <s:else>
					<li>We would like to hear from you!</li>
					<li>
						Please post your comments and suggestions to
						<a href="http://forum.listers.in/" target="_blank">THIS FORUM</a>.
					</li>
					<li>Or send an email to support [at] listers.in.</li>
        </s:else>
      </ul>
	    
	    <h2><s:text name="Screenshot"/></h2>
	    <a href="/img/screenshot.png" target="_blank">
		    <img id="screenshot" src="/img/screenshot.png" />
	    </a>
	    <div>
		    <a href="/img/screenshot.png" target="_blank">Click to enlarge</a>
	    </div>
	    <h2><s:text name="Blog"/></h2>
	    <ul class="blog">
		    <s:iterator value="feed.entries">
		      <li>
			      <a href="${link}">${title}</a>
						<div style="text-align:right; color:#aaa;">${publishedDate}</div>
						<s:if test="#request.locale.language=='ja'">
							<p>${description.value.replaceAll("<[^>]+>", "")}・・・</p>
						</s:if>
						<s:else>
							<p>${description.value}</p>
						</s:else>
		      </li>
		    </s:iterator>
	    </ul>
	    
	    <!--
	        todo: signup tutorial
	      -->
      
	  </div>
	  
	  <div id="rightpane">
	    
	    <div id="signinbox">
		    
		    <div style="margin-bottom:10px; font-weight:bold; font-size:120%;">
          <s:text name="SignIn"/>
		    </div>
		    
		    <form method="post" action="/">
		      <label for="email"><s:text name="Email"/></label><br />
		      <input type="text" id="email" name="email" size="40" value=""><br />
          <s:text name="DemoAccount"/>: demo@listers.in<br/>
          <br />
		      
		      <label for="password"><s:text name="Password"/></label><br />
		      <input type="password" id="password" name="password" size="40" value=""><br />
          <s:text name="DemoAccount"/>: demo<br/>
          <br />
		      
		      <button><s:text name="SignIn"/></button>
		      
		      <a id="forgotpasswordlink" href="#">Forgot password ?</a>
		    </form>
				
				<div id="forgotpassworddiv">
					<div id="forgotpasswordmessage"></div>
					<form method="post" action="">
						Please input your email address and click button.<br />
						<input type="text" id="fpemail" name="fpemail" size="40" value=""><br />
						<br />
						<button><s:text name="SendPasswordResetMail"/></button>
					</form>
				</div>
		    
	    </div>

	    <div id="signupbox">
		    
		    <div style="margin-bottom:10px; font-weight:bold; font-size:120%;">
          <s:text name="SignUp"/>
					&nbsp;<span style="color:#088253;">FREE!</span>
		    </div>
		    <div id="signupmessage"></div>

				<div style="margin:5px 0;">
					Please input your email address and password.<br/>
					This is NOT your eBay account. You don't need to<br/>
					input your eBay account email and password.
				</div>
				
		    <form method="post" action="">
		      <s:text name="Email"/><br />
		      <input type="text" name="email" size="40"><br /><br />
		      
		      <s:text name="Password"/><br />
		      <input type="text" name="password" size="40"><br /><br />
		      
		      <s:text name="ConfirmPassword"/><br />
		      <input type="text" name="password2" size="40"><br /><br />
		      
		      <button><s:text name="SignUp"/></button>
		    </form>
		    
	    </div><!-- signupbox -->
			
			<div id="ebaylogo">
				<img src="/img/developersprogrammember_vert.png" />
			</div>
			
			<div style="margin:10px 20px;">
				<span id="siteseal"><script type="text/javascript" src="https://seal.godaddy.com/getSeal?sealID=yll915qgMP7yndpJAjqzM9yakbiWYQYo1WTrUzmLPbgYnnprdG"></script></span>
			</div>
			
		</div><!-- rightpane -->
		
  </div><!-- content -->
  
	<div id="footer">
		ListersIn - <s:text name="eBayListingSoftware"/>
	</div>
	
</div><!-- container -->

</body>
</html>
