begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * Represents a<a href="http://activemq.apache.org/camel/http.html">HTTP endpoint</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpEndpoint
specifier|public
class|class
name|HttpEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|HttpExchange
argument_list|>
block|{
DECL|field|binding
specifier|private
name|HttpBinding
name|binding
decl_stmt|;
DECL|field|component
specifier|private
name|HttpComponent
name|component
decl_stmt|;
DECL|field|httpUri
specifier|private
name|URI
name|httpUri
decl_stmt|;
DECL|method|HttpEndpoint (String uri, HttpComponent component)
specifier|protected
name|HttpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|HttpComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|httpUri
operator|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|HttpProducer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|HttpProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
DECL|method|createExchange ()
specifier|public
name|HttpExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|HttpExchange
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createExchange (HttpServletRequest request, HttpServletResponse response)
specifier|public
name|HttpExchange
name|createExchange
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
return|return
operator|new
name|HttpExchange
argument_list|(
name|this
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|HttpBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|HttpBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
DECL|method|setBinding (HttpBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|HttpBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
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
DECL|method|connect (HttpConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|connect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (HttpConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|component
operator|.
name|disconnect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|httpUri
operator|.
name|getPath
argument_list|()
return|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
if|if
condition|(
name|httpUri
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
literal|"https"
operator|.
name|equals
argument_list|(
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|443
return|;
block|}
else|else
block|{
return|return
literal|80
return|;
block|}
block|}
return|return
name|httpUri
operator|.
name|getPort
argument_list|()
return|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|httpUri
operator|.
name|getScheme
argument_list|()
return|;
block|}
block|}
end_class

end_unit

