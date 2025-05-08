import React from "react";
import "../styles/LoginPageStyle.css";

const Login: React.FC = () => {
    return (
        <div className="relative w-full min-h-screen bg-black flex justify-center items-center overflow-hidden">
            {/* Fondo animado */}
            <div
                className="fixed top-0 left-0 w-full h-full bg-cover bg-center animate-fadeInBg"
                style={{
                    backgroundImage:
                        "url('frontend/src/assets/bg.jpg')", // Asegúrate de tener esta imagen en /public/images/
                }}
            ></div>

            {/* Título */}
            <div className="relative z-10 text-center mb-8 opacity-0 animate-fadeIn delay-200">
                <h1 className="font-poppins font-medium text-white text-[clamp(3rem,8vw,132px)] leading-none mb-1">
                    NeuronApp
                </h1>
                <div className="w-4/5 max-w-[748px] h-[3px] bg-white mx-auto my-4" />
                <p className="font-poppins text-white text-[clamp(1.2rem,2vw,31px)]">
                    Learning happens through connection
                </p>
            </div>

            {/* Formulario */}
            <div className="relative w-[90%] max-w-[753px] z-10 opacity-0 animate-fadeIn">
                <div className="bg-black/50 backdrop-blur-lg rounded-[36px] px-8 py-12 sm:px-6 sm:py-8">
                    <h2 className="text-center text-white font-poppins text-[22px] mb-8">
                        Login
                    </h2>

                    <div className="mb-8">
                        <label
                            htmlFor="email"
                            className="block text-white font-poppins text-[22px] mb-2"
                        >
                            Email
                        </label>
                        <input
                            id="email"
                            type="email"
                            placeholder="Enter your email"
                            className="w-full h-[38px] rounded-lg bg-white/30 text-white px-4 text-sm font-poppins placeholder:text-gray-300 focus:outline-none focus:bg-white/40"
                        />
                    </div>

                    <div className="mb-8">
                        <label
                            htmlFor="password"
                            className="block text-white font-poppins text-[22px] mb-2"
                        >
                            Password
                        </label>
                        <input
                            id="password"
                            type="password"
                            placeholder="Enter your password"
                            className="w-full h-[38px] rounded-lg bg-white/30 text-white px-4 text-sm font-poppins placeholder:text-gray-300 focus:outline-none focus:bg-white/40"
                        />
                    </div>

                    <div className="text-center mt-8">
                        <p className="text-white font-poppins text-xs mb-1">
                            Don't have an account yet?
                        </p>
                        <a
                            href="/RegisterPage"
                            className="block text-pink-500 font-poppins text-xs mb-6 hover:opacity-80 transition-opacity"
                        >
                            Create one now
                        </a>
                        <button className="w-[190px] h-[34px] bg-pink-500 text-white font-poppins text-[18px] rounded hover:scale-105 transition-transform">
                            Login
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
