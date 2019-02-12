begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.extension.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
operator|.
name|verifier
package|;
end_package

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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|extension
operator|.
name|ComponentVerifierExtension
operator|.
name|VerificationError
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Helper that validates component parameters.  */
end_comment

begin_class
DECL|class|ResultErrorHelper
specifier|public
specifier|final
class|class
name|ResultErrorHelper
block|{
DECL|method|ResultErrorHelper ()
specifier|private
name|ResultErrorHelper
parameter_list|()
block|{     }
comment|// **********************************
comment|// Helpers
comment|// **********************************
comment|/**      *      * @param parameterName the required option      * @param parameters the      * @return      */
DECL|method|requiresOption (String parameterName, Map<String, Object> parameters)
specifier|public
specifier|static
name|Optional
argument_list|<
name|VerificationError
argument_list|>
name|requiresOption
parameter_list|(
name|String
name|parameterName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
name|parameterName
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|ResultErrorBuilder
operator|.
name|withMissingOption
argument_list|(
name|parameterName
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
comment|/**      * Validates that the given parameters satisfy any grouped options      * ({@link OptionsGroup}). A parameter set is valid if it is      * present and required by least one of the groups.      *      *<p>As an example consider that there are two option groups that      * can be specified:      *<ul>      *<li>optionA: requires param1 and param2      *<li>optionB: requires param1 and param3      *</ul>      *      * Valid parameters are those that include param1 and either param2      * and/or param3.      *      *<p>Note the special syntax of {@link OptionsGroup#getOptions()}      * that can require an property ({@code "propertyName"}) or can      * forbid the presence of a property ({@code "!propertyName"}).      *      *<p>With that if in the example above if param2 is specified      * specifying param3 is not allowed, and vice versa option groups      * should be defined with options:      *<ul>      *<li>optionA: ["param1", "param2", "!param3"]      *<li>optionB: ["param1", "!param2", "param3"]      *</ul>      *      * @param parameters given parameters of a component      * @param groups groups of options      * @see OptionsGroup      */
DECL|method|requiresAny (Map<String, Object> parameters, OptionsGroup... groups)
specifier|public
specifier|static
name|List
argument_list|<
name|VerificationError
argument_list|>
name|requiresAny
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|OptionsGroup
modifier|...
name|groups
parameter_list|)
block|{
return|return
name|requiresAny
argument_list|(
name|parameters
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|groups
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Validates that the given parameters satisfy any grouped options      * ({@link OptionsGroup}). A parameter set is valid if it is      * present and required by least one of the groups.      *      * @param parameters given parameters of a component      * @param groups groups of options      * @see #requiresAny(Map, OptionsGroup...)      * @see OptionsGroup      */
DECL|method|requiresAny (Map<String, Object> parameters, Collection<OptionsGroup> groups)
specifier|public
specifier|static
name|List
argument_list|<
name|VerificationError
argument_list|>
name|requiresAny
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|Collection
argument_list|<
name|OptionsGroup
argument_list|>
name|groups
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|VerificationError
argument_list|>
name|verificationErrors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|parameters
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|OptionsGroup
name|group
range|:
name|groups
control|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|required
init|=
name|required
argument_list|(
name|group
operator|.
name|getOptions
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|excluded
init|=
name|excluded
argument_list|(
name|group
operator|.
name|getOptions
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|ResultErrorBuilder
name|builder
init|=
operator|new
name|ResultErrorBuilder
argument_list|()
operator|.
name|code
argument_list|(
name|VerificationError
operator|.
name|StandardCode
operator|.
name|ILLEGAL_PARAMETER_GROUP_COMBINATION
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|GroupAttribute
operator|.
name|GROUP_NAME
argument_list|,
name|group
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|GroupAttribute
operator|.
name|GROUP_OPTIONS
argument_list|,
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|parameters
argument_list|(
name|group
operator|.
name|getOptions
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|keys
operator|.
name|containsAll
argument_list|(
name|required
argument_list|)
condition|)
block|{
comment|// All the options of this group are found so we are good
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|shouldBeExcluded
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|keys
argument_list|)
decl_stmt|;
name|shouldBeExcluded
operator|.
name|retainAll
argument_list|(
name|excluded
argument_list|)
expr_stmt|;
if|if
condition|(
name|shouldBeExcluded
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// None of the excluded properties is present, also good
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|shouldBeExcluded
operator|.
name|forEach
argument_list|(
name|builder
operator|::
name|parameterKey
argument_list|)
expr_stmt|;
name|verificationErrors
operator|.
name|add
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|String
name|option
range|:
name|required
control|)
block|{
if|if
condition|(
operator|!
name|parameters
operator|.
name|containsKey
argument_list|(
name|option
argument_list|)
condition|)
block|{
name|builder
operator|.
name|parameterKey
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|option
range|:
name|excluded
control|)
block|{
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
name|option
argument_list|)
condition|)
block|{
name|builder
operator|.
name|parameterKey
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
block|}
name|verificationErrors
operator|.
name|add
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|verificationErrors
return|;
block|}
DECL|method|required (final Set<String> options)
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|required
parameter_list|(
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|options
parameter_list|)
block|{
return|return
name|options
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|o
lambda|->
operator|!
name|o
operator|.
name|startsWith
argument_list|(
literal|"!"
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|excluded (final Set<String> options)
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|excluded
parameter_list|(
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|options
parameter_list|)
block|{
return|return
name|options
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|o
lambda|->
name|o
operator|.
name|startsWith
argument_list|(
literal|"!"
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|o
lambda|->
name|o
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|parameters (final Set<String> options)
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|parameters
parameter_list|(
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|options
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|withoutExclusionMark
init|=
name|options
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|o
lambda|->
name|o
operator|.
name|replaceFirst
argument_list|(
literal|"!"
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|withoutExclusionMark
argument_list|)
return|;
block|}
block|}
end_class

end_unit
