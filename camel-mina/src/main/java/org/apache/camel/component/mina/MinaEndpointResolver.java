begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
package|;
end_package

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
name|camel
operator|.
name|component
operator|.
name|queue
operator|.
name|QueueComponent
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
name|URISyntaxException
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link EndpointResolver} that creates  * {@link MinaEndpoint} objects.  *  * The syntax for a MINA URI looks like:  *  *<pre><code>mina:</code></pre>  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|MinaEndpointResolver
specifier|public
class|class
name|MinaEndpointResolver
implements|implements
name|EndpointResolver
argument_list|<
name|MinaExchange
argument_list|>
block|{
DECL|field|DEFAULT_COMPONENT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_COMPONENT_NAME
init|=
name|MinaEndpointResolver
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/** 	 * Finds the {@link MinaComponent} specified by the uri.  If the {@link MinaComponent} 	 * object do not exist, it will be created. 	 */
DECL|method|resolveComponent (CamelContext container, String uri)
specifier|public
name|Component
name|resolveComponent
parameter_list|(
name|CamelContext
name|container
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|String
index|[]
name|id
init|=
name|getEndpointId
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|resolveMinaComponent
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
comment|/** 	 * Finds the {@link MinaEndpoint} specified by the uri.  If the {@link MinaEndpoint} or it's associated 	 * {@see QueueComponent} object do not exist, they will be created. 	 */
DECL|method|resolveEndpoint (CamelContext container, String uri)
specifier|public
name|MinaEndpoint
name|resolveEndpoint
parameter_list|(
name|CamelContext
name|container
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
name|String
index|[]
name|urlParts
init|=
name|getEndpointId
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|MinaComponent
name|component
init|=
name|resolveMinaComponent
argument_list|(
name|container
argument_list|,
name|urlParts
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
name|urlParts
argument_list|)
return|;
block|}
comment|/** 	 * @return an array that looks like: [componentName,endpointName] 	 */
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
DECL|method|resolveMinaComponent (final CamelContext context, final String componentName)
specifier|private
name|MinaComponent
name|resolveMinaComponent
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|,
specifier|final
name|String
name|componentName
parameter_list|)
block|{
name|Component
name|rc
init|=
name|context
operator|.
name|getOrCreateComponent
argument_list|(
name|componentName
argument_list|,
operator|new
name|Callable
argument_list|()
block|{
specifier|public
name|MinaComponent
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|MinaComponent
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
operator|(
name|MinaComponent
operator|)
name|rc
return|;
block|}
block|}
end_class

end_unit

