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

const createItem = async () => {
  const item = generateItem();
  const response = await axios.post(`${BASE_URL}/api/items/`, item);
  return response;
}

const updateItem = async (id) => {
  const item = generateItem()
  const response = await axios.put(`${BASE_URL}/api/items/id?id=${id}`, item);
  return response;
}

const deleteItem = async (id) => {
  const response = await axios.delete(`${BASE_URL}/api/items/id?id=${id}`);
  return response;
}

const getAllItems = async (id) => {
  const response = await axios.get(`${BASE_URL}/api/items/`)
  return response;
}

describe('Item API Integration Tests', () => {
  test('Should create 100 knowledge items successfully', async () => {
    for (let i = 0; i < 100; i++) {
      const response = await createItem();
      expect(response.status).toBe(201);
      expect(response.data.success).toBe(true);
      expect(response.data.data).toHaveLength(1);
    }
  }, 60_000);

  test('Should delete id 3', async () => {
    const response = await deleteItem(3);
    expect(response.status).toBe(204);
  }, 60_000);

  test('Should update id 7', async () => {
    const response = await updateItem(7);
    expect(response.status).toBe(200);
    expect(response.data.success).toBe(true);
    expect(response.data.data).toHaveLength(1); 
  }, 60_000);
});
