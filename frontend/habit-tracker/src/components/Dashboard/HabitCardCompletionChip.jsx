import { Button } from "@mui/material";
import { format } from "date-fns";
import PropTypes from "prop-types";
import React from "react";
import { CompletionStatus } from "../../utils/enums";

export default function HabitCardCompletionChip({
    habit,
    interval,
    completionStatus,
    isCurrent,
    onClick,
}) {
    const getColor = (status) => {
        switch (status) {
            case CompletionStatus.COMPLETE:
                return "success";
            case CompletionStatus.PARTIAL:
                return "warning";
            case CompletionStatus.INCOMPLETE:
                return "error";
            default:
                return "default";
        }
    };

    const getVariant = (status) => {
        switch (status) {
            case CompletionStatus.COMPLETE:
                return "contained";
            case CompletionStatus.PARTIAL:
                return "contained";
            case CompletionStatus.INCOMPLETE:
                return "outlined";
            default:
                return "text";
        }
    };

    return (
        <Button
            onClick={onClick}
            color={getColor(completionStatus)}
            variant={getVariant(completionStatus)}
            sx={
                { 
                    border: isCurrent ? "2px solid" : "",
                    borderRadius: 200
                }
            }
        >
            {habit.interval.toLowerCase() === "daily"
                ? format(new Date(interval), "EE").charAt(0) // First letter of weekday
                : habit.interval.toLowerCase() === "weekly"
                  ? `W${interval.split("W")[1]}` // Week number
                  : habit.interval.toLowerCase() === "monthly"
                    ? interval.split("-")[1] // Month name
                    : ""}
        </Button>
    );
}
HabitCardCompletionChip.propTypes = {
    habit: PropTypes.object.isRequired,
    interval: PropTypes.string.isRequired,
    completionStatus: PropTypes.string.isRequired,
    isCurrent: PropTypes.bool.isRequired,
    onClick: PropTypes.func.isRequired,
};
