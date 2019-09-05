
import experss from "express";
import { appFilter, appLogin, appSignUp, appMetadata ,appOrder,appMultipleFilter} from "../controllers/applicationController";
import { appSignUpValidation , loginValidation,filterValidation,orderValidation,multipleFilterValidation } from "../middleware/utils";

var router = experss.Router();

router.post("/signup", appSignUpValidation, appSignUp);
router.post("/login", loginValidation, appLogin);
router.get("/api/metadata", appMetadata);
router.post("/api/filter",filterValidation, appFilter);
router.post("/api/order", orderValidation, appOrder);

export default router;