begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbpm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbpm
package|;
end_package

begin_comment
comment|/**  * Indicates that class implementing this interface should receive (at some point)  * JBPMConsumer instance that is required to operate.  */
end_comment

begin_interface
DECL|interface|JBPMCamelConsumerAware
specifier|public
interface|interface
name|JBPMCamelConsumerAware
block|{
DECL|method|addConsumer (JBPMConsumer consumer)
name|void
name|addConsumer
parameter_list|(
name|JBPMConsumer
name|consumer
parameter_list|)
function_decl|;
DECL|method|removeConsumer (JBPMConsumer consumer)
name|void
name|removeConsumer
parameter_list|(
name|JBPMConsumer
name|consumer
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

