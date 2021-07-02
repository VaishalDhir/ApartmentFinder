const { validationResult } = require("express-validator");
const bcrypt = require("bcryptjs");
const conn = require("../config/db");
const jwt = require("jsonwebtoken");

const SignIn = async (req, res, next) => {
  const error = validationResult(req);

  if (!error) {
    console.log(error);
    return res.json({ status: 412, error: error.array() });
  }
  let uname = req.body.email;
  if (uname.trim() !== "" && uname !== undefined) {
    try {
      await conn.query(
        "SELECT * from User WHERE email=?",
        [uname],
        function (error, results, fields) {
          if (error) {
            res.json({
              status: false,
              message: "there are some error with query",
            });
          } else {
            if (results.length > 0) {
              const passwordMatch = bcrypt.compare(
                req.body.password,
                results[0].password
              );
              if (!passwordMatch) {
                res.json({
                  status: 422,
                  message: "incorrect password",
                });
              } else {
                const jToken = jwt.sign(
                  { id: results[0].id },
                  "the-super-strong-secrect",
                  {
                    expiresIn: "1h",
                  }
                );
                return res.json({
                  token: jToken,
                });
              }
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
      res.json({ status: 412, error: erroerrr.array() });
    }
  } else {
    return res.json({ status: 412, error: error.array() });
  }
};
module.exports = SignIn;