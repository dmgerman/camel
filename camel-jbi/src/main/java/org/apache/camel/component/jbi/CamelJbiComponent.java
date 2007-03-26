begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE  * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file  * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the  * License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the  * specific language governing permissions and limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbi
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
name|EndpointResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|common
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
name|servicemix
operator|.
name|common
operator|.
name|ServiceUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|jbi
operator|.
name|util
operator|.
name|IntrospectionSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|jbi
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|jbi
operator|.
name|resolver
operator|.
name|URIResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|servicedesc
operator|.
name|ServiceEndpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
comment|/**  * Deploys the camel endpoints within JBI  *  * @version $Revision: 426415 $  */
end_comment

begin_class
DECL|class|CamelJbiComponent
specifier|public
class|class
name|CamelJbiComponent
extends|extends
name|DefaultComponent
implements|implements
name|Component
argument_list|<
name|JbiExchange
argument_list|>
implements|,
name|EndpointResolver
block|{
DECL|field|binding
specifier|private
name|JbiBinding
name|binding
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
comment|/**      * @return List of endpoints      * @see org.apache.servicemix.common.DefaultComponent#getConfiguredEndpoints()      */
annotation|@
name|Override
DECL|method|getConfiguredEndpoints ()
specifier|protected
name|List
argument_list|<
name|CamelJbiEndpoint
argument_list|>
name|getConfiguredEndpoints
parameter_list|()
block|{
comment|// TODO need to register to the context for new endpoints...
name|List
argument_list|<
name|CamelJbiEndpoint
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|CamelJbiEndpoint
argument_list|>
argument_list|()
decl_stmt|;
comment|//        Collection<Endpoint> endpoints = camelContext.getEndpoints();
comment|//        for (Endpoint endpoint : endpoints) {
comment|//          answer.add(createJbiEndpoint(endpoint));
comment|//        }
return|return
name|answer
return|;
block|}
comment|/**      * @return Class[]      * @see org.apache.servicemix.common.DefaultComponent#getEndpointClasses()      */
annotation|@
name|Override
DECL|method|getEndpointClasses ()
specifier|protected
name|Class
index|[]
name|getEndpointClasses
parameter_list|()
block|{
return|return
operator|new
name|Class
index|[]
block|{
name|CamelJbiEndpoint
operator|.
name|class
block|}
return|;
block|}
comment|/**      * @return the binding      */
DECL|method|getBinding ()
specifier|public
name|JbiBinding
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
name|JbiBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * @param binding the binding to set      */
DECL|method|setBinding (JbiBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|JbiBinding
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
annotation|@
name|Override
DECL|method|getEPRProtocols ()
specifier|protected
name|String
index|[]
name|getEPRProtocols
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"camel"
block|}
return|;
block|}
DECL|method|getResolvedEPR (ServiceEndpoint ep)
specifier|protected
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|common
operator|.
name|Endpoint
name|getResolvedEPR
parameter_list|(
name|ServiceEndpoint
name|ep
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelJbiEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|ep
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|activate
argument_list|()
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * A factory method for creating endpoints from a service endpoint      * which is public so that it can be easily unit tested      */
DECL|method|createEndpoint (ServiceEndpoint ep)
specifier|public
name|CamelJbiEndpoint
name|createEndpoint
parameter_list|(
name|ServiceEndpoint
name|ep
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|ep
operator|.
name|getEndpointName
argument_list|()
argument_list|)
decl_stmt|;
name|Map
name|map
init|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|uri
operator|.
name|getQuery
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|camelUri
init|=
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
name|Endpoint
name|camelEndpoint
init|=
name|getCamelContext
argument_list|()
operator|.
name|resolveEndpoint
argument_list|(
name|camelUri
argument_list|)
decl_stmt|;
name|CamelJbiEndpoint
name|endpoint
init|=
operator|new
name|CamelJbiEndpoint
argument_list|(
name|getServiceUnit
argument_list|()
argument_list|,
name|camelEndpoint
argument_list|,
name|getBinding
argument_list|()
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|map
argument_list|)
expr_stmt|;
comment|// TODO
comment|//endpoint.setRole(MessageExchange.Role.PROVIDER);
return|return
name|endpoint
return|;
block|}
comment|// Resolve Camel Endpoints
comment|//-------------------------------------------------------------------------
DECL|method|resolveComponent (CamelContext context, String uri)
specifier|public
name|Component
name|resolveComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
DECL|method|resolveEndpoint (CamelContext context, String uri)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"jbi:"
argument_list|)
condition|)
block|{
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|"jbi:"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|JbiEndpoint
argument_list|(
name|this
argument_list|,
name|uri
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
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
comment|/**      * Returns a JBI endpoint created for the given Camel endpoint      */
DECL|method|activateJbiEndpoint (JbiEndpoint camelEndpoint)
specifier|public
name|CamelJbiEndpoint
name|activateJbiEndpoint
parameter_list|(
name|JbiEndpoint
name|camelEndpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelJbiEndpoint
name|jbiEndpoint
init|=
literal|null
decl_stmt|;
name|String
name|endpointUri
init|=
name|camelEndpoint
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpointUri
operator|.
name|startsWith
argument_list|(
literal|"service:"
argument_list|)
condition|)
block|{
comment|// lets decode "service:serviceNamespace:serviceName:endpointName
name|String
name|uri
init|=
name|endpointUri
operator|.
name|substring
argument_list|(
literal|"service:"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts
init|=
operator|new
name|String
index|[
literal|0
index|]
decl_stmt|;
try|try
block|{
name|parts
operator|=
name|URIResolver
operator|.
name|split3
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected syntax service:serviceNamespace:serviceName:endpointName but was given: "
operator|+
name|endpointUri
operator|+
literal|". Cause: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|QName
name|service
init|=
operator|new
name|QName
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|String
name|endpoint
init|=
name|parts
index|[
literal|2
index|]
decl_stmt|;
name|jbiEndpoint
operator|=
operator|new
name|CamelJbiEndpoint
argument_list|(
name|getServiceUnit
argument_list|()
argument_list|,
name|service
argument_list|,
name|endpoint
argument_list|,
name|camelEndpoint
argument_list|,
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jbiEndpoint
operator|=
operator|new
name|CamelJbiEndpoint
argument_list|(
name|getServiceUnit
argument_list|()
argument_list|,
name|camelEndpoint
argument_list|,
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// the following method will activate the new dynamic JBI endpoint
name|addEndpoint
argument_list|(
name|jbiEndpoint
argument_list|)
expr_stmt|;
return|return
name|jbiEndpoint
return|;
block|}
block|}
end_class

end_unit

