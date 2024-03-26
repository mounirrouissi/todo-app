import React, { useState, createContext } from 'react';

const ThemeContext = createContext();

const ThemeProvider = ({ children }) => {
    
    const [isDarkMode, setIsDarkMode] = useState(false); 

    const toggleTheme = () => {
        setIsDarkMode(!isDarkMode); 
    };

    return (
        <ThemeContext.Provider value={{ isDarkMode, toggleTheme }}>
            {children}
        </ThemeContext.Provider>
    );
};

export { ThemeContext, ThemeProvider };
