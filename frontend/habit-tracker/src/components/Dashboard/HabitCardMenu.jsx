import { Menu, MenuItem, Typography } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";

export default function HabitCardMenu({
    menuAnchorElement,
    menuOpen,
    handleMenuClose,
    handleDeleteClicked,
}) {
    return (
        <Menu anchorEl={menuAnchorElement} open={menuOpen} onClose={handleMenuClose}>
            <MenuItem onClick={handleDeleteClicked}>
                <Typography color="error" variant="body2">
                    Delete
                </Typography>
            </MenuItem>
        </Menu>
    );
}
HabitCardMenu.propTypes = {
    menuAnchorElement: PropTypes.object,
    menuOpen: PropTypes.bool.isRequired,
    handleMenuClose: PropTypes.func.isRequired,
    handleDeleteClicked: PropTypes.func.isRequired,
};
