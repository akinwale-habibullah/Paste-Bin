import React from 'react'
import moment from 'moment'
import { Link } from 'react-router-dom'
import {
  Grid,
  Box,
  Typography,
  Stack,
  Paper,
  CircularProgress
} from '@mui/material'
import {
  PublicOutlined
} from '@mui/icons-material'

function PublicPasteList({ recentPastes }) {
  return (
    <Paper elevation={3} sx={{ mt: 5, pt: 0, pl: 2 }}>
          <Typography variant='overline' sx={{fontWeight: 'bold'}}>Public Pastes</Typography>

          <Stack 
            spacing={1}
            paddingTop={2}
            paddingBottom={5}
          >
            {
              recentPastes.length != null ?
                recentPastes.map((paste) => {
                  return (
                    <Grid container key={paste.id} alignItems='flex-start'>
                      <Grid item xs={2}>
                        <PublicOutlined color='secondary' />
                      </Grid>
                      <Grid item container xs={10}>
                        <Grid item xs={12}>
                            <Typography variant='body2'>
                              <Link to={`/paste/${paste.id}`} style={{textDecoration: 'none', color: 'inherit' }}>
                                {paste.name[0].toUpperCase() + paste.name.slice(1)}
                              </Link>
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                          <Grid container spacing={1}>
                            <Grid item>
                              <Typography variant='caption'>{paste.syntax ? paste.syntax : 'None'} |</Typography>
                            </Grid>
                            <Grid item>
                              <Typography variant='caption'>{paste.createdOn ? moment(paste.createdOn, "YYYY-MM-DD").fromNow() : "Null"} |</Typography>
                            </Grid>
                            <Grid item>
                              <Typography variant='caption'>{paste.filesize ? paste.filesize : 'Null'}</Typography>
                            </Grid>
                          </Grid>
                        </Grid>
                      </Grid>
                    </Grid>
                  )
                }) :
                <Box sx={{ display: 'flex' }}>
                  <CircularProgress />
                </Box>
            }
          </Stack>
        </Paper>
  )
}

export default PublicPasteList