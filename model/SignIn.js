const { validationResult } = require("express-validator");
const bcrypt = require("bcryptjs");
const conn = require("../config/db");
const jwt = require("jsonwebtoken");

const SignIn = async (req, res, next) => {
  const error = validationResult(req);
  // const salt = await bcrypt.genSalt(10);
  // now we set user password to hashed password
  // let pass = await bcrypt.hash(req.body.password, salt);
  if (!error) {
    console.log(error);
    return res.json({ status: 412, error: error.array() });
  }
  let uname = req.body.email;
  if (uname.trim() !== "" && uname !== undefined) {
    try {
      await conn.query(
        "SELECT * from user WHERE email=?",
        [uname],
        function (error, results, fields) {
          if (error) {
            res.json({
              status: false,
              message: "there are some error with query",
            });
          } else {
            console.log("here");
            if (results.length > 0) {
              bcrypt.compare(
                req.body.password,
                results[0].password,
                function (err, resb) {
                  if (resb) {
                    return res.json({
                      status: 404,
                      success: true,
                      message: "Login Successfully",
                      data: results,
                      //  token: jToken,
                    });
                  } else {
                    res.json({
                      status: 422,
                      success: false,
                      message: "incorrect password",
                      data: [],
                    });
                  }
                }
              );
            } else {
              res.json({
                status: 404,
                message: "Something Went Wrong",
              });
            }
          }
        }
      );
    } catch (err) {
      next(err);
      res.json({ status: 412, error: err.array() });
    }
  } else {
    return res.json({ status: 412, error: error.array() });
  }
};
module.exports = SignIn;
