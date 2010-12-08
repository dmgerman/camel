begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|ExchangePattern
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
name|impl
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
name|HeaderFilterStrategy
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
name|HeaderFilterStrategyAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Method
import|;
end_import

begin_comment
comment|/**  * Represents a<a href="http://www.restlet.org/"> endpoint</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RestletEndpoint
specifier|public
class|class
name|RestletEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|HeaderFilterStrategyAware
implements|,
name|Service
block|{
DECL|field|DEFAULT_PORT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_PORT
init|=
literal|80
decl_stmt|;
DECL|field|DEFAULT_PROTOCOL
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_PROTOCOL
init|=
literal|"http"
decl_stmt|;
DECL|field|DEFAULT_HOST
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_HOST
init|=
literal|"localhost"
decl_stmt|;
DECL|field|restletMethod
specifier|private
name|Method
name|restletMethod
init|=
name|Method
operator|.
name|GET
decl_stmt|;
comment|// Optional and for consumer only.  This allows a single route to service multiple
comment|// methods.  If it is non-null, restletMethod is ignored.
DECL|field|restletMethods
specifier|private
name|Method
index|[]
name|restletMethods
decl_stmt|;
DECL|field|protocol
specifier|private
name|String
name|protocol
init|=
name|DEFAULT_PROTOCOL
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
init|=
name|DEFAULT_HOST
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
init|=
name|DEFAULT_PORT
decl_stmt|;
DECL|field|uriPattern
specifier|private
name|String
name|uriPattern
decl_stmt|;
comment|// Optional and for consumer only.  This allows a single route to service multiple
comment|// URI patterns.  The URI pattern defined in the endpoint will still be honored.
DECL|field|restletUriPatterns
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|restletUriPatterns
decl_stmt|;
DECL|field|restletRealm
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|restletRealm
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|field|restletBinding
specifier|private
name|RestletBinding
name|restletBinding
decl_stmt|;
DECL|field|throwExceptionOnFailure
specifier|private
name|boolean
name|throwExceptionOnFailure
init|=
literal|true
decl_stmt|;
DECL|method|RestletEndpoint (RestletComponent component, String remaining)
specifier|public
name|RestletEndpoint
parameter_list|(
name|RestletComponent
name|component
parameter_list|,
name|String
name|remaining
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|remaining
argument_list|,
name|component
argument_list|)
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
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// true to allow dynamic URI options to be configured and passed to external system.
return|return
literal|true
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
name|RestletConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
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
name|RestletProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|connect (RestletConsumer restletConsumer)
specifier|public
name|void
name|connect
parameter_list|(
name|RestletConsumer
name|restletConsumer
parameter_list|)
throws|throws
name|Exception
block|{
operator|(
operator|(
name|RestletComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|connect
argument_list|(
name|restletConsumer
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (RestletConsumer restletConsumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|RestletConsumer
name|restletConsumer
parameter_list|)
throws|throws
name|Exception
block|{
operator|(
operator|(
name|RestletComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|disconnect
argument_list|(
name|restletConsumer
argument_list|)
expr_stmt|;
block|}
DECL|method|getRestletMethod ()
specifier|public
name|Method
name|getRestletMethod
parameter_list|()
block|{
return|return
name|restletMethod
return|;
block|}
DECL|method|setRestletMethod (Method restletMethod)
specifier|public
name|void
name|setRestletMethod
parameter_list|(
name|Method
name|restletMethod
parameter_list|)
block|{
name|this
operator|.
name|restletMethod
operator|=
name|restletMethod
expr_stmt|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getUriPattern ()
specifier|public
name|String
name|getUriPattern
parameter_list|()
block|{
return|return
name|uriPattern
return|;
block|}
DECL|method|setUriPattern (String uriPattern)
specifier|public
name|void
name|setUriPattern
parameter_list|(
name|String
name|uriPattern
parameter_list|)
block|{
name|this
operator|.
name|uriPattern
operator|=
name|uriPattern
expr_stmt|;
block|}
DECL|method|getRestletBinding ()
specifier|public
name|RestletBinding
name|getRestletBinding
parameter_list|()
block|{
return|return
name|restletBinding
return|;
block|}
DECL|method|setRestletBinding (RestletBinding restletBinding)
specifier|public
name|void
name|setRestletBinding
parameter_list|(
name|RestletBinding
name|restletBinding
parameter_list|)
block|{
name|this
operator|.
name|restletBinding
operator|=
name|restletBinding
expr_stmt|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
if|if
condition|(
name|restletBinding
operator|instanceof
name|HeaderFilterStrategyAware
condition|)
block|{
operator|(
operator|(
name|HeaderFilterStrategyAware
operator|)
name|restletBinding
operator|)
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setRestletRealm (Map<String, String> restletRealm)
specifier|public
name|void
name|setRestletRealm
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|restletRealm
parameter_list|)
block|{
name|this
operator|.
name|restletRealm
operator|=
name|restletRealm
expr_stmt|;
block|}
DECL|method|getRestletRealm ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getRestletRealm
parameter_list|()
block|{
return|return
name|restletRealm
return|;
block|}
annotation|@
name|Override
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
comment|// should always use in out for restlet
return|return
name|ExchangePattern
operator|.
name|InOut
return|;
block|}
DECL|method|setRestletMethods (Method[] restletMethods)
specifier|public
name|void
name|setRestletMethods
parameter_list|(
name|Method
index|[]
name|restletMethods
parameter_list|)
block|{
name|this
operator|.
name|restletMethods
operator|=
name|restletMethods
expr_stmt|;
block|}
DECL|method|getRestletMethods ()
specifier|public
name|Method
index|[]
name|getRestletMethods
parameter_list|()
block|{
return|return
name|restletMethods
return|;
block|}
DECL|method|setRestletUriPatterns (List<String> restletUriPatterns)
specifier|public
name|void
name|setRestletUriPatterns
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|restletUriPatterns
parameter_list|)
block|{
name|this
operator|.
name|restletUriPatterns
operator|=
name|restletUriPatterns
expr_stmt|;
block|}
DECL|method|getRestletUriPatterns ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRestletUriPatterns
parameter_list|()
block|{
return|return
name|restletUriPatterns
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|headerFilterStrategy
operator|==
literal|null
condition|)
block|{
name|headerFilterStrategy
operator|=
operator|new
name|RestletHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|restletBinding
operator|==
literal|null
condition|)
block|{
name|restletBinding
operator|=
operator|new
name|DefaultRestletBinding
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|restletBinding
operator|instanceof
name|HeaderFilterStrategyAware
condition|)
block|{
operator|(
operator|(
name|HeaderFilterStrategyAware
operator|)
name|restletBinding
operator|)
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|isThrowExceptionOnFailure ()
specifier|public
name|boolean
name|isThrowExceptionOnFailure
parameter_list|()
block|{
return|return
name|throwExceptionOnFailure
return|;
block|}
DECL|method|setThrowExceptionOnFailure (boolean throwExceptionOnFailure)
specifier|public
name|void
name|setThrowExceptionOnFailure
parameter_list|(
name|boolean
name|throwExceptionOnFailure
parameter_list|)
block|{
name|this
operator|.
name|throwExceptionOnFailure
operator|=
name|throwExceptionOnFailure
expr_stmt|;
block|}
block|}
end_class

end_unit

