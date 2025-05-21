import React from 'react';
import Header from '../components/Header';
import SearchSection from '../components/SearchSection';
import ResultSection from '../components/ResultSection';
import SimilarContents from '../components/SimilarContents';
import '../styles/ContentView.css';

const ContentView: React.FC = () => {
    return (
        <div className="contenido-container">
            <Header />
            <main className="contenido-main">
                <SearchSection />
                <ResultSection />
                <SimilarContents />
            </main>
        </div>
    );
};

export default ContentView;
