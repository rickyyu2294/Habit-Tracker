import { useState, useEffect } from "react";
import { Typography, Box, Stack, Fab } from "@mui/material";
import HabitCard from "../components/Dashboard/HabitCard";
import Page from "../components/Layout/Page";
import api from "../services/habit-tracker-api";
import { Add } from "@mui/icons-material";
import ErrorMessage from "../components/Layout/ErrorMessage";
import React from "react";
import NewHabitModal from "../components/Modals/NewHabitModal";

const Dashboard = () => {
    const [error, setError] = useState("");
    const [habits, setHabits] = useState([]);
    const [newHabitModalOpen, setNewHabitModalOpen] = useState(false);

    const handleNewHabitOnClose = () => {
        setNewHabitModalOpen(false);
        fetchHabits();
    };

    const fetchHabits = async () => {
        try {
            const response = await api.getHabits();
            setHabits(response.data);
        } catch (err) {
            setError("Failed to load habits. Please try again.");
            console.error(err);
        }
    };

    useEffect(() => {
        fetchHabits();
    }, []);

    return (
        <Page title="Dashboard">
            <Box display="flex" flexDirection="column" alignItems="center">
                {error && <ErrorMessage error={error} />}
                {habits.length > 0 ? (
                    <Stack
                        maxWidth="lg"
                        minWidth="lg"
                        spacing={3}
                        sx={{
                            justifyContent: "center",
                            alignItems: "center",
                        }}
                    >
                        {habits.map((habit, index) => (
                            <HabitCard
                                key={index}
                                habit={habit}
                                onComplete={fetchHabits}
                            />
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
                <Box
                    display="flex"
                    justifyContent="flex-end"
                    width="100%"
                    p={2}
                    sx={{
                        position: "fixed",
                        bottom: "5vw", // 16px from the bottom of the screen
                        right: "5vw", // 16px from the right of the screen
                        pointerEvents: "none",
                    }}
                >
                    <Fab
                        color="primary"
                        aria-label="add"
                        onClick={() => setNewHabitModalOpen(true)}
                        sx={{
                            pointerEvents: "auto",
                        }}
                    >
                        <Add />
                    </Fab>
                </Box>
            </Box>

            {/* New Habit Modal */}
            <NewHabitModal
                open={newHabitModalOpen}
                onClose={handleNewHabitOnClose}
            />
        </Page>
    );
};

export default Dashboard;
