import React, { useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import "@fontsource/roboto/300.css";
import "@fontsource/roboto/400.css";
import "@fontsource/roboto/500.css";
import "@fontsource/roboto/700.css";

function Home() {
    const isLoggedIn = localStorage.getItem("accessToken") != null;
    const navigate = useNavigate();

    const redirect = () => {
        if (isLoggedIn) {
            navigate("/dashboard");
        } else {
            navigate("/login");
        }
    };

    useEffect(() => {
        redirect();
    }, []);

    return <></>;
}

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/dashboard" element={<Dashboard />} />
            </Routes>
        </Router>
    );
}

export default App;
