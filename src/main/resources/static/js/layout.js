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
    /* ページ最下部の位置を取得 */
    var bottom = $(document).innerHeight() - document.documentElement.clientHeight; 
    
    /* ページ最下部より30px上部に到達でボトムメニューを非表示にする */
    if (bottom - 30 <= $(window).scrollTop()) {
      bottommenu.fadeOut();
    } else {
      bottommenu.fadeIn();
    }
  });
});