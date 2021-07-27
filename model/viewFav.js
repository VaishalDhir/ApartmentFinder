const conn = require("../config/db");

const viewFav = (req, res, next) => {
  conn.query(
    "SELECT * FROM favouriteapt WHERE apartmentId=" + req.body.apartmentId,
    function (err, data, fields) {
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
    }
  );
};
module.exports = viewFav;
