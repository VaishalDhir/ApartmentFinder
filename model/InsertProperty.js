var fs = require("fs");
const { validationResult } = require("express-validator");
const conn = require("../config/db");
const InsertProperty = async (req, res, next) => {
  const img1 = Buffer.from(req.body.img1);
  const img2 = Buffer.from(req.body.img2);

  const img3 = Buffer.from(req.body.img3);

  var apartmentinfo = {
    userId: req.body.userId,
    Address: req.body.Address,
    ApartmentType: req.body.ApartmentType,
    Rent: req.body.Rent,
    Size: req.body.Size,
    Facility: req.body.Facility,
    Description: req.body.Description,
    RenterType: req.body.RenterType,
    img1: img1,
    img2: img2,
    img3: img3,
    latitude: req.body.latitude,
    longitude: req.body.longitude,
  };

  try {
    console.log("hello world");
    await conn.query(
      "INSERT INTO apartmentinfo SET ?",
      apartmentinfo,
      function (error, results, fields) {
        if (error) {
          // console.log(error);
          res.json({
            status: false,
            message: "there are some errors with query!!!",
            error: error.sqlMessage,
          });
        } else {
          res.json({
            status: true,
            message: "user registered sucessfully!! Great Work",
            error: "",
          });
        }
      }
    );
  } catch (err) {
    next(err);
    res.json({ status: 412, error: err.array() });
  }
};

function base64_encode(file) {
  // read binary data
  console.log(file);
  var bitmap = fs.readFileSync(file);
  // convert binary data to base64 encoded string
  return new Buffer(bitmap);
}
module.exports = InsertProperty;
