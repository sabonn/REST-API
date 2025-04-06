const express = require('express');
const router = express.Router();
const itemController = require('../controllers/controller');
const { validate, updateItemSchema, createItemSchema }  = require('../validation/validation');

// Route definitions
// Base Route
router.get('/', itemController.getAllItems);
router.post('/', validate(createItemSchema),itemController.createItem);

// Id Route
router.put('/id', validate(updateItemSchema),itemController.updateItemById);
router.get('/id', itemController.getItemById);
router.delete('/id', itemController.deleteItemById);

// Tags Route
router.get('/tags', itemController.getItemsByTags);

module.exports = router;