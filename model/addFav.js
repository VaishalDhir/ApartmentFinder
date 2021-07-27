const conn = require("../config/db");

const addFav = (req, res, next) => {
  console.log(req.body.userId);
  let sql =
    `SELECT status FROM apartmentinfo WHERE apartmentId=` +
    req.body.apartmentId;
  conn.query(sql, function (err, data, fields) {
    if (err) {
      res.json({
        status: false,
        message: "there is some error in query",
        err: err.sqlMessage,
      });
    } else {
      const [{ status }] = data;
      if (status == 0) {
        let updatequery = `UPDATE apartmentinfo SET status=1 WHERE apartmentId=${req.body.apartmentId}`;
        console.log(updatequery);

        conn.query(updatequery, function (err, data, fields) {
          if (err) {
            res.json({
              status: false,
              message: "there is some error in query UPDATE",
              err: err.sqlMessage,
            });
          } else {
            if (data.length != 0) {
              conn.query(
                `INSERT INTO favouriteapt SET ?`,
                { apartmentId: req.body.apartmentId, userId: req.body.userId },
                function (err, data, fields) {
                  if (err) {
                    res.json({
                      status: false,
                      message: "there is some error in query INSERT",
                      err: err.sqlMessage,
                    });
                  } else {
                    if (data.length != 0) {
                      res.json({
                        status: true,
                        message: "Apartment Added to Your Favourite",
                        err: "",
                      });
                    } else
                      res.json({
                        status: false,
                        message: "there is Some Problem",
                        err: err.sqlMessage,
                      });
                  }
                }
              );
            } else {
              res.json({
                status: false,
                message: "there is Some Problem",
                err: err.sqlMessage,
              });
            }
          }
        });
      } else {
        conn.query(
          `UPDATE apartmentinfo SET status=0 WHERE apartmentId=${req.body.apartmentId}`,
          function (err, data, fields) {
            if (err) {
              res.json({
                status: false,
                message: "there is some error in query UPDATE2",
                err: err.sqlMessage,
              });
            } else {
              if (data.length != 0) {
                conn.query(
                  `DELETE FROM favouriteapt WHERE apartmentId=?`,
                  req.body.apartmentId,
                  function (err, data, fields) {
                    if (err) {
                      res.json({
                        status: false,
                        message: "there is some error in query DELETE",
                        err: err.sqlMessage,
                      });
                    } else {
                      if (data.length != 0) {
                        res.json({
                          status: true,
                          message: "Apartment Removed from Your Favourite",
                          err: "",
                        });
                      } else
                        res.json({
                          status: false,
                          message: "there is Some Problem",
                          err: err.sqlMessage,
                        });
                    }
                  }
                );
              } else {
                res.json({
                  status: false,
                  message: "there is Some Problem",
                  err: err.sqlMessage,
                });
              }
            }
          }
        );
      }
    }
  });
};

module.exports = addFav;
