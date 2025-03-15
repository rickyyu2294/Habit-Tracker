import React, {  } from "react";
import Form from "./Form";
import { Typography } from "@mui/material";
import PropTypes from "prop-types";

export default function ManageGroupForm({ onClose }) {
    return (
        <Form>
            <Typography variant="h5" align="center" gutterBottom>
                <b>Manage Groups</b>
            </Typography>
            <Typography variant="subtitle2" align="center" gutterBottom>
                Moo
            </Typography>
        </Form>
    );
}
ManageGroupForm.propTypes = {
    habit: PropTypes.object.isRequired,
    interval: PropTypes.string.isRequired,
};
