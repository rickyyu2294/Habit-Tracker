import { format, subDays, subMonths, subWeeks } from "date-fns";
import { CompletionStatus } from "./enums";

export function getCurrentDate() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0"); // Months are 0-indexed
    const day = String(today.getDate()).padStart(2, "0");

    const localDateString = `${year}-${month}-${day}`;
    return localDateString;
}

/**
 * Takes interval and interval type and returns a formatted string
 * ex. interval "2024-W01" and intervalType "weekly" returns "Week 1 of 2024"
 * interval "2024-02" and intervalType "monthly" returns "February 2024"
 * @param {*} interval
 * @param {*} intervalType
 * @returns
 */
export function intervalToFormattedString(interval, intervalType) {
    if (intervalType.toLowerCase() === "daily") {
        return interval;
    } else if (intervalType.toLowerCase() === "weekly") {
        return `Week ${interval.split("W")[1]} of ${interval.split("-W")[0]}`;
    } else if (intervalType.toLowerCase() === "monthly") {
        return (
            monthNumToName(Number(interval.split("-")[1])) +
            " " +
            interval.split("-")[0]
        );
    } else {
        return "";
    }
}

export function monthNumToName(num) {
    switch (num) {
        case 1:
            return "January";
        case 2:
            return "February";
        case 3:
            return "March";
        case 4:
            return "April";
        case 5:
            return "May";
        case 6:
            return "June";
        case 7:
            return "July";
        case 8:
            return "August";
        case 9:
            return "September";
        case 10:
            return "October";
        case 11:
            return "November";
        case 12:
            return "December";
        default:
            return "";
    }
}

/**
 *
 * @param {*} length
 * @param {*} frequency
 * @returns
 */
export const getIntervals = (length, frequency) => {
    const today = new Date();
    switch (frequency.toLowerCase()) {
        case "daily":
            return Array.from({ length: length }, (_, i) =>
                format(subDays(today, i), "yyyy-MM-dd"),
            ).reverse();
        case "weekly":
            return Array.from({ length: length }, (_, i) =>
                format(subWeeks(today, i), "YYYY-'W'ww", {
                    useAdditionalWeekYearTokens: true,
                }),
            ).reverse();
        case "monthly":
            return Array.from({ length: length }, (_, i) =>
                format(subMonths(today, i), "yyyy-MM"),
            ).reverse();
        default:
            return [];
    }
};

/**
 *
 * @param {*} habit
 * @param {*} completions
 * @param {*} interval
 * @returns
 */
export const getCompletionStatus = (habit, completions, interval) => {
    if (!completions?.groupedIntervalResponses) {
        return CompletionStatus.INCOMPLETE; // Early return if the groupedIntervalResponses are not available
    }

    // Find the interval in the grouped responses
    const intervalGroup = completions.groupedIntervalResponses.find(
        (group) => group.interval === interval,
    );

    // Check if completions exist for the interval
    const completionCount = intervalGroup?.completions?.length || 0;

    if (completionCount == habit.frequency) {
        return CompletionStatus.COMPLETE;
    } else if (completionCount == 0) {
        return CompletionStatus.INCOMPLETE;
    } else {
        return CompletionStatus.PARTIAL;
    }
};
