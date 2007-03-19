begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|ExchangeConverter
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
name|seda
operator|.
name|SedaEndpoint
import|;
end_import

begin_comment
comment|/**  * A default implementation of {@link org.apache.camel.EndpointResolver}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultEndpointResolver
specifier|public
class|class
name|DefaultEndpointResolver
parameter_list|<
name|E
parameter_list|>
implements|implements
name|EndpointResolver
argument_list|<
name|E
argument_list|>
block|{
DECL|field|container
specifier|private
specifier|final
name|CamelContainer
name|container
decl_stmt|;
DECL|method|DefaultEndpointResolver (CamelContainer container)
specifier|public
name|DefaultEndpointResolver
parameter_list|(
name|CamelContainer
name|container
parameter_list|)
block|{
name|this
operator|.
name|container
operator|=
name|container
expr_stmt|;
block|}
DECL|method|resolve (String uri)
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|resolve
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
comment|// TODO we may want to cache them?
return|return
operator|new
name|SedaEndpoint
argument_list|<
name|E
argument_list|>
argument_list|(
name|uri
argument_list|,
name|container
argument_list|)
return|;
block|}
block|}
end_class

end_unit

