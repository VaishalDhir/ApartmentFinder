const { check } = require("express-validator");
const reg = require("../model/register");
//const login = require("../model/SignIn");
//const products = require("../model/products");
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

// router.post(
//   "/login",
//   [
//     check("email", "Please enter a valid email").notEmpty().escape().isEmail(),
//     check("password", "Please enter a valid password")
//       .notEmpty()
//       .trim()
//       .isLength({
//         min: 6,
//       }),
//   ],
//   login
// );
//router.post("/getProduct", products);
module.exports = router;
