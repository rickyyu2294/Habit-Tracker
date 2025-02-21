import React, { useEffect, useState } from "react";
import { Divider, Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Toolbar } from "@mui/material";
import { InboxOutlined } from "@mui/icons-material";
import api from "../../services/habit-tracker-api";

const drawerWidth = 240;

export default function Sidebar() {
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
                '& .MuiDrawer-paper': {
                    width: drawerWidth,
                    boxSizing: 'border-box',
                },
            }}
            variant="permanent"
            anchor="left"
        >
            <Toolbar />
            <Divider />
            <List>
                {groups.map((group, index) => (
                    <ListItem key={group.id} disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <InboxOutlined />
                            </ListItemIcon>
                            <ListItemText primary={`${index + 1}. ${group.name}`} />
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
      </Drawer>

    );
}
Sidebar.propTypes = {
};
