begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *   * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE  * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file  * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the  * License. You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the  * specific language governing permissions and limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|RandomAccessFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|FileChannel
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
name|Producer
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
name|DefaultProducer
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
comment|/**  * A {@link Producer} implementation for MINA  *   * @version $Revision: 523016 $  */
end_comment

begin_class
DECL|class|FileProducer
specifier|public
class|class
name|FileProducer
extends|extends
name|DefaultProducer
argument_list|<
name|FileExchange
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FileProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|FileEndpoint
name|endpoint
decl_stmt|;
DECL|method|FileProducer (FileEndpoint endpoint)
specifier|public
name|FileProducer
parameter_list|(
name|FileEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
comment|/**      * @param arg0      * @see org.apache.camel.Processor#process(java.lang.Object)      */
DECL|method|process (FileExchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|FileExchange
name|exchange
parameter_list|)
block|{
name|ByteBuffer
name|payload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|)
decl_stmt|;
name|File
name|file
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getFile
argument_list|()
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getFile
argument_list|()
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|endpoint
operator|.
name|getFile
argument_list|()
argument_list|,
name|exchange
operator|.
name|getFile
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|file
operator|=
name|exchange
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|FileChannel
name|fc
init|=
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"rw"
argument_list|)
operator|.
name|getChannel
argument_list|()
decl_stmt|;
name|fc
operator|.
name|position
argument_list|(
name|fc
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|fc
operator|.
name|write
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|fc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to write to File: "
operator|+
name|file
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

