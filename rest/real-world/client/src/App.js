import React, { useState, useEffect, useCallback } from 'react';

const s = {
  app: { display: 'flex', minHeight: '100vh', fontFamily: 'sans-serif', background: '#f0f2f5' },
  sidebar: { width: 220, background: '#1a1a2e', color: '#eee', padding: '24px 0', flexShrink: 0 },
  sidebarTitle: { fontSize: 13, fontWeight: 700, textTransform: 'uppercase', letterSpacing: 1, color: '#888', padding: '0 20px', marginBottom: 8 },
  catItem: { padding: '10px 20px', cursor: 'pointer', fontSize: 15, transition: 'background 0.15s' },
  catActive: { background: '#4a90d9', color: '#fff', borderRadius: 0 },
  main: { flex: 1, padding: 24 },
  topBar: { display: 'flex', gap: 10, marginBottom: 20, flexWrap: 'wrap', alignItems: 'center' },
  input: { padding: '8px 12px', border: '1px solid #ccc', borderRadius: 6, fontSize: 14, background: '#fff' },
  filterBtn: { padding: '8px 18px', background: '#4a90d9', color: '#fff', border: 'none', borderRadius: 6, cursor: 'pointer', fontSize: 14 },
  grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 18 },
  card: { background: '#fff', borderRadius: 10, boxShadow: '0 1px 4px rgba(0,0,0,0.08)', overflow: 'hidden' },
  cardImg: { width: '100%', height: 140, objectFit: 'cover', background: '#e9ecef' },
  cardBody: { padding: 14 },
  cardName: { fontWeight: 600, fontSize: 15, marginBottom: 4 },
  cardCat: { fontSize: 12, color: '#4a90d9', marginBottom: 6 },
  cardPrice: { fontSize: 18, fontWeight: 700, color: '#1a1a2e' },
  cardStock: { fontSize: 12, color: '#888', marginTop: 4 },
  pagination: { display: 'flex', gap: 10, marginTop: 24, alignItems: 'center', justifyContent: 'center' },
  pageBtn: { padding: '7px 16px', border: '1px solid #ccc', borderRadius: 6, cursor: 'pointer', background: '#fff', fontSize: 14 },
  pageInfo: { fontSize: 14, color: '#555' },
  empty: { gridColumn: '1 / -1', textAlign: 'center', color: '#aaa', padding: 40 },
  header: { fontSize: 22, fontWeight: 700, marginBottom: 18, color: '#1a1a2e' },
};

export default function App() {
  const [categories, setCategories] = useState([]);
  const [products, setProducts] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [search, setSearch] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [page, setPage] = useState(0);
  const [activeSearch, setActiveSearch] = useState({ search: '', minPrice: '', maxPrice: '' });

  useEffect(() => {
    fetch('/api/categories').then(r => r.json()).then(setCategories);
  }, []);

  const fetchProducts = useCallback(() => {
    const params = new URLSearchParams({ page, size: 9 });
    if (selectedCategory) params.set('category', selectedCategory);
    if (activeSearch.search) params.set('search', activeSearch.search);
    if (activeSearch.minPrice && activeSearch.maxPrice) {
      params.set('minPrice', activeSearch.minPrice);
      params.set('maxPrice', activeSearch.maxPrice);
    }
    fetch(`/api/products?${params}`)
      .then(r => r.json())
      .then(data => {
        setProducts(data.content || []);
        setTotalPages(data.totalPages || 0);
      });
  }, [page, selectedCategory, activeSearch]);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  function handleCategoryClick(id) {
    setSelectedCategory(prev => (prev === id ? null : id));
    setPage(0);
  }

  function handleFilter() {
    setActiveSearch({ search, minPrice, maxPrice });
    setPage(0);
  }

  function handleKeyDown(e) {
    if (e.key === 'Enter') handleFilter();
  }

  return (
    <div style={s.app}>
      <aside style={s.sidebar}>
        <div style={{ padding: '0 20px 20px', fontSize: 20, fontWeight: 700, color: '#fff', borderBottom: '1px solid #333', marginBottom: 16 }}>
          Catalog
        </div>
        <div style={s.sidebarTitle}>Categories</div>
        <div
          style={{ ...s.catItem, ...(selectedCategory === null ? s.catActive : {}) }}
          onClick={() => handleCategoryClick(null)}
        >
          All Products
        </div>
        {categories.map(cat => (
          <div
            key={cat.id}
            style={{ ...s.catItem, ...(selectedCategory === cat.id ? s.catActive : {}) }}
            onClick={() => handleCategoryClick(cat.id)}
          >
            {cat.name}
          </div>
        ))}
      </aside>

      <main style={s.main}>
        <div style={s.header}>Product Catalog</div>
        <div style={s.topBar}>
          <input
            style={{ ...s.input, minWidth: 200 }}
            placeholder="Search products..."
            value={search}
            onChange={e => setSearch(e.target.value)}
            onKeyDown={handleKeyDown}
          />
          <input
            style={{ ...s.input, width: 90 }}
            placeholder="Min $"
            type="number"
            value={minPrice}
            onChange={e => setMinPrice(e.target.value)}
          />
          <input
            style={{ ...s.input, width: 90 }}
            placeholder="Max $"
            type="number"
            value={maxPrice}
            onChange={e => setMaxPrice(e.target.value)}
          />
          <button style={s.filterBtn} onClick={handleFilter}>Filter</button>
          <button
            style={{ ...s.filterBtn, background: '#95a5a6' }}
            onClick={() => {
              setSearch('');
              setMinPrice('');
              setMaxPrice('');
              setSelectedCategory(null);
              setActiveSearch({ search: '', minPrice: '', maxPrice: '' });
              setPage(0);
            }}
          >
            Clear
          </button>
        </div>

        <div style={s.grid}>
          {products.length === 0 && <div style={s.empty}>No products found.</div>}
          {products.map(p => (
            <div key={p.id} style={s.card}>
              <img src={p.imageUrl} alt={p.name} style={s.cardImg} />
              <div style={s.cardBody}>
                <div style={s.cardName}>{p.name}</div>
                <div style={s.cardCat}>{p.category?.name}</div>
                <div style={s.cardPrice}>${p.price?.toFixed(2)}</div>
                <div style={s.cardStock}>{p.stock} in stock</div>
              </div>
            </div>
          ))}
        </div>

        {totalPages > 1 && (
          <div style={s.pagination}>
            <button style={s.pageBtn} disabled={page === 0} onClick={() => setPage(p => p - 1)}>Prev</button>
            <span style={s.pageInfo}>Page {page + 1} of {totalPages}</span>
            <button style={s.pageBtn} disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Next</button>
          </div>
        )}
      </main>
    </div>
  );
}
