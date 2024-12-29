import { Modal, Box, Typography, Button } from "@mui/material";
import NewHabitForm from "./NewHabitForm";
import Form from "./Form";
import api from "../services/habit-tracker-api";

function DeleteHabitModal({ habit, open, onClose, onComplete }) {
  const handleDelete = async () => {
    await api.deleteHabit(habit.id);
    onComplete();
  };

  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={{ display: "flex", flexDirection: "column" }}>
        <Form>
          <Typography variant="h5" component="h1" gutterBottom>
            Delete habit <b>{habit.name}</b>?
          </Typography>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              marginTop: 2,
            }}
          >
            <Button variant="outlined" onClick={onClose}>
              Cancel
            </Button>
            <Button variant="contained" color="error" onClick={handleDelete}>
              Delete
            </Button>
          </Box>
        </Form>
      </Box>
    </Modal>
  );
}

export default DeleteHabitModal;
