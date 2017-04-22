#cordova-plugin-video-picture-preview-picker-V2
===================

Cordova  plugin allows selection of  image(s)/videos(s) with a smart preview  in a cordova app - implemented for Android 4.0 and above.

 here: [Demo](http://80.241.214.164/Cordovapicker/preview.html).
## OUTPUT
![](http://80.241.214.164/Cordovapicker/output.png)

## Installing the plugin

The plugin conforms to the Cordova plugin specification, it can be installed
using the Cordova / Phonegap command line interface.

    phonegap plugin add https://github.com/newsof1111/cordova-plugin-video-picture-preview-picker-V2.git

    cordova plugin add https://github.com/newsof1111/cordova-plugin-video-picture-preview-picker-V2.git


## Using the plugin

The plugin creates the object `window.VideoPicturePreviewPickerV2` with the method `openPicker(success, fail, options)` 

###Example1 - Get Full Size Images (all default options):
```javascript
window.VideoPicturePreviewPickerV2.openPicker(
	function(results) {
			console.log(results);
	}, function (error) {
		console.log(error);
	}
);
```

###Example2 - set as a multiSelector get at most 10 images/videos:
```javascript
window.VideoPicturePreviewPickerV2.openPicker(
	function(results) {
		console.log(results);
	}, function (error) {
		console.log(error);
	}, {
		limit_Select: 10,
		Is_multiSelect: true
	}
);

//****************OR********************** 
 options = 
    {
      limit_Select: 10,
      Is_multiSelect: true
    };
window.VideoPicturePreviewPickerV2.openPicker
(
	function(results){ console.log(results);}, 
  function (error){console.log(error);},
  options
);
```

### Options
if you want to select just ONE picture/video , just dont leave options in the function-call and it will consider the default options `openPicker(success, fail)` LIKE EXAMPLE 1
 ```javascript  
   options = 
    {
     //max images or videos to be selected.
    	limit_Select: int, //defaults to 5

    	//Is_multiSelect defines if it will select multiple images and video.
    	Is_multiSelect: boolean //defaults to false
    	
    };
```

#### FakeR

Code(FakeR) was  taken from the phonegap BarCodeScanner plugin.  This code uses the MIT license.

https://github.com/wildabeast/BarcodeScanner

## License

The MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.