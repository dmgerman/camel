begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.coap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|coap
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
name|support
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapServer
import|;
end_import

begin_comment
comment|/**  * The coap component is used for sending and receiving messages from COAP capable devices.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"coap"
argument_list|,
name|title
operator|=
literal|"CoAP"
argument_list|,
name|syntax
operator|=
literal|"coap:uri"
argument_list|,
name|label
operator|=
literal|"iot"
argument_list|)
DECL|class|CoAPEndpoint
specifier|public
class|class
name|CoAPEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|coapMethodRestrict
specifier|private
name|String
name|coapMethodRestrict
decl_stmt|;
DECL|field|component
specifier|private
name|CoAPComponent
name|component
decl_stmt|;
DECL|method|CoAPEndpoint (String uri, CoAPComponent component)
specifier|public
name|CoAPEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CoAPComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|net
operator|.
name|URISyntaxException
name|use
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
block|}
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
DECL|method|setCoapMethodRestrict (String coapMethodRestrict)
specifier|public
name|void
name|setCoapMethodRestrict
parameter_list|(
name|String
name|coapMethodRestrict
parameter_list|)
block|{
name|this
operator|.
name|coapMethodRestrict
operator|=
name|coapMethodRestrict
expr_stmt|;
block|}
comment|/**      * Comma separated list of methods that the CoAP consumer will bind to. The default is to bind to all methods (DELETE, GET, POST, PUT).      */
DECL|method|getCoapMethodRestrict ()
specifier|public
name|String
name|getCoapMethodRestrict
parameter_list|()
block|{
return|return
name|this
operator|.
name|coapMethodRestrict
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CoAPProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
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
name|CoAPConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|setUri (URI u)
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
name|u
parameter_list|)
block|{
name|uri
operator|=
name|u
expr_stmt|;
block|}
comment|/**      * The URI for the CoAP endpoint      */
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|getCoapServer ()
specifier|public
name|CoapServer
name|getCoapServer
parameter_list|()
block|{
return|return
name|component
operator|.
name|getServer
argument_list|(
name|getUri
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

