begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.crypto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|crypto
package|;
end_package

begin_comment
comment|/**  *<code>HexUtils</code> provides utility methods for hex conversions  */
end_comment

begin_class
DECL|class|HexUtils
specifier|public
specifier|final
class|class
name|HexUtils
block|{
DECL|field|hexChars
specifier|private
specifier|static
name|char
name|hexChars
index|[]
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
DECL|method|HexUtils ()
specifier|private
name|HexUtils
parameter_list|()
block|{ }
comment|/**      * Creates a string representation of the supplied byte array in Hexidecimal      * format      *      * @param in the byte array to convert to a hex string.      * @param start where to begin in the array      * @param length how many bytes from the array to include in the hexidecimal      *            representation      * @return a string containing the hexidecimal representation of the      *         requested bytes from the array      */
DECL|method|byteArrayToHexString (byte in[], int start, int length)
specifier|public
specifier|static
name|String
name|byteArrayToHexString
parameter_list|(
name|byte
name|in
index|[]
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|String
name|asHexString
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|out
init|=
operator|new
name|StringBuilder
argument_list|(
name|in
operator|.
name|length
operator|*
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
name|start
init|;
name|x
operator|<
name|length
condition|;
name|x
operator|++
control|)
block|{
name|int
name|nybble
init|=
name|in
index|[
name|x
index|]
operator|&
literal|0xF0
decl_stmt|;
name|nybble
operator|=
name|nybble
operator|>>>
literal|4
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|hexChars
index|[
name|nybble
index|]
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|hexChars
index|[
name|in
index|[
name|x
index|]
operator|&
literal|0x0F
index|]
argument_list|)
expr_stmt|;
block|}
name|asHexString
operator|=
name|out
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|asHexString
return|;
block|}
comment|/**      * Creates a string representation of the supplied byte array in Hexidecimal      * format      *      * @param in the byte array to convert to a hex string.      * @return a string containing the hexidecimal representation of the array      */
DECL|method|byteArrayToHexString (byte in[])
specifier|public
specifier|static
name|String
name|byteArrayToHexString
parameter_list|(
name|byte
name|in
index|[]
parameter_list|)
block|{
return|return
name|byteArrayToHexString
argument_list|(
name|in
argument_list|,
literal|0
argument_list|,
name|in
operator|.
name|length
argument_list|)
return|;
block|}
comment|/**      * Convert a hex string into an array of bytes. The string is expected to      * consist entirely of valid Hex characters i.e. 0123456789abcdefABCDEF. The      * array is calculated by traversing the string from from left to right,      * ignoring whitespace. Every 2 valid hex chars will constitute a new byte      * for the array. If the string is uneven then it the last byte will be      * padded with a '0'.      *      * @param hexString String to be converted      */
DECL|method|hexToByteArray (String hexString)
specifier|public
specifier|static
name|byte
index|[]
name|hexToByteArray
parameter_list|(
name|String
name|hexString
parameter_list|)
block|{
name|StringBuilder
name|normalize
init|=
operator|new
name|StringBuilder
argument_list|(
name|hexString
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|hexString
operator|.
name|length
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|char
name|current
init|=
name|Character
operator|.
name|toLowerCase
argument_list|(
name|hexString
operator|.
name|charAt
argument_list|(
name|x
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|isHexChar
argument_list|(
name|current
argument_list|)
condition|)
block|{
name|normalize
operator|.
name|append
argument_list|(
name|current
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Character
operator|.
name|isWhitespace
argument_list|(
name|current
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Conversion of hex string to array failed. '%c' is not a valid hex character"
argument_list|,
name|current
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|// pad with a zero if we have an uneven number of characters.
if|if
condition|(
name|normalize
operator|.
name|length
argument_list|()
operator|%
literal|2
operator|>
literal|0
condition|)
block|{
name|normalize
operator|.
name|append
argument_list|(
literal|'0'
argument_list|)
expr_stmt|;
block|}
name|byte
index|[]
name|hexArray
init|=
operator|new
name|byte
index|[
name|hexString
operator|.
name|length
argument_list|()
operator|+
literal|1
operator|>>
literal|1
index|]
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|hexArray
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
name|int
name|ni
init|=
name|x
operator|<<
literal|1
decl_stmt|;
name|int
name|mostSignificantNybble
init|=
name|Character
operator|.
name|digit
argument_list|(
name|normalize
operator|.
name|charAt
argument_list|(
name|ni
argument_list|)
argument_list|,
literal|16
argument_list|)
decl_stmt|;
name|int
name|leastSignificantNybble
init|=
name|Character
operator|.
name|digit
argument_list|(
name|normalize
operator|.
name|charAt
argument_list|(
name|ni
operator|+
literal|1
argument_list|)
argument_list|,
literal|16
argument_list|)
decl_stmt|;
name|int
name|value
init|=
operator|(
operator|(
name|mostSignificantNybble
operator|<<
literal|4
operator|)
operator|)
operator||
operator|(
name|leastSignificantNybble
operator|&
literal|0x0F
operator|)
decl_stmt|;
name|hexArray
index|[
name|x
index|]
operator|=
operator|(
name|byte
operator|)
name|value
expr_stmt|;
block|}
return|return
name|hexArray
return|;
block|}
DECL|method|isHexChar (char current)
specifier|public
specifier|static
name|boolean
name|isHexChar
parameter_list|(
name|char
name|current
parameter_list|)
block|{
return|return
name|Character
operator|.
name|digit
argument_list|(
name|current
argument_list|,
literal|16
argument_list|)
operator|>=
literal|0
return|;
block|}
block|}
end_class

end_unit

