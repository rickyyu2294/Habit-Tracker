import { Container, Paper } from "@mui/material";

function Form({ children }) {
  return (
    <Container maxWidth="xs">
      <Paper elevation={3} sx={{ padding: 4, marginTop: 8 }}>
        {children}
      </Paper>
    </Container>
  );
}

export default Form;
