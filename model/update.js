const { validationResult } = require("express-validator");
const bcrypt = require("bcryptjs");
const conn = require("../config/db");

const updateProfile = async (req, res, next) => {
  try {
    conn.query(
      `UPDATE user SET firstname='${req.body.firstname}',lastname='${req.body.lastname}',Contact=${req.body.Contact},type='${req.body.type}' WHERE email='${req.body.email}'`,

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
            message: "Profile Updated Successfully",
          });
        }
      }
    );
  } catch (err) {
    next(err);
  }
};
module.exports = updateProfile;
