
import experss from "express";
import { webLogin, webSignUp, addProduct, deleteProduct, editProduct, getProducts, getUserById, getCooradinatesByAddress as getCoordinatesByAddress, getOrdersLocationBySupermarketId } from "../controllers/webController";
import { webSignUpValidation, loginValidation, addProductValidation, editProductValidation, deleteProductValidation } from "../middleware/utils";

var router = experss.Router();

router.post("/signup", webSignUpValidation, webSignUp);
router.post("/login", loginValidation, webLogin);
router.post("/api/addproduct", addProductValidation, addProduct);
router.put("/api/editproduct", editProductValidation, editProduct);
router.delete("/api/deleteproduct", deleteProductValidation, deleteProduct);
router.get("/api/getproducts", getProducts);
router.get('/api/get-user/:userId', getUserById);
router.get('/api/get-coordinates', getCoordinatesByAddress);
router.get('/api/orders-locations', getOrdersLocationBySupermarketId);

export default router;