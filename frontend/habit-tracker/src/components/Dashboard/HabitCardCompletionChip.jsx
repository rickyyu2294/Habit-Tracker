import { Button } from "@mui/material";
import { format } from "date-fns";
import PropTypes from "prop-types";
import React from "react";
import { CompletionStatus, IntervalType } from "../../utils/enums";

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

    const getIntervalShort = () => {
        let result = "";
        switch (habit.interval.toLowerCase()) {
            case IntervalType.DAILY: {
                const date = new Date(`${interval}T00:00:00`);
                result = format(date, "EE").charAt(0);
                break;
            }
            case IntervalType.WEEKLY: {
                result = `W${interval.split("W")[1]}`;
                break;
            }
            case IntervalType.MONTHLY: {
                result = interval.split("-")[1];
                break;
            }
        }
        return result;
    };

    return (
        <Button
            onClick={onClick}
            color={getColor(completionStatus)}
            variant={getVariant(completionStatus)}
            sx={{
                border: isCurrent ? "2px solid" : "",
                borderRadius: 200,
            }}
        >
            {getIntervalShort()}
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
