import React, { useContext } from 'react'
import { FaHome, FaInfo, FaEnvelope, FaMoon, FaSun } from 'react-icons/fa'
import IMG from "./../../public/vite.svg"
import { ThemeContext } from '../context/ThemeContext';


      function Header() {
            const { isDarkMode, toggleTheme } = useContext(ThemeContext);

          return (
            <header className="header flex flex-wrap justify-center md:justify-between items-center py-4 bg-gray-800 text-white">
              <div className="logo-container mx-5">
                <img src={IMG} alt="Your Company Logo" className="h-8 md:h-12" />
              </div>
              {/* ... other header elements ... */}
              <nav className="mx-5">
                <ul className='flex gap-4  flex-wrap md:gap-8'>
                  <li>
                        <button onClick={toggleTheme}>
                            {isDarkMode ? <FaSun className="text-xl" /> : <FaMoon className="text-xl" />}
                        </button>
                  </li>
                </ul>
              </nav>
            </header>
          )
        }

        export default Header
    
  