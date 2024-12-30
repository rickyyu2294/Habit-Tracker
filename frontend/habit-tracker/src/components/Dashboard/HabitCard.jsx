import { format, subDays, subWeeks, subMonths } from "date-fns";
import api from "../../services/habit-tracker-api";
import { useState, useEffect } from "react";
import {
    Box,
    Card,
    CardContent,
    Chip,
    IconButton,
    Typography,
} from "@mui/material";
import DeleteHabitModal from "../Modals/DeleteHabitModal";
import React from "react";
import PropTypes from "prop-types";
import MenuIcon from "@mui/icons-material/Menu";
import HabitCardMenu from "./HabitCardMenu";

export default function HabitCard({ habit, onComplete }) {
    const [completions, setCompletions] = useState(null);
    const [deleteModalOpen, setDeleteModalOpen] = useState(false);
    const [menuAnchorEl, setMenuAnchorEl] = useState(null);
    const menuOpen = Boolean(menuAnchorEl);

    const fetchCompletions = async () => {
        try {
            const response = await api.getCompletions(habit.id, habit.interval);
            setCompletions(response.data);
        } catch (err) {
            console.log(err);
        }
    };

    const getIntervals = (frequency) => {
        const today = new Date();
        switch (frequency.toLowerCase()) {
            case "daily":
                return Array.from({ length: 7 }, (_, i) =>
                    format(subDays(today, i), "yyyy-MM-dd"),
                ).reverse();
            case "weekly":
                return Array.from({ length: 7 }, (_, i) =>
                    format(subWeeks(today, i), "yyyy-'W'ww"),
                ).reverse();
            case "monthly":
                return Array.from({ length: 7 }, (_, i) =>
                    format(subMonths(today, i), "yyyy-MM"),
                ).reverse();
            default:
                return [];
        }
    };

    const intervals = getIntervals(habit.interval);

    const isIntervalComplete = (interval) => {
        // if completions is populated, find if completions map contains a key with this interval
        if (completions && completions.completions) {
            return Object.keys(completions.completions).some(
                (value) => value === interval,
            );
        } else {
            return false;
        }
    };

    const handleMenuClick = (event) => {
        setMenuAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setMenuAnchorEl(null);
    }

    const handleDeleteClicked = () => {
        setDeleteModalOpen(true);
    };

    const handleDeleteModalClose = async () => {
        setDeleteModalOpen(false);
        onComplete();
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
            console.log(err);
        }
    };

    useEffect(() => {
        fetchCompletions();
    }, []);

    return (
        <>
            <Card
                elevation={3}
                sx={{
                    minWidth: 500,
                    margin: "auto",
                    padding: 2,
                    borderRadius: 8,
                    backgroundColor: "#f8f9fa",
                }}
            >
                <CardContent>
                    {/* Action Icons */}
                    <Box display={"flex"} justifyContent={"flex-end"}>
                        <IconButton onClick={handleMenuClick}>
                            <MenuIcon />
                        </IconButton>
                        <HabitCardMenu 
                            menuAnchorEl={menuAnchorEl} 
                            menuOpen={menuOpen} 
                            handleMenuClose={handleMenuClose} 
                            handleDeleteClicked={handleDeleteClicked}
                        />
                    </Box>

                    {/* Title */}
                    <Typography variant="h6" align="center" gutterBottom>
                        {habit.name}
                    </Typography>
                    <Typography
                        variant="subtitle2"
                        align="center"
                        sx={{
                            textTransform: "lowercase",
                            color: "text.secondary",
                        }}
                    >
                        {habit.interval}
                    </Typography>

                    {/* Completion Icons */}
                    <Box display="flex" justifyContent="center" gap={1} mt={2}>
                        {intervals.map((interval, index) => {
                            const isCurrent =
                                interval === intervals[intervals.length - 1];
                            const isComplete = isIntervalComplete(interval);
                            return (
                                <Chip
                                    key={index}
                                    label={
                                        habit.interval.toLowerCase() === "daily"
                                            ? format(
                                                  new Date(interval),
                                                  "EE",
                                              ).charAt(0) // First letter of weekday
                                            : habit.interval.toLowerCase() ===
                                                "weekly"
                                              ? `W${interval.split("W")[1]}` // Week number
                                              : habit.interval.toLowerCase() ===
                                                  "monthly"
                                                ? interval.split("-")[1] // Month name
                                                : ""
                                    }
                                    onClick={() =>
                                        isCurrent &&
                                        toggleCompletion(habit, interval)
                                    }
                                    clickable={isCurrent}
                                    color={isComplete ? "success" : "default"}
                                    sx={{
                                        border: isCurrent
                                            ? "2px solid grey"
                                            : "",
                                        opacity: isCurrent ? 1 : 0.8,
                                        fontWeight: "bold",
                                    }}
                                />
                            );
                        })}
                    </Box>
                </CardContent>
            </Card>
            <DeleteHabitModal
                open={deleteModalOpen}
                onClose={handleDeleteModalClose}
                habit={habit}
                onComplete={onComplete}
            />
        </>
    );
}
HabitCard.propTypes = {
    habit: PropTypes.object.isRequired,
    onComplete: PropTypes.func.isRequired,
};
