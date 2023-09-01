import React from 'react'
import {
  Box,
  Stack,
  Typography,
  IconButton
} from '@mui/material'
import {
  CancelOutlined
} from '@mui/icons-material'

function Cancel({ handleDelete}) {
  return (
    <IconButton sx={{ padding: 0, color: "#ffffff" }} size='small' onClick={handleDelete}>
      <CancelOutlined color={'inherit'}/>
    </IconButton>
  )
}

function Tag({ data, handleDelete }) {
  return (
    <Box
      sx={{
        bgcolor: "#283240",
        height: "100%",
        display: "flex",
        padding: "0.4rem",
        margin: "0 0.5rem 0 0",
        justifyContent: "center",
        alignContent: "center",
        color: "#fff",
        borderRadius: '5%'
      }}
    >
      <Stack direction='row' gap={1} alignItems={'start'}>
        <Typography variant='body2'>{data}</Typography>
        <Cancel sx={{ cursor: "pointer" }} handleDelete={() => handleDelete(data)}/>
      </Stack>
    </Box>
  )
}

export default Tag