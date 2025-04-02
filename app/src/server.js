const express = require('express');
const dotenv = require('dotenv');
const item = require('./models/item');
const itemRoutes = require('./routes/routes');

// Load environment variables
dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware for parsing JSON
app.use(express.json());

// Initialize database table
(async () => {
  try {
    await item.initTable();
  } catch (err) {
    console.error('Error initializing database:', err);
    process.exit(1);
  }
})();

// Routes
app.use('/api/items', itemRoutes);

// Root route
app.get('/', (req, res) => {
  res.json({
    message: 'Welcome to the REST API with CRUD operations',
    endpoints: {
      getAllItems: 'GET /api/items',
      getItemById: 'GET /api/items/:id',
      createItem: 'POST /api/items'
    }
  });
});

// Start server
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});