import React from "react";
import '@/styles/LandingPageStyle.css';

const Landing: React.FC = () => {
    return (
        <div id="scale-wrapper">
            <div id="bg">
                <div id="digital-planet-techn"></div>
                <div id="rectangle-bg"></div>
            </div>

            <section id="landing">
                <header id="header">
                    <div className="logo-container">
                        <h1 id="neuronapp">NeuronApp</h1>
                        <p id="learninghappenstroug">Where learning happens through connection</p>
                    </div>
                    <div className="auth-buttons">
                        <a id="login" href="/LoginPage">Login</a>
                        <a id="register" href="/RegisterPage">Register</a>
                    </div>
                </header>

                <main id="body">
                    <h2 id="thenetworkforendless">The network for endless learning</h2>
                    <p id="connectwithstudentsa">Connect with students around the world</p>
                    <div id="webinputdefault">
                        <input id="preferences-input" type="text" placeholder="What do you want to learn today?" />
                    </div>
                    <button id="webbuttonprimarydefa">
                        <span id="label">Get Started</span>
                    </button>
                </main>

                <section id="about-section">
                    <div className="about-container">
                        <h2 className="about-title">About NeuronApp</h2>
                        <div className="about-content">
                            <div className="about-card">
                                <h3>Global Community</h3>
                                <p>Connect with learners and educators from all over the world.</p>
                            </div>
                            <div className="about-card">
                                <h3>Personalized Learning</h3>
                                <p>Receive recommendations based on your interests and goals.</p>
                            </div>
                            <div className="about-card">
                                <h3>Real-Time Collaboration</h3>
                                <p>Join live sessions and interactive study groups to learn together.</p>
                            </div>
                        </div>
                    </div>
                </section>
            </section>
        </div>
    );
};
export default Landing;