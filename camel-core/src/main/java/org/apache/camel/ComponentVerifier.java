begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
comment|/**  * Defines the interface used to validate component/endpoint parameters.  */
end_comment

begin_interface
DECL|interface|ComponentVerifier
specifier|public
interface|interface
name|ComponentVerifier
block|{
DECL|field|CODE_EXCEPTION
name|String
name|CODE_EXCEPTION
init|=
literal|"exception"
decl_stmt|;
DECL|field|CODE_INTERNAL
name|String
name|CODE_INTERNAL
init|=
literal|"internal"
decl_stmt|;
DECL|field|CODE_MISSING_OPTION
name|String
name|CODE_MISSING_OPTION
init|=
literal|"missing-option"
decl_stmt|;
DECL|field|CODE_MISSING_OPTION_GROUP
name|String
name|CODE_MISSING_OPTION_GROUP
init|=
literal|"missing-option-group"
decl_stmt|;
DECL|field|CODE_UNKNOWN_OPTION
name|String
name|CODE_UNKNOWN_OPTION
init|=
literal|"unknown-option"
decl_stmt|;
DECL|field|CODE_ILLEGAL_OPTION
name|String
name|CODE_ILLEGAL_OPTION
init|=
literal|"illegal-option"
decl_stmt|;
DECL|field|CODE_ILLEGAL_OPTION_GROUP_COMBINATION
name|String
name|CODE_ILLEGAL_OPTION_GROUP_COMBINATION
init|=
literal|"illegal-option-group-combination"
decl_stmt|;
DECL|field|CODE_ILLEGAL_OPTION_VALUE
name|String
name|CODE_ILLEGAL_OPTION_VALUE
init|=
literal|"illegal-option-value"
decl_stmt|;
DECL|field|CODE_INCOMPLETE_OPTION_GROUP
name|String
name|CODE_INCOMPLETE_OPTION_GROUP
init|=
literal|"incomplete-option-group"
decl_stmt|;
DECL|field|CODE_UNSUPPORTED
name|String
name|CODE_UNSUPPORTED
init|=
literal|"unsupported"
decl_stmt|;
DECL|field|CODE_UNSUPPORTED_SCOPE
name|String
name|CODE_UNSUPPORTED_SCOPE
init|=
literal|"unsupported-scope"
decl_stmt|;
DECL|field|ERROR_TYPE_ATTRIBUTE
name|String
name|ERROR_TYPE_ATTRIBUTE
init|=
literal|"error.type"
decl_stmt|;
DECL|field|ERROR_TYPE_EXCEPTION
name|String
name|ERROR_TYPE_EXCEPTION
init|=
literal|"exception"
decl_stmt|;
DECL|field|ERROR_TYPE_HTTP
name|String
name|ERROR_TYPE_HTTP
init|=
literal|"http"
decl_stmt|;
DECL|field|HTTP_CODE
name|String
name|HTTP_CODE
init|=
literal|"http.code"
decl_stmt|;
DECL|field|HTTP_TEXT
name|String
name|HTTP_TEXT
init|=
literal|"http.text"
decl_stmt|;
DECL|field|HTTP_REDIRECT
name|String
name|HTTP_REDIRECT
init|=
literal|"http.redirect"
decl_stmt|;
DECL|field|HTTP_REDIRECT_LOCATION
name|String
name|HTTP_REDIRECT_LOCATION
init|=
literal|"http.redirect.location"
decl_stmt|;
DECL|field|EXCEPTION_CLASS
name|String
name|EXCEPTION_CLASS
init|=
literal|"exception.class"
decl_stmt|;
DECL|field|EXCEPTION_INSTANCE
name|String
name|EXCEPTION_INSTANCE
init|=
literal|"exception.instance"
decl_stmt|;
DECL|field|GROUP_NAME
name|String
name|GROUP_NAME
init|=
literal|"group.name"
decl_stmt|;
DECL|field|GROUP_OPTIONS
name|String
name|GROUP_OPTIONS
init|=
literal|"group.options"
decl_stmt|;
DECL|enum|Scope
enum|enum
name|Scope
block|{
DECL|enumConstant|NONE
name|NONE
block|,
DECL|enumConstant|PARAMETERS
name|PARAMETERS
block|,
DECL|enumConstant|CONNECTIVITY
name|CONNECTIVITY
block|;
DECL|field|VALUES
specifier|private
specifier|static
specifier|final
name|Scope
index|[]
name|VALUES
init|=
name|values
argument_list|()
decl_stmt|;
DECL|method|fromString (String scope)
specifier|public
specifier|static
name|Scope
name|fromString
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|VALUES
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|scope
argument_list|,
name|VALUES
index|[
name|i
index|]
operator|.
name|name
argument_list|()
argument_list|,
literal|true
argument_list|)
condition|)
block|{
return|return
name|VALUES
index|[
name|i
index|]
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown scope<"
operator|+
name|scope
operator|+
literal|">"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Represent an error      */
DECL|interface|Error
interface|interface
name|Error
extends|extends
name|Serializable
block|{
comment|/**          * @return the error code          */
DECL|method|getCode ()
name|String
name|getCode
parameter_list|()
function_decl|;
comment|/**          * @return the error description (if available)          */
DECL|method|getDescription ()
name|String
name|getDescription
parameter_list|()
function_decl|;
comment|/**          * @return the parameters in error          */
DECL|method|getParameters ()
name|Set
argument_list|<
name|String
argument_list|>
name|getParameters
parameter_list|()
function_decl|;
comment|/**          * @return a number of key/value pair with additional information related to the validation.          */
DECL|method|getAttributes ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getAttributes
parameter_list|()
function_decl|;
block|}
comment|/**      * Represent a validation Result.      */
DECL|interface|Result
interface|interface
name|Result
extends|extends
name|Serializable
block|{
DECL|enum|Status
enum|enum
name|Status
block|{
DECL|enumConstant|OK
name|OK
block|,
DECL|enumConstant|ERROR
name|ERROR
block|,
DECL|enumConstant|UNSUPPORTED
name|UNSUPPORTED
block|}
comment|/**          * @return the scope against which the parameters have been validated.          */
DECL|method|getScope ()
name|Scope
name|getScope
parameter_list|()
function_decl|;
comment|/**          * @return the status          */
DECL|method|getStatus ()
name|Status
name|getStatus
parameter_list|()
function_decl|;
comment|/**          * @return a list of errors          */
DECL|method|getErrors ()
name|List
argument_list|<
name|Error
argument_list|>
name|getErrors
parameter_list|()
function_decl|;
block|}
comment|/**      * Validate the given parameters against the provided scope.      *      *<p>      * The supported scopes are:      *<ul>      *<li>PARAMETERS: to validate that all the mandatory options are provided and syntactically correct.      *<li>CONNECTIVITY: to validate that the given options (i.e. credentials, addresses) are correct.      *</ul>      *      * @param scope the scope of the validation      * @param parameters the parameters to validate      * @return the validation result      */
DECL|method|verify (Scope scope, Map<String, Object> parameters)
name|Result
name|verify
parameter_list|(
name|Scope
name|scope
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

