import React, { useEffect, useState } from "react";
import {
    Divider,
    Drawer,
    List,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
} from "@mui/material";
import DashboardIcon from "@mui/icons-material/Dashboard";
import MenuIcon from "@mui/icons-material/Menu";
import api from "../../services/habit-tracker-api";
import PropTypes from "prop-types";

const drawerWidth = 240;

export default function Sidebar({ selectedGroup, onGroupSelect }) {
    const [groups, setGroups] = useState([]);

    const fetchHabitGroups = async () => {
        try {
            const response = await api.getHabitGroups();
            setGroups(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchHabitGroups();
    }, []);

    return (
        <Drawer
            sx={{
                width: drawerWidth,
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                    width: drawerWidth,
                    boxSizing: "border-box",
                },
            }}
            variant="permanent"
            anchor="left"
        >
            <Toolbar />
            <Divider />
            <List>
                {groups.map((group) => (
                    <ListItem key={group.id} disablePadding>
                        <ListItemButton
                            onClick={() => onGroupSelect(group.id)}
                            sx={{
                                backgroundColor:
                                    selectedGroup === group.id
                                        ? "#eeeeee"
                                        : "transparent",
                                "&:hover": {
                                    backgroundColor: "#e0e0e0",
                                },
                            }}
                        >
                            <ListItemIcon>
                                <DashboardIcon />
                            </ListItemIcon>
                            <ListItemText primary={group.name} />
                            <ListItemIcon>
                                <MenuIcon />
                            </ListItemIcon>
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
        </Drawer>
    );
}
Sidebar.propTypes = {
    selectedGroup: PropTypes.number,
    onGroupSelect: PropTypes.func,
};
