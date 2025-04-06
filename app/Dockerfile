# Use official Node.js image
FROM node:20

# Set working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the app
COPY . .

# Expose the app port (change if needed)
EXPOSE 3000

# Start the app
CMD ["npm", "run", "dev"]
