import {useState, useEffect} from 'react'
import Error from '../components/Error'
import habitTrackerApi, { habitTrackerApiGet } from '../services/habit-tracker-api' 
import { getCurrentDate } from '../utils/utils'
import DashboardHabit from '../components/DashboardHabit'

function Dashboard() {
    const [error, setError] = useState("")
    const [habits, setHabits] = useState([])

    const fetchHabits = async () => {
        try {
            const response = await habitTrackerApiGet('/habits')
            setHabits(response.data)
        } catch (err) {
            setError("Failed to load habits. Please try again.")
        }
    }

    useEffect(() => {
        fetchHabits()
    }, [])

    return (
        // Display a list of habits for the logged-in user
        // Allow users to View/Complete/Add/edit/delete habits
        <div>
            <div>
                <h1 className='text-2xl font-bold p-8 mb-4'>Dashboard</h1>
                <Error error={error} />
            </div>
            
            <div className='min-h-80 flex items-center justify-center bg-gray-100'>
                <ul className='flex flex-col w-3/5 gap-6'>
                    {habits.map((habit) => (
                        <DashboardHabit key={habit.id} habit={habit} onComplete={fetchHabits}/> 
                    ))}
                </ul>
            </div>
            
        </div>
    )
}

export default Dashboard