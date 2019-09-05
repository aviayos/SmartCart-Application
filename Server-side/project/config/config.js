export default (function() {
    var that = {
        WEB_SIGNUP: '/web/signup',
        WEB_LOGIN: '/web/login',
        WEB_ADD_PRODUCT: '/web/api/addproduct',
        WEB_UPLOAD_IMAGE:'/web/api/upload-image',
        WEB_GET_IMAGE: '/web/api/get-image',
        WEB_GET_PRODUCTS: '/web/api/getproducts',
        WEB_GET_USER: '/web/api/get-user',
        WEB_FORGOT_PASS: '/web/forgot-password',
        WEB_UPDATE_PROD: '/web/api/editproduct',
        WEB_DELETE_PROD: '/web/api/deleteproduct',
        WEB_GET_LOCATION: '/web/api/get-coordinates',
        WEB_GET_ORDERS: '/web/api/orders-locations',
        WEB_DEETE_COOKIE: '/delete-cookie',
        DEFAULT_MAP_CENTER: [35.209344, 31.767805],
        CATEGORY_ENUM: {
            fruitsAndVeggies: 'FRUITS AND VEGS',
            dairy: 'DAIRY',
            meet: 'MEET',
            other: 'OTHER'       
        }
    }
    return that;
})();