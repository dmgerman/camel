begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|DefaultProducer
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_comment
comment|/**  * Sends messages from Camel into the CXF endpoint  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfProducer
specifier|public
class|class
name|CxfProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
name|CxfEndpoint
name|endpoint
decl_stmt|;
DECL|field|client
specifier|private
name|Client
name|client
decl_stmt|;
DECL|field|conduit
specifier|private
name|Conduit
name|conduit
decl_stmt|;
DECL|method|CxfProducer (CxfEndpoint endpoint)
specifier|public
name|CxfProducer
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|)
throws|throws
name|MalformedURLException
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
name|client
operator|=
name|createClient
argument_list|()
expr_stmt|;
block|}
DECL|method|createClient ()
specifier|private
name|Client
name|createClient
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
comment|// setup the ClientFactoryBean with endpoint
name|ClientFactoryBean
name|cfb
init|=
operator|new
name|ClientFactoryBean
argument_list|()
decl_stmt|;
name|cfb
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|cfb
operator|.
name|setAddress
argument_list|(
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|endpoint
operator|.
name|getServiceClass
argument_list|()
condition|)
block|{
name|cfb
operator|.
name|setServiceClass
argument_list|(
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
condition|)
block|{
name|cfb
operator|.
name|setWsdlURL
argument_list|(
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// there may other setting work
comment|// create client
return|return
name|cfb
operator|.
name|create
argument_list|()
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|CxfExchange
name|cxfExchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|process
argument_list|(
name|cxfExchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (CxfExchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|CxfExchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|CxfBinding
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|MessageImpl
name|m
init|=
name|binding
operator|.
name|createCxfMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|//InputStream is = m.getContent(InputStream.class);
comment|// now we just deal with the POJO invocations
name|List
name|paraments
init|=
name|m
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|response
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|paraments
operator|!=
literal|null
condition|)
block|{
name|String
name|operation
init|=
operator|(
name|String
operator|)
name|paraments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
index|[]
name|args
init|=
operator|new
name|Object
index|[
name|paraments
operator|.
name|size
argument_list|()
operator|-
literal|1
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|paraments
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|args
index|[
name|i
index|]
operator|=
name|paraments
operator|.
name|get
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// now we just deal with the invoking the paraments
name|Object
index|[]
name|result
init|=
name|client
operator|.
name|invoke
argument_list|(
name|operation
argument_list|,
name|args
argument_list|)
decl_stmt|;
name|response
operator|.
name|setContent
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|binding
operator|.
name|storeCxfResponse
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
name|client
operator|=
name|createClient
argument_list|()
expr_stmt|;
name|conduit
operator|=
name|client
operator|.
name|getConduit
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

