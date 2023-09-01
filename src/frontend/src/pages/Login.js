import React, { useState} from 'react';
import {
  Link,
  TextField,
  Typography,
  Grid,
  Stack,
  Button
} from '@mui/material';
import {
  PublicOutlined,
  Facebook,
  Twitter,
  Google
} from '@mui/icons-material';


function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const handleUsername = e => setUsername(e.target.value);
  const handlePassword = e => setPassword(e.target.value);
  const handleFormSubmit = (e) => {
    e.preventDefault();

    alert('Form submit');
  }
  
  const recentPasteList = [
    {
      name:'Pong',
      metadata: {
        syntax: 'C',
        createdAt: '6 min ago',
        filesize: '3.91KB'
      }
    },
    {
      name:'Add UI on top of a dataframe to let vie..',
      metadata: {
        syntax: 'Python',
        createdAt: '1 hour ago',
        filesize: '0.50KB'
      }
    },
    {
      name:'Untitled',
      metadata: {
        syntax: 'JSON',
        createdAt: '1 hour ago',
        filesize: '0.06KB'
      }
    },
    {
      name:'PALABIA',
      metadata: {
        syntax: 'JSON',
        createdAt: '1 hour ago',
        filesize: '0.06'
      }
    },
    {
      name:'Godot 4 HTML Apache fix',
      metadata: {
        syntax: 'Bash',
        createdAt: '1 hour ago',
        filesize: '3.28'
      }
    },
    {
      name:'Regularly Scheduled Pickup Data',
      metadata: {
        syntax: 'JavaScript',
        createdAt: '1 hour ago',
        filesize: '1.17'
      }
    },
    {
      name:'Basic SysV-Init Script',
      metadata: {
        syntax: 'Bash',
        createdAt: '2 hours ago',
        filesize: '1.00KB'
      }
    },
  ]

  return (
    <Grid container spacing={3} columnSpacing={1}>
      <Grid item container xs={9}>
        <Grid item container xs={12}>
          <Grid item xs={12}>
            <Typography variant='subtitle' gutterBottom={true}>Signup Page </Typography>
          </Grid>
          
          <Grid item container xs={10} justifyContent='space-between'>
            <Grid item>
              <Button variant='outlined' startIcon={<Facebook />} size='small'>Sign in With FaceBook</Button>
            </Grid>
            <Grid item>
              <Button variant='outlined' startIcon={<Twitter />} size='small'>Sign in With Twitter</Button>
            </Grid>
            <Grid item>
              <Button variant='outlined' startIcon={<Google />} size='small'>Sign in With Google</Button>
            </Grid>
          </Grid>
          <Grid item container xs={9} justifyContent='center'>
            <Grid item>
              <Typography variant='body2'>OR</Typography>
            </Grid>
          </Grid>
        </Grid>

        <Grid item container xs={12} spacing={2}>
          <Grid item container xs={8}>
            <form action="" onSubmit={handleFormSubmit} style={{width: '100%'}}>
              <Grid item container alignItems="flex-start" spacing={2}>
                <Grid item xs={3}>
                  <Typography variant="body1"> Username </Typography>
                </Grid>
                <Grid item xs={9}>
                  <TextField
                      id="username"
                      value={username}
                      inputProps={{ 'aria-label': 'username' }}
                      onChange={handleUsername}
                      size='small'
                      fullWidth
                    />
                </Grid>
              </Grid>
              <Grid item container alignItems="flex-start" spacing={2}>
                <Grid item xs={3}>
                  <Typography variant="body1"> Password </Typography>
                </Grid>
                <Grid item xs={9}>
                  <TextField
                      id="password"
                      value={password}
                      inputProps={{ 'aria-label': 'username' }}
                      onChange={handlePassword}
                      size='small'
                      type='password'
                      fullWidth
                    />
                </Grid>
              </Grid>
              <Grid item container alignItems="flex-start" spacing={2}>
                <Grid item xs={3}>
                </Grid>
                <Grid item xs={9}>
                  <Button type='submit' variant='outlined' fullWidth>Login</Button>
                </Grid>
              </Grid>
            </form>
          </Grid>
          <Grid item xs={4}>
            <Typography variant='body1'>Related pages</Typography>
            <Grid item container xs={12} flexDirection={'column'}>
              <Link>Create New Account</Link>
              <Link>Forgot Username</Link>
              <Link>Forgot Password</Link>
              <Link>No Activation Mail?</Link>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
      <Grid item xs={3}>
        <Typography variant='h6'>Public Pastes</Typography>

        <Stack spacing={2}>
          {
            recentPasteList.map((paste) => {
              return (
                <Grid container key={paste.name}>
                  <Grid item xs={1}><PublicOutlined /></Grid>
                  <Grid item container xs={11} >
                    <Grid item xs={12}><Typography variant='body1'>{paste.name}</Typography></Grid>
                    <Grid item container xs={12} spacing={1}>
                      <Grid item>
                        <Typography variant='body2'>{paste.metadata.syntax} |</Typography>
                      </Grid>
                      <Grid item>
                        <Typography variant='body2'>{paste.metadata.createdAt} |</Typography>
                      </Grid>
                      <Grid item>
                        <Typography variant='body2'>{paste.metadata.filesize}</Typography>
                      </Grid>
                    </Grid>
                  </Grid>
                </Grid>
              )
            })
          }
        </Stack>
      </Grid>
    </Grid>
  )
}

export default Login
