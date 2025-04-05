const item = require('../models/item');

class itemController {
    // Get all items
    static async getAllItems(req, res) {
        try {
            const items = await item.queryDB('SELECT * FROM items');
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
            const items = await item.queryDB('SELECT * FROM items WHERE id = $1', [id]);
      
            if (!items) {
                return res.status(404).json({
                    success: false,
                    message: `Item with id ${id} not found`
                });
            }
      
            res.status(200).json({
                success: true,
                data: items
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
            const { title, subtitle, content, tags} = req.body;
            const vettedDate = req.body.vettedDate || new Date()
            const newItem = await item.queryDB(
                'INSERT INTO items (title, subtitle, vetted_date, content, tags) VALUES ($1, $2, $3, $4, $5) RETURNING *',
                [title, subtitle, vettedDate, content, tags]
            );
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

    // Get items by tag
    static async getItemsByTags(req, res) {
        try{

        } catch (error) {
            console.error('Error in getItemsByTags:', error);
        }
    }
}

module.exports = itemController;