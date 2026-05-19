const express = require('express');
const cookieParser = require('cookie-parser');
const path = require('path');

const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

const SESSION_TOKEN = 'a1b2c3d4e5f6';
const API_KEY = 'demo-api-key';
const CREDENTIALS = { email: 'demo@example.com', password: 'changeme' };

const products = [
  { id: 1, name: 'Sample Widget', sku: 'WGT-001', price: '9.99', status: 'active' },
  { id: 2, name: 'Starter Kit', sku: 'KIT-001', price: '49.99', status: 'active' },
];
let nextId = 3;

function requireAuth(req, res, next) {
  if (req.cookies.session === SESSION_TOKEN) return next();
  if (req.headers['api-key'] === API_KEY) return next();
  res.status(401).json({ error: 'Unauthorized' });
}

// ── Auth ──────────────────────────────────────────────────────────────────────

app.post('/api/auth/login', (req, res) => {
  const { email, password } = req.body;
  if (email === CREDENTIALS.email && password === CREDENTIALS.password) {
    res.cookie('session', SESSION_TOKEN, { httpOnly: true });
    const isForm = req.headers['content-type']?.includes('application/x-www-form-urlencoded');
    return isForm ? res.redirect('/products') : res.status(200).json({ message: 'success' });
  }
  res.status(401).json({ error: 'Invalid credentials' });
});

app.get('/api/auth/token', (req, res) => {
  res.json({ name: 'session', value: SESSION_TOKEN });
});

// ── Products API ──────────────────────────────────────────────────────────────

app.get('/api/products', requireAuth, (req, res) => {
  res.json(products);
});

app.post('/api/products', requireAuth, (req, res) => {
  const { name, sku, price } = req.body;
  const product = { id: nextId++, name, sku, price, status: 'active' };
  products.push(product);
  res.status(201).json(product);
});

// ── Pages ─────────────────────────────────────────────────────────────────────

app.get('/products', (req, res) => {
  if (req.cookies.session !== SESSION_TOKEN) return res.redirect('/');
  res.sendFile(path.join(__dirname, 'public', 'products.html'));
});

app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'login.html'));
});

app.listen(3000, () => console.log('Mock app running on http://localhost:3000'));
