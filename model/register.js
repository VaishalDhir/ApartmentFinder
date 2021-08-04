const { validationResult } = require("express-validator");
const bcrypt = require("bcryptjs");
const conn = require("../config/db");

const register = async (req, res, next) => {
  const error = validationResult(req);

  if (!error) {
    return res.json({ status: 412, error: error.array() });
  }
  try {
    const salt = await bcrypt.genSalt(10);
    // now we set user password to hashed password
    let pass = await bcrypt.hash(req.body.password, salt);
    conn.query(
      "SELECT * from user WHERE email=?",
      [req.body.email],
      function (error, results, fields) {
        if (error) {
          console.log(error);
          res.json({
            status: false,
            message: "there are some error with query",
          });
        } else if (results.length != 0) {
          res.json({
            status: false,
            message: "Email id already exists",
          });
        } else {
          let uname = req.body.email;
          if (uname.trim() !== "" && uname !== undefined) {
            conn.query(
              "INSERT INTO user (firstname,lastname,email,Contact,password,type) VALUES (?,?,?,?,?,?)",
              [
                req.body.firstname,
                req.body.lastname,
                req.body.email,
                req.body.Contact,
                pass,
                req.body.type,
              ],
              function (error, results, fields) {
                if (error) {
                  console.log(error);
                  res.json({
                    status: false,
                    message: "there are some error with query",
                  });
                } else {
                  console.log("response here");
                  res.json({
                    status: true,
                    message: "success",
                  });
                }
              }
            );
          } else {
            return res.json({ status: false, message: error.array() });
          }
        }
      }
    );
  } catch (err) {
    next(err);
  }
};
module.exports = register;
