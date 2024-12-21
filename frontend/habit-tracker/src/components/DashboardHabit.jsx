import { format, isSameDay, subDays } from "date-fns";
import { getCurrentDate } from "../utils/utils";
import habitTrackerApi from "../services/habit-tracker-api";

function DashboardHabit({habit, onComplete}) {
    const today = new Date();
    const last7Days = Array.from(
        { length: 7 }, (_, i) => format(subDays(today, i), 'yyyy-MM-dd')
    ).reverse();

    // Check if each day is in the completions list
    const completions = habit.completions || [];
    const isDayComplete = (day) => {
        return completions.some((completion) => {
            const completionDate = new Date(completion.completionDate)
            const match = isSameDay(completionDate, day)
            return match
        }
        );
    };

    const completeHabit = async (id, completeDate) => {
        const date = completeDate
        try {
            await habitTrackerApi.post(`/habits/${id}/completions`,
                { date: date },
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                    }
                }
            )
            onComplete()
        } catch (err) {
            console.log(err)
        }
    }

    return (
        <li className='flex flex-col gap-2'>
            {/* Habit title */}
            <div className='flex justify-between items-center'>
                <p className='font-thin lowercase'>{habit.frequency}</p>
                <h2 className='flex-1 text-center'>{habit.name}</h2>
            </div>
            {/* Last 7 days completions */}
            <div className="flex gap-4 justify-center"> 
                {last7Days.map((day, index) => (
                    <button
                        key={index}
                        // Date completion icon
                        className={`w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold
                            ${isDayComplete(day) ? 'bg-blue-200 text-black' : 'bg-gray-300 text-gray-600'}
                        `}
                        onClick={() => completeHabit(habit.id, day)}
                    >
                        {format(day, 'EE').charAt(0)} {/* First letter of weekday */}
                    </button>
                ))}
            </div>
        </li> 
    )
}

export default DashboardHabit