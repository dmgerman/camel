begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|salesforce
operator|.
name|api
operator|.
name|SalesforceException
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|GlobalObjects
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|SObject
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|SObjectDescription
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|SObjectField
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
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|JsonUtils
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
name|salesforce
operator|.
name|internal
operator|.
name|client
operator|.
name|RestClient
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
name|salesforce
operator|.
name|internal
operator|.
name|client
operator|.
name|SyncResponseCallback
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_class
DECL|class|ObjectDescriptions
specifier|final
class|class
name|ObjectDescriptions
block|{
DECL|field|client
specifier|private
specifier|final
name|RestClient
name|client
decl_stmt|;
DECL|field|descriptions
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|SObjectDescription
argument_list|>
name|descriptions
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|responseTimeout
specifier|private
specifier|final
name|long
name|responseTimeout
decl_stmt|;
DECL|method|ObjectDescriptions (final RestClient client, final long responseTimeout, final String[] includes, final String includePattern, final String[] excludes, final String excludePattern, final Log log)
name|ObjectDescriptions
parameter_list|(
specifier|final
name|RestClient
name|client
parameter_list|,
specifier|final
name|long
name|responseTimeout
parameter_list|,
specifier|final
name|String
index|[]
name|includes
parameter_list|,
specifier|final
name|String
name|includePattern
parameter_list|,
specifier|final
name|String
index|[]
name|excludes
parameter_list|,
specifier|final
name|String
name|excludePattern
parameter_list|,
specifier|final
name|Log
name|log
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|this
operator|.
name|responseTimeout
operator|=
name|responseTimeout
expr_stmt|;
name|fetchSpecifiedDescriptions
argument_list|(
name|includes
argument_list|,
name|includePattern
argument_list|,
name|excludes
argument_list|,
name|excludePattern
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
DECL|method|count ()
name|int
name|count
parameter_list|()
block|{
return|return
name|descriptions
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|descriptionOf (final String name)
name|SObjectDescription
name|descriptionOf
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|descriptions
operator|.
name|computeIfAbsent
argument_list|(
name|name
argument_list|,
name|this
operator|::
name|fetchDescriptionOf
argument_list|)
return|;
block|}
DECL|method|hasDescription (final String name)
name|boolean
name|hasDescription
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|descriptions
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|externalIdsOf (final String name)
name|List
argument_list|<
name|SObjectField
argument_list|>
name|externalIdsOf
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|descriptionOf
argument_list|(
name|name
argument_list|)
operator|.
name|getFields
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|SObjectField
operator|::
name|isExternalId
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|hasExternalIds (final String name)
name|boolean
name|hasExternalIds
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|descriptionOf
argument_list|(
name|name
argument_list|)
operator|.
name|getFields
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|SObjectField
operator|::
name|isExternalId
argument_list|)
return|;
block|}
DECL|method|fetched ()
name|Iterable
argument_list|<
name|SObjectDescription
argument_list|>
name|fetched
parameter_list|()
block|{
return|return
name|descriptions
operator|.
name|values
argument_list|()
return|;
block|}
DECL|method|fetchDescriptionOf (final String name)
specifier|private
name|SObjectDescription
name|fetchDescriptionOf
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
try|try
block|{
specifier|final
name|ObjectMapper
name|mapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
specifier|final
name|SyncResponseCallback
name|callback
init|=
operator|new
name|SyncResponseCallback
argument_list|()
decl_stmt|;
name|client
operator|.
name|getDescription
argument_list|(
name|name
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|callback
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|callback
operator|.
name|await
argument_list|(
name|responseTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Timeout waiting for getDescription for sObject "
operator|+
name|name
argument_list|)
throw|;
block|}
specifier|final
name|SalesforceException
name|ex
init|=
name|callback
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
specifier|final
name|SObjectDescription
name|description
init|=
name|mapper
operator|.
name|readValue
argument_list|(
name|callback
operator|.
name|getResponse
argument_list|()
argument_list|,
name|SObjectDescription
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// remove some of the unused used metadata
comment|// properties in order to minimize the code size
comment|// for CAMEL-11310
return|return
name|description
operator|.
name|prune
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Error getting SObject description for '"
operator|+
name|name
operator|+
literal|"': "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|fetchSpecifiedDescriptions (final String[] includes, final String includePattern, final String[] excludes, final String excludePattern, final Log log)
specifier|private
name|void
name|fetchSpecifiedDescriptions
parameter_list|(
specifier|final
name|String
index|[]
name|includes
parameter_list|,
specifier|final
name|String
name|includePattern
parameter_list|,
specifier|final
name|String
index|[]
name|excludes
parameter_list|,
specifier|final
name|String
name|excludePattern
parameter_list|,
specifier|final
name|Log
name|log
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
comment|// use Jackson json
specifier|final
name|ObjectMapper
name|mapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
comment|// call getGlobalObjects to get all SObjects
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|objectNames
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|SyncResponseCallback
name|callback
init|=
operator|new
name|SyncResponseCallback
argument_list|()
decl_stmt|;
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Getting Salesforce Objects..."
argument_list|)
expr_stmt|;
name|client
operator|.
name|getGlobalObjects
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|callback
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|callback
operator|.
name|await
argument_list|(
name|responseTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Timeout waiting for getGlobalObjects!"
argument_list|)
throw|;
block|}
specifier|final
name|SalesforceException
name|ex
init|=
name|callback
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
specifier|final
name|GlobalObjects
name|globalObjects
init|=
name|mapper
operator|.
name|readValue
argument_list|(
name|callback
operator|.
name|getResponse
argument_list|()
argument_list|,
name|GlobalObjects
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// create a list of object names
for|for
control|(
specifier|final
name|SObject
name|sObject
range|:
name|globalObjects
operator|.
name|getSobjects
argument_list|()
control|)
block|{
name|objectNames
operator|.
name|add
argument_list|(
name|sObject
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Error getting global Objects: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// check if we are generating POJOs for all objects or not
if|if
condition|(
name|includes
operator|!=
literal|null
operator|&&
name|includes
operator|.
name|length
operator|>
literal|0
operator|||
name|excludes
operator|!=
literal|null
operator|&&
name|excludes
operator|.
name|length
operator|>
literal|0
operator|||
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|includePattern
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|excludePattern
argument_list|)
condition|)
block|{
name|filterObjectNames
argument_list|(
name|objectNames
argument_list|,
name|includes
argument_list|,
name|includePattern
argument_list|,
name|excludes
argument_list|,
name|excludePattern
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Generating Java classes for all %s Objects, this may take a while..."
argument_list|,
name|objectNames
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Retrieving Object descriptions..."
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|String
name|name
range|:
name|objectNames
control|)
block|{
name|descriptionOf
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|filterObjectNames (final Set<String> objectNames, final String[] includes, final String includePattern, final String[] excludes, final String excludePattern, final Log log)
specifier|private
specifier|static
name|void
name|filterObjectNames
parameter_list|(
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|objectNames
parameter_list|,
specifier|final
name|String
index|[]
name|includes
parameter_list|,
specifier|final
name|String
name|includePattern
parameter_list|,
specifier|final
name|String
index|[]
name|excludes
parameter_list|,
specifier|final
name|String
name|excludePattern
parameter_list|,
specifier|final
name|Log
name|log
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Looking for matching Object names..."
argument_list|)
expr_stmt|;
comment|// create a list of accepted names
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|includedNames
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|includes
operator|!=
literal|null
operator|&&
name|includes
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|includes
control|)
block|{
name|name
operator|=
name|name
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Invalid empty name in includes"
argument_list|)
throw|;
block|}
name|includedNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|excludedNames
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|excludes
operator|!=
literal|null
operator|&&
name|excludes
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|excludes
control|)
block|{
name|name
operator|=
name|name
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"Invalid empty name in excludes"
argument_list|)
throw|;
block|}
name|excludedNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|// check whether a pattern is in effect
name|Pattern
name|incPattern
decl_stmt|;
if|if
condition|(
name|includePattern
operator|!=
literal|null
operator|&&
operator|!
name|includePattern
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|incPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|includePattern
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|includedNames
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// include everything by default if no include names are set
name|incPattern
operator|=
name|Defaults
operator|.
name|MATCH_EVERYTHING_PATTERN
expr_stmt|;
block|}
else|else
block|{
comment|// include nothing by default if include names are set
name|incPattern
operator|=
name|Defaults
operator|.
name|MATCH_NOTHING_PATTERN
expr_stmt|;
block|}
comment|// check whether a pattern is in effect
name|Pattern
name|excPattern
decl_stmt|;
if|if
condition|(
name|excludePattern
operator|!=
literal|null
operator|&&
operator|!
name|excludePattern
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|excPattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|excludePattern
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// exclude nothing by default
name|excPattern
operator|=
name|Defaults
operator|.
name|MATCH_NOTHING_PATTERN
expr_stmt|;
block|}
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|acceptedNames
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|name
range|:
name|objectNames
control|)
block|{
comment|// name is included, or matches include pattern
comment|// and is not excluded and does not match exclude pattern
if|if
condition|(
operator|(
name|includedNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
operator|||
name|incPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
operator|.
name|matches
argument_list|()
operator|)
operator|&&
operator|!
name|excludedNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
operator|&&
operator|!
name|excPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|acceptedNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|objectNames
operator|.
name|clear
argument_list|()
expr_stmt|;
name|objectNames
operator|.
name|addAll
argument_list|(
name|acceptedNames
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Found %s matching Objects"
argument_list|,
name|objectNames
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

