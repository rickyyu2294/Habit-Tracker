import { Box, Modal, Typography } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";
import ManageGroupForm from "../Forms/ManageGroupForm";

export default function ManageGroupModal({ open, onClose }) {
    return (
        <Modal open={open} onClose={onClose}>
            <Box display={"flex"}>
                <ManageGroupForm onClose={onClose} />
            </Box>
        </Modal>
    );
}
ManageGroupModal.propTypes = {
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    groups: PropTypes.array.isRequired,
};
