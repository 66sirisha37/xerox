// Auth utility functions for managing JWT tokens

export const isLoggedIn = () => {
  return !!localStorage.getItem('token');
};

export const getToken = () => {
  return localStorage.getItem('token');
};

export const getUsername = () => {
  return localStorage.getItem('username');
};

export const getRole = () => {
  return localStorage.getItem('role');
};

export const setAuthData = (token, username, role) => {
  if (token) {
    localStorage.setItem('token', token);
  }
  if (username) {
    localStorage.setItem('username', username);
  }
  if (role) {
    localStorage.setItem('role', role);
  }
};

export const clearAuthData = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('username');
  localStorage.removeItem('role');
};

export const hasAuthToken = () => {
  const token = localStorage.getItem('token');
  return token && token.length > 0;
};
