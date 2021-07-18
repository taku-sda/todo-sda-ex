/* ページトップへのスクロールボタンの動作 */
$(function(){
  var pagetop = $('#page_top');
  pagetop.hide();
  $(window).scroll(function () {
     if ($(this).scrollTop() > 400) {
          pagetop.fadeIn();
     } else {
          pagetop.fadeOut();
     }
  });
  pagetop.click(function () {
     $('body, html').animate({ scrollTop: 0 }, 500);
     return false;
  });
});

/* ボトムメニューの動作 */
$(function(){
  var bottommenu = $('#bottom_menu');
  $(window).scroll(function () {
  
    /* ブラウザの差異を考慮したページ全体の高さを取得 */
    var scrollHeight = Math.max(
      document.body.scrollHeight, document.documentElement.scrollHeight,
      document.body.offsetHeight, document.documentElement.offsetHeight,
      document.body.clientHeight, document.documentElement.clientHeight
    );
    
    /* ページ最下部の位置を取得 */
    var bottom = scrollHeight - window.innerHeight; 
    
    /* スクロール量を取得 */
    scrollTop = window.pageYOffset || document.documentElement.scrollTop
    
    /* ページ最下部より70px上部に到達していない場合はボトムメニューを表示、到達していない場合は非表示にする */
    if (bottom - 70 > scrollTop) {
      bottommenu.fadeIn();
    } else {
      bottommenu.fadeOut();
    }
    
    console.log("$(document).innerHeight() = " + $(document).innerHeight());
    console.log("document.body.scrollHeight = " + document.body.scrollHeight);
    console.log("document.documentElement.scrollHeight = " + document.documentElement.scrollHeight);
    console.log("document.body.offsetHeight = " + document.body.offsetHeight);
    console.log("document.documentElement.offsetHeight = " + document.documentElement.offsetHeight);
    console.log("document.body.clientHeight = " + document.body.clientHeight);
    console.log("document.documentElement.clientHeight = " + document.documentElement.clientHeight);
    console.log("window.innerHeight = " + window.innerHeight);
    console.log("$(window).innerHeight() = " + $(window).innerHeight());
    console.log("$(window).height() = " + $(window).height());
    console.log("window.pageYOffset = " + window.pageYOffset);
    console.log("document.documentElement.scrollTop = " + document.documentElement.scrollTop);
    console.log("$(window).scrollTop = " + $(window).scrollTop());
     console.log("======")
  });
});