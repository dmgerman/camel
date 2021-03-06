begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
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
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageEOFException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|StreamMessage
import|;
end_import

begin_class
DECL|class|StreamMessageInputStream
specifier|public
class|class
name|StreamMessageInputStream
extends|extends
name|InputStream
block|{
DECL|field|message
specifier|private
specifier|final
name|StreamMessage
name|message
decl_stmt|;
DECL|field|eof
specifier|private
specifier|volatile
name|boolean
name|eof
decl_stmt|;
DECL|method|StreamMessageInputStream (StreamMessage message)
specifier|public
name|StreamMessageInputStream
parameter_list|(
name|StreamMessage
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
return|return
name|message
operator|.
name|readByte
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MessageEOFException
name|e
parameter_list|)
block|{
name|eof
operator|=
literal|true
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|read (byte[] array)
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|array
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|int
name|num
init|=
name|message
operator|.
name|readBytes
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|eof
operator|=
name|num
operator|<
literal|0
expr_stmt|;
return|return
name|num
return|;
block|}
catch|catch
parameter_list|(
name|MessageEOFException
name|e
parameter_list|)
block|{
name|eof
operator|=
literal|true
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|read (byte[] array, int off, int len)
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
comment|// we cannot honor off and len, but assuming off is always 0
try|try
block|{
name|int
name|num
init|=
name|message
operator|.
name|readBytes
argument_list|(
name|array
argument_list|)
decl_stmt|;
name|eof
operator|=
name|num
operator|<
literal|0
expr_stmt|;
return|return
name|num
return|;
block|}
catch|catch
parameter_list|(
name|MessageEOFException
name|e
parameter_list|)
block|{
name|eof
operator|=
literal|true
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|message
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|available ()
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
comment|// if we are end of file then there is no more data, otherwise assume there is at least one more byte
return|return
name|eof
condition|?
literal|0
else|:
literal|1
return|;
block|}
block|}
end_class

end_unit

