begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.verifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|ComponentVerifier
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
name|ComponentVerifier
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

begin_class
DECL|class|ResultErrorBuilder
specifier|public
specifier|final
class|class
name|ResultErrorBuilder
block|{
DECL|field|code
specifier|private
name|VerificationError
operator|.
name|Code
name|code
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|parameters
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|parameters
decl_stmt|;
DECL|field|attributes
specifier|private
name|Map
argument_list|<
name|VerificationError
operator|.
name|Attribute
argument_list|,
name|Object
argument_list|>
name|attributes
decl_stmt|;
DECL|method|ResultErrorBuilder ()
specifier|public
name|ResultErrorBuilder
parameter_list|()
block|{     }
comment|// **********************************
comment|// Accessors
comment|// **********************************
DECL|method|code (VerificationError.Code code)
specifier|public
name|ResultErrorBuilder
name|code
parameter_list|(
name|VerificationError
operator|.
name|Code
name|code
parameter_list|)
block|{
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|code (String code)
specifier|public
name|ResultErrorBuilder
name|code
parameter_list|(
name|String
name|code
parameter_list|)
block|{
name|code
argument_list|(
name|VerificationError
operator|.
name|asCode
argument_list|(
name|code
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|description (String description)
specifier|public
name|ResultErrorBuilder
name|description
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|parameterKey (String parameter)
specifier|public
name|ResultErrorBuilder
name|parameterKey
parameter_list|(
name|String
name|parameter
parameter_list|)
block|{
if|if
condition|(
name|parameter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|parameters
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|parameters
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|parameters
operator|.
name|add
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|parameterKeys (Collection<String> parameterList)
specifier|public
name|ResultErrorBuilder
name|parameterKeys
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|parameterList
parameter_list|)
block|{
if|if
condition|(
name|parameterList
operator|!=
literal|null
condition|)
block|{
name|parameterList
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|parameterKey
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|detail (String key, Object value)
specifier|public
name|ResultErrorBuilder
name|detail
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|detail
argument_list|(
name|VerificationError
operator|.
name|asAttribute
argument_list|(
name|key
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|detail (VerificationError.Attribute key, Object value)
specifier|public
name|ResultErrorBuilder
name|detail
parameter_list|(
name|VerificationError
operator|.
name|Attribute
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|attributes
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|attributes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|attributes
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|detail (String key, Supplier<Optional<T>> supplier)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ResultErrorBuilder
name|detail
parameter_list|(
name|String
name|key
parameter_list|,
name|Supplier
argument_list|<
name|Optional
argument_list|<
name|T
argument_list|>
argument_list|>
name|supplier
parameter_list|)
block|{
name|detail
argument_list|(
name|VerificationError
operator|.
name|asAttribute
argument_list|(
name|key
argument_list|)
argument_list|,
name|supplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|detail (VerificationError.Attribute key, Supplier<Optional<T>> supplier)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ResultErrorBuilder
name|detail
parameter_list|(
name|VerificationError
operator|.
name|Attribute
name|key
parameter_list|,
name|Supplier
argument_list|<
name|Optional
argument_list|<
name|T
argument_list|>
argument_list|>
name|supplier
parameter_list|)
block|{
name|supplier
operator|.
name|get
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|value
lambda|->
name|detail
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// **********************************
comment|// Build
comment|// **********************************
DECL|method|build ()
specifier|public
name|VerificationError
name|build
parameter_list|()
block|{
return|return
operator|new
name|DefaultResultVerificationError
argument_list|(
name|code
argument_list|,
name|description
argument_list|,
name|parameters
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|parameters
argument_list|)
else|:
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|attributes
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|attributes
argument_list|)
else|:
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
return|;
block|}
comment|// **********************************
comment|// Helpers
comment|// **********************************
DECL|method|withCode (VerificationError.Code code)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withCode
parameter_list|(
name|VerificationError
operator|.
name|Code
name|code
parameter_list|)
block|{
return|return
operator|new
name|ResultErrorBuilder
argument_list|()
operator|.
name|code
argument_list|(
name|code
argument_list|)
return|;
block|}
DECL|method|withCode (String code)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withCode
parameter_list|(
name|String
name|code
parameter_list|)
block|{
return|return
operator|new
name|ResultErrorBuilder
argument_list|()
operator|.
name|code
argument_list|(
name|code
argument_list|)
return|;
block|}
DECL|method|withHttpCode (int code)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withHttpCode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
return|return
name|withCode
argument_list|(
name|convertHttpCodeToErrorCode
argument_list|(
name|code
argument_list|)
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|HttpAttribute
operator|.
name|HTTP_CODE
argument_list|,
name|code
argument_list|)
return|;
block|}
DECL|method|withHttpCodeAndText (int code, String text)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withHttpCodeAndText
parameter_list|(
name|int
name|code
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
name|withCodeAndDescription
argument_list|(
name|convertHttpCodeToErrorCode
argument_list|(
name|code
argument_list|)
argument_list|,
name|text
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|HttpAttribute
operator|.
name|HTTP_CODE
argument_list|,
name|code
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|HttpAttribute
operator|.
name|HTTP_TEXT
argument_list|,
name|text
argument_list|)
return|;
block|}
DECL|method|convertHttpCodeToErrorCode (int code)
specifier|private
specifier|static
name|VerificationError
operator|.
name|StandardCode
name|convertHttpCodeToErrorCode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
return|return
name|code
operator|>=
literal|400
operator|&&
name|code
operator|<
literal|500
condition|?
name|VerificationError
operator|.
name|StandardCode
operator|.
name|AUTHENTICATION
else|:
name|VerificationError
operator|.
name|StandardCode
operator|.
name|GENERIC
return|;
block|}
DECL|method|withCodeAndDescription (VerificationError.Code code, String description)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withCodeAndDescription
parameter_list|(
name|VerificationError
operator|.
name|Code
name|code
parameter_list|,
name|String
name|description
parameter_list|)
block|{
return|return
operator|new
name|ResultErrorBuilder
argument_list|()
operator|.
name|code
argument_list|(
name|code
argument_list|)
operator|.
name|description
argument_list|(
name|description
argument_list|)
return|;
block|}
DECL|method|withUnsupportedScope (String scope)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withUnsupportedScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
return|return
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
name|UNSUPPORTED_SCOPE
argument_list|)
operator|.
name|description
argument_list|(
literal|"Unsupported scope: "
operator|+
name|scope
argument_list|)
return|;
block|}
DECL|method|withException (Exception exception)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withException
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
return|return
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
name|EXCEPTION
argument_list|)
operator|.
name|description
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_INSTANCE
argument_list|,
name|exception
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_CLASS
argument_list|,
name|exception
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|withMissingOption (String optionName)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withMissingOption
parameter_list|(
name|String
name|optionName
parameter_list|)
block|{
return|return
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
name|MISSING_PARAMETER
argument_list|)
operator|.
name|description
argument_list|(
name|optionName
operator|+
literal|" should be set"
argument_list|)
operator|.
name|parameterKey
argument_list|(
name|optionName
argument_list|)
return|;
block|}
DECL|method|withUnknownOption (String optionName)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withUnknownOption
parameter_list|(
name|String
name|optionName
parameter_list|)
block|{
return|return
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
name|UNKNOWN_PARAMETER
argument_list|)
operator|.
name|description
argument_list|(
literal|"Unknown option "
operator|+
name|optionName
argument_list|)
operator|.
name|parameterKey
argument_list|(
name|optionName
argument_list|)
return|;
block|}
DECL|method|withIllegalOption (String optionName)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withIllegalOption
parameter_list|(
name|String
name|optionName
parameter_list|)
block|{
return|return
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
name|ILLEGAL_PARAMETER
argument_list|)
operator|.
name|description
argument_list|(
literal|"Illegal option "
operator|+
name|optionName
argument_list|)
operator|.
name|parameterKey
argument_list|(
name|optionName
argument_list|)
return|;
block|}
DECL|method|withIllegalOption (String optionName, String optionValue)
specifier|public
specifier|static
name|ResultErrorBuilder
name|withIllegalOption
parameter_list|(
name|String
name|optionName
parameter_list|,
name|String
name|optionValue
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|optionValue
argument_list|)
condition|?
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
name|ILLEGAL_PARAMETER_VALUE
argument_list|)
operator|.
name|description
argument_list|(
name|optionName
operator|+
literal|" has wrong value ("
operator|+
name|optionValue
operator|+
literal|")"
argument_list|)
operator|.
name|parameterKey
argument_list|(
name|optionName
argument_list|)
else|:
name|withIllegalOption
argument_list|(
name|optionName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

