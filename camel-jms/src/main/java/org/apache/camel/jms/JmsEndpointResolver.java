begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|axis
operator|.
name|transport
operator|.
name|jms
operator|.
name|JMSEndpoint
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
name|CamelContainer
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
name|Component
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
name|EndpointResolver
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
name|queue
operator|.
name|QueueComponent
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link EndpointResolver} that creates  * {@link JMSEndpoint} objects.  *  * The syntax for a JMS URI looks like:  *  *<pre><code>jms:[component:]destination</code></pre>  * the component is optional, and if it is not specified, the default component name  * is assumed.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsEndpointResolver
specifier|public
class|class
name|JmsEndpointResolver
implements|implements
name|EndpointResolver
argument_list|<
name|JmsExchange
argument_list|>
block|{
DECL|field|DEFAULT_COMPONENT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_COMPONENT_NAME
init|=
name|QueueComponent
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/** 	 * Finds the {@see JmsComponent} specified by the uri.  If the {@see JmsComponent} 	 * object do not exist, it will be created. 	 */
DECL|method|resolveComponent (CamelContainer container, String uri)
specifier|public
name|Component
name|resolveComponent
parameter_list|(
name|CamelContainer
name|container
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|String
name|id
index|[]
init|=
name|getEndpointId
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|resolveJmsComponent
argument_list|(
name|container
argument_list|,
name|id
index|[
literal|0
index|]
argument_list|)
return|;
block|}
comment|/** 	 * Finds the {@see QueueEndpoint} specified by the uri.  If the {@see QueueEndpoint} or it's associated 	 * {@see QueueComponent} object do not exist, they will be created. 	 */
DECL|method|resolveEndpoint (CamelContainer container, String uri)
specifier|public
name|JmsEndpoint
name|resolveEndpoint
parameter_list|(
name|CamelContainer
name|container
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|String
name|id
index|[]
init|=
name|getEndpointId
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|JmsComponent
name|component
init|=
name|resolveJmsComponent
argument_list|(
name|container
argument_list|,
name|id
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
return|return
name|component
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|id
index|[
literal|1
index|]
argument_list|)
return|;
block|}
comment|/** 	 * @return an array that looks like: [componentName,endpointName]  	 */
DECL|method|getEndpointId (String uri)
specifier|private
name|String
index|[]
name|getEndpointId
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|String
name|rc
index|[]
init|=
block|{
name|DEFAULT_COMPONENT_NAME
block|,
literal|null
block|}
decl_stmt|;
name|String
name|splitURI
index|[]
init|=
name|ObjectHelper
operator|.
name|splitOnCharacter
argument_list|(
name|uri
argument_list|,
literal|":"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitURI
index|[
literal|2
index|]
operator|!=
literal|null
condition|)
block|{
name|rc
index|[
literal|0
index|]
operator|=
name|splitURI
index|[
literal|1
index|]
expr_stmt|;
name|rc
index|[
literal|1
index|]
operator|=
name|splitURI
index|[
literal|2
index|]
expr_stmt|;
block|}
else|else
block|{
name|rc
index|[
literal|1
index|]
operator|=
name|splitURI
index|[
literal|1
index|]
expr_stmt|;
block|}
return|return
name|rc
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|resolveJmsComponent (final CamelContainer container, final String componentName)
specifier|private
name|JmsComponent
name|resolveJmsComponent
parameter_list|(
specifier|final
name|CamelContainer
name|container
parameter_list|,
specifier|final
name|String
name|componentName
parameter_list|)
block|{
name|Component
name|rc
init|=
name|container
operator|.
name|getOrCreateComponent
argument_list|(
name|componentName
argument_list|,
operator|new
name|Callable
argument_list|<
name|JmsComponent
argument_list|>
argument_list|()
block|{
specifier|public
name|JmsComponent
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|JmsComponent
argument_list|(
name|container
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
operator|(
name|JmsComponent
operator|)
name|rc
return|;
block|}
block|}
end_class

end_unit

