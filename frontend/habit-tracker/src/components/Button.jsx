import React from 'react'

function Button({
    children, 
    onClick, 
    type = "button", 
    className = ""
}) {
    return (
        <button
            type={type}
            onClick={onClick}
            className={`w-full bg-gray-300 py-2 rounded hover:bg-gray-500 ${className}`}
        >
            {children}
        </button>
    )
}

export default Button