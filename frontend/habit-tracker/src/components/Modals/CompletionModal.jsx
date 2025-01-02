import { Box, Modal, Typography } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";
import CompletionForm from "../Forms/CompletionForm";

export default function CompletionModal({ open, onClose, habit, interval }) {
    return (
        <Modal open={open} onClose={onClose}>
            <Box sx={{ display: "flex", flexDirection: "column" }}>
                {interval ? (
                    <CompletionForm habit={habit} interval={interval} />
                ) : (
                    <Typography
                        variant="body1"
                        align="center"
                        color="textSecondary"
                    >
                        No interval selected.
                    </Typography>
                )}
            </Box>
        </Modal>
    );
}
CompletionModal.propTypes = {
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    habit: PropTypes.object.isRequired,
    interval: PropTypes.string,
};
