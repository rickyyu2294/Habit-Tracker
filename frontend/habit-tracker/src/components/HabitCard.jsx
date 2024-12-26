import { format, isSameDay, subDays, subWeeks } from "date-fns";
import api from "../services/habit-tracker-api";
import { useState, useEffect } from "react";
import HabitCardCompletionIcon from "./HabitCardCompletionIcon";

function HabitCard({ habit, onComplete }) {
    const [completions, setCompletions] = useState(null)

    const fetchCompletions = async () => {
        try {
            const response = await api.getCompletions(habit.id, habit.frequency);
            setCompletions(response.data)
        } catch (err) {
            console.log(err)
        }
    };

    const getIntervals = (frequency) => {
        const today = new Date();
        switch (frequency.toLowerCase()) {
            case "daily":
                return Array.from(
                    { length: 7 }, (_, i) => format(subDays(today, i), "yyyy-MM-dd")
                ).reverse();
            case "weekly":
                return Array.from(
                    { length: 7 }, (_, i) => format(subWeeks(today, i), "yyyy-'W'ww")
                ).reverse();
            case "monthly":
                return Array.from(
                    { length: 12 }, (_, i) => format(subMonths(today, i), "yyyy-MM")
                ).reverse();
            default:
                return [];
        }
    }

    const intervals = getIntervals(habit.frequency);

    const isIntervalComplete = (interval) => {
        // if completions is populated, find if completions map contains a key with this interval
        if (completions) {
            return Object.keys(completions.completions).some((value) => value === interval);
        } else {
            return false
        }
    };

    const toggleCompletion = async (habit, date) => {
        const isComplete = isIntervalComplete(date);
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
            {/* Completion buttons */}
            <div className="flex gap-4 justify-center">
                {intervals.map((interval, index) => {
                    const isCurrent = interval === intervals[intervals.length - 1];
                    return ( // Date completion icon
                        <HabitCardCompletionIcon 
                            key={index}
                            onClick={() => isCurrent && toggleCompletion(habit, interval)}
                            isCurrent={isCurrent}
                            isComplete={isIntervalComplete(interval)}
                        >
                            {habit.frequency.toLowerCase() === "daily"
                                ? format(new Date(interval), "EE").charAt(0) // First letter of weekday
                                : habit.frequency.toLowerCase() === "weekly"
                                ? `W${interval.split("W")[1]}` // Week number
                                : habit.frequency.toLowerCase() === "monthly"
                                ? format(new Date(interval + "-01"), "MM") // Month name
                                : ""}
                        </HabitCardCompletionIcon>
                    )})
                }
            </div>
        </li>
    )
}

export default HabitCard