import React from "react";
import Navbar from "@/components/Navbar";
import Sidebar from "@/components/Sidebar";
import ContentSection from "@/components/ContentSection";

export default function Home() {
    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Navbar />

            <main className="flex flex-1 px-4 py-6 gap-4">
                {/* Sidebar lateral */}
                <aside className="hidden md:block w-full md:w-1/4 lg:w-1/5">
                    <Sidebar />
                </aside>

                {/* Sección de contenidos */}
                <section className="flex-1 w-full">
                    <ContentSection />
                </section>
            </main>
        </div>
    );
}
