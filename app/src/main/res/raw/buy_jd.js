function buy_jd(type, sum) {
    var sum = sum || 5;
    var type = type || 20107;
    var request_data = {
        cardDefId: "5",
        skus: [{
            skuId: type,
            quantity: sum
        }]
    };
    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'https://api.zuifuli.com/api/product/v1/card/defs/5/detail', false);
    xhr.withCredentials = true;
    xhr.send(null);
    var response = JSON.parse(xhr.response);
    if (response["code"] !== "0") {
        Android.showToast(">>>" + xhr.response + "<<<<");
    } else {
        if (response.result.skus[type - 20107].stockQuantity > 0) {
            xhr.open('POST', 'https://api.zuifuli.com/api/product/v1/card/buy', false);
            xhr.withCredentials = true;
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.send(JSON.stringify(request_data));
            Android.showToast(">>>" + xhr.response + "<<<<");
            var response = JSON.parse(xhr.response);
            if(response["code"] === "0") {
                Android.setSuccess();
            }
        } else {
            Android.showToast(">>>query result is 0<<<<");
        }
    }
}