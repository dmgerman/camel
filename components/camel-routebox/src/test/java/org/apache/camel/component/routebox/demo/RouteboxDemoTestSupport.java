begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox.demo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
operator|.
name|demo
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
name|ProducerTemplate
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
name|routebox
operator|.
name|strategy
operator|.
name|RouteboxDispatchStrategy
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_class
DECL|class|RouteboxDemoTestSupport
specifier|public
class|class
name|RouteboxDemoTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|method|sendAddToCatalogRequest (ProducerTemplate template, String endpointUri, String operation, Book book)
specifier|public
name|String
name|sendAddToCatalogRequest
parameter_list|(
name|ProducerTemplate
name|template
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|String
name|operation
parameter_list|,
name|Book
name|book
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|response
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
name|book
argument_list|,
literal|"ROUTE_DISPATCH_KEY"
argument_list|,
name|operation
argument_list|)
decl_stmt|;
return|return
name|response
return|;
block|}
DECL|method|sendFindBookRequest (ProducerTemplate template, String endpointUri, String operation, String body)
specifier|public
name|Book
name|sendFindBookRequest
parameter_list|(
name|ProducerTemplate
name|template
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|String
name|operation
parameter_list|,
name|String
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|Book
name|response
init|=
operator|(
name|Book
operator|)
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
literal|"ROUTE_DISPATCH_KEY"
argument_list|,
name|operation
argument_list|)
decl_stmt|;
return|return
name|response
return|;
block|}
DECL|class|SimpleRouteDispatchStrategy
specifier|public
class|class
name|SimpleRouteDispatchStrategy
implements|implements
name|RouteboxDispatchStrategy
block|{
comment|/* (non-Javadoc)          * @see org.apache.camel.component.routebox.strategy.RouteboxDispatchStrategy#selectDestinationUri(java.util.List, org.apache.camel.Exchange)          */
DECL|method|selectDestinationUri (List<URI> destinations, Exchange exchange)
specifier|public
name|URI
name|selectDestinationUri
parameter_list|(
name|List
argument_list|<
name|URI
argument_list|>
name|destinations
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|URI
name|dispatchDestination
init|=
literal|null
decl_stmt|;
name|String
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"ROUTE_DISPATCH_KEY"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|URI
name|destination
range|:
name|destinations
control|)
block|{
if|if
condition|(
name|destination
operator|.
name|toASCIIString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"seda:"
operator|+
name|operation
argument_list|)
condition|)
block|{
name|dispatchDestination
operator|=
name|destination
expr_stmt|;
break|break;
block|}
block|}
return|return
name|dispatchDestination
return|;
block|}
block|}
block|}
end_class

end_unit

