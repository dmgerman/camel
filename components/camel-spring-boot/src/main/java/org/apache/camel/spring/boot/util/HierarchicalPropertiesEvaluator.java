begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|bind
operator|.
name|RelaxedPropertyResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|env
operator|.
name|Environment
import|;
end_import

begin_class
DECL|class|HierarchicalPropertiesEvaluator
specifier|public
specifier|final
class|class
name|HierarchicalPropertiesEvaluator
block|{
DECL|method|HierarchicalPropertiesEvaluator ()
specifier|private
name|HierarchicalPropertiesEvaluator
parameter_list|()
block|{     }
comment|/**      * Determine the value of the "enabled" flag for a hierarchy of properties.      *      * @param environment the environment      * @param prefixes an ordered list of prefixed (less restrictive to more restrictive)      * @return the value of the key `enabled` for most restrictive prefix      */
DECL|method|evaluate (Environment environment, String... prefixes)
specifier|public
specifier|static
name|boolean
name|evaluate
parameter_list|(
name|Environment
name|environment
parameter_list|,
name|String
modifier|...
name|prefixes
parameter_list|)
block|{
name|boolean
name|answer
init|=
literal|true
decl_stmt|;
comment|// Loop over all the prefixes to find out the value of the key `enabled`
comment|// for the most restrictive prefix.
for|for
control|(
name|String
name|prefix
range|:
name|prefixes
control|)
block|{
comment|// evaluate the value of the current prefix using the parent one
comment|// as default value so if the enabled property is not set, the parent
comment|// one is used.
name|answer
operator|=
name|isEnabled
argument_list|(
name|environment
argument_list|,
name|prefix
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|isEnabled (Environment environment, String prefix, boolean defaultValue)
specifier|private
specifier|static
name|boolean
name|isEnabled
parameter_list|(
name|Environment
name|environment
parameter_list|,
name|String
name|prefix
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
name|RelaxedPropertyResolver
name|resolver
init|=
operator|new
name|RelaxedPropertyResolver
argument_list|(
name|environment
argument_list|,
name|prefix
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|?
name|prefix
else|:
name|prefix
operator|+
literal|"."
argument_list|)
decl_stmt|;
return|return
name|resolver
operator|.
name|getProperty
argument_list|(
literal|"enabled"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
block|}
end_class

end_unit

