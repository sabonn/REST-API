const { faker } = require('@faker-js/faker');

const {
  generateItem,
  createItem,
  updateItem,
  deleteItem,
  getAllItems,
  getItemsByTags,
  getItemById,
} = require('./client');


// Test Suite
describe('Item API Basic Tests', () => {

  test('Should create 50 knowledge items successfully', async () => {
    for (let i = 0; i < 50; i++) {
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

describe('Item API Advanced Tests', () => {

  test('Should perform full CRUD lifecycle on a single item', async () => {
    const createRes = await createItem();
    expect(createRes.status).toBe(201);
    const itemId = createRes.data.data[0].id;

    const updatedRes = await updateItem(itemId);
    expect(updatedRes.status).toBe(200);
    expect(updatedRes.data.success).toBe(true);

    const retrieveRes = await getItemById(itemId);
    expect(retrieveRes.status).toBe(200);
    expect(retrieveRes.data.data[0].id).toBe(itemId);

    const deleteRes = await deleteItem(itemId);
    expect(deleteRes.status).toBe(204);

    try {
      await getItemById(itemId);
    } catch (err) {
      expect(err.response.status).toBe(404);
    }
  }, 60_000);

  test('All returned items should contain the requested tag(s)', async () => {
    const tag = 'oncology';
    const res = await getItemsByTags([tag]);
    expect(res.status).toBe(200);
    for (const item of res.data.data) {
      expect(item.tags).toContain(tag);
    }
  }, 60_000);

  test('Should bulk create and bulk delete items', async () => {
    const ids = [];

    for (let i = 0; i < 5; i++) {
      const res = await createItem();
      expect(res.status).toBe(201);
      ids.push(res.data.data[0].id);
    }

    const deleteRes = await deleteItem(ids);
    expect(deleteRes.status).toBe(204);

    for (const id of ids) {
      try {
        await getItemById(id);
      } catch (err) {
        expect(err.response.status).toBe(404);
      }
    }
  }, 60_000);

  test('Should fail to create item with missing required fields', async () => {
    const invalidItem = {
      subtitle: faker.lorem.sentence(),
      vettedDate: faker.date.past().toISOString().split('T')[0],
      content: faker.lorem.paragraphs(2),
      tags: []
    };

    try {
      await createItem(invalidItem);
    } catch (err) {
      expect(err.response.status).toBe(400);
    }
  }, 60_000);

  test('Should not store duplicate tags on item', async () => {
    const item = generateItem();
    item.tags = ['oncology', 'oncology'];

    const res = await createItem(item);
    expect(res.status).toBe(201);
    const created = res.data.data[0];
    const uniqueTags = new Set(created.tags);

    expect(uniqueTags.size).toBe(created.tags.length);
  }, 60_000);

});