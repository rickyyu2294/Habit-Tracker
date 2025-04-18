import React from "react";
import { Container, Typography, Box } from "@mui/material";
import PropTypes from "prop-types";

export default function Page({ title, children }) {
    return (
        <Container
            maxWidth="lg"
            sx={{
                minHeight: "100vh",
                py: 4,
                display: "flex",
                flexDirection: "column",
                alignContent: "center",
            }}
        >
            <Box sx={{ mb: 4, textAlign: "center" }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    {title}
                </Typography>
            </Box>
            {children}
        </Container>
    );
}
Page.propTypes = {
    title: PropTypes.string.isRequired,
    children: PropTypes.node.isRequired,
};
