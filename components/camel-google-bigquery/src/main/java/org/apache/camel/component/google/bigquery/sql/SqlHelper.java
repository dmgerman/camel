begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.bigquery.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|bigquery
operator|.
name|sql
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
name|*
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
name|support
operator|.
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|Set
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
name|Matcher
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

begin_class
DECL|class|SqlHelper
specifier|public
specifier|final
class|class
name|SqlHelper
block|{
DECL|field|pattern
specifier|private
specifier|static
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\$\\{(\\w+)}"
argument_list|)
decl_stmt|;
DECL|field|parameterPattern
specifier|private
specifier|static
name|Pattern
name|parameterPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"@(\\w+)"
argument_list|)
decl_stmt|;
DECL|method|SqlHelper ()
specifier|private
name|SqlHelper
parameter_list|()
block|{     }
comment|/**      * Resolve the query by loading the query from the classpath or file resource if needed.      */
DECL|method|resolveQuery (CamelContext camelContext, String query, String placeholder)
specifier|public
specifier|static
name|String
name|resolveQuery
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|query
parameter_list|,
name|String
name|placeholder
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
throws|,
name|IOException
block|{
name|String
name|answer
init|=
name|query
decl_stmt|;
if|if
condition|(
name|ResourceHelper
operator|.
name|hasScheme
argument_list|(
name|query
argument_list|)
condition|)
block|{
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|answer
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
if|if
condition|(
name|placeholder
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|answer
operator|.
name|replaceAll
argument_list|(
name|placeholder
argument_list|,
literal|"@"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Replaces pattern in query in form of "${param}" with values from message header      * Raises an error if param value not found in headers      * @param exchange      * @return Translated query text      */
DECL|method|translateQuery (String query, Exchange exchange)
specifier|public
specifier|static
name|String
name|translateQuery
parameter_list|(
name|String
name|query
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|StringBuffer
name|stringBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|paramKey
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|paramKey
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|paramKey
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"SQL pattern with name '"
operator|+
name|paramKey
operator|+
literal|"' not found in the message headers"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|String
name|replacement
init|=
name|Matcher
operator|.
name|quoteReplacement
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|matcher
operator|.
name|appendReplacement
argument_list|(
name|stringBuffer
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
block|}
name|matcher
operator|.
name|appendTail
argument_list|(
name|stringBuffer
argument_list|)
expr_stmt|;
return|return
name|stringBuffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Extracts list of parameters in form "@name" from query text      * @param query      * @return list of parameter names      */
DECL|method|extractParameterNames (String query)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|extractParameterNames
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|parameterPattern
operator|.
name|matcher
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|paramName
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|paramName
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

