import React, { useEffect } from "react";
import Form from "./Form";
import {
    Box,
    Button,
    List,
    ListItem,
    ListItemButton,
    Typography,
} from "@mui/material";
import PropTypes from "prop-types";

export default function ManageGroupForm({ groups, onClose }) {
    console.log(groups.length);

    return (
        <Form>
            <Typography variant="h5" align="center" gutterBottom>
                <b>Manage Groups</b>
            </Typography>

            <List>
                {groups.map((group) => (
                    <ListItemButton key={group.id}>
                        <Typography>{group.name}</Typography>
                    </ListItemButton>
                ))}
            </List>

            <Box>
                <Button onClick={() => onClose()}>Close</Button>
                <Button onClick={() => console.log("Add new group")}>Add New Group</Button>
            </Box>
        </Form>
    );
}
ManageGroupForm.propTypes = {
    groups: PropTypes.array,
    onClose: PropTypes.func,
};
