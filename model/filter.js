"use strict";
const conn = require("../config/db");

const GetFilterProperty = (req, res, next) => {
  const query = `SELECT * FROM apartmentinfo WHERE Address LIKE '%${req.body.Address}%' AND ApartmentType LIKE '%${req.body.ApartmentType}%'
    AND RenterType LIKE '%${req.body.RenterType}%' AND Rent BETWEEN ${req.body.min} AND ${req.body.max}`;
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
module.exports = GetFilterProperty;
