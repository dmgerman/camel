begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api
package|package
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
name|List
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
name|CamelException
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
name|RestError
import|;
end_import

begin_class
DECL|class|SalesforceException
specifier|public
class|class
name|SalesforceException
extends|extends
name|CamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|errors
specifier|private
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
decl_stmt|;
DECL|field|statusCode
specifier|private
specifier|final
name|int
name|statusCode
decl_stmt|;
DECL|method|SalesforceException (Throwable cause)
specifier|public
name|SalesforceException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceException (String message, Throwable cause)
specifier|public
name|SalesforceException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceException (String message, int statusCode)
specifier|public
name|SalesforceException
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|statusCode
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|statusCode
argument_list|,
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceException (String message, int statusCode, Throwable cause)
specifier|public
name|SalesforceException
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|statusCode
argument_list|,
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceException (List<RestError> errors, int statusCode)
specifier|public
name|SalesforceException
parameter_list|(
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
parameter_list|,
name|int
name|statusCode
parameter_list|)
block|{
name|this
argument_list|(
name|errors
argument_list|,
name|statusCode
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceException (List<RestError> errors, int statusCode, Throwable cause)
specifier|public
name|SalesforceException
parameter_list|(
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|this
argument_list|(
name|errors
argument_list|,
name|statusCode
argument_list|,
literal|null
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceException (List<RestError> errors, int statusCode, String message)
specifier|public
name|SalesforceException
parameter_list|(
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|errors
argument_list|,
name|statusCode
argument_list|,
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceException (List<RestError> errors, int statusCode, String message, Throwable cause)
specifier|public
name|SalesforceException
parameter_list|(
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
operator|==
literal|null
condition|?
name|toErrorMessage
argument_list|(
name|errors
argument_list|,
name|statusCode
argument_list|)
else|:
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|errors
operator|=
name|errors
expr_stmt|;
name|this
operator|.
name|statusCode
operator|=
name|statusCode
expr_stmt|;
block|}
DECL|method|getErrors ()
specifier|public
name|List
argument_list|<
name|RestError
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|errors
operator|==
literal|null
condition|?
name|Collections
operator|.
name|emptyList
argument_list|()
else|:
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|errors
argument_list|)
return|;
block|}
DECL|method|getStatusCode ()
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|statusCode
return|;
block|}
DECL|method|toErrorMessage (List<RestError> errors, int statusCode)
specifier|private
specifier|static
name|String
name|toErrorMessage
parameter_list|(
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
parameter_list|,
name|int
name|statusCode
parameter_list|)
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"{"
argument_list|)
decl_stmt|;
if|if
condition|(
name|errors
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"errors:["
argument_list|)
expr_stmt|;
for|for
control|(
name|RestError
name|error
range|:
name|errors
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|error
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"],"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"statusCode:"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|statusCode
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

