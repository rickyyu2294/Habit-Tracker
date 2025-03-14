import { Modal, Box, Typography, Button } from "@mui/material";
import Form from "../Forms/Form";
import api from "../../services/habit-tracker-api";
import React from "react";
import PropTypes from "prop-types";

export default function DeleteHabitModal({ habit, open, onClose }) {
    const handleDelete = async () => {
        await api.deleteHabit(habit.id);
        onClose();
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
                        <Button
                            variant="contained"
                            color="error"
                            onClick={handleDelete}
                        >
                            Delete
                        </Button>
                    </Box>
                </Form>
            </Box>
        </Modal>
    );
}
DeleteHabitModal.propTypes = {
    habit: PropTypes.object.isRequired,
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
};
