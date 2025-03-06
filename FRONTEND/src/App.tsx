import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import HomeView from './views/HomeView';
import LoginView from './views/LoginView';
import RegisterView from './views/RegisterView';

// import Navbar from './components/Navbar'
// import Footer from './components/Footer'

function App() {
    return (
        <>
            <Router>
                {/* <Navbar /> */}
                <Routes>
                <Route path="/" element={<HomeView />} />
                <Route path="/logowanie" element={<LoginView />} />
                <Route path='/rejestracja' element={<RegisterView />} />
                <Route path="*" element={<Navigate to="/" />} />
                </Routes>
                {/* <Footer /> */}
            </Router>
        </>
    )
}

export default App