const conn = require("../config/db");

const GetProperties = (req, res, next) => {
  let sql = `SELECT * FROM apartmentinfo`;
  conn.query(sql, function (err, data, fields) {
    if (err) {
      res.json({
        status: false,
        data,
        err: err.sqlMessage,
      });
    } else {
      res.json({
        status: true,
        data,
        err: "",
      });
    }
  });
};

module.exports = GetProperties;
