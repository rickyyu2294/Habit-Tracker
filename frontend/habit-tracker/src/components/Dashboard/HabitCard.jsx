import { format, subDays, subWeeks, subMonths } from "date-fns";
import api from "../../services/habit-tracker-api";
import { useState, useEffect } from "react";
import { Box, Card, CardContent, IconButton, Typography } from "@mui/material";
import DeleteHabitModal from "../Modals/DeleteHabitModal";
import React from "react";
import PropTypes from "prop-types";
import MenuIcon from "@mui/icons-material/Menu";
import HabitCardMenu from "./HabitCardMenu";
import HabitCardCompletionChip from "./HabitCardCompletionChip";
import CompletionModal from "../Modals/CompletionModal";

export default function HabitCard({ habit, onComplete }) {
    const [completions, setCompletions] = useState(null);
    const [deleteModalOpen, setDeleteModalOpen] = useState(false);
    const [completionModalOpen, setCompletionModalOpen] = useState(false);
    const [selectedInterval, setSelectedInterval] = useState(null);
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
        if (!completions?.groupedIntervalResponses) {
            return false; // Early return if the groupedIntervalResponses are not available
        }

        // Find the interval in the grouped responses
        const intervalGroup = completions.groupedIntervalResponses.find(
            (group) => group.interval === interval,
        );

        // Check if completions exist for the interval
        return intervalGroup?.completions?.length > 0 || false;
    };

    const handleMenuClick = (event) => {
        setMenuAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setMenuAnchorEl(null);
    };

    const handleDeleteClicked = () => {
        setDeleteModalOpen(true);
        setMenuAnchorEl(null);
    };

    const handleDeleteModalClose = async () => {
        setDeleteModalOpen(false);
        onComplete();
    };

    const handleCompletionChipClick = (interval) => {
        setSelectedInterval(interval);
        setCompletionModalOpen(true);
    };

    const handleCompletionModalClose = async () => {
        setCompletionModalOpen(false);
        onComplete();
    };

    useEffect(() => {
        fetchCompletions();
    }, []);

    return (
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
                            <>
                                <HabitCardCompletionChip
                                    key={index}
                                    interval={interval}
                                    habit={habit}
                                    isCurrent={isCurrent}
                                    isComplete={isComplete}
                                    onClick={() =>
                                        handleCompletionChipClick(interval)
                                    }
                                />
                            </>
                        );
                    })}
                </Box>
            </CardContent>
            {/* Menus */}
            <HabitCardMenu
                menuAnchorEl={menuAnchorEl}
                menuOpen={menuOpen}
                handleMenuClose={handleMenuClose}
                handleDeleteClicked={handleDeleteClicked}
            />

            {/* Modals */}
            <DeleteHabitModal
                open={deleteModalOpen}
                onClose={handleDeleteModalClose}
                habit={habit}
            />

            <CompletionModal
                key={selectedInterval}
                open={completionModalOpen}
                onClose={handleCompletionModalClose}
                habit={habit}
                interval={selectedInterval}
            />
        </Card>
    );
}
HabitCard.propTypes = {
    habit: PropTypes.object.isRequired,
    onComplete: PropTypes.func.isRequired,
};
