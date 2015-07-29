begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpHandler
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
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
name|undertow
operator|.
name|handlers
operator|.
name|HttpCamelHandler
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
name|DefaultConsumer
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
comment|/**  * The Undertow consumer.  */
end_comment

begin_class
DECL|class|UndertowConsumer
specifier|public
class|class
name|UndertowConsumer
extends|extends
name|DefaultConsumer
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
name|UndertowConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|UndertowConsumer (UndertowEndpoint endpoint, Processor processor)
specifier|public
name|UndertowConsumer
parameter_list|(
name|UndertowEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|UndertowEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|UndertowEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Undertow consumer is starting"
argument_list|)
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|registerConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|UndertowHostFactory
name|factory
init|=
name|UndertowHostFactory
operator|.
name|Locator
operator|.
name|getUndertowHostFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|factory
operator|=
operator|new
name|DefaultUndertowHostFactory
argument_list|()
expr_stmt|;
block|}
name|URI
name|httpUri
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHttpURI
argument_list|()
decl_stmt|;
name|UndertowHost
name|host
init|=
name|factory
operator|.
name|createUndertowHost
argument_list|()
decl_stmt|;
name|host
operator|.
name|validateEndpointURI
argument_list|(
name|httpUri
argument_list|)
expr_stmt|;
name|host
operator|.
name|registerHandler
argument_list|(
name|httpUri
operator|.
name|getPath
argument_list|()
argument_list|,
operator|new
name|HttpCamelHandler
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Undertow consumer is stopping"
argument_list|)
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|unregisterConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|class|DefaultUndertowHostFactory
class|class
name|DefaultUndertowHostFactory
implements|implements
name|UndertowHostFactory
block|{
annotation|@
name|Override
DECL|method|createUndertowHost ()
specifier|public
name|UndertowHost
name|createUndertowHost
parameter_list|()
block|{
return|return
operator|new
name|UndertowHost
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|validateEndpointURI
parameter_list|(
name|URI
name|httpURI
parameter_list|)
block|{
comment|// all URIs are good
block|}
annotation|@
name|Override
specifier|public
name|void
name|registerHandler
parameter_list|(
name|String
name|path
parameter_list|,
name|HttpHandler
name|handler
parameter_list|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|startServer
argument_list|(
name|UndertowConsumer
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

