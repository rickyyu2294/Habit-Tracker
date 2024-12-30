import { Chip } from "@mui/material";
import { format } from "date-fns";
import PropTypes from "prop-types";
import React from "react";

export default function HabitCardCompletionChip({
    habit,
    interval,
    isComplete,
    isCurrent,
}) {
    return (
        <Chip
            label={
                habit.interval.toLowerCase() === "daily"
                    ? format(new Date(interval), "EE").charAt(0) // First letter of weekday
                    : habit.interval.toLowerCase() === "weekly"
                      ? `W${interval.split("W")[1]}` // Week number
                      : habit.interval.toLowerCase() === "monthly"
                        ? interval.split("-")[1] // Month name
                        : ""
            }
            onClick={() => isCurrent }
            // implement click to open completion dialog
            // && toggleCompletion(habit, interval)}
            clickable={isCurrent}
            color={isComplete ? "success" : "default"}
            sx={{
                border: isCurrent ? "2px solid grey" : "",
                opacity: isCurrent ? 1 : 0.8,
                fontWeight: "bold",
            }}
        />
    );
}
HabitCardCompletionChip.propTypes = {
    habit: PropTypes.object.isRequired,
    interval: PropTypes.string.isRequired,
    isComplete: PropTypes.bool.isRequired,
    isCurrent: PropTypes.bool.isRequired
}