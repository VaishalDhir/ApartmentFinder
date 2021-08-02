"use strict";
const conn = require("../config/db");

const GetRProperty = (req, res, next) => {
  conn.query(
    "SELECT * FROM apartmentinfo WHERE userid=?",
    req.body.userId,
    function (err, data, fields) {
      if (err) {
        res.json({
          status: false,
          data,
          err: err.sqlMessage,
        });
      } else {
        if (data.length === 0) {
          res.json({
            status: false,
            data,
            err: "No such Data Found",
          });
        } else
          res.json({
            status: true,
            data,
            err: "",
          });
      }
    }
  );
};
module.exports = GetRProperty;
