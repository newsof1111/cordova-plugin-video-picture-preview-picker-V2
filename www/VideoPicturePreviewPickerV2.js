
var VideoPicturePreviewPickerV2 = function () {};


VideoPicturePreviewPickerV2.prototype.openPicker = function (success, fail, options) {
	if (!options) 
		{
			options = {};
		}

	var params = 
		{
			picture_selector: options.picture_selector ? options.picture_selector : true,
			video_selector: options.video_selector ? options.video_selector : true,
			display_video_time: options.display_video_time ? options.display_video_time : true,
			display_preview: options.display_preview ? options.display_preview : true,
			limit_Select: options.limit_Select ? options.limit_Select : 5,
			Is_multiSelect: options.Is_multiSelect ? options.Is_multiSelect : true
		};

	return cordova.exec(success, fail, "VideoPicturePreviewPickerV2", "openPicker", [params]);
};



window.VideoPicturePreviewPickerV2 = new VideoPicturePreviewPickerV2();
