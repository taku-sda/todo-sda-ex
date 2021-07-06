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
     if ($(this).scrollTop() > 1200) {
          bottommenu.fadeOut();
     } else {
          bottommenu.fadeIn();
     }
  });
});