const express = require('express');
const router = express.Router();
const itemController = require('../controllers/controller');

// Route definitions
router.get('/', itemController.getAllItems);
router.get('/:id', itemController.getItemById);
router.post('/', itemController.createItem);

module.exports = router;