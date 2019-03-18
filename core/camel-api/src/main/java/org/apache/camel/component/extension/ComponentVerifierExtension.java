begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.extension
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
name|Component
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
name|ComponentVerifierExtensionHelper
operator|.
name|ErrorAttribute
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
name|ComponentVerifierExtensionHelper
operator|.
name|ErrorCode
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
name|ComponentVerifierExtensionHelper
operator|.
name|ExceptionErrorAttribute
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
name|ComponentVerifierExtensionHelper
operator|.
name|GroupErrorAttribute
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
name|ComponentVerifierExtensionHelper
operator|.
name|HttpErrorAttribute
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
name|ComponentVerifierExtensionHelper
operator|.
name|StandardErrorCode
import|;
end_import

begin_comment
comment|/**  * Defines the interface used for validating component/endpoint parameters. The central method of this  * interface is {@link #verify(ComponentVerifierExtension.Scope, Map)} which takes a scope and a set of parameters which should be verified.  *<p/>  * The return value is a {@link ComponentVerifierExtension.Result} of the verification  *  */
end_comment

begin_interface
DECL|interface|ComponentVerifierExtension
specifier|public
interface|interface
name|ComponentVerifierExtension
extends|extends
name|ComponentExtension
block|{
comment|/**      * Verify the given parameters against a provided scope.      *      *<p>      * The supported scopes are:      *<ul>      *<li><strong>{@link ComponentVerifierExtension.Scope#PARAMETERS}</strong>: to validate that all the mandatory options are provided and syntactically correct.</li>      *<li><strong>{@link ComponentVerifierExtension.Scope#CONNECTIVITY}</strong>: to validate that the given options (i.e. credentials, addresses) are correct. Verifying with this      *       scope typically implies reaching out to the backend via some sort of network connection.</li>      *</ul>      *      * @param scope the scope of the verification      * @param parameters the parameters to verify which are interpreted individually by each component verifier      * @return the verification result      */
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
comment|/**      * The result of a verification      */
DECL|interface|Result
interface|interface
name|Result
extends|extends
name|Serializable
block|{
comment|/**          * Status of the verification          */
DECL|enum|Status
enum|enum
name|Status
block|{
comment|/**              * Verification succeeded              */
DECL|enumConstant|OK
name|OK
block|,
comment|/**              * Error occurred during the verification              */
DECL|enumConstant|ERROR
name|ERROR
block|,
comment|/**              * Verification is not supported. This can depend on the given scope.              */
DECL|enumConstant|UNSUPPORTED
name|UNSUPPORTED
block|}
comment|/**          * Scope of the verification. This is the scope given to the call to {@link #verify(Scope, Map)}  and          * can be used for correlation.          *          * @return the scope against which the parameters have been validated.          */
DECL|method|getScope ()
name|Scope
name|getScope
parameter_list|()
function_decl|;
comment|/**          * Result of the validation as status. This should be the first datum to check after a verification          * happened.          *          * @return the status          */
DECL|method|getStatus ()
name|Status
name|getStatus
parameter_list|()
function_decl|;
comment|/**          * Collection of errors happened for the verification. This list is empty (but non null) if the verification          * succeeded.          *          * @return a list of errors. Can be empty when verification was successful          */
DECL|method|getErrors ()
name|List
argument_list|<
name|VerificationError
argument_list|>
name|getErrors
parameter_list|()
function_decl|;
block|}
comment|/**      * The scope defines how the parameters should be verified.      */
DECL|enum|Scope
enum|enum
name|Scope
block|{
comment|/**          * Only validate the parameters for their<em>syntactic</em> soundness. Verifications in this scope should          * be as fast as possible          */
DECL|enumConstant|PARAMETERS
name|PARAMETERS
block|,
comment|/**          * Reach out to the backend and verify that a connection can be established. This means, if the verification          * in this scope succeeds, then it can safely be assumed that the component can be used.          */
DECL|enumConstant|CONNECTIVITY
name|CONNECTIVITY
block|;
comment|/**          * Get an instance of this scope from a string representation          *          * @param scope the scope as string, which can be in any case          * @return the scope enum represented by this string          */
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
return|return
name|Scope
operator|.
name|valueOf
argument_list|(
name|scope
operator|!=
literal|null
condition|?
name|scope
operator|.
name|toUpperCase
argument_list|()
else|:
literal|null
argument_list|)
return|;
block|}
block|}
comment|// =============================================================================================
comment|/**      * This interface represents a detailed error in case when the verification fails.      */
DECL|interface|VerificationError
interface|interface
name|VerificationError
extends|extends
name|Serializable
block|{
comment|/**          * The overall error code, which can be either a {@link StandardCode} or a custom code. It is          * recommended to stick to the predefined standard codes          *          * @return the general error code.          */
DECL|method|getCode ()
name|Code
name|getCode
parameter_list|()
function_decl|;
comment|/**          * A human readable description of the error in plain english          *          * @return the error description (if available)          */
DECL|method|getDescription ()
name|String
name|getDescription
parameter_list|()
function_decl|;
comment|/**          * A set of input parameter names which fails the verification. These are keys to the parameter provided          * to {@link #verify(ComponentVerifierExtension.Scope, Map)}.          *          * @return the parameter names which are malformed and caused the failure of the validation          */
DECL|method|getParameterKeys ()
name|Set
argument_list|<
name|String
argument_list|>
name|getParameterKeys
parameter_list|()
function_decl|;
comment|/**          * Details about the failed verification. The keys can be either predefined values          * ({@link ExceptionAttribute}, {@link HttpAttribute}, {@link GroupAttribute}) or it can be free-form          * custom keys specific to a component. The standard attributes are defined as enums in all uppercase (with          * underscore as separator), custom attributes are supposed to be in all lower case (also with underscores          * as separators)          *          * @return a number of key/value pair with additional information related to the verification.          */
DECL|method|getDetails ()
name|Map
argument_list|<
name|Attribute
argument_list|,
name|Object
argument_list|>
name|getDetails
parameter_list|()
function_decl|;
comment|/**          * Get a single detail for a given attribute          *          * @param attribute the attribute to lookup          * @return the detail value or null if no such attribute exists          */
DECL|method|getDetail (Attribute attribute)
specifier|default
name|Object
name|getDetail
parameter_list|(
name|Attribute
name|attribute
parameter_list|)
block|{
name|Map
argument_list|<
name|Attribute
argument_list|,
name|Object
argument_list|>
name|details
init|=
name|getDetails
argument_list|()
decl_stmt|;
if|if
condition|(
name|details
operator|!=
literal|null
condition|)
block|{
return|return
name|details
operator|.
name|get
argument_list|(
name|attribute
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**          * Get a single detail for a given attribute          *          * @param attribute the attribute to lookup          * @return the detail value or null if no such attribute exists          */
DECL|method|getDetail (String attribute)
specifier|default
name|Object
name|getDetail
parameter_list|(
name|String
name|attribute
parameter_list|)
block|{
return|return
name|getDetail
argument_list|(
name|asAttribute
argument_list|(
name|attribute
argument_list|)
argument_list|)
return|;
block|}
comment|/**          * Convert a string to an {@link Code}          *          * @param code the code to convert. It should be in all lower case (with          *             underscore as a separator) to avoid overlap with {@link StandardCode}          * @return error code          */
DECL|method|asCode (String code)
specifier|static
name|Code
name|asCode
parameter_list|(
name|String
name|code
parameter_list|)
block|{
return|return
operator|new
name|ErrorCode
argument_list|(
name|code
argument_list|)
return|;
block|}
comment|/**          * Convert a string to an {@link Attribute}          *          * @param attribute the string representation of an attribute to convert. It should be in all lower case (with          *                  underscore as a separator) to avoid overlap with standard attributes like {@link ExceptionAttribute},          *                  {@link HttpAttribute} or {@link GroupAttribute}          * @return generated attribute          */
DECL|method|asAttribute (String attribute)
specifier|static
name|Attribute
name|asAttribute
parameter_list|(
name|String
name|attribute
parameter_list|)
block|{
return|return
operator|new
name|ErrorAttribute
argument_list|(
name|attribute
argument_list|)
return|;
block|}
comment|/**          * Interface defining an error code. This is implemented by the {@link StandardCode} but also          * own code can be generated by implementing this interface. This is best done via {@link #asCode(String)}          * If possible, the standard codes should be reused          */
DECL|interface|Code
interface|interface
name|Code
extends|extends
name|Serializable
block|{
comment|/**              * Name of the code. All uppercase for standard codes, all lower case for custom codes.              * Separator between two words is an underscore.              *              * @return code name              */
DECL|method|name ()
name|String
name|name
parameter_list|()
function_decl|;
comment|/**              * Bean style accessor to name.              * This is required for framework like Jackson using bean convention for object serialization.              *              * @return code name              */
DECL|method|getName ()
specifier|default
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
argument_list|()
return|;
block|}
block|}
comment|/**          * Standard set of error codes          */
DECL|interface|StandardCode
interface|interface
name|StandardCode
extends|extends
name|Code
block|{
comment|/**              * Authentication failed              */
DECL|field|AUTHENTICATION
name|StandardCode
name|AUTHENTICATION
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"AUTHENTICATION"
argument_list|)
decl_stmt|;
comment|/**              * An exception occurred              */
DECL|field|EXCEPTION
name|StandardCode
name|EXCEPTION
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"EXCEPTION"
argument_list|)
decl_stmt|;
comment|/**              * Internal error while performing the verification              */
DECL|field|INTERNAL
name|StandardCode
name|INTERNAL
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"INTERNAL"
argument_list|)
decl_stmt|;
comment|/**              * A mandatory parameter is missing              */
DECL|field|MISSING_PARAMETER
name|StandardCode
name|MISSING_PARAMETER
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"MISSING_PARAMETER"
argument_list|)
decl_stmt|;
comment|/**              * A given parameter is not known to the component              */
DECL|field|UNKNOWN_PARAMETER
name|StandardCode
name|UNKNOWN_PARAMETER
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"UNKNOWN_PARAMETER"
argument_list|)
decl_stmt|;
comment|/**              * A given parameter is illegal              */
DECL|field|ILLEGAL_PARAMETER
name|StandardCode
name|ILLEGAL_PARAMETER
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"ILLEGAL_PARAMETER"
argument_list|)
decl_stmt|;
comment|/**              * A combination of parameters is illegal. See {@link VerificationError#getParameterKeys()} for the set              * of affected parameters              */
DECL|field|ILLEGAL_PARAMETER_GROUP_COMBINATION
name|StandardCode
name|ILLEGAL_PARAMETER_GROUP_COMBINATION
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"ILLEGAL_PARAMETER_GROUP_COMBINATION"
argument_list|)
decl_stmt|;
comment|/**              * A parameter<em>value</em> is not valid              */
DECL|field|ILLEGAL_PARAMETER_VALUE
name|StandardCode
name|ILLEGAL_PARAMETER_VALUE
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"ILLEGAL_PARAMETER_VALUE"
argument_list|)
decl_stmt|;
comment|/**              * A group of parameters is not complete in order to be valid              */
DECL|field|INCOMPLETE_PARAMETER_GROUP
name|StandardCode
name|INCOMPLETE_PARAMETER_GROUP
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"INCOMPLETE_PARAMETER_GROUP"
argument_list|)
decl_stmt|;
comment|/**              * The verification is not supported              */
DECL|field|UNSUPPORTED
name|StandardCode
name|UNSUPPORTED
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"UNSUPPORTED"
argument_list|)
decl_stmt|;
comment|/**              * The requested {@link Scope} is not supported              */
DECL|field|UNSUPPORTED_SCOPE
name|StandardCode
name|UNSUPPORTED_SCOPE
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"UNSUPPORTED_SCOPE"
argument_list|)
decl_stmt|;
comment|/**              * The requested {@link Component} is not supported              */
DECL|field|UNSUPPORTED_COMPONENT
name|StandardCode
name|UNSUPPORTED_COMPONENT
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"UNSUPPORTED_COMPONENT"
argument_list|)
decl_stmt|;
comment|/**              * Generic error which is explained in more details with {@link VerificationError#getDetails()}              */
DECL|field|GENERIC
name|StandardCode
name|GENERIC
init|=
operator|new
name|StandardErrorCode
argument_list|(
literal|"GENERIC"
argument_list|)
decl_stmt|;
block|}
comment|/**          * Interface defining an attribute which is a key for the detailed error messages. This is implemented by several          * standard enums like {@link ExceptionAttribute}, {@link HttpAttribute} or {@link GroupAttribute} but can also          * implemented for component specific details. This is best done via {@link #asAttribute(String)}          * or using one of the other builder method in this error builder (like          * {@link org.apache.camel.component.extension.verifier.ResultErrorBuilder#detail(String, Object)}          *<p>          * With respecting to name, the same rules as for {@link Code} apply: Standard attributes are all upper case with _          * as separators, whereas custom attributes are lower case with underscore separators.          */
DECL|interface|Attribute
interface|interface
name|Attribute
extends|extends
name|Serializable
block|{
comment|/**              * Name of the attribute. All uppercase for standard attributes and all lower case for custom attributes.              * Separator between words is an underscore.              *              * @return attribute name              */
DECL|method|name ()
name|String
name|name
parameter_list|()
function_decl|;
comment|/**              * Bean style accessor to name;              * This is required for framework like Jackson using bean convention for object serialization.              *              * @return attribute name              */
DECL|method|getName ()
specifier|default
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
argument_list|()
return|;
block|}
block|}
comment|/**          * Attributes for details about an exception that was raised          */
DECL|interface|ExceptionAttribute
interface|interface
name|ExceptionAttribute
extends|extends
name|Attribute
block|{
comment|/**              * The exception object that has been thrown. Note that this can be a complex              * object and can cause large content when e.g. serialized as JSON              */
DECL|field|EXCEPTION_INSTANCE
name|ExceptionAttribute
name|EXCEPTION_INSTANCE
init|=
operator|new
name|ExceptionErrorAttribute
argument_list|(
literal|"EXCEPTION_INSTANCE"
argument_list|)
decl_stmt|;
comment|/**              * The exception class              */
DECL|field|EXCEPTION_CLASS
name|ExceptionAttribute
name|EXCEPTION_CLASS
init|=
operator|new
name|ExceptionErrorAttribute
argument_list|(
literal|"EXCEPTION_CLASS"
argument_list|)
decl_stmt|;
block|}
comment|/**          * HTTP related error details          */
DECL|interface|HttpAttribute
interface|interface
name|HttpAttribute
extends|extends
name|Attribute
block|{
comment|/**              * The erroneous HTTP code that occurred              */
DECL|field|HTTP_CODE
name|HttpAttribute
name|HTTP_CODE
init|=
operator|new
name|HttpErrorAttribute
argument_list|(
literal|"HTTP_CODE"
argument_list|)
decl_stmt|;
comment|/**              * HTTP response's body              */
DECL|field|HTTP_TEXT
name|HttpAttribute
name|HTTP_TEXT
init|=
operator|new
name|HttpErrorAttribute
argument_list|(
literal|"HTTP_TEXT"
argument_list|)
decl_stmt|;
comment|/**              * If given as details, specifies that a redirect happened and the              * content of this detail is the redirect URL              */
DECL|field|HTTP_REDIRECT
name|HttpAttribute
name|HTTP_REDIRECT
init|=
operator|new
name|HttpErrorAttribute
argument_list|(
literal|"HTTP_REDIRECT"
argument_list|)
decl_stmt|;
block|}
comment|/**          * Group related details          */
DECL|interface|GroupAttribute
interface|interface
name|GroupAttribute
extends|extends
name|Attribute
block|{
comment|/**              * Group name              */
DECL|field|GROUP_NAME
name|GroupAttribute
name|GROUP_NAME
init|=
operator|new
name|GroupErrorAttribute
argument_list|(
literal|"GROUP_NAME"
argument_list|)
decl_stmt|;
comment|/**              * Options for the group              */
DECL|field|GROUP_OPTIONS
name|GroupAttribute
name|GROUP_OPTIONS
init|=
operator|new
name|GroupErrorAttribute
argument_list|(
literal|"GROUP_OPTIONS"
argument_list|)
decl_stmt|;
block|}
block|}
block|}
end_interface

end_unit

