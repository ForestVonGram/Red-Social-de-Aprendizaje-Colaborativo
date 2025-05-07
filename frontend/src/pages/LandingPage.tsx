import React from "react";
import "@/styles/RegisterPageStyle.css";
import { Link } from "react-router-dom";

const RegisterPage: React.FC = () => {
    return (
        <div id="register">
            <div id="futuristic-light-wav"></div>

            <div id="titledev">
                <h1 id="neuronapp">NeuronApp</h1>
                <div id="path"></div>
                <p id="learninghappenstroug">Learning happens through connection</p>
            </div>

            <div id="registerdev">
                <div id="rectangle">
                    <h2 className="input-label" style={{ textAlign: "center", marginBottom: "2rem" }}>
                        Register
                    </h2>

                    <div className="input-group">
                        <label className="input-label" htmlFor="name">Name</label>
                        <input
                            type="text"
                            id="name"
                            className="input-field"
                            placeholder="Enter your name"
                        />
                    </div>

                    <div className="input-group">
                        <label className="input-label" htmlFor="email">Email</label>
                        <input
                            type="email"
                            id="email"
                            className="input-field"
                            placeholder="Enter your email"
                        />
                    </div>

                    <div className="input-group">
                        <label className="input-label" htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            className="input-field"
                            placeholder="Enter your password"
                        />
                    </div>

                    <div id="loginaccount">
                        <p id="alreadyhaveanaccount">Already have an account?</p>
                        <Link to="/login" className="login-link">Log in</Link>
                        <button id="webbuttonprimarydefa">
                            <span id="label">Register</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;