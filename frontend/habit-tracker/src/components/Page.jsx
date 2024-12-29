import React from "react";
import { Container, Typography, Box } from "@mui/material";

const Page = ({ title, children }) => {
  return (
    <Container maxWidth="lg" sx={{ minHeight: "100vh", py: 4 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          {title}
        </Typography>
      </Box>
      {children}
    </Container>
  );
};

export default Page;
