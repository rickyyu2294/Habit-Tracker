import { Container, Paper } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";

function Form({ children }) {
    return (
        <Container maxWidth="xs">
            <Paper elevation={3} sx={{ padding: 4, marginTop: 8 }}>
                {children}
            </Paper>
        </Container>
    );
}
Form.propTypes = {
    children: PropTypes.node.isRequired,
};

export default Form;
