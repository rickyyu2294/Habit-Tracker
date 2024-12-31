import React from "react";
import Form from "./Form";
import { Box, IconButton, Typography } from "@mui/material";
import PropTypes from "prop-types";
import { intervalToFormattedString } from "../../utils/utils";
import { Add, Remove } from "@mui/icons-material";

export default function CompletionForm({ habit, interval, numCompletions }) {
    return (
        <Form>
            <Typography variant="h5" align="center" gutterBottom>
                <b>{habit.name}</b>
            </Typography>
            <Typography variant="subtitle2" align="center" gutterBottom>
                <b>{intervalToFormattedString(interval, habit.interval)}</b>
            </Typography>
            {habit.frequency === 1 ? (
                <Typography variant="body1" align="center">
                    Complete Task?
                </Typography>
            ) : (
                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                    }}
                >
                    <Box
                        sx={{
                            display: "flex",
                            justifyContent: "center",
                        }}
                    >
                        <Typography variant="h6" align="center">
                            {numCompletions} / {habit.frequency}
                        </Typography>
                    </Box>
                    <Box
                        sx={{
                            display: "flex",
                            justifyContent: "center",
                            gap: 2,
                            marginTop: 2,
                        }}
                    >
                        <IconButton>
                            <Remove color="primary" />
                        </IconButton>
                        <IconButton>
                            <Add color="primary" />
                        </IconButton>
                    </Box>
                </Box>
            )}
        </Form>
    );
}
CompletionForm.propTypes = {
    habit: PropTypes.object.isRequired,
    interval: PropTypes.string.isRequired,
    numCompletions: PropTypes.number.isRequired,
};
