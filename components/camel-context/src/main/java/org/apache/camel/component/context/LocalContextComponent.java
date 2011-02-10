begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.context
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|context
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
name|Endpoint
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
name|NoSuchEndpointException
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
name|DefaultComponent
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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

begin_comment
comment|/**  * A Camel Component which exposes a local {@link CamelContext} instance as a black box set of endpoints.  */
end_comment

begin_class
DECL|class|LocalContextComponent
specifier|public
class|class
name|LocalContextComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LocalContextComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|localCamelContext
specifier|private
name|CamelContext
name|localCamelContext
decl_stmt|;
DECL|field|localProtocolSchemes
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|localProtocolSchemes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"direct"
argument_list|,
literal|"seda"
argument_list|)
argument_list|)
decl_stmt|;
DECL|method|LocalContextComponent (CamelContext localCamelContext)
specifier|public
name|LocalContextComponent
parameter_list|(
name|CamelContext
name|localCamelContext
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|localCamelContext
argument_list|,
literal|"localCamelContext"
argument_list|)
expr_stmt|;
name|this
operator|.
name|localCamelContext
operator|=
name|localCamelContext
expr_stmt|;
block|}
DECL|method|getLocalProtocolSchemes ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getLocalProtocolSchemes
parameter_list|()
block|{
return|return
name|localProtocolSchemes
return|;
block|}
comment|/**      * Sets the list of protocols which are used to expose public endpoints by default      */
DECL|method|setLocalProtocolSchemes (List<String> localProtocolSchemes)
specifier|public
name|void
name|setLocalProtocolSchemes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|localProtocolSchemes
parameter_list|)
block|{
name|this
operator|.
name|localProtocolSchemes
operator|=
name|localProtocolSchemes
expr_stmt|;
block|}
DECL|method|getLocalCamelContext ()
specifier|public
name|CamelContext
name|getLocalCamelContext
parameter_list|()
block|{
if|if
condition|(
name|localCamelContext
operator|==
literal|null
condition|)
block|{          }
return|return
name|localCamelContext
return|;
block|}
DECL|method|setLocalCamelContext (CamelContext localCamelContext)
specifier|public
name|void
name|setLocalCamelContext
parameter_list|(
name|CamelContext
name|localCamelContext
parameter_list|)
block|{
name|this
operator|.
name|localCamelContext
operator|=
name|localCamelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets first check if we are using a fully qualified name: [context:]contextId:endpointUri
name|Map
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|map
init|=
name|getLocalCamelContext
argument_list|()
operator|.
name|getEndpointMap
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|map
operator|.
name|get
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|logUsingEndpoint
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|// lets look to see if there is an endpoint of name 'remaining' using one of the local endpoints within
comment|// the black box CamelContext
name|String
index|[]
name|separators
init|=
block|{
literal|":"
block|,
literal|"://"
block|}
decl_stmt|;
for|for
control|(
name|String
name|scheme
range|:
name|localProtocolSchemes
control|)
block|{
for|for
control|(
name|String
name|separator
range|:
name|separators
control|)
block|{
name|String
name|newUri
init|=
name|scheme
operator|+
name|separator
operator|+
name|remaining
decl_stmt|;
name|endpoint
operator|=
name|map
operator|.
name|get
argument_list|(
name|newUri
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|logUsingEndpoint
argument_list|(
name|uri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|logUsingEndpoint (String uri, Endpoint endpoint)
specifier|protected
name|void
name|logUsingEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Mapping the URI "
operator|+
name|uri
operator|+
literal|" to local endpoint "
operator|+
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

