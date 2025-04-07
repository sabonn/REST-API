const axios = require('axios');
const { faker } = require('@faker-js/faker');

const BASE_URL = 'http://localhost:3000/api/items';
const TAGS = ['cardiology', 'oncology', 'radiology', 'internal medicine'];

const generateItem = () => ({
  title: faker.lorem.sentence(),
  subtitle: faker.lorem.sentence(),
  vettedDate: faker.date.past().toISOString().split('T')[0],
  content: faker.lorem.paragraphs(2),
  tags: faker.helpers.arrayElements(TAGS, Math.floor(Math.random() * 3) + 1)
});

// Request helpers
const createItem = async () => {
  const item = generateItem();
  return await axios.post(`${BASE_URL}/`, item);
};

const updateItem = async (id) => {
  let url = `${BASE_URL}/id`;
  if (Array.isArray(id) && id.length > 0){
    url += `?id=${id[0]}`
    for(let i=1; i < id.length; i++) {
      url += `&id=${id[i]}`;
    }
  } else {
    url += `?id=${id}`;
  }
  const item = generateItem();
  return await axios.put(url, item);
};

const deleteItem = async (id) => {
  let url = `${BASE_URL}/id`;
  if (Array.isArray(id) && id.length > 0){
    url += `?id=${id[0]}`
    for(let i=1; i < id.length; i++) {
      url += `&id=${id[i]}`;
    }
  } else {
    url += `?id=${id}`;
  }
  return await axios.delete(url);
};

const getAllItems = async () => {
  return await axios.get(`${BASE_URL}/`);
};

const getItemsByTags = async (tags) => {
  let url = `${BASE_URL}/tags`;
  if (Array.isArray(tags) && tags.length > 0) {
    url += `?tags=${tags[0]}`;
    for(let i=1; i < tags.length; i++) {
      url += `&tags=${tags[i]}`;
    }
  } else {
    url += `?tags=${tags}`;
  }
  return await axios.get(url);
};

const getItemById = async (id) => {
  let url = `${BASE_URL}/id`;
  if (Array.isArray(id) && id.length > 0){
    url += `?id=${id[0]}`
    for(let i=1; i < id.length; i++) {
      url += `&id=${id[i]}`;
    }
  } else {
    url += `?id=${id}`;
  }
  return await axios.get(url);
};

// Test Suite
describe('Item API Basic Tests', () => {

  test('Should create 100 knowledge items successfully', async () => {
    for (let i = 0; i < 100; i++) {
      const response = await createItem();
      expect(response.status).toBe(201);
      expect(response.data.success).toBe(true);
      expect(response.data.data).toHaveLength(1);
    }
  }, 60_000);

  test('Should get item with id 5', async () => {
    const response = await getItemById(5);
    expect(response.status).toBe(200);
    expect(response.data.data[0].id).toBe(5);
  }, 60_000);

  test('Should delete item with id 3', async () => {
    const response = await deleteItem(3);
    expect(response.status).toBe(204);
  }, 60_000);

  test('Should update item with id 7', async () => {
    const item = generateItem();
    const response = await updateItem(7, item);
    expect(response.status).toBe(200);
    expect(response.data.success).toBe(true);
    expect(response.data.data).toHaveLength(1);
  }, 60_000);

  test('Should retrieve all items', async () => {
    const response = await getAllItems();
    expect(response.status).toBe(200);
    expect(response.data.success).toBe(true);
    expect(Array.isArray(response.data.data)).toBe(true);
  }, 60_000);

  test('Should retrieve items by tags', async () => {
    const tags = ['oncology', 'radiology'];
    const response = await getItemsByTags(tags);
    expect(response.status).toBe(200);
    expect(response.data.success).toBe(true);
  }, 60_000);
});
