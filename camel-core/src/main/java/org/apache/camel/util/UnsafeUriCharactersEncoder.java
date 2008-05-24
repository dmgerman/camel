begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|BitSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Encoder for unsafe URI characters.  */
end_comment

begin_class
DECL|class|UnsafeUriCharactersEncoder
specifier|public
specifier|final
class|class
name|UnsafeUriCharactersEncoder
block|{
DECL|field|unsafeCharacters
specifier|private
specifier|static
name|BitSet
name|unsafeCharacters
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|HEX_DIGITS
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|HEX_DIGITS
init|=
block|{
literal|'0'
block|,
literal|'1'
block|,
literal|'2'
block|,
literal|'3'
block|,
literal|'4'
block|,
literal|'5'
block|,
literal|'6'
block|,
literal|'7'
block|,
literal|'8'
block|,
literal|'9'
block|,
literal|'A'
block|,
literal|'B'
block|,
literal|'C'
block|,
literal|'D'
block|,
literal|'E'
block|,
literal|'F'
block|}
decl_stmt|;
static|static
block|{
name|unsafeCharacters
operator|=
operator|new
name|BitSet
argument_list|(
literal|256
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'<'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'%'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'|'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'^'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'~'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
name|unsafeCharacters
operator|.
name|set
argument_list|(
literal|'`'
argument_list|)
expr_stmt|;
block|}
DECL|method|UnsafeUriCharactersEncoder ()
specifier|private
name|UnsafeUriCharactersEncoder
parameter_list|()
block|{
comment|// util class
block|}
DECL|method|encode (String s)
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|int
name|n
init|=
name|s
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|==
literal|0
condition|)
block|{
return|return
name|s
return|;
block|}
try|try
block|{
comment|// First check whether we actually need to encode
name|byte
index|[]
name|bytes
init|=
name|s
operator|.
name|getBytes
argument_list|(
literal|"UTF8"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
condition|;
control|)
block|{
if|if
condition|(
name|unsafeCharacters
operator|.
name|get
argument_list|(
name|bytes
index|[
name|i
index|]
argument_list|)
condition|)
block|{
break|break;
block|}
if|if
condition|(
operator|++
name|i
operator|>=
name|bytes
operator|.
name|length
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
comment|// okay there are some unsafe characters so we do need to encode
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
if|if
condition|(
name|unsafeCharacters
operator|.
name|get
argument_list|(
name|b
argument_list|)
condition|)
block|{
name|appendEscape
argument_list|(
name|sb
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|b
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Can't encoding the uri: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|method|appendEscape (StringBuffer sb, byte b)
specifier|private
specifier|static
name|void
name|appendEscape
parameter_list|(
name|StringBuffer
name|sb
parameter_list|,
name|byte
name|b
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'%'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|HEX_DIGITS
index|[
operator|(
name|b
operator|>>
literal|4
operator|)
operator|&
literal|0x0f
index|]
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|HEX_DIGITS
index|[
operator|(
name|b
operator|>>
literal|0
operator|)
operator|&
literal|0x0f
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

