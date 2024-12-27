import { useState, useEffect } from 'react';
import { Container, Typography, Box, Grid, Grid2, Stack } from '@mui/material';
import Error from '../components/Error';
import HabitCard from '../components/HabitCard';
import Page from '../components/Page';
import api from '../services/habit-tracker-api';

function Dashboard() {
    const [error, setError] = useState('');
    const [habits, setHabits] = useState([]);

    const fetchHabits = async () => {
        try {
            const response = await api.getHabits();
            setHabits(response.data);
        } catch (err) {
            setError('Failed to load habits. Please try again.');
        }
    };

    useEffect(() => {
        fetchHabits();
    }, []);

    return (
        <Page title="Dashboard">
            <Box display="flex" flexDirection="column" alignItems="center">
                {error && <Error error={error} />}
                {habits.length > 0 ? (
                    <Stack maxWidth="sm" minWidth="sm" spacing={3} sx={{
                        justifyContent: "center",
                        alignItems: "center",
                    }}>
                        {habits.map((habit) => (
                            <Grid2 key={habit.id}>
                                <HabitCard habit={habit} onComplete={fetchHabits} />
                            </Grid2>
                        ))}
                    </Stack>
                ) : (
                    <Box
                        display="flex"
                        justifyContent="center"
                        alignItems="center"
                        minHeight="50vh"
                    >
                        <Typography variant="body1" color="textSecondary">
                            No habits found. Add a new habit to get started!
                        </Typography>
                    </Box>
                )}
            </Box>
        </Page>
    );
}

export default Dashboard;
