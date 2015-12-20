begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/* Generated By:JavaCC: Do not edit this line. SimpleCharStream.java Version 5.0 */
end_comment

begin_comment
comment|/* JavaCCOptions:STATIC=false,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.sspt.parser
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|sspt
operator|.
name|parser
package|;
end_package

begin_comment
comment|/**  * An implementation of interface CharStream, where the stream is assumed to  * contain only ASCII characters (without unicode processing).  */
end_comment

begin_class
DECL|class|SimpleCharStream
specifier|public
class|class
name|SimpleCharStream
block|{
comment|/** Whether parser is static. */
DECL|field|staticFlag
specifier|public
specifier|static
specifier|final
name|boolean
name|staticFlag
init|=
literal|false
decl_stmt|;
DECL|field|bufsize
name|int
name|bufsize
decl_stmt|;
DECL|field|available
name|int
name|available
decl_stmt|;
DECL|field|tokenBegin
name|int
name|tokenBegin
decl_stmt|;
comment|/** Position in buffer. */
DECL|field|bufpos
specifier|public
name|int
name|bufpos
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|bufline
specifier|protected
name|int
name|bufline
index|[]
decl_stmt|;
DECL|field|bufcolumn
specifier|protected
name|int
name|bufcolumn
index|[]
decl_stmt|;
DECL|field|column
specifier|protected
name|int
name|column
init|=
literal|0
decl_stmt|;
DECL|field|line
specifier|protected
name|int
name|line
init|=
literal|1
decl_stmt|;
DECL|field|prevCharIsCR
specifier|protected
name|boolean
name|prevCharIsCR
init|=
literal|false
decl_stmt|;
DECL|field|prevCharIsLF
specifier|protected
name|boolean
name|prevCharIsLF
init|=
literal|false
decl_stmt|;
DECL|field|inputStream
specifier|protected
name|java
operator|.
name|io
operator|.
name|Reader
name|inputStream
decl_stmt|;
DECL|field|buffer
specifier|protected
name|char
index|[]
name|buffer
decl_stmt|;
DECL|field|maxNextCharInd
specifier|protected
name|int
name|maxNextCharInd
init|=
literal|0
decl_stmt|;
DECL|field|inBuf
specifier|protected
name|int
name|inBuf
init|=
literal|0
decl_stmt|;
DECL|field|tabSize
specifier|protected
name|int
name|tabSize
init|=
literal|8
decl_stmt|;
DECL|method|setTabSize (int i)
specifier|protected
name|void
name|setTabSize
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|tabSize
operator|=
name|i
expr_stmt|;
block|}
DECL|method|getTabSize (int i)
specifier|protected
name|int
name|getTabSize
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|tabSize
return|;
block|}
DECL|method|ExpandBuff (boolean wrapAround)
specifier|protected
name|void
name|ExpandBuff
parameter_list|(
name|boolean
name|wrapAround
parameter_list|)
block|{
name|char
index|[]
name|newbuffer
init|=
operator|new
name|char
index|[
name|bufsize
operator|+
literal|2048
index|]
decl_stmt|;
name|int
name|newbufline
index|[]
init|=
operator|new
name|int
index|[
name|bufsize
operator|+
literal|2048
index|]
decl_stmt|;
name|int
name|newbufcolumn
index|[]
init|=
operator|new
name|int
index|[
name|bufsize
operator|+
literal|2048
index|]
decl_stmt|;
try|try
block|{
if|if
condition|(
name|wrapAround
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
name|tokenBegin
argument_list|,
name|newbuffer
argument_list|,
literal|0
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|newbuffer
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|,
name|bufpos
argument_list|)
expr_stmt|;
name|buffer
operator|=
name|newbuffer
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bufline
argument_list|,
name|tokenBegin
argument_list|,
name|newbufline
argument_list|,
literal|0
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bufline
argument_list|,
literal|0
argument_list|,
name|newbufline
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|,
name|bufpos
argument_list|)
expr_stmt|;
name|bufline
operator|=
name|newbufline
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bufcolumn
argument_list|,
name|tokenBegin
argument_list|,
name|newbufcolumn
argument_list|,
literal|0
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bufcolumn
argument_list|,
literal|0
argument_list|,
name|newbufcolumn
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|,
name|bufpos
argument_list|)
expr_stmt|;
name|bufcolumn
operator|=
name|newbufcolumn
expr_stmt|;
name|maxNextCharInd
operator|=
operator|(
name|bufpos
operator|+=
operator|(
name|bufsize
operator|-
name|tokenBegin
operator|)
operator|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
name|tokenBegin
argument_list|,
name|newbuffer
argument_list|,
literal|0
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|)
expr_stmt|;
name|buffer
operator|=
name|newbuffer
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bufline
argument_list|,
name|tokenBegin
argument_list|,
name|newbufline
argument_list|,
literal|0
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|)
expr_stmt|;
name|bufline
operator|=
name|newbufline
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bufcolumn
argument_list|,
name|tokenBegin
argument_list|,
name|newbufcolumn
argument_list|,
literal|0
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|)
expr_stmt|;
name|bufcolumn
operator|=
name|newbufcolumn
expr_stmt|;
name|maxNextCharInd
operator|=
operator|(
name|bufpos
operator|-=
name|tokenBegin
operator|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|Error
argument_list|(
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|bufsize
operator|+=
literal|2048
expr_stmt|;
name|available
operator|=
name|bufsize
expr_stmt|;
name|tokenBegin
operator|=
literal|0
expr_stmt|;
block|}
DECL|method|FillBuff ()
specifier|protected
name|void
name|FillBuff
parameter_list|()
throws|throws
name|java
operator|.
name|io
operator|.
name|IOException
block|{
if|if
condition|(
name|maxNextCharInd
operator|==
name|available
condition|)
block|{
if|if
condition|(
name|available
operator|==
name|bufsize
condition|)
block|{
if|if
condition|(
name|tokenBegin
operator|>
literal|2048
condition|)
block|{
name|bufpos
operator|=
name|maxNextCharInd
operator|=
literal|0
expr_stmt|;
name|available
operator|=
name|tokenBegin
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tokenBegin
operator|<
literal|0
condition|)
name|bufpos
operator|=
name|maxNextCharInd
operator|=
literal|0
expr_stmt|;
else|else
name|ExpandBuff
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|available
operator|>
name|tokenBegin
condition|)
name|available
operator|=
name|bufsize
expr_stmt|;
elseif|else
if|if
condition|(
operator|(
name|tokenBegin
operator|-
name|available
operator|)
operator|<
literal|2048
condition|)
name|ExpandBuff
argument_list|(
literal|true
argument_list|)
expr_stmt|;
else|else
name|available
operator|=
name|tokenBegin
expr_stmt|;
block|}
name|int
name|i
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|(
name|i
operator|=
name|inputStream
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
name|maxNextCharInd
argument_list|,
name|available
operator|-
name|maxNextCharInd
argument_list|)
operator|)
operator|==
operator|-
literal|1
condition|)
block|{
name|inputStream
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|java
operator|.
name|io
operator|.
name|IOException
argument_list|()
throw|;
block|}
else|else
name|maxNextCharInd
operator|+=
name|i
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|io
operator|.
name|IOException
name|e
parameter_list|)
block|{
operator|--
name|bufpos
expr_stmt|;
name|backup
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|tokenBegin
operator|==
operator|-
literal|1
condition|)
name|tokenBegin
operator|=
name|bufpos
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
comment|/** Start. */
DECL|method|BeginToken ()
specifier|public
name|char
name|BeginToken
parameter_list|()
throws|throws
name|java
operator|.
name|io
operator|.
name|IOException
block|{
name|tokenBegin
operator|=
operator|-
literal|1
expr_stmt|;
name|char
name|c
init|=
name|readChar
argument_list|()
decl_stmt|;
name|tokenBegin
operator|=
name|bufpos
expr_stmt|;
return|return
name|c
return|;
block|}
DECL|method|UpdateLineColumn (char c)
specifier|protected
name|void
name|UpdateLineColumn
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|column
operator|++
expr_stmt|;
if|if
condition|(
name|prevCharIsLF
condition|)
block|{
name|prevCharIsLF
operator|=
literal|false
expr_stmt|;
name|line
operator|+=
operator|(
name|column
operator|=
literal|1
operator|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|prevCharIsCR
condition|)
block|{
name|prevCharIsCR
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\n'
condition|)
block|{
name|prevCharIsLF
operator|=
literal|true
expr_stmt|;
block|}
else|else
name|line
operator|+=
operator|(
name|column
operator|=
literal|1
operator|)
expr_stmt|;
block|}
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'\r'
case|:
name|prevCharIsCR
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|'\n'
case|:
name|prevCharIsLF
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|'\t'
case|:
name|column
operator|--
expr_stmt|;
name|column
operator|+=
operator|(
name|tabSize
operator|-
operator|(
name|column
operator|%
name|tabSize
operator|)
operator|)
expr_stmt|;
break|break;
default|default :
break|break;
block|}
name|bufline
index|[
name|bufpos
index|]
operator|=
name|line
expr_stmt|;
name|bufcolumn
index|[
name|bufpos
index|]
operator|=
name|column
expr_stmt|;
block|}
comment|/** Read a character. */
DECL|method|readChar ()
specifier|public
name|char
name|readChar
parameter_list|()
throws|throws
name|java
operator|.
name|io
operator|.
name|IOException
block|{
if|if
condition|(
name|inBuf
operator|>
literal|0
condition|)
block|{
operator|--
name|inBuf
expr_stmt|;
if|if
condition|(
operator|++
name|bufpos
operator|==
name|bufsize
condition|)
name|bufpos
operator|=
literal|0
expr_stmt|;
return|return
name|buffer
index|[
name|bufpos
index|]
return|;
block|}
if|if
condition|(
operator|++
name|bufpos
operator|>=
name|maxNextCharInd
condition|)
name|FillBuff
argument_list|()
expr_stmt|;
name|char
name|c
init|=
name|buffer
index|[
name|bufpos
index|]
decl_stmt|;
name|UpdateLineColumn
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
annotation|@
name|Deprecated
comment|/**    * @deprecated    * @see #getEndColumn    */
DECL|method|getColumn ()
specifier|public
name|int
name|getColumn
parameter_list|()
block|{
return|return
name|bufcolumn
index|[
name|bufpos
index|]
return|;
block|}
annotation|@
name|Deprecated
comment|/**    * @deprecated    * @see #getEndLine    */
DECL|method|getLine ()
specifier|public
name|int
name|getLine
parameter_list|()
block|{
return|return
name|bufline
index|[
name|bufpos
index|]
return|;
block|}
comment|/** Get token end column number. */
DECL|method|getEndColumn ()
specifier|public
name|int
name|getEndColumn
parameter_list|()
block|{
return|return
name|bufcolumn
index|[
name|bufpos
index|]
return|;
block|}
comment|/** Get token end line number. */
DECL|method|getEndLine ()
specifier|public
name|int
name|getEndLine
parameter_list|()
block|{
return|return
name|bufline
index|[
name|bufpos
index|]
return|;
block|}
comment|/** Get token beginning column number. */
DECL|method|getBeginColumn ()
specifier|public
name|int
name|getBeginColumn
parameter_list|()
block|{
return|return
name|bufcolumn
index|[
name|tokenBegin
index|]
return|;
block|}
comment|/** Get token beginning line number. */
DECL|method|getBeginLine ()
specifier|public
name|int
name|getBeginLine
parameter_list|()
block|{
return|return
name|bufline
index|[
name|tokenBegin
index|]
return|;
block|}
comment|/** Backup a number of characters. */
DECL|method|backup (int amount)
specifier|public
name|void
name|backup
parameter_list|(
name|int
name|amount
parameter_list|)
block|{
name|inBuf
operator|+=
name|amount
expr_stmt|;
if|if
condition|(
operator|(
name|bufpos
operator|-=
name|amount
operator|)
operator|<
literal|0
condition|)
name|bufpos
operator|+=
name|bufsize
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SimpleCharStream (java.io.Reader dstream, int startline, int startcolumn, int buffersize)
specifier|public
name|SimpleCharStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|Reader
name|dstream
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|,
name|int
name|buffersize
parameter_list|)
block|{
name|inputStream
operator|=
name|dstream
expr_stmt|;
name|line
operator|=
name|startline
expr_stmt|;
name|column
operator|=
name|startcolumn
operator|-
literal|1
expr_stmt|;
name|available
operator|=
name|bufsize
operator|=
name|buffersize
expr_stmt|;
name|buffer
operator|=
operator|new
name|char
index|[
name|buffersize
index|]
expr_stmt|;
name|bufline
operator|=
operator|new
name|int
index|[
name|buffersize
index|]
expr_stmt|;
name|bufcolumn
operator|=
operator|new
name|int
index|[
name|buffersize
index|]
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SimpleCharStream (java.io.Reader dstream, int startline, int startcolumn)
specifier|public
name|SimpleCharStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|Reader
name|dstream
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|)
block|{
name|this
argument_list|(
name|dstream
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SimpleCharStream (java.io.Reader dstream)
specifier|public
name|SimpleCharStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|Reader
name|dstream
parameter_list|)
block|{
name|this
argument_list|(
name|dstream
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.Reader dstream, int startline, int startcolumn, int buffersize)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|Reader
name|dstream
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|,
name|int
name|buffersize
parameter_list|)
block|{
name|inputStream
operator|=
name|dstream
expr_stmt|;
name|line
operator|=
name|startline
expr_stmt|;
name|column
operator|=
name|startcolumn
operator|-
literal|1
expr_stmt|;
if|if
condition|(
name|buffer
operator|==
literal|null
operator|||
name|buffersize
operator|!=
name|buffer
operator|.
name|length
condition|)
block|{
name|available
operator|=
name|bufsize
operator|=
name|buffersize
expr_stmt|;
name|buffer
operator|=
operator|new
name|char
index|[
name|buffersize
index|]
expr_stmt|;
name|bufline
operator|=
operator|new
name|int
index|[
name|buffersize
index|]
expr_stmt|;
name|bufcolumn
operator|=
operator|new
name|int
index|[
name|buffersize
index|]
expr_stmt|;
block|}
name|prevCharIsLF
operator|=
name|prevCharIsCR
operator|=
literal|false
expr_stmt|;
name|tokenBegin
operator|=
name|inBuf
operator|=
name|maxNextCharInd
operator|=
literal|0
expr_stmt|;
name|bufpos
operator|=
operator|-
literal|1
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.Reader dstream, int startline, int startcolumn)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|Reader
name|dstream
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|)
block|{
name|ReInit
argument_list|(
name|dstream
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.Reader dstream)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|Reader
name|dstream
parameter_list|)
block|{
name|ReInit
argument_list|(
name|dstream
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SimpleCharStream (java.io.InputStream dstream, String encoding, int startline, int startcolumn, int buffersize)
specifier|public
name|SimpleCharStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|String
name|encoding
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|,
name|int
name|buffersize
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
block|{
name|this
argument_list|(
name|encoding
operator|==
literal|null
condition|?
operator|new
name|java
operator|.
name|io
operator|.
name|InputStreamReader
argument_list|(
name|dstream
argument_list|)
else|:
operator|new
name|java
operator|.
name|io
operator|.
name|InputStreamReader
argument_list|(
name|dstream
argument_list|,
name|encoding
argument_list|)
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
name|buffersize
argument_list|)
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SimpleCharStream (java.io.InputStream dstream, int startline, int startcolumn, int buffersize)
specifier|public
name|SimpleCharStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|,
name|int
name|buffersize
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|java
operator|.
name|io
operator|.
name|InputStreamReader
argument_list|(
name|dstream
argument_list|)
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
name|buffersize
argument_list|)
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SimpleCharStream (java.io.InputStream dstream, String encoding, int startline, int startcolumn)
specifier|public
name|SimpleCharStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|String
name|encoding
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
block|{
name|this
argument_list|(
name|dstream
argument_list|,
name|encoding
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SimpleCharStream (java.io.InputStream dstream, int startline, int startcolumn)
specifier|public
name|SimpleCharStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|)
block|{
name|this
argument_list|(
name|dstream
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SimpleCharStream (java.io.InputStream dstream, String encoding)
specifier|public
name|SimpleCharStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
block|{
name|this
argument_list|(
name|dstream
argument_list|,
name|encoding
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Constructor. */
DECL|method|SimpleCharStream (java.io.InputStream dstream)
specifier|public
name|SimpleCharStream
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|)
block|{
name|this
argument_list|(
name|dstream
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.InputStream dstream, String encoding, int startline, int startcolumn, int buffersize)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|String
name|encoding
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|,
name|int
name|buffersize
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
block|{
name|ReInit
argument_list|(
name|encoding
operator|==
literal|null
condition|?
operator|new
name|java
operator|.
name|io
operator|.
name|InputStreamReader
argument_list|(
name|dstream
argument_list|)
else|:
operator|new
name|java
operator|.
name|io
operator|.
name|InputStreamReader
argument_list|(
name|dstream
argument_list|,
name|encoding
argument_list|)
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
name|buffersize
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.InputStream dstream, int startline, int startcolumn, int buffersize)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|,
name|int
name|buffersize
parameter_list|)
block|{
name|ReInit
argument_list|(
operator|new
name|java
operator|.
name|io
operator|.
name|InputStreamReader
argument_list|(
name|dstream
argument_list|)
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
name|buffersize
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.InputStream dstream, String encoding)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
block|{
name|ReInit
argument_list|(
name|dstream
argument_list|,
name|encoding
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.InputStream dstream)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|)
block|{
name|ReInit
argument_list|(
name|dstream
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.InputStream dstream, String encoding, int startline, int startcolumn)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|String
name|encoding
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
block|{
name|ReInit
argument_list|(
name|dstream
argument_list|,
name|encoding
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Reinitialise. */
DECL|method|ReInit (java.io.InputStream dstream, int startline, int startcolumn)
specifier|public
name|void
name|ReInit
parameter_list|(
name|java
operator|.
name|io
operator|.
name|InputStream
name|dstream
parameter_list|,
name|int
name|startline
parameter_list|,
name|int
name|startcolumn
parameter_list|)
block|{
name|ReInit
argument_list|(
name|dstream
argument_list|,
name|startline
argument_list|,
name|startcolumn
argument_list|,
literal|4096
argument_list|)
expr_stmt|;
block|}
comment|/** Get token literal value. */
DECL|method|GetImage ()
specifier|public
name|String
name|GetImage
parameter_list|()
block|{
if|if
condition|(
name|bufpos
operator|>=
name|tokenBegin
condition|)
return|return
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
name|tokenBegin
argument_list|,
name|bufpos
operator|-
name|tokenBegin
operator|+
literal|1
argument_list|)
return|;
else|else
return|return
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
name|tokenBegin
argument_list|,
name|bufsize
operator|-
name|tokenBegin
argument_list|)
operator|+
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|bufpos
operator|+
literal|1
argument_list|)
return|;
block|}
comment|/** Get the suffix. */
DECL|method|GetSuffix (int len)
specifier|public
name|char
index|[]
name|GetSuffix
parameter_list|(
name|int
name|len
parameter_list|)
block|{
name|char
index|[]
name|ret
init|=
operator|new
name|char
index|[
name|len
index|]
decl_stmt|;
if|if
condition|(
operator|(
name|bufpos
operator|+
literal|1
operator|)
operator|>=
name|len
condition|)
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
name|bufpos
operator|-
name|len
operator|+
literal|1
argument_list|,
name|ret
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
else|else
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
name|bufsize
operator|-
operator|(
name|len
operator|-
name|bufpos
operator|-
literal|1
operator|)
argument_list|,
name|ret
argument_list|,
literal|0
argument_list|,
name|len
operator|-
name|bufpos
operator|-
literal|1
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|ret
argument_list|,
name|len
operator|-
name|bufpos
operator|-
literal|1
argument_list|,
name|bufpos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
comment|/** Reset buffer when finished. */
DECL|method|Done ()
specifier|public
name|void
name|Done
parameter_list|()
block|{
name|buffer
operator|=
literal|null
expr_stmt|;
name|bufline
operator|=
literal|null
expr_stmt|;
name|bufcolumn
operator|=
literal|null
expr_stmt|;
block|}
comment|/**    * Method to adjust line and column numbers for the start of a token.    */
DECL|method|adjustBeginLineColumn (int newLine, int newCol)
specifier|public
name|void
name|adjustBeginLineColumn
parameter_list|(
name|int
name|newLine
parameter_list|,
name|int
name|newCol
parameter_list|)
block|{
name|int
name|start
init|=
name|tokenBegin
decl_stmt|;
name|int
name|len
decl_stmt|;
if|if
condition|(
name|bufpos
operator|>=
name|tokenBegin
condition|)
block|{
name|len
operator|=
name|bufpos
operator|-
name|tokenBegin
operator|+
name|inBuf
operator|+
literal|1
expr_stmt|;
block|}
else|else
block|{
name|len
operator|=
name|bufsize
operator|-
name|tokenBegin
operator|+
name|bufpos
operator|+
literal|1
operator|+
name|inBuf
expr_stmt|;
block|}
name|int
name|i
init|=
literal|0
decl_stmt|,
name|j
init|=
literal|0
decl_stmt|,
name|k
init|=
literal|0
decl_stmt|;
name|int
name|nextColDiff
init|=
literal|0
decl_stmt|,
name|columnDiff
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|i
operator|<
name|len
operator|&&
name|bufline
index|[
name|j
operator|=
name|start
operator|%
name|bufsize
index|]
operator|==
name|bufline
index|[
name|k
operator|=
operator|++
name|start
operator|%
name|bufsize
index|]
condition|)
block|{
name|bufline
index|[
name|j
index|]
operator|=
name|newLine
expr_stmt|;
name|nextColDiff
operator|=
name|columnDiff
operator|+
name|bufcolumn
index|[
name|k
index|]
operator|-
name|bufcolumn
index|[
name|j
index|]
expr_stmt|;
name|bufcolumn
index|[
name|j
index|]
operator|=
name|newCol
operator|+
name|columnDiff
expr_stmt|;
name|columnDiff
operator|=
name|nextColDiff
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|<
name|len
condition|)
block|{
name|bufline
index|[
name|j
index|]
operator|=
name|newLine
operator|++
expr_stmt|;
name|bufcolumn
index|[
name|j
index|]
operator|=
name|newCol
operator|+
name|columnDiff
expr_stmt|;
while|while
condition|(
name|i
operator|++
operator|<
name|len
condition|)
block|{
if|if
condition|(
name|bufline
index|[
name|j
operator|=
name|start
operator|%
name|bufsize
index|]
operator|!=
name|bufline
index|[
operator|++
name|start
operator|%
name|bufsize
index|]
condition|)
name|bufline
index|[
name|j
index|]
operator|=
name|newLine
operator|++
expr_stmt|;
else|else
name|bufline
index|[
name|j
index|]
operator|=
name|newLine
expr_stmt|;
block|}
block|}
name|line
operator|=
name|bufline
index|[
name|j
index|]
expr_stmt|;
name|column
operator|=
name|bufcolumn
index|[
name|j
index|]
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|/* JavaCC - OriginalChecksum=740c413ce2f513b4d926ea988f37e3da (do not edit this line) */
end_comment

end_unit

