const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const db = require('./db');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(bodyParser.json());

// Endpoint: POST /api/confirm-delivery
app.post('/api/confirm-delivery', async (req, res) => {
    const { shipmentId, otp, agentName } = req.body;

    if (!shipmentId || !otp || !agentName) {
        return res.status(400).send('Missing Data');
    }

    try {
        // 1. Check if shipment exists
        const [rows] = await db.execute('SELECT * FROM shipments WHERE shipment_id = ?', [shipmentId]);

        if (rows.length === 0) {
            return res.status(404).send('Shipment ID not found');
        }

        const shipment = rows[0];

        // 3. Check if already delivered
        if (shipment.status === 'Delivered') {
            return res.status(400).send('Already Delivered');
        }

        // 2. Verify OTP
        if (shipment.otp_code !== otp) {
            return res.status(401).send('Invalid OTP');
        }

        // 4. Update status
        await db.execute(
            'UPDATE shipments SET status = ?, delivered_at = NOW(), delivered_by = ? WHERE id = ?',
            ['Delivered', agentName, shipment.id]
        );

        res.status(200).send('Delivery Confirmed');

    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }
});

// Endpoint: POST /api/request-otp
app.post('/api/request-otp', async (req, res) => {
    const { shipmentId } = req.body;

    if (!shipmentId) {
        return res.status(400).send('Missing Shipment ID');
    }

    try {
        const [rows] = await db.execute('SELECT * FROM shipments WHERE shipment_id = ?', [shipmentId]);

        if (rows.length === 0) {
            return res.status(404).send('Shipment ID not found');
        }

        const shipment = rows[0];
        // In a real app, we would send SMS/Email here.
        // For simulation, we just log it.
        console.log(`[OTP REQUEST] OTP for Shipment ${shipmentId} is: ${shipment.otp_code}`);

        res.status(200).send('OTP Sent to Customer');

    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }
});

app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
