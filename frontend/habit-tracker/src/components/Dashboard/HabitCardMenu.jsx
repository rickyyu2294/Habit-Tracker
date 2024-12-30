import { Menu, MenuItem, Typography } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";

export default function HabitCardMenu({menuAnchorEl, menuOpen, handleMenuClose, handleDeleteClicked}) {
    return (
        <Menu anchorEl={menuAnchorEl} open={menuOpen} onClose={handleMenuClose}>
            <MenuItem onClick={handleDeleteClicked}>
                <Typography color="error" variant="body2">
                    Delete
                </Typography>
            </MenuItem>
        </Menu>
    );
}
HabitCardMenu.propTypes = {
    menuAnchorEl: PropTypes.object,
    menuOpen: PropTypes.bool.isRequired,
    handleMenuClose: PropTypes.func.isRequired,
    handleDeleteClicked: PropTypes.func.isRequired,
}