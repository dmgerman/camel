begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|model
operator|.
name|FromDefinition
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
name|model
operator|.
name|ModelCamelContext
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|ProcessorDefinitionHelper
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
name|model
operator|.
name|RouteDefinition
import|;
end_import

begin_comment
comment|/**  * A number of helper methods  */
end_comment

begin_class
DECL|class|ModelCamelContextHelper
specifier|public
specifier|final
class|class
name|ModelCamelContextHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ModelCamelContextHelper ()
specifier|private
name|ModelCamelContextHelper
parameter_list|()
block|{     }
comment|/**      * Checks if any of the Camel routes is using an EIP with the given name      *      * @param camelContext  the Camel context      * @param name          the name of the EIP      * @return<tt>true</tt> if in use,<tt>false</tt> if not      */
DECL|method|isEipInUse (CamelContext camelContext, String name)
specifier|public
specifier|static
name|boolean
name|isEipInUse
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|RouteDefinition
name|route
range|:
name|camelContext
operator|.
name|adapt
argument_list|(
name|ModelCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getRouteDefinitions
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|route
operator|.
name|getInput
argument_list|()
operator|.
name|getShortName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|ProcessorDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessorDefinition
name|def
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|def
operator|.
name|getShortName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

