import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    mode: "dark",
    primary: { main: "#5b7cfa" },
  },
  shape: { borderRadius: 10 },
  components: {
    MuiButton: {
      defaultProps: { variant: "contained" },
    },
  },
});

export default theme;


