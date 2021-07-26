const { check } = require("express-validator");
const reg = require("../model/register");
const login = require("../model/SignIn");
const update = require("../model/update");
const resetPassword = require("../model/resetpassword");
const insertProp = require("../model/InsertProperty");
const Properties = require("../model/GetProperties");
const UpdateProp = require("../model/UpdateProperty");
const DeleteProperty = require("../model/DeleteProperty");
const getRenterProperty = require("../model/GetRProperty");
const router = require("express").Router();

router.post(
  "/Signup",
  [
    check("email", "Please enter a valid email")
      .not()
      .isEmpty()
      .escape()
      .isEmail(),
    check("password", "Please enter a valid password")
      .notEmpty()
      .trim()
      .isLength({
        min: 6,
      }),
  ],
  reg
);

router.post(
  "/login",
  [
    check("email", "Please enter a valid email").notEmpty().escape().isEmail(),
    check("password", "Please enter a valid password")
      .notEmpty()
      .trim()
      .isLength({
        min: 6,
      }),
  ],
  login
);

router.post("/update", update);
router.post("/reset", resetPassword);
router.post("/insertprop", insertProp);

router.get("/getprop", Properties);
router.post("/updateprop", UpdateProp);
router.post("/deleteprop", DeleteProperty);
router.post("/getrenterprop", getRenterProperty);

module.exports = router;
