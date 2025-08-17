const express = require('express');
const path = require('path');
const axios = require('axios');
const multer = require('multer');
const upload = multer({ dest: 'tmp/' });

const app = express();
const PORT = process.env.PORT || 3000;
const BACKEND = process.env.BACKEND_URL || 'http://localhost:8080';

app.use(express.static(path.join(__dirname, 'public')));
app.use(express.urlencoded({ extended: true }));

app.get('/api/books', async (req, res) => {
  try {
    const r = await axios.get(`${BACKEND}/api/books`);
    res.json(r.data);
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
});

app.post('/api/upload', upload.single('file'), async (req, res) => {
  try {
    const FormData = require('form-data');
    const fs = require('fs');
    const form = new FormData();
    form.append('title', req.body.title || 'Untitled');
    form.append('author', req.body.author || 'Unknown');
    form.append('file', fs.createReadStream(req.file.path), req.file.originalname);

    const r = await axios.post(`${BACKEND}/api/books/upload`, form, { headers: form.getHeaders() });
    fs.unlinkSync(req.file.path);
    res.redirect('/');
  } catch (e) {
    res.status(500).send(e.toString());
  }
});

app.get('/api/download/:id', async (req, res) => {
  try {
    const url = `${BACKEND}/api/books/${req.params.id}/download`;
    res.redirect(url);
  } catch (e) {
    res.status(500).send(e.toString());
  }
});

app.listen(PORT, () => console.log(`Frontend running on ${PORT}`));
