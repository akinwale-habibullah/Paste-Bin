import { createSlice } from '@reduxjs/toolkit'
import { createAsyncThunk } from '@reduxjs/toolkit';

const initialState = {
  error: null,
  loading: false,
  user: null,
  token: null,
  isAuthenticated: false
};

export const signup = createAsyncThunk(
  'auth/signup',
  async (userObject, thunkAPI) => {
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userObject)
    }

    const response = await fetch('/api/v1/auth/signup', options)

    if (response.status !== 201) {
      return thunkAPI.rejectWithValue(response.statusText)
    }

    return {}
  }
)

export const loginAsync = createAsyncThunk(
  'auth/login',
  async (userObject, thunkAPI) => {
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userObject)
    }

    const response = await fetch('/api/v1/auth/login', options)
    return await response.json()
  }
) 

export const authSlice = createSlice({
  name: 'Auth',
  initialState,
  reducers: {
    login: (state, action) => {
      state.user = action.payload;
      state.isAuthenticated = true;
    },
    logout: (state) => {
      state.user = null;
      state.isAuthenticated = false;
    }
  },
  extraReducers: (builder) => {
    builder.addCase(signup.pending, (state, action) => {
      state.loading = true;
      state.newUserCreated = false;
    })
    builder.addCase(signup.fulfilled, (state, action) => {
      state.error = null;
      state.loading = false;
      state.newUserCreated = true;
    })
    builder.addCase(signup.rejected, (state, action) => {
      state.error = action.payload
      state.loading = false;
      state.newUserCreated = false;
    })

    builder.addCase(loginAsync.pending, (state, action) => {
      state.loading = true
      state.newUserCreated = false
    })
    builder.addCase(loginAsync.fulfilled, (state, action) => {
      state.error = null
      state.loading = false
      state.isAuthenticated = true
      state.user = action.payload.user
      state.token = action.payload.token
    })
    builder.addCase(loginAsync.rejected, (state, action) => {
      state.error = action.payload
      state.loading = false
      state.isAuthenticated = false
      state.user = null
      state.token = action.payload.token
    })
  }
});

export const { login, logout } = authSlice.actions

export default authSlice.reducer
