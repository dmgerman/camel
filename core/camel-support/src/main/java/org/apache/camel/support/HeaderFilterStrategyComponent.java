begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Base class for components to support configuring a {@link org.apache.camel.spi.HeaderFilterStrategy}.  */
end_comment

begin_class
DECL|class|HeaderFilterStrategyComponent
specifier|public
specifier|abstract
class|class
name|HeaderFilterStrategyComponent
extends|extends
name|DefaultComponent
implements|implements
name|HeaderFilterStrategyAware
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|,
name|description
operator|=
literal|"To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter header to and from Camel message."
argument_list|)
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|HeaderFilterStrategyComponent ()
specifier|public
name|HeaderFilterStrategyComponent
parameter_list|()
block|{     }
DECL|method|HeaderFilterStrategyComponent (CamelContext context)
specifier|public
name|HeaderFilterStrategyComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
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
comment|/**      * To use a custom {@link org.apache.camel.spi.HeaderFilterStrategy} to filter header to and from Camel message.      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
name|headerFilterStrategy
operator|=
name|strategy
expr_stmt|;
block|}
comment|/**      * Sets the header filter strategy to use from the given endpoint if the endpoint is a {@link HeaderFilterStrategyAware} type.      */
DECL|method|setEndpointHeaderFilterStrategy (Endpoint endpoint)
specifier|public
name|void
name|setEndpointHeaderFilterStrategy
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
name|endpoint
operator|instanceof
name|HeaderFilterStrategyAware
condition|)
block|{
operator|(
operator|(
name|HeaderFilterStrategyAware
operator|)
name|endpoint
operator|)
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

