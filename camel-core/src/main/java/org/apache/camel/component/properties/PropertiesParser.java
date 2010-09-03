begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A parser to parse a string which contains property placeholders  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|PropertiesParser
specifier|public
specifier|final
class|class
name|PropertiesParser
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PropertiesParser
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|PropertiesParser ()
specifier|private
name|PropertiesParser
parameter_list|()
block|{     }
comment|/**      * Parses the string and replaces the property placeholders with values from the given properties      *      * @param uri the uri      * @param properties the properties      * @param prefixToken the prefix token      * @param suffixToken the suffix token      * @return the uri with replaced placeholders      * @throws IllegalArgumentException if uri syntax is not valid or a property is not found      */
DECL|method|parseUri (String uri, Properties properties, String prefixToken, String suffixToken)
specifier|public
specifier|static
name|String
name|parseUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|String
name|prefixToken
parameter_list|,
name|String
name|suffixToken
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|String
name|answer
init|=
name|uri
decl_stmt|;
name|boolean
name|done
init|=
literal|false
decl_stmt|;
comment|// the placeholders can contain nested placeholders so we need to do recursive parsing
comment|// we must therefore also do circular reference check and must keep a list of visited keys
name|List
argument_list|<
name|String
argument_list|>
name|visited
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|replaced
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|=
name|doParseUri
argument_list|(
name|answer
argument_list|,
name|properties
argument_list|,
name|replaced
argument_list|,
name|prefixToken
argument_list|,
name|suffixToken
argument_list|)
expr_stmt|;
comment|// check the replaced with the visited to avoid circular reference
for|for
control|(
name|String
name|replace
range|:
name|replaced
control|)
block|{
if|if
condition|(
name|visited
operator|.
name|contains
argument_list|(
name|replace
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Circular reference detected with key ["
operator|+
name|replace
operator|+
literal|"] in uri "
operator|+
name|uri
argument_list|)
throw|;
block|}
block|}
comment|// okay all okay so add the replaced as visited
name|visited
operator|.
name|addAll
argument_list|(
name|replaced
argument_list|)
expr_stmt|;
comment|// are we done yet
name|done
operator|=
operator|!
name|answer
operator|.
name|contains
argument_list|(
name|prefixToken
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|doParseUri (String uri, Properties properties, List<String> replaced, String prefixToken, String suffixToken)
specifier|private
specifier|static
name|String
name|doParseUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|replaced
parameter_list|,
name|String
name|prefixToken
parameter_list|,
name|String
name|suffixToken
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|pivot
init|=
literal|0
decl_stmt|;
name|int
name|size
init|=
name|uri
operator|.
name|length
argument_list|()
decl_stmt|;
while|while
condition|(
name|pivot
operator|<
name|size
condition|)
block|{
name|int
name|idx
init|=
name|uri
operator|.
name|indexOf
argument_list|(
name|prefixToken
argument_list|,
name|pivot
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|createConstantPart
argument_list|(
name|uri
argument_list|,
name|pivot
argument_list|,
name|size
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
else|else
block|{
if|if
condition|(
name|pivot
operator|<
name|idx
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|createConstantPart
argument_list|(
name|uri
argument_list|,
name|pivot
argument_list|,
name|idx
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|pivot
operator|=
name|idx
operator|+
name|prefixToken
operator|.
name|length
argument_list|()
expr_stmt|;
name|int
name|endIdx
init|=
name|uri
operator|.
name|indexOf
argument_list|(
name|suffixToken
argument_list|,
name|pivot
argument_list|)
decl_stmt|;
if|if
condition|(
name|endIdx
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expecting "
operator|+
name|suffixToken
operator|+
literal|" but found end of string for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|String
name|key
init|=
name|uri
operator|.
name|substring
argument_list|(
name|pivot
argument_list|,
name|endIdx
argument_list|)
decl_stmt|;
name|String
name|part
init|=
name|createPlaceholderPart
argument_list|(
name|key
argument_list|,
name|properties
argument_list|,
name|replaced
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Property with key ["
operator|+
name|key
operator|+
literal|"] not found in properties for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|part
argument_list|)
expr_stmt|;
name|pivot
operator|=
name|endIdx
operator|+
name|suffixToken
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|createConstantPart (String uri, int start, int end)
specifier|private
specifier|static
name|String
name|createConstantPart
parameter_list|(
name|String
name|uri
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
return|return
name|uri
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
return|;
block|}
DECL|method|createPlaceholderPart (String placeholderPart, Properties properties, List<String> replaced)
specifier|private
specifier|static
name|String
name|createPlaceholderPart
parameter_list|(
name|String
name|placeholderPart
parameter_list|,
name|Properties
name|properties
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|replaced
parameter_list|)
block|{
name|String
name|propertyValue
decl_stmt|;
name|propertyValue
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|placeholderPart
argument_list|)
expr_stmt|;
if|if
condition|(
name|propertyValue
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Found a JVM system property: "
operator|+
name|placeholderPart
operator|+
literal|". Overriding property set via Property Location"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|replaced
operator|.
name|add
argument_list|(
name|placeholderPart
argument_list|)
expr_stmt|;
name|propertyValue
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
name|placeholderPart
argument_list|)
expr_stmt|;
block|}
return|return
name|propertyValue
return|;
block|}
block|}
end_class

end_unit

