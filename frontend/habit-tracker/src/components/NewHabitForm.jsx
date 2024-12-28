import React, { useState } from "react";
import {
    Box,
    TextField,
    Button,
    Typography,
    Container,
    Paper,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
} from "@mui/material";
import api from "../services/habit-tracker-api";

const NewHabitForm = ({ onClose }) => {
    const [formStep, setFormStep] = useState(1);
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [interval, setInterval] = useState("");
    const [frequency, setFrequency] = useState("");
    const [error, setError] = useState("");

    const validateStep = () => {
        switch (formStep) {
            case 1:
                if (!name) return "Habit Name is required.";
                if (!description) return "Description is required.";
                return "";
            case 2:
                if (!frequency) return "Frequency is required.";
                if (!interval) return "Interval is required.";
                return "";
            default:
                return "";
        }
    };

    const handleNext = () => {
        const validationError = validateStep();
        if (validationError) {
            setError(validationError);
            return;
        }
        setError("");
        setFormStep(formStep + 1);
    };

    const handleBack = () => {
        setError("");
        setFormStep(formStep - 1);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newHabit = { name, description, frequency, interval };
        console.log("New Habit:", newHabit);
        try {
            await api.createHabit(
                name,
                description,
                interval,
                frequency
            )
            onClose();
        } catch (err) {
            setError("Failed to create habit. Please try again.");
            console.error(err);
        }
    };

    const renderStepContent = () => {
        switch (formStep) {
            case 1:
                return (
                    <>
                        <TextField
                            label="Habit Name"
                            type="text"
                            variant="outlined"
                            fullWidth
                            required
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                        <TextField
                            label="Description"
                            type="text"
                            variant="outlined"
                            fullWidth
                            required
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                        />
                    </>
                );
            case 2:
                return (
                    <>
                        <FormControl fullWidth required>
                            <InputLabel id="frequency-select-label">Frequency</InputLabel>
                            <Select
                                labelId="frequency-select-label"
                                id="frequency-select"
                                value={frequency}
                                onChange={(e) => setFrequency(e.target.value)}
                            >
                                {[...Array(30).keys()].map((num) => (
                                    <MenuItem key={num + 1} value={num + 1}>
                                        {num + 1} time{num + 1 > 1 ? "s" : ""}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <FormControl fullWidth required>
                            <InputLabel id="interval-select-label">Interval</InputLabel>
                            <Select
                                labelId="interval-select-label"
                                id="interval-select"
                                value={interval}
                                onChange={(e) => setInterval(e.target.value)}
                            >
                                <MenuItem value={"DAILY"}>Per Day</MenuItem>
                                <MenuItem value={"WEEKLY"}>Per Week</MenuItem>
                                <MenuItem value={"MONTHLY"}>Per Month</MenuItem>
                            </Select>
                        </FormControl>
                    </>
                );
            default:
                return null;
        }
    };

    return (
        <Container maxWidth="xs">
            <Paper elevation={3} sx={{ padding: 4, marginTop: 8 }}>
                <Typography variant="h5" component="h1" gutterBottom>
                    New Habit
                </Typography>
                {error && (
                    <Typography color="error" variant="body2" gutterBottom>
                        {error}
                    </Typography>
                )}
                <form onSubmit={handleSubmit}>
                    <Box display="flex" flexDirection="column" gap={2}>
                        {renderStepContent()}
                        <Box display="flex" justifyContent="space-between">
                            {formStep > 1 ? (
                                <Button variant="outlined" onClick={handleBack}>
                                    Back
                                </Button>
                            ) : (
                                <Button variant="outlined" color="secondary" onClick={onClose}>
                                    Cancel
                                </Button>
                            )}
                            {formStep < 2 ? (
                                <Button variant="contained" color="primary" onClick={handleNext}>
                                    Next
                                </Button>
                            ) : (
                                <Button type="submit" variant="contained" color="primary">
                                    Submit
                                </Button>
                            )}
                        </Box>
                    </Box>
                </form>
            </Paper>
        </Container>
    );
};

export default NewHabitForm;
