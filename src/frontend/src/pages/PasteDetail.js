import React, { useEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { useParams } from 'react-router-dom'
import {
  Switch,
  TextField,
  Grid,
  FormControlLabel,
} from '@mui/material'

import { getRecentPastes, getPasteById } from '../app/features/pasteSlice'
import PublicPasteList from '../components/PublicPasteList'

function PasteDetail({ fields = {} }) {
  const dispatch = useDispatch()
  const { id } = useParams()
  const recentPastes = useSelector(state => state.pastes.recentPastes)
  const paste = useSelector(state => state.pastes.paste)

  useEffect(() => {
    dispatch(getRecentPastes())
    dispatch(getPasteById(id))
  },[dispatch, id, paste])

  return (
    <Grid container spacing={3} columnSpacing={4} sx={{ mt: 1 }}>
      <Grid item container xs={9} rowSpacing={3}>
        {/* Text editor */}
        <Grid item container pr={1} rowSpacing={1}>
          <Grid item container xs={12} justifyContent={'end'} pr={1}>
            <FormControlLabel
              labelPlacement='start'
              label='Syntax Highlighting'
              slotProps={{
                typography: {
                  variant: 'overline',
                }
              }}
              control={
                <Switch
                  name='syntaxHighlight'
                  checked={fields.syntaxHighlight}
                  value={fields.syntaxHighlight}
                  size='small'
                  color={'secondary'}
                />
              }
            />
          </Grid>

          <Grid item container xs={12}>
            <TextField
              id="paste"
              name="paste"
              multiline
              rows={40}
              variant="filled"
              fullWidth
              value={paste && paste.content ? paste.content : ''}
            />
          </Grid>
        </Grid>
      </Grid>

      <Grid item xs={3}>
        <PublicPasteList recentPastes={recentPastes} />
      </Grid>
    </Grid>
  )
}

export default PasteDetail
