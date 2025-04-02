const db = require('../config/db');

class Item {
  // Get all items
  static async getAll() {
    try {
      const result = await db.query('SELECT * FROM items');
      return result.rows;
    } catch (error) {
      console.error('Error getting all items:', error);
      throw error;
    }
  }

  // Get item by ID
  static async getById(id) {
    try {
      const result = await db.query('SELECT * FROM items WHERE id = $1', [id]);
      return result.rows[0];
    } catch (error) {
      console.error(`Error getting item with id ${id}:`, error);
      throw error;
    }
  }

  // Create new item
  static async create(item) {
    try {
      const { title, subtitle, content, tags } = item;
      const vettedDate = item.vettedDate || new Date();
      
      const result = await db.query(
        'INSERT INTO items (title, subtitle, vetted_date, content, tags) VALUES ($1, $2, $3, $4, $5) RETURNING *',
        [title, subtitle, vettedDate, content, tags]
      );
      return result.rows[0];
    } catch (error) {
      console.error('Error creating item:', error);
      throw error;
    }
  }

  // Update item
  static async update(id, item) {
    try {
      const { title, subtitle, vettedDate, content, tags } = item;
      
      const result = await db.query(
        'UPDATE items SET title = $1, subtitle = $2, vetted_date = $3, content = $4, tags = $5 WHERE id = $6 RETURNING *',
        [title, subtitle, vettedDate, content, tags, id]
      );
      return result.rows[0];
    } catch (error) {
      console.error(`Error updating item with id ${id}:`, error);
      throw error;
    }
  }

  // Delete item
  static async delete(id) {
    try {
      const result = await db.query('DELETE FROM items WHERE id = $1 RETURNING *', [id]);
      return result.rows[0];
    } catch (error) {
      console.error(`Error deleting item with id ${id}:`, error);
      throw error;
    }
  }

  // Initialize database table
  static async initTable() {
    const query = `
      CREATE TABLE IF NOT EXISTS items (
        id SERIAL PRIMARY KEY,
        title VARCHAR(200) NOT NULL,
        subtitle VARCHAR(500),
        vetted_date DATE DEFAULT CURRENT_DATE,
        content TEXT,
        tags TEXT[] DEFAULT '{}',
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      )
    `;
    
    try {
      await db.query(query);
      console.log('Items table is ready');
    } catch (error) {
      console.error('Error creating items table:', error);
      throw error;
    }
  }
}

module.exports = Item;