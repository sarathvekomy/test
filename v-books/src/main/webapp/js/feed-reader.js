/** 
 * VbooksFeedReader Plug-in 
 * 
 */
jQuery.extend(jQuery.easing, {
  easeOutCubic : function(x, t, b, c, d) {
    return c * ((t = t / d - 1) * t * t + 1) + b;
  }
});

// Adding Defaults To Bubble. 
function VbooksFeedReader(cfg) {
  defaults = {name: 'Feed Reader', id: 'Feed Reader'}, this.init(cfg, this);
}

// Instantiating Bubble.
VbooksFeedReader.prototype.init = function(cfg, container) {
  var me = this;
  $('.page-content').append(me.html(cfg));
  opts = $.extend({}, me.defaults, cfg);
  opts.container = $('#vbooks-feed-reader-' + opts.name);
  thisContainer = opts.container;
  thisContainer.children().addClass(thisContainer.attr('id'));
  $(thisContainer.find('.'+thisContainer.attr('id')+':first')).bind({click : function() {
	var containerObj = $(this).parent();
      if (containerObj.children().last().hasClass('open')) {
        me.hideNotification({container: containerObj});
      } else {
        me.showNotification({container: containerObj});
      }
      containerObj.children().toggleClass('open');
    }
  });
  
  setTimeout(function() {
    var bitHeight = $(thisContainer.children().last()).height();
    thisContainer.animate({
      bottom : '-' + bitHeight - 30 + 'px'
    }, 50);
  }, 100);
};

// Destroying Bubble.
VbooksFeedReader.prototype.delete1 = function(userName) {
	$('.vbooks-feed-reader-' + userName).remove();
};

// To Show Notification Bubble.
VbooksFeedReader.prototype.showNotification = function(cfg) {
  var me = this;
  var thisContainer = cfg.container;
  thisContainer.stop();
  thisContainer.animate({
    bottom : '0px'
  }, {
    duration : 400,
    easing : "easeOutCubic"
  });
};

//To Hide Notification Bubble.
VbooksFeedReader.prototype.hideNotification = function(cfg) {
  var me = this;
  var thisContainer = cfg.container;
  var bitHeight = $(thisContainer.children().last()).height();
  var thisContainer = cfg.container;
  thisContainer.stop();
  thisContainer.animate({
    bottom : '-' + bitHeight - 30 + 'px'
  }, 200, function() {
  });
};

// Preparing Notification Bubble.
VbooksFeedReader.prototype.html = function(cfg) {
  var me = this, html = '';
  html += '<div id="vbooks-feed-reader-' + cfg.name + '" class="notification-block" style="width:204px !important; float:left;">';
  html += '<a href="javascript:void(0)" class="notification-header"><span class="notification-header-text">' + cfg.name + '</span></a>';
  html += '<div class="notifcation-content">';
  html += '</div>';
  html += '</div>';
  return html;
};

// Writing Content to Bubble.
VbooksFeedReader.prototype.writeContent = function(feedName, cfg) {
  thisContainer = $('#vbooks-feed-reader-' + feedName);
  thisContainer.children().last().prepend('<span class="notifcation-note roundTop roundBottom">'+cfg+'</span>');
  $(thisContainer.children().last()).animate({
    scrollTop : 0
  }, 'slow');
};
