const item = require('../models/item');

class itemController {
    // Get all items
    static async getAllItems(req, res) {
        try {
            const items = await item.queryDB('SELECT * FROM items');
            if (items.length === 0) {
                return res.status(404).json({
                    success: false,
                    message: 'No items found'
                });
            }
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
            let ids = req.query.id;
            if (typeof ids === 'string') {
                ids = [parseInt(ids)];
            } else {
                ids = ids.map(id => parseInt(id));
            
                if (ids.includes(NaN)) {
                    return res.status(400).json({
                        success: false,
                        message: 'Invalid characters in URL'
                    });
                }
            }

            const items = await item.queryDB('SELECT * FROM items WHERE id = ANY($1::int[])', [ids]);

            if (items.length === 0) {
                return res.status(404).json({
                    success: false,
                    message: `Item(s) with id(s) ${ids} not found`
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
            const { title, subtitle, content, tags } = req.body;
            const vettedDate = req.body.vettedDate || new Date();

            if (!title || !subtitle || !content || !tags) {
                return res.status(400).json({
                    success: false,
                    message: 'Missing required fields: title, subtitle, content, or tags'
                });
            }

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

    // Update item by id
    static async updateItemById(req, res) {
        try {
            const { title, subtitle, content, tags } = req.body;
            const vettedDate = req.body.vettedDate || new Date();
            let ids = req.query.id;

            if (typeof ids === 'string') {
                ids = [parseInt(ids)];
            } else {
                ids = ids.map(id => parseInt(id));
            
                if (ids.includes(NaN)) {
                    return res.status(400).json({
                        success: false,
                        message: 'Invalid characters in URL'
                    });
                }
            }

            const update = await item.queryDB(
                'UPDATE items SET title = $1, subtitle = $2, vetted_date = $3, content = $4, tags = $5 WHERE id = ANY($6::int[]) RETURNING *',
                [title, subtitle, vettedDate, content, tags, ids]
            );

            if (update.length === 0) {
                return res.status(404).json({
                    success: false,
                    message: 'Item not found'
                });
            }

            res.status(200).json({
                success: true,
                data: update
            });
        } catch (error) {
            console.error('Error in updateItemById:', error);
            res.status(500).json({
                success: false,
                message: 'Server error'
            });
        }
    }

    // Delete item by id
    static async deleteItemById(req, res) {
        try {
            let ids = req.query.id;

            if (typeof ids === 'string') {
                ids = [parseInt(ids)];
            } else {
                ids = ids.map(id => parseInt(id));
            
                if (ids.includes(NaN)) {
                    return res.status(400).json({
                        success: false,
                        message: 'Invalid characters in URL'
                    });
                }
            }

            const del = await item.queryDB(
                'DELETE FROM items WHERE id = ANY($1::int[]) RETURNING *',
                [ids]  
            );

            if (del.length === 0) {
                return res.status(404).json({
                    success: false,
                    message: 'Item not found'
                });
            }

            res.status(204).send(); // No content to return
        } catch (error) {
            console.error('Error in deleteItemById:', error);
            res.status(500).json({
                success: false,
                message: 'Server error'
            });
        }
    }

    // Get items by tag
    static async getItemsByTags(req, res) {
        try {
            let tags = req.query.tags;
            if (typeof tags === 'string') {
                tags = [tags];
            }

            // Ensure tags are an array
            if (!Array.isArray(tags) || tags.length === 0) {
                return res.status(400).json({
                    success: false,
                    message: 'At least one tag is required'
                });
            }

            const items = await item.queryDB(
                'SELECT * FROM items WHERE tags @> $1',
                [tags]
            );

            if (items.length === 0) {
                return res.status(404).json({
                    success: false,
                    message: 'No items found with the provided tags'
                });
            }

            res.status(200).json({
                success: true,
                data: items
            });
        } catch (error) {
            console.error('Error in getItemsByTags:', error);
            res.status(500).json({
                success: false,
                message: 'Server error'
            });
        }
    }
}

module.exports = itemController;
