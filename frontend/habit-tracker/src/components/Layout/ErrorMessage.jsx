import { Box, Typography } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";

function ErrorMessage({ error }) {
    return (
        <>
            {error && (
                <Box sx={{ marginTop: 2 }}>
                    <Typography variant="h6" color="error">
                        {error}
                    </Typography>
                </Box>
            )}
        </>
    );
}
ErrorMessage.propTypes = {
    error: PropTypes.string,
};

export default ErrorMessage;
