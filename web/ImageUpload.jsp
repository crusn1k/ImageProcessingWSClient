<%--
    Document   : index
    Created on : Aug 19, 2010, 6:03:04 PM
    Author     : n1sh1kanT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
      
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
<style type="text/css">
<!--
body {
	font: 100%/1.4 Verdana, Arial, Helvetica, sans-serif;
	background: #4E5869;
	margin: 0;
	padding: 0;
	color: #000;
	background-image: url(BLOG%20BACKGROUND.jpg);
	background-repeat: repeat-x;
}

/* ~~ Element/tag selectors ~~ */
ul, ol, dl { /* Due to variations between browsers, it's best practices to zero padding and margin on lists. For consistency, you can either specify the amounts you want here, or on the list items (LI, DT, DD) they contain. Remember that what you do here will cascade to the .nav list unless you write a more specific selector. */
	padding: 0;
	margin: 0;
}
h1, h2, h3, h4, h5, h6, p {
	margin-top: 0;	 /* removing the top margin gets around an issue where margins can escape from their containing div. The remaining bottom margin will hold it away from any elements that follow. */
	padding-right: 15px;
	padding-left: 15px; /* adding the padding to the sides of the elements within the divs, instead of the divs themselves, gets rid of any box model math. A nested div with side padding can also be used as an alternate method. */
	font-family: "Comic Sans MS", cursive;
	font-size: 12px;
	font-weight: normal;
	color: #FFF;
	text-align: right;
}
a img { /* this selector removes the default blue border displayed in some browsers around an image when it is surrounded by a link */
	border: none;
}

/* ~~ Styling for your site's links must remain in this order - including the group of selectors that create the hover effect. ~~ */
a:link {
	color:#414958;
	text-decoration: underline; /* unless you style your links to look extremely unique, it's best to provide underlines for quick visual identification */
}
a:visited {
	color: #4E5869;
	text-decoration: underline;
}
a:hover, a:active, a:focus { /* this group of selectors will give a keyboard navigator the same hover experience as the person using a mouse. */
	text-decoration: none;
}

/* ~~ this container surrounds all other divs giving them their percentage-based width ~~ */
.container {
	width: 80%;
	max-width: 1260px;/* a max-width may be desirable to keep this layout from getting too wide on a large monitor. This keeps line length more readable. IE6 does not respect this declaration. */
	min-width: 780px;/* a min-width may be desirable to keep this layout from getting too narrow. This keeps line length more readable in the side columns. IE6 does not respect this declaration. */
	background: #FFF;
	margin: 0 auto; /* the auto value on the sides, coupled with the width, centers the layout. It is not needed if you set the .container's width to 100%. */
}

/* ~~the header is not given a width. It will extend the full width of your layout. It contains an image placeholder that should be replaced with your own linked logo~~ */
.header {
	background-color: #000;
	background-image: url(Untitled.png);
	color: #FFF;
}

/* ~~ This is the layout information. ~~

1) Padding is only placed on the top and/or bottom of the div. The elements within this div have padding on their sides. This saves you from any "box model math". Keep in mind, if you add any side padding or border to the div itself, it will be added to the width you define to create the *total* width. You may also choose to remove the padding on the element in the div and place a second div within it with no width and the padding necessary for your design.

*/
.content {
	padding: 10px 0;
	background-color: #111;
	color: #FFF;
	font-family: Chiller, "Most Wazted", "Viner Hand ITC", "Harlow Solid Italic";
	font-size: xx-small;
	font-weight: bold;
}

/* ~~ This grouped selector gives the lists in the .content area space ~~ */
.content ul, .content ol {
	padding: 0 15px 15px 40px; /* this padding mirrors the right padding in the headings and paragraph rule above. Padding was placed on the bottom for space between other elements on the lists and on the left to create the indention. These may be adjusted as you wish. */
}

/* ~~ The footer ~~ */
.footer {
	padding: 10px 0;
	background-color: #6F7D94;
	background-image: url(footer.png);
	font-family: "Comic Sans MS", cursive;
	text-decoration: blink;
	color: #FFF;
	text-align: right;
}

/* ~~ miscellaneous float/clear classes ~~ */
.fltrt {  /* this class can be used to float an element right in your page. The floated element must precede the element it should be next to on the page. */
	float: right;
	margin-left: 8px;
}
.fltlft { /* this class can be used to float an element left in your page. The floated element must precede the element it should be next to on the page. */
	float: left;
	margin-right: 8px;
}
.clearfloat { /* this class can be placed on a <br /> or empty div as the final element following the last floated div (within the #container) if the #footer is removed or taken out of the #container */
	clear:both;
	height:0;
	font-size: 1px;
	line-height: 0px;
}
.footer1 {	padding: 10px 0;
	background-color: #6F7D94;
	background-image: url(footer.png);
	font-family: "Comic Sans MS", cursive;
	text-decoration: blink;
}
.container .content #div1 {
	font-size: xx-large;
}

.ddd {
	font-family: "Comic Sans MS", cursive;
	font-size: 12px;
	color: #CCC;
	background-color: #111;
	font-weight: lighter;
	font-style: italic;
}
#Logo {
	font-family: Chiller, "Most Wazted", "Viner Hand ITC", "Harlow Solid Italic";
	background-color: #111;
}
-->
</style><script  type="text/javascript">
    function validateForm()
{
var x=document.forms["f1"]["image1"].value
if (x==null || x=="")
  {
  alert("Select the image to be processed dumbass");
  return false;
  }
  else
      if(x.indexOf(".jpg")==-1)
          {
              alert("abe bot select image files only(eg: jpeg)");
              return false;
          }
}

</script>

    </head>
     <body>
       <div class="container">
  <div class="header"><img src="Untitled-1.png" width="467" height="84" alt="Logo.mF" /></div>
 <div class="ddd">
        <p>IP: <%System.out.println(request.getRemoteAddr());out.println(request.getRemoteAddr());%><br>Port: <%System.out.println(request.getRemoteAddr());out.println(request.getLocalPort());%><br>Protocol: <%System.out.println(request.getRemoteAddr());out.println(request.getProtocol());%></p>
        <a href="Panorama.jsp" >For Panorama click here</a>
           <form action="MainServlet" name="f1" method="post" enctype="multipart/form-data" onsubmit="return validateForm()" >
            <label>Choices
            <select name="effect" id="1">
              <option>Sobel</option>
              <option>Negative</option>
             <option>Blur</option>
             <option>Mirror</option>
         </select>
         </label>
            <input type="file" name="image1"/><br />
            <input type="submit" name="Upload" value="Upload"/>
        </form>
        </div>
  <div class="footer"><!-- end .footer -->   <span class="footer1"><img src="names1.png" width="173" height="42" alt="nishikant" /></span></div>
  <!-- end .container --></div>
     </body>
</html>
