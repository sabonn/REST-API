const db = require('../config/db');
const joi = require('joi');

class Item { 

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
      await db.query(query);
      await db.query(createGinIndexQuery);
      console.log('Items table is ready');
    } catch (error) {
      console.error('Error creating items table:', error);
      throw error;
    }
  }
}

module.exports = Item;