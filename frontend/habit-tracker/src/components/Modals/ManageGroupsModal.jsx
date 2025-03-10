import { Box, Modal, Typography } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";

export default function CompletionModal({ open, onClose, groups }) {
    return (
        <Modal open={open} onClose={onClose}>
            <Box sx={{ display: "flex", flexDirection: "column" }}>
                <Typography>
                    Moo
                </Typography>
                <Typography>
                    {groups.map((group) => {
                        console.log(group.name)
                    })}
                </Typography>
            </Box>
        </Modal>
    );
}
CompletionModal.propTypes = {
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    groups: PropTypes.array.isRequired,
};
