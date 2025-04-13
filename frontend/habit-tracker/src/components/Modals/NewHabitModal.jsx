import React from "react";
import PropTypes from "prop-types";
import { Box, Modal } from "@mui/material";
import NewHabitForm from "../Forms/NewHabitForm";

export default function NewHabitModal({ open, onClose }) {
    return (
        <Modal open={open} onClose={onClose}>
            <Box display={"flex"}>
                <NewHabitForm onClose={onClose} />
            </Box>
        </Modal>
    );
}
NewHabitModal.propTypes = {
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
};
