begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.zipfile
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|zipfile
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The Iterator which can go through the ZipInputStream according to ZipEntry  * Based on the thread<a href="http://camel.465427.n5.nabble.com/zip-file-best-practices-td5713437.html">zip file best practices</a>  */
end_comment

begin_class
DECL|class|ZipIterator
specifier|public
class|class
name|ZipIterator
implements|implements
name|Iterator
argument_list|<
name|Message
argument_list|>
implements|,
name|Closeable
block|{
DECL|field|LOGGER
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ZipIterator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|inputMessage
specifier|private
specifier|final
name|Message
name|inputMessage
decl_stmt|;
DECL|field|zipInputStream
specifier|private
specifier|volatile
name|ZipInputStream
name|zipInputStream
decl_stmt|;
DECL|field|parent
specifier|private
specifier|volatile
name|Message
name|parent
decl_stmt|;
DECL|method|ZipIterator (Message inputMessage)
specifier|public
name|ZipIterator
parameter_list|(
name|Message
name|inputMessage
parameter_list|)
block|{
name|this
operator|.
name|inputMessage
operator|=
name|inputMessage
expr_stmt|;
name|InputStream
name|inputStream
init|=
name|inputMessage
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|inputStream
operator|instanceof
name|ZipInputStream
condition|)
block|{
name|zipInputStream
operator|=
operator|(
name|ZipInputStream
operator|)
name|inputStream
expr_stmt|;
block|}
else|else
block|{
name|zipInputStream
operator|=
operator|new
name|ZipInputStream
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
name|inputStream
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|parent
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|zipInputStream
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|availableDataInCurrentEntry
init|=
name|zipInputStream
operator|.
name|available
argument_list|()
operator|==
literal|1
decl_stmt|;
if|if
condition|(
operator|!
name|availableDataInCurrentEntry
condition|)
block|{
comment|// advance to the next entry.
name|parent
operator|=
name|getNextElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|zipInputStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|availableDataInCurrentEntry
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|availableDataInCurrentEntry
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|availableDataInCurrentEntry
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
comment|//Just wrap the IOException as CamelRuntimeException
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|Message
name|next
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|parent
operator|=
name|getNextElement
argument_list|()
expr_stmt|;
block|}
name|Message
name|answer
init|=
name|parent
decl_stmt|;
name|parent
operator|=
literal|null
expr_stmt|;
name|checkNullAnswer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getNextElement ()
specifier|private
name|Message
name|getNextElement
parameter_list|()
block|{
if|if
condition|(
name|zipInputStream
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|ZipEntry
name|current
init|=
name|getNextEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"read zipEntry {}"
argument_list|,
name|current
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|answer
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|answer
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|inputMessage
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
literal|"zipFileName"
argument_list|,
name|current
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|current
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
operator|new
name|ZipInputStreamWrapper
argument_list|(
name|zipInputStream
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"close zipInputStream"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
comment|//Just wrap the IOException as CamelRuntimeException
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
block|}
DECL|method|checkNullAnswer (Message answer)
specifier|public
name|void
name|checkNullAnswer
parameter_list|(
name|Message
name|answer
parameter_list|)
block|{
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|zipInputStream
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|zipInputStream
argument_list|)
expr_stmt|;
name|zipInputStream
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getNextEntry ()
specifier|private
name|ZipEntry
name|getNextEntry
parameter_list|()
throws|throws
name|IOException
block|{
name|ZipEntry
name|entry
decl_stmt|;
while|while
condition|(
operator|(
name|entry
operator|=
name|zipInputStream
operator|.
name|getNextEntry
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|entry
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
name|entry
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|zipInputStream
argument_list|)
expr_stmt|;
name|zipInputStream
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

