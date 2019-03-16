var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var Book = require('../models/Book.js');

/* GET ALL BOOKS */
router.get('/', (req, res, next) => {
  Book.find((err, books) => {
    if (err) return next(err);
    res.json(books);
  })
});

/* GET SINGLE BOOK BY ID */
router.get('/:id', (req, res, next) => {
  Book.findById(req.params.id, (err, post) => {
    if (err) return next(err);
    res.json(post);
  })
});

/* SAVE BOOK */
router.post('/', (req, res, next) => {
  Book.create(req.body, (err, post) => {
    if (err) return next(err);
    res.json(post);
  })
});

/* UPDATE BOOK */
router.put('/:id', (req, res, next) => {
  Book.findByIdAndUpdate(req.params.id, req.body, (err, post) => {
    if (err) return next(err);
    res.json(post);
  });
});

/* DELETE BOOK */
router.delete('/:id', (req, res, next) => {
  Book.findByIdAndRemove(req.params.id, req.body, (err, post) => {
    if (err) return next(err);
    res.json(post);
  });
});

module.exports = router;

