import api from "../../services/habit-tracker-api";
import { useState, useEffect, useCallback } from "react";
import { Box, Card, CardContent, IconButton, Typography } from "@mui/material";
import DeleteHabitModal from "../Modals/DeleteHabitModal";
import React from "react";
import PropTypes from "prop-types";
import MenuIcon from "@mui/icons-material/Menu";
import HabitCardMenu from "./HabitCardMenu";
import HabitCardCompletionChip from "./HabitCardCompletionChip";
import CompletionModal from "../Modals/CompletionModal";
import { getCompletionStatus, getIntervals } from "../../utils/utils";

const NUM_INTERVALS = 7;

export default function HabitCard({ habit, onComplete }) {
    const [completions, setCompletions] = useState(null);
    const [deleteModalOpen, setDeleteModalOpen] = useState(false);
    const [completionModalOpen, setCompletionModalOpen] = useState(false);
    const [selectedInterval, setSelectedInterval] = useState(null);
    const [menuAnchorElement, setMenuAnchorEl] = useState(null);
    const menuOpen = Boolean(menuAnchorElement);

    const fetchCompletions = useCallback(async () => {
        try {
            const response = await api.getCompletions(habit.id, habit.interval);
            setCompletions(response.data);
        } catch (err) {
            console.log(err);
        }
    }, [habit.id, habit.interval]);

    const intervals = getIntervals(NUM_INTERVALS, habit.interval);

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
        fetchCompletions();
        onComplete();
    };

    useEffect(() => {
        fetchCompletions();
    }, [fetchCompletions]);

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

                <Box display="flex" justifyContent="space-between" alignItems="center">
                    <Box display="flex" flexDirection="column" alignItems="center" flexGrow="1">
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
                    </Box>

                    <IconButton onClick={handleMenuClick}>
                        <MenuIcon />
                    </IconButton>
                </Box>

                {/* Title */}

                {/* Completion Icons */}
                <Box display="flex" justifyContent="center" gap={1} mt={2}>
                    {intervals.map((interval) => {
                        const isCurrent =
                            interval === intervals[intervals.length - 1];
                        const completionStatus = getCompletionStatus(
                            habit,
                            completions,
                            interval,
                        );
                        return (
                            <HabitCardCompletionChip
                                key={interval}
                                interval={interval}
                                habit={habit}
                                isCurrent={isCurrent}
                                completionStatus={completionStatus}
                                onClick={() =>
                                    handleCompletionChipClick(interval)
                                }
                            />
                        );
                    })}
                </Box>
            </CardContent>
            {/* Menus */}
            <HabitCardMenu
                menuAnchorElement={menuAnchorElement}
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
