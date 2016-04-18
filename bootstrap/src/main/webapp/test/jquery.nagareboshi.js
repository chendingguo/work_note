/**
 * jquery.nagareboshi
 *
 *
 * @version 1 (3/31/2016)
 * @author 头头哥
 * @requires jQuery
 *
 * @params flakeChar - 漂浮图标样式, 默认是雪花, 其它漂亮符号参考： ❥ღ❣♔♕♖♚♛♜☀☁☂☃☼☽☾♨❄❅❆★☆✦✪✫✿
 * @params minSize - 默认size最小是10
 * @params maxSize - 默认size最大是20
 * @params newOn - 出现新图标的频率，默认是500
 * @params flakeColors - 默认的图标颜色 , 默认是#FFFFFF
 * @params durationMillis - 停止加载图标的时间，默认是一直加载
 * @example $.fn.nagareboshi({ maxSize: 200, newOn: 1000 });
 */
;
(function($, window, document, undefined) {

	$.fn.nagareboshi = function(options) {

		var $flake = $('<div class="flake" />').css({
			'position' : 'absolute',
			'top' : '-50px'
		}), documentHeight = $(document).height(), documentWidth = $(document)
				.width(), defaults = {
			flakeChar : "&#10052;",
			minSize : 10,
			maxSize : 20,
			newOn : 500,
			flakeColor : [ "#ffffff" ],
			durationMillis : null
		},

		// 当给extend方法传递一个以上的参数时，它会将所有参数对象合并到第一个里。同时，如果对象中有同名属性时，合并的时候后面的会覆盖前面的。
		// 所以如果传递进来的options参数有的值，将会覆盖defaults对象里对应的值
		options = $.extend({}, defaults, options);

		$flake.html(options.flakeChar);

		var interval = setInterval(
				function() {
					var startPositionLeft = Math.random() * documentWidth - 100, startOpacity = 0.5 + Math
							.random(), sizeFlake = options.minSize
							+ Math.random() * options.maxSize, endPositionTop = documentHeight
							- defaults.maxSize - 40, endPositionLeft = startPositionLeft
							- 100 + Math.random() * 200, durationFall = documentHeight
							* 10 + Math.random() * 5000;
					$flake
							.clone()
							.appendTo('body')
							.css(
									{
										left : startPositionLeft,
										opacity : startOpacity,
										'font-size' : sizeFlake,
										color : options.flakeColor[Math
												.floor((Math.random() * options.flakeColor.length))]
									}).animate({
								top : endPositionTop,
								left : endPositionLeft,
								opacity : 0.2
							}, durationFall, 'linear', function() {
								$(this).remove()
							});
				}, options.newOn);
		console.log(options.durationMillis);
		if (options.durationMillis) {
			setTimeout(function() {
				clearInterval(interval);
			}, options.durationMillis);
		}
	};

})(jQuery, window, document);