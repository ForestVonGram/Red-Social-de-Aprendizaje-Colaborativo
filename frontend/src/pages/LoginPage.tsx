import React from "react";
import "@/styles/LoginPageStyle.css";
import { Link } from "react-router-dom";

const LoginPage: React.FC = () => {
    return (
        <div id="login">
            <div id="futuristic-light-wav"></div>

            <div id="titledev">
                <h1 id="neuronapp">NeuronApp</h1>
                <div id="path"></div>
                <p id="learninghappenstroug">Learning happens through connection</p>
            </div>

            <div id="logindev">
                <div id="rectangle">
                    <h2 className="input-label" style={{ textAlign: "center", marginBottom: "2rem" }}>
                        Login
                    </h2>

                    <div className="input-group">
                        <label className="input-label" htmlFor="email">Email</label>
                        <input
                            id="email"
                            type="email"
                            className="input-field"
                            placeholder="Enter your email"
                        />
                    </div>

                    <div className="input-group">
                        <label className="input-label" htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            className="input-field"
                            placeholder="Enter your password"
                        />
                    </div>

                    <div id="createaccount">
                        <p id="donthaveanaccountyet">Don't have an account yet?</p>
                        <Link to="/register" className="create-account-link">Create one now</Link>
                        <button id="webbuttonprimarydefa">
                            <span id="label">Login</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;