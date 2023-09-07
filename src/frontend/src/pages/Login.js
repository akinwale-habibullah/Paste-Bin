import React, { useEffect, useReducer } from 'react';
import { useSelector, useDispatch  } from 'react-redux';
import { Navigate } from 'react-router-dom'
import {
  Link,
  TextField,
  Typography,
  Grid,
  Button
} from '@mui/material';
import {
  Facebook,
  Twitter,
  Google
} from '@mui/icons-material';
import { getRecentPastes } from '../app/features/pasteSlice';
import { loginAsync } from '../app/features/authSlice'
import PublicPasteList from '../components/PublicPasteList';

function formReducer (state, action) {
  switch (action.type) {
    case 'setEmail': {
      return {
        ...state,
        email: action.value
      }
    }
    case 'setPassword': {
      return {
        ...state,
        password: action.value
      }
    }
    default:
      throw Error("Unknown action: " + action.type)
  }
}

function Login() {
  const initialFormState = {
    email: '',
    password: '',
  }

  const dispatch = useDispatch()
  const recentPastes = useSelector(state => state.pastes.recentPastes)
  const isAuthenticated = useSelector(state => state.auth.isAuthenticated)
  const [fields, dispatchFormAction] = useReducer(formReducer, initialFormState)

  const validate = () => {
    if (fields.email === '' || fields.password === '') {
      return false
    }

    return true
  }

  const handleChangeEvent = (type, e) => {
    dispatchFormAction({
      type,
      value: e.target.value
    })
  }

  const handleFormSubmit = (e) => {
    e.preventDefault();
    dispatch(loginAsync(fields))
  }

  useEffect(() => {
    dispatch(getRecentPastes())
  }, [dispatch])

  if (isAuthenticated) {
    return <Navigate to="/dashboard" />
  }

  return (
    <>
      <Grid container spacing={3} columnSpacing={4} sx={{ mt: 1 }}>
        <Grid item container xs={8} rowSpacing={3}>
          <Grid item container xs={12}>
            <Grid item xs={12} sx={{ pt: 2 }}>
              <Typography variant='subtitle' gutterBottom={true}>Signup Page </Typography>
              <Typography variant='body1' gutterBottom={true}>Join the Pastebin community with over 4,000,000 members! </Typography>
            </Grid>
            <Grid item container xs={10} justifyContent='space-between'>
              <Grid item>
                <Button variant='contained' startIcon={<Facebook />} size='small'>Sign in With FaceBook</Button>
              </Grid>
              <Grid item>
                <Button variant='contained' startIcon={<Twitter />} size='small'>Sign in With Twitter</Button>
              </Grid>
              <Grid item>
                <Button variant='contained' startIcon={<Google />} size='small'>Sign in With Google</Button>
              </Grid>
            </Grid>
            <Grid item container xs={10} justifyContent='center' sx={{ mt: 2}}>
              <Grid item>
                <Typography variant='body2'>OR</Typography>
              </Grid>
            </Grid>
          </Grid>

          <Grid item container xs={12} spacing={2} justifyContent={'space-between'}>
            <Grid item container xs={7}>
              <form action="" onSubmit={handleFormSubmit} style={{ width: '100%' }}>
                <Grid item container rowGap={1}>
                  <Grid item container alignItems="flex-start" spacing={2}>
                    <Grid item xs={3}>
                      <Typography variant="body1"> Email </Typography>
                    </Grid>
                    <Grid item xs={9}>
                      <TextField
                        id="email"
                        value={fields.email}
                        inputProps={{ 'aria-label': 'email' }}
                        onChange={(e) => handleChangeEvent('setEmail', e)}
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
                        value={fields.password}
                        inputProps={{ 'aria-label': 'username' }}
                        onChange={(e) => handleChangeEvent('setPassword', e)}
                        size='small'
                        type='password'
                        fullWidth
                      />
                    </Grid>
                  </Grid>
                  <Grid item container alignItems="flex-start" spacing={2} >
                    <Grid item xs={3}>
                    </Grid>
                    <Grid item xs={9}>
                      <Button type='submit' variant='contained' fullWidth disabled={!validate()}>Login</Button>
                    </Grid>
                  </Grid>
                </Grid>
              </form>
            </Grid>
            <Grid item xs={4}>
              <Typography variant='bodyline'>Related pages</Typography>
              <Grid item container xs={12} flexDirection={'column'}>
                <Typography variant='body2'>
                  <Link to="/#" style={{textDecoration: 'none' }}>
                    Create New Account
                  </Link>
                </Typography>
                <Typography variant='body2' style={{textDecoration: 'none' }}>
                  <Link to="/#">
                    Forgot Username
                  </Link>
                </Typography>
                <Typography variant='body2' style={{textDecoration: 'none' }}>
                  <Link to="/#">
                    No Activation Mail
                  </Link>
                </Typography>
              </Grid>
            </Grid>
          </Grid>
        </Grid>

        <Grid item xs={4}>
          <PublicPasteList recentPastes={recentPastes} />
        </Grid>
      </Grid>
    </>
  )
}

export default Login
