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

const createItem = async (item = null) => {
  if (item === null) {
    const sendItem = generateItem();
    return await axios.post(`${BASE_URL}/`, sendItem);  
  }
  return await axios.post(`${BASE_URL}/`, item);
};

const updateItem = async (id) => {
  let url = `${BASE_URL}/id`;
  if (Array.isArray(id) && id.length > 0) {
    url += `?id=${id[0]}`;
    for (let i = 1; i < id.length; i++) {
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
  if (Array.isArray(id) && id.length > 0) {
    url += `?id=${id[0]}`;
    for (let i = 1; i < id.length; i++) {
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
    for (let i = 1; i < tags.length; i++) {
      url += `&tags=${tags[i]}`;
    }
  } else {
    url += `?tags=${tags}`;
  }
  return await axios.get(url);
};

const getItemById = async (id) => {
  let url = `${BASE_URL}/id`;
  if (Array.isArray(id) && id.length > 0) {
    url += `?id=${id[0]}`;
    for (let i = 1; i < id.length; i++) {
      url += `&id=${id[i]}`;
    }
  } else {
    url += `?id=${id}`;
  }
  return await axios.get(url);
};

module.exports = {
  generateItem,
  createItem,
  updateItem,
  deleteItem,
  getAllItems,
  getItemsByTags,
  getItemById,
};
