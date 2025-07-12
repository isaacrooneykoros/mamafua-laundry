const axios = require('axios');
const moment = require('moment');
require('dotenv').config();

module.exports = async (req, res) => {
  const {
    CONSUMER_KEY,
    CONSUMER_SECRET,
    BUSINESS_SHORT_CODE,
    PASSKEY,
    CALLBACK_URL,
  } = process.env;

  const { phone, amount } = req.body;

  try {
    // 1. Get access token
    const auth = Buffer.from(`${CONSUMER_KEY}:${CONSUMER_SECRET}`).toString('base64');
    const tokenRes = await axios.get(
      'https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials',
      {
        headers: { Authorization: `Basic ${auth}` },
      }
    );
    const access_token = tokenRes.data.access_token;

    // 2. Generate password and timestamp
    const timestamp = moment().format('YYYYMMDDHHmmss');
    const password = Buffer.from(`${BUSINESS_SHORT_CODE}${PASSKEY}${timestamp}`).toString('base64');

    // 3. Build STK payload
    const payload = {
      BusinessShortCode: BUSINESS_SHORT_CODE,
      Password: password,
      Timestamp: timestamp,
      TransactionType: 'CustomerPayBillOnline',
      Amount: amount,
      PartyA: phone,
      PartyB: BUSINESS_SHORT_CODE,
      PhoneNumber: phone,
      CallBackURL: CALLBACK_URL,
      AccountReference: "MamaFua",
      TransactionDesc: "Laundry Payment"
    };

    // 4. Send STK push
    const stkRes = await axios.post(
      'https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest',
      payload,
      {
        headers: {
          Authorization: `Bearer ${access_token}`,
          'Content-Type': 'application/json'
        }
      }
    );

    res.json(stkRes.data);
  } catch (err) {
    console.error("STK Error:", err.response?.data || err.message);
    res.status(500).json({ error: 'Failed to initiate payment' });
  }
};
