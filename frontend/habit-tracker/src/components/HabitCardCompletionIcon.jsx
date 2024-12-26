function HabitCardCompletionIcon({ isComplete, isCurrent, onClick, children }) {
    return (
    <button
        className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold
            ${isComplete ? 'bg-blue-200 text-black' : 'bg-gray-300 text-gray-600'}
            ${isCurrent ? 'border-2 border-slate-400 hover:border-blue-300 hover:bg-blue-100' : 'cursor-default opacity-80'}
        `}
    >
        { children }
    </button>)
}

export default HabitCardCompletionIcon