const db = require('../config/db');
const item = require('../models/item');

const handleError = (res, err, origin = '') => {
    console.error(`Error in ${origin}:`, err);
    res.status(500).json({ success: false, message: 'Server error' });
}


class itemController {
    // Get all items
    static async getAllItems(req, res) {
        try {
            const items = await db.queryDB(item.getAllItemsQuery);
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
            handleError(res, error, "getAllItems");
        }
    }

    // Get item by ID
    static async getItemById(req, res) {
        try {

            const id = parseInt(req.query.id);
            const items = await db.queryDB(item.getItemByIdQuery, [id]);

            if (items.length === 0) {
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
            handleError(res, error, "getItemById");
        }
    }

    // Create new item
    static async createItem(req, res) {
        try {
            const { title, subtitle, content, tags } = req.body;
            const vettedDate = req.body.vettedDate || new Date();
            const uniqueTags = [...new Set(tags)]; 

            const newItem = await db.queryDB(
                item.createItemQuery,
                [title, subtitle, vettedDate, content, uniqueTags]
            );

            res.status(201).json({
                success: true,
                data: newItem
            });
        } catch (error) {
            handleError(res, error, "createItem");
        }
    }

    // Update item by id
    static async updateItemById(req, res) {
        try {
            const { title, subtitle, content, tags } = req.body;
            const vettedDate = req.body.vettedDate || new Date();
            const uniqueTags = [...new Set(tags)];
            let id = parseInt(req.query.id); 

            let updateItem = await db.queryDB(item.getItemByIdQuery, [id]);

            updateItem.title = title ? title : updateItem.title;
            updateItem.subtitle = subtitle ? subtitle : updateItem.subtitle;
            updateItem.vettedDate = vettedDate ? vettedDate : updateItem.vettedDate;
            updateItem.content = content ? content : updateItem.content;
            updateItem.tags = uniqueTags ? uniqueTags : updateItem.tags;

            const update = await db.queryDB(
                item.updateItemByIdQuery,
                [updateItem.title, updateItem.subtitle, updateItem.vettedDate, updateItem.content, updateItem.tags, id]
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
            handleError(res, error, "updateItemById");
        }
    }

    // Delete item by id
    static async deleteItemById(req, res) {
        try {
            const id = parseInt(req.query.id);

            const del = await db.queryDB(
                item.deleteItemByIdQuery,
                [id]  
            );

            if (del.length === 0) {
                return res.status(404).json({
                    success: false,
                    message: 'Item not found'
                });
            }

            res.status(204).send(); // No content to return
        } catch (error) {
            handleError(res, error, "deleteItemById");
        }
    }

    // Get items by tag
    static async getItemsByTags(req, res) {
        try {

            let tags = req.query.tags;
            if (typeof tags === 'string') {
                tags = [tags];
            }

            const items = await db.queryDB(
                item.getItemsByTagsQuery,
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
            handleError(res,error, "getItemByTags");
        }
    }
}

module.exports = itemController;
