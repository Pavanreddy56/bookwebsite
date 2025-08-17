async function loadBooks() {
  const res = await fetch('/api/books');
  const books = await res.json();
  const tbody = document.querySelector('#booksTable tbody');
  tbody.innerHTML = '';
  books.forEach(b => {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td>${b.id}</td><td>${b.title}</td><td>${b.author}</td><td>${b.uploadedAt || ''}</td>
      <td><a href="/api/download/${b.id}">Download</a></td>`;
    tbody.appendChild(tr);
  });
}
document.addEventListener('DOMContentLoaded', loadBooks);
