const { createItem } = require('./client');
require('dotenv').config();

const seed = async () => {
    for(let i = 0; i < process.env.SEED_AMOUNT; i++){
        await createItem();
    }
}

seed();