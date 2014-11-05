begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
package|;
end_package

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
name|component
operator|.
name|seda
operator|.
name|SedaEndpoint
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
name|ActiveMQUuidGenerator
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
name|RestConsumerFactory
import|;
end_import

begin_class
DECL|class|DummyRestConsumerFactory
specifier|public
class|class
name|DummyRestConsumerFactory
implements|implements
name|RestConsumerFactory
block|{
annotation|@
name|Override
DECL|method|createConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
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
comment|// just use a seda endpoint for testing purpose
name|String
name|id
decl_stmt|;
if|if
condition|(
name|uriTemplate
operator|!=
literal|null
condition|)
block|{
name|id
operator|=
name|ActiveMQUuidGenerator
operator|.
name|generateSanitizedId
argument_list|(
name|basePath
operator|+
name|uriTemplate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|id
operator|=
name|ActiveMQUuidGenerator
operator|.
name|generateSanitizedId
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
comment|// remove leading dash as we add that ourselves
if|if
condition|(
name|id
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|id
operator|=
name|id
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|SedaEndpoint
name|seda
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"seda:"
operator|+
name|verb
operator|+
literal|"-"
operator|+
name|id
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|seda
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

