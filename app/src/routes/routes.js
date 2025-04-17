const express = require('express');
const router = express.Router();
const itemController = require('../controllers/controller');
const { validate, validateId, updateItemSchema, createItemSchema }  = require('../validation/validation');

// Route definitions
// Base Route
router.get('/', itemController.getAllItems);
router.post('/', validate(createItemSchema, "body"),itemController.createItem);

// Id Route
router.put('/id', validate(validateId,"query"), validate(updateItemSchema, "body"), itemController.updateItemById);
router.get('/id', validate(validateId,"query"), itemController.getItemById);
router.delete('/id', validate(validateId,"query"), itemController.deleteItemById);

// Tags Route
router.get('/tags', itemController.getItemsByTags);

module.exports = router;