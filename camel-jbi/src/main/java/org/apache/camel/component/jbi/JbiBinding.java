begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|NormalizedMessage
import|;
end_import

begin_comment
comment|/**  * The binding of how Camel messages get mapped to JBI and back again  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JbiBinding
specifier|public
class|class
name|JbiBinding
block|{
comment|/**      * Extracts the body from the given normalized message      */
DECL|method|extractBodyFromJbi (JbiExchange exchange, NormalizedMessage normalizedMessage)
specifier|public
name|Object
name|extractBodyFromJbi
parameter_list|(
name|JbiExchange
name|exchange
parameter_list|,
name|NormalizedMessage
name|normalizedMessage
parameter_list|)
block|{
comment|// TODO we may wish to turn this into a POJO such as a JAXB/DOM
return|return
name|normalizedMessage
operator|.
name|getContent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

