import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

function Home() {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState("");
  const [tokenValid, setTokenValid] = useState(false);

  // Decode JWT token to extract username
  const decodeTokenAndGetUsername = (token) => {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      
      const decoded = JSON.parse(jsonPayload);
      return decoded.sub; // 'sub' contains the username
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  };

  useEffect(() => {
    // Check if token exists in localStorage
    const token = localStorage.getItem('token');
    
    if (token) {
      // Decode token to get username
      const decodedUsername = decodeTokenAndGetUsername(token);
      
      if (decodedUsername) {
        setIsLoggedIn(true);
        setUsername(decodedUsername);
        setTokenValid(true);
      } else {
        setIsLoggedIn(false);
        setTokenValid(false);
      }
    } else {
      setIsLoggedIn(false);
      setTokenValid(false);
    }
  }, []);

  const handleLogout = () => {
    // Clear all auth data from localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');

    setIsLoggedIn(false);
    setUsername("");
    setTokenValid(false);
    navigate('/login');
  };

  return (
    <div style={styles.container}>
      {isLoggedIn && tokenValid ? (
        <>
          <h1>Welcome, {username}</h1>
          <div style={styles.authStatus}>
            <p>✓ You are logged in with valid token</p>
          </div>
        </>
      ) : (
        <h1>Welcome</h1>
      )}

      <div style={styles.buttons}>
        {isLoggedIn && tokenValid ? (
          <button onClick={handleLogout} style={styles.logoutButton}>Logout</button>
        ) : (
          <>
            <button onClick={() => navigate("/login")}>Login</button>
            <button onClick={() => navigate("/signup")}>Signup</button>
          </>
        )}
      </div>
    </div>
  );
}

export default Home;

const styles = {
  container: {
    height: "100vh",
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
  },
  buttons: {
    display: "flex",
    gap: "20px",
  },
  logoutButton: {
    backgroundColor: "#dc3545",
    color: "white",
    padding: "10px 20px",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
  },
  authStatus: {
    marginTop: "20px",
    padding: "10px 20px",
    backgroundColor: "#d4edda",
    borderRadius: "5px",
    color: "#155724",
  },
};

