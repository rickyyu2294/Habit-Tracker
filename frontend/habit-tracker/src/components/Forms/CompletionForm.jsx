import React, { useCallback, useEffect, useState } from "react";
import Form from "./Form";
import { Box, Button, IconButton, Typography } from "@mui/material";
import PropTypes from "prop-types";
import { intervalToFormattedString } from "../../utils/utils";
import { Add, Remove } from "@mui/icons-material";
import api from "../../services/habit-tracker-api";

export default function CompletionForm({ habit, interval }) {
    const [numCompletions, setNumCompletions] = useState(0);

    const fetchCompletions = useCallback(async () => {
        try {
            // fetch completions
            const response = await api.getCompletions(habit.id, habit.interval);
            const groupedIntervalResponse =
                response.data.groupedIntervalResponses.find(
                    (group) => group.interval === interval,
                );
            setNumCompletions(groupedIntervalResponse?.completions.length || 0);
        } catch (err) {
            console.error(err);
        }
    }, [habit.id, habit.interval]);

    const handleIncrementCompletion = async () => {
        await api.createCompletionInInterval(habit.id, interval);
        fetchCompletions();
        console.log(`Incrementing ${habit.name} completions in ${interval}`);
    };

    const handleDecrementCompletion = async () => {
        await api.deleteLatestCompletionInInterval(habit.id, interval);
        fetchCompletions();
        console.log(`Decrementing ${habit.name} completions in ${interval}`);
    };

    const isComplete = () => {
        return numCompletions === habit.frequency;
    };

    const handleCompletionToggle = () => {
        if (isComplete()) {
            handleDecrementCompletion();
        } else {
            handleIncrementCompletion();
        }
    };

    useEffect(() => {
        fetchCompletions();
    }, [fetchCompletions]);

    return (
        <Form>
            <Typography variant="h5" align="center" gutterBottom>
                <b>{habit.name}</b>
            </Typography>
            <Typography variant="subtitle2" align="center" gutterBottom>
                <b>{intervalToFormattedString(interval, habit.interval)}</b>
            </Typography>
            {habit.frequency === 1 ? (
                // Single Frequency
                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        mt: 2,
                    }}
                >
                    <Button
                        variant="contained"
                        color={isComplete() ? "success" : "error"}
                        onClick={handleCompletionToggle}
                    >
                        {isComplete() ? "Complete" : "Incomplete"}
                    </Button>
                </Box>
            ) : (
                // Multi Frequency
                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        mt: 2,
                    }}
                >
                    <Box
                        sx={{
                            display: "flex",
                            justifyContent: "center",
                        }}
                    >
                        <Typography
                            variant="h6"
                            align="center"
                            color={
                                numCompletions === habit.frequency
                                    ? "success"
                                    : "text.primary"
                            }
                        >
                            {numCompletions} / {habit.frequency}
                        </Typography>
                    </Box>
                    <Box
                        sx={{
                            display: "flex",
                            justifyContent: "center",
                            gap: 2,
                        }}
                    >
                        <IconButton
                            onClick={handleDecrementCompletion}
                            disabled={numCompletions === 0}
                        >
                            <Remove />
                        </IconButton>
                        <IconButton
                            onClick={handleIncrementCompletion}
                            disabled={numCompletions === habit.frequency}
                        >
                            <Add />
                        </IconButton>
                    </Box>
                </Box>
            )}
        </Form>
    );
}
CompletionForm.propTypes = {
    habit: PropTypes.object.isRequired,
    interval: PropTypes.string.isRequired,
};
