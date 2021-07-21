const conn = require("../config/db");

const DeleteProperty = (req, res, next) => {
  let sql =
    `DELETE FROM apartmentinfo WHERE apartmentId=` + req.body.apartmentId;
  conn.query(sql, function (err, data, fields) {
    if (err) {
      res.json({
        status: false,
        message: "there is some error in query",
        err: err.sqlMessage,
      });
    } else {
      res.json({
        status: true,
        message: "Deleted Successfully",
        err: "",
      });
    }
  });
};

module.exports = GetProperties;
