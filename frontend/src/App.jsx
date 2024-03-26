import React, { useContext } from 'react'
import Header from './components/Header'
import Hero from './components/Hero'
import Footer from './components/Footer'
import { ThemeContext } from './context/ThemeContext';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
function App() {
  const { isDarkMode } = useContext(ThemeContext);

  return (
    <div className='w-full min-h-screen flex flex-col  bg-slate-500 '>
      <Header className="bg-green-700" />
      <main className={` w-full flex-1 flex flex-col justify-center items-center  ${isDarkMode ? 'dark:bg-gray-900' : 'bg-red-400'} `} >
        <Hero />
      </main>
      <Footer />
      <ToastContainer />
    </div>
  );
}


export default App