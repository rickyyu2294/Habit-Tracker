import { useState, useEffect } from "react";
import { Typography, Box, Grid2, Stack, Fab, Modal } from "@mui/material";
import Error from "../components/Error";
import HabitCard from "../components/HabitCard";
import Page from "../components/Page";
import api from "../services/habit-tracker-api";
import { Add } from "@mui/icons-material";
import NewHabitForm from "../components/NewHabitForm";

function Dashboard() {
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
          <Stack
            maxWidth="sm"
            minWidth="sm"
            spacing={3}
            sx={{
              justifyContent: "center",
              alignItems: "center",
            }}
          >
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
        <Box
          display="flex"
          justifyContent="flex-end"
          width="100%"
          p={2}
          sx={{
            position: "fixed",
            bottom: "5vw", // 16px from the bottom of the screen
            right: "5vw", // 16px from the right of the screen
          }}
        >
          <Fab
            color="primary"
            aria-label="add"
            onClick={() => setNewHabitModalOpen(true)}
          >
            <Add />
          </Fab>
        </Box>
      </Box>

      {/* New Habit Modal */}
      <Modal
        open={newHabitModalOpen}
        onClose={handleNewHabitOnClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box display={"flex"}>
          <NewHabitForm onClose={handleNewHabitOnClose} />
        </Box>
      </Modal>
    </Page>
  );
}

export default Dashboard;
