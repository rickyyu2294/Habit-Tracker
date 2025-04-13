import { useState, useEffect } from "react";
import { Typography, Box, Stack, Fab } from "@mui/material";
import HabitCard from "../components/Dashboard/HabitCard";
import Page from "../components/Layout/Page";
import api from "../services/habit-tracker-api";
import { Add } from "@mui/icons-material";
import ErrorMessage from "../components/Layout/ErrorMessage";
import React from "react";
import NewHabitModal from "../components/Modals/NewHabitModal";
import Sidebar from "../components/Layout/Sidebar";

const Dashboard = () => {
    const [error, setError] = useState("");
    const [habits, setHabits] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState(null);
    const [newHabitModalOpen, setNewHabitModalOpen] = useState(false);

    const handleNewHabitOnClose = () => {
        setNewHabitModalOpen(false);
        fetchHabits(selectedGroup);
    };

    const fetchHabits = async () => {
        try {
            const response = selectedGroup
                ? await api.getHabits(selectedGroup.id)
                : await api.getHabits();
            setHabits(response.data);
        } catch (err) {
            setError("Failed to load habits. Please try again.");
            console.error(err);
        }
    };

    useEffect(() => {
        fetchHabits(selectedGroup);
    }, [selectedGroup]);

    return (
        <Page title={selectedGroup ? selectedGroup.name : "All"}>
            <Sidebar
                selectedGroup={selectedGroup}
                onGroupSelect={setSelectedGroup}
            />
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
                        {habits.map((habit) => (
                            <HabitCard
                                key={habit.id}
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
