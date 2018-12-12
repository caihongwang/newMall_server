wx.uploadFile({
    url: 'https://' + app.globalData.host + '/api/?sign=' + sign,
    filePath: tempFilePaths[0],
    name: 'upload',
    header: {
        "content-type": "multipart/form-data",
        "content-type": "application/x-www-form-urlencoded"
    },
    formData: formData,
    success: function (res) {
        var $data = JSON.parse(res.data);
        if (typeof ($data.data) != "undefined" && $data.code) {
            var imgBase64 = "data:image/jpeg;base64," + $data.data;
        }
    }
})