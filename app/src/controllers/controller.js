const item = require('../models/item');

class itemController {
  // Get all items
  static async getAllItems(req, res) {
    try {
      const items = await item.getAll();
      res.status(200).json({
        success: true,
        count: items.length,
        data: items
      });
    } catch (error) {
      console.error('Error in getAllItems controller:', error);
      res.status(500).json({
        success: false,
        message: 'Server error'
      });
    }
  }

  // Get item by ID
  static async getItemById(req, res) {
    try {
      const id = parseInt(req.params.id);
      const item = await item.getById(id);
      
      if (!item) {
        return res.status(404).json({
          success: false,
          message: `Item with id ${id} not found`
        });
      }
      
      res.status(200).json({
        success: true,
        data: item
      });
    } catch (error) {
      console.error('Error in getItemById controller:', error);
      res.status(500).json({
        success: false,
        message: 'Server error'
      });
    }
  }

  // Create new item
  static async createItem(req, res) {
    try {
      const newItem = await item.create(req.body);
      res.status(201).json({
        success: true,
        data: newItem
      });
    } catch (error) {
      console.error('Error in createItem controller:', error);
      res.status(500).json({
        success: false,
        message: 'Server error'
      });
    }
  }
}

module.exports = itemController;