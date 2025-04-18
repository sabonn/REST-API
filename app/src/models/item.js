const db = require('../config/db');

class Item { 

  // Item queries
  static getAllItemsQuery = 'SELECT * FROM items';
  static getItemByIdQuery = 'SELECT * FROM items WHERE id = $1)';
  static createItemQuery = 'INSERT INTO items (title, subtitle, vetted_date, content, tags) VALUES ($1, $2, $3, $4, $5) RETURNING *';
  static updateItemByIdQuery = 'UPDATE items SET title = $1, subtitle = $2, vetted_date = $3, content = $4, tags = $5 WHERE id = $6 RETURNING *';
  static deleteItemByIdQuery = 'DELETE FROM items WHERE id = $1 RETURNING *';
  static getItemsByTagsQuery = 'SELECT * FROM items WHERE tags @> $1';

  // Only keep special logic here
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
    const createGinIndexQuery = `
      CREATE INDEX IF NOT EXISTS idx_items_tags ON items USING GIN (tags)
    `;
    try {
      await db.queryDB(query);
      await db.queryDB(createGinIndexQuery);
      console.log('Items table is ready');
    } catch (error) {
      console.error('Error creating items table:', error);
      throw error;
    }
  }
}

module.exports = Item;