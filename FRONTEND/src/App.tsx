import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import HomeView from './views/HomeView';
import LoginView from './views/LoginView';
import RegisterView from './views/RegisterView';
import HomePageView from './views/HomePageView';
import Navbar from './components/Navbar';
import AdminPanelView from './views/AdminPanelView';
import ChatView from "./views/ChatView.tsx";
import ProjectView from './views/ProjectView.tsx';
import TaskView from './views/TaskView.tsx';
import ProjectByIdView from './views/ProjectByIdView.tsx';
import {ToastContainer, toast} from "react-toastify";
import {useEffect} from "react";

function App() {
    const token = sessionStorage.getItem("accessToken");
    const role = sessionStorage.getItem("accessRole");
    const isAdmin = role === "ADMIN";

    useEffect(() => {
        window.addEventListener("error", (event) => {
            console.log("hi");
            toast.error("Wystąpił błąd. Spróbuj ponownie później.");
            event?.preventDefault();
        });

        window.addEventListener("unhandledrejection", (event) => {
            toast("Nieobsłużony Promise: " + event.reason);
        });
    }, []);

    return (
        <>
            <ToastContainer />
            <Router>
                {token && <Navbar />}
                <Routes>
                    {/* Publiczne trasy */}
                    {!token ? (
                        <>
                            <Route path="/" element={<HomeView />} />
                            <Route path="/logowanie" element={<LoginView />} />
                            <Route path="/rejestracja" element={<RegisterView />} />
                            <Route path="*" element={<Navigate to="/" />} />
                        </>
                    ) : (
                        <>
                            <Route path="/home" element={<HomePageView />} />
                            <Route path="/projects" element={<ProjectView />} />
                            <Route path="/project/:id" element={<ProjectByIdView />} />
                            <Route path="/tasks" element={<TaskView />} />
                            <Route path="/chat" element={<ChatView />} />
                            <Route path="*" element={<Navigate to="/home" />} />
                            {isAdmin && (
                                <Route path="/admin" element={<AdminPanelView />} />
                            )}

                        </>
                    )}
                </Routes>
            </Router>
        </>
    );
}

export default App;
