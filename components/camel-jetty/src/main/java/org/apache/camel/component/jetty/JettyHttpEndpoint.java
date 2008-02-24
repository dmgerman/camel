begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

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
name|Consumer
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
name|PollingConsumer
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
name|component
operator|.
name|http
operator|.
name|HttpConsumer
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
name|http
operator|.
name|HttpEndpoint
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
name|http
operator|.
name|HttpExchange
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
name|EventDrivenPollingConsumer
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
name|httpclient
operator|.
name|HttpConnectionManager
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JettyHttpEndpoint
specifier|public
class|class
name|JettyHttpEndpoint
extends|extends
name|HttpEndpoint
block|{
DECL|field|component
specifier|private
name|JettyHttpComponent
name|component
decl_stmt|;
DECL|method|JettyHttpEndpoint (JettyHttpComponent component, String uri, URI httpURL, HttpConnectionManager httpConnectionManager)
specifier|public
name|JettyHttpEndpoint
parameter_list|(
name|JettyHttpComponent
name|component
parameter_list|,
name|String
name|uri
parameter_list|,
name|URI
name|httpURL
parameter_list|,
name|HttpConnectionManager
name|httpConnectionManager
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|httpURL
argument_list|,
name|httpConnectionManager
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|HttpExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|super
operator|.
name|createProducer
argument_list|()
return|;
comment|// return new JettyHttpProducer(this);
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
argument_list|<
name|HttpExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|HttpConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
argument_list|<
name|HttpExchange
argument_list|>
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|EventDrivenPollingConsumer
argument_list|<
name|HttpExchange
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|JettyHttpComponent
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
block|}
end_class

end_unit

