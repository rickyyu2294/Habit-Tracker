import React, { useEffect, useState } from "react";
import {
    Button,
    Divider,
    Drawer,
    List,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography,
} from "@mui/material";
import DashboardIcon from "@mui/icons-material/Dashboard";
import SettingsIcon from "@mui/icons-material/Settings";
import api from "../../services/habit-tracker-api";
import PropTypes from "prop-types";

const drawerWidth = 360;

export default function Sidebar({ selectedGroup, onGroupSelect }) {
    const [groups, setGroups] = useState([]);

    const fetchHabitGroups = async () => {
        try {
            const response = await api.getHabitGroups();
            setGroups(response.data);

            // set 'all' group to selectedby default
            const allGroup = response.data.find(
                (group) => group.name === "All",
            );
            if (!selectedGroup) {
                onGroupSelect(allGroup);
            }
        } catch (err) {
            console.error(err);
        }
    };

    const openManageHabitGroupsModal = () => {
        // set openManageHabitGroupsModal to true
        console.log("Open manage habit groups modal")
    };

    useEffect(() => {
        fetchHabitGroups();
    }, [selectedGroup]);

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
            <Toolbar
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                }}
            >
                <Typography variant="h6">Habit Groups</Typography>
                <Button
                    onClick={() => openManageHabitGroupsModal()}
                >
                    <SettingsIcon color="action"/>
                </Button>
            </Toolbar>
            <List>
               
            </List>
            <Divider />
            <List>
                {groups.map((group) => (
                    <ListItem key={group.id} disablePadding>
                        <ListItemButton
                            onClick={() => onGroupSelect(group)}
                            sx={{
                                backgroundColor:
                                    (selectedGroup?.id ?? false) === group.id
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
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
        </Drawer>
    );
}
Sidebar.propTypes = {
    selectedGroup: PropTypes.object,
    onGroupSelect: PropTypes.func,
};
