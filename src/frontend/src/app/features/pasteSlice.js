import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'

const initialState = {
  loading: false,
  recentPastes: [],
  userPastes: []
};

export const createPaste = createAsyncThunk(
  'pastes/createPaste',
  async (pasteObject, thunkAPI) => {
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(pasteObject)
    };

    const response = await fetch('/api/v1/pastes', options)
    return await response.json()
  }
)

export const getRecentPastes = createAsyncThunk(
  'pastes/getRecentPastes',
  async (thunkAPI) => {
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    };

    const response = await fetch('/api/v1/pastes/recentPastes', options)
    return await response.json()
  }
)

export const pasteSlice = createSlice({
  name: 'authentication',
  initialState,
  reducers: {
    setRecentPaste: (state, action) => {
      state.recentPastes = action.payload;
    },
    setUserPastes: (state, action) => {
      state.userPastes = action.payload;
    },
    deletePaste: (state, action) => {
      let pasteId = action.payload;
      console.log('deletePaste ' + pasteId);
    },
    getPaste: (state, action) => {
      let pasteId = action.payload;
      console.log('getPaste ' + pasteId);
    },
  },
  extraReducers: (builder) => {
    builder.addCase(createPaste.pending, (state, action) => {
      state.loading = true;
    })
    builder.addCase(createPaste.fulfilled, (state, action) => {
      state.loading = false;
      state.userPastes.push(action.payload.data);
    })
    builder.addCase(createPaste.rejected, (state, action) => {
      state.loading = false;
    })
    builder.addCase(getRecentPastes.pending, (state, action) => {
      state.loading = true;
    })
    builder.addCase(getRecentPastes.fulfilled, (state, action) => {
      state.loading = false;
      state.recentPastes = action.payload.data;
    })
    builder.addCase(getRecentPastes.rejected, (state, action) => {
      state.loading = false;
    })
  }
});

export const { setRecentPaste, setUserPastes, deletePaste, getPaste } = pasteSlice.actions

export default pasteSlice.reducer
