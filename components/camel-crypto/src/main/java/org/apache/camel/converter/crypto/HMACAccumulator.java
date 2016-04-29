begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Mac
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|crypto
operator|.
name|HexUtils
operator|.
name|byteArrayToHexString
import|;
end_import

begin_comment
comment|/**  *<code>HMACAccumulator</code> is used to build Hash Message Authentication  * Codes. It has two modes, one where all the data acquired is used to build the  * MAC and a second that assumes that the last n bytes of the acquired data will  * contain a MAC for all the data previous.  *<p>  * The first mode it used in an encryption phase to create a MAC for the  * encrypted data. The second mode is used in the decryption phase and  * recalculates the MAC taking into account that for cases where the encrypted  * data MAC has been appended. Internally the accumulator uses a circular buffer  * to simplify the housekeeping of avoiding the last n bytes.  *<p>  * It is assumed that the supplied buffersize is always greater than or equal to  * the mac length.  */
end_comment

begin_class
DECL|class|HMACAccumulator
specifier|public
class|class
name|HMACAccumulator
block|{
DECL|field|outputStream
specifier|protected
name|OutputStream
name|outputStream
decl_stmt|;
DECL|field|unprocessed
specifier|private
name|CircularBuffer
name|unprocessed
decl_stmt|;
DECL|field|calculatedMac
specifier|private
name|byte
index|[]
name|calculatedMac
decl_stmt|;
DECL|field|hmac
specifier|private
name|Mac
name|hmac
decl_stmt|;
DECL|field|maclength
specifier|private
name|int
name|maclength
decl_stmt|;
DECL|field|appended
specifier|private
name|byte
index|[]
name|appended
decl_stmt|;
DECL|method|HMACAccumulator ()
name|HMACAccumulator
parameter_list|()
block|{     }
DECL|method|HMACAccumulator (Key key, String macAlgorithm, String cryptoProvider, int buffersize)
specifier|public
name|HMACAccumulator
parameter_list|(
name|Key
name|key
parameter_list|,
name|String
name|macAlgorithm
parameter_list|,
name|String
name|cryptoProvider
parameter_list|,
name|int
name|buffersize
parameter_list|)
throws|throws
name|Exception
block|{
name|hmac
operator|=
name|cryptoProvider
operator|==
literal|null
condition|?
name|Mac
operator|.
name|getInstance
argument_list|(
name|macAlgorithm
argument_list|)
else|:
name|Mac
operator|.
name|getInstance
argument_list|(
name|macAlgorithm
argument_list|,
name|cryptoProvider
argument_list|)
expr_stmt|;
name|Key
name|hmacKey
init|=
operator|new
name|SecretKeySpec
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|,
name|macAlgorithm
argument_list|)
decl_stmt|;
name|hmac
operator|.
name|init
argument_list|(
name|hmacKey
argument_list|)
expr_stmt|;
name|maclength
operator|=
name|hmac
operator|.
name|getMacLength
argument_list|()
expr_stmt|;
name|unprocessed
operator|=
operator|new
name|CircularBuffer
argument_list|(
name|buffersize
operator|+
name|maclength
argument_list|)
expr_stmt|;
block|}
comment|/**      * Update buffer with MAC. Typically used in the encryption phase where no      * hmac is appended to the buffer.      */
DECL|method|encryptUpdate (byte[] buffer, int read)
specifier|public
name|void
name|encryptUpdate
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|read
parameter_list|)
block|{
name|hmac
operator|.
name|update
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
comment|/**      * Update buffer with MAC taking into account that a MAC is appended to the      * buffer and should be precluded from the MAC calculation.      */
DECL|method|decryptUpdate (byte[] buffer, int read)
specifier|public
name|void
name|decryptUpdate
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|read
parameter_list|)
throws|throws
name|IOException
block|{
name|unprocessed
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
name|int
name|safe
init|=
name|unprocessed
operator|.
name|availableForRead
argument_list|()
operator|-
name|maclength
decl_stmt|;
if|if
condition|(
name|safe
operator|>
literal|0
condition|)
block|{
name|unprocessed
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|safe
argument_list|)
expr_stmt|;
name|hmac
operator|.
name|update
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|safe
argument_list|)
expr_stmt|;
if|if
condition|(
name|outputStream
operator|!=
literal|null
condition|)
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|safe
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getCalculatedMac ()
specifier|public
name|byte
index|[]
name|getCalculatedMac
parameter_list|()
block|{
if|if
condition|(
name|calculatedMac
operator|==
literal|null
condition|)
block|{
name|calculatedMac
operator|=
name|hmac
operator|.
name|doFinal
argument_list|()
expr_stmt|;
block|}
return|return
name|calculatedMac
return|;
block|}
DECL|method|getAppendedMac ()
specifier|public
name|byte
index|[]
name|getAppendedMac
parameter_list|()
block|{
if|if
condition|(
name|appended
operator|==
literal|null
condition|)
block|{
name|appended
operator|=
operator|new
name|byte
index|[
name|maclength
index|]
expr_stmt|;
name|unprocessed
operator|.
name|read
argument_list|(
name|appended
argument_list|,
literal|0
argument_list|,
name|maclength
argument_list|)
expr_stmt|;
block|}
return|return
name|appended
return|;
block|}
DECL|method|validate ()
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|byte
index|[]
name|actual
init|=
name|getCalculatedMac
argument_list|()
decl_stmt|;
name|byte
index|[]
name|expected
init|=
name|getAppendedMac
argument_list|()
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
name|actual
operator|.
name|length
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
name|expected
index|[
name|x
index|]
operator|!=
name|actual
index|[
name|x
index|]
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected mac did not match actual mac\nexpected:"
operator|+
name|byteArrayToHexString
argument_list|(
name|expected
argument_list|)
operator|+
literal|"\n     actual:"
operator|+
name|byteArrayToHexString
argument_list|(
name|actual
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|getMaclength ()
specifier|public
name|int
name|getMaclength
parameter_list|()
block|{
return|return
name|maclength
return|;
block|}
DECL|method|attachStream (final OutputStream outputStream)
specifier|public
name|void
name|attachStream
parameter_list|(
specifier|final
name|OutputStream
name|outputStream
parameter_list|)
block|{
name|this
operator|.
name|outputStream
operator|=
name|outputStream
expr_stmt|;
block|}
DECL|class|CircularBuffer
specifier|static
class|class
name|CircularBuffer
block|{
DECL|field|buffer
specifier|private
name|byte
index|[]
name|buffer
decl_stmt|;
DECL|field|write
specifier|private
name|int
name|write
decl_stmt|;
DECL|field|read
specifier|private
name|int
name|read
decl_stmt|;
DECL|field|available
specifier|private
name|int
name|available
decl_stmt|;
DECL|method|CircularBuffer (int bufferSize)
name|CircularBuffer
parameter_list|(
name|int
name|bufferSize
parameter_list|)
block|{
name|buffer
operator|=
operator|new
name|byte
index|[
name|bufferSize
index|]
expr_stmt|;
name|available
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|write (byte[] data, int pos, int len)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|len
parameter_list|)
block|{
if|if
condition|(
name|available
operator|>=
name|len
condition|)
block|{
if|if
condition|(
name|write
operator|+
name|len
operator|>
name|buffer
operator|.
name|length
condition|)
block|{
name|int
name|overlap
init|=
name|write
operator|+
name|len
operator|%
name|buffer
operator|.
name|length
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|buffer
argument_list|,
name|write
argument_list|,
name|len
operator|-
name|overlap
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
name|len
operator|-
name|overlap
argument_list|,
name|buffer
argument_list|,
literal|0
argument_list|,
name|overlap
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|data
argument_list|,
name|pos
argument_list|,
name|buffer
argument_list|,
name|write
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
name|write
operator|=
operator|(
name|write
operator|+
name|len
operator|)
operator|%
name|buffer
operator|.
name|length
expr_stmt|;
name|available
operator|-=
name|len
expr_stmt|;
block|}
block|}
DECL|method|read (byte[] dest, int position, int len)
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|dest
parameter_list|,
name|int
name|position
parameter_list|,
name|int
name|len
parameter_list|)
block|{
if|if
condition|(
name|dest
operator|.
name|length
operator|-
name|position
operator|>=
name|len
condition|)
block|{
if|if
condition|(
name|buffer
operator|.
name|length
operator|-
name|available
operator|>=
name|len
condition|)
block|{
name|int
name|overlap
init|=
operator|(
name|read
operator|+
name|len
operator|)
operator|%
name|buffer
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|read
operator|>
name|write
condition|)
block|{
name|int
name|x
init|=
name|buffer
operator|.
name|length
operator|-
name|read
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|buffer
argument_list|,
name|read
argument_list|,
name|dest
argument_list|,
name|position
argument_list|,
name|buffer
operator|.
name|length
operator|-
name|read
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
name|dest
argument_list|,
name|position
operator|+
name|x
argument_list|,
name|overlap
argument_list|)
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
name|read
argument_list|,
name|dest
argument_list|,
name|position
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
name|read
operator|=
operator|(
name|read
operator|+
name|len
operator|)
operator|%
name|buffer
operator|.
name|length
expr_stmt|;
name|available
operator|+=
name|len
expr_stmt|;
return|return
name|len
return|;
block|}
block|}
return|return
literal|0
return|;
block|}
DECL|method|compareTo (byte[] compare, int pos, int len)
specifier|public
name|boolean
name|compareTo
parameter_list|(
name|byte
index|[]
name|compare
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|boolean
name|equal
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|len
operator|<=
name|availableForRead
argument_list|()
condition|)
block|{
name|int
name|x
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|equal
operator|&&
name|x
operator|<
name|len
condition|)
block|{
name|equal
operator|=
name|compare
index|[
name|pos
operator|+
name|x
index|]
operator|!=
name|buffer
index|[
name|read
operator|+
name|x
operator|%
name|buffer
operator|.
name|length
index|]
expr_stmt|;
block|}
block|}
return|return
name|equal
return|;
block|}
DECL|method|availableForRead ()
specifier|public
name|int
name|availableForRead
parameter_list|()
block|{
return|return
name|buffer
operator|.
name|length
operator|-
name|available
return|;
block|}
DECL|method|availableForWrite ()
specifier|public
name|int
name|availableForWrite
parameter_list|()
block|{
return|return
name|available
return|;
block|}
DECL|method|show ()
specifier|public
name|String
name|show
parameter_list|()
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|(
name|HexUtils
operator|.
name|byteArrayToHexString
argument_list|(
name|buffer
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
name|read
init|;
operator|--
name|x
operator|>=
literal|0
condition|;
control|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"--"
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"r"
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
name|write
init|;
operator|--
name|x
operator|>=
literal|0
condition|;
control|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"--"
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"w"
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

