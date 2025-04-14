import { Box, Modal } from "@mui/material";
import PropTypes from "prop-types";
import React, { useEffect, useState } from "react";
import ManageGroupForm from "../Forms/ManageGroupForm";
import AddGroupForm from "../Forms/AddGroupForm";
import EditGroupForm from "../Forms/EditGroupForm";
import { ManageGroupMode } from "../../utils/enums";

export default function ManageGroupModal({ open, onClose, groups }) {
    const [mode, setMode] = useState(ManageGroupMode.DEFAULT);
    const [selectedGroupId, setSelectedGroupId] = useState(null);

    const modalClose = () => {
        setMode(ManageGroupMode.DEFAULT);
        onClose();
    };

    const renderContent = () => {
        switch (mode) {
            case ManageGroupMode.DEFAULT:
                return  <ManageGroupForm
                onClose={onClose}
                groups={groups}
                setMode={setMode}
                setSelectedGroup={setSelectedGroupId}
            />;
            case ManageGroupMode.ADD:
                return <AddGroupForm />;
            case ManageGroupMode.EDIT:
                return <EditGroupForm selectedGroupId={selectedGroupId} />;
            default:
                console.log("Unknown Mode: " + mode);
        }
    };

    return (
        <Modal open={open} onClose={modalClose}>
            <Box display={"flex"}>{renderContent()}</Box>
        </Modal>
    );
}
ManageGroupModal.propTypes = {
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    groups: PropTypes.array.isRequired,
};
