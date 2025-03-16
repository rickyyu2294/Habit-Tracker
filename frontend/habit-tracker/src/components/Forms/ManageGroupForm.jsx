import React, {  } from "react";
import Form from "./Form";
import { Button, Typography } from "@mui/material";
import PropTypes from "prop-types";

export default function ManageGroupForm({ groups, onClose }) {
    return (
        <Form>
            <Typography variant="h5" align="center" gutterBottom>
                <b>Manage Groups</b>
            </Typography>
            <Typography variant="subtitle2" align="center" gutterBottom>
                Moo
            </Typography>

            <Button onClick={onClose()}>
                Close
            </Button>
        </Form>
    );
}
ManageGroupForm.propTypes = {
    onClose: PropTypes.func
};
