import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";

function Signup() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [role, setRole] = useState("USER");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handlesignup = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      setMessage("Passwords do not match");
      return;
    }

    try {
      const response = await API.post("/auth/signup", {
        username,
        email,
        password,
        confirmPassword,
        role,
      });

      // Store token if provided in signup response
      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
      }
      
      localStorage.setItem('username', response.data.username || username);
      localStorage.setItem('role', response.data.role || role);

      setMessage(response.data.message || "Signup successful");
      
      // Clear form fields
      setUsername("");
      setEmail("");
      setPassword("");
      setConfirmPassword("");
      setRole("USER");
      
      // Redirect to home after successful signup
      setTimeout(() => {
        navigate('/');
      }, 1500);
    } catch (err) {
      setMessage(err.response?.data?.message || "Signup failed");
      setPassword("");
      setConfirmPassword("");
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('role');
    }
  };

  return (
    <form onSubmit={handlesignup}>
      <h2>Signup</h2>

      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        required
      />

      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
      />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />

      <input
        type="password"
        placeholder="Confirm Password"
        value={confirmPassword}
        onChange={(e) => setConfirmPassword(e.target.value)}
        required
      />

      <select value={role} onChange={(e) => setRole(e.target.value)}>
        <option value="USER">User</option>
        <option value="OPERATOR">Operator</option>
        <option value="ADMIN">Admin</option>
        <option value="DELIVERY">Delivery</option>
      </select>
      <button type="submit">Signup</button>

      {message && <p>{message}</p>}
    </form>
  );
}

export default Signup;
