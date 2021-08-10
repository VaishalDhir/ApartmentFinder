"use strict";
const conn = require("../config/db");

const GetEmailId = (req, res, next) => {
  const query = `SELECT us.email FROM apartmentinfo AS ap Inner JOIN user as us WHERE ap.userId=us.userid AND ap.apartmentId=${req.body.apartmentId}`;
  console.log(query);
  conn.query(query, req.body.userId, function (err, data, fields) {
    if (err) {
      console.log(err);
      res.json({
        status: false,
        data,
        err: err.sqlMessage,
      });
    } else {
      console.log(data.length);
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
  });
};
module.exports = GetEmailId;
