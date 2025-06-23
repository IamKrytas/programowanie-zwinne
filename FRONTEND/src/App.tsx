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
import {ToastContainer} from "react-toastify";

function App() {
    const token = localStorage.getItem("accessToken");
    const role = localStorage.getItem("accessRole");
    const isAdmin = role === "ADMIN";

    return (
        <div className={"bg-warning-subtle"} style={{minHeight: "100vh"}}>
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
        </div>
    );
}

export default App;
