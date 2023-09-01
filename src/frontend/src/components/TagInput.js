import React, { useRef, useState } from 'react'
import {
  Box,
  TextField
} from '@mui/material'
import Tag from './Tag'

function TagInput({ tags, handleSetTags, handleDeleteTag }) {
  const tagRef = useRef()
  const [tag, setTag] = useState('')

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      if (tag.trim() !== "") {
        handleSetTags(tagRef.current.value)
        setTag('')
      }

      e.stopPropagation()
    }
  }
  const handleDelete = (value) => {
    handleDeleteTag(value)
  }

  return (
    <Box>
      <TextField
        inputRef={tagRef}
        fullWidth
        variant='filled'
        size='small'
        margin='none'
        placeholder={tags.length < 4 ? "Enter tags here" : ""}
        value={tag}
        onChange={(e) => setTag(e.target.value)}
        onKeyDown={handleKeyDown}
        InputProps={{
          startAdornment: (
            <Box sx={{ margin: "0 0.2rem 0 0", display: "flex" }}>
              {tags.map((data, index) => {
                return (
                  <Tag key={index} data={data} name={data} handleDelete={handleDelete} />
                )
              })}
            </Box>
          )
        }}
      />
    </Box>
  )
}

export default TagInput