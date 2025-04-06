const express = require('express');
const router = express.Router();
const itemController = require('../controllers/controller');

// Route definitions
// Base Route
router.get('/', itemController.getAllItems);
router.post('/', itemController.createItem);

// Id Route
router.put('/id', itemController.updateItemById);
router.get('/id', itemController.getItemById);
router.delete('/id', itemController.deleteItemById);

// Tags Route
router.get('/tags', itemController.getItemsByTags);

module.exports = router;