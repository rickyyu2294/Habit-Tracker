import { format, subDays, subWeeks, subMonths } from "date-fns";
import api from "../services/habit-tracker-api";
import { useState, useEffect } from "react";
import HabitCardCompletionIcon from "./HabitCardCompletionIcon";
import { Box, Card, CardContent, Chip, Typography } from "@mui/material";

function HabitCard({ habit, onComplete }) {
    const [completions, setCompletions] = useState(null)

    const fetchCompletions = async () => {
        try {
            const response = await api.getCompletions(habit.id, habit.interval);
            setCompletions(response.data)
        } catch (err) {
            console.log(err)
        }
    };

    const getIntervals = (frequency) => {
        const today = new Date();
        switch (frequency.toLowerCase()) {
            case "daily":
                return Array.from(
                    { length: 7 }, (_, i) => format(subDays(today, i), "yyyy-MM-dd")
                ).reverse();
            case "weekly":
                return Array.from(
                    { length: 7 }, (_, i) => format(subWeeks(today, i), "yyyy-'W'ww")
                ).reverse();
            case "monthly":
                return Array.from(
                    { length: 7 }, (_, i) => format(subMonths(today, i), "yyyy-MM")
                ).reverse();
            default:
                return [];
        }
    }

    const intervals = getIntervals(habit.interval);

    const isIntervalComplete = (interval) => {
        // if completions is populated, find if completions map contains a key with this interval
        if (completions && completions.completions) {
            return Object.keys(completions.completions).some((value) => value === interval);
        } else {
            return false
        }
    };

    const toggleCompletion = async (habit, date) => {
        // todo: change this from toggling to opening a dialog to complete or delete

        const isComplete = isIntervalComplete(date);
        const habitId = habit.id;
        try {
            if (isComplete) {
                await api.deleteCompletion(habitId, date);
            } else {
                await api.markCompletion(habitId, date);
            }
            const response = await api.getCompletions(habitId);
            setCompletions(response.data || []);
            onComplete();
        } catch (err) {
            console.log(err)
        }
    }

    useEffect(() => {
        fetchCompletions();
    }, [])

    return (
        <Card
            elevation={3}
            sx={{
                minWidth: 500,
                margin: "auto",
                padding: 2,
                borderRadius: 8,
                backgroundColor: "#f8f9fa",
            }}>
            <CardContent>
                <Typography variant="h6" align="center" gutterBottom>
                    {habit.name}
                </Typography>
                <Typography
                    variant="subtitle2"
                    align="center"
                    sx={{ textTransform: "capitalize", color: "text.secondary" }}
                >
                    {habit.interval}
                </Typography>
                <Box display="flex" justifyContent="center" gap={1} mt={2}>
                    {intervals.map((interval, index) => {
                        const isCurrent = interval === intervals[intervals.length - 1];
                        const isComplete = isIntervalComplete(interval)
                        return (
                            <Chip
                                key={index}
                                label={
                                    habit.interval.toLowerCase() === "daily"
                                        ? format(new Date(interval), "EE").charAt(0) // First letter of weekday
                                        : habit.interval.toLowerCase() === "weekly"
                                            ? `W${interval.split("W")[1]}` // Week number
                                            : habit.interval.toLowerCase() === "monthly"
                                                ? interval.split("-")[1] // Month name
                                                : ""
                                }
                                onClick={() => isCurrent && toggleCompletion(habit, interval)}
                                clickable={isCurrent}
                                sx={{
                                    border: isCurrent ? "2px solid grey" : "",
                                    backgroundColor: isComplete ? "green" : "grey.300",
                                    color: isComplete ? "white" : "grey.800",
                                    opacity: isCurrent ? 1 : 0.8,
                                    fontWeight: "bold",
                                }}
                            />
                        );
                    })}
                </Box>
            </CardContent>
        </Card>
    )
}

export default HabitCard