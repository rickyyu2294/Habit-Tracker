import React, { useEffect, useState } from "react";
import Form from "./Form";
import { Box, Button, List, ListItemButton, Typography } from "@mui/material";
import PropTypes from "prop-types";
import { ManageGroupMode } from "../../utils/enums";

export default function ManageGroupForm({
    groups,
    onClose,
    setMode,
    setSelectedGroup,
}) {
    return (
        <Form>
            <Typography variant="h5" align="center" gutterBottom>
                <b>Manage Groups</b>
            </Typography>

            <List>
                {groups.map((group) => (
                    <ListItemButton
                        key={group.id}
                        onClick={() => {
                            setSelectedGroup(group.id);
                            setMode(ManageGroupMode.EDIT);
                        }}
                    >
                        <Typography>{group.name}</Typography>
                    </ListItemButton>
                ))}
            </List>

            <Box>
                <Button onClick={() => onClose()}>Close</Button>
                <Button onClick={() => setMode(ManageGroupMode.ADD)}>
                    Add New Group
                </Button>
            </Box>
        </Form>
    );
}
ManageGroupForm.propTypes = {
    groups: PropTypes.array,
    onClose: PropTypes.func,
    setMode: PropTypes.func,
    setSelectedGroup: PropTypes.func,
};
