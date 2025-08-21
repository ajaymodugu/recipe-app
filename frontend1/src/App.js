import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Rating from 'react-rating-stars-component';
import { Drawer, Table, TableBody, TableCell, TableHead, TableRow } from '@material-ui/core';

const App = () => {
  const [recipes, setRecipes] = useState([]);
  const [page, setPage] = useState(1);
  const [limit, setLimit] = useState(15);
  const [total, setTotal] = useState(0);
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const [filters, setFilters] = useState({});  // For search
  const [expanded, setExpanded] = useState(false);  // For time expand

  useEffect(() => {
    fetchRecipes();
  }, [page, limit, filters]);

  const fetchRecipes = () => {
    let url = `/api/recipes?page=${page}&limit=${limit}`;
    if (Object.keys(filters).length > 0) {
      url = '/api/recipes/search?' + new URLSearchParams(filters).toString();
    }
    axios.get(url)
      .then(res => {
        setRecipes(res.data.data || res.data);
        setTotal(res.data.total || res.data.length);
      })
      .catch(() => setRecipes([]));
  };

  const handleFilterChange = (field, value) => {
    setFilters(prev => ({ ...prev, [field]: value }));
    setPage(1);  // Reset page on filter
  };

  const handleRowClick = (recipe) => {
    setSelectedRecipe(recipe);
  };

  return (
    <div className="p-4">
      {/* Filters: Inputs for title, cuisine, rating, etc. */}
      <div className="mb-4">
        <input placeholder="Title" onChange={(e) => handleFilterChange('title', e.target.value)} className="border p-1 mr-2" />
        <input placeholder="Cuisine" onChange={(e) => handleFilterChange('cuisine', e.target.value)} className="border p-1 mr-2" />
        <input placeholder="Rating >= " onChange={(e) => handleFilterChange('rating', `>=${e.target.value}`)} className="border p-1 mr-2" />
        <input placeholder="Total Time <= " onChange={(e) => handleFilterChange('total_time', `<=${e.target.value}`)} className="border p-1 mr-2" />
        {/* Add for calories similarly */}
      </div>

      {/* Table */}
      {recipes.length === 0 ? (
        <div>No results found / No data available</div>
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Title</TableCell>
              <TableCell>Cuisine</TableCell>
              <TableCell>Rating</TableCell>
              <TableCell>Total Time</TableCell>
              <TableCell>Serves</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {recipes.map(recipe => (
              <TableRow key={recipe.id} onClick={() => handleRowClick(recipe)} className="cursor-pointer">
                <TableCell className="truncate max-w-xs">{recipe.title}</TableCell>
                <TableCell>{recipe.cuisine}</TableCell>
                <TableCell>
                  <Rating value={recipe.rating || 0} edit={false} size={20} />
                </TableCell>
                <TableCell>{recipe.totalTime}</TableCell>
                <TableCell>{recipe.serves}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}

      {/* Pagination */}
      <div className="mt-4">
        <select value={limit} onChange={(e) => setLimit(Number(e.target.value))} className="border p-1 mr-2">
          {[15, 20, 30, 40, 50].map(opt => <option key={opt}>{opt}</option>)}
        </select>
        <button onClick={() => setPage(Math.max(1, page - 1))} disabled={page === 1}>Prev</button>
        <span> Page {page} of {Math.ceil(total / limit)} </span>
        <button onClick={() => setPage(page + 1)} disabled={page >= Math.ceil(total / limit)}>Next</button>
      </div>

      {/* Drawer */}
      <Drawer anchor="right" open={!!selectedRecipe} onClose={() => setSelectedRecipe(null)}>
        {selectedRecipe && (
          <div className="p-4 w-80">
            <h2>{selectedRecipe.title} - {selectedRecipe.cuisine}</h2>
            <p><strong>Description:</strong> {selectedRecipe.description}</p>
            <div onClick={() => setExpanded(!expanded)} className="cursor-pointer">
              <strong>Total Time:</strong> {selectedRecipe.totalTime} <span>{expanded ? '-' : '+'}</span>
            </div>
            {expanded && (
              <div>
                <p>Prep Time: {selectedRecipe.prepTime}</p>
                <p>Cook Time: {selectedRecipe.cookTime}</p>
              </div>
            )}
            <h3>Nutrients</h3>
            <table className="table-auto">
              <tbody>
                {Object.entries(JSON.parse(selectedRecipe.nutrients || '{}')).map(([key, value]) => (
                  <tr key={key}><td>{key}</td><td>{value}</td></tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </Drawer>
    </div>
  );
};

export default App;
