begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.runtimecatalog.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|runtimecatalog
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Encoder for unsafe URI characters.  *<p/>  * A good source for details is<a href="http://en.wikipedia.org/wiki/Url_encode">wikipedia url encode</a> article.  */
end_comment

begin_class
DECL|class|UnsafeUriCharactersEncoder
specifier|public
specifier|final
class|class
name|UnsafeUriCharactersEncoder
block|{
DECL|field|unsafeCharactersRfc1738
specifier|private
specifier|static
name|BitSet
name|unsafeCharactersRfc1738
decl_stmt|;
DECL|field|unsafeCharactersHttp
specifier|private
specifier|static
name|BitSet
name|unsafeCharactersHttp
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
block|,
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|,
literal|'f'
block|}
decl_stmt|;
static|static
block|{
name|unsafeCharactersRfc1738
operator|=
operator|new
name|BitSet
argument_list|(
literal|256
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'<'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'%'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'|'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'^'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'~'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
name|unsafeCharactersRfc1738
operator|.
name|set
argument_list|(
literal|'`'
argument_list|)
expr_stmt|;
block|}
static|static
block|{
name|unsafeCharactersHttp
operator|=
operator|new
name|BitSet
argument_list|(
literal|256
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'<'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'%'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'|'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'^'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
operator|.
name|set
argument_list|(
literal|'~'
argument_list|)
expr_stmt|;
name|unsafeCharactersHttp
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
return|return
name|encode
argument_list|(
name|s
argument_list|,
name|unsafeCharactersRfc1738
argument_list|)
return|;
block|}
DECL|method|encodeHttpURI (String s)
specifier|public
specifier|static
name|String
name|encodeHttpURI
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|encode
argument_list|(
name|s
argument_list|,
name|unsafeCharactersHttp
argument_list|)
return|;
block|}
DECL|method|encode (String s, BitSet unsafeCharacters)
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|s
parameter_list|,
name|BitSet
name|unsafeCharacters
parameter_list|)
block|{
return|return
name|encode
argument_list|(
name|s
argument_list|,
name|unsafeCharacters
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|encode (String s, boolean checkRaw)
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|s
parameter_list|,
name|boolean
name|checkRaw
parameter_list|)
block|{
return|return
name|encode
argument_list|(
name|s
argument_list|,
name|unsafeCharactersRfc1738
argument_list|,
name|checkRaw
argument_list|)
return|;
block|}
DECL|method|encodeHttpURI (String s, boolean checkRaw)
specifier|public
specifier|static
name|String
name|encodeHttpURI
parameter_list|(
name|String
name|s
parameter_list|,
name|boolean
name|checkRaw
parameter_list|)
block|{
return|return
name|encode
argument_list|(
name|s
argument_list|,
name|unsafeCharactersHttp
argument_list|,
name|checkRaw
argument_list|)
return|;
block|}
comment|// Just skip the encode for isRAW part
DECL|method|encode (String s, BitSet unsafeCharacters, boolean checkRaw)
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|s
parameter_list|,
name|BitSet
name|unsafeCharacters
parameter_list|,
name|boolean
name|checkRaw
parameter_list|)
block|{
name|List
argument_list|<
name|Pair
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|rawPairs
decl_stmt|;
if|if
condition|(
name|checkRaw
condition|)
block|{
name|rawPairs
operator|=
name|URISupport
operator|.
name|scanRaw
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rawPairs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|int
name|n
init|=
name|s
operator|==
literal|null
condition|?
literal|0
else|:
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
comment|// First check whether we actually need to encode
name|char
name|chars
index|[]
init|=
name|s
operator|.
name|toCharArray
argument_list|()
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
comment|// just deal with the ascii character
if|if
condition|(
name|chars
index|[
name|i
index|]
operator|>
literal|0
operator|&&
name|chars
index|[
name|i
index|]
operator|<
literal|128
condition|)
block|{
if|if
condition|(
name|unsafeCharacters
operator|.
name|get
argument_list|(
name|chars
index|[
name|i
index|]
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
operator|++
name|i
operator|>=
name|chars
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
comment|// see details at: http://en.wikipedia.org/wiki/Url_encode
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|chars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|ch
init|=
name|chars
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|ch
operator|>
literal|0
operator|&&
name|ch
operator|<
literal|128
operator|&&
name|unsafeCharacters
operator|.
name|get
argument_list|(
name|ch
argument_list|)
condition|)
block|{
comment|// special for % sign as it may be a decimal encoded value
if|if
condition|(
name|ch
operator|==
literal|'%'
condition|)
block|{
name|char
name|next
init|=
name|i
operator|+
literal|1
operator|<
name|chars
operator|.
name|length
condition|?
name|chars
index|[
name|i
operator|+
literal|1
index|]
else|:
literal|' '
decl_stmt|;
name|char
name|next2
init|=
name|i
operator|+
literal|2
operator|<
name|chars
operator|.
name|length
condition|?
name|chars
index|[
name|i
operator|+
literal|2
index|]
else|:
literal|' '
decl_stmt|;
if|if
condition|(
name|isHexDigit
argument_list|(
name|next
argument_list|)
operator|&&
name|isHexDigit
argument_list|(
name|next2
argument_list|)
operator|&&
operator|!
name|URISupport
operator|.
name|isRaw
argument_list|(
name|i
argument_list|,
name|rawPairs
argument_list|)
condition|)
block|{
comment|// its already encoded (decimal encoded) so just append as is
name|sb
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// must escape then, as its an unsafe character
name|appendEscape
argument_list|(
name|sb
argument_list|,
operator|(
name|byte
operator|)
name|ch
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// must escape then, as its an unsafe character
name|appendEscape
argument_list|(
name|sb
argument_list|,
operator|(
name|byte
operator|)
name|ch
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|ch
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
DECL|method|appendEscape (StringBuilder sb, byte b)
specifier|private
specifier|static
name|void
name|appendEscape
parameter_list|(
name|StringBuilder
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
DECL|method|isHexDigit (char ch)
specifier|private
specifier|static
name|boolean
name|isHexDigit
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
for|for
control|(
name|char
name|hex
range|:
name|HEX_DIGITS
control|)
block|{
if|if
condition|(
name|hex
operator|==
name|ch
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

