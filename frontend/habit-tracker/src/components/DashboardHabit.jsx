import { format, isSameDay, subDays } from "date-fns";
import api from "../services/habit-tracker-api";
import { useState, useEffect } from "react";

function DashboardHabit({ habit, onComplete }) {
    const [completions, setCompletions] = useState([])

    const today = new Date();
    const last7Days = Array.from(
        { length: 7 }, (_, i) => format(subDays(today, i), 'yyyy-MM-dd')
    ).reverse();

    const fetchCompletions = async () => {
        try {
            const response = await api.getCompletions(habit.id);
            setCompletions(response.data || [])
        } catch (err) {

        }
    };

    // Check if each day is in the completions list
    const isDayComplete = (day) => {
        return completions.some((completion) => {
            const match = isSameDay(completion.completionDate, day)
            return match
        }
        );
    };

    const toggleCompletion = async (habitId, date) => {
        const isComplete = isDayComplete(date);
        try {
            if (isComplete) {
                await api.deleteCompletion(habitId, date);
            } else {
                await api.markCompletion(habitId, date);
            }
            const response = await api.getCompletions(habitId);
            setCompletions(response.data || []);
            onComplete();
        } catch (err) {
            console.log(err)
        }
    }

    useEffect(() => {
        fetchCompletions();
    }, [])

    return (
        <li className='flex flex-col gap-2 bg-slate-50 shadow-lg border rounded-xl border-gray-200 p-4 hover:border-slate-300 hover:bg-slate-100'>
            {/* Habit title */}
            <div className='flex flex-col justify-between items-center'>
                <h2 className='flex-1 text-center font-bold'>{habit.name}</h2>

                <p className='font-thin lowercase'>{habit.frequency}</p>
            </div>
            {/* Last 7 days completions */}
            <div className="flex gap-4 justify-center">
                {last7Days.map((day, index) => (
                    // Date completion icon
                    <button
                        key={index}
                        className={`w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold
                            ${isDayComplete(day) ? 'bg-blue-200 text-black hover:bg-blue-100' : 'bg-gray-300 text-gray-600 hover:bg-blue-100'}
                        `}
                        onClick={() => toggleCompletion(habit.id, day)}
                    >
                        {format(day, 'EE').charAt(0)} {/* First letter of weekday */}
                    </button>
                ))}
            </div>
        </li>
    )
}

export default DashboardHabit