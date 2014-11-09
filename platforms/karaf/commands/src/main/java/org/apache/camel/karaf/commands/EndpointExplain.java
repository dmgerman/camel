begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.karaf.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|karaf
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|Comparator
import|;
end_import

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
name|util
operator|.
name|EndpointHelper
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
name|JsonSchemaHelper
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
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Option
import|;
end_import

begin_comment
comment|/**  * Explain the Camel endpoints available in the Karaf instance.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"camel"
argument_list|,
name|name
operator|=
literal|"endpoint-explain"
argument_list|,
name|description
operator|=
literal|"Explain all Camel endpoints available in CamelContexts."
argument_list|)
DECL|class|EndpointExplain
specifier|public
class|class
name|EndpointExplain
extends|extends
name|CamelCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"name"
argument_list|,
name|description
operator|=
literal|"The Camel context name where to look for the endpoints"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|name
name|String
name|name
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--verbose"
argument_list|,
name|aliases
operator|=
literal|"-v"
argument_list|,
name|description
operator|=
literal|"Verbose output to explain all options"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|,
name|valueToShowInHelp
operator|=
literal|"false"
argument_list|)
DECL|field|verbose
name|boolean
name|verbose
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--filter"
argument_list|,
name|aliases
operator|=
literal|"-f"
argument_list|,
name|description
operator|=
literal|"To filter endpoints by pattern"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|filter
name|String
name|filter
decl_stmt|;
DECL|method|doExecute ()
specifier|protected
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
init|=
name|camelController
operator|.
name|getEndpoints
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoints
operator|==
literal|null
operator|||
name|endpoints
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// filter endpoints
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|Endpoint
argument_list|>
name|it
init|=
name|endpoints
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Endpoint
name|endpoint
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|filter
argument_list|)
condition|)
block|{
comment|// did not match
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|final
name|PrintStream
name|out
init|=
name|System
operator|.
name|out
decl_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
name|String
name|json
init|=
name|camelController
operator|.
name|explainEndpoint
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|verbose
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"Context:\t"
operator|+
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// sanitize and mask uri so we dont see passwords
name|String
name|uri
init|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|header
init|=
literal|"Uri:            "
operator|+
name|uri
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|header
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|header
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
comment|// use a basic json parser
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|options
init|=
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
name|json
argument_list|)
decl_stmt|;
comment|// lets sort the options by name
name|Collections
operator|.
name|sort
argument_list|(
name|options
argument_list|,
operator|new
name|Comparator
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|o1
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|option
range|:
name|options
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"Option:\t\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|option
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|type
init|=
name|option
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"Type:\t\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
name|String
name|javaType
init|=
name|option
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
decl_stmt|;
if|if
condition|(
name|javaType
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"Java Type:\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|javaType
argument_list|)
expr_stmt|;
block|}
name|String
name|value
init|=
name|option
operator|.
name|get
argument_list|(
literal|"value"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"Value:\t\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|String
name|defaultValue
init|=
name|option
operator|.
name|get
argument_list|(
literal|"defaultValue"
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"Default Value:\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|defaultValue
argument_list|)
expr_stmt|;
block|}
name|String
name|description
init|=
name|option
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
decl_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"Description:\t"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

