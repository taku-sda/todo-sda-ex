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
      document.body.clientHeight
    );
    
    /* ページ最下部の位置を取得 */
    var bottom = scrollHeight - window.innerHeight; 
    
    /* ページ最下部より30px上部に到達でボトムメニューを非表示にする */
    if (bottom - 30 <= $(window).scrollTop()) {
      bottommenu.fadeOut();
    } else {
      bottommenu.fadeIn();
    }
  });
});