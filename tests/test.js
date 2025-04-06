const axios = require('axios');
const { faker } = require('@faker-js/faker');

const BASE_URL = 'http://localhost:3000';
const TAGS = ['cardiology', 'oncology', 'radiology', 'internal medicine'];

const generateItem = () => ({
    title: faker.lorem.sentence(),
    subtitle: faker.lorem.sentence(),
    vettedDate: faker.date.past().toISOString().split('T')[0],
    content: faker.lorem.paragraphs(2),
    tags: faker.helpers.arrayElements(TAGS, Math.floor(Math.random() * 3) + 1)
});
  
const seed = async () => {
    console.log('Starting seeding...');
    for (let i = 0; i < 100; i++) {
      const item = generateItem();
      try {
        const res = await axios.post(`${BASE_URL}/api/items/`, item);
        console.log(`Inserted item ${i + 1}:\n`, res.data.data.id ?? '✔');
      } catch (err) {
        console.error(`Failed to insert item ${i + 1}:`, err.response?.data || err.message);
      }
    }
    console.log('Done seeding.');
  };
  
seed();