import React from "react";
import { Card, CardContent, Chip, Stack, Typography } from "@mui/material";

import DeviceService from "../modules/device-service";


export default function Home() {
    const [devices, setDevices] = React.useState([]);

    React.useEffect(() => {
        DeviceService
            .getAllDevices()
            .then((devices: any[]) => setDevices(devices))
            .catch(err => console.log(err));
    }, []);

    return (
        <Stack spacing={2}>
            {devices.map(device => (
                <Card variant="outlined" key={device.id}>
                    <CardContent>
                        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>{device.name}</Typography>
                        <Chip label={device.user} />
                        <div>{JSON.stringify(device.coordinates)}</div>
                    </CardContent>
                </Card>
            ))}
        </Stack>
    );
}