import React, { useEffect, useReducer } from 'react'
import { useSelector, useDispatch } from 'react-redux';
import moment from 'moment'
import { Navigate } from 'react-router-dom'
import {
  Switch,
  Avatar,
  Checkbox,
  TextField,
  Typography,
  Grid,
  Select,
  MenuItem,
  FormControl,
  FormControlLabel,
  Button,
  Autocomplete
} from '@mui/material'
import {
  Facebook,
  Twitter,
  Google,
  AccountBoxOutlined
} from '@mui/icons-material'
import { createPaste, getRecentPastes } from '../app/features/pasteSlice'
import TagInput from '../components/TagInput'

import categories from '../constants/categories'
import expiryOptions from '../constants/expiryOptions'
import syntaxList from '../constants/syntax'
import PublicPasteList from '../components/PublicPasteList';

function formReducer (fields, action) {
  switch (action.type) {
    case 'setContent': {
      return {
        ...fields,
        content: action.value
      }
    }
    case 'setPassword': {
      return {
        ...fields,
        password: action.value
      }
    }
    case 'setCategory': {
      return {
        ...fields,
        category: action.value
      }
    }
    case 'setExpiresOn': {
      let [value, type] = action.value.split(' ')
      type = type.toLowerCase()

      return {
        ...fields,
        expiresOn: moment().add(value, type).format('YYYY-MM-DD HH:mm:ss'),
        expiry: action.value
      }
    }
    case 'setExposure': {
      return {
        ...fields,
        exposure: action.value
      }
    }
    case 'setPasswordEnabled': {
      return {
        ...fields,
        passwordEnabled: !fields.passwordEnabled,
        password: "" 
      }
    }
    case 'setBurnAfterRead': {
      return {
        ...fields,
        burnAfterRead: !fields.burnAfterRead
      }
    }
    case 'setPasteName': {
      return {
        ...fields,
        pasteName: action.value
      }
    }
    case 'setSyntaxHighlight': {
      return {
        ...fields,
        syntaxHighlight: !fields.syntaxHighlight
      }
    }
    case 'setSyntax': {
      return {
        ...fields,
        syntax: action.value
      }
    }
    case 'setTags': {
      return {
        ...fields,
        tags: [...fields.tags, action.value]
      }
    }
    case 'deleteTag': {
      return {
        ...fields,
        tags: fields.tags.filter(tag => tag !== action.value)
      }
    }
    case 'setFolder': {
      return {
        ...fields,
        folder: action.value
      }
    }
    case 'clearForm': {
      return {
        pasteName: '',
        content: '',
        category: 'None',
        passwordEnabled: false,
        password: 'password',
        tags: [],
        syntaxHighlight: false,
        syntax: null,
        expiresOn: '',
        expiry: '',
        exposure: 'public',
        burnAfterRead: false,
        folder: 'none'
      }
    }
    default:
      throw Error("Unknown action: " + action.type)
  }
}

function Home() {
  const initialFormState = {
    pasteName: '',
    content: '',
    category: 'None',
    passwordEnabled: false,
    password: 'password',
    tags: [],
    syntaxHighlight: false,
    syntax: null,
    expiresOn: '',
    expiry: '',
    exposure: 'public',
    burnAfterRead: false,
    folder: 'none'
  }

  const dispatch = useDispatch()
  const auth = useSelector(state => state.auth);
  const recentPastes = useSelector(state => state.pastes.recentPastes);
  const paste = useSelector(state => state.pastes.paste);
  const [fields, dispatchReducerAction] = useReducer(formReducer, initialFormState)

  useEffect(() => {
    dispatch(getRecentPastes())
  },[dispatch])

  const validate = () => {
    if (
      (fields.pasteName === '' || fields.pasteName == null) ||
      (fields.content === '' || fields.content == null)
    ) {
      return false;
    }

    return true;
  }

  const handleFormSubmit = (e) => {
    e.preventDefault()

    let pasteObject = {
      name: fields.pasteName,
      content: fields.content,
      password: fields.password,
      tags: fields.tags,
      category: fields.category ? fields.category.toUpperCase() : "NONE",
      syntax: fields.syntax && fields.syntax.name,
      expiresOn: fields.expiresOn,
      exposure: fields.exposure ? fields.exposure.toUpperCase() : "PUBLIC",
      burnAfterRead: fields.burnAfterRead,
      // folder,
    };

    // send network request
    dispatch(createPaste(pasteObject))
    
    // clear form
    dispatchReducerAction({ type: 'clearForm' })
  }

  if (Object(paste).hasOwnProperty('id') ) {
    return <Navigate replace to={`/paste/${paste.id}`} />
  }

  return (
    <Grid container spacing={3} columnSpacing={4} sx={{ mt: 1}}>
      <Grid item container xs={8} rowSpacing={3}>
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
                  onChange={() => dispatchReducerAction({
                    type: 'setSyntaxHighlight'
                  })}
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
              label="Paste"
              multiline
              rows={20}
              variant="filled"
              fullWidth
              value={fields.content}
              onChange={(event) => dispatchReducerAction({
                type: 'setContent',
                value: event.target.value
              })}
            />
          </Grid>
        </Grid>

        {/* Text controls */}
        <Grid item container xs={12} columnSpacing={4}>
          <Grid item container xs={8} rowGap={2}>
            <Grid item xs={12}>
                <Typography variant='overline'>Optional Paste Settings</Typography>
            </Grid>

            <form action="" onSubmit={handleFormSubmit}>
              <Grid item container xs={12} rowGap={1}>
                <Grid container alignItems="center">
                  <Grid item xs={4}>
                    <Typography variant='body2'>Category</Typography>
                  </Grid>
                  <Grid item xs={8}>
                    <FormControl fullWidth>
                      <Select
                        autoWidth
                        size='small'
                        variant='filled'
                        name="category"
                        value={fields.category}
                        onChange={(e) => dispatchReducerAction({
                          type: 'setCategory',
                          value: e.target.value
                        })}
                        inputProps={{ 'aria-label': 'category' }}
                      >
                        <MenuItem value='none'>None</MenuItem>
                        {
                          categories.map((item) => <MenuItem key={item} value={item}>{item}</MenuItem>)
                        }
                      </Select>
                    </FormControl>
                  </Grid>
                </Grid>
                <Grid container alignItems="center">
                  <Grid item xs={4}>
                    <Typography variant='body2'>Tags</Typography>
                  </Grid>
                  <Grid item xs={8}>
                    <FormControl fullWidth sx={{maxWidth: '100%'}}>
                      <TagInput 
                        tags={fields.tags} 
                        handleSetTags={(tag) => dispatchReducerAction({
                          type: 'setTags',
                          value: tag
                        })}
                        handleDeleteTag={(tag) => dispatchReducerAction({
                          type: 'deleteTag',
                          value: tag
                        })}
                      />
                    </FormControl>
                  </Grid>
                </Grid>
                <Grid container alignItems="center">
                  <Grid item xs={4}>
                    <Typography variant='body2'>Syntax Highlighting</Typography>
                  </Grid>
                  <Grid item xs={8}>
                    <FormControl fullWidth>
                      <Autocomplete
                        disabled={!fields.syntaxHighlight}
                        options={syntaxList}
                        getOptionLabel={(option) => option.name}
                        value={fields.syntax}
                        onChange={(e, newValue) => dispatchReducerAction({
                          type: 'setSyntax',
                          value: newValue
                        })}
                        renderInput={(params) => <TextField {...params} name="syntax" variant={'filled'}/>}
                      />
                    </FormControl>
                  </Grid>
                </Grid>
                <Grid container alignItems="center">
                  <Grid item xs={4}>
                    <Typography variant='body2'>Paste Expiry</Typography>
                  </Grid>
                  <Grid item xs={8}>
                    <FormControl fullWidth>
                      <Select
                        autoWidth
                        name="expiry"
                        value={fields.expiry}
                        inputProps={{ 'aria-label': 'expiry' }}
                        onChange={(e) => dispatchReducerAction({
                            type: 'setExpiresOn',
                            value: e.target.value
                          })
                        }
                        size='small'
                        variant='filled'
                      >
                        <MenuItem value='none'>None</MenuItem>
                        {
                          expiryOptions.map(option => <MenuItem key={option} value={option}>{option}</MenuItem>)
                        }
                      </Select>
                    </FormControl>
                  </Grid>
                </Grid>
                <Grid container alignItems="center">
                  <Grid item xs={4}>
                    <Typography variant='body2'>Paste Exposure</Typography>
                  </Grid>
                  <Grid item xs={8}>
                    <FormControl fullWidth>
                      <Select
                        autoWidth
                        name="exposure"
                        value={fields.exposure}
                        inputProps={{ 'aria-label': 'exposure' }}
                        onChange={(e) => dispatchReducerAction({
                          type: 'setExposure',
                          value: e.target.value
                        })}
                        size='small'
                        variant='filled'
                      >
                        <MenuItem value='public'>Public</MenuItem>
                        <MenuItem value='private'>Private</MenuItem>
                      </Select>
                    </FormControl>
                  </Grid>
                </Grid>
                <Grid container alignItems="center">
                  <Grid item xs={4}>
                    <Typography variant='body2'>Folder</Typography>
                  </Grid>
                  <Grid item xs={8}>
                    <FormControl fullWidth>
                      <Select
                        autoWidth
                        name="folder"
                        inputProps={{ 'aria-label': 'folder' }}
                        value={fields.folder}
                        onChange={(e) => dispatchReducerAction({
                          type: 'setFolder',
                          value: e.target.value
                        })}
                        size='small'
                        variant='filled'
                        disabled={!auth.isAuthenticated}
                        
                      >
                        <MenuItem value='none'>None</MenuItem>
                      </Select>
                    </FormControl>
                  </Grid>
                </Grid>
                <Grid container alignItems="flex-start">
                  <Grid item xs={4}>
                    <Typography variant='body2'>Password</Typography>
                  </Grid>
                  <Grid item xs={8}>
                    <FormControlLabel
                      name='passwordEnabled'
                      control={<Checkbox checked={fields.passwordEnabled} onChange={() => dispatchReducerAction({
                        type: 'setPasswordEnabled'
                      })} />}
                      label={fields.passwordEnabled ? 'Enabled' : 'Disabled'} />

                    <FormControl fullWidth>
                      <TextField variant="filled" name='password' type="password" value={fields.password} onChange={(e) => dispatchReducerAction({
                        type: 'setPassword',
                        value: e.target.value
                      })} disabled={!fields.passwordEnabled} placeholder='password'/>
                      <FormControlLabel
                        name='burnAfterRead' value={fields.burnAfterRead}
                        control={<Checkbox checked={fields.burnAfterRead} onChange={(e) => dispatchReducerAction({
                          type: 'setBurnAfterRead'
                        })} />}
                        label='Burn after read' />
                    </FormControl>
                  </Grid>
                </Grid>
                <Grid container alignItems="center">
                  <Grid item xs={4}>
                    <Typography variant='body2'>Paste Name / Title</Typography>
                  </Grid>
                  <Grid item xs={8}>
                    <FormControl fullWidth>
                      <TextField variant="filled" name='pasteName' value={fields.pasteName} onChange={(e) => dispatchReducerAction({
                        type: 'setPasteName',
                        value: e.target.value
                      })} placeholder='Paste name'/>
                    </FormControl>
                  </Grid>
                </Grid>
                <Grid container alignItems="center">
                  <Grid item xs={4}></Grid>
                  <Grid item xs={8}>
                    <Button
                      variant={'contained'}
                      fullWidth={true}
                      onClick={handleFormSubmit}
                      sx={{mb: 4}}
                      disabled={!validate()}
                    >
                        Create new paste
                    </Button>
                  </Grid>
                </Grid>
              </Grid>
            </form>
          </Grid>
          <Grid item xs={4}>
            <Grid container rowGap={2} sx={{mt: 6}}>
              <Grid item xs={12}>
                <Grid container alignItems='center' spacing={4}>
                  <Grid item container xs={3} >
                    <Avatar
                      variant='rounded'
                      sx={{ width: 56, height: 56 }}
                    >
                      <AccountBoxOutlined />
                    </Avatar>
                  </Grid>
                  <Grid item container xs={9}>
                    <Grid item size={12}>
                      <Typography variant='body2'>Hello Guest</Typography>
                    </Grid>
                    <Grid item container size={12} spacing={1} justifyContent='space-between'>
                      <Grid item>
                        <Button variant='outlined' color='secondary' size='small'>Sign Up</Button>
                      </Grid>
                      <Grid item>
                        <Button variant='outlined' color='secondary' size='small'>Login</Button>
                      </Grid>
                    </Grid>
                  </Grid>
                </Grid>
              </Grid>
              <Grid item xs={12}>
                <Grid item container xs={12} flexDirection={'column'} rowGap={1}>
                  <Button variant='outlined' startIcon={<Facebook />} color='secondary' size='small'>Sign in With FaceBook</Button>
                  <Button variant='outlined' startIcon={<Twitter />} color='secondary' size='small'>Sign in With Twitter</Button>
                  <Button variant='outlined' startIcon={<Google />} color='secondary' size='small'>Sign in With Google</Button>
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      </Grid>

      <Grid item xs={4}>
        <PublicPasteList recentPastes={recentPastes} />
      </Grid>
    </Grid>
  )
}

export default Home
