const db = require('../config/db');

class Item {
  static async queryDB(query, params = []) {
    try {
      const result = await db.query(query, params);
      return result.rows;
    } catch (error) {
      console.log('Error in queryDB: ', error);
      throw error;
    }
  }

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