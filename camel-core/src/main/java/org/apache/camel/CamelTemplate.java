begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|impl
operator|.
name|DefaultProducerTemplate
import|;
end_import

begin_comment
comment|/**  * A client helper object (named like Spring's TransactionTemplate& JmsTemplate  * et al) for working with Camel and sending {@link Message} instances in an  * {@link Exchange} to an {@link Endpoint}.  *  * @version $Revision$  * @deprecated use {@link ProducerTemplate} instead, can be created using {@link org.apache.camel.CamelContext#createProducerTemplate()}. Will be removed in Camel 2.0  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|CamelTemplate
specifier|public
class|class
name|CamelTemplate
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|DefaultProducerTemplate
argument_list|<
name|E
argument_list|>
block|{
DECL|method|CamelTemplate (CamelContext context)
specifier|public
name|CamelTemplate
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
DECL|method|CamelTemplate (CamelContext context, Endpoint defaultEndpoint)
specifier|public
name|CamelTemplate
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Endpoint
name|defaultEndpoint
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|defaultEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

