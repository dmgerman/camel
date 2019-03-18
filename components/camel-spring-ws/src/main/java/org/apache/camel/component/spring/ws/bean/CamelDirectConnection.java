begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|bean
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|CamelContext
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
name|CamelExecutionException
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|WebServiceMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|WebServiceMessageFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|WebServiceConnection
import|;
end_import

begin_comment
comment|/**  * Passes wsa:replyTo message back to the camel routing  */
end_comment

begin_class
DECL|class|CamelDirectConnection
specifier|public
class|class
name|CamelDirectConnection
implements|implements
name|WebServiceConnection
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
name|CamelDirectConnection
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|destination
specifier|private
name|URI
name|destination
decl_stmt|;
DECL|method|CamelDirectConnection (CamelContext camelContext, URI uri)
specifier|public
name|CamelDirectConnection
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|URI
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|destination
operator|=
operator|new
name|URI
argument_list|(
literal|"direct:"
operator|+
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|send (WebServiceMessage message)
specifier|public
name|void
name|send
parameter_list|(
name|WebServiceMessage
name|message
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
name|destination
operator|.
name|toString
argument_list|()
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
comment|// simply discard replyTo message
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not found any camel endpoint ["
operator|+
name|destination
operator|+
literal|"] for wsa:ReplyTo camel mapping."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|receive (WebServiceMessageFactory messageFactory)
specifier|public
name|WebServiceMessage
name|receive
parameter_list|(
name|WebServiceMessageFactory
name|messageFactory
parameter_list|)
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
throws|throws
name|URISyntaxException
block|{
return|return
name|destination
return|;
block|}
annotation|@
name|Override
DECL|method|hasError ()
specifier|public
name|boolean
name|hasError
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getErrorMessage ()
specifier|public
name|String
name|getErrorMessage
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
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
block|{      }
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
block|}
end_class

end_unit

