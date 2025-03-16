import { Box, Modal } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";
import ManageGroupForm from "../Forms/ManageGroupForm";

export default function ManageGroupModal({ open, onClose, groups }) {
    return (
        <Modal open={open} onClose={onClose}>
            <Box display={"flex"}>
                <ManageGroupForm onClose={onClose} groups={groups}/>
            </Box>
        </Modal>
    );
}
ManageGroupModal.propTypes = {
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    groups: PropTypes.array.isRequired,
};
