begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.splunk.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|splunk
operator|.
name|support
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
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|com
operator|.
name|splunk
operator|.
name|Args
import|;
end_import

begin_import
import|import
name|com
operator|.
name|splunk
operator|.
name|Service
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
name|component
operator|.
name|splunk
operator|.
name|SplunkEndpoint
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
name|component
operator|.
name|splunk
operator|.
name|event
operator|.
name|SplunkEvent
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

begin_class
DECL|class|SplunkDataWriter
specifier|public
specifier|abstract
class|class
name|SplunkDataWriter
implements|implements
name|DataWriter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SplunkDataWriter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|SplunkEndpoint
name|endpoint
decl_stmt|;
DECL|field|args
specifier|protected
name|Args
name|args
decl_stmt|;
DECL|field|connected
specifier|private
name|boolean
name|connected
decl_stmt|;
DECL|field|socket
specifier|private
name|Socket
name|socket
decl_stmt|;
DECL|method|SplunkDataWriter (SplunkEndpoint endpoint, Args args)
specifier|public
name|SplunkDataWriter
parameter_list|(
name|SplunkEndpoint
name|endpoint
parameter_list|,
name|Args
name|args
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
DECL|method|createSocket (Service service)
specifier|protected
specifier|abstract
name|Socket
name|createSocket
parameter_list|(
name|Service
name|service
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|write (SplunkEvent event)
specifier|public
name|void
name|write
parameter_list|(
name|SplunkEvent
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|doWrite
argument_list|(
name|event
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|write (String event)
specifier|public
name|void
name|write
parameter_list|(
name|String
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|doWrite
argument_list|(
name|event
operator|+
name|SplunkEvent
operator|.
name|LINEBREAK
argument_list|)
expr_stmt|;
block|}
DECL|method|doWrite (String event)
specifier|protected
specifier|synchronized
name|void
name|doWrite
parameter_list|(
name|String
name|event
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"writing event to splunk:{}"
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|OutputStream
name|ostream
init|=
name|socket
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|Writer
name|writer
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|ostream
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
specifier|synchronized
name|void
name|start
parameter_list|()
block|{
try|try
block|{
name|socket
operator|=
name|createSocket
argument_list|(
name|endpoint
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|connected
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|connected
operator|=
literal|false
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
specifier|synchronized
name|void
name|stop
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|socket
operator|!=
literal|null
condition|)
block|{
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
name|connected
operator|=
literal|false
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|isConnected ()
specifier|public
name|boolean
name|isConnected
parameter_list|()
block|{
return|return
name|connected
return|;
block|}
block|}
end_class

end_unit

