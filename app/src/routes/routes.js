const express = require('express');
const router = express.Router();
const itemController = require('../controllers/controller');

// Route definitions
// Base Route
router.get('/', itemController.getAllItems);
router.post('/', itemController.createItem);

// Id Route
router.put('/id/:id', itemController.updateItemById);
router.get('/id/:id', itemController.getItemById);
router.delete('/id/:id', itemController.deleteItemById);

// Tags Route

module.exports = router;