function HabitCardCompletionIcon({ isComplete, isCurrent, onClick, children }) {
  return (
    <button
      className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold
            ${isComplete ? "bg-green-400 text-black border-2 border-green-500" : "bg-gray-300 text-gray-600"}
            ${isCurrent ? "border-2 border-slate-400 hover:border-blue-300 hover:bg-blue-100" : "cursor-default opacity-80"}`}
      onClick={onClick}
    >
      {children}
    </button>
  );
}

export default HabitCardCompletionIcon;
