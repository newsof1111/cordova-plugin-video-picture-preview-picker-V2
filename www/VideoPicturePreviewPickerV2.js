

var VideoPicturePreviewPickerV2 = function () {};


VideoPicturePreviewPickerV2.prototype.openPicker = function (success, fail, options) {
	if (!options) 
		{
			options = {};
		}

	var params = 
		{
			limit_Select: options.limit_Select ? options.limit_Select : 5,
			Is_multiSelect: options.Is_multiSelect ? options.Is_multiSelect : false
		};

	return cordova.exec(success, fail, "VideoPicturePreviewPickerV2", "openPicker", [params]);
};



window.VideoPicturePreviewPickerV2 = new VideoPicturePreviewPickerV2();
