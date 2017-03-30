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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ComponentVerifier
operator|.
name|VerificationError
operator|.
name|Attribute
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
operator|.
name|Code
import|;
end_import

begin_comment
comment|/**  * Package visible helper class holding implementation classes for  * constant like error code and attributes in {@link ComponentVerifier.VerificationError}  */
end_comment

begin_class
DECL|class|ComponentVerifierHelper
class|class
name|ComponentVerifierHelper
block|{
comment|/**      * Custom class for error codes      */
DECL|class|ErrorCode
specifier|static
class|class
name|ErrorCode
implements|implements
name|Code
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|ErrorCode (String name)
name|ErrorCode
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Name of an error code must not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|name ()
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Code
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Code
name|errorCode
init|=
operator|(
name|Code
operator|)
name|o
decl_stmt|;
return|return
name|name
operator|.
name|equals
argument_list|(
name|errorCode
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|name
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|name
argument_list|()
return|;
block|}
block|}
DECL|class|ErrorAttribute
specifier|static
class|class
name|ErrorAttribute
implements|implements
name|Attribute
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|ErrorAttribute (String name)
name|ErrorAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Name of an error attribute must not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|name ()
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Attribute
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Attribute
name|that
init|=
operator|(
name|Attribute
operator|)
name|o
decl_stmt|;
return|return
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|name
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|name
argument_list|()
return|;
block|}
block|}
comment|// ===========================================================================================================
comment|// Helper classes for implementing the constants in ComponentVerifier:
DECL|class|StandardErrorCode
specifier|static
class|class
name|StandardErrorCode
extends|extends
name|ErrorCode
implements|implements
name|ComponentVerifier
operator|.
name|VerificationError
operator|.
name|StandardCode
block|{
DECL|method|StandardErrorCode (String name)
name|StandardErrorCode
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ExceptionErrorAttribute
specifier|static
class|class
name|ExceptionErrorAttribute
extends|extends
name|ErrorAttribute
implements|implements
name|ComponentVerifier
operator|.
name|VerificationError
operator|.
name|ExceptionAttribute
block|{
DECL|method|ExceptionErrorAttribute (String name)
name|ExceptionErrorAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|HttpErrorAttribute
specifier|static
class|class
name|HttpErrorAttribute
extends|extends
name|ErrorAttribute
implements|implements
name|ComponentVerifier
operator|.
name|VerificationError
operator|.
name|HttpAttribute
block|{
DECL|method|HttpErrorAttribute (String name)
name|HttpErrorAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|GroupErrorAttribute
specifier|static
class|class
name|GroupErrorAttribute
extends|extends
name|ErrorAttribute
implements|implements
name|ComponentVerifier
operator|.
name|VerificationError
operator|.
name|GroupAttribute
block|{
DECL|method|GroupErrorAttribute (String name)
name|GroupErrorAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

