const Joi = require('joi');

const createItemSchema = Joi.object({
  title: Joi.string().min(1).required(),
  subtitle: Joi.string().min(1).required(),
  content: Joi.string().required(),
  tags: Joi.array().items(Joi.string()).min(1).required(),
  vettedDate: Joi.date().optional()
});

const updateItemSchema = Joi.object({
  title: Joi.string().min(1).optional(),
  subtitle: Joi.string().min(1).optional(),
  content: Joi.string().optional(),
  tags: Joi.array().items(Joi.string()).optional(),
  vettedDate: Joi.date().optional()
});

function validate(schema) {
    return (req, res, next) => {
      const { error } = schema.validate(req.body);
      if (error) {
        return res.status(400).json({
            success: false,
            message: error.details[0].message
        });
    }
    next();
    };
};

module.exports = {
  createItemSchema,
  updateItemSchema,
  validate
};
