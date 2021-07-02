const { validationResult } = require("express-validator");
const bcrypt = require("bcryptjs");
const conn = require("../config/db");

const updatePass = async (req, res, next) => {
  try {
    const pass = await bcrypt.hash(req.body.password, 12);

    conn.query(
      `UPDATE user SET password='${pass}' WHERE email='${req.body.email}'`,

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
            message: "password updated Successfully",
          });
        }
      }
    );
  } catch (err) {
    next(err);
  }
};
module.exports = updatePass;
