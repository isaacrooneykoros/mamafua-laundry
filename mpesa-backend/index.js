const express = require('express');
require('dotenv').config();
const stkPushHandler = require('./stkpush');

const app = express();
app.use(express.json());

app.post('/stkpush', stkPushHandler);

app.post('/callback', (req, res) => {
  console.log("Callback received from Safaricom:", req.body);
  res.sendStatus(200);
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
